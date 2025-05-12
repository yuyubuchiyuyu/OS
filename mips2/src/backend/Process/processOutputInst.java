package backend.Process;

import backend.Instructions.MOVE;
import backend.Instructions.SYSCALL;
import backend.Instructions.load.LA;
import backend.Instructions.load.LI;
import backend.Instructions.load.LW;
import backend.Instructions.mipsInstruction;
import backend.Optimize;
import backend.Symbol.MipsSymbol;
import backend.Symbol.MipsSymbolTable;
import middle.Class.Instruction.OutputInst;

import java.util.ArrayList;

public class processOutputInst {
    public static ArrayList<mipsInstruction> processOutputInst(OutputInst outputInst, MipsSymbolTable mipsSymbolTable) {
        if (Optimize.optimize7) {
            return optimized(outputInst, mipsSymbolTable);
        } else {
            return noOptimized(outputInst, mipsSymbolTable);
        }
    }

    private static ArrayList<mipsInstruction> optimized(OutputInst outputInst, MipsSymbolTable mipsSymbolTable) {
        ArrayList<mipsInstruction> instructions = new ArrayList<>();
        if (outputInst.strName != null) {   // 输出字符串
            MipsSymbol mipsSymbol = mipsSymbolTable.search(outputInst.strName);
            LA la = new LA(4, mipsSymbol.irName);   // la $a0
            LI li = new LI(2, 4);     // li $v0, 4
            SYSCALL syscall = new SYSCALL();
            instructions.add(la);
            instructions.add(li);
            instructions.add(syscall);
            return instructions;
        } else if (outputInst.name != null) {
            if (outputInst.require == 'd') {
                MipsSymbol mipsSymbol = mipsSymbolTable.search(outputInst.name);
                if (mipsSymbol.isStored) {
                    MOVE move = new MOVE(4, mipsSymbol.reg);
                    instructions.add(move);
                } else if (mipsSymbol.isGlobal) {
                    LW lw = new LW(4, mipsSymbol.irName);
                    instructions.add(lw);
                } else {
                    LW lw = new LW(4, mipsSymbol.base, mipsSymbol.offset);
                    instructions.add(lw);
                }
                LI li = new LI(2, 1);     // 输出整数
                SYSCALL syscall = new SYSCALL();
                instructions.add(li);
                instructions.add(syscall);
                return instructions;
            } else {
                MipsSymbol mipsSymbol = mipsSymbolTable.search(outputInst.name);
                if (mipsSymbol.isStored) {
                    MOVE move = new MOVE(4, mipsSymbol.reg);
                    instructions.add(move);
                } else if (mipsSymbol.isGlobal) {
                    LW lw = new LW(4, mipsSymbol.irName);
                    instructions.add(lw);
                } else {
                    LW lw = new LW(4, mipsSymbol.base, mipsSymbol.offset);
                    instructions.add(lw);
                }
                LI li = new LI(2, 11);     // 输出字符
                SYSCALL syscall = new SYSCALL();
                instructions.add(li);
                instructions.add(syscall);
                return instructions;
            }
        } else {  // outputInst.value != null
            if (outputInst.require == 'd') {
                LI li1 = new LI(4, outputInst.value);
                LI li2 = new LI(2, 1);     // 输出整数
                SYSCALL syscall = new SYSCALL();
                instructions.add(li1);
                instructions.add(li2);
                instructions.add(syscall);
                return instructions;
            } else {
                LI li1 = new LI(4, outputInst.value);
                LI li2 = new LI(2, 11);     // 输出字符
                SYSCALL syscall = new SYSCALL();
                instructions.add(li1);
                instructions.add(li2);
                instructions.add(syscall);
                return instructions;
            }
        }
    }

    private static ArrayList<mipsInstruction> noOptimized(OutputInst outputInst, MipsSymbolTable mipsSymbolTable) {
        ArrayList<mipsInstruction> instructions = new ArrayList<>();
        if (outputInst.strName != null) {   // 输出字符串
            MipsSymbol mipsSymbol = mipsSymbolTable.search(outputInst.strName);
            LA la = new LA(4, mipsSymbol.irName);   // la $a0
            LI li = new LI(2, 4);     // li $v0, 4
            SYSCALL syscall = new SYSCALL();
            instructions.add(la);
            instructions.add(li);
            instructions.add(syscall);
            return instructions;
        } else if (outputInst.name != null) {
            if (outputInst.require == 'd') {
                MipsSymbol mipsSymbol = mipsSymbolTable.search(outputInst.name);
                LW lw = null;
                if (mipsSymbol.isGlobal) {
                    lw = new LW(4, mipsSymbol.irName);
                } else {
                    lw = new LW(4, mipsSymbol.base, mipsSymbol.offset);
                }
                LI li = new LI(2, 1);     // 输出整数
                SYSCALL syscall = new SYSCALL();
                instructions.add(lw);
                instructions.add(li);
                instructions.add(syscall);
                return instructions;
            } else {
                MipsSymbol mipsSymbol = mipsSymbolTable.search(outputInst.name);
                LW lw = null;
                if (mipsSymbol.isGlobal) {
                    lw = new LW(4, mipsSymbol.irName);
                } else {
                    lw = new LW(4, mipsSymbol.base, mipsSymbol.offset);
                }
                LI li = new LI(2, 11);     // 输出字符
                SYSCALL syscall = new SYSCALL();
                instructions.add(lw);
                instructions.add(li);
                instructions.add(syscall);
                return instructions;
            }
        } else {  // outputInst.value != null
            if (outputInst.require == 'd') {
                LI li1 = new LI(4, outputInst.value);
                LI li2 = new LI(2, 1);     // 输出整数
                SYSCALL syscall = new SYSCALL();
                instructions.add(li1);
                instructions.add(li2);
                instructions.add(syscall);
                return instructions;
            } else {
                LI li1 = new LI(4, outputInst.value);
                LI li2 = new LI(2, 11);     // 输出字符
                SYSCALL syscall = new SYSCALL();
                instructions.add(li1);
                instructions.add(li2);
                instructions.add(syscall);
                return instructions;
            }
        }
    }
}
