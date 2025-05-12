package frontend.Parser.Class;

import frontend.Parser.Parser;

import java.util.ArrayList;

public class LOrExp {
    // LOrExp → LAndExp LOrExp'
    // LOrExp' → '||' LAndExp LOrExp' | ε
    private LAndExp lAndExp = null;
    private LOrExpTemp lOrExpTemp = null;
    public ArrayList<LAndExp> lAndExps;

    public LOrExp(LAndExp lAndExp, LOrExpTemp lOrExpTemp, ArrayList<String> outputList) {
        this.lAndExp = lAndExp;
        this.lOrExpTemp = lOrExpTemp;
        if (Parser.printFlag) {
            outputList.add("<LOrExp>");
        }
        this.lAndExps = new ArrayList<>();
        lAndExps.add(lAndExp);
        LOrExpTemp lOrTemp = this.lOrExpTemp;
        while(lOrTemp!=null){
            lAndExps.add(lOrTemp.getLAndTemp());
            lOrTemp = lOrTemp.getLOrExpTemp();
        }
    }

}
