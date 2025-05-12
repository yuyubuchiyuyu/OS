package middle.Class.Instruction;

import middle.Value;

public class BranchInst extends Value {
    private String cond = null;
    private String ifTrue = null;
    private String ifFalse = null;
    private String dest = null;

    public BranchInst(String cond, String ifTrue, String ifFalse) {
        this.cond = cond;
        this.ifTrue = ifTrue;
        this.ifFalse = ifFalse;
    }

    public BranchInst(String dest) {
        this.dest = dest;
    }

    public String getOutput() {
        if (dest == null) {
            return "br i1 " + cond + ", label " + ifTrue + ", label " + ifFalse;
        } else {
            return "br label " + dest;
        }
    }


}
