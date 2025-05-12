package backend;

import backend.Instructions.mipsInstruction;
import backend.Instructions.store.SW;
import backend.Symbol.MipsSymbol;

import java.util.ArrayList;

public class MipsCount {
    public static Integer spOffset = 0;
    public static boolean mainFlag = false;

    // t类寄存器：8-15，24-25
    public static Integer tType = 8;
    public static Integer farTType = 8;

    public static Integer getTType() {
        while (!RegPool.regs[tType]) {  // 当前寄存器正在被用
            tType++;
            if (tType == 16) {
                tType = 24;
            } else if (tType == 26) {
                tType = 8;
            }
        }
        RegPool.regs[tType] = false;
        return tType;
    }

    public static boolean judgeTYpeOptimized() {
        for (int i = 8; i <= 25; ) {
            if (RegPool.regs[i]) {
                return true;
            }
            i = i + 1;
            if (i == 16 && !Optimize.optimize10) {
                i = 24;
            }
        }
        // 全满了
        return false;
    }

    public static Integer gettTYpeOptimized() {
        for (int i = 8; i <= 25; ) {
            if (RegPool.regs[i]) {
                RegPool.regs[i] = false;
                farTType = i + 1;
                if (farTType == 16 && !Optimize.optimize10) {
                    farTType = 24;
                } else if (farTType == 26) {
                    farTType = 8;
                }
                return i;
            }
            i = i + 1;
            if (i == 16 && !Optimize.optimize10) {
                i = 24;
            }
        }
        // 全满了
        return null;
    }

    public static ArrayList<mipsInstruction> clearRegPool() {
        ArrayList<mipsInstruction> instructions = new ArrayList<>();
        for (int i = 0; i < 32; i++) {
            if (!RegPool.regs[i]) {
                RegPool.regs[i] = true;
                if (RegPool.regSymbol.containsKey(i)) {
                    MipsSymbol mipsSymbol = RegPool.regSymbol.get(i);
                    if (mipsSymbol.isGlobal) {
                        SW sw = new SW(i, mipsSymbol.irName);
                        instructions.add(sw);
                    } else {
                        SW sw = new SW(i, mipsSymbol.base, mipsSymbol.offset);
                        instructions.add(sw);
                    }
                    mipsSymbol.isStored = false;
                    mipsSymbol.reg = null;
                }
            }
        }
        RegPool.regSymbol.clear();
        Operation.except.clear();
        return instructions;
    }
    public static Integer divCount = 0;
}
