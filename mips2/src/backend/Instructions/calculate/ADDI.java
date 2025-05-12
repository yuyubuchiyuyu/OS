package backend.Instructions.calculate;

import backend.Instructions.mipsInstruction;

public class ADDI extends mipsInstruction {
    public Integer resReg = null;
    public Integer reg = null;
    public Integer num = null;
    public boolean needModify = false;

    public ADDI(Integer resReg, Integer reg, Integer num) {
        if (resReg == null || reg == null ) {
            throw new RuntimeException("ADDI reg cannot be null.");
        }
        this.resReg = resReg;
        this.reg = reg;
        this.num = num;
    }

    public String getOutput() {
        return "addi $" + resReg + ", $" + reg + ", " + num;
    }
}
