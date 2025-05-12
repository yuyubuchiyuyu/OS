package frontend.Parser.Class;

import frontend.Parser.Parser;

import java.util.ArrayList;

public class RelExp {
    // RelExp → AddExp RelExp'
    private AddExp addExp;
    private RelExpTemp relExpTemp;

    public RelExp(AddExp addExp, RelExpTemp relExpTemp, ArrayList<String> outputList) {
        this.addExp = addExp;
        this.relExpTemp = relExpTemp;
        if (Parser.printFlag) {
            outputList.add("<RelExp>");
        }
    }

    public RelExp(AddExp addExp, ArrayList<String> outputList) {
        this.addExp = addExp;
        if (Parser.printFlag) {
            outputList.add("<RelExp>");
        }
    }
}
