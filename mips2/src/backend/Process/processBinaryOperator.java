package backend.Process;

import backend.Instructions.LABEL;
import backend.Instructions.MOVE;
import backend.Instructions.calculate.*;
import backend.Instructions.load.LI;
import backend.Instructions.load.LW;
import backend.Instructions.logic.*;
import backend.Instructions.mipsInstruction;
import backend.Instructions.store.SW;
import backend.MipsCount;
import backend.Operation;
import backend.Optimize;
import backend.RegPool;
import backend.Symbol.MipsSymbol;
import backend.Symbol.MipsSymbolTable;
import middle.Class.Instruction.BinaryOperator;

import java.util.ArrayList;

public class processBinaryOperator {
    public static ArrayList<mipsInstruction> processBinaryOperator(BinaryOperator binaryOperator, MipsSymbolTable mipsSymbolTable) {
        if (Optimize.optimize7) {
            return optimized(binaryOperator, mipsSymbolTable);
        } else {
            return noOptimized(binaryOperator, mipsSymbolTable);
        }
    }

    private static ArrayList<mipsInstruction> optimized(BinaryOperator binaryOperator, MipsSymbolTable mipsSymbolTable) {
        ArrayList<mipsInstruction> instructions = new ArrayList<>();
        if (binaryOperator.num1 != null && binaryOperator.num2 != null) {
            System.out.println("两数计算错误 " + binaryOperator.num1 + " " + binaryOperator.num2);
        }
        if ((binaryOperator.type == BinaryOperator.BinaryOperatorType.add ||
                binaryOperator.type == BinaryOperator.BinaryOperatorType.sub ||
                binaryOperator.type == BinaryOperator.BinaryOperatorType.mul ||
                (binaryOperator.type == BinaryOperator.BinaryOperatorType.sdiv && Optimize.optimize12) ||
                binaryOperator.type == BinaryOperator.BinaryOperatorType.and ||
                binaryOperator.type == BinaryOperator.BinaryOperatorType.or) &&
                binaryOperator.name1 != null && binaryOperator.num2 != null) {
            MipsSymbol mipsSymbol1 = mipsSymbolTable.search(binaryOperator.name1);
            Integer reg1 = Operation.fromSymbolGetSymbolValue(mipsSymbol1, instructions);
            Operation.except.add(reg1);
            Integer resReg = Operation.getTTYpeOptimized(instructions);
            Operation.except.add(resReg);
            instructions.addAll(processRegNumOperator(binaryOperator.type, resReg, reg1, binaryOperator.num2));
            Operation.fromNameCreateNewSymbol(binaryOperator.resName, mipsSymbolTable, resReg);
            return instructions;
        }
        if ((binaryOperator.type == BinaryOperator.BinaryOperatorType.add ||
                binaryOperator.type == BinaryOperator.BinaryOperatorType.mul)
                && binaryOperator.num1 != null && binaryOperator.name2 != null) {
            MipsSymbol mipsSymbol2 = mipsSymbolTable.search(binaryOperator.name2);
            Integer reg2 = Operation.fromSymbolGetSymbolValue(mipsSymbol2, instructions);
            Operation.except.add(reg2);
            Integer resReg = Operation.getTTYpeOptimized(instructions);
            Operation.except.add(resReg);
            instructions.addAll(processRegNumOperator(binaryOperator.type, resReg, reg2, binaryOperator.num1));
            Operation.fromNameCreateNewSymbol(binaryOperator.resName, mipsSymbolTable, resReg);
            return instructions;
        }
        Integer reg1 = null;
        boolean reg1Flag = false;
        if (binaryOperator.name1 != null) {
            MipsSymbol mipsSymbol1 = mipsSymbolTable.search(binaryOperator.name1);
            reg1 = Operation.fromSymbolGetSymbolValue(mipsSymbol1, instructions);
        } else {     // binaryOperator.num1 != null
            if (Optimize.optimize11 && binaryOperator.num1.equals(0)) {
                reg1 = 0;
            } else {
                reg1 = Operation.getTTYpeOptimized(instructions);
                LI li = new LI(reg1, binaryOperator.num1);
                instructions.add(li);
                reg1Flag = true;
            }
        }
        Operation.except.add(reg1);
        Integer reg2 = null;
        boolean reg2Flag = false;
        if (binaryOperator.name2 != null) {
            MipsSymbol mipsSymbol2 = mipsSymbolTable.search(binaryOperator.name2);
            reg2 = Operation.fromSymbolGetSymbolValue(mipsSymbol2, instructions);
        } else {     // binaryOperator.num2 != null
            if (Optimize.optimize11 && binaryOperator.num2.equals(0)) {
                reg2 = 0;
            } else {
                reg2 = Operation.getTTYpeOptimized(instructions);
                LI li = new LI(reg2, binaryOperator.num2);
                instructions.add(li);
                reg2Flag = true;
            }
        }
        Operation.except.add(reg2);
        Integer resReg = Operation.getTTYpeOptimized(instructions);
        Operation.except.add(resReg);
        instructions.addAll(processRegRegOperator(binaryOperator.type, resReg, reg1, reg2));
        Operation.fromNameCreateNewSymbol(binaryOperator.resName, mipsSymbolTable, resReg);
        if (reg1Flag) {
            RegPool.regs[reg1] = true;
        }
        if (reg2Flag) {
            RegPool.regs[reg2] = true;
        }
        return instructions;
    }


