package frontend.Parser.Class;

import frontend.Lexer.Token;
import frontend.Parser.Parser;

import java.util.ArrayList;

public class UnaryOp {
    private Token.tokenType type;

    public UnaryOp(Token.tokenType type, ArrayList<String> outputList) {
        this.type = type;
        if (Parser.printFlag) {
            outputList.add("<UnaryOp>");
        }
    }

    public Token.tokenType getType() {
        return type;
    }
}
