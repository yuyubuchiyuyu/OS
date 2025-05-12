package backend.Process;

import backend.Instructions.calculate.XORI;
import backend.Instructions.compare.SLT;
import backend.Instructions.compare.SLTI;
import backend.Instructions.load.LI;
import backend.Instructions.load.LW;
import backend.Instructions.logic.OR;
import backend.Instructions.mipsInstruction;
import backend.Instructions.store.SW;
import backend.MipsCount;
import backend.Operation;
import backend.Optimize;
import backend.RegPool;
import backend.Symbol.MipsSymbol;
import backend.Symbol.MipsSymbolTable;
import middle.Class.Instruction.CompareInst;

import java.util.ArrayList;

public class processCompareInst {
    public static ArrayList<mipsInstruction> processCompareInst(CompareInst compareInst, MipsSymbolTable mipsSymbolTable) {
        if (Optimize.optimize7) {
            return optimized(compareInst, mipsSymbolTable);
        } else {
            return noOptimized(compareInst, mipsSymbolTable);
        }
    }

    private static ArrayList<mipsInstruction> optimized(CompareInst compareInst, MipsSymbolTable mipsSymbolTable) {
        ArrayList<mipsInstruction> instructions = new ArrayList<>();
        if (compareInst.num1 != null && compareInst.num2 != null) {
            System.out.println("两数比较错误");
        }
        if (compareInst.type == CompareInst.CompareType.slt &&
                compareInst.name1 != null && compareInst.num2 != null) {    // name1 < num2
            MipsSymbol mipsSymbol1 = mipsSymbolTable.search(compareInst.name1);
            Integer reg1 = Operation.fromSymbolGetSymbolValue(mipsSymbol1, instructions);
            Operation.except.add(reg1);
            Integer resReg = Operation.getTTYpeOptimized(instructions);
            Operation.except.add(resReg);
            instructions.addAll(processRegNumOperator(compareInst.type, resReg, reg1, compareInst.num2));
            Operation.fromNameCreateNewSymbol(compareInst.resName, mipsSymbolTable, resReg);
            return instructions;
        }
        if (compareInst.type == CompareInst.CompareType.sgt
                && compareInst.num1 != null && compareInst.name2 != null) { // num1 > name2 <=> name2 < num1
            MipsSymbol mipsSymbol2 = mipsSymbolTable.search(compareInst.name2);
            Integer reg2 = Operation.fromSymbolGetSymbolValue(mipsSymbol2, instructions);
            Operation.except.add(reg2);
            Integer resReg = Operation.getTTYpeOptimized(instructions);
            Operation.except.add(resReg);
            instructions.addAll(processRegNumOperator(compareInst.type, resReg, reg2, compareInst.num1));
            Operation.fromNameCreateNewSymbol(compareInst.resName, mipsSymbolTable, resReg);
            return instructions;
        }
        Integer reg1 = null;
        boolean reg1Flag = false;
        if (compareInst.name1 != null) {
            MipsSymbol mipsSymbol1 = mipsSymbolTable.search(compareInst.name1);
            reg1 = Operation.fromSymbolGetSymbolValue(mipsSymbol1, instructions);
        } else {     // binaryOperator.num1 != null
            if(Optimize.optimize11 && compareInst.num1.equals(0)){
                reg1 = 0;
            } else {
                reg1 = Operation.getTTYpeOptimized(instructions);
                LI li = new LI(reg1, compareInst.num1);
                instructions.add(li);
                reg1Flag = true;
            }
        }
        Operation.except.add(reg1);
        Integer reg2 = null;
        boolean reg2Flag = false;
        if (compareInst.name2 != null) {
            MipsSymbol mipsSymbol2 = mipsSymbolTable.search(compareInst.name2);
            reg2 = Operation.fromSymbolGetSymbolValue(mipsSymbol2, instructions);
        } else {     // binaryOperator.num2 != null
            if(Optimize.optimize11 && compareInst.num2.equals(0)){
                reg2 = 0;
            } else {
                reg2 = Operation.getTTYpeOptimized(instructions);
                LI li = new LI(reg2, compareInst.num2);
                instructions.add(li);
                reg2Flag = true;
            }
        }
        Operation.except.add(reg2);
        Integer resReg = Operation.getTTYpeOptimized(instructions);
        Operation.except.add(resReg);
        instructions.addAll(processRegRegOperator(compareInst.type, resReg, reg1, reg2));
        Operation.fromNameCreateNewSymbol(compareInst.resName, mipsSymbolTable, resReg);
        if (reg1Flag) {
            RegPool.regs[reg1] = true;
        }
        if (reg2Flag) {
            RegPool.regs[reg2] = true;
        }
        return instructions;
    }

