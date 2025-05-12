package middle.Class.Instruction;

import middle.Class.IrType.IrType;
import middle.Value;

public class UnaryOperator extends Value {
    public enum UnaryOperatorType {
        xor
    }
    public String resName = null;
    public UnaryOperator.UnaryOperatorType type = null;
    public IrType irType = null;
    public String name = null;
    public Integer num = null;
    public UnaryOperator(String resName, UnaryOperator.UnaryOperatorType type, IrType irType, String name){
        this.resName = resName;
        this.type = type;
        this.irType = irType;
        this.name = name;
    }
    public UnaryOperator(String resName, UnaryOperator.UnaryOperatorType type, IrType irType, Integer num){
        this.resName = resName;
        this.type = type;
        this.irType = irType;
        this.num = num;
    }
    public String getOutput(){
        if(name!=null){
            return resName+" = "+type.toString()+" "+irType.getOutput()+" "+name+", true";
        }else{
            return resName+" = "+type.toString()+" "+irType.getOutput()+" "+num+", true";
        }
    }
    public String getResName(){
        return resName;
    }
    public IrType getResIrType(){
        return new IrType(IrType.TypeID.IntegerTyID,1);
    }
}
