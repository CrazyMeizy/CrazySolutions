package expression.exceptions;

public interface CharSource {
    int getPos();
    char setPos(int pos);
    char getPrev();
    boolean hasNext();
    char next();
    IllegalArgumentException error(String message);
}
