package frontend.Parser.Class;

import frontend.Parser.Parser;

import java.util.ArrayList;

public class LAndExp {
    // LAndExp → EqExp LAndExp'
    private EqExp eqExp;
    private LAndExpTemp lAndExpTemp;

    public LAndExp(EqExp eqExp, LAndExpTemp lAndExpTemp, ArrayList<String> outputList) {
        this.eqExp = eqExp;
        this.lAndExpTemp = lAndExpTemp;
        if (Parser.printFlag) {
            outputList.add("<LAndExp>");
        }
    }

    public LAndExp(EqExp eqExp, ArrayList<String> outputList) {
        this.eqExp = eqExp;
        if (Parser.printFlag) {
            outputList.add("<LAndExp>");
        }
    }
}
