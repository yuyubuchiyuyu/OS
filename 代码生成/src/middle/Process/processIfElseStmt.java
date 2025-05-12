package middle.Process;

import frontend.Parser.Class.Cond;
import frontend.Parser.Class.EqExp;
import frontend.Parser.Class.LAndExp;
import frontend.Parser.Class.Stmt;
import middle.Class.Instruction.BranchInst;
import middle.Class.Instruction.CompareInst;
import middle.Class.Instruction.LabelInst;
import middle.Class.IrType.IrType;
import middle.Count;
import middle.Symbol.SymbolTable;
import middle.Value;

import java.util.ArrayList;

public class processIfElseStmt {
    public static ArrayList<Value> processIfElseStmt(Stmt stmt, SymbolTable symbolTable, String funcName, String continueName, String breakName) {
        // 没有 进入 cond 的标签
        ArrayList<Value> instructions = new ArrayList<>();
        Stmt ifStmt = stmt.ifStmt;
        Cond cond = stmt.ifCond;
        Stmt elseStmt = stmt.elseStmt;

        String ifLabel = "%if_" + Count.getIfLabel();
        String elseLabel = "%else_" + Count.getElseLabel();
        String endLabel = "%ifElseEnd_" + Count.getIfElseEndLabel();
        if (elseStmt != null) {
            instructions.addAll(processCond(cond, symbolTable, ifLabel, elseLabel));
            // if
            LabelInst ifLabelInst = new LabelInst(ifLabel);
            instructions.add(ifLabelInst);
            instructions.addAll(processStmt.processStmt(ifStmt, symbolTable, funcName, continueName, breakName));
            BranchInst ifToEndBranchInst = new BranchInst(endLabel);
            instructions.add(ifToEndBranchInst);
            // else
            LabelInst elseLabelInst = new LabelInst(elseLabel);
            instructions.add(elseLabelInst);
            instructions.addAll(processStmt.processStmt(elseStmt, symbolTable, funcName, continueName, breakName));
            BranchInst elseToEndBranchInst = new BranchInst(endLabel);
            instructions.add(elseToEndBranchInst);
            // end
            LabelInst endLabelInst = new LabelInst(endLabel);
            instructions.add(endLabelInst);
        } else {
            instructions.addAll(processCond(cond, symbolTable, ifLabel, endLabel));
            // if
            LabelInst ifLabelInst = new LabelInst(ifLabel);
            instructions.add(ifLabelInst);
            instructions.addAll(processStmt.processStmt(ifStmt, symbolTable, funcName, continueName, breakName));
            BranchInst ifToEndBranchInst = new BranchInst(endLabel);
            instructions.add(ifToEndBranchInst);
            // end
            LabelInst endLabelInst = new LabelInst(endLabel);
            instructions.add(endLabelInst);
        }
        return instructions;
    }

    public static ArrayList<Value> processCond(Cond cond, SymbolTable symbolTable, String ifLabel, String elseLabel) {
        ArrayList<Value> instructions = new ArrayList<>();
        ArrayList<LAndExp> lAndExps = cond.lOrExp.lAndExps;

        ArrayList<ArrayList<String>> eqExpsName = new ArrayList<>();
        for (int i = 0; i < lAndExps.size(); i++) {
            ArrayList<String> eqExpsNameTemp = new ArrayList<>();
            for (int j = 0; j < lAndExps.get(i).eqExps.size(); j++) {
                String eqExpName = "%Cond_" + Count.getIfCondLabel();
                eqExpsNameTemp.add(eqExpName);
            }
            eqExpsName.add(eqExpsNameTemp);
        }
        IrType irType = new IrType(IrType.TypeID.IntegerTyID, 1);
        for (int i = 0; i < lAndExps.size(); i++) {
            LAndExp lAndExp = lAndExps.get(i);
            for (int j = 0; j < lAndExp.eqExps.size(); j++) {
                if (!(i == 0 && j == 0)) {
                    LabelInst labelInst2 = new LabelInst(eqExpsName.get(i).get(j));
                    instructions.add(labelInst2);
                }
                EqExp eqExp = lAndExp.eqExps.get(j);
                String resName = "%LocalVariable_" + Count.getFuncInner();
                String nextEqExp;
                String nextLAndExp;
                if (j + 1 < lAndExp.eqExps.size()) {
                    nextEqExp = eqExpsName.get(i).get(j + 1);
                } else {
                    nextEqExp = ifLabel;
                }
                if (i + 1 < lAndExps.size()) {
                    nextLAndExp = eqExpsName.get(i + 1).get(0);
                } else {
                    nextLAndExp = elseLabel;
                }

                if (eqExp.judgeGetTF(symbolTable)) {
                    Integer res = eqExp.getTFResult(symbolTable);
                    BranchInst branchInst = null;
                    if (res != 0) {
                        branchInst = new BranchInst(nextEqExp);
                    } else {
                        branchInst = new BranchInst(nextLAndExp);
                    }
                    instructions.add(branchInst);
                } else {
                    instructions.addAll(eqExp.getInstructions(symbolTable));
                    CompareInst compareInst = new CompareInst(resName, CompareInst.CompareType.ne, irType, instructions.get(instructions.size() - 1).getResName(), 0);
                    instructions.add(compareInst);
                    BranchInst branchInst = new BranchInst(instructions.get(instructions.size() - 1).getResName(),
                            nextEqExp, nextLAndExp);
                    instructions.add(branchInst);
                }
            }

        }
        return instructions;
    }
}