    private static ArrayList<mipsInstruction> noOptimized(CompareInst compareInst, MipsSymbolTable mipsSymbolTable) {
        ArrayList<mipsInstruction> instructions = new ArrayList<>();
        if (compareInst.num1 != null && compareInst.num2 != null) {
            System.out.println("两数比较错误");
        }
        if (compareInst.type == CompareInst.CompareType.slt &&
                compareInst.name1 != null && compareInst.num2 != null) {    // name1 < num2
            Integer reg1 = MipsCount.getTType();
            MipsSymbol mipsSymbol1 = mipsSymbolTable.search(compareInst.name1);
            LW lw = null;
            if (mipsSymbol1.isGlobal) {
                lw = new LW(reg1, mipsSymbol1.irName);
            } else {
                Integer base = mipsSymbol1.base;
                Integer offset = mipsSymbol1.offset;
                lw = new LW(reg1, base, offset);
            }
            instructions.add(lw);
            Integer resReg = MipsCount.getTType();
            instructions.addAll(processRegNumOperator(compareInst.type, resReg, reg1, compareInst.num2));
            MipsSymbol resSymbol = new MipsSymbol(compareInst.resName);
            mipsSymbolTable.addSymbol(resSymbol);
            SW sw = new SW(resReg, resSymbol.base, resSymbol.offset);
            instructions.add(sw);
            RegPool.regs[reg1] = true;
            RegPool.regs[resReg] = true;
            return instructions;
        }
        if (compareInst.type == CompareInst.CompareType.sgt
                && compareInst.num1 != null && compareInst.name2 != null) { // num1 > name2 <=> name2 < num1
            Integer reg2 = MipsCount.getTType();
            MipsSymbol mipsSymbol2 = mipsSymbolTable.search(compareInst.name2);
            LW lw = null;
            if (mipsSymbol2.isGlobal) {
                lw = new LW(reg2, mipsSymbol2.irName);
            } else {
                Integer base = mipsSymbol2.base;
                Integer offset = mipsSymbol2.offset;
                lw = new LW(reg2, base, offset);
            }
            instructions.add(lw);
            Integer resReg = MipsCount.getTType();
            instructions.addAll(processRegNumOperator(compareInst.type, resReg, reg2, compareInst.num1));
            MipsSymbol resSymbol = new MipsSymbol(compareInst.resName);
            mipsSymbolTable.addSymbol(resSymbol);
            SW sw = new SW(resReg, resSymbol.base, resSymbol.offset);
            instructions.add(sw);
            RegPool.regs[reg2] = true;
            RegPool.regs[resReg] = true;
            return instructions;
        }
        Integer reg1 = MipsCount.getTType();
        if (compareInst.name1 != null) {
            MipsSymbol mipsSymbol1 = mipsSymbolTable.search(compareInst.name1);
            LW lw1 = null;
            if (mipsSymbol1.isGlobal) {
                lw1 = new LW(reg1, mipsSymbol1.irName);
            } else {
                Integer base = mipsSymbol1.base;
                Integer offset = mipsSymbol1.offset;
                lw1 = new LW(reg1, base, offset);
            }
            instructions.add(lw1);
        } else {     // binaryOperator.num1 != null
            LI li = new LI(reg1, compareInst.num1);
            instructions.add(li);
        }
        Integer reg2 = MipsCount.getTType();
        if (compareInst.name2 != null) {
            MipsSymbol mipsSymbol2 = mipsSymbolTable.search(compareInst.name2);
            LW lw2 = null;
            if (mipsSymbol2.isGlobal) {
                lw2 = new LW(reg2, mipsSymbol2.irName);
            } else {
                Integer base = mipsSymbol2.base;
                Integer offset = mipsSymbol2.offset;
                lw2 = new LW(reg2, base, offset);
            }
            instructions.add(lw2);
        } else {     // binaryOperator.num2 != null
            LI li = new LI(reg2, compareInst.num2);
            instructions.add(li);
        }
        Integer resReg = MipsCount.getTType();
        instructions.addAll(processRegRegOperator(compareInst.type, resReg, reg1, reg2));
        MipsSymbol resSymbol = new MipsSymbol(compareInst.resName);
        mipsSymbolTable.addSymbol(resSymbol);
        SW sw = new SW(resReg, resSymbol.base, resSymbol.offset);
        instructions.add(sw);
        RegPool.regs[reg1] = true;
        RegPool.regs[reg2] = true;
        RegPool.regs[resReg] = true;
        return instructions;
    }

