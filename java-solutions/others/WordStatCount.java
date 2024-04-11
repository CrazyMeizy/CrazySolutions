//import java.util.Scanner;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class WordStatCount {
    public static void main(String[] args){
        //сохранение названий файлов, если их дано 2
        if (args.length != 2) {
            System.err.println("Неверное количество аргументов: " + args.length);
            return;
        }
        String inputFileName = args[0];
        String outputFileName = args[1];
        String word;
        String[] keys = new String[10];
        int[] values = new int[10];
        int keyPointer;
        int counter = 0;
        try {
            Scanner sc = new Scanner(inputFileName, "UTF-8");
            try {
                while (sc.hasNextWord()) {
                    word = sc.nextWord();
                    keyPointer = 0;
                    while (values[keyPointer] != 0 && !(keys[keyPointer].contentEquals(word))) {
                        keyPointer++;
                    }
                    if (keyPointer == keys.length - 1) {
                        keys = Arrays.copyOf(keys, (keys.length * 3) / 2 + 1);
                        values = Arrays.copyOf(values, (values.length * 3) / 2 + 1);
                    }
                    if (values[keyPointer] == 0) {
                        keys[keyPointer] = word;
                        counter++;
                        //System.out.println(counter);
                    }
                    values[keyPointer]++;
                }
            }catch (IOException e){
                System.err.println("Ошибка чтения из файла: " + e.getMessage());
            }
            //закрытие потока ввода
            try {
                sc.close();
            }catch (IOException e){
                System.out.println("Ошибка закрытия файла ввода: " + e.getMessage());
            }
        }catch (IOException e ){
            System.err.println("Ошибка ввода: " + e.getMessage());
        }
        keys = Arrays.copyOf(keys, counter);
        values = Arrays.copyOf(values, counter);
        //сортировка массивов по возрастанию values(стабильная сортировка пузырьком)
        int tempValue;
        String tempKey;
        for (int i = 0; i < values.length - 1; i++) {
            for (int j = 0; j < values.length - i - 1; j++) {
                if (values[j] > values[j + 1]) {
                    tempValue = values[j];
                    values[j] = values[j + 1];
                    values[j + 1] = tempValue;
                    tempKey = keys[j];
                    keys[j] = keys[j + 1];
                    keys[j + 1] = tempKey;
                }
            }
        }
        try {
            //отбрасывание пустых ячеек массивов
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(args[1]),
                    StandardCharsets.UTF_8));
            try {
                //вывод данных
                StringBuilder output = new StringBuilder();
                for (int i = 0; i < values.length; i++) {
                    output.setLength(0);
                    output.append(keys[i]).append(" ").append(values[i]);
                    //System.out.println(output);
                    writer.write(output.toString());
                    writer.newLine();
                    System.out.println(output.toString());
                }
            }catch (IOException e){
                System.err.println("Ошибка записи в файл: " + e.getMessage());
            }
            try {
                writer.close();
            }catch (IOException e){
                System.err.println("Ошибка закрытия файлы вывода: " + e.getMessage());
            }
        }catch (IOException e){
            System.err.println("Ошибка доступа к файлу вывода:" + e.getMessage());
        }
    }
}
