package expression.generic;


public class StringSource implements CharSource {
    private final String data;
    private int pos;
    private static final char END = '\0';
    public StringSource(final String data) {
        this.data = data;
    }

    @Override
    public boolean hasNext() {
        return pos < data.length();
    }

    @Override
    public char next() {
        return data.charAt(pos++);
    }

    @Override
    public char getEnd() {
        return END;
    }

    @Override
    public ParsingException error(final String message) {
        return new ParsingException("At pos: " + pos + "\t" +message);
    }
    @Override
    public int getPos(){
        return pos;
    }
    @Override
    public char setPos(int pos){
        this.pos = pos;
        return pos-1 < data.length() ? data.charAt(pos-1) : END;
    }
    @Override
    public  char getPrev(){
        char temp = data.charAt(pos-2);
        return temp;
    }

}
