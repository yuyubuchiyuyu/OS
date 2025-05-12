package frontend.Parser.Class;

import frontend.Parser.Parser;

import java.util.ArrayList;

public class ConstExp {
    private AddExp addExp;

    public ConstExp(AddExp addExp, ArrayList<String> outputList) {
        this.addExp = addExp;
        if (Parser.printFlag) {
            outputList.add("<ConstExp>");
        }
    }
}
