package backend.Instructions.logic;

import backend.Instructions.mipsInstruction;

public class ANDI extends mipsInstruction {
    public Integer resReg = null;
    public Integer reg = null;
    public Integer num = null;

    public ANDI(Integer resReg, Integer reg, Integer num) {
        if (resReg == null || reg == null) {
            throw new RuntimeException("ANDI reg cannot be null.");
        }
        this.resReg = resReg;
        this.reg = reg;
        this.num = num;
    }

    public String getOutput() {
        return "andi $" + resReg + ", $" + reg + ", " + num;
    }
}
