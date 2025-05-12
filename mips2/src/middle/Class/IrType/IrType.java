package middle.Class.IrType;

public class IrType {

    public TypeID getId() {
        return typeID;
    }

    public enum TypeID {
        // Primitive types
        VoidTyID,       //空返回值
        LabelTyID,      //标签类型
        // Derived types
        IntegerTyID,    //整数类型
        FunctionTyID,   //浮点数类型
        PointerTyID     //指针类型
    }

    ;
    private TypeID typeID = null;

    // VoidTyID
    public IrType(TypeID typeID) {
        this.typeID = typeID;
    }

    // IntegerTyID
    // PointerTyID
    private Integer num = null;

    public IrType(TypeID typeID, Integer num) {
        this.typeID = typeID;
        this.num = num;
    }

    public Integer getNum() {
        return num;
    }


    public String getOutput() {
        if (typeID == TypeID.VoidTyID) {
            return "void";
        }
        if (typeID == TypeID.IntegerTyID) {
            return "i" + num;
        }
        if (typeID == TypeID.PointerTyID) {
            return "i" + num + "*";
        }
        return "";
    }
}
