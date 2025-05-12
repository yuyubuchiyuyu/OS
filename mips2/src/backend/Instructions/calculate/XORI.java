package backend.Instructions.calculate;

import backend.Instructions.mipsInstruction;

public class XORI extends mipsInstruction {
    public Integer resReg = null;
    public Integer reg = null;
    public Integer num = null;

    public XORI(Integer resReg, Integer reg, Integer num) {
        if (resReg == null || reg == null ) {
            throw new RuntimeException("XORI reg cannot be null.");
        }
        this.resReg = resReg;
        this.reg = reg;
        this.num = num;
    }

    public String getOutput() {
        return "xori $" + resReg + ", $" + reg + ", " + num;
    }

}
