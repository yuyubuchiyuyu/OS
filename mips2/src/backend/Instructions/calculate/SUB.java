package backend.Instructions.calculate;

import backend.Instructions.mipsInstruction;

public class SUB extends mipsInstruction {
    public Integer resReg = null;
    public Integer reg1 = null;
    public Integer reg2 = null;

    public SUB(Integer resReg, Integer reg1, Integer reg2) {
        if (resReg == null || reg1 == null || reg2 == null) {
            throw new RuntimeException("SUB reg cannot be null.");
        }
        this.resReg = resReg;
        this.reg1 = reg1;
        this.reg2 = reg2;
    }

    public String getOutput() {
        return "subu $" + resReg + ", $" + reg1 + ", $" + reg2;
    }
}
