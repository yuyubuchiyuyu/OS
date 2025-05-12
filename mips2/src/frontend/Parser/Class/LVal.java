package frontend.Parser.Class;

import frontend.Parser.Parser;
import middle.Class.Instruction.GetelementptrInstr;
import middle.Class.Instruction.LoadInst;
import middle.Class.Instruction.TransferInst;
import middle.Class.IrType.IrType;
import middle.Count;
import middle.Symbol.LLVMSymbol;
import middle.Symbol.LLVMSymbolTable;
import middle.Value;

import java.util.ArrayList;

public class LVal {
    private String ident = null;
    private Exp exp = null;

    public LVal(String ident, Exp exp, ArrayList<String> outputList) {
        this.ident = ident;
        this.exp = exp;
        if (Parser.printFlag) {
            outputList.add("<LVal>");
        }
    }

    public LVal(String ident, ArrayList<String> outputList) {
        this.ident = ident;
        if (Parser.printFlag) {
            outputList.add("<LVal>");
        }
    }

    public String getIdent() {
        return ident;
    }

    public Exp getExp() {
        return exp;
    }

    public LLVMSymbol getSymbol(LLVMSymbolTable LLVMSymbolTable) {
        return LLVMSymbolTable.search(ident);
    }

    public Integer calculate(LLVMSymbolTable LLVMSymbolTable) { // 算
        LLVMSymbol LLVMSymbol = getSymbol(LLVMSymbolTable);
        if (LLVMSymbol.getDimension() == 0) {   // 一维
            return LLVMSymbol.value;
        } else if (LLVMSymbol.getDimension() == 1) {
            if(LLVMSymbol.ifArrayValueZero){
                return 0;
            }else{
                return LLVMSymbol.arrayValue.get(exp.calculate(LLVMSymbolTable));
            }
        }
        return null;
    }

    public boolean judgeCalculate(LLVMSymbolTable LLVMSymbolTable) {    // 是否可计算
        LLVMSymbol LLVMSymbol = getSymbol(LLVMSymbolTable);
        if (LLVMSymbol.getDimension() == 0) {
            return LLVMSymbol.con == 0;    // 常量可以用于计算
        } else {
            if(exp==null){
                return false;
            }
            return LLVMSymbol.con == 0 && exp.judgeCalculate(LLVMSymbolTable);
        }
    }

    public ArrayList<Value> getCalInstructions(LLVMSymbolTable LLVMSymbolTable) {   // 计算指令
        ArrayList<Value> instructions = new ArrayList<>();
        LLVMSymbol LLVMSymbol = getSymbol(LLVMSymbolTable);
        if (LLVMSymbol.getDimension() == 0) {   // 一维
            String name = "%LocalVariable_" + Count.getFuncInner();
            LoadInst loadInst = new LoadInst(name, LLVMSymbol.irType, LLVMSymbol.irName);
            instructions.add(loadInst);
        } else if (LLVMSymbol.getDimension() == 1) {
            if (exp != null) {
                if (exp.judgeCalculate(LLVMSymbolTable)) {
                    if (LLVMSymbol.size == null) {      // 函数形参
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
                    if (LLVMSymbol.size == null) {      // 函数形参
                        String loadName = "%LocalVariable_" + Count.getFuncInner();
                        LoadInst loadInst = new LoadInst(loadName, LLVMSymbol.irType, LLVMSymbol.irName);
                        instructions.add(loadInst);
                        instructions.addAll(exp.getCalInstructions(LLVMSymbolTable));
                        TransferInst.typeTransfer(instructions,new IrType(IrType.TypeID.IntegerTyID,32));
                        String offsetName = instructions.get(instructions.size() - 1).getResName();
                        String geteName = "%LocalVariable_" + Count.getFuncInner();
                        GetelementptrInstr getelementptrInstr = new GetelementptrInstr(geteName, LLVMSymbol, offsetName, loadName);
                        instructions.add(getelementptrInstr);
                    } else {
                        instructions.addAll(exp.getCalInstructions(LLVMSymbolTable));
                        TransferInst.typeTransfer(instructions,new IrType(IrType.TypeID.IntegerTyID,32));
                        String offsetName = instructions.get(instructions.size() - 1).getResName();
                        String geteName = "%LocalVariable_" + Count.getFuncInner();
                        GetelementptrInstr getelementptrInstr = new GetelementptrInstr(geteName, LLVMSymbol, offsetName);
                        instructions.add(getelementptrInstr);
                    }
                }
                String resName = "%LocalVariable_" + Count.getFuncInner();
                LoadInst loadInst = new LoadInst(resName, LLVMSymbol.valueIrType, instructions.get(instructions.size() - 1).getResName());
                instructions.add(loadInst);
            } else {    // 取的是数组整体
                if (LLVMSymbol.size == null) {      // 函数形参
                    String loadName = "%LocalVariable_" + Count.getFuncInner();
                    LoadInst loadInst = new LoadInst(loadName, LLVMSymbol.irType, LLVMSymbol.irName);
                    instructions.add(loadInst);
                    String geteName = "%LocalVariable_" + Count.getFuncInner();
                    GetelementptrInstr getelementptrInstr = new GetelementptrInstr(geteName, LLVMSymbol, 0, loadName);
                    instructions.add(getelementptrInstr);
                } else {
                    String geteName = "%LocalVariable_" + Count.getFuncInner();
                    GetelementptrInstr getelementptrInstr = new GetelementptrInstr(geteName, LLVMSymbol, 0);
                    instructions.add(getelementptrInstr);
                }
            }
        }
        return instructions;
    }


    public IrType getIrType(LLVMSymbolTable LLVMSymbolTable) {
        LLVMSymbol LLVMSymbol = getSymbol(LLVMSymbolTable);
        return LLVMSymbol.irType;
    }
}
