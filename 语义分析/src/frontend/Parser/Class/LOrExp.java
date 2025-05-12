package frontend.Parser.Class;

import frontend.Parser.Parser;

import java.util.ArrayList;

public class LOrExp {
    // LOrExp → LAndExp LOrExp'
    private LAndExp lAndExp;
    private LOrExpTemp lOrExpTemp;

    public LOrExp(LAndExp lAndExp, LOrExpTemp lOrExpTemp, ArrayList<String> outputList) {
        this.lAndExp = lAndExp;
        this.lOrExpTemp = lOrExpTemp;
        if (Parser.printFlag) {
            outputList.add("<LOrExp>");
        }
    }

    public LOrExp(LAndExp lAndExp, ArrayList<String> outputList) {
        this.lAndExp = lAndExp;
        if (Parser.printFlag) {
            outputList.add("<LOrExp>");
        }
    }
}
