package frontend.Parser.Class;

import frontend.Parser.Parser;
import middle.Class.IrType.IrType;
import middle.Symbol.LLVMSymbol;
import middle.Symbol.LLVMSymbolTable;
import middle.Value;

import java.util.ArrayList;

public class PrimaryExp {
    private Exp exp = null;
    private LVal lVal = null;
    private Number number = null;
    private Character character = null;

    public PrimaryExp(Exp exp, ArrayList<String> outputList) {
        this.exp = exp;
        if (Parser.printFlag) {
            outputList.add("<PrimaryExp>");
        }
    }

    public PrimaryExp(LVal lVal, ArrayList<String> outputList) {
        this.lVal = lVal;
        if (Parser.printFlag) {
            outputList.add("<PrimaryExp>");
        }
    }

    public PrimaryExp(Number number, ArrayList<String> outputList) {
        this.number = number;
        if (Parser.printFlag) {
            outputList.add("<PrimaryExp>");
        }
    }

    public PrimaryExp(Character character, ArrayList<String> outputList) {
        this.character = character;
        if (Parser.printFlag) {
            outputList.add("<PrimaryExp>");
        }
    }

    public Integer calculate(LLVMSymbolTable LLVMSymbolTable) {
        if (this.exp != null) {
            return this.exp.calculate(LLVMSymbolTable);
        } else if (this.lVal != null) {
            return lVal.calculate(LLVMSymbolTable);
        } else if (this.number != null) {
            return this.number.calculate();
        } else if (this.character != null) {
            return this.character.calculate();
        }
        System.out.println("primaryExp_error");
        return null;
    }

    public boolean judgeCalculate(LLVMSymbolTable LLVMSymbolTable) {
        if (this.exp != null) {
            return this.exp.judgeCalculate(LLVMSymbolTable);
        } else if (this.lVal != null) {
            return lVal.judgeCalculate(LLVMSymbolTable);
        } else if (this.number != null) {
            return true;
        } else if (this.character != null) {
            return true;
        }
        System.out.println("primaryExp_error");
        return true;
    }

    public ArrayList<Value> getCalInstructions(LLVMSymbolTable LLVMSymbolTable) {
        ArrayList<Value> instructions = new ArrayList<>();
        if(this.exp!=null){
            return exp.getCalInstructions(LLVMSymbolTable);
        } else if(this.lVal!=null){
            return lVal.getCalInstructions(LLVMSymbolTable);
        }
        return instructions;
    }


    public IrType getIrType(LLVMSymbolTable LLVMSymbolTable) {
        if(this.exp!=null){
            return new IrType(IrType.TypeID.IntegerTyID,32);
        } else if(lVal!=null){
            return lVal.getIrType(LLVMSymbolTable);
        } else if(number!=null){
            return new IrType(IrType.TypeID.IntegerTyID,32);
        } else {
            return new IrType(IrType.TypeID.IntegerTyID,8);
        }
    }

    public LLVMSymbol judgeSymbolPure(LLVMSymbolTable LLVMSymbolTable) {
        if(this.lVal!=null){
            return lVal.getSymbol(LLVMSymbolTable);
        } else {
            return null;
        }
    }
}
