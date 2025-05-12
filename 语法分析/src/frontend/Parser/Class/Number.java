package frontend.Parser.Class;

import frontend.Parser.Parser;

import java.util.ArrayList;

public class Number {
    private String number;
    public Number(String number, ArrayList<String> outputList){
        this.number=number;
        if (Parser.printFlag) {
            outputList.add("<Number>");
        }
    }
}
