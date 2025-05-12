package frontend.Parser.Class;

import frontend.Parser.Parser;

import java.util.ArrayList;

public class PrimaryExp {
    private Exp exp;
    private LVal lVal;
    private Number number;
    private Character character;
    public PrimaryExp(Exp exp, ArrayList<String> outputList){
        this.exp = exp;
        if (Parser.printFlag) {
            outputList.add("<PrimaryExp>");
        }
    }
    public PrimaryExp(LVal lVal, ArrayList<String> outputList){
        this.lVal = lVal;
        if (Parser.printFlag) {
            outputList.add("<PrimaryExp>");
        }
    }
    public PrimaryExp(Number number, ArrayList<String> outputList) {
        this.number = number;
        if (Parser.printFlag) {
            outputList.add("<PrimaryExp>");
        }
    }
    public PrimaryExp(Character character, ArrayList<String> outputList) {
        this.character = character;
        if (Parser.printFlag) {
            outputList.add("<PrimaryExp>");
        }
    }
}
