package expression.generic;


public class BaseParser<T> {
    private final CharSource source;
    private char ch = 0xffff;

    protected BaseParser(final CharSource source) {
        this.source = source;
        take();
    }
    protected char getChar(){
        return ch;
    }

    protected char take() {
        final char result = ch;
        if(source.hasNext()){
            ch = source.next();
        }else{
            ch = source.setPos(source.getPos()+1);
        }
        return result;
    }

    protected boolean test(final char expected) {
        return ch == expected;
    }

    protected boolean take(final char expected) {
        if (test(expected)) {
            take();
            return true;
        }
        return false;
    }
    protected boolean take(final String expected){
        int pos = source.getPos();
        for (final char c : expected.toCharArray()) {
            if(!take(c)){
                ch = source.setPos(pos);
                return false;
            }
        }
        return true;
    }
    protected boolean test(final String expected){
        int pos = source.getPos();
        for (final char c : expected.toCharArray()) {
            if(!take(c)){
                ch = source.setPos(pos);
                return false;
            }
        }
        ch = source.setPos(pos);
        return true;
    }
    protected ParsingException error(final String message) {
        return source.error(message);
    }
    protected boolean isDigit() {
        return Character.isDigit(ch);
    }
    protected char getEnd(){
        return source.getEnd();
    }
    protected void skipWhitespace() {
        while(Character.isWhitespace(ch)) {
            take();
        }
    }
}

