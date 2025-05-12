package frontend.Parser.Class;

import frontend.Parser.Parser;

import java.util.ArrayList;

public class LVal {
    private String ident;
    private Exp exp;

    public LVal(String ident, Exp exp, ArrayList<String> outputList) {
        this.ident = ident;
        this.exp = exp;
        if (Parser.printFlag) {
            outputList.add("<LVal>");
        }
    }

    public LVal(String ident, ArrayList<String> outputList) {
        this.ident = ident;
        if (Parser.printFlag) {
            outputList.add("<LVal>");
        }
    }

    public String getIdent() {
        return ident;
    }
}
