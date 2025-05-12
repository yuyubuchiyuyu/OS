package frontend;

public class Lexer {
    Token.tokenType type;
    String text;
    public Lexer(Token.tokenType type, String text){
        this.type = type;
        this.text = text;
    }
    public String toString(){
        return type + " " + text;
    }
}
