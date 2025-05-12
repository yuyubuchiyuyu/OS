package backend.Instructions.store;

import backend.Instructions.mipsInstruction;

public class SW extends mipsInstruction {
    public Integer reg = null;
    public String variableName = null;
    public Integer base = null;
    public Integer offset = null;

    public SW(Integer reg, String variableName) {
        this.reg = reg;
        this.variableName = variableName;
    }

    public SW(Integer reg, Integer base, Integer offset) {
        if (offset == null) {
            throw new RuntimeException("SW Offset cannot be null.");
        }
        this.reg = reg;
        this.base = base;
        this.offset = offset;
    }


    public String getOutput() {
        if (variableName != null) {
            return "sw $" + reg + ", " + variableName.replace("@", "").replace("%", "");
        } else {
            return "sw $" + reg + ", " + offset + "($" + base + ")";
        }
    }
}
