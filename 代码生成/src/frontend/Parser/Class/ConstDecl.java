package frontend.Parser.Class;

import frontend.Parser.Parser;

import java.util.ArrayList;

public class ConstDecl extends Decl {
    // 常量声明 ConstDecl → 'const' BType ConstDef { ',' ConstDef } ';' // i
    private final BType bType;
    private final ArrayList<ConstDef> constDefs;

    public ConstDecl(BType bType, ArrayList<ConstDef> constDefs, ArrayList<String> outputList) {
        this.bType = bType;
        this.constDefs = constDefs;
        if (Parser.printFlag) {
            outputList.add("<ConstDecl>");
            super.printName(outputList);
        }
    }

    public ArrayList<ConstDef> getDefs() {
        return constDefs;
    }

    public BType getBType() {
        return bType;
    }
}
