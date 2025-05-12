package frontend.Parser;

import frontend.Lexer.Token;

import java.util.ArrayList;

public class Operation {
    public static boolean judgeExpContains(Token.tokenType type) {
        return type == Token.tokenType.PLUS ||
                type == Token.tokenType.MINU ||
                type == Token.tokenType.MULT ||
                type == Token.tokenType.DIV ||
                type == Token.tokenType.MOD ||
                type == Token.tokenType.LPARENT ||
                type == Token.tokenType.RPARENT ||
                type == Token.tokenType.INTCON ||
                type == Token.tokenType.CHRCON ||
                type == Token.tokenType.IDENFR ||
                type == Token.tokenType.LBRACK ||
                type == Token.tokenType.RBRACK ||
                type == Token.tokenType.NOT ||
                type == Token.tokenType.COMMA;
    }
    public static void addIError(int currentLineNum, ArrayList<ErrorInfo> errorList) {
        if(Parser.printFlag){
            errorList.add(new ErrorInfo(currentLineNum, "i"));
        }
    }
    public static void addJError(int currentLineNum, ArrayList<ErrorInfo> errorList){
        if(Parser.printFlag){
            errorList.add(new ErrorInfo(currentLineNum, "j"));
        }
    }
    public static void addKError(int currentLineNum, ArrayList<ErrorInfo> errorList){
        if(Parser.printFlag){
            errorList.add(new ErrorInfo(currentLineNum, "k"));
        }
    }
}
