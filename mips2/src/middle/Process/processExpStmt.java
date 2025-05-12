package middle.Process;

import frontend.Parser.Class.Exp;
import frontend.Parser.Class.Stmt;
import middle.Symbol.LLVMSymbolTable;
import middle.Value;

import java.util.ArrayList;

public class processExpStmt {
    public static ArrayList<Value> processExpStmt(Stmt stmt, LLVMSymbolTable LLVMSymbolTable) {
        ArrayList<Value> instructions = new ArrayList<>();
        Exp exp = stmt.exp;
        if (exp == null) {
            return instructions;
        } else if (exp.judgeCalculate(LLVMSymbolTable)) {
            return instructions;
        } else {
            return exp.getCalInstructions(LLVMSymbolTable);
        }
    }
}
