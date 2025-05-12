package frontend.Parser.Class;

import frontend.Parser.Parser;
import middle.Symbol.SymbolTable;

import java.util.ArrayList;

public class InitVal {
    private Exp exp = null;
    private ArrayList<Exp> exps = null;
    private String stringConst = null;
    private Integer dimension;

    public InitVal(Exp exp, ArrayList<String> outputList) {
        this.exp = exp;
        this.dimension = 0;
        if (Parser.printFlag) {
            outputList.add("<InitVal>");
        }
    }

    public InitVal(ArrayList<Exp> exps, ArrayList<String> outputList) {
        this.exps = exps;
        this.dimension = 1;
        if (Parser.printFlag) {
            outputList.add("<InitVal>");
        }
    }

    public InitVal(String stringConst, ArrayList<String> outputList) {
        this.stringConst = stringConst;
        this.dimension = 1;
        if (Parser.printFlag) {
            outputList.add("<InitVal>");
        }
    }

    public Exp getExp() {
        return exp;
    }
    public ArrayList<Exp> getExps(){
        return exps;
    }
    public String getStringConst(){
        return stringConst;
    }
}
