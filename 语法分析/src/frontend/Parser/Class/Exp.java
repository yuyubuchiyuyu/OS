package frontend.Parser.Class;

import frontend.Parser.Parser;

import java.util.ArrayList;

public class Exp {
    private AddExp addExp;
    public Exp(AddExp addExp, ArrayList<String> outputList){
        this.addExp= addExp;
        if (Parser.printFlag) {
            outputList.add("<Exp>");
        }
    }
}