    private static ArrayList<mipsInstruction> processRegNumOperator(CompareInst.CompareType type, Integer resReg, Integer reg, Integer num) {
        ArrayList<mipsInstruction> instructions = new ArrayList<>();
        SLTI slti = new SLTI(resReg, reg, num);
        instructions.add(slti);
        return instructions;
    }

    private static ArrayList<mipsInstruction> processRegRegOperator(CompareInst.CompareType type, Integer resReg, Integer reg1, Integer reg2) {
        if (Optimize.optimize9) {
            return OptimizedprocessRegRegOperator(type, resReg, reg1, reg2);
        } else {
            return NoOptimizedprocessRegRegOperator(type, resReg, reg1, reg2);
        }
    }

    private static ArrayList<mipsInstruction> OptimizedprocessRegRegOperator(CompareInst.CompareType type, Integer resReg, Integer reg1, Integer reg2) {
        Operation.except.add(resReg);
        Operation.except.add(reg1);
        Operation.except.add(reg2);
        ArrayList<mipsInstruction> instructions = new ArrayList<>();
        switch (type) {
            case eq:    // reg1 == reg2     <=>     !(reg1 < reg2 || reg1 > reg2)   <=>     !(reg1 < reg2 || reg2 < reg1)
                Integer regTempEQ1;
                if (Optimize.optimize7) {
                    regTempEQ1 = Operation.getTTYpeOptimized(instructions);
                } else {
                    regTempEQ1 = MipsCount.getTType();
                }
                Operation.except.add(regTempEQ1);
                SLT sltEQ1 = new SLT(regTempEQ1, reg1, reg2);
                SLT sltEQ2 = new SLT(resReg, reg2, reg1);
                OR orEQ = new OR(resReg, regTempEQ1, resReg);
                XORI xoriEQ = new XORI(resReg, resReg, 1);
                instructions.add(sltEQ1);
                instructions.add(sltEQ2);
                instructions.add(orEQ);
                instructions.add(xoriEQ);
                RegPool.regs[regTempEQ1] = true;
                break;
            case ne:    // reg1 != reg2     <=>     reg1 < reg2 || reg1 > reg2
                Integer regTempNE1;
                if (Optimize.optimize7) {
                    regTempNE1 = Operation.getTTYpeOptimized(instructions);
                } else {
                    regTempNE1 = MipsCount.getTType();
                }
                Operation.except.add(regTempNE1);
                SLT sltNE1 = new SLT(regTempNE1, reg1, reg2);
                SLT sltNE2 = new SLT(resReg, reg2, reg1);
                OR orNE = new OR(resReg, regTempNE1, resReg);
                instructions.add(sltNE1);
                instructions.add(sltNE2);
                instructions.add(orNE);
                RegPool.regs[regTempNE1] = true;
                break;
            case sgt:   // reg1 > reg2     <=>      reg2 < reg1
                SLT sltSGT = new SLT(resReg, reg2, reg1);
                instructions.add(sltSGT);
                break;
            case sge:   // reg1 >= reg2     <=>     !(reg1 < reg2)
                SLT sltSGE = new SLT(resReg, reg1, reg2);
                XORI xoriSGE = new XORI(resReg, resReg, 1);
                instructions.add(sltSGE);
                instructions.add(xoriSGE);
                break;
            case slt:   // reg1 < reg2
                SLT sltSLT = new SLT(resReg, reg1, reg2);
                instructions.add(sltSLT);
                break;
            case sle:   // reg1 <= reg2     <=>     !(reg1 > reg2)      <=>     !(reg2 < reg1)
                SLT sltSLE = new SLT(resReg, reg2, reg1);
                XORI xoriSLE = new XORI(resReg, resReg, 1);
                instructions.add(sltSLE);
                instructions.add(xoriSLE);
                break;
            default:
                throw new IllegalArgumentException("Unsupported CompareType: " + type);
        }
        return instructions;
    }

