package frontend.Parser.Class;

import frontend.Parser.Parser;
import middle.Class.IrType.IrType;
import middle.Symbol.Symbol;
import middle.Symbol.SymbolTable;
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

    public Integer calculate(SymbolTable symbolTable) {
        return addExp.calculate(symbolTable);
    }

    public boolean judgeCalculate(SymbolTable symbolTable) {
        return addExp.judgeCalculate(symbolTable);
    }

    public ArrayList<Value> getCalInstructions(SymbolTable symbolTable) {
        return addExp.getCalInstructions(symbolTable);
    }

    public Symbol judgeSymbolPure(SymbolTable symbolTable){
        return addExp.judgeSymbolPure(symbolTable);
    }
}
