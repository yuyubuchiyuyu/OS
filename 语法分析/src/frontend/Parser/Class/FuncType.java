package frontend.Parser.Class;

import frontend.Lexer.Token;
import frontend.Parser.Parser;

import java.util.ArrayList;

public class FuncType {
    Token.tokenType funcType;

    public FuncType(Token.tokenType funcType, ArrayList<String> outputList) {
        this.funcType = funcType;
        if (Parser.printFlag) {
            outputList.add("<FuncType>");
        }
    }

}
