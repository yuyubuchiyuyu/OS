package frontend.Parser.Class;

import frontend.Lexer.Token;
import frontend.Parser.Parser;
import middle.Class.Instruction.CompareInst;
import middle.Class.Instruction.TransferInst;
import middle.Class.IrType.IrType;
import middle.Count;
import middle.Symbol.LLVMSymbolTable;
import middle.Value;

import java.util.ArrayList;
import java.util.Objects;

public class EqExp {
    // EqExp → RelExp EqExp'
    // EqExp' → ('==' | '!=') RelExp EqExp' | ε
    private RelExp relExp = null;
    private EqExpTemp eqExpTemp = null;

    public EqExp(RelExp relExp, EqExpTemp eqExpTemp, ArrayList<String> outputList) {
        this.relExp = relExp;
        this.eqExpTemp = eqExpTemp;
        if (Parser.printFlag) {
            outputList.add("<EqExp>");
        }
    }

    public boolean judgeGetTF(LLVMSymbolTable LLVMSymbolTable) {   // 是否直接能得到结果
        if (!relExp.judgeGetTF(LLVMSymbolTable)) {
            return false;
        }
        EqExpTemp eqTemp = this.eqExpTemp;
        while (eqTemp != null) {
            RelExp relTemp = eqTemp.getRelTemp();
            if (!relTemp.judgeGetTF(LLVMSymbolTable)) {
                return false;
            }
            eqTemp = eqTemp.getEqExpTemp();
        }
        return true;
    }

    public Integer getTFResult(LLVMSymbolTable LLVMSymbolTable) {   // 得到结果
        Integer res = relExp.getTFResult(LLVMSymbolTable);

        EqExpTemp eqTemp = this.eqExpTemp;
        while (eqTemp != null) {
            Token.tokenType type = eqTemp.getCharacterType();
            RelExp relTemp = eqTemp.getRelTemp();
            if (type == Token.tokenType.EQL) {
                if (!Objects.equals(res, relTemp.getTFResult(LLVMSymbolTable))) {
                    return 0;
                } else {
                    res = 1;
                }
            } else {
                if (Objects.equals(res, relTemp.getTFResult(LLVMSymbolTable))) {
                    return 0;
                } else {
                    res = 1;
                }
            }
            eqTemp = eqTemp.getEqExpTemp();
        }
        return res;
    }

    public ArrayList<Value> getInstructions(LLVMSymbolTable LLVMSymbolTable) {
        ArrayList<Value> instructions = new ArrayList<>();
        IrType irType = new IrType(IrType.TypeID.IntegerTyID, 32);
        Integer res1 = null;
        String name1 = null;
        String resName = null;
        if (relExp.judgeGetTF(LLVMSymbolTable)) {
            res1 = relExp.getTFResult(LLVMSymbolTable);
        } else {
            instructions.addAll(relExp.getInstructions(LLVMSymbolTable));
            name1 = instructions.get(instructions.size() - 1).getResName();
        }

        EqExpTemp eqTemp = this.eqExpTemp;
        while (eqTemp != null) {
            if(name1!=null){
                TransferInst.typeTransfer(instructions,irType);
                name1 = instructions.get(instructions.size() - 1).getResName();
            }
            Integer res2 = null;
            String name2 = null;
            Token.tokenType type = eqTemp.getCharacterType();
            RelExp relTemp = eqTemp.getRelTemp();
            if (relTemp.judgeGetTF(LLVMSymbolTable)) {
                res2 = relTemp.getTFResult(LLVMSymbolTable);
            } else {
                instructions.addAll(relTemp.getInstructions(LLVMSymbolTable));
                TransferInst.typeTransfer(instructions,irType);
                name2 = instructions.get(instructions.size() - 1).getResName();
            }
            resName = "%LocalVariable_" + Count.getFuncInner();
            CompareInst.CompareType compareType = null;
            if (type == Token.tokenType.EQL) {
                compareType = CompareInst.CompareType.eq;
            } else {
                compareType = CompareInst.CompareType.ne;
            }
            CompareInst compareInst = null;
            if (res1 != null && res2 != null) {
                compareInst = new CompareInst(resName, compareType, irType, res1, res2);
            } else if (res1 != null && name2 != null) {
                compareInst = new CompareInst(resName, compareType, irType, res1, name2);
            } else if (name1 != null && res2 != null) {
                compareInst = new CompareInst(resName, compareType, irType, name1, res2);
            } else {
                compareInst = new CompareInst(resName, compareType, irType, name1, name2);
            }
            instructions.add(compareInst);
            name1 = instructions.get(instructions.size() - 1).getResName();
            res1 = null;

            eqTemp = eqTemp.getEqExpTemp();
        }

        if (instructions.get(instructions.size() - 1).getResIrType().getNum() != 1) {
            String compareName =  "%LocalVariable_" + Count.getFuncInner();
            CompareInst compareInst = new CompareInst(compareName, CompareInst.CompareType.ne,
                    instructions.get(instructions.size()-1).getResIrType(),
                    instructions.get(instructions.size()-1).getResName(),
                    0);
            instructions.add(compareInst);
        }
        return instructions;

    }
}
