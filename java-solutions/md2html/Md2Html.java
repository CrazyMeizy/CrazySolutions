package md2html;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Md2Html {

    private static Map<String, String> createSpecialMarkTransformation() {
        Map<String, String> specialMarkTransformation = new HashMap<>();
        specialMarkTransformation.put("<", "&lt;");
        specialMarkTransformation.put(">", "&gt;");
        specialMarkTransformation.put("&", "&amp;");
        return specialMarkTransformation;
    }

    private static Map<String, Boolean> createMarkCondition() {
        Map<String, Boolean> markCondition = new HashMap<>();
        markCondition.put("*", false);
        markCondition.put("_", false);
        markCondition.put("**", false);
        markCondition.put("__", false);
        markCondition.put("--", false);
        markCondition.put("`", false);
        markCondition.put("%", false);
        return markCondition;
    }

    private static Map<String, String> createMarkTransformation() {
        Map<String, String> markTransformation = new HashMap<>();
        markTransformation.put("*", "<em>");
        markTransformation.put("_", "<em>");
        markTransformation.put("**", "<strong>");
        markTransformation.put("__", "<strong>");
        markTransformation.put("--", "<s>");
        markTransformation.put("`", "<code>");
        markTransformation.put("%", "<var>");
        return markTransformation;
    }

    private static void mdToHtml(StringBuilder paragraph,
                                 Map<String, String> markTransformation,
                                 Map<String, String> specialMarkTransformation,
                                 Map<String, Boolean> markCondition,
                                 List<MyOwnPair> list) {

        for (int i = 0; i < paragraph.length(); i++) {
            if (paragraph.charAt(i) == '\\' &&
                    markTransformation.containsKey(Character.toString(paragraph.charAt(i+1)))){
                paragraph.replace(i,i+1,"");
                continue;
            }
            String specialMark = specialMarkTransformation.get(Character.toString(paragraph.charAt(i)));
            if (specialMark != null){
                paragraph.replace(i,i+1, specialMark);
                i+=(specialMark.length()-1);
            }
            String f = Character.toString(paragraph.charAt(i));
            String fs = (i < paragraph.length() - 1) ? f + paragraph.charAt(i + 1) : " ";
            String  hmark = markTransformation.get(fs);
            String mark="";
            if (hmark == null) {
                hmark = markTransformation.get(f);
                if (hmark != null) {
                    mark = f;
                }
            }else{
                i++;
                mark=fs;
            }
            if(hmark!=null){
                String hmark_close = (new StringBuilder().append(hmark).insert(1,"/")).toString();
                MyOwnPair pair = new MyOwnPair(mark,i);
                boolean used = markCondition.get(mark);
                String strFromList;
                int intFromList;
                MyOwnPair pairFromList;
                if (used){
                    do{
                        pairFromList=list.remove(list.size()-1);
                        strFromList=pairFromList.getString();
                        markCondition.put(strFromList,false);
                        intFromList=pairFromList.getInteger();
                    }while(!strFromList.equals(mark));
                    paragraph.replace(intFromList-mark.length()+1,intFromList+1,hmark);
                    i+=(hmark.length()-mark.length());
                    paragraph.replace(i-mark.length()+1,i+1,hmark_close);
                    i+=(hmark_close.length()-mark.length());
                    markCondition.put(mark,false);
                }else{
                    list.add(pair);
                    markCondition.put(mark,true);
                }
            }

        }
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Недостаточное количество аргументов");
            return;
        }
        String inputFileName = args[0];
        String outputFileName = args[1];
        List<MyOwnPair> list = new ArrayList<>();
        StringBuilder html = new StringBuilder();
        Map<String, String> specialMarkTransformation = createSpecialMarkTransformation();
        Map<String, String> markTransformation = createMarkTransformation();
        Map<String, Boolean> markCondition = createMarkCondition();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(inputFileName),
                StandardCharsets.UTF_8))) {
            String line;
            StringBuilder paragraph = new StringBuilder();
            boolean last_blanc = true;
            int hashtagNum = 0;
            boolean now_blanc;
            do {
                line = reader.readLine();
                if (line == null) {
                    now_blanc = true;
                } else {
                    now_blanc = line.isBlank();
                }
                if (!now_blanc) {
                    paragraph.append(line).append('\n');
                    if (last_blanc) {
                        int i = 0;
                        while (line.charAt(i) == '#') {
                            i++;
                        }
                        if (Character.isWhitespace(line.charAt(i))) {
                            hashtagNum = i;
                        }
                    }
                }
                if (now_blanc && !paragraph.isEmpty()) {
                    markCondition.replaceAll((k, v) -> false);
                    mdToHtml(paragraph, markTransformation, specialMarkTransformation, markCondition, list);
                    paragraph.setLength(paragraph.length() - 1);
                    if (hashtagNum == 0) {
                        html.append("<p>").append(paragraph).append("</p>").append('\n');
                    } else {
                        String tag1 = "<h" + hashtagNum + ">";
                        String tag2 = "</h" + hashtagNum + ">";
                        paragraph.replace(0,hashtagNum+1,"");
                        html.append(tag1).append(paragraph).append(tag2).append('\n');
                    }
                    paragraph.setLength(0);
                    hashtagNum = 0;
                }
                last_blanc = now_blanc;
            } while (line != null);
        } catch (IOException e) {
            System.err.println("Input error: " + e.getMessage());
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName, StandardCharsets.UTF_8))) {
            writer.write(html.toString());
        }catch (IOException e){
            System.err.println("Output error: " + e.getMessage());
        }
    }
}
