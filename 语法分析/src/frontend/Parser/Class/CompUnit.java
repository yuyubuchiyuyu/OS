package frontend.Parser.Class;
import frontend.Parser.Parser;

import java.util.ArrayList;

public class CompUnit {
    // 编译单元 CompUnit → {Decl} {FuncDef} MainFuncDef
    private final ArrayList<Decl> decls;
    private final ArrayList<FuncDef> funcDefs;
    private final MainFuncDef mainFuncDef;
    public CompUnit(ArrayList<Decl> decls, ArrayList<FuncDef> funcDefs, MainFuncDef mainFuncDef,ArrayList<String> outputList) {
        this.decls = decls;
        this.funcDefs = funcDefs;
        this.mainFuncDef = mainFuncDef;
        if (Parser.printFlag) {
            outputList.add("<CompUnit>");
        }
    }

}
