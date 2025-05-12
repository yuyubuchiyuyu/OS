package frontend.Parser.Class;

import frontend.Parser.Parser;

import java.util.ArrayList;

public class FuncDef {
    // 函数定义 FuncDef → FuncType Ident '(' [FuncFParams] ')' Block // j
    private FuncType funcType = null;
    private String ident = null;
    private FuncFParams funcFParams = null;
    private Block block = null;

    public FuncDef(FuncType funcType, String ident, FuncFParams funcFParams, Block block, ArrayList<String> outputList) {
        this.funcType = funcType;
        this.ident = ident;
        this.funcFParams = funcFParams;
        this.block = block;
        if (outputList != null && Parser.printFlag) {
            outputList.add("<FuncDef>");
        }
    }
    public FuncType getFuncType(){
        return funcType;
    }

    public String getName() {
        return ident;
    }

    public FuncFParams getFuncParams() {
        return funcFParams;
    }

    public Block getBlock() {
        return block;
    }
}
