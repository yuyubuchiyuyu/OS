package middle.Class;

import middle.Class.IrType.IrType;
import middle.Symbol.Symbol;
import middle.Value;

import java.util.ArrayList;

public class GlobalVariable extends Value {
    private Symbol symbol;

    public GlobalVariable(Symbol symbol){
        this.symbol = symbol;
    }

    public String getOutput() {
        StringBuilder res = new StringBuilder();
        if (symbol.type == 0) { // 变量
            res.append(symbol.irName + " = dso_local global "
                    + symbol.irType.getOutput()
                    + " " + symbol.value);
        } else {    // 数组
            IrType irType = symbol.valueIrType;
            res.append(symbol.irName + " = dso_local global ["
                    + symbol.size +" x "+ irType.getOutput()+"]"
                    + " ");
            if(symbol.ifArrayValueZero){
                res.append("zeroinitializer");
            }else {
                res.append("[");
                for(int i = 0;i<symbol.arrayValue.size();i++){
                    Integer value = symbol.arrayValue.get(i);
                    res.append(irType.getOutput()+" "+value);
                    if(i!=symbol.arrayValue.size()-1){
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
