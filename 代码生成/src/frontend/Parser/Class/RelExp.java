package frontend.Parser.Class;

import frontend.Lexer.Token;
import frontend.Parser.Parser;
import middle.Class.Instruction.CompareInst;
import middle.Class.Instruction.TransferInst;
import middle.Class.IrType.IrType;
import middle.Count;
import middle.Symbol.SymbolTable;
import middle.Value;

import java.util.ArrayList;

public class RelExp {
    // RelExp → AddExp RelExp'
    // RelExp' → ('<' | '>' | '<=' | '>=') AddExp RelExp' | ε
    private AddExp addExp = null;
    private RelExpTemp relExpTemp = null;

    public RelExp(AddExp addExp, RelExpTemp relExpTemp, ArrayList<String> outputList) {
        this.addExp = addExp;
        this.relExpTemp = relExpTemp;
        if (Parser.printFlag) {
            outputList.add("<RelExp>");
        }
    }


    public boolean judgeGetTF(SymbolTable symbolTable) {   // 是否直接能得到结果
        if (!addExp.judgeCalculate(symbolTable)) {
            return false;
        }

        RelExpTemp relTemp = this.relExpTemp;
        while (relTemp != null) {
            AddExp addTemp = relTemp.getAddTemp();
            if (!addTemp.judgeCalculate(symbolTable)) {
                return false;
            }
            relTemp = relTemp.getRelExpTemp();
        }
        return true;
    }

    public Integer getTFResult(SymbolTable symbolTable) {   // 得到结果
        Integer res = addExp.calculate(symbolTable);

        RelExpTemp relTemp = this.relExpTemp;
        while (relTemp != null) {
            Token.tokenType type = relTemp.getCharacterType();
            AddExp addTemp = relTemp.getAddTemp();
            Integer resTemp = addTemp.calculate(symbolTable);
            if (type == Token.tokenType.LSS && res < resTemp) {
                res = 1;
            } else if (type == Token.tokenType.LEQ && res <= resTemp) {
                res = 1;
            } else if (type == Token.tokenType.GRE && res > resTemp) {
                res = 1;
            } else if (type == Token.tokenType.GEQ && res >= resTemp) {
                res = 1;
            } else {
                return 0;
            }
            relTemp = relTemp.getRelExpTemp();
        }
        return res;
    }

    public ArrayList<Value> getInstructions(SymbolTable symbolTable) {
        ArrayList<Value> instructions = new ArrayList<>();
        IrType irType = new IrType(IrType.TypeID.IntegerTyID, 32);
        Integer res1 = null;
        String name1 = null;
        String resName = null;
        if (addExp.judgeCalculate(symbolTable)) {
            res1 = addExp.calculate(symbolTable);
        } else {
            instructions.addAll(addExp.getCalInstructions(symbolTable));
            name1 = instructions.get(instructions.size() - 1).getResName();
        }

        RelExpTemp relTemp = this.relExpTemp;
        while (relTemp != null) {
            if(name1!=null){
                TransferInst.typeTransfer(instructions,irType);
                name1 =instructions.get(instructions.size() - 1).getResName();
            }

            Integer res2 = null;
            String name2 = null;
            Token.tokenType type = relTemp.getCharacterType();
            AddExp addTemp = relTemp.getAddTemp();
            if (addTemp.judgeCalculate(symbolTable)) {
                res2 = addTemp.calculate(symbolTable);
            } else {
                instructions.addAll(addTemp.getCalInstructions(symbolTable));
                TransferInst.typeTransfer(instructions,irType);
                name2 = instructions.get(instructions.size() - 1).getResName();
            }

            resName = "%LocalVariable_" + Count.getFuncInner();
            CompareInst.CompareType compareType = null;
            if (type == Token.tokenType.LSS) {
                compareType = CompareInst.CompareType.slt;
            } else if (type == Token.tokenType.LEQ) {
                compareType = CompareInst.CompareType.sle;
            } else if (type == Token.tokenType.GRE) {
                compareType = CompareInst.CompareType.sgt;
            } else {
                compareType = CompareInst.CompareType.sge;
            }
            CompareInst compareInst = null;
            if(res1!=null && res2!=null){
                compareInst = new CompareInst(resName,compareType,irType,res1,res2);
            } else if(res1!=null &&name2!=null){
                compareInst = new CompareInst(resName,compareType,irType,res1,name2);
            } else if(name1!=null && res2!=null){
                compareInst = new CompareInst(resName,compareType,irType,name1,res2);
            }else{
                compareInst = new CompareInst(resName,compareType,irType,name1,name2);
            }
            instructions.add(compareInst);
            name1 = instructions.get(instructions.size()-1).getResName();
            res1 = null;

            relTemp = relTemp.getRelExpTemp();
        }
        return instructions;
    }
}
