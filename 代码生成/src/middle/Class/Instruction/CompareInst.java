package middle.Class.Instruction;

import middle.Class.IrType.IrType;
import middle.Value;

public class CompareInst extends Value {
    public enum CompareType {
        eq,     // ==
        ne,     // !=
        sgt,    // >
        sge,    // >=
        slt,    // <
        sle,    // <=
    }
    private String resName = null;
    private CompareType type = null;
    private IrType irType = null;
    private String name1 = null;
    private String name2 = null;
    private Integer num1 = null;
    private Integer num2 = null;
    public CompareInst(String resName, CompareType type, IrType irType, String name1, String name2){
        this.resName = resName;
        this.type = type;
        this.irType = irType;
        this.name1 = name1;
        this.name2 = name2;
    }
    public CompareInst(String resName, CompareType type, IrType irType, String name1, Integer num2){
        this.resName = resName;
        this.type = type;
        this.irType = irType;
        this.name1 = name1;
        this.num2 = num2;
    }
    public CompareInst(String resName, CompareType type, IrType irType, Integer num1, String name2){
        this.resName = resName;
        this.type = type;
        this.irType = irType;
        this.num1 = num1;
        this.name2 = name2;
    }
    public CompareInst(String resName, CompareType type, IrType irType, Integer num1, Integer num2){
        this.resName = resName;
        this.type = type;
        this.irType = irType;
        this.num1 = num1;
        this.num2 = num2;
    }
    public String getOutput(){
        if(name1!=null && name2!=null){
            return resName+" = icmp "+type.toString()+" "+irType.getOutput()+" "+name1+", "+name2;
        } else if(name1!=null && num2 !=null){
            return resName+" = icmp "+type.toString()+" "+irType.getOutput()+" "+name1+", "+num2;
        } else if(num1!=null && name2!=null){
            return resName+" = icmp "+type.toString()+" "+irType.getOutput()+" "+num1+", "+name2;
        } else {
            return resName+" = icmp "+type.toString()+" "+irType.getOutput()+" "+num1+", "+num2;
        }
    }
    public String getResName(){
        return resName;
    }
    public IrType getResIrType(){
        return new IrType(IrType.TypeID.IntegerTyID,1);
    }
}
