package backend.Instructions.calculate;

import backend.Instructions.mipsInstruction;

public class MFHI extends mipsInstruction {
    public Integer resReg = null;

    public MFHI(Integer resReg) {
        if (resReg == null ) {
            throw new RuntimeException("MFHI reg cannot be null.");
        }
        this.resReg = resReg;
    }

    public String getOutput() {
        return "mfhi $" + resReg;
    }
}
