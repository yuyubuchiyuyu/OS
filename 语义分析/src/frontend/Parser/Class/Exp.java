package frontend.Parser.Class;

import frontend.Parser.Parser;

import java.util.ArrayList;

public class Exp {
    private String ident;
    private String type;
    private AddExp addExp;

    public Exp(String ident, String type, AddExp addExp, ArrayList<String> outputList) {
        this.ident = ident;
        this.type = type;
        this.addExp = addExp;
        if (Parser.printFlag) {
            outputList.add("<Exp>");
        }
    }

    public String getType() {
        return type;
    }
}
