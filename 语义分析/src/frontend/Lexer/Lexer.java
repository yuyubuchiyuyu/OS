package frontend.Lexer;

public class Lexer {
    Token.tokenType type;
    String text;
    int lineNum;

    public Lexer(Token.tokenType type, String text, int lineNum) {
        this.type = type;
        this.text = text;
        this.lineNum = lineNum;
    }

    public Token.tokenType getType() {
        return type;
    }

    public String getText() {
        return text;
    }
    public int getLineNum() {
        return lineNum;
    }

    public String toString() {
        return type + " " + text;
    }
}
