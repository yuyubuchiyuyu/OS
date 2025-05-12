package backend.Instructions.jump;

import backend.Instructions.mipsInstruction;

public class JAL extends mipsInstruction {
    public String label = null;

    public JAL(String label) {
        this.label = label;
    }

    public String getOutput() {
        return "jal " + label.replace("%", "");
    }
}
