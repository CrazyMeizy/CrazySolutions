package info.kgeorgiy.java.advanced.implementor;

import info.kgeorgiy.java.advanced.implementor.basic.classes.AbstractClassWithInterface;
import info.kgeorgiy.java.advanced.implementor.basic.classes.standard.*;
import info.kgeorgiy.java.advanced.implementor.full.classes.ClassWithPackagePrivateConstructor;
import info.kgeorgiy.java.advanced.implementor.full.classes.ClassWithPrivateConstructor;
import info.kgeorgiy.java.advanced.implementor.full.classes.EmptyRecord;
import info.kgeorgiy.java.advanced.implementor.full.classes.RecursiveRecord;
import info.kgeorgiy.java.advanced.implementor.full.classes.standard.*;
import info.kgeorgiy.java.advanced.implementor.full.interfaces.CovariantReturns;

import org.junit.jupiter.api.Test;

import javax.annotation.processing.Completions;
import java.util.Formatter;

/**
 * Full {@link Impler} tests for hard version.
 *
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class ClassImplementorTest extends InterfaceImplementorTest {
    public ClassImplementorTest() {
    }

    @Test
    public void test11_defaultConstructorClasses() {
        testOk(RelationNotFoundException.class, AbstractClassWithInterface.class, BMPImageWriteParam.class);
    }

    @Test
    public void test12_noDefaultConstructorClasses() {
        testOk(IIOException.class, ImmutableDescriptor.class, LdapReferralException.class, ClassLogger.class);
    }

    @Test
    public void test13_ambiguousConstructorClasses() {
        testOk(IIOImage.class);
    }

    @Test
    public void test14_utilityClasses() {
        testFail(Completions.class);
    }

    @Test
    public void test15_finalClasses() {
        testFail(Integer.class, String.class);
    }

    @Test
    public void test16_standardNonClasses() {
        testFail(void.class, String[].class, int[].class, String.class, boolean.class);
    }

    @Test
    public void test17_constructorThrows() {
        testOk(FileCacheImageInputStream.class);
    }

    @Test
    public void test18_nonPublicAbstractMethod() {
        testOk(RMIServerImpl.class, RMIIIOPServerImpl.class);
    }

    @Test
    public void test19_enum() {
        testFail(Enum.class, Formatter.BigDecimalLayoutForm.class);
    }

    @Test
    public void test20_record() {
        testFail(EmptyRecord.class, RecursiveRecord.class, Record.class, VMStorage.class);
    }

    @Test
    public void test21_covariantReturns() {
        testFail(CovariantReturns.class);
    }

    @Test
    public void test30_packagePrivateConstructor() {
        testOk(ClassWithPackagePrivateConstructor.class);
    }

    @Test
    public void test31_privateConstructor() {
        testOk(ClassWithPrivateConstructor.class);
    }
}
