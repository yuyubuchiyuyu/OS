package frontend.Parser.Class;

import frontend.Parser.Parser;

import java.util.ArrayList;

public class Number {
    private String number;

    public Number(String number, ArrayList<String> outputList) {
        this.number = number;
        if (Parser.printFlag) {
            outputList.add("<Number>");
        }
    }

    public Integer calculate() {
        Integer res = 0;
        for (Integer i = 0; i < this.number.length(); i++) {
            res = 10 * res + this.number.charAt(i) - '0';
        }
        return res;
    }
}
