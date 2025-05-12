package backend;

import backend.Symbol.MipsSymbolTable;
import middle.Class.Function;
import middle.Class.GlobalVariable;
import middle.Class.Module;
import middle.Class.StrStatement;

import java.util.ArrayList;

public class Mips {
    private Module module = null;

    public Mips(Module module) {
        this.module = module;
    }

    public String getOutput() {
        ArrayList<GlobalVariable> globalVariables = module.globalVariables;
        ArrayList<StrStatement> strStatements = module.strStatements;
        ArrayList<Function> functions = module.functions;
        Function main = module.main;
        MipsSymbolTable mipsSymbolTable = new MipsSymbolTable(null);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Data.buildData(globalVariables,strStatements, mipsSymbolTable));
        stringBuilder.append(Text.buildText(functions,main, mipsSymbolTable));
        return stringBuilder.toString();
    }
}
