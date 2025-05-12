package frontend.Symbol;

import frontend.ErrorInfo;
import frontend.Lexer.Token;
import frontend.Parser.Class.*;
import frontend.Parser.Parser;

import java.util.ArrayList;

public class Operation {
    public static boolean judgeExpContains(Token.tokenType type) {
        return type == Token.tokenType.PLUS ||
                type == Token.tokenType.MINU ||
                type == Token.tokenType.MULT ||
                type == Token.tokenType.DIV ||
                type == Token.tokenType.MOD ||
                type == Token.tokenType.LPARENT ||
                type == Token.tokenType.RPARENT ||
                type == Token.tokenType.INTCON ||
                type == Token.tokenType.CHRCON ||
                type == Token.tokenType.IDENFR ||
                type == Token.tokenType.LBRACK ||
                type == Token.tokenType.RBRACK ||
                type == Token.tokenType.NOT ||
                type == Token.tokenType.COMMA;
    }
    public static void addIError(Integer currentLineNum, ArrayList<ErrorInfo> errorList){
        if(Parser.printFlag){
            errorList.add(new ErrorInfo(currentLineNum, "i"));
        }
    }
    public static void addJError(Integer currentLineNum, ArrayList<ErrorInfo> errorList){
        if(Parser.printFlag){
            errorList.add(new ErrorInfo(currentLineNum, "j"));
        }
    }
    public static void addKError(Integer currentLineNum, ArrayList<ErrorInfo> errorList){
        if(Parser.printFlag){
            errorList.add(new ErrorInfo(currentLineNum, "k"));
        }
    }

    public static void judgeBError(Integer currentLineNum, ArrayList<ErrorInfo> errorList, SymbolTable symbolTable, String ident) {
        if (symbolTable.layerLookUpSymbol(ident) && Parser.printFlag) {
            errorList.add(new ErrorInfo(currentLineNum, "b"));
        }
    }

    public static void judgeCError(Integer currentLineNum, ArrayList<ErrorInfo> errorList, SymbolTable symbolTable, String ident) {
        if (!symbolTable.layerTopLookUpSymbol(ident) && Parser.printFlag) {
            errorList.add(new ErrorInfo(currentLineNum, "c"));
        }
    }

    public static void judgeDError(Integer currentLineNum, ArrayList<ErrorInfo> errorList, String ident, FuncRParams funcRParams) {
        if (!FuncList.matchNum(ident, funcRParams) && Parser.printFlag) {
            errorList.add(new ErrorInfo(currentLineNum, "d"));
        }
    }

    public static void judgeEError(SymbolTable symbolTable, Integer currentLineNum, ArrayList<ErrorInfo> errorList, String ident, FuncRParams funcRParams) {
        if (!FuncList.matchType(symbolTable, ident, funcRParams) && Parser.printFlag) {
            errorList.add(new ErrorInfo(currentLineNum, "e"));
        }
    }

    public static void judgeFError(Integer currentLineNum, ArrayList<ErrorInfo> errorList, boolean voidFuncFlag) {
        if (voidFuncFlag && Parser.printFlag) {
            errorList.add(new ErrorInfo(currentLineNum, "f"));
        }
    }

    public static void judgeGError(Integer currentLineNum, ArrayList<ErrorInfo> errorList, Token.tokenType type, Block block) {
        boolean result = block.getResult(); // 有符合要求的return时为真
        if (type != Token.tokenType.VOIDTK && Parser.printFlag) {
            if (!result) {
                errorList.add(new ErrorInfo(currentLineNum, "g"));
            }
        }
    }

    public static void judgeHError(Integer currentLineNum, ArrayList<ErrorInfo> errorList, SymbolTable symbolTable, String ident) {
        if (symbolTable.allConstLookUpSymbol(ident) && Parser.printFlag) {
            errorList.add(new ErrorInfo(currentLineNum, "h"));
        }
    }

    public static void judgeLError(Integer currentLineNum, ArrayList<ErrorInfo> errorList, String stringConst, ArrayList<Exp> exps) {
        Integer charNum = 0;
        Integer i = 0;
        while (i + 1 < stringConst.length()) {
            if (stringConst.charAt(i) == '%' && stringConst.charAt(i + 1) == 'd') {
                charNum++;
            } else if (stringConst.charAt(i) == '%' && stringConst.charAt(i + 1) == 'c') {
                charNum++;
            }
            i++;
        }
        if (charNum != exps.size() && Parser.printFlag) {
            errorList.add(new ErrorInfo(currentLineNum, "l"));
        }
    }

    public static void judgeMError(Integer currentLineNum, ArrayList<ErrorInfo> errorList, Stmt stmt) {
        if (Parser.printFlag && (stmt.getKind() == Stmt.StmtType.Break || stmt.getKind() == Stmt.StmtType.Continue)) {
            errorList.add(new ErrorInfo(currentLineNum, "m"));
        }
    }


    public static Integer judgeType(Token.tokenType type) {
        if (type == Token.tokenType.INTTK) {
            return 0;
        } else if (type == Token.tokenType.CHARTK) {
            return 1;
        } else {
            return -1;
        }
    }

    public static Integer judgeConstDefArrayType(ConstDef constDef) {
        if (constDef.getArrayType()) {
            return 1;
        } else {
            return 0;
        }
    }

    public static Integer judgeVarDefArrayType(VarDef varDef) {
        if (varDef.getArrayType()) {
            return 1;
        } else {
            return 0;
        }
    }

}
