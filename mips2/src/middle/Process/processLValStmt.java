package middle.Process;

import frontend.Parser.Class.Exp;
import frontend.Parser.Class.Stmt;
import middle.Class.Instruction.*;
import middle.Class.IrType.IrType;
import middle.Count;
import middle.Symbol.LLVMSymbol;
import middle.Symbol.LLVMSymbolTable;
import middle.Value;

import java.util.ArrayList;

public class processLValStmt {
    public static ArrayList<Value> processLValStmt(Stmt stmt, LLVMSymbolTable LLVMSymbolTable) {
        ArrayList<Value> instructions = new ArrayList<>();
        LLVMSymbol lValLLVMSymbol = LLVMSymbolTable.search(stmt.lVal.getIdent());
        Exp lValExp = stmt.lValExp; // 右值
        String addressName;
        IrType irType;
        if(stmt.lVal.getExp()!=null){
            instructions.addAll(locate(lValLLVMSymbol, LLVMSymbolTable,stmt.lVal.getExp()));
            addressName = instructions.get(instructions.size()-1).getResName();
            irType = lValLLVMSymbol.valueIrType;
        }else{
            addressName = lValLLVMSymbol.irName;
            irType = lValLLVMSymbol.irType;
        }
        StoreInst storeInst = null;
        if (lValExp.judgeCalculate(LLVMSymbolTable)) {
            int value = lValExp.calculate(LLVMSymbolTable);
            if(irType.getNum()==8){
                value = value % 256;
            }
            storeInst = new StoreInst(irType, value, addressName);
            instructions.add(storeInst);
        } else {
            instructions.addAll(lValExp.getCalInstructions(LLVMSymbolTable));
            TransferInst.typeTransfer(instructions,irType);
            storeInst = new StoreInst(irType,
                    instructions.get(instructions.size() - 1).getResName(), addressName);
            instructions.add(storeInst);
        }

        return instructions;
    }
    public static ArrayList<Value> locate(LLVMSymbol LLVMSymbol, LLVMSymbolTable LLVMSymbolTable, Exp exp) {
        ArrayList<Value> instructions = new ArrayList<>();
        if (exp != null) {
            if (exp.judgeCalculate(LLVMSymbolTable)) {
                if (LLVMSymbol.size == null) {
                    String loadName = "%LocalVariable_" + Count.getFuncInner();
                    LoadInst loadInst = new LoadInst(loadName, LLVMSymbol.irType, LLVMSymbol.irName);
                    instructions.add(loadInst);
                    Integer offset = exp.calculate(LLVMSymbolTable);
                    String geteName = "%LocalVariable_" + Count.getFuncInner();
                    GetelementptrInstr getelementptrInstr = new GetelementptrInstr(geteName, LLVMSymbol, offset, loadName);
                    instructions.add(getelementptrInstr);
                } else {
                    Integer offset = exp.calculate(LLVMSymbolTable);
                    String geteName = "%LocalVariable_" + Count.getFuncInner();
                    GetelementptrInstr getelementptrInstr = new GetelementptrInstr(geteName, LLVMSymbol, offset);
                    instructions.add(getelementptrInstr);
                }
            } else {
                if (LLVMSymbol.size == null) {
                    String loadName = "%LocalVariable_" + Count.getFuncInner();
                    LoadInst loadInst = new LoadInst(loadName, LLVMSymbol.irType, LLVMSymbol.irName);
                    instructions.add(loadInst);
                    instructions.addAll(exp.getCalInstructions(LLVMSymbolTable));
                    IrType irTypeTemp = instructions.get(instructions.size() - 1).getResIrType();
                    if (irTypeTemp.getNum() != null && irTypeTemp.getNum() != 32) {
                        String transferName = "%LocalVariable_" + Count.getFuncInner();
                        TransferInst transferInst = new TransferInst(transferName, instructions.get(instructions.size() - 1).getResName(),
                                irTypeTemp, new IrType(IrType.TypeID.IntegerTyID, 32));
                        instructions.add(transferInst);
                    }
                    String offsetName = instructions.get(instructions.size() - 1).getResName();
                    String geteName = "%LocalVariable_" + Count.getFuncInner();
                    GetelementptrInstr getelementptrInstr = new GetelementptrInstr(geteName, LLVMSymbol, offsetName, loadName);
                    instructions.add(getelementptrInstr);
                } else {
                    instructions.addAll(exp.getCalInstructions(LLVMSymbolTable));
                    IrType irTypeTemp = instructions.get(instructions.size() - 1).getResIrType();
                    if (irTypeTemp.getNum() != null && irTypeTemp.getNum() != 32) {
                        String transferName = "%LocalVariable_" + Count.getFuncInner();
                        TransferInst transferInst = new TransferInst(transferName, instructions.get(instructions.size() - 1).getResName(),
                                irTypeTemp, new IrType(IrType.TypeID.IntegerTyID, 32));
                        instructions.add(transferInst);
                    }
                    String offsetName = instructions.get(instructions.size() - 1).getResName();
                    String geteName = "%LocalVariable_" + Count.getFuncInner();
                    GetelementptrInstr getelementptrInstr = new GetelementptrInstr(geteName, LLVMSymbol, offsetName);
                    instructions.add(getelementptrInstr);
                }
            }
        }
        return instructions;
    }
}
