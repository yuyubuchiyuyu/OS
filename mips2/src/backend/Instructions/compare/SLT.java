package backend.Instructions.compare;

import backend.Instructions.mipsInstruction;

public class SLT extends mipsInstruction {
    public Integer resReg = null;
    public Integer reg1 = null;
    public Integer reg2 = null;

    public SLT(Integer resReg, Integer reg1, Integer reg2) {
        if (resReg == null || reg1 == null || reg2 == null) {
            throw new RuntimeException("SLT reg cannot be null.");
        }
        this.resReg = resReg;
        this.reg1 = reg1;
        this.reg2 = reg2;
    }

    public String getOutput() {
        return "slt $" + resReg + ", $" + reg1 + ", $" + reg2;
    }
}
