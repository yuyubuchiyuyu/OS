package middle.Class.Instruction;

import middle.Class.IrType.IrType;
import middle.Value;

public class ReturnInst extends Value {
    private IrType irType = null;
    public Integer value = null;
    public String variableName = null;

    public ReturnInst(IrType irType) {
        this.irType = irType;
    }

    public ReturnInst(IrType irType, Integer value) {
        this.irType = irType;
        this.value = value;
    }

    public ReturnInst(IrType irType, String variableName) {
        this.irType = irType;
        this.variableName = variableName;
    }

    public String getOutput() {
        if (variableName != null) {
            return "ret " + irType.getOutput() + " " + variableName;
        } else if(value!=null){
            return "ret " + irType.getOutput() + " " + value;
        } else {
            return "ret "+irType.getOutput();
        }
    }

}
