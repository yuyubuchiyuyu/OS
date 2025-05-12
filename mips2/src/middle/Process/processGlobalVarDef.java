package middle.Process;

import frontend.Lexer.Token;
import frontend.Parser.Class.BType;
import frontend.Parser.Class.Exp;
import frontend.Parser.Class.VarDef;
import middle.Class.GlobalVariable;
import middle.Class.IrType.IrType;
import middle.Count;
import middle.Symbol.LLVMSymbol;
import middle.Symbol.LLVMSymbolTable;

import java.util.ArrayList;

public class processGlobalVarDef {
    public static GlobalVariable processGlobalVarDef(VarDef varDef, BType bType, LLVMSymbolTable LLVMSymbolTable) {
        GlobalVariable globalVariable = null;
        String ident = varDef.getIdent();
        Integer dimension = varDef.getDimension();
        String name = "@GlobalVariable_" + Count.getGlobalVariableCount();
        IrType irType;
        if (dimension == 0) {
            LLVMSymbol LLVMSymbol;
            if (bType.getBType() == Token.tokenType.INTTK) {
                irType = new IrType(IrType.TypeID.IntegerTyID, 32);
                LLVMSymbol = new LLVMSymbol(ident, name, irType, 0, 0, 1);
            } else {
                irType = new IrType(IrType.TypeID.IntegerTyID, 8);
                LLVMSymbol = new LLVMSymbol(ident, name, irType, 0, 1, 1);
            }
            Integer value = 0;
            if (varDef.getInitVal() != null && varDef.getInitVal().getExp() != null) {
                value = varDef.getInitVal().getExp().calculate(LLVMSymbolTable);
            }
            if (irType.getNum() == 8) {
                value = value % 256;
            }
            LLVMSymbol.setValue(value);
            LLVMSymbolTable.addSymbol(LLVMSymbol);
            globalVariable = new GlobalVariable(LLVMSymbol);
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
            if (varDef.getInitVal() != null) {
                if (varDef.getInitVal().getExps() != null) {
                    int index = 0;
                    ArrayList<Exp> exps = varDef.getInitVal().getExps();
                    while (index < exps.size()) {
                        Exp exp = exps.get(index);
                        Integer value = exp.calculate(LLVMSymbolTable);
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
                } else if (varDef.getInitVal().getStringConst() != null) {
                    int index = 0;
                    int totalCharacter = 0;
                    String stringConstTemp = varDef.getInitVal().getStringConst();
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
