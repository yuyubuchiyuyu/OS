package middle.Process;

import frontend.Parser.Class.Exp;
import frontend.Parser.Class.FuncRParams;
import frontend.Parser.Class.Stmt;
import middle.Class.Instruction.*;
import middle.Class.IrType.IrType;
import middle.Count;
import middle.Symbol.Symbol;
import middle.Symbol.SymbolTable;
import middle.Value;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Objects;

public class processLValStmt {
    public static ArrayList<Value> processLValStmt(Stmt stmt, SymbolTable symbolTable) {
        ArrayList<Value> instructions = new ArrayList<>();
        Symbol lValSymbol = symbolTable.search(stmt.lVal.getIdent());
        Exp lValExp = stmt.lValExp; // 右值
        String addressName;
        IrType irType;
        if(stmt.lVal.getExp()!=null){
            instructions.addAll(locate(lValSymbol,symbolTable,stmt.lVal.getExp()));
            addressName = instructions.get(instructions.size()-1).getResName();
            irType = lValSymbol.valueIrType;
        }else{
            addressName = lValSymbol.irName;
            irType = lValSymbol.irType;
        }
        StoreInst storeInst = null;
        if (lValExp.judgeCalculate(symbolTable)) {
            int value = lValExp.calculate(symbolTable);
            if(irType.getNum()==8){
                value = value % 256;
            }
            storeInst = new StoreInst(irType, value, addressName);
            instructions.add(storeInst);
        } else {
            instructions.addAll(lValExp.getCalInstructions(symbolTable));
            TransferInst.typeTransfer(instructions,irType);
            storeInst = new StoreInst(irType,
                    instructions.get(instructions.size() - 1).getResName(), addressName);
            instructions.add(storeInst);
        }

        return instructions;
    }
    public static ArrayList<Value> locate(Symbol symbol, SymbolTable symbolTable, Exp exp) {
        ArrayList<Value> instructions = new ArrayList<>();
        if (exp != null) {
            if (exp.judgeCalculate(symbolTable)) {
                if (symbol.size == null) {
                    String loadName = "%LocalVariable_" + Count.getFuncInner();
                    LoadInst loadInst = new LoadInst(loadName, symbol.irType, symbol.irName);
                    instructions.add(loadInst);
                    Integer offset = exp.calculate(symbolTable);
                    String geteName = "%LocalVariable_" + Count.getFuncInner();
                    GetelementptrInstr getelementptrInstr = new GetelementptrInstr(geteName, symbol, offset, loadName);
                    instructions.add(getelementptrInstr);
                } else {
                    Integer offset = exp.calculate(symbolTable);
                    String geteName = "%LocalVariable_" + Count.getFuncInner();
                    GetelementptrInstr getelementptrInstr = new GetelementptrInstr(geteName, symbol, offset);
                    instructions.add(getelementptrInstr);
                }
            } else {
                if (symbol.size == null) {
                    String loadName = "%LocalVariable_" + Count.getFuncInner();
                    LoadInst loadInst = new LoadInst(loadName, symbol.irType, symbol.irName);
                    instructions.add(loadInst);
                    instructions.addAll(exp.getCalInstructions(symbolTable));
                    IrType irTypeTemp = instructions.get(instructions.size() - 1).getResIrType();
                    if (irTypeTemp.getNum() != null && irTypeTemp.getNum() != 32) {
                        String transferName = "%LocalVariable_" + Count.getFuncInner();
                        TransferInst transferInst = new TransferInst(transferName, instructions.get(instructions.size() - 1).getResName(),
                                irTypeTemp, new IrType(IrType.TypeID.IntegerTyID, 32));
                        instructions.add(transferInst);
                    }
                    String offsetName = instructions.get(instructions.size() - 1).getResName();
                    String geteName = "%LocalVariable_" + Count.getFuncInner();
                    GetelementptrInstr getelementptrInstr = new GetelementptrInstr(geteName, symbol, offsetName, loadName);
                    instructions.add(getelementptrInstr);
                } else {
                    instructions.addAll(exp.getCalInstructions(symbolTable));
                    IrType irTypeTemp = instructions.get(instructions.size() - 1).getResIrType();
                    if (irTypeTemp.getNum() != null && irTypeTemp.getNum() != 32) {
                        String transferName = "%LocalVariable_" + Count.getFuncInner();
                        TransferInst transferInst = new TransferInst(transferName, instructions.get(instructions.size() - 1).getResName(),
                                irTypeTemp, new IrType(IrType.TypeID.IntegerTyID, 32));
                        instructions.add(transferInst);
                    }
                    String offsetName = instructions.get(instructions.size() - 1).getResName();
                    String geteName = "%LocalVariable_" + Count.getFuncInner();
                    GetelementptrInstr getelementptrInstr = new GetelementptrInstr(geteName, symbol, offsetName);
                    instructions.add(getelementptrInstr);
                }
            }
        }
        return instructions;
    }
}
