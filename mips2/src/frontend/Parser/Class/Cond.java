package frontend.Parser.Class;

import frontend.Parser.Parser;

import java.util.ArrayList;

public class Cond {
    public LOrExp lOrExp;

    public Cond(LOrExp lOrExp, ArrayList<String> outputList) {
        this.lOrExp = lOrExp;
        if (Parser.printFlag) {
            outputList.add("<Cond>");
        }
    }


}
