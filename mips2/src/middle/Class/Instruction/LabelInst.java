package middle.Class.Instruction;

import middle.Value;

public class LabelInst extends Value {
    public String labelName = null;

    public LabelInst(String labelName) {
        this.labelName = labelName;
    }

    public String getOutput() {
        return labelName.replace("%","") + ":";
    }
}
