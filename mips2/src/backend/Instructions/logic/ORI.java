package backend.Instructions.logic;

import backend.Instructions.mipsInstruction;

public class ORI extends mipsInstruction {
    public Integer resReg = null;
    public Integer reg = null;
    public Integer num = null;

    public ORI(Integer resReg, Integer reg, Integer num) {
        if (resReg == null || reg == null ) {
            throw new RuntimeException("ORI reg cannot be null.");
        }
        this.resReg = resReg;
        this.reg = reg;
        this.num = num;
    }

    public String getOutput() {
        return "ori $" + resReg + ", $" + reg + ", " + num;
    }
}
