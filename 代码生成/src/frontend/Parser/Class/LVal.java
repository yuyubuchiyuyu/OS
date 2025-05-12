package frontend.Parser.Class;

import frontend.Parser.Parser;
import middle.Class.Instruction.GetelementptrInstr;
import middle.Class.Instruction.LoadInst;
import middle.Class.Instruction.TransferInst;
import middle.Class.IrType.IrType;
import middle.Count;
import middle.Symbol.Symbol;
import middle.Symbol.SymbolTable;
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

    public Symbol getSymbol(SymbolTable symbolTable) {
        return symbolTable.search(ident);
    }

    public Integer calculate(SymbolTable symbolTable) { // 算
        Symbol symbol = getSymbol(symbolTable);
        if (symbol.getDimension() == 0) {   // 一维
            return symbol.value;
        } else if (symbol.getDimension() == 1) {
            if(symbol.ifArrayValueZero){
                return 0;
            }else{
                return symbol.arrayValue.get(exp.calculate(symbolTable));
            }
        }
        return null;
    }

    public boolean judgeCalculate(SymbolTable symbolTable) {    // 是否可计算
        Symbol symbol = getSymbol(symbolTable);
        if (symbol.getDimension() == 0) {
            return symbol.con == 0;    // 常量可以用于计算
        } else {
            if(exp==null){
                return false;
            }
            return symbol.con == 0 && exp.judgeCalculate(symbolTable);
        }
    }

    public ArrayList<Value> getCalInstructions(SymbolTable symbolTable) {   // 计算指令
        ArrayList<Value> instructions = new ArrayList<>();
        Symbol symbol = getSymbol(symbolTable);
        if (symbol.getDimension() == 0) {   // 一维
            String name = "%LocalVariable_" + Count.getFuncInner();
            LoadInst loadInst = new LoadInst(name, symbol.irType, symbol.irName);
            instructions.add(loadInst);
        } else if (symbol.getDimension() == 1) {
            if (exp != null) {
                if (exp.judgeCalculate(symbolTable)) {
                    if (symbol.size == null) {      // 函数形参
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
                    if (symbol.size == null) {      // 函数形参
                        String loadName = "%LocalVariable_" + Count.getFuncInner();
                        LoadInst loadInst = new LoadInst(loadName, symbol.irType, symbol.irName);
                        instructions.add(loadInst);
                        instructions.addAll(exp.getCalInstructions(symbolTable));
                        TransferInst.typeTransfer(instructions,new IrType(IrType.TypeID.IntegerTyID,32));
                        String offsetName = instructions.get(instructions.size() - 1).getResName();
                        String geteName = "%LocalVariable_" + Count.getFuncInner();
                        GetelementptrInstr getelementptrInstr = new GetelementptrInstr(geteName, symbol, offsetName, loadName);
                        instructions.add(getelementptrInstr);
                    } else {
                        instructions.addAll(exp.getCalInstructions(symbolTable));
                        TransferInst.typeTransfer(instructions,new IrType(IrType.TypeID.IntegerTyID,32));
                        String offsetName = instructions.get(instructions.size() - 1).getResName();
                        String geteName = "%LocalVariable_" + Count.getFuncInner();
                        GetelementptrInstr getelementptrInstr = new GetelementptrInstr(geteName, symbol, offsetName);
                        instructions.add(getelementptrInstr);
                    }
                }
                String resName = "%LocalVariable_" + Count.getFuncInner();
                LoadInst loadInst = new LoadInst(resName, symbol.valueIrType, instructions.get(instructions.size() - 1).getResName());
                instructions.add(loadInst);
            } else {    // 取的是数组整体
                if (symbol.size == null) {      // 函数形参
                    String loadName = "%LocalVariable_" + Count.getFuncInner();
                    LoadInst loadInst = new LoadInst(loadName, symbol.irType, symbol.irName);
                    instructions.add(loadInst);
                    String geteName = "%LocalVariable_" + Count.getFuncInner();
                    GetelementptrInstr getelementptrInstr = new GetelementptrInstr(geteName, symbol, 0, loadName);
                    instructions.add(getelementptrInstr);
                } else {
                    String geteName = "%LocalVariable_" + Count.getFuncInner();
                    GetelementptrInstr getelementptrInstr = new GetelementptrInstr(geteName, symbol, 0);
                    instructions.add(getelementptrInstr);
                }
            }
        }
        return instructions;
    }


    public IrType getIrType(SymbolTable symbolTable) {
        Symbol symbol = getSymbol(symbolTable);
        return symbol.irType;
    }
}
