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
import middle.Symbol.Symbol;
import middle.Symbol.SymbolTable;
import middle.Value;

import java.util.ArrayList;

public class processConstDef {
    public static ArrayList<Value> processConstDef(ConstDef constDef, BType bType, SymbolTable symbolTable) {
        ArrayList<Value> instructions = new ArrayList<>();
        String ident = constDef.getIdent();
        String name = "%LocalVariable_" + Count.getFuncInner();
        Integer dimension = constDef.getDimension();
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
            AllocaInst allocaInst = new AllocaInst(name, irType);
            instructions.add(allocaInst);
            StoreInst storeInst = new StoreInst(irType, value, symbol.irName);
            instructions.add(storeInst);
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
            symbolTable.addSymbol(symbol);
            IrType valueIrType = symbol.valueIrType;
            AllocaInst allocaInst = new AllocaInst(name,valueIrType, size);
            instructions.add(allocaInst);
            if (constDef.getConstInitVal() != null) {
                IrType irTypeInit = symbol.valueIrType;
                if (constDef.getConstInitVal().getConstExps() != null) {
                    int offset = 0;
                    ArrayList<ConstExp> constExps = constDef.getConstInitVal().getConstExps();
                    while (offset < size) {
                        String geteName = "%LocalVariable_" + Count.getFuncInner();
                        GetelementptrInstr getelementptrInstr = new GetelementptrInstr(geteName, symbol, offset);
                        instructions.add(getelementptrInstr);
                        Integer value = 0;
                        if (offset < constExps.size()) {
                            ConstExp constExp = constExps.get(offset);
                            value = constExp.calculate(symbolTable);
                            if (irType.getNum() == 8) {
                                value = value % 256;
                            }
                        }
                        symbol.setArrayValue(value);
                        StoreInst storeInst = new StoreInst(irTypeInit, value,  geteName);
                        instructions.add(storeInst);
                        offset = offset + 1;
                    }
                } else if (constDef.getConstInitVal().getStringConst() != null) {
                    int offset = 0;
                    String stringConstTemp = constDef.getConstInitVal().getStringConst();
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < stringConstTemp.length() - 2; i++) {
                        stringBuilder.append(stringConstTemp.charAt(i+1));
                    }
                    String stringConst = stringBuilder.toString();
                    while (offset < size) {
                        String geteName = "%LocalVariable_" + Count.getFuncInner();
                        GetelementptrInstr getelementptrInstr = new GetelementptrInstr(geteName, symbol, offset);
                        instructions.add(getelementptrInstr);
                        int value = 0;
                        if (offset < stringConst.length()) {
                            value = (int) stringConst.charAt(offset);
                        }
                        symbol.setArrayValue(value);
                        StoreInst storeInst = new StoreInst(irTypeInit, value, geteName);
                        instructions.add(storeInst);
                        offset = offset + 1;
                    }
                }
            }
        }
        return instructions;
    }
}
