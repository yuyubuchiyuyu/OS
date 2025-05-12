package middle.Class.Instruction;

import middle.Class.IrType.IrType;
import middle.Value;

public class AllocaInst extends Value {
    public String irName = null;
    public IrType irType = null;
    public Integer size = null;

    public AllocaInst(String name, IrType irType) {
        this.irName = name;
        this.irType = irType;
    }

    public AllocaInst(String name, IrType irType, Integer size) {
        this.irName = name;
        this.irType = irType;
        this.size = size;
    }

    public String getOutput() {
        if (size == null) {
            return irName + " = alloca " + irType.getOutput();
        } else {
            return irName + " = alloca [" + size + " x " + irType.getOutput() + "]";
        }
    }

    public String getResName() {
        return irName;
    }

    public IrType getResIrType() {
        return new IrType(IrType.TypeID.PointerTyID,irType.getNum());
    }
}
