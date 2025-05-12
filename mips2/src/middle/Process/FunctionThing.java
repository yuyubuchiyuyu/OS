package middle.Process;

import middle.Class.Argument;
import middle.Class.IrType.IrType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class FunctionThing {
    public static HashSet<String> isCalled = new HashSet<>();
    public static HashMap<String, IrType> funcIrType = new HashMap<>();
    public static HashMap<String, ArrayList<Argument>> arguments = new HashMap<>();
    private static boolean funcReturn = false;

    public static void initFuncReturn() {
        funcReturn = false;
    }
    public static boolean getFuncReturn() {
        return funcReturn;
    }
    public static void setFuncReturn() {
        funcReturn = true;
    }
}
