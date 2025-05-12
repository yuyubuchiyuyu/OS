package middle.Symbol;

import middle.Class.IrType.IrType;

import java.util.ArrayList;

public class LLVMSymbol {

    public String name;    // 当前单词所对应的字符串。
    public String irName;   // 地址名
    public IrType irType;   // symbol实值对应的irType

    public Integer type;        // 0 -> var, 1 -> array, 2 -> func
    public Integer bType;    // 0 -> int, 1 -> char, -1 -> void
    public Integer con;        // 0 -> const, 1 -> var
    public Integer value;
    // 对于数组
    public IrType valueIrType;
    public Integer size;
    public ArrayList<Integer> arrayValue = new ArrayList<>();
    public boolean ifArrayValueZero;

    public LLVMSymbol(String name, String irName, IrType irType, Integer type, Integer bType, Integer con) {
        this.name = name;
        this.irName = irName;
        this.irType = irType;
        this.type = type;
        this.bType = bType;
        this.con = con;
        this.value = null;
    }

    public LLVMSymbol(String name, String irName, IrType irType, Integer type, Integer bType, Integer con, Integer size) {
        this.name = name;
        this.irName = irName;
        this.irType = irType;
        this.valueIrType = new IrType(IrType.TypeID.IntegerTyID, irType.getNum());
        this.type = type;
        this.bType = bType;
        this.con = con;
        this.size = size;
        this.ifArrayValueZero = true;
    }

    public int getDimension() {
        if (type == 0 || type == 1) {
            return type;
        }
        return -1;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public void setArrayValue(Integer value) {
        this.ifArrayValueZero = false;
        this.arrayValue.add(value);
    }

}
