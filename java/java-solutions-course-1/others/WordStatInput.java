//import java.util.Scanner;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class WordStatInput {
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
            //закрытие потока ввода
            try {
                sc.close();
            }catch (IOException e){
                System.err.println("Ошибка закрытия потока ввода: " + e.getMessage());
            }
        }catch (IOException e ){
            System.err.println("Ошибка ввода: " + e.getMessage());
        }
        keys = Arrays.copyOf(keys, counter);
        values = Arrays.copyOf(values, counter);
        try {
            //отбрасывание пустых ячеек массивов
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(args[1]),
                    StandardCharsets.UTF_8));
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
            try {
                writer.close();
            }catch(IOException e){
                System.err.println("Ошибка закрытия потока вывода: " + e.getMessage());
            }
        }catch (IOException e){
            System.err.println("Ошибка вывода:" + e.getMessage());
        }
    }
}
