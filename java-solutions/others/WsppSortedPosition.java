//import java.util.Scanner;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class WsppSortedPosition {
    public static void main(String[] args) {
        //сохранение названий файлов, если их дано 2
        if (args.length != 2) {
            System.err.println("Неверное количество аргументов: " + args.length);
            return;
        }
        String inputFileName = args[0];
        String outputFileName = args[1];

        //инициализация переменных
        String wordStr="";
        int wordNum = 0;
        int lineNum = 1;
        Map<String, ArrayList<String>> map = new TreeMap<>();
        List<String> keys = new ArrayList<>();
        List<String> temp;
        boolean notEnd =true;

        try {
            Scanner sc = new Scanner(inputFileName, StandardCharsets.UTF_8.name()); //standardcharset
            while (notEnd | (notEnd=sc.hasNextWord())) {
                if (notEnd) {
                    wordStr = sc.nextWord();
                }
                if (sc.getSkipLine() | !notEnd) {
                    for (int i = 0; i < wordNum; i++) {// getordefault.add / computeifabsent / compute / putifabsent
                        temp =map.computeIfAbsent(keys.get(i), k -> new ArrayList<>(List.of("0")));
                        temp.add((lineNum + ":" + (wordNum - i)));
                        temp.set(0, String.valueOf(Integer.parseInt(temp.get(0)) + 1));
                    }
                    lineNum++;
                    keys = new ArrayList<>();
                    wordNum = 0;
                }
                keys.add(wordStr);
                wordNum++;
            }

            //закрытие потока ввода
            try {
                sc.close();
            } catch (IOException e) {
                System.err.println("Ошибка закрытия потока ввода: " + e.getMessage());
            }
        } catch (IOException e) {
            System.err.println("Ошибка ввода: " + e.getMessage());
            return;
        }

        //вывод
        try {
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(outputFileName),
                    StandardCharsets.UTF_8/*System.out*/))) {
                StringBuilder output = new StringBuilder();
                for (Map.Entry<String, ArrayList<String>> entry : map.entrySet()) {
                    output.setLength(0);
                    output.append(entry.getKey()).append(" ");
                    int count = 0;
                    for (String i : entry.getValue()) {
                        if (count == entry.getValue().size() - 1) {
                            output.append(i);
                        } else {
                            output.append(i).append(" ");
                        }
                        count++;
                    }
                    writer.write(output.toString());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка вывода данных: " + e.getMessage());
        }
    }
}