    private static ArrayList<mipsInstruction> NoOptimizedprocessRegRegOperator(CompareInst.CompareType type, Integer resReg, Integer reg1, Integer reg2) {
        ArrayList<mipsInstruction> instructions = new ArrayList<>();
        switch (type) {
            case eq:    // reg1 == reg2     <=>     !(reg1 < reg2 || reg1 > reg2)   <=>     !(reg1 < reg2 || reg2 < reg1)
                Integer regTempEQ1 = MipsCount.getTType();
                Integer regTempEQ2 = MipsCount.getTType();
                Integer regTempEQ3 = MipsCount.getTType();
                SLT sltEQ1 = new SLT(regTempEQ1, reg1, reg2);
                SLT sltEQ2 = new SLT(regTempEQ2, reg2, reg1);
                OR orEQ = new OR(regTempEQ3, regTempEQ1, regTempEQ2);
                XORI xoriEQ = new XORI(resReg, regTempEQ3, 1);
                instructions.add(sltEQ1);
                instructions.add(sltEQ2);
                instructions.add(orEQ);
                instructions.add(xoriEQ);
                RegPool.regs[regTempEQ1] = true;
                RegPool.regs[regTempEQ2] = true;
                RegPool.regs[regTempEQ3] = true;
                break;
            case ne:    // reg1 != reg2     <=>     reg1 < reg2 || reg1 > reg2
                Integer regTempNE1 = MipsCount.getTType();
                Integer regTempNE2 = MipsCount.getTType();
                SLT sltNE1 = new SLT(regTempNE1, reg1, reg2);
                SLT sltNE2 = new SLT(regTempNE2, reg2, reg1);
                OR orNE = new OR(resReg, regTempNE1, regTempNE2);
                instructions.add(sltNE1);
                instructions.add(sltNE2);
                instructions.add(orNE);
                RegPool.regs[regTempNE1] = true;
                RegPool.regs[regTempNE2] = true;
                break;
            case sgt:   // reg1 > reg2     <=>      reg2 < reg1
                SLT sltSGT = new SLT(resReg, reg2, reg1);
                instructions.add(sltSGT);
                break;
            case sge:   // reg1 >= reg2     <=>     !(reg1 < reg2)
                Integer resTempSGE = MipsCount.getTType();
                SLT sltSGE = new SLT(resTempSGE, reg1, reg2);
                XORI xoriSGE = new XORI(resReg, resTempSGE, 1);
                instructions.add(sltSGE);
                instructions.add(xoriSGE);
                RegPool.regs[resTempSGE] = true;
                break;
            case slt:   // reg1 < reg2
                SLT sltSLT = new SLT(resReg, reg1, reg2);
                instructions.add(sltSLT);
                break;
            case sle:   // reg1 <= reg2     <=>     !(reg1 > reg2)      <=>     !(reg2 < reg1)
                Integer resTempSLE = MipsCount.getTType();
                SLT sltSLE = new SLT(resTempSLE, reg2, reg1);
                XORI xoriSLE = new XORI(resReg, resTempSLE, 1);
                instructions.add(sltSLE);
                instructions.add(xoriSLE);
                RegPool.regs[resTempSLE] = true;
                break;
            default:
                throw new IllegalArgumentException("Unsupported CompareType: " + type);
        }
        return instructions;
    }
}
