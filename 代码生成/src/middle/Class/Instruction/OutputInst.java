package middle.Class.Instruction;

import middle.Class.IrType.IrType;
import middle.Count;
import middle.Value;

import java.util.ArrayList;
import java.util.Objects;

public class OutputInst extends Value {

    private String name = null;
    private Integer value = null;
    private Character now = null;
    private Character require = null;

    public OutputInst(String name, Character now, Character require) {
        this.name = name;
        this.now = now;
        this.require = require;
    }

    public OutputInst(Integer value, Character now, Character require) {
        this.value = value;
        this.now = now;
        this.require = require;
    }

    private String strName = null;
    private Integer size = 0;
    private String text = null;

    public OutputInst(String strName, Integer size, String text) {
        this.strName = strName;
        this.size = size;
        this.text = text;
    }

    public ArrayList<Value> prepareInst() {
        ArrayList<Value> instructions = new ArrayList<>();
        if (name != null) {
            IrType irType = null;
            if (this.now.equals('c')) {   // c->c c->d 都要转换成32
                irType = new IrType(IrType.TypeID.IntegerTyID, 8);
                String transferName = "%LocalVariable_" + Count.getFuncInner();
                TransferInst transferInst = new TransferInst(transferName, name, irType, new IrType(IrType.TypeID.IntegerTyID, 32));
                instructions.add(transferInst);
                name = transferName;
            } else {  // d->c 要 % 256 转成32 d->d 不动
                irType = new IrType(IrType.TypeID.IntegerTyID, 32);
                if (this.require.equals('c')) {
                    String calName = "%LocalVariable_" + Count.getFuncInner();
                    BinaryOperator binaryOperator = new BinaryOperator(calName, BinaryOperator.BinaryOperatorType.srem, irType, name, 256);
                    instructions.add(binaryOperator);
                    name = calName;
                }
            }
        }
        if (value != null) {
            if (this.require.equals('c')) {
                value = value % 256;
            }
        }
        return instructions;
    }


    public String getOutput() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("call void @put");
        if (strName != null) {
            stringBuilder.append("str(i8* getelementptr inbounds ([" + size + " x i8], [" + size + " x i8]* "
                    + strName + ", i64 0, i64 0))");
        } else {
            if (Objects.equals(require, 'd')) {
                stringBuilder.append("int(i32 ");
            } else {
                stringBuilder.append("ch(i32 ");
            }
            if (name != null) {
                stringBuilder.append(name);
            } else {
                stringBuilder.append(value);
            }
            stringBuilder.append(")");
        }
        return stringBuilder.toString();
    }
}
