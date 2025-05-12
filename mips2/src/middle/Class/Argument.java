package middle.Class;

import middle.Class.IrType.IrType;

public class Argument {
    public IrType irType;
    public String irName;
    public  String ident;

    public Argument(IrType irType, String irName, String ident) {
        this.irType = irType;
        this.irName = irName;
        this.ident = ident;
    }

    public String getOutput() {
        return irType.getOutput()+" "+irName;
    }
}
