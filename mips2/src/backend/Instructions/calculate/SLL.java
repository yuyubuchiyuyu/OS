package backend.Instructions.calculate;

import backend.Instructions.mipsInstruction;

public class SLL extends mipsInstruction {
    public Integer resReg = null;
    public Integer reg = null;
    public Integer num = null;

    public SLL(Integer resReg, Integer reg, Integer num) {
        if (resReg == null || reg == null ) {
            throw new RuntimeException("SLL reg cannot be null.");
        }
        this.resReg = resReg;
        this.reg = reg;
        this.num = num;
    }

    public String getOutput() {
        return "sll $" + resReg + ", $" + reg + ", " + num;
    }
}
