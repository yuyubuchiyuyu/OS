package middle.Process;

import frontend.Lexer.Token;
import frontend.Parser.Class.BType;
import frontend.Parser.Class.ConstDef;
import frontend.Parser.Class.ConstExp;
import middle.Class.Instruction.AllocaInst;
import middle.Class.Instruction.GetelementptrInstr;
import middle.Class.Instruction.StoreInst;
import middle.Class.IrType.IrType;
import middle.Count;
import middle.Symbol.LLVMSymbol;
import middle.Symbol.LLVMSymbolTable;
import middle.Value;

import java.util.ArrayList;

public class processConstDef {
    public static ArrayList<Value> processConstDef(ConstDef constDef, BType bType, LLVMSymbolTable LLVMSymbolTable) {
        ArrayList<Value> instructions = new ArrayList<>();
        String ident = constDef.getIdent();
        String name = "%LocalVariable_" + Count.getFuncInner();
        Integer dimension = constDef.getDimension();
        IrType irType;
        if (dimension == 0) {
            LLVMSymbol LLVMSymbol;
            if (bType.getBType() == Token.tokenType.INTTK) {
                irType = new IrType(IrType.TypeID.IntegerTyID, 32);
                LLVMSymbol = new LLVMSymbol(ident, name, irType, 0, 0, 0);
            } else {
                irType = new IrType(IrType.TypeID.IntegerTyID, 8);
                LLVMSymbol = new LLVMSymbol(ident, name, irType, 0, 1, 0);
            }
            Integer value = 0;
            if (constDef.getConstInitVal() != null && constDef.getConstInitVal().getConstExp() != null) {
                value = constDef.getConstInitVal().getConstExp().calculate(LLVMSymbolTable);
            }
            if (irType.getNum() == 8) {
                value = value % 256;
            }
            LLVMSymbol.setValue(value);
            LLVMSymbolTable.addSymbol(LLVMSymbol);
            AllocaInst allocaInst = new AllocaInst(name, irType);
            instructions.add(allocaInst);
            StoreInst storeInst = new StoreInst(irType, value, LLVMSymbol.irName);
            instructions.add(storeInst);
        } else {
            Integer size = constDef.getConstExp().calculate(LLVMSymbolTable);
            LLVMSymbol LLVMSymbol;
            if (bType.getBType() == Token.tokenType.INTTK) {
                irType = new IrType(IrType.TypeID.PointerTyID, 32);
                LLVMSymbol = new LLVMSymbol(ident, name, irType, 1, 0, 0, size);
            } else {
                irType = new IrType(IrType.TypeID.PointerTyID, 8);
                LLVMSymbol = new LLVMSymbol(ident, name, irType, 1, 1, 0, size);
            }
            LLVMSymbolTable.addSymbol(LLVMSymbol);
            IrType valueIrType = LLVMSymbol.valueIrType;
            AllocaInst allocaInst = new AllocaInst(name, valueIrType, size);
            instructions.add(allocaInst);
            if (constDef.getConstInitVal() != null) {
                IrType irTypeInit = LLVMSymbol.valueIrType;
                if (constDef.getConstInitVal().getConstExps() != null) {
                    int offset = 0;
                    ArrayList<ConstExp> constExps = constDef.getConstInitVal().getConstExps();
                    while (offset < size) {
                        String geteName = "%LocalVariable_" + Count.getFuncInner();
                        GetelementptrInstr getelementptrInstr = new GetelementptrInstr(geteName, LLVMSymbol, offset);
                        instructions.add(getelementptrInstr);
                        Integer value = 0;
                        if (offset < constExps.size()) {
                            ConstExp constExp = constExps.get(offset);
                            value = constExp.calculate(LLVMSymbolTable);
                            if (irType.getNum() == 8) {
                                value = value % 256;
                            }
                        }
                        LLVMSymbol.setArrayValue(value);
                        StoreInst storeInst = new StoreInst(irTypeInit, value, geteName);
                        instructions.add(storeInst);
                        offset = offset + 1;
                    }
                } else if (constDef.getConstInitVal().getStringConst() != null) {
                    int offset = 0;
                    int totalCharacter = 0;
                    String stringConstTemp = constDef.getConstInitVal().getStringConst();
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < stringConstTemp.length() - 2; i++) {
                        stringBuilder.append(stringConstTemp.charAt(i + 1));
                    }
                    String stringConst = stringBuilder.toString();
                    while (totalCharacter < size) {
                        String geteName = "%LocalVariable_" + Count.getFuncInner();
                        GetelementptrInstr getelementptrInstr = new GetelementptrInstr(geteName, LLVMSymbol, totalCharacter);
                        instructions.add(getelementptrInstr);
                        int value = 0;
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
                        StoreInst storeInst = new StoreInst(irTypeInit, value, geteName);
                        instructions.add(storeInst);
                        totalCharacter = totalCharacter + 1;
                    }
                }
            }
        }
        return instructions;
    }
}
