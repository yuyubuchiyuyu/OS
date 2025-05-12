package frontend.Parser.Class;

import frontend.Parser.Parser;
import middle.Symbol.SymbolTable;

import java.util.ArrayList;

public class ConstDef {
    // 常量定义 ConstDef → Ident [ '[' ConstExp ']' ] '=' ConstInitVal // k
    private String ident = null;
    private ConstExp constExp = null;
    private ConstInitVal constInitVal = null;
    private final Integer dimension;

    public ConstDef(String ident, ConstExp constExp, ConstInitVal constInitVal, ArrayList<String> outputList) {
        this.ident = ident;
        if (constExp != null) {
            this.dimension = 1;
            this.constExp = constExp;
        } else {
            this.dimension = 0;
        }
        this.constInitVal = constInitVal;
        if (Parser.printFlag) {
            outputList.add("<ConstDef>");
        }
    }

    public String getIdent() {
        return ident;
    }

    public boolean getArrayType() {
        return constExp != null;
    }

    public Integer getDimension() {
        return dimension;
    }

    public ConstExp getConstExp(){
        return constExp;
    }

    public ConstInitVal getConstInitVal() {
        return constInitVal;
    }
}
