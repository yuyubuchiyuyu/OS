package frontend.Parser.Class;

import frontend.Parser.Parser;

import java.util.ArrayList;

public class ForStmt {
    public LVal lVal;
    public Exp exp;
    public ForStmt(LVal lVal, Exp exp, ArrayList<String> outputList){
        this.lVal = lVal;
        this.exp = exp;
        if (Parser.printFlag) {
            outputList.add("<ForStmt>");
        }
    }
}
