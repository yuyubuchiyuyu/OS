package frontend.Parser.Class;

import frontend.Parser.Parser;

import java.util.ArrayList;

public class VarDef {
    private String ident;
    private ConstExp constExp;
    private InitVal initVal;

    public VarDef(String ident, ConstExp constExp, InitVal initVal, ArrayList<String> outputList) {
        this.ident = ident;
        this.constExp = constExp;
        this.initVal = initVal;
        if (Parser.printFlag) {
            outputList.add("<VarDef>");
        }
    }

    public VarDef(String ident, InitVal initVal, ArrayList<String> outputList) {
        this.ident = ident;
        this.initVal = initVal;
        if (Parser.printFlag) {
            outputList.add("<VarDef>");
        }
    }

    public VarDef(String ident, ConstExp constExp, ArrayList<String> outputList) {
        this.ident = ident;
        this.constExp = constExp;
        if (Parser.printFlag) {
            outputList.add("<VarDef>");
        }
    }

    public VarDef(String ident, ArrayList<String> outputList) {
        this.ident = ident;
        if (Parser.printFlag) {
            outputList.add("<VarDef>");
        }
    }
}

