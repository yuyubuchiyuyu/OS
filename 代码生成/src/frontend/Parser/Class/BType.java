package frontend.Parser.Class;

import frontend.Lexer.Token;

public class BType {
    Token.tokenType bType;
    public BType(Token.tokenType bType){
        this.bType = bType;
        // System.out.println("<BType>");
    }
    public Token.tokenType getBType(){
        return bType;
    }
}
