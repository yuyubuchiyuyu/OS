package frontend.Parser.Class;

import frontend.Parser.Parser;

import java.util.ArrayList;

public class InitVal {
    Exp exp;
    ArrayList<Exp> exps;
    String stringConst;

    public InitVal(Exp exp, ArrayList<String> outputList) {
        this.exp = exp;
        if (Parser.printFlag) {
            outputList.add("<InitVal>");
        }
    }

    public InitVal(ArrayList<Exp> exps, ArrayList<String> outputList) {
        this.exps = exps;
        if (Parser.printFlag) {
            outputList.add("<InitVal>");
        }
    }

    public InitVal(String stringConst, ArrayList<String> outputList) {
        this.stringConst = stringConst;
        if (Parser.printFlag) {
            outputList.add("<InitVal>");
        }
    }
}
