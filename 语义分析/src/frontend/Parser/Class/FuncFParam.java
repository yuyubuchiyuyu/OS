package frontend.Parser.Class;

import frontend.Lexer.Token;
import frontend.Parser.Parser;
import frontend.Symbol.Operation;
import frontend.Symbol.SymbolTable;

import java.util.ArrayList;

public class FuncFParam {
    private BType bType;
    private String ident;
    private Token.tokenType left;
    private Token.tokenType right;

    public FuncFParam(BType bType, String ident, Token.tokenType left, Token.tokenType right, ArrayList<String> outputList) {
        this.bType = bType;
        this.ident = ident;
        this.left = left;
        this.right = right;
        if (Parser.printFlag) {
            outputList.add("<FuncFParam>");
        }
    }

    public FuncFParam(BType bType, String ident, ArrayList<String> outputList) {
        this.bType = bType;
        this.ident = ident;
        if (Parser.printFlag) {
            outputList.add("<FuncFParam>");
        }
    }

    public String getType(SymbolTable symbolTable,String fatherName) {
        return symbolTable.searchFType(fatherName,ident);
    }
}
