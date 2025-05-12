package middle.Process;

import frontend.Parser.Class.Exp;
import frontend.Parser.Class.Stmt;
import middle.Class.Instruction.ReturnInst;
import middle.Class.Instruction.TransferInst;
import middle.Class.IrType.IrType;
import middle.Symbol.LLVMSymbolTable;
import middle.Value;

import java.util.ArrayList;

public class processReturnStmt {
    public static ArrayList<Value> processReturnStmt(Stmt stmt, LLVMSymbolTable LLVMSymbolTable, String funcName) {
        ArrayList<Value> instructions = new ArrayList<>();
        if (stmt.returnExp == null) {
            IrType irType = new IrType(IrType.TypeID.VoidTyID);

            ReturnInst returnInst = new ReturnInst(irType);
            instructions.add(returnInst);
        } else {
            Exp exp = stmt.returnExp;
            IrType funcIrType = FunctionThing.funcIrType.get(funcName);
            if (exp.judgeCalculate(LLVMSymbolTable)) {
                Integer value = exp.calculate(LLVMSymbolTable);
                if (funcIrType.getNum()!=null && funcIrType.getNum() == 8) {
                    value = value % 256;
                }
                ReturnInst returnInst = new ReturnInst(funcIrType, value);
                instructions.add(returnInst);
            } else {
                instructions.addAll(exp.getCalInstructions(LLVMSymbolTable));
                TransferInst.typeTransfer(instructions,funcIrType);
                String name = instructions.get(instructions.size() - 1).getResName();
                ReturnInst returnInst = new ReturnInst(funcIrType, name);
                instructions.add(returnInst);
            }
        }
        return instructions;
    }
}
