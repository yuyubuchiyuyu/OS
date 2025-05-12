package backend.Instructions.logic;

import backend.Instructions.mipsInstruction;

public class BNE extends mipsInstruction {
    public Integer reg1 = null;
    public Integer reg2 = null;
    public String label = null;

    public BNE(Integer reg1, Integer reg2, String label) {
        if ( reg1 == null || reg2 == null) {
            throw new RuntimeException("BNE reg cannot be null.");
        }
        this.reg1 = reg1;
        this.reg2 = reg2;
        this.label = label;
    }

    public String getOutput() {
        return "bne $" + reg1 + ", $" + reg2 + ", " + label.replace("%","");
    }
}
