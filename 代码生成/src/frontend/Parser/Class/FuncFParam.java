package frontend.Parser.Class;

import frontend.Lexer.Token;
import frontend.Parser.Parser;
import frontend.Symbol.SymbolTable;

import java.util.ArrayList;

public class FuncFParam {
    private BType bType;
    private String ident;
    private Token.tokenType left = null;
    private Token.tokenType right = null;
    private Integer dimension;

    public FuncFParam(BType bType, String ident, Token.tokenType left, Token.tokenType right, ArrayList<String> outputList) {
        this.bType = bType;
        this.ident = ident;
        this.left = left;
        this.right = right;
        if (left != null) {
            dimension = 1;
        } else {
            dimension = 0;
        }
        if (Parser.printFlag) {
            outputList.add("<FuncFParam>");
        }
    }

    public String getType(SymbolTable symbolTable, String fatherName) {
        return symbolTable.searchFType(fatherName, ident);
    }

    public BType getbType() {
        return bType;
    }
    public Integer getDimension(){
        return dimension;
    }

    public String getIdent() {
        return ident;
    }
}
