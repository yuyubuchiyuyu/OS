package backend.Process;

import backend.Instructions.jump.J;
import backend.Instructions.load.LI;
import backend.Instructions.load.LW;
import backend.Instructions.logic.BEQ;
import backend.Instructions.mipsInstruction;
import backend.MipsCount;
import backend.Operation;
import backend.Optimize;
import backend.RegPool;
import backend.Symbol.MipsSymbol;
import backend.Symbol.MipsSymbolTable;
import middle.Class.Instruction.BranchInst;

import java.util.ArrayList;

public class processBranchInst {
    public static ArrayList<mipsInstruction> processBranchInst(BranchInst branchInst, MipsSymbolTable mipsSymbolTable) {
        if (Optimize.optimize7) {
            return optimized(branchInst, mipsSymbolTable);
        } else {
            return noOptimized(branchInst, mipsSymbolTable);
        }

    }

    private static ArrayList<mipsInstruction> optimized(BranchInst branchInst, MipsSymbolTable mipsSymbolTable) {
        ArrayList<mipsInstruction> instructions = new ArrayList<>();
        if (branchInst.dest == null) {  // "br i1 " + con     d + ", label " + ifTrue + ", label " + ifFalse
            if(Optimize.optimize8){
                MipsSymbol mipsSymbol = mipsSymbolTable.search(branchInst.cond);
                Integer reg1 = Operation.fromSymbolGetSymbolValue(mipsSymbol, instructions);
                Operation.except.add(reg1);
                BEQ beq = new BEQ(reg1, 0, branchInst.ifFalse);
                J j = new J(branchInst.ifTrue);
                instructions.add(beq);
                instructions.add(j);
            } else {
                MipsSymbol mipsSymbol = mipsSymbolTable.search(branchInst.cond);
                Integer reg1 = Operation.fromSymbolGetSymbolValue(mipsSymbol, instructions);
                Operation.except.add(reg1);
                Integer reg2 = Operation.getTTYpeOptimized(instructions);
                Operation.except.add(reg2);
                LI li = new LI(reg2, 1);
                BEQ beq = new BEQ(reg1, reg2, branchInst.ifTrue);
                J j = new J(branchInst.ifFalse);
                instructions.add(li);
                instructions.add(beq);
                instructions.add(j);
                RegPool.regs[reg2] = true;
            }
        } else {    //  "br label " + dest
            J j = new J(branchInst.dest);
            instructions.add(j);
        }
        return instructions;
    }

    private static ArrayList<mipsInstruction> noOptimized(BranchInst branchInst, MipsSymbolTable mipsSymbolTable) {
        ArrayList<mipsInstruction> instructions = new ArrayList<>();
        if (branchInst.dest == null) {  // "br i1 " + con     d + ", label " + ifTrue + ", label " + ifFalse
            Integer reg1 = MipsCount.getTType();
            MipsSymbol mipsSymbol = mipsSymbolTable.search(branchInst.cond);
            LW lw = null;
            if (mipsSymbol.isGlobal) {
                lw = new LW(reg1, mipsSymbol.irName);
            } else {
                lw = new LW(reg1, mipsSymbol.base, mipsSymbol.offset);
            }
            instructions.add(lw);
            if(Optimize.optimize8){
                BEQ beq = new BEQ(reg1, 0, branchInst.ifFalse);
                J j = new J(branchInst.ifTrue);
                instructions.add(beq);
                instructions.add(j);
            } else {
                Integer reg2 = MipsCount.getTType();
                LI li = new LI(reg2, 1);
                BEQ beq = new BEQ(reg1, reg2, branchInst.ifTrue);
                J j = new J(branchInst.ifFalse);
                instructions.add(li);
                instructions.add(beq);
                instructions.add(j);
                RegPool.regs[reg2] = true;
            }
            RegPool.regs[reg1] = true;
        } else {    //  "br label " + dest
            J j = new J(branchInst.dest);
            instructions.add(j);
        }
        return instructions;
    }
}
