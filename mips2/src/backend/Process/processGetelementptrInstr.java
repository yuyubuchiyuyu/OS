package backend.Process;

import backend.Instructions.MOVE;
import backend.Instructions.calculate.ADD;
import backend.Instructions.calculate.ADDI;
import backend.Instructions.calculate.SLL;
import backend.Instructions.load.LA;
import backend.Instructions.load.LW;
import backend.Instructions.mipsInstruction;
import backend.Instructions.store.SW;
import backend.MipsCount;
import backend.Operation;
import backend.Optimize;
import backend.RegPool;
import backend.Symbol.MipsSymbol;
import backend.Symbol.MipsSymbolTable;
import middle.Class.Instruction.GetelementptrInstr;

import java.util.ArrayList;

public class processGetelementptrInstr {
    public static ArrayList<mipsInstruction> processGetelementptrInstr(GetelementptrInstr getelementptrInstr, MipsSymbolTable mipsSymbolTable) {
        if (Optimize.optimize7) {
            return optimized(getelementptrInstr, mipsSymbolTable);
        } else {
            return noOptimized(getelementptrInstr, mipsSymbolTable);
        }
    }

    private static ArrayList<mipsInstruction> optimized(GetelementptrInstr getelementptrInstr, MipsSymbolTable mipsSymbolTable) {
        ArrayList<mipsInstruction> instructions = new ArrayList<>();
        MipsSymbol mipsSymbol = mipsSymbolTable.search(getelementptrInstr.LLVMSymbol.irName);
        if(mipsSymbol.isStored){
            if(mipsSymbol.isGlobal){
                SW sw = new SW(mipsSymbol.reg,mipsSymbol.irName);
                instructions.add(sw);
            }else{
                SW sw = new SW(mipsSymbol.reg,mipsSymbol.base,mipsSymbol.offset);
                instructions.add(sw);
            }
            RegPool.regs[mipsSymbol.reg] = true;
            RegPool.regSymbol.remove(mipsSymbol.reg);
            mipsSymbol.isStored = false;
            mipsSymbol.reg = null;
        }
        Integer reg0 = Operation.getTTYpeOptimized(instructions);
        Operation.except.add(reg0);
        Integer reg = Operation.getTTYpeOptimized(instructions);
        Operation.except.add(reg);
        if (getelementptrInstr.offset != null) {    // 偏移为数字
            if (mipsSymbol.isAddress) {
                LW lw = new LW(reg0, mipsSymbol.base, mipsSymbol.offset); //得到基地址
                ADDI addi = new ADDI(reg, reg0, 4 * getelementptrInstr.offset);   // 得到地址
                instructions.add(lw);
                instructions.add(addi);
            } else if (mipsSymbol.isGlobal) {
                LA la = new LA(reg0, mipsSymbol.irName);     // 得到基地址
                ADDI addi = new ADDI(reg, reg0, 4 * getelementptrInstr.offset);   // 得到地址
                instructions.add(la);
                instructions.add(addi);
            } else {
                MOVE move = new MOVE(reg0, mipsSymbol.base);       // 得到基地址
                ADDI addi = new ADDI(reg, reg0, mipsSymbol.offset + 4 * getelementptrInstr.offset);    // 得到地址
                instructions.add(move);
                instructions.add(addi);
            }
        } else {    // getelementptrInstr.offsetName != null
            MipsSymbol offsetSymbol = mipsSymbolTable.search(getelementptrInstr.offsetName);    // 偏移值变量
            Integer reg1 = Operation.getTTYpeOptimized(instructions);
            Operation.except.add(reg1);
            if (offsetSymbol.isStored) {
                SLL sll = new SLL(reg1, offsetSymbol.reg, 2);     //  得到偏移值
                instructions.add(sll);
            } else if (offsetSymbol.isGlobal) {
                LW lw = new LW(reg1, offsetSymbol.irName);
                SLL sll = new SLL(reg1, reg1, 2);     //  得到偏移值
                instructions.add(lw);
                instructions.add(sll);
            } else {
                LW lw = new LW(reg1, offsetSymbol.base, offsetSymbol.offset);
                SLL sll = new SLL(reg1, reg1, 2);    // 得到偏移值
                instructions.add(lw);
                instructions.add(sll);
            }
            if (mipsSymbol.isAddress) {
                LW lw = new LW(reg0, mipsSymbol.base, mipsSymbol.offset); // 得到基地址
                ADD add = new ADD(reg, reg0, reg1);    // 得到地址
                instructions.add(lw);
                instructions.add(add);
            } else if (mipsSymbol.isGlobal) {
                LA la = new LA(reg0, mipsSymbol.irName);     // 得到基地址
                ADD add = new ADD(reg, reg0, reg1);    // 得到地址
                instructions.add(la);
                instructions.add(add);
            } else {
                MOVE move = new MOVE(reg0, mipsSymbol.base);       // 得到基地址
                ADDI addi = new ADDI(reg1, reg1, mipsSymbol.offset);
                ADD add = new ADD(reg, reg0, reg1);    // 得到地址
                instructions.add(move);
                instructions.add(addi);
                instructions.add(add);
            }
            RegPool.regs[reg1] = true;
        }
        /*
        MipsSymbol newSymbol = new MipsSymbol(getelementptrInstr.getResName());
        newSymbol.isAddress = true;
        mipsSymbolTable.addSymbol(newSymbol);
        SW sw = new SW(reg, newSymbol.base, newSymbol.offset);
        instructions.add(sw);
        RegPool.regs[reg] = true;
         */
        Operation.fromNameCreateNewSymbol(getelementptrInstr.getResName(),mipsSymbolTable,reg);
        MipsSymbol newSymbol = mipsSymbolTable.search(getelementptrInstr.getResName());
        newSymbol.isAddress = true;
        RegPool.regs[reg0] = true;
        return instructions;
    }

