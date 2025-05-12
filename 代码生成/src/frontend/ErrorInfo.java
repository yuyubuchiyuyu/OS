package frontend;

public class ErrorInfo {
    private Integer lineNum;
    private String character;

    public ErrorInfo(Integer lineNum, String character) {
        this.lineNum = lineNum;
        this.character = character;
    }

    public String toString() {
        return lineNum + " " + character;
    }

    public Integer getLine() {
        return lineNum;
    }
}
