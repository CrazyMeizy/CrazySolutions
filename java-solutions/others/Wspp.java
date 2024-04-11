//import java.util.Scanner;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Wspp {
    public static void main(String[] args){
        //сохранение названий файлов, если их дано 2
        if (args.length != 2) {
            System.err.println("Неверное количество аргументов: " + args.length);
            return;
        }
        String inputFileName = args[0];
        String outputFileName = args[1];
        String wordStr;
        Map<String, ArrayList<Integer>> map = new LinkedHashMap<>();
        int counter = 0;
        try {
            Scanner sc = new Scanner(inputFileName, StandardCharsets.UTF_8.name());
            while (sc.hasNextWord()) {
                wordStr = sc.nextWord();
                counter++;
                if(map.containsKey(wordStr)){
                    ArrayList<Integer> temp = map.get(wordStr);
                    temp.set(0,temp.get(0)+1);
                    temp.add(counter);
                }else{
                    ArrayList<Integer> temp = new ArrayList<Integer>(List.of(0));
                    temp.set(0,temp.get(0)+1);
                    temp.add(counter);
                    map.put(wordStr,temp);
                }
            }
            //закрытие потока ввода
            try {
                sc.close();
            }catch (IOException e){
                System.err.println("Ошибка закрытия потока ввода: " + e.getMessage());
            }
        }catch (IOException e ){
            System.err.println("Ошибка ввода: " + e.getMessage());
        }
        try {
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(outputFileName),
                    StandardCharsets.UTF_8))) {
                StringBuilder output = new StringBuilder();
                for (Map.Entry<String, ArrayList<Integer>> entry : map.entrySet()) {
                    output.setLength(0);
                    output.append(entry.getKey() + " ");
                    int count = 0;
                    for (Integer i : entry.getValue()){
                        if (count == entry.getValue().size()-1){
                            output.append(i);
                        }else{
                            output.append(i+" ");
                        }
                        count++;
                    }
                    writer.write(output.toString());
                    writer.newLine();
                    //System.out.println(output.toString());
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка вывода данных: " + e.getMessage());
        }
    }
}