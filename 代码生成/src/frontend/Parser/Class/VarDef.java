package frontend.Parser.Class;

import frontend.Parser.Parser;
import middle.Symbol.SymbolTable;
import middle.Value;

import java.util.ArrayList;

public class VarDef {
    private String ident;
    private ConstExp constExp = null;
    private InitVal initVal = null;
    private final Integer dimension;

    public VarDef(String ident, ConstExp constExp, InitVal initVal, ArrayList<String> outputList) {
        this.ident = ident;
        if (constExp != null) {
            this.dimension = 1;
            this.constExp = constExp;
        } else {
            this.dimension = 0;
        }
        this.initVal = initVal;
        if (Parser.printFlag) {
            outputList.add("<VarDef>");
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
    public InitVal getInitVal() {
        return initVal;
    }
    public ConstExp getConstExp(){
        return constExp;
    }
}

