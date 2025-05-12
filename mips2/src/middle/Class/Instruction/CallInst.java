package middle.Class.Instruction;

import frontend.Parser.Class.Exp;
import frontend.Parser.Class.FuncRParams;
import middle.Class.Argument;
import middle.Class.IrType.IrType;
import middle.Count;
import middle.Process.FunctionThing;
import middle.Symbol.LLVMSymbolTable;
import middle.Value;

import java.util.ArrayList;

public class CallInst extends Value {
    public String resName = null;
    public IrType irType = null;
    public String funcName = null;
    public FuncRParams funcRParams = null;
    public LLVMSymbolTable LLVMSymbolTable = null;

    public CallInst(String resName, IrType irType, String funcName, FuncRParams funcRParams, LLVMSymbolTable LLVMSymbolTable) {
        this.resName = resName;
        this.irType = irType;
        this.funcName = funcName;
        this.funcRParams = funcRParams;
        this.LLVMSymbolTable = LLVMSymbolTable;
    }

    public String getResName() {
        return resName;
    }

    public IrType getResIrType() {
        return irType;
    }

    public ArrayList<String> argumentName = new ArrayList<>();
    public ArrayList<IrType> argumentIrType = new ArrayList<>();

    public ArrayList<Value> prepareInst() {
        ArrayList<Value> instructions = new ArrayList<>();
        if (funcRParams != null) {
            ArrayList<Exp> exps = funcRParams.getExps();
            for (int i = 0; i < exps.size(); i++) {
                Exp exp = exps.get(i);
                Argument argument = FunctionThing.arguments.get(funcName).get(i);
                if (exp.judgeCalculate(LLVMSymbolTable)) {    // 可计算
                    Integer value = exp.calculate(LLVMSymbolTable);
                    if (argument.irType.getNum() == 8) {
                        value = value % 256;
                    }
                    argumentName.add(value.toString());
                    argumentIrType.add(argument.irType);
                } else {    // 关系式
                    instructions.addAll(exp.getCalInstructions(LLVMSymbolTable));
                    IrType irTypeTemp = instructions.get(instructions.size() - 1).getResIrType();
                    if (!argument.irType.getNum().equals(irTypeTemp.getNum())) {
                        String transferName = "%LocalVariable_" + Count.getFuncInner();
                        TransferInst transferInst = new TransferInst(transferName, instructions.get(instructions.size() - 1).getResName(),
                                irTypeTemp, argument.irType);
                        instructions.add(transferInst);
                    }
                    argumentName.add(instructions.get(instructions.size() - 1).getResName());
                    argumentIrType.add(argument.irType);
                }
            }
        }
        return instructions;
    }

    public String getOutput() {
        StringBuilder stringBuilder = new StringBuilder();
        if (irType.getId() != IrType.TypeID.VoidTyID) {
            stringBuilder.append(resName + " = ");
        }
        stringBuilder.append("call " + irType.getOutput() + " @" + funcName + "(");
        for (int i = 0; i < argumentName.size(); i++) {
            IrType irType = argumentIrType.get(i);
            String name = argumentName.get(i);
            stringBuilder.append(irType.getOutput() + " " + name);
            if (i != argumentName.size() - 1) {
                stringBuilder.append(", ");
            }
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }
}
