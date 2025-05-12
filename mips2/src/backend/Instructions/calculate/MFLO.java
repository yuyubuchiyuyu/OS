package backend.Instructions.calculate;

import backend.Instructions.mipsInstruction;

public class MFLO extends mipsInstruction {
    public Integer resReg = null;

    public MFLO(Integer resReg) {
        if (resReg == null) {
            throw new RuntimeException("MFLO reg cannot be null.");
        }
        this.resReg = resReg;
    }

    public String getOutput() {
        return "mflo $" + resReg;
    }
}
