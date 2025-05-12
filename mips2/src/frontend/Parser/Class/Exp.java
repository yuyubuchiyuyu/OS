package frontend.Parser.Class;

import frontend.Parser.Parser;
import middle.Symbol.LLVMSymbol;
import middle.Symbol.LLVMSymbolTable;
import middle.Value;

import java.util.ArrayList;

public class Exp {
    public String ident = null;
    public String type = null;
    public AddExp addExp = null;

    public Exp(String ident, String type, AddExp addExp, ArrayList<String> outputList) {
        this.ident = ident;
        this.type = type;
        this.addExp = addExp;
        if (Parser.printFlag) {
            outputList.add("<Exp>");
        }
    }

    public String getType() {
        return type;
    }

    public Integer calculate(LLVMSymbolTable LLVMSymbolTable) {
        return addExp.calculate(LLVMSymbolTable);
    }

    public boolean judgeCalculate(LLVMSymbolTable LLVMSymbolTable) {
        return addExp.judgeCalculate(LLVMSymbolTable);
    }

    public ArrayList<Value> getCalInstructions(LLVMSymbolTable LLVMSymbolTable) {
        return addExp.getCalInstructions(LLVMSymbolTable);
    }

    public LLVMSymbol judgeSymbolPure(LLVMSymbolTable LLVMSymbolTable){
        return addExp.judgeSymbolPure(LLVMSymbolTable);
    }
}
