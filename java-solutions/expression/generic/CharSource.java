package expression.generic;


public interface CharSource {
    int getPos();
    char setPos(int pos);
    char getPrev();
    boolean hasNext();
    char next();
    char getEnd();
    ParsingException error(String message);
}
