package frontend.Parser.Class;

import frontend.Parser.Parser;

import java.util.ArrayList;

public class EqExp {
    // EqExp → RelExp EqExp'
    private RelExp relExp;
    private EqExpTemp eqExpTemp;

    public EqExp(RelExp relExp, EqExpTemp eqExpTemp, ArrayList<String> outputList) {
        this.relExp = relExp;
        this.eqExpTemp = eqExpTemp;
        if (Parser.printFlag) {
            outputList.add("<EqExp>");
        }
    }

    public EqExp(RelExp relExp, ArrayList<String> outputList) {
        this.relExp = relExp;
        if (Parser.printFlag) {
            outputList.add("<EqExp>");
        }
    }
}
