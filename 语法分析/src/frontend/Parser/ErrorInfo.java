package frontend.Parser;

public class ErrorInfo {
    private int lineNum;
    private String character;

    public ErrorInfo(int lineNum, String character) {
        this.lineNum = lineNum;
        this.character = character;
    }

    public String toString() {
        return lineNum + " " + character;
    }

    public int getLine() {
        return lineNum;
    }
}
