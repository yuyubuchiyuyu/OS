package middle.Class;

import middle.Class.IrType.IrType;
import middle.Symbol.LLVMSymbol;
import middle.Value;

public class GlobalVariable extends Value {
    public LLVMSymbol LLVMSymbol;

    public GlobalVariable(LLVMSymbol LLVMSymbol){
        this.LLVMSymbol = LLVMSymbol;
    }

    public String getOutput() {
        StringBuilder res = new StringBuilder();
        if (LLVMSymbol.type == 0) { // 变量
            res.append(LLVMSymbol.irName + " = dso_local global "
                    + LLVMSymbol.irType.getOutput()
                    + " " + LLVMSymbol.value);
        } else {    // 数组
            IrType irType = LLVMSymbol.valueIrType;
            res.append(LLVMSymbol.irName + " = dso_local global ["
                    + LLVMSymbol.size +" x "+ irType.getOutput()+"]"
                    + " ");
            if(LLVMSymbol.ifArrayValueZero){
                res.append("zeroinitializer");
            }else {
                res.append("[");
                for(int i = 0; i< LLVMSymbol.arrayValue.size(); i++){
                    Integer value = LLVMSymbol.arrayValue.get(i);
                    res.append(irType.getOutput()+" "+value);
                    if(i!= LLVMSymbol.arrayValue.size()-1){
                        res.append(", ");
                    }else{
                        res.append("]");
                    }
                }
            }
        }
        return res.toString();
    }
}
