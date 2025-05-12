package frontend.Parser.Class;

import frontend.Parser.Parser;

import java.util.ArrayList;

public class FuncFParams {
    private ArrayList<FuncFParam> funcFParams;
    public FuncFParams(ArrayList<FuncFParam> funcFParams,ArrayList<String> outputList) {
        this.funcFParams = funcFParams;
        if (Parser.printFlag) {
            outputList.add("<FuncFParams>");
        }
    }
}
