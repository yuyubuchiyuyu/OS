package frontend.Parser.Class;

import frontend.Parser.Parser;

import java.util.ArrayList;

public class ConstDef {
    // 常量定义 ConstDef → Ident [ '[' ConstExp ']' ] '=' ConstInitVal // k
    private String ident;
    private ConstExp constExp = null;
    private ConstInitVal constInitVal;

    public ConstDef(String ident, ConstExp constExp, ConstInitVal constInitVal, ArrayList<String> outputList) {
        this.ident = ident;
        this.constExp = constExp;
        this.constInitVal = constInitVal;
        if (Parser.printFlag) {
            outputList.add("<ConstDef>");
        }
    }

    public String getName() {
        return ident;
    }

    public boolean getArrayType() {
        return constExp != null;
    }
}
