package frontend.Parser.Class;

import frontend.Parser.Parser;

import java.util.ArrayList;

public class Character {
    private String character;

    public Character(String character, ArrayList<String> outputList) {
        this.character = character;
        if (Parser.printFlag) {
            outputList.add("<Character>");
        }
    }
}
