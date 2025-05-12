package backend.Process;

import backend.Instructions.SYSCALL;
import backend.Instructions.load.LI;
import backend.Instructions.mipsInstruction;
import backend.Instructions.store.SW;
import backend.Optimize;
import backend.Symbol.MipsSymbol;
import backend.Symbol.MipsSymbolTable;
import middle.Class.Instruction.InputInst;

import java.util.ArrayList;

public class processInputInst extends mipsInstruction {
    public static ArrayList<mipsInstruction> processInputInst(InputInst inputInst, MipsSymbolTable mipsSymbolTable) {
        ArrayList<mipsInstruction> instructions = new ArrayList<>();
        LI li = null;
        if (inputInst.irType.getNum() == 32) {
            li = new LI(2, 5);    // li $v0, 5
        } else {
            li = new LI(2, 12);    // li $v0, 12
        }
        SYSCALL syscall = new SYSCALL();
        MipsSymbol mipsSymbol = new MipsSymbol(inputInst.getResName());
        mipsSymbolTable.addSymbol(mipsSymbol);
        SW sw = new SW(2, mipsSymbol.base, mipsSymbol.offset);
        instructions.add(li);
        instructions.add(syscall);
        instructions.add(sw);
        return instructions;
    }

}
