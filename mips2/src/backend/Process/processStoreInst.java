package backend.Process;

import backend.Instructions.MOVE;
import backend.Instructions.load.LI;
import backend.Instructions.load.LW;
import backend.Instructions.mipsInstruction;
import backend.Instructions.store.SW;
import backend.MipsCount;
import backend.Operation;
import backend.Optimize;
import backend.RegPool;
import backend.Symbol.MipsSymbol;
import backend.Symbol.MipsSymbolTable;
import middle.Class.Instruction.StoreInst;

import java.util.ArrayList;

public class processStoreInst {
    public static ArrayList<mipsInstruction> processStoreInst(StoreInst storeInst, MipsSymbolTable mipsSymbolTable) {
        if (Optimize.optimize7) {
            return optimized(storeInst, mipsSymbolTable);
        } else {
            return noOptimized(storeInst, mipsSymbolTable);
        }
    }

    private static ArrayList<mipsInstruction> optimized(StoreInst storeInst, MipsSymbolTable mipsSymbolTable) {
        ArrayList<mipsInstruction> instructions = new ArrayList<>();
        Integer reg;
        boolean regFlag = false;
        if (storeInst.irName1 != null) {
            MipsSymbol mipsSymbol1 = mipsSymbolTable.search(storeInst.irName1);
            if (mipsSymbol1.isStored) {
                reg = mipsSymbol1.reg;
            } else {
                reg = Operation.getTTYpeOptimized(instructions);
                LW lw = null;
                if (mipsSymbol1.isGlobal) {
                    lw = new LW(reg, mipsSymbol1.irName);
                } else {
                    lw = new LW(reg, mipsSymbol1.base, mipsSymbol1.offset);
                }
                instructions.add(lw);
                regFlag = true;
            }
        } else {
            if (Optimize.optimize11 && storeInst.value == 0) {
                reg = 0;
            } else {
                reg = Operation.getTTYpeOptimized(instructions);
                LI li = new LI(reg, storeInst.value);
                instructions.add(li);
                regFlag = true;
            }

        }
        Operation.except.add(reg);
        MipsSymbol mipsSymbol2 = mipsSymbolTable.search(storeInst.irName2);
        SW sw = null;
        if (mipsSymbol2.isAddress) {
            //Integer reg0 = Operation.getTTYpeOptimized(instructions);
            //LW lw = new LW(reg0, mipsSymbol2.base, mipsSymbol2.offset);
            //instructions.add(lw);
            sw = new SW(reg, Operation.fromSymbolGetSymbolValue(mipsSymbol2, instructions), 0);
            instructions.add(sw);
            //RegPool.regs[reg0] = true;
        } else if (mipsSymbol2.isStored) {
            MOVE move = new MOVE(mipsSymbol2.reg, reg);
            instructions.add(move);
        } else if (mipsSymbol2.isGlobal) {
            sw = new SW(reg, mipsSymbol2.irName);
            instructions.add(sw);
        } else {
            sw = new SW(reg, mipsSymbol2.base, mipsSymbol2.offset);
            instructions.add(sw);
        }
        if (regFlag) {
            RegPool.regs[reg] = true;
        }
        return instructions;
    }

    private static ArrayList<mipsInstruction> noOptimized(StoreInst storeInst, MipsSymbolTable mipsSymbolTable) {
        ArrayList<mipsInstruction> instructions = new ArrayList<>();
        Integer reg = MipsCount.getTType();
        Integer reg0 = MipsCount.getTType();
        if (storeInst.irName1 != null) {
            MipsSymbol mipsSymbol1 = mipsSymbolTable.search(storeInst.irName1);
            LW lw = null;
            if (mipsSymbol1.isGlobal) {
                lw = new LW(reg, mipsSymbol1.irName);
            } else {
                lw = new LW(reg, mipsSymbol1.base, mipsSymbol1.offset);
            }
            instructions.add(lw);
        } else {
            LI li = new LI(reg, storeInst.value);
            instructions.add(li);
        }
        MipsSymbol mipsSymbol2 = mipsSymbolTable.search(storeInst.irName2);
        SW sw = null;
        if (mipsSymbol2.isAddress) {
            LW lw = new LW(reg0, mipsSymbol2.base, mipsSymbol2.offset);
            instructions.add(lw);
            sw = new SW(reg, reg0, 0);
        } else if (mipsSymbol2.isGlobal) {
            sw = new SW(reg, mipsSymbol2.irName);
        } else {
            sw = new SW(reg, mipsSymbol2.base, mipsSymbol2.offset);
        }
        instructions.add(sw);
        RegPool.regs[reg] = true;
        RegPool.regs[reg0] = true;
        return instructions;
    }
}
