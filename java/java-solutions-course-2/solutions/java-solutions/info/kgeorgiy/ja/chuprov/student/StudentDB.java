package info.kgeorgiy.ja.chuprov.student;

import info.kgeorgiy.java.advanced.student.*;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StudentDB implements GroupQuery {

    private static final String EMPTY_STRING = "";

    private static final Function<Student,String> FULL_NAME_FOO =
            student -> student.firstName() + " " + student.lastName();

    private static final Comparator<Student> STUDENTS_NATURAL_ORDER =
            Comparator.naturalOrder();

    private static final Comparator<Student> STUDENTS_NAME_ORDER =
            Comparator.comparing(Student::firstName)
                    .thenComparing(Student::lastName)
                    .thenComparing(STUDENTS_NATURAL_ORDER);

    private <T> Stream<T> getMappedStream(List<Student> students, Function<Student, T> fieldMapper){
        return students.stream()
                .map(fieldMapper);
    }
    private <T> List<T> getMappedList(List<Student> students, Function<Student, T> fieldMapper){
        return getMappedStream(students, fieldMapper)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getFirstNames(List<Student> students) {
        return getMappedList(students, Student::firstName);
    }

    @Override
    public List<String> getLastNames(List<Student> students) {
        return getMappedList(students, Student::lastName);
    }

    @Override
    public List<GroupName> getGroupNames(List<Student> students) {
        return  getMappedList(students, Student::groupName);
    }

    @Override
    public List<String> getFullNames(List<Student> students) {
        return getMappedList(students, FULL_NAME_FOO);
    }

    @Override
    public Set<String> getDistinctFirstNames(List<Student> students) {
        return getMappedStream(students, Student::firstName)
                .collect(Collectors.toSet());
    }

    @Override
    public String getMaxStudentFirstName(List<Student> students) {
        return students.stream()
                .reduce(BinaryOperator.maxBy(Student::compareTo))
                .map(Student::firstName)
                .orElse(EMPTY_STRING);
    }

    private List<Student> sortStudentsByOrder(Collection<Student> students, Comparator<Student> order) {
        return students.stream()
                .sorted(order)
                .collect(Collectors.toList());
    }

    @Override
    public List<Student> sortStudentsById(Collection<Student> students) {
        return sortStudentsByOrder(students, STUDENTS_NATURAL_ORDER);
    }

    @Override
    public List<Student> sortStudentsByName(Collection<Student> students) {
        return sortStudentsByOrder(students, STUDENTS_NAME_ORDER);
    }

    private <T> Stream<Student> getStreamFilteredByValue(Collection<Student> students,
                                                         Function<Student, ? extends T> mapper, T value) {
        return students.stream()
                .filter(student -> mapper.apply(student).equals(value));
    }

    private <T> List<Student> getStudentsFilteredAndSortedByName(
            Collection<Student> students, Function<Student, ? extends T> mapper, T value) {
        return getStreamFilteredByValue(students, mapper, value).
                sorted(STUDENTS_NAME_ORDER)
                .collect(Collectors.toList());
    }

    @Override
    public List<Student> findStudentsByFirstName(Collection<Student> students, String name) {
        return getStudentsFilteredAndSortedByName(students, Student::firstName, name);
    }

    @Override
    public List<Student> findStudentsByLastName(Collection<Student> students, String name) {
        return getStudentsFilteredAndSortedByName(students, Student::lastName, name);
    }

    @Override
    public List<Student> findStudentsByGroup(Collection<Student> students, GroupName group) {
        return getStudentsFilteredAndSortedByName(students, Student::groupName, group);
    }

    @Override
    public Map<String, String> findStudentNamesByGroup(Collection<Student> students, GroupName group) {
        return getStreamFilteredByValue(students, Student::groupName, group)
                .collect(Collectors.toMap(
                        Student::lastName,
                        Student::firstName,
                        BinaryOperator.minBy(String::compareTo)));
    }

    private List<Group> getGroupsByOrder(Collection<Student> students, Comparator<Student> order) {
        return students.stream()
                .collect(Collectors.groupingBy(Student::groupName, Collectors.toList()))
                .entrySet().stream()
                .map(entry -> new Group(entry.getKey(),
                        entry.getValue().stream().sorted(order).toList()))
                .sorted(Comparator.comparing(Group::name))
                .collect(Collectors.toList());
    }

    @Override
    public List<Group> getGroupsByName(Collection<Student> students) {
        return getGroupsByOrder(students, STUDENTS_NAME_ORDER);
    }

    @Override
    public List<Group> getGroupsById(Collection<Student> students) {
        return getGroupsByOrder(students, STUDENTS_NATURAL_ORDER);
    }

    private <T> GroupName getMaxByOrderInCollectedGroups(Collection<Student> students,
                                                        Collector<Student, ?, T> downstream,
                                                        Comparator<Map.Entry<GroupName, T>> orderForMapping) {
        return students.stream()
                .collect(Collectors.groupingBy(Student::groupName, downstream))
                .entrySet().stream()
                .max(orderForMapping)
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    @Override
    public GroupName getLargestGroup(Collection<Student> students) {
        return getMaxByOrderInCollectedGroups(students,
                Collectors.counting(),
                Map.Entry.<GroupName, Long>comparingByValue()
                        .thenComparing(Map.Entry::getKey));
    }

    @Override
    public GroupName getLargestGroupFirstName(Collection<Student> students) {
        return getMaxByOrderInCollectedGroups(students,
                Collectors.collectingAndThen(
                        Collectors.mapping(Student::firstName, Collectors.toSet()),
                        Set::size),
                Map.Entry.<GroupName, Integer>comparingByValue()
                        .thenComparing(Map.Entry.<GroupName, Integer>comparingByKey().reversed()));
    }
}
