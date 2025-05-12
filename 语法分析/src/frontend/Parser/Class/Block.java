package frontend.Parser.Class;

import frontend.Parser.Parser;

import java.util.ArrayList;

public class Block {
    private ArrayList<BlockItem> blockItems;

    public Block(ArrayList<BlockItem> blockItems,ArrayList<String> outputList) {
        this.blockItems = blockItems;
        if (Parser.printFlag) {
            outputList.add("<Block>");
        }
    }
}
