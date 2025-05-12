package frontend.Parser.Class;

import frontend.Parser.Parser;

import java.util.ArrayList;

public class MulExp {
    // MulExp → UnaryExp MulExp'
    private UnaryExp unaryExp;
    private MulExpTemp mulExpTemp;

    public MulExp(UnaryExp unaryExp, ArrayList<String> outputList) {
        this.unaryExp = unaryExp;
        if (Parser.printFlag) {
            outputList.add("<MulExp>");
        }
    }

    public MulExp(UnaryExp unaryExp, MulExpTemp mulExpTemp, ArrayList<String> outputList) {
        this.unaryExp = unaryExp;
        this.mulExpTemp = mulExpTemp;
        if (Parser.printFlag) {
            outputList.add("<MulExp>");
        }
    }
}
