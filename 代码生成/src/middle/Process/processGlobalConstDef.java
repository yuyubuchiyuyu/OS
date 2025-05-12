package middle.Process;

import frontend.Lexer.Token;
import frontend.Parser.Class.BType;
import frontend.Parser.Class.ConstDef;
import frontend.Parser.Class.ConstExp;
import middle.Class.GlobalVariable;
import middle.Class.IrType.IrType;
import middle.Count;
import middle.Symbol.Symbol;
import middle.Symbol.SymbolTable;

import java.util.ArrayList;

public class processGlobalConstDef {
    public static GlobalVariable processGlobalConstDef(ConstDef constDef, BType bType, SymbolTable symbolTable) {
        GlobalVariable globalVariable = null;
        String ident = constDef.getIdent();
        Integer dimension = constDef.getDimension();
        String name = "@GlobalVariable_" + Count.getGlobalVariableCount();
        IrType irType;
        if (dimension == 0) {
            Symbol symbol;
            if (bType.getBType() == Token.tokenType.INTTK) {
                irType = new IrType(IrType.TypeID.IntegerTyID, 32);
                symbol = new Symbol(ident, name, irType, 0, 0, 0);
            } else {
                irType = new IrType(IrType.TypeID.IntegerTyID, 8);
                symbol = new Symbol(ident, name, irType, 0, 1, 0);
            }
            Integer value = 0;
            if (constDef.getConstInitVal() != null && constDef.getConstInitVal().getConstExp() != null) {
                value = constDef.getConstInitVal().getConstExp().calculate(symbolTable);
            }
            if (irType.getNum() == 8) {
                value = value % 256;
            }
            symbol.setValue(value);
            symbolTable.addSymbol(symbol);
            globalVariable = new GlobalVariable(symbol);
        } else {
            Integer size = constDef.getConstExp().calculate(symbolTable);
            Symbol symbol;
            if (bType.getBType() == Token.tokenType.INTTK) {
                irType = new IrType(IrType.TypeID.PointerTyID, 32);
                symbol = new Symbol(ident, name, irType, 1, 0, 0, size);
            } else {
                irType = new IrType(IrType.TypeID.PointerTyID, 8);
                symbol = new Symbol(ident, name, irType, 1, 1, 0, size);
            }
            if (constDef.getConstInitVal() != null) {
                if (constDef.getConstInitVal().getConstExps() != null) {
                    int index = 0;
                    ArrayList<ConstExp> constExps = constDef.getConstInitVal().getConstExps();
                    while (index < constExps.size()) {
                        ConstExp constExp = constExps.get(index);
                        Integer value = constExp.calculate(symbolTable);
                        if (irType.getNum() == 8) {
                            value = value % 256;
                        }
                        symbol.setArrayValue(value);
                        index = index + 1;
                    }
                    while (index < size) {
                        symbol.setArrayValue(0);
                        index = index + 1;
                    }
                } else if (constDef.getConstInitVal().getStringConst() != null) {
                    int index = 0;
                    String stringConstTemp = constDef.getConstInitVal().getStringConst();
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < stringConstTemp.length() - 2; i++) {
                        stringBuilder.append(stringConstTemp.charAt(i + 1));
                    }
                    String stringConst = stringBuilder.toString();
                    while (index < stringConst.length()) {
                        symbol.setArrayValue((int) stringConst.charAt(index));
                        index = index + 1;
                    }
                    while (index < size) {
                        symbol.setArrayValue(0);
                        index = index + 1;
                    }
                }
            }
            symbolTable.addSymbol(symbol);
            globalVariable = new GlobalVariable(symbol);
        }
        return globalVariable;
    }
}
