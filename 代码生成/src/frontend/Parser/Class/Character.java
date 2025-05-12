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

    public Integer calculate() {
        if (character.length() == 4) {
            if (character.charAt(1) == '\\') {
                if (character.charAt(2) == 'a') {
                    return 7;
                } else if (character.charAt(2) == 'b') {
                    return 8;
                } else if (character.charAt(2) == 't') {
                    return 9;
                } else if (character.charAt(2) == 'n') {
                    return 10;
                } else if (character.charAt(2) == 'v') {
                    return 11;
                } else if (character.charAt(2) == 'f') {
                    return 12;
                } else if (character.charAt(2) == '\"') {
                    return 34;
                } else if (character.charAt(2) == '\'') {
                    return 39;
                } else if (character.charAt(2) == '\\') {
                    return 92;
                } else if (character.charAt(2) == '0') {
                    return 0;
                }
            }
        }
        return (int)(character.charAt(1));
    }
}
