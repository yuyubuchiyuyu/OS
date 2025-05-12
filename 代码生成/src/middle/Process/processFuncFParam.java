package middle.Process;

import frontend.Lexer.Token;
import frontend.Parser.Class.FuncFParam;
import middle.Class.Argument;
import middle.Class.IrType.IrType;
import middle.Symbol.Symbol;
import middle.Symbol.SymbolTable;

public class processFuncFParam {
    public static Argument processFuncFParam(FuncFParam funcFParam, SymbolTable symbolTable, int count) {
        Argument argumentTy = null;
        IrType irType;
        Integer bType;    // 填表用
        String ident = funcFParam.getIdent();
        String name = "%Argument_" + count;
        Integer dimension = funcFParam.getDimension();
        if (dimension == 0) {
            if (funcFParam.getbType().getBType() == Token.tokenType.INTTK) {
                irType = new IrType(IrType.TypeID.IntegerTyID, 32);
                bType = 0;
            } else {
                irType = new IrType(IrType.TypeID.IntegerTyID, 8);
                bType = 1;
            }
            argumentTy = new Argument(irType, name, ident);
            Symbol symbol = new Symbol(ident, name, irType, 0, bType, 1);
            symbolTable.addSymbol(symbol);
        } else { // todo
            if (funcFParam.getbType().getBType() == Token.tokenType.INTTK) {
                irType = new IrType(IrType.TypeID.PointerTyID, 32);
                bType = 0;
            } else {
                irType = new IrType(IrType.TypeID.PointerTyID, 8);
                bType = 1;
            }
            argumentTy = new Argument(irType,name,ident);
            Symbol symbol = new Symbol(ident,name,irType,1,bType,1,null);
            symbolTable.addSymbol(symbol);
        }
        return argumentTy;
    }
}
