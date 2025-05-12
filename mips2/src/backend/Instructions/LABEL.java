package backend.Instructions;

public class LABEL extends mipsInstruction {
    public String labelName = null;

    public LABEL(String labelName) {
        this.labelName = labelName;
    }

    public String getOutput() {
        return labelName.replace("%", "") + ":";
    }
}
