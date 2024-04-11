import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.charset.Charset;
public class Scanner {
    private char[] cbuf = new char[1024];
    private int blockLength;
    private int pointer;
    private boolean skipLine = false;
    private StringBuilder tempString = new StringBuilder();
    Reader reader;
    Scanner(InputStream input) throws IOException {
        this.reader = new InputStreamReader(input);
        blockLength = reader.read(cbuf);
        pointer = 0;
    }
    Scanner(String string) throws IOException {
        this.reader = new StringReader(string);
        blockLength = reader.read(cbuf);
        pointer = 0;
    }
    Scanner(String filename, String codeType) throws IOException {
        this.reader = new InputStreamReader(new FileInputStream(filename), codeType);
        blockLength = reader.read(cbuf);
        pointer = 0;
    }
    void close() throws IOException {
        reader.close();
    }
    void upPointer() throws IOException {
        if (pointer == blockLength - 1) {
            cbuf = new char[1024];
            blockLength = reader.read(cbuf);
            pointer = -1;
        }
        pointer+=1;
    }
    boolean hasNext() throws IOException {
        int temppointer = 0;
        if (!tempString.isEmpty()) {
            while (temppointer < tempString.length() && Character.isWhitespace(tempString.charAt(temppointer))) {
                temppointer++;
            }
            if (temppointer < tempString.length()) {
                return true;
            }
        }
        while (blockLength > -1 && Character.isWhitespace(cbuf[pointer])) {
            tempString.append(cbuf[pointer]);
            upPointer();
        }
        return blockLength != -1;
    }
    String next() throws IOException {
        StringBuilder next = new StringBuilder();
        int temppointer=0;
        if (!tempString.isEmpty()){
            while (temppointer<tempString.length() && Character.isWhitespace(tempString.charAt(temppointer))){
                temppointer++;
            }
            while (temppointer<tempString.length() && !Character.isWhitespace(tempString.charAt(temppointer))){
                if (cbuf[pointer]>='a' && cbuf[pointer]<='j') {
                    next.append((char)(tempString.charAt(temppointer)+'0'-'a'));
                }else{
                    next.append(tempString.charAt(temppointer));
                }
                //next.append(tempString.charAt(temppointer));
                temppointer++;
            }
            tempString.delete(0,temppointer);//может быть и не включительно, тогда пробелма
            if(temppointer<tempString.length()){
                return next.toString();
            }else{
                while (blockLength>-1 && !Character.isWhitespace(cbuf[pointer])){
                    if (cbuf[pointer]>='a' && cbuf[pointer]<='j') {
                        next.append((char)(cbuf[pointer] +'0'-'a'));
                    }else{
                        next.append(cbuf[pointer]);
                    }
                    //next.append(cbuf[pointer]);
                    upPointer();
                }
                return next.toString();
            }
        }else{
            while (blockLength>-1 && Character.isWhitespace(cbuf[pointer])){
                upPointer();
            }
            while (blockLength>-1 && !Character.isWhitespace(cbuf[pointer])){
                if (cbuf[pointer]>='a' && cbuf[pointer]<='j') {
                    next.append((char)(cbuf[pointer]+'0'-'a'));
                }else{
                    next.append(cbuf[pointer]);
                }
                //next.append(cbuf[pointer]);
                upPointer();
            }
            return next.toString();
        }
    }
    String nextToken() throws IOException {
        StringBuilder next = new StringBuilder();
        int temppointer=0;
        if (!tempString.isEmpty()){
            while (temppointer<tempString.length() && Character.isWhitespace(tempString.charAt(temppointer))){
                temppointer++;
            }
            while (temppointer<tempString.length() && !Character.isWhitespace(tempString.charAt(temppointer))){
                next.append(tempString.charAt(temppointer));
                temppointer++;
            }
            tempString.delete(0,temppointer);
            if(temppointer<tempString.length()){
                return next.toString();
            }else{
                while (blockLength>-1 && !Character.isWhitespace(cbuf[pointer])){
                    next.append(cbuf[pointer]);
                    upPointer();
                }
                return next.toString();
            }
        }else{
            while (blockLength>-1 && Character.isWhitespace(cbuf[pointer])){
                upPointer();
            }
            while (blockLength>-1 && !Character.isWhitespace(cbuf[pointer])){
                next.append(cbuf[pointer]);
                upPointer();
            }
            return next.toString();
        }
    }
    boolean isPartOfWord(char a){
        return (Character.isLetter(a) | Character.getType(a) == Character.DASH_PUNCTUATION | a == '\'');
    }
    boolean hasNextWord() throws IOException {
        int temppointer = 0;
        if (!tempString.isEmpty()) {
            while (temppointer < tempString.length() && !isPartOfWord(tempString.charAt(temppointer))) {
                temppointer++;
            }
            if (temppointer < tempString.length()) {
                return true;
            }
        }
        while (blockLength > -1 && !isPartOfWord(cbuf[pointer])) {
            tempString.append(cbuf[pointer]);
            upPointer();
        }
        return blockLength != -1;
    }
    String nextWord() throws IOException {
        StringBuilder next = new StringBuilder();
        int temppointer=0;
        if (!tempString.isEmpty()){
            while (temppointer<tempString.length() && !isPartOfWord(tempString.charAt(temppointer))){
                if (tempString.charAt(temppointer)=='\n'){
                    skipLine = true;
                }
                temppointer++;
            }
            while (temppointer<tempString.length() && isPartOfWord(tempString.charAt(temppointer))){
                next.append(Character.toLowerCase(tempString.charAt(temppointer)));
                temppointer++;
            }
            tempString.delete(0,temppointer);
            if(temppointer<tempString.length()){
                //System.out.println(next + " 1");
                return next.toString();
            }else{
                while (blockLength>-1 && isPartOfWord(cbuf[pointer])){
                    next.append(Character.toLowerCase(cbuf[pointer]));
                    upPointer();
                }
                //System.out.println(next + " 2");
                return next.toString();
            }
        }else{
            while (blockLength>-1 && !isPartOfWord(cbuf[pointer])){
                upPointer();
                if (cbuf[pointer]=='\n'){
                    skipLine = true;
                }
            }
            while (blockLength>-1 && isPartOfWord(cbuf[pointer])){
                next.append(Character.toLowerCase(cbuf[pointer]));
                upPointer();
            }
            //System.out.println(next + " 3");
            return next.toString();
        }
    }
    boolean hasNextLine() throws IOException {
        int temppointer = 0;
        if (!tempString.isEmpty()) {
            while (temppointer < tempString.length() && tempString.charAt(temppointer) != Character.LINE_SEPARATOR) {
                temppointer++;
            }
            if (temppointer < tempString.length()) {
                return true;
            }
        }
        while (blockLength > -1 && cbuf[pointer] != Character.LINE_SEPARATOR) {
            tempString.append(cbuf[pointer]);
            upPointer();
        }
        return blockLength != -1;
    }
    String nextLine() throws IOException {
        StringBuilder next = new StringBuilder();
        int temppointer = 0;
        while (temppointer < tempString.length() && tempString.charAt(temppointer) != Character.LINE_SEPARATOR) {
            next.append(tempString.charAt(temppointer));
            temppointer++;
        }
        if (temppointer < tempString.length()) {
            tempString.delete(0, temppointer + 1);
            return next.toString();
        } else {
            //System.out.println(tempString.charAt(tempString.length()-1));
            tempString.delete(0, temppointer);
            //System.out.println(cbuf[pointer]);
            while (blockLength > -1 && cbuf[pointer] != Character.LINE_SEPARATOR) {
                next.append(cbuf[pointer]);
                upPointer();
            }
            if (blockLength > -1) {
                upPointer();
            }
            return next.toString();
        }
    }
    boolean getSkipLine(){
        if (skipLine){
            skipLine = false;
            return true;
        }
        return false;
    }
}
