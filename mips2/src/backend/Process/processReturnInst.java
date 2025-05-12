package backend.Process;

import backend.Instructions.MOVE;
import backend.Instructions.SYSCALL;
import backend.Instructions.calculate.ADDI;
import backend.Instructions.jump.JR;
import backend.Instructions.load.LI;
import backend.Instructions.load.LW;
import backend.Instructions.mipsInstruction;
import backend.MipsCount;
import backend.Optimize;
import backend.Symbol.MipsSymbol;
import backend.Symbol.MipsSymbolTable;
import middle.Class.Instruction.ReturnInst;

import java.util.ArrayList;

public class processReturnInst {
    public static ArrayList<mipsInstruction> processReturnInst(ReturnInst returnInst, MipsSymbolTable mipsSymbolTable) {
        if (Optimize.optimize7) {
            return optimized(returnInst, mipsSymbolTable);
        } else {
            return noOptimized(returnInst, mipsSymbolTable);
        }
    }

    private static ArrayList<mipsInstruction> optimized(ReturnInst returnInst, MipsSymbolTable mipsSymbolTable) {
        ArrayList<mipsInstruction> instructions = new ArrayList<>();
        if (MipsCount.mainFlag) {
            LI li = new LI(2, 10);   // li $v0, 10         # 10 号系统调用，结束程序
            SYSCALL syscall = new SYSCALL();
            instructions.add(li);
            instructions.add(syscall);
            return instructions;
        }
        //  $v0 = $2
        if (returnInst.value != null) {
            LI li = new LI(2, returnInst.value);
            instructions.add(li);
        } else if (returnInst.variableName != null) {
            MipsSymbol mipsSymbol = mipsSymbolTable.search(returnInst.variableName);
            if (mipsSymbol.isStored) {
                MOVE move = new MOVE(2, mipsSymbol.reg);
                instructions.add(move);
            } else if (mipsSymbol.isGlobal) {
                LW lw = new LW(2, mipsSymbol.irName);
                instructions.add(lw);
            } else {
                LW lw = new LW(2, mipsSymbol.base, mipsSymbol.offset);
                instructions.add(lw);
            }
        }
        LW lw = new LW(31, 29, -4);
        lw.needModify = true;
        MipsCount.spOffset++;
        ADDI addi = new ADDI(29, 29, 0);
        addi.needModify = true;
        JR jr = new JR();
        instructions.add(lw);
        instructions.add(addi);
        instructions.add(jr);
        return instructions;
    }

    private static ArrayList<mipsInstruction> noOptimized(ReturnInst returnInst, MipsSymbolTable mipsSymbolTable) {
        ArrayList<mipsInstruction> instructions = new ArrayList<>();
        if (MipsCount.mainFlag) {
            LI li = new LI(2, 10);   // li $v0, 10         # 10 号系统调用，结束程序
            SYSCALL syscall = new SYSCALL();
            instructions.add(li);
            instructions.add(syscall);
            return instructions;
        }
        //  $v0 = $2
        if (returnInst.value != null) {
            LI li = new LI(2, returnInst.value);
            instructions.add(li);
        } else if (returnInst.variableName != null) {
            MipsSymbol mipsSymbol = mipsSymbolTable.search(returnInst.variableName);
            LW lw = null;
            if (mipsSymbol.isGlobal) {
                lw = new LW(2, mipsSymbol.irName);
            } else {
                lw = new LW(2, mipsSymbol.base, mipsSymbol.offset);
            }
            instructions.add(lw);
        }
        LW lw = new LW(31, 29, -4);
        lw.needModify = true;
        MipsCount.spOffset++;
        ADDI addi = new ADDI(29, 29, 0);
        addi.needModify = true;
        JR jr = new JR();
        instructions.add(lw);
        instructions.add(addi);
        instructions.add(jr);
        return instructions;
    }
}