    private static ArrayList<mipsInstruction> noOptimized(BinaryOperator binaryOperator, MipsSymbolTable mipsSymbolTable) {
        ArrayList<mipsInstruction> instructions = new ArrayList<>();
        if (binaryOperator.num1 != null && binaryOperator.num2 != null) {
            System.out.println("两数计算错误 " + binaryOperator.num1 + " " + binaryOperator.num2);
        }
        if ((binaryOperator.type == BinaryOperator.BinaryOperatorType.add ||
                binaryOperator.type == BinaryOperator.BinaryOperatorType.sub ||
                binaryOperator.type == BinaryOperator.BinaryOperatorType.mul ||
                binaryOperator.type == BinaryOperator.BinaryOperatorType.and ||
                binaryOperator.type == BinaryOperator.BinaryOperatorType.or) &&
                binaryOperator.name1 != null && binaryOperator.num2 != null) {
            Integer reg1 = MipsCount.getTType();
            MipsSymbol mipsSymbol1 = mipsSymbolTable.search(binaryOperator.name1);
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
            instructions.addAll(processRegNumOperator(binaryOperator.type, resReg, reg1, binaryOperator.num2));
            MipsSymbol resSymbol = new MipsSymbol(binaryOperator.resName);
            mipsSymbolTable.addSymbol(resSymbol);
            SW sw = new SW(resReg, resSymbol.base, resSymbol.offset);
            instructions.add(sw);
            RegPool.regs[reg1] = true;
            RegPool.regs[resReg] = true;
            return instructions;
        }
        if ((binaryOperator.type == BinaryOperator.BinaryOperatorType.add ||
                binaryOperator.type == BinaryOperator.BinaryOperatorType.mul)
                && binaryOperator.num1 != null && binaryOperator.name2 != null) {
            Integer reg2 = MipsCount.getTType();
            MipsSymbol mipsSymbol2 = mipsSymbolTable.search(binaryOperator.name2);
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
            instructions.addAll(processRegNumOperator(binaryOperator.type, resReg, reg2, binaryOperator.num1));
            MipsSymbol resSymbol = new MipsSymbol(binaryOperator.resName);
            mipsSymbolTable.addSymbol(resSymbol);
            SW sw = new SW(resReg, resSymbol.base, resSymbol.offset);
            instructions.add(sw);
            RegPool.regs[reg2] = true;
            RegPool.regs[resReg] = true;
            return instructions;
        }
        Integer reg1 = MipsCount.getTType();
        if (binaryOperator.name1 != null) {
            MipsSymbol mipsSymbol1 = mipsSymbolTable.search(binaryOperator.name1);
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
            LI li = new LI(reg1, binaryOperator.num1);
            instructions.add(li);
        }
        Integer reg2 = MipsCount.getTType();
        if (binaryOperator.name2 != null) {
            MipsSymbol mipsSymbol2 = mipsSymbolTable.search(binaryOperator.name2);
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
            LI li = new LI(reg2, binaryOperator.num2);
            instructions.add(li);
        }
        Integer resReg = MipsCount.getTType();
        instructions.addAll(processRegRegOperator(binaryOperator.type, resReg, reg1, reg2));
        MipsSymbol resSymbol = new MipsSymbol(binaryOperator.resName);
        mipsSymbolTable.addSymbol(resSymbol);
        SW sw = new SW(resReg, resSymbol.base, resSymbol.offset);
        instructions.add(sw);
        RegPool.regs[reg1] = true;
        RegPool.regs[reg2] = true;
        RegPool.regs[resReg] = true;
        return instructions;
    }

