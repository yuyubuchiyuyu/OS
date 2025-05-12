package frontend.Parser.Class;

import frontend.Parser.Parser;

import java.util.ArrayList;

public class MainFuncDef {
    private Block block;

    public MainFuncDef(Block block, ArrayList<String> outputList) {
        this.block = block;
        if (Parser.printFlag) {
            outputList.add("<MainFuncDef>");
        }
    }
}
