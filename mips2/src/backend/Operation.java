package backend;

import backend.Instructions.load.LW;
import backend.Instructions.mipsInstruction;
import backend.Instructions.store.SW;
import backend.Symbol.MipsSymbol;
import backend.Symbol.MipsSymbolTable;

import java.util.ArrayList;
import java.util.HashSet;

public class Operation {
    public static HashSet<Integer> except = new HashSet<>();

    public static Integer getTTYpeOptimized(ArrayList<mipsInstruction> instructions) {
        if (!MipsCount.judgeTYpeOptimized()) {
            while (except.contains(MipsCount.farTType)) {
                // while (!RegPool.regSymbol.containsKey(MipsCount.farTType)) {
                MipsCount.farTType++;
                if (MipsCount.farTType == 16 && !Optimize.optimize10) {
                    MipsCount.farTType = 24;
                } else if (MipsCount.farTType == 26) {
                    MipsCount.farTType = 8;
                }
            }
            MipsSymbol mipsSymbol = RegPool.regSymbol.get(MipsCount.farTType);
            if (mipsSymbol.isGlobal) {
                SW sw = new SW(MipsCount.farTType, mipsSymbol.irName);
                instructions.add(sw);
            } else {
                SW sw = new SW(MipsCount.farTType, mipsSymbol.base, mipsSymbol.offset);
                instructions.add(sw);
            }
            mipsSymbol.isStored = false;
            mipsSymbol.reg = null;
            RegPool.regs[MipsCount.farTType] = true;
            RegPool.regSymbol.remove(MipsCount.farTType);
        }
        return MipsCount.gettTYpeOptimized();
    }

    public static Integer fromSymbolGetSymbolValue(MipsSymbol mipsSymbol, ArrayList<mipsInstruction> instructions) {
        Integer reg = null;
        if (mipsSymbol.isStored) {
            reg = mipsSymbol.reg;
        } else {
            reg = Operation.getTTYpeOptimized(instructions);
            LW lw = null;
            if (mipsSymbol.isGlobal) {
                lw = new LW(reg, mipsSymbol.irName);
            } else {
                lw = new LW(reg, mipsSymbol.base, mipsSymbol.offset);
            }
            mipsSymbol.isStored = true;
            mipsSymbol.reg = reg;
            RegPool.regs[reg] = false;
            RegPool.regSymbol.put(reg, mipsSymbol);
            instructions.add(lw);
        }
        return reg;
    }

    public static void fromNameCreateNewSymbol(String name, MipsSymbolTable mipsSymbolTable, Integer reg) {
        MipsSymbol resSymbol = new MipsSymbol(name);
        resSymbol.isStored = true;
        resSymbol.reg = reg;
        RegPool.regs[reg] = false;
        RegPool.regSymbol.put(reg, resSymbol);
        mipsSymbolTable.addSymbol(resSymbol);
    }
}
