package frontend.Parser.Class;

import frontend.Parser.Parser;
import middle.Symbol.SymbolTable;
import middle.Value;

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
