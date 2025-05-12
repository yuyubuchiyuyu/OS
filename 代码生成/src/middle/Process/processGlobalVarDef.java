package middle.Process;

import frontend.Lexer.Token;
import frontend.Parser.Class.BType;
import frontend.Parser.Class.ConstExp;
import frontend.Parser.Class.Exp;
import frontend.Parser.Class.VarDef;
import middle.Class.GlobalVariable;
import middle.Class.IrType.IrType;
import middle.Count;
import middle.Symbol.Symbol;
import middle.Symbol.SymbolTable;

import java.util.ArrayList;

public class processGlobalVarDef {
    public static GlobalVariable processGlobalVarDef(VarDef varDef, BType bType, SymbolTable symbolTable) {
        GlobalVariable globalVariable = null;
        String ident = varDef.getIdent();
        Integer dimension = varDef.getDimension();
        String name = "@GlobalVariable_" + Count.getGlobalVariableCount();
        IrType irType;
        if (dimension == 0) {
            Symbol symbol;
            if (bType.getBType() == Token.tokenType.INTTK) {
                irType = new IrType(IrType.TypeID.IntegerTyID, 32);
                symbol = new Symbol(ident, name, irType, 0, 0, 1);
            } else {
                irType = new IrType(IrType.TypeID.IntegerTyID, 8);
                symbol = new Symbol(ident, name, irType, 0, 1, 1);
            }
            Integer value = 0;
            if (varDef.getInitVal() != null&&varDef.getInitVal().getExp()!=null) {
                value = varDef.getInitVal().getExp().calculate(symbolTable);
            }
            if (irType.getNum() == 8) {
                value = value % 256;
            }
            symbol.setValue(value);
            symbolTable.addSymbol(symbol);
            globalVariable = new GlobalVariable(symbol);
        } else {
            Integer size = varDef.getConstExp().calculate(symbolTable);
            Symbol symbol;
            if (bType.getBType() == Token.tokenType.INTTK) {
                irType = new IrType(IrType.TypeID.PointerTyID, 32);
                symbol = new Symbol(ident, name, irType, 1, 0, 1, size);
            } else {
                irType = new IrType(IrType.TypeID.PointerTyID, 8);
                symbol = new Symbol(ident, name, irType, 1, 1, 1, size);
            }
            if(varDef.getInitVal()!=null){
                if(varDef.getInitVal().getExps()!=null){
                    int index = 0;
                    ArrayList<Exp> exps = varDef.getInitVal().getExps();
                    while (index < exps.size()) {
                        Exp exp = exps.get(index);
                        Integer value = exp.calculate(symbolTable);
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
                }else if(varDef.getInitVal().getStringConst()!=null){
                    int index = 0;
                    String stringConstTemp = varDef.getInitVal().getStringConst();
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < stringConstTemp.length() - 2; i++) {
                        stringBuilder.append(stringConstTemp.charAt(i+1));
                    }
                    String stringConst = stringBuilder.toString();
                    while(index<stringConst.length()){
                        symbol.setArrayValue((int) stringConst.charAt(index));
                        index=index +1;
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
