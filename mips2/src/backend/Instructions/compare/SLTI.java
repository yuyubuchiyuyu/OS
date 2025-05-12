package backend.Instructions.compare;

import backend.Instructions.mipsInstruction;

public class SLTI extends mipsInstruction {
    public Integer resReg = null;
    public Integer reg = null;
    public Integer num = null;

    public SLTI(Integer resReg, Integer reg, Integer num) {
        if (resReg == null || reg == null ) {
            throw new RuntimeException("SLTI reg cannot be null.");
        }
        this.resReg = resReg;
        this.reg = reg;
        this.num = num;
    }

    public String getOutput() {
        return "slti $" + resReg + ", $" + reg + ", " + num;
    }
}
