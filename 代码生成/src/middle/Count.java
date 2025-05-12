package middle;

public class Count {
    private static Integer globalVariable = 0;

    public static Integer getGlobalVariableCount() {
        Integer temp = globalVariable;
        globalVariable++;
        return temp;
    }
    public static Integer str = 0;
    public static Integer getStrCount(){
        Integer temp = str;
        str++;
        return temp;
    }

    private static Integer func = 0;
    private static Integer funcInner = 0;

    public static Integer getFuncCount() {
        Integer temp = func;
        func++;
        funcInner = 0;
        return temp;
    }

    public static Integer getFuncInner() {
        Integer temp = funcInner;
        funcInner++;
        return temp;
    }

    private static Integer ifLabel = 0;
    private static Integer elseLabel = 0;
    private static Integer ifelseEndLabel = 0;

    public static Integer getIfLabel() {
        Integer temp = ifLabel;
        ifLabel++;
        return temp;
    }

    public static Integer getElseLabel() {
        Integer temp = elseLabel;
        elseLabel++;
        return temp;
    }

    public static Integer getIfElseEndLabel() {
        Integer temp = ifelseEndLabel;
        ifelseEndLabel++;
        return temp;
    }

    private static Integer ifCondLabel = 0;

    public static Integer getIfCondLabel() {
        Integer temp = ifCondLabel;
        ifCondLabel++;
        return temp;
    }

    private static Integer forCondLabel = 0;
    private static  Integer forStmt2Label = 0;
    private static Integer forMainStmtLabel = 0;
    private static Integer forEndLabel = 0;

    public static Integer getForCondLabel() {
        Integer temp = forCondLabel;
        forCondLabel++;
        return temp;
    }
    public static Integer getForMainStmtLabel() {
        Integer temp = forMainStmtLabel;
        forMainStmtLabel++;
        return temp;
    }

    public static Integer getForEndLabel() {
        Integer temp = forEndLabel;
        forEndLabel++;
        return temp;
    }

    public static Integer getForStmt2Label() {
        Integer temp = forStmt2Label;
        forStmt2Label++;
        return temp;
    }
}
