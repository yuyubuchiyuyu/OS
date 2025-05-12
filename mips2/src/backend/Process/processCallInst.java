package backend.Process;

import backend.Instructions.MOVE;
import backend.Instructions.calculate.ADDI;
import backend.Instructions.jump.JAL;
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
import middle.Class.Instruction.CallInst;
import middle.Class.IrType.IrType;

import java.util.ArrayList;

public class processCallInst {
    public static ArrayList<mipsInstruction> processCallInst(CallInst callInst, MipsSymbolTable mipsSymbolTable) {
        if (Optimize.optimize7) {
            return optimized(callInst, mipsSymbolTable);
        } else {
            return noOptimized(callInst, mipsSymbolTable);
        }
    }

    private static ArrayList<mipsInstruction> optimized(CallInst callInst, MipsSymbolTable mipsSymbolTable) {
        ArrayList<mipsInstruction> instructions = new ArrayList<>();
        instructions.addAll(MipsCount.clearRegPool());
        Integer offset = (callInst.argumentName.size() - 4) * 4;
        for (int i = 0; i < callInst.argumentName.size(); i++) {
            String argumentName = callInst.argumentName.get(i);
            MipsSymbol mipsSymbol = mipsSymbolTable.search(argumentName);
            if (i < 4) {    // $a0=$4,$a1=$5,$a2=$6,$a3=$7
                if (mipsSymbol == null) {   // 常量
                    int value = 0;
                    for (int j = 0; j < argumentName.length(); j++) {
                        value = 10 * value + (argumentName.charAt(j) - '0');
                    }
                    LI li = new LI(i + 4, value);
                    instructions.add(li);
                } else {  // 变量名
                    if (mipsSymbol.isGlobal) {
                        LW lw = new LW(i + 4, mipsSymbol.irName);
                        instructions.add(lw);
                    } else {
                        LW lw = new LW(i + 4, mipsSymbol.base, mipsSymbol.offset);
                        instructions.add(lw);
                    }
                }
            } else {    // 多余参数压入栈
                if (i == 4) {
                    ADDI addi1 = new ADDI(29, 29, -offset);
                    instructions.add(addi1);
                }
                Integer reg = Operation.getTTYpeOptimized(instructions);
                if (mipsSymbol == null) {   // 常量
                    int value = 0;
                    for (int j = 0; j < argumentName.length(); j++) {
                        value = 10 * value + (argumentName.charAt(j) - '0');
                    }
                    if(Optimize.optimize11 && value==0){
                        RegPool.regs[reg] = true;
                        reg = 0;
                    }else{
                        LI li = new LI(reg, value);
                        instructions.add(li);
                    }
                } else {  // 变量名
                    LW lw = null;
                    if (mipsSymbol.isGlobal) {
                        lw = new LW(reg, mipsSymbol.irName);
                    } else {
                        lw = new LW(reg, mipsSymbol.base, mipsSymbol.offset + offset);
                    }
                    instructions.add(lw);
                }
                SW sw = new SW(reg, 29, i * 4 - 16);
                instructions.add(sw);
                RegPool.regs[reg] = true;
            }
        }
        JAL jal = new JAL(callInst.funcName);
        instructions.add(jal);
        if (callInst.argumentName.size() > 4) {
            ADDI addi2 = new ADDI(29, 29, offset);
            instructions.add(addi2);
        }
        //  $v0 = $2
        if (callInst.irType.getId() != IrType.TypeID.VoidTyID) {
            Integer reg = Operation.getTTYpeOptimized(instructions);
            MOVE move = new MOVE(reg, 2);
            instructions.add(move);
            Operation.fromNameCreateNewSymbol(callInst.resName, mipsSymbolTable, reg);
        }
        return instructions;
    }

    private static ArrayList<mipsInstruction> noOptimized(CallInst callInst, MipsSymbolTable mipsSymbolTable) {
        ArrayList<mipsInstruction> instructions = new ArrayList<>();
        Integer offset = (callInst.argumentName.size() - 4) * 4;
        for (int i = 0; i < callInst.argumentName.size(); i++) {
            String argumentName = callInst.argumentName.get(i);
            MipsSymbol mipsSymbol = mipsSymbolTable.search(argumentName);
            if (i < 4) {    // $a0=$4,$a1=$5,$a2=$6,$a3=$7
                if (mipsSymbol == null) {   // 常量
                    int value = 0;
                    for (int j = 0; j < argumentName.length(); j++) {
                        value = 10 * value + (argumentName.charAt(j) - '0');
                    }
                    LI li = new LI(i + 4, value);
                    instructions.add(li);
                } else {  // 变量名
                    LW lw = null;
                    if (mipsSymbol.isGlobal) {
                        lw = new LW(i + 4, mipsSymbol.irName);
                    } else {
                        lw = new LW(i + 4, mipsSymbol.base, mipsSymbol.offset);
                    }
                    instructions.add(lw);
                }
            } else {    // 多余参数压入栈
                if (i == 4) {
                    ADDI addi1 = new ADDI(29, 29, -offset);
                    instructions.add(addi1);
                }
                Integer reg = MipsCount.getTType();
                if (mipsSymbol == null) {   // 常量
                    int value = 0;
                    for (int j = 0; j < argumentName.length(); j++) {
                        value = 10 * value + (argumentName.charAt(j) - '0');
                    }
                    LI li = new LI(reg, value);
                    instructions.add(li);
                } else {  // 变量名
                    LW lw = null;
                    if (mipsSymbol.isGlobal) {
                        lw = new LW(reg, mipsSymbol.irName);
                        instructions.add(lw);
                    } else {
                        lw = new LW(reg, mipsSymbol.base, mipsSymbol.offset + offset);
                        instructions.add(lw);
                    }
                }
                SW sw = new SW(reg, 29, i * 4 - 16);
                instructions.add(sw);
                RegPool.regs[reg] = true;
            }
        }
        JAL jal = new JAL(callInst.funcName);
        instructions.add(jal);
        if (callInst.argumentName.size() > 4) {
            ADDI addi2 = new ADDI(29, 29, offset);
            instructions.add(addi2);
        }
        //  $v0 = $2
        if (callInst.irType.getId() != IrType.TypeID.VoidTyID) {
            Integer reg = MipsCount.getTType();
            MOVE move = new MOVE(reg, 2);
            MipsSymbol mipsSymbol = new MipsSymbol(callInst.resName);
            mipsSymbolTable.addSymbol(mipsSymbol);
            SW sw = new SW(reg, mipsSymbol.base, mipsSymbol.offset);
            instructions.add(move);
            instructions.add(sw);
            RegPool.regs[reg] = true;
        }
        return instructions;
    }
}
