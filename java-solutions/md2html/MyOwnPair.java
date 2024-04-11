package md2html;

public class MyOwnPair {
    private String string;
    private int integer;

    public MyOwnPair(String string, int integer) {
        this.string = string;
        this.integer = integer;
    }

    public String getString() {
        return string;
    }

    public int getInteger() {
        return integer;
    }

    public void setString(String string) {
        this.string = string;
    }

    public void setInteger(Integer integer) {
        this.integer = integer;
    }
}
