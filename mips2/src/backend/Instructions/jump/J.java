package backend.Instructions.jump;

import backend.Instructions.mipsInstruction;

public class J extends mipsInstruction {
    public String label = null;

    public J(String label) {
        this.label = label;
    }

    public String getOutput() {
        return "j " + label.replace("%", "");
    }
}
