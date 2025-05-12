package middle.Process;

import frontend.Lexer.Token;
import frontend.Parser.Class.BType;
import frontend.Parser.Class.ConstDef;
import frontend.Parser.Class.ConstExp;
import middle.Class.GlobalVariable;
import middle.Class.IrType.IrType;
import middle.Count;
import middle.Symbol.LLVMSymbol;
import middle.Symbol.LLVMSymbolTable;

import java.util.ArrayList;

public class processGlobalConstDef {
    public static GlobalVariable processGlobalConstDef(ConstDef constDef, BType bType, LLVMSymbolTable LLVMSymbolTable) {
        GlobalVariable globalVariable = null;
        String ident = constDef.getIdent();
        Integer dimension = constDef.getDimension();
        String name = "@GlobalVariable_" + Count.getGlobalVariableCount();
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
            globalVariable = new GlobalVariable(LLVMSymbol);
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
            if (constDef.getConstInitVal() != null) {
                if (constDef.getConstInitVal().getConstExps() != null) {
                    int index = 0;
                    ArrayList<ConstExp> constExps = constDef.getConstInitVal().getConstExps();
                    while (index < constExps.size()) {
                        ConstExp constExp = constExps.get(index);
                        Integer value = constExp.calculate(LLVMSymbolTable);
                        if (irType.getNum() == 8) {
                            value = value % 256;
                        }
                        LLVMSymbol.setArrayValue(value);
                        index = index + 1;
                    }
                    while (index < size) {
                        LLVMSymbol.setArrayValue(0);
                        index = index + 1;
                    }
                } else if (constDef.getConstInitVal().getStringConst() != null) {
                    int index = 0;
                    int totalCharacter = 0;
                    String stringConstTemp = constDef.getConstInitVal().getStringConst();
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < stringConstTemp.length() - 2; i++) {
                        stringBuilder.append(stringConstTemp.charAt(i + 1));
                    }
                    String stringConst = stringBuilder.toString();
                    while (index < stringConst.length()) {
                        if (stringConst.charAt(index) == '\\' && index + 1 < stringConst.length()) {
                            if (stringConst.charAt(index + 1) == 'a') {
                                LLVMSymbol.setArrayValue(7);
                                index = index + 2;
                            } else if (stringConst.charAt(index + 1) == 'b') {
                                LLVMSymbol.setArrayValue(8);
                                index = index + 2;
                            } else if (stringConst.charAt(index + 1) == 't') {
                                LLVMSymbol.setArrayValue(9);
                                index = index + 2;
                            } else if (stringConst.charAt(index + 1) == 'n') {
                                LLVMSymbol.setArrayValue(10);
                                index = index + 2;
                            } else if (stringConst.charAt(index + 1) == 'v') {
                                LLVMSymbol.setArrayValue(11);
                                index = index + 2;
                            } else if (stringConst.charAt(index + 1) == 'f') {
                                LLVMSymbol.setArrayValue(12);
                                index = index + 2;
                            } else if (stringConst.charAt(index + 1) == '\"') {
                                LLVMSymbol.setArrayValue(34);
                                index = index + 2;
                            } else if (stringConst.charAt(index + 1) == '\'') {
                                LLVMSymbol.setArrayValue(39);
                                index = index + 2;
                            } else if (stringConst.charAt(index + 1) == '\\') {
                                LLVMSymbol.setArrayValue(92);
                                index = index + 2;
                            } else if (stringConst.charAt(index + 1) == '0') {
                                LLVMSymbol.setArrayValue(0);
                                index = index + 2;
                            } else {
                                LLVMSymbol.setArrayValue((int) (stringConst.charAt(index)));
                                index = index + 1;
                            }
                        } else {
                            LLVMSymbol.setArrayValue((int) (stringConst.charAt(index)));
                            index = index + 1;
                        }
                        totalCharacter = totalCharacter + 1;
                    }
                    while (totalCharacter < size) {
                        LLVMSymbol.setArrayValue(0);
                        totalCharacter = totalCharacter + 1;
                    }
                }
            }
            LLVMSymbolTable.addSymbol(LLVMSymbol);
            globalVariable = new GlobalVariable(LLVMSymbol);
        }
        return globalVariable;
    }
}