    private static ArrayList<mipsInstruction> processRegNumOperator(BinaryOperator.BinaryOperatorType type, Integer resReg, Integer reg, Integer num) {
        Operation.except.add(resReg);
        Operation.except.add(reg);
        ArrayList<mipsInstruction> instructions = new ArrayList<>();
        switch (type) {
            case add:
                ADDI addiu = new ADDI(resReg, reg, num);
                instructions.add(addiu);
                break;
            case sub:
                ADDI sub = new ADDI(resReg, reg, -num);
                instructions.add(sub);
                break;
            case mul:
                if (Optimize.optimize6) {
                    ArrayList<mipsInstruction> instructions1 = new ArrayList<>();
                    // 移位运算
                    if (num == 0) {
                        LI li = new LI(resReg, 0);
                        instructions1.add(li);
                    } else if (num == 1) {
                        MOVE move = new MOVE(resReg, reg);
                        instructions1.add(move);
                    } else {
                        Integer numABS = Math.abs(num);
                        int shift = 0;
                        boolean lowest = true;
                        while (numABS > 0) {
                            if ((numABS & 1) == 1) {    // 最低位为1
                                if (lowest) {
                                    SLL sll = new SLL(resReg, reg, shift);
                                    instructions1.add(sll);
                                    lowest = false;
                                } else {
                                    Integer regTemp;
                                    if (Optimize.optimize7) {
                                        regTemp = Operation.getTTYpeOptimized(instructions);
                                    } else {
                                        regTemp = MipsCount.getTType();
                                    }
                                    SLL sll = new SLL(regTemp, reg, shift);
                                    ADD add = new ADD(resReg, resReg, regTemp);
                                    instructions1.add(sll);
                                    instructions1.add(add);
                                    RegPool.regs[regTemp] = true;
                                }
                            }
                            numABS = numABS >> 1;
                            shift++;
                        }
                        if (num < 0) {
                            SUB subMUL = new SUB(resReg, 0, resReg);
                            instructions1.add(subMUL);
                        }
                    }
                    // 正常运算
                    ArrayList<mipsInstruction> instructions2 = new ArrayList<>();
                    LI li = new LI(resReg, num);
                    MUL mul = new MUL(resReg, resReg, reg);
                    instructions2.add(li);
                    instructions2.add(mul);
                    if (instructions1.size() < 4) {
                        instructions.addAll(instructions1);
                    } else {
                        instructions.addAll(instructions2);
                    }
                } else {
                    LI li = new LI(resReg, num);
                    MUL mul = new MUL(resReg, resReg, reg);
                    instructions.add(li);
                    instructions.add(mul);
                }
                break;
            case sdiv:
                int numABS = Math.abs(num);
                if (numABS == 1) {
                    if(num==1){
                        instructions.add(new MOVE(resReg, reg));
                    }else{
                        instructions.add(new SUB(resReg,0,reg));
                    }
                } else if(numABS==2){
                    Integer regTemp0;   // 存被除数符号位
                    Integer regTemp1;   // 被除数的绝对值
                    if (Optimize.optimize7) {
                        regTemp0 = Operation.getTTYpeOptimized(instructions);
                        Operation.except.add(regTemp0);
                        regTemp1 = Operation.getTTYpeOptimized(instructions);
                        Operation.except.add(regTemp1);
                    } else {
                        regTemp0 = MipsCount.getTType();
                        regTemp1 = MipsCount.getTType();
                    }
                    instructions.add(new SRL(regTemp0, reg, 31));    // 记录被除数的符号位
                    instructions.add(new MOVE(regTemp1, reg));  //  拷贝被除数
                    instructions.add(new BEQ(regTemp0, 0, "div_start_" + MipsCount.divCount));  // 符号位是0，是正数
                    instructions.add(new SUB(regTemp1, 0, reg));    // 是负数，取绝对值
                    instructions.add(new LABEL("div_start_" + MipsCount.divCount));
                    instructions.add(new SRL(resReg,regTemp1,1));
                    if (num < 0) {
                        instructions.add(new SUB(resReg, 0, resReg));
                    }
                    instructions.add(new BEQ(regTemp0, 0, "div_end_" + MipsCount.divCount));
                    instructions.add(new SUB(resReg, 0, resReg));
                    instructions.add(new LABEL("div_end_" + MipsCount.divCount));
                    MipsCount.divCount++;
                    RegPool.regs[regTemp0] = true;
                    RegPool.regs[regTemp1] = true;
                }else {
                    Integer regTemp0;   // 存被除数符号位
                    Integer regTemp1;   // 被除数的绝对值
                    Integer regTemp2;
                    if (Optimize.optimize7) {
                        regTemp0 = Operation.getTTYpeOptimized(instructions);
                        Operation.except.add(regTemp0);
                        regTemp1 = Operation.getTTYpeOptimized(instructions);
                        Operation.except.add(regTemp1);
                        regTemp2 = Operation.getTTYpeOptimized(instructions);
                        Operation.except.add(regTemp2);
                    } else {
                        regTemp0 = MipsCount.getTType();
                        regTemp1 = MipsCount.getTType();
                        regTemp2 = MipsCount.getTType();
                    }
                    instructions.add(new SRL(regTemp0, reg, 31));    // 记录被除数的符号位
                    instructions.add(new MOVE(regTemp1, reg));  //  拷贝被除数
                    instructions.add(new BEQ(regTemp0, 0, "div_start_" + MipsCount.divCount));  // 符号位是0，是正数
                    instructions.add(new SUB(regTemp1, 0, reg));    // 是负数，取绝对值
                    instructions.add(new LABEL("div_start_" + MipsCount.divCount));
                    int l = 1;
                    long m = (long) (Math.pow(2, 32 + l) / numABS);
                    if (!(m * numABS <= (Math.pow(2, 32 + l) + Math.pow(2, l)) && m * numABS >= Math.pow(2, 32 + l))) {
                        m++;
                    }
                    instructions.add(new LI(regTemp2, (int)m));
                    // instructions.add(new LI(regTemp2, m, true));
                    instructions.add(new MULTU(regTemp1, regTemp2));
                    instructions.add(new MFHI(regTemp1));
                    instructions.add(new SRL(resReg, regTemp1, l));
                    if (num < 0) {
                        instructions.add(new SUB(resReg, 0, resReg));
                    }
                    instructions.add(new BEQ(regTemp0, 0, "div_end_" + MipsCount.divCount));
                    instructions.add(new SUB(resReg, 0, resReg));
                    instructions.add(new LABEL("div_end_" + MipsCount.divCount));
                    MipsCount.divCount++;
                    RegPool.regs[regTemp0] = true;
                    RegPool.regs[regTemp1] = true;
                    RegPool.regs[regTemp2] = true;
                }
                break;
            case and:
                ANDI andi = new ANDI(resReg, reg, num);
                instructions.add(andi);
                break;
            case or:
                ORI ori = new ORI(resReg, reg, num);
                instructions.add(ori);
                break;
            default:
                throw new IllegalArgumentException("Unsupported BinaryOperatorType: " + type);
        }
        return instructions;
    }

