package backend.Instructions.load;

import backend.Instructions.mipsInstruction;

public class LA extends mipsInstruction {
    public Integer num = null;
    public String strName = null;

    public LA(Integer num, String strName) {
        this.num = num;
        this.strName = strName;
    }

    public String getOutput() {
        return "la $" + num + ", " + strName.replace("@", "");
    }

}
