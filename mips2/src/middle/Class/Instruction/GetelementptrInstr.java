package middle.Class.Instruction;

import middle.Class.IrType.IrType;
import middle.Symbol.LLVMSymbol;
import middle.Value;

public class GetelementptrInstr extends Value {
    private String resName = null;
    public LLVMSymbol LLVMSymbol = null;
    public Integer offset = null;
    public String offsetName = null;
    public String loadName = null;

    public GetelementptrInstr(String resName, LLVMSymbol LLVMSymbol, Integer offset) {
        this.resName = resName;
        this.LLVMSymbol = LLVMSymbol;
        this.offset = offset;
    }

    public GetelementptrInstr(String resName, LLVMSymbol LLVMSymbol, String offsetName) {
        this.resName = resName;
        this.LLVMSymbol = LLVMSymbol;
        this.offsetName = offsetName;
    }

    public GetelementptrInstr(String resName, LLVMSymbol LLVMSymbol, Integer offset, String loadName) {
        this.resName = resName;
        this.LLVMSymbol = LLVMSymbol;
        this.offset = offset;
        this.loadName = loadName;
    }

    public GetelementptrInstr(String resName, LLVMSymbol LLVMSymbol, String offsetName, String loadName) {
        this.resName = resName;
        this.LLVMSymbol = LLVMSymbol;
        this.offsetName = offsetName;
        this.loadName = loadName;
    }

    public String getOutput() {
        IrType irType = LLVMSymbol.valueIrType;
        Integer size = LLVMSymbol.size;
        if (size == null) {     // 函数参数
            if (offset != null) {
                return resName + " = getelementptr " + irType.getOutput() + ", " + LLVMSymbol.irType.getOutput() + " " + loadName + ", " + "i32 " + offset;
            } else {
                return resName + " = getelementptr " + irType.getOutput() + ", " + LLVMSymbol.irType.getOutput() + " " + loadName + ", " + "i32 " + offsetName;
            }
        } else {
            if (offset != null) {
                return resName + " = getelementptr [" + size + " x " + irType.getOutput() + "], [" + size + " x " + irType.getOutput() + "]* " + LLVMSymbol.irName + ", " + "i32 0, " + "i32 " + offset;
            } else {
                return resName + " = getelementptr [" + size + " x " + irType.getOutput() + "], [" + size + " x " + irType.getOutput() + "]* " + LLVMSymbol.irName + ", " + "i32 0, " + "i32 " + offsetName;
            }
        }

    }

    public String getResName() {
        return resName;
    }

    public IrType getResIrType() {
        return LLVMSymbol.irType;
    }
}
