package middle.Class.Instruction;

import middle.Class.IrType.IrType;
import middle.Symbol.Symbol;
import middle.Value;

public class GetelementptrInstr extends Value {
    private String resName = null;
    private Symbol symbol = null;
    private Integer offset = null;
    private String offsetName = null;
    private String loadName = null;

    public GetelementptrInstr(String resName, Symbol symbol, Integer offset) {
        this.resName = resName;
        this.symbol = symbol;
        this.offset = offset;
    }

    public GetelementptrInstr(String resName, Symbol symbol, String offsetName) {
        this.resName = resName;
        this.symbol = symbol;
        this.offsetName = offsetName;
    }

    public GetelementptrInstr(String resName, Symbol symbol, Integer offset, String loadName) {
        this.resName = resName;
        this.symbol = symbol;
        this.offset = offset;
        this.loadName = loadName;
    }

    public GetelementptrInstr(String resName, Symbol symbol, String offsetName, String loadName) {
        this.resName = resName;
        this.symbol = symbol;
        this.offsetName = offsetName;
        this.loadName = loadName;
    }

    public String getOutput() {
        IrType irType = symbol.valueIrType;
        Integer size = symbol.size;
        if (size == null) {
            if (offset != null) {
                return resName + " = getelementptr " + irType.getOutput() + ", " + symbol.irType.getOutput() + " " + loadName + ", " + "i32 " + offset;
            } else {
                return resName + " = getelementptr " + irType.getOutput() + ", " + symbol.irType.getOutput() + " " + loadName + ", " + "i32 " + offsetName;
            }
        } else {
            if (offset != null) {
                return resName + " = getelementptr [" + size + " x " + irType.getOutput() + "], [" + size + " x " + irType.getOutput() + "]* " + symbol.irName + ", " + "i32 0, " + "i32 " + offset;
            } else {
                return resName + " = getelementptr [" + size + " x " + irType.getOutput() + "], [" + size + " x " + irType.getOutput() + "]* " + symbol.irName + ", " + "i32 0, " + "i32 " + offsetName;
            }
        }

    }

    public String getResName() {
        return resName;
    }

    public IrType getResIrType() {
        return symbol.irType;
    }
}
