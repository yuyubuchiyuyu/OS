package backend.Instructions.calculate;

import backend.Instructions.mipsInstruction;

public class DIV extends mipsInstruction {
    public Integer reg1 = null;
    public Integer reg2 = null;

    public DIV(Integer reg1, Integer reg2) {
        if (reg1 == null || reg2 == null) {
            throw new RuntimeException("DIV reg cannot be null.");
        }
        this.reg1 = reg1;
        this.reg2 = reg2;
    }

    public String getOutput() {
        return "div $" + reg1 + ", $" + reg2;
    }
}
