package frontend.Parser.Class;

import frontend.Parser.Parser;

import java.util.ArrayList;

public class AddExp {
    // AddExp → MulExp AddExp'
    private MulExp mulExp;
    public AddExpTemp addExpTemp;

    public AddExp(MulExp mulExp, ArrayList<String> outputList) {
        this.mulExp = mulExp;
        if (Parser.printFlag) {
            outputList.add("<AddExp>");
        }

    }

    public AddExp(MulExp mulExp, AddExpTemp addExpTemp, ArrayList<String> outputList) {
        this.mulExp = mulExp;
        this.addExpTemp = addExpTemp;
        if (Parser.printFlag) {
            outputList.add("<AddExp>");
        }
    }
}
