package backend.Instructions.logic;

import backend.Instructions.mipsInstruction;

public class BEQ extends mipsInstruction {
    public Integer reg1 = null;
    public Integer reg2 = null;
    public String label = null;

    public BEQ(Integer reg1, Integer reg2, String label) {
        if (reg1 == null || reg2 == null) {
            throw new RuntimeException("BEQ reg cannot be null.");
        }
        this.reg1 = reg1;
        this.reg2 = reg2;
        this.label = label;
    }

    public String getOutput() {
        return "beq $" + reg1 + ", $" + reg2 + ", " + label.replace("%","");
    }
}
