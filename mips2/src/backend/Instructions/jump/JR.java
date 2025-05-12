package backend.Instructions.jump;

import backend.Instructions.mipsInstruction;

public class JR extends mipsInstruction {
    public JR() {
    }

    public String getOutput() {
        return "jr $31";
    }
}
