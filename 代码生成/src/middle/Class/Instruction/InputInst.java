package middle.Class.Instruction;

import middle.Class.IrType.IrType;
import middle.Value;

public class InputInst extends Value {
    private String resName;
    private IrType irType;
    public InputInst(String resName, IrType irType) {
        this.resName = resName;
        this.irType = irType;

    }
    public IrType getResIrType(){
        return new IrType(IrType.TypeID.IntegerTyID,32);
    }
    public String getResName(){
        return resName;
    }
    public String getOutput(){
        if(irType.getNum()==32){
            return resName + " = call i32 @getint()";
        } else {
            return resName + " = call i32 @getchar()";
        }
    }
}
