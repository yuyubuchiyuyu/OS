package frontend.Parser.Class;

import frontend.Parser.Parser;

import java.util.ArrayList;

public class ConstInitVal {
    ConstExp constExp;
    ArrayList<ConstExp> constExps;
    String stringConst;
    public ConstInitVal(ConstExp constExp,ArrayList<String> outputList){
        this.constExp = constExp;
        if(Parser.printFlag){
            outputList.add("<ConstInitVal>");
        }
    }
    public ConstInitVal(ArrayList<ConstExp> constExps,ArrayList<String> outputList){
        this.constExps = constExps;
        if(Parser.printFlag){
            outputList.add("<ConstInitVal>");
        }
    }
    public ConstInitVal(String stringConst,ArrayList<String> outputList){
        this.stringConst = stringConst;
        if(Parser.printFlag){
            outputList.add("<ConstInitVal>");
        }
    }

    public ConstInitVal(ArrayList<String> outputList) {
        if(Parser.printFlag){
            outputList.add("<ConstInitVal>");
        }
    }
}