    private static ArrayList<mipsInstruction> processRegRegOperator(BinaryOperator.BinaryOperatorType type, Integer resReg, Integer reg1, Integer reg2) {
        Operation.except.add(resReg);
        Operation.except.add(reg1);
        Operation.except.add(reg2);
        ArrayList<mipsInstruction> instructions = new ArrayList<>();
        switch (type) {
            case add:
                ADD add = new ADD(resReg, reg1, reg2);
                instructions.add(add);
                break;
            case sub:
                SUB sub = new SUB(resReg, reg1, reg2);
                instructions.add(sub);
                break;
            case mul:
                MUL mul = new MUL(resReg, reg1, reg2);
                instructions.add(mul);
                break;
            case sdiv:  // 除法运算，商存入LO，MFLO 拷贝LO到rd
                DIV div1 = new DIV(reg1, reg2);
                MFLO mflo = new MFLO(resReg);
                instructions.add(div1);
                instructions.add(mflo);
                break;
            case srem:  // 除法运算，余数存入HI，MFHI 拷贝HI到rd
                DIV div2 = new DIV(reg1, reg2);
                MFHI mfhi = new MFHI(resReg);
                instructions.add(div2);
                instructions.add(mfhi);
                break;
            case and:
                AND and = new AND(resReg, reg1, reg2);
                instructions.add(and);
                break;
            case or:
                OR or = new OR(resReg, reg1, reg2);
                instructions.add(or);
                break;
            default:
                throw new IllegalArgumentException("Unsupported BinaryOperatorType: " + type);
        }
        return instructions;

    }
}
