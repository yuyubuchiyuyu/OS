package frontend.Parser.Class;

import frontend.Parser.Parser;

import java.util.ArrayList;

public class Block {
    private ArrayList<BlockItem> blockItems;

    public Block(ArrayList<BlockItem> blockItems, ArrayList<String> outputList) {
        this.blockItems = blockItems;
        if (Parser.printFlag) {
            outputList.add("<Block>");
        }
    }

    public boolean getResult() { // 是否有return语句
        if (!blockItems.isEmpty()) {
            return blockItems.get(blockItems.size() - 1).getResult();
        }
        return false;
    }

}
