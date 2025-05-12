package middle.Class.Instruction;

import middle.Class.IrType.IrType;
import middle.Count;
import middle.Value;

import java.util.ArrayList;

public class TransferInst extends Value {
    public String resName = null;
    public IrType irType1 = null;
    public IrType irType2 = null;
    public Integer value = null;
    public String name = null;

    public TransferInst(String resName, Integer value, IrType irType1, IrType irType2) {
        this.resName = resName;
        this.value = value;
        this.irType1 = irType1;
        this.irType2 = irType2;
    }

    public TransferInst(String resName, String name, IrType irType1, IrType irType2) {
        this.resName = resName;
        this.name = name;
        this.irType1 = irType1;
        this.irType2 = irType2;
    }

    public String getResName() {
        return resName;
    }

    public IrType getResIrType() {
        return irType2;
    }

    public String getOutput() {
        if (value != null) {
            if (irType1.getNum() < irType2.getNum()) {
                return resName + " = zext " + irType1.getOutput() + " " + value + " to " + irType2.getOutput();
            } else {
                return resName + " = trunc " + irType1.getOutput() + " " + value + " to " + irType2.getOutput();
            }
        } else {
            if (irType1.getNum() < irType2.getNum()) {
                return resName + " = zext " + irType1.getOutput() + " " + name + " to " + irType2.getOutput();
            } else {
                return resName + " = trunc " + irType1.getOutput() + " " + name + " to " + irType2.getOutput();
            }
        }
    }
    public static void typeTransfer(ArrayList<Value> instructions,IrType irType){
        IrType irTypeTemp = instructions.get(instructions.size()-1).getResIrType();
        if (irType.getNum()!=null && irTypeTemp.getNum()!=null&&!irType.getNum().equals(irTypeTemp.getNum())) {
            String transferName = "%LocalVariable_" + Count.getFuncInner();
            TransferInst transferInst = new TransferInst(transferName, instructions.get(instructions.size() - 1).getResName(),
                    irTypeTemp, irType);
            instructions.add(transferInst);
        }
    }
}
