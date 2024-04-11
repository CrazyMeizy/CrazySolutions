package expression.exceptions;

public class StringSource implements CharSource {
    private final String data;
    private int pos;

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
    public IllegalArgumentException error(final String message) {
        return new IllegalArgumentException(pos + ": " + message);
    }
    @Override
    public int getPos(){
        return pos;
    }
    @Override
    public char setPos(int pos){
        this.pos = pos;
        return pos-1 < data.length() ? data.charAt(pos-1) : '\0';
    }
    @Override
    public  char getPrev(){
        char temp = data.charAt(pos-2);
        return temp;
    }

}
