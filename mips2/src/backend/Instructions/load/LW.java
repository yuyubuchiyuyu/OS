package backend.Instructions.load;

import backend.Instructions.mipsInstruction;

public class LW extends mipsInstruction {
    public Integer reg = null;
    public String variableName = null;
    public Integer base = null;
    public Integer offset = null;
    public boolean needModify = false;

    public LW(Integer reg, String variableName) {
        this.reg = reg;
        this.variableName = variableName;
    }

    public LW(Integer reg, Integer base, Integer offset) {
        if (offset == null) {
            throw new RuntimeException("LW Offset cannot be null.");
        }
        this.reg = reg;
        this.base = base;
        this.offset = offset;
    }


    public String getOutput() {
        if (variableName != null) {
            return "lw $" + reg + ", " + variableName.replace("@","").replace("%","");
        } else {
            return "lw $" + reg + ", " + offset + "($" + base + ")";
        }
    }
}
