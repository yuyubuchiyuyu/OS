package frontend.Parser.Class;

import frontend.Parser.Parser;

import java.util.ArrayList;

public class FuncRParams {
    private ArrayList<Exp> exps;

    public FuncRParams(ArrayList<Exp> exps, ArrayList<String> outputList) {
        this.exps = exps;
        if (Parser.printFlag) {
            outputList.add("<FuncRParams>");
        }
    }
    public ArrayList<Exp> getExps(){
        return exps;
    }
}
