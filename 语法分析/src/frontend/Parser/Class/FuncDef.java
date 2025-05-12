package frontend.Parser.Class;

import frontend.Parser.Parser;

import java.util.ArrayList;

public class FuncDef {
    // 函数定义 FuncDef → FuncType Ident '(' [FuncFParams] ')' Block // j
    private FuncType funcType;
    private String ident;
    private FuncFParams funcFParams;
    private Block block;

    public FuncDef(FuncType funcType, String ident, FuncFParams funcFParams, Block block,ArrayList<String> outputList) {
        this.funcType = funcType;
        this.ident = ident;
        this.funcFParams = funcFParams;
        this.block = block;
        if (outputList != null && Parser.printFlag) {
            outputList.add("<FuncDef>");
        }
    }
}
