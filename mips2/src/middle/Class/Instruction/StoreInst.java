package middle.Class.Instruction;

import middle.Class.IrType.IrType;
import middle.Value;

public class StoreInst extends Value{ // 把1存到2里
    public String irName1 = null;
    private IrType irType = null;
    public String irName2 = null;
    public Integer value;

    public StoreInst(IrType irType, String irName1, String irName2) {
        this.irType = irType;
        this.irName1 = irName1;
        this.irName2 = irName2;
    }
    public StoreInst(IrType irType, Integer value, String irName2) {
        this.irType = irType;
        this.value = value;
        this.irName2 = irName2;
    }

    public String getOutput() {
        if(irName1!=null){
            return "store " + irType.getOutput() + " " + irName1 + ", " + irType.getOutput() + "* " + irName2;
        } else {
            return "store " + irType.getOutput() + " " + value + ", " + irType.getOutput() + "* " + irName2;
        }
    }
    public IrType getResIrType(){
        return irType;
    }
    public String getResName(){
        return irName2;
    }
}
