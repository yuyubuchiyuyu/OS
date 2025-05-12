package middle.Process;

import frontend.Lexer.Token;
import frontend.Parser.Class.BType;
import frontend.Parser.Class.Exp;
import frontend.Parser.Class.VarDef;
import middle.Class.Instruction.AllocaInst;
import middle.Class.Instruction.GetelementptrInstr;
import middle.Class.Instruction.StoreInst;
import middle.Class.Instruction.TransferInst;
import middle.Class.IrType.IrType;
import middle.Count;
import middle.Symbol.LLVMSymbol;
import middle.Symbol.LLVMSymbolTable;
import middle.Value;

import java.util.ArrayList;

public class processVarDef {
    public static ArrayList<Value> processVarDef(VarDef varDef, BType bType, LLVMSymbolTable LLVMSymbolTable) {
        ArrayList<Value> instructions = new ArrayList<>();
        String ident = varDef.getIdent();
        String name = "%LocalVariable_" + Count.getFuncInner();
        Integer dimension = varDef.getDimension();
        IrType irType;
        int bTypeSymbol;    // 填表用
        if (dimension == 0) {
            Integer value;
            if (bType.getBType() == Token.tokenType.INTTK) {
                bTypeSymbol = 0;
                irType = new IrType(IrType.TypeID.IntegerTyID, 32);
            } else {
                bTypeSymbol = 1;
                irType = new IrType(IrType.TypeID.IntegerTyID, 8);
            }
            LLVMSymbol LLVMSymbol = new LLVMSymbol(ident, name, irType, 0, bTypeSymbol, 1);
            LLVMSymbolTable.addSymbol(LLVMSymbol);
            AllocaInst allocaInst = new AllocaInst(name, irType);
            instructions.add(allocaInst);
            if (varDef.getInitVal() != null && varDef.getInitVal().getExp() != null) {
                Exp exp = varDef.getInitVal().getExp();
                if (exp.judgeCalculate(LLVMSymbolTable)) {    // 初值可以计算得到
                    value = exp.calculate(LLVMSymbolTable);
                    if (irType.getNum() == 8) {
                        value = value % 256;
                    }
                    LLVMSymbol.setValue(value);
                    StoreInst storeInst = new StoreInst(irType, value, LLVMSymbol.irName);
                    instructions.add(storeInst);
                } else {
                    instructions.addAll(exp.getCalInstructions(LLVMSymbolTable));
                    TransferInst.typeTransfer(instructions, irType);
                    StoreInst storeInst = new StoreInst(irType,
                            instructions.get(instructions.size() - 1).getResName(), LLVMSymbol.irName);
                    instructions.add(storeInst);
                }
            }
        } else {
            Integer size = varDef.getConstExp().calculate(LLVMSymbolTable);
            LLVMSymbol LLVMSymbol;
            if (bType.getBType() == Token.tokenType.INTTK) {
                irType = new IrType(IrType.TypeID.PointerTyID, 32);
                LLVMSymbol = new LLVMSymbol(ident, name, irType, 1, 0, 1, size);
            } else {
                irType = new IrType(IrType.TypeID.PointerTyID, 8);
                LLVMSymbol = new LLVMSymbol(ident, name, irType, 1, 1, 1, size);
            }
            LLVMSymbolTable.addSymbol(LLVMSymbol);
            IrType valueIrType = LLVMSymbol.valueIrType;
            AllocaInst allocaInst = new AllocaInst(name, valueIrType, size);
            instructions.add(allocaInst);
            if (varDef.getInitVal() != null) {
                if (varDef.getInitVal().getExps() != null) {
                    int offset = 0;
                    ArrayList<Exp> exps = varDef.getInitVal().getExps();
                    while (offset < exps.size()) {
                        String geteName = "%LocalVariable_" + Count.getFuncInner();
                        GetelementptrInstr getelementptrInstr = new GetelementptrInstr(geteName, LLVMSymbol, offset);
                        instructions.add(getelementptrInstr);
                        Exp exp = exps.get(offset);
                        if (exp.judgeCalculate(LLVMSymbolTable)) {
                            Integer value = exp.calculate(LLVMSymbolTable);
                            if (irType.getNum() == 8) {
                                value = value % 256;
                            }
                            LLVMSymbol.setArrayValue(value);
                            StoreInst storeInst = new StoreInst(valueIrType, value, geteName);
                            instructions.add(storeInst);
                        } else {
                            instructions.addAll(exp.getCalInstructions(LLVMSymbolTable));
                            TransferInst.typeTransfer(instructions, irType);
                            StoreInst storeInst = new StoreInst(valueIrType, instructions.get(instructions.size() - 1).getResName(), geteName);
                            instructions.add(storeInst);
                        }
                        offset = offset + 1;
                    }
                    if (bType.getBType() != Token.tokenType.INTTK) {    // 唯一不需要编译器置0的情况只有局部变量int数组部分初始化。
                        while (offset < size) {
                            String geteName = "%LocalVariable_" + Count.getFuncInner();
                            GetelementptrInstr getelementptrInstr = new GetelementptrInstr(geteName, LLVMSymbol, offset);
                            instructions.add(getelementptrInstr);
                            LLVMSymbol.setArrayValue(0);
                            StoreInst storeInst = new StoreInst(valueIrType, 0, geteName);
                            instructions.add(storeInst);
                            offset = offset + 1;
                        }
                    }
                } else if (varDef.getInitVal().getStringConst() != null) {
                    int offset = 0;
                    int totalCharacter = 0;
                    String stringConstTemp = varDef.getInitVal().getStringConst();
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < stringConstTemp.length() - 2; i++) {
                        stringBuilder.append(stringConstTemp.charAt(i + 1));
                    }
                    String stringConst = stringBuilder.toString();
                    while (totalCharacter < size) {
                        String geteName = "%LocalVariable_" + Count.getFuncInner();
                        GetelementptrInstr getelementptrInstr = new GetelementptrInstr(geteName, LLVMSymbol, totalCharacter);
                        instructions.add(getelementptrInstr);
                        Integer value = 0;
                        if (offset < stringConst.length()) {
                            if (stringConst.charAt(offset) == '\\' && offset + 1 < stringConst.length()) {
                                if (stringConst.charAt(offset + 1) == 'a') {
                                    value = 7;
                                    offset = offset + 2;
                                } else if (stringConst.charAt(offset + 1) == 'b') {
                                    value = 8;
                                    offset = offset + 2;
                                } else if (stringConst.charAt(offset + 1) == 't') {
                                    value = 9;
                                    offset = offset + 2;
                                } else if (stringConst.charAt(offset + 1) == 'n') {
                                    value = 10;
                                    offset = offset + 2;
                                } else if (stringConst.charAt(offset + 1) == 'v') {
                                    value = 11;
                                    offset = offset + 2;
                                } else if (stringConst.charAt(offset + 1) == 'f') {
                                    value = 12;
                                    offset = offset + 2;
                                } else if (stringConst.charAt(offset + 1) == '\"') {
                                    value = 34;
                                    offset = offset + 2;
                                } else if (stringConst.charAt(offset + 1) == '\'') {
                                    value = 39;
                                    offset = offset + 2;
                                } else if (stringConst.charAt(offset + 1) == '\\') {
                                    value = 92;
                                    offset = offset + 2;
                                } else if (stringConst.charAt(offset + 1) == '0') {
                                    value = 0;
                                    offset = offset + 2;
                                } else {
                                    value = (int) stringConst.charAt(offset);
                                    offset = offset + 1;
                                }
                            } else {
                                value = (int) stringConst.charAt(offset);
                                offset = offset + 1;
                            }
                        }
                        LLVMSymbol.setArrayValue(value);
                        StoreInst storeInst = new StoreInst(valueIrType, value, geteName);
                        instructions.add(storeInst);
                        totalCharacter = totalCharacter + 1;
                    }
                }
            }
        }
        return instructions;
    }
}
