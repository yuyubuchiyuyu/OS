package backend.Process;

import backend.Instructions.MOVE;
import backend.Instructions.load.LW;
import backend.Instructions.mipsInstruction;
import backend.Instructions.store.SW;
import backend.MipsCount;
import backend.Operation;
import backend.Optimize;
import backend.RegPool;
import backend.Symbol.MipsSymbol;
import backend.Symbol.MipsSymbolTable;
import middle.Class.Instruction.LoadInst;

import java.util.ArrayList;

public class processLoadInst {
    public static ArrayList<mipsInstruction> processLoadInst(LoadInst loadInst, MipsSymbolTable mipsSymbolTable) {
        if (Optimize.optimize7) {
            return optimized(loadInst, mipsSymbolTable);
        } else {
            return noOptimized(loadInst, mipsSymbolTable);
        }
    }

    private static ArrayList<mipsInstruction> optimized(LoadInst loadInst, MipsSymbolTable mipsSymbolTable) {
        ArrayList<mipsInstruction> instructions = new ArrayList<>();
        MipsSymbol mipsSymbol = mipsSymbolTable.search(loadInst.name);
        Integer reg = Operation.getTTYpeOptimized(instructions);
        Operation.except.add(reg);
        if (mipsSymbol.isAddress) {
            //LW lw0 = new LW(reg, mipsSymbol.base, mipsSymbol.offset);
            //instructions.add(lw0);
            LW lw = new LW(reg, Operation.fromSymbolGetSymbolValue(mipsSymbol,instructions), 0);
            instructions.add(lw);
        } else if (mipsSymbol.isStored) {
            MOVE move = new MOVE(reg, mipsSymbol.reg);
            instructions.add(move);
        } else if (mipsSymbol.isGlobal) {
            LW lw = new LW(reg, mipsSymbol.irName);
            instructions.add(lw);
        } else {
            LW lw = new LW(reg, mipsSymbol.base, mipsSymbol.offset);
            instructions.add(lw);
        }
        Operation.fromNameCreateNewSymbol(loadInst.getResName(), mipsSymbolTable, reg);
        return instructions;
    }

    private static ArrayList<mipsInstruction> noOptimized(LoadInst loadInst, MipsSymbolTable mipsSymbolTable) {
        ArrayList<mipsInstruction> instructions = new ArrayList<>();
        MipsSymbol mipsSymbol = mipsSymbolTable.search(loadInst.name);
        Integer reg = MipsCount.getTType();
        Integer reg0 = MipsCount.getTType();
        LW lw = null;
        if (mipsSymbol.isAddress) {
            LW lw0 = new LW(reg0, mipsSymbol.base, mipsSymbol.offset);
            instructions.add(lw0);
            lw = new LW(reg, reg0, 0);
        } else if (mipsSymbol.isGlobal) {
            lw = new LW(reg, mipsSymbol.irName);
        } else {
            lw = new LW(reg, mipsSymbol.base, mipsSymbol.offset);
        }
        MipsSymbol newMipsSymbol = new MipsSymbol(loadInst.getResName());
        mipsSymbolTable.addSymbol(newMipsSymbol);
        SW sw = new SW(reg, newMipsSymbol.base, newMipsSymbol.offset);
        instructions.add(lw);
        instructions.add(sw);
        RegPool.regs[reg] = true;
        RegPool.regs[reg0] = true;
        return instructions;
    }
}
