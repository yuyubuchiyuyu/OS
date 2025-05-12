package middle.Class.Instruction;

import middle.Class.IrType.IrType;
import middle.Value;

public class LoadInst extends Value {
    private String resName = null;
    private IrType irType = null;
    private String name = null;

    public LoadInst(String resName, IrType irType, String name) {
        this.resName = resName;
        this.irType = irType;
        this.name = name;
    }

    public String getOutput() {
        return resName + " = load " + irType.getOutput() + ", " + irType.getOutput() + "* " + name;
    }
    public String getResName(){
        return resName;
    }
    public IrType getResIrType(){
        return irType;
    }
}
