package frontend.Parser.Class;

import frontend.Parser.Parser;

import java.util.ArrayList;

public class VarDecl extends Decl {
    // 变量声明 VarDecl → BType VarDef { ',' VarDef } ';' // i
    private final BType bType;
    private final ArrayList<VarDef> varDefs;

    public VarDecl(BType bType, ArrayList<VarDef> varDefs,ArrayList<String> outputList) {
        this.bType = bType;
        this.varDefs = varDefs;
        if (Parser.printFlag) {
            outputList.add("<VarDecl>");
            super.printName(outputList);
        }
    }
}