    private static ArrayList<mipsInstruction> noOptimized(GetelementptrInstr getelementptrInstr, MipsSymbolTable mipsSymbolTable) {
        ArrayList<mipsInstruction> instructions = new ArrayList<>();
        MipsSymbol mipsSymbol = mipsSymbolTable.search(getelementptrInstr.LLVMSymbol.irName);
        Integer reg0 = MipsCount.getTType();
        Integer reg1 = MipsCount.getTType();
        Integer reg = MipsCount.getTType();
        if (getelementptrInstr.offset != null) {    // 偏移为数字
            if (mipsSymbol.isAddress) {
                LW lw = new LW(reg0, mipsSymbol.base, mipsSymbol.offset); //得到基地址
                ADDI addi = new ADDI(reg, reg0, 4 * getelementptrInstr.offset);   // 得到地址
                instructions.add(lw);
                instructions.add(addi);
            } else if (mipsSymbol.isGlobal) {
                LA la = new LA(reg0, mipsSymbol.irName);     // 得到基地址
                ADDI addi = new ADDI(reg, reg0, 4 * getelementptrInstr.offset);   // 得到地址
                instructions.add(la);
                instructions.add(addi);
            } else {
                MOVE move = new MOVE(reg0, mipsSymbol.base);       // 得到基地址
                ADDI addi = new ADDI(reg, reg0, mipsSymbol.offset + 4 * getelementptrInstr.offset);    // 得到地址
                instructions.add(move);
                instructions.add(addi);
            }
        } else {    // getelementptrInstr.offsetName != null
            MipsSymbol offsetSymbol = mipsSymbolTable.search(getelementptrInstr.offsetName);    // 偏移值变量
            if (offsetSymbol.isGlobal) {
                LW lw = new LW(reg1, offsetSymbol.irName);
                SLL sll = new SLL(reg1, reg1, 2);     //  得到偏移值
                instructions.add(lw);
                instructions.add(sll);
            } else {
                LW lw = new LW(reg1, offsetSymbol.base, offsetSymbol.offset);
                SLL sll = new SLL(reg1, reg1, 2);    // 得到偏移值
                instructions.add(lw);
                instructions.add(sll);
            }
            if (mipsSymbol.isAddress) {
                LW lw = new LW(reg0, mipsSymbol.base, mipsSymbol.offset); // 得到基地址
                ADD add = new ADD(reg, reg0, reg1);    // 得到地址
                instructions.add(lw);
                instructions.add(add);
            } else if (mipsSymbol.isGlobal) {
                LA la = new LA(reg0, mipsSymbol.irName);     // 得到基地址
                ADD add = new ADD(reg, reg0, reg1);    // 得到地址
                instructions.add(la);
                instructions.add(add);
            } else {
                MOVE move = new MOVE(reg0, mipsSymbol.base);       // 得到基地址
                ADDI addi = new ADDI(reg1, reg1, mipsSymbol.offset);
                ADD add = new ADD(reg, reg0, reg1);    // 得到地址
                instructions.add(move);
                instructions.add(addi);
                instructions.add(add);
            }
        }
        MipsSymbol newSymbol = new MipsSymbol(getelementptrInstr.getResName());
        newSymbol.isAddress = true;
        mipsSymbolTable.addSymbol(newSymbol);
        SW sw = new SW(reg, newSymbol.base, newSymbol.offset);
        instructions.add(sw);
        RegPool.regs[reg0] = true;
        RegPool.regs[reg1] = true;
        RegPool.regs[reg] = true;
        return instructions;
    }
}
