package frontend.Parser.Class;

import frontend.Lexer.Token;
import frontend.Parser.Parser;

import java.util.ArrayList;

public class Stmt {

    /*
        语句 Stmt → LVal '=' Exp ';' // 1
    | [Exp] ';' // 2
    | Block // 3
     | 'if' '(' Cond ')' Stmt [ 'else' Stmt ] // 4
     | 'for' '(' [ForStmt] ';' [Cond] ';' [ForStmt] ')' Stmt // 5
    | 'break' ';'   // 6
    | 'continue' ';'    // 7
     | 'return' [Exp] ';' // 8
     | LVal '=' 'getint''('')'';'   // 9
     | LVal '=' 'getchar''('')'';'  // 10
     | 'printf''('StringConst {','Exp}')'';' // 11
         */
    public enum StmtType {
        LVal, Exp, Block, IfElse, For, Break,
        Continue, Return, Getint, GetChar, Print
    }

    private StmtType kind;


    // LVal '=' Exp ';'
    public LVal lVal = null;
    public Exp lValExp = null;

    public Stmt(LVal lVal, Exp lValExp, ArrayList<String> outputList) {
        this.kind = StmtType.LVal;
        this.lVal = lVal;
        this.lValExp = lValExp;
        if (Parser.printFlag) {
            outputList.add("<Stmt>");
        }
    }

    // [Exp]
    public Exp exp = null;

    public Stmt(Exp exp, ArrayList<String> outputList) {
        this.kind = StmtType.Exp;
        this.exp = exp;
        if (Parser.printFlag) {
            outputList.add("<Stmt>");
        }
    }

    // Block
    public Block block = null;

    public Stmt(Block block, ArrayList<String> outputList) {
        this.kind = StmtType.Block;
        this.block = block;
        if (Parser.printFlag) {
            outputList.add("<Stmt>");
        }
    }

    // 'if' '(' Cond ')' Stmt [ 'else' Stmt ]
    public Cond ifCond = null;
    public Stmt ifStmt = null;
    public Stmt elseStmt = null;

    public Stmt(Cond ifCond, Stmt ifStmt, Stmt elseStmt, ArrayList<String> outputList) {
        this.kind = StmtType.IfElse;
        this.ifCond = ifCond;
        this.ifStmt = ifStmt;
        this.elseStmt = elseStmt;
        if (Parser.printFlag) {
            outputList.add("<Stmt>");
        }
    }

    // 'for' '(' [ForStmt] ';' [Cond] ';' [ForStmt] ')' Stmt
    public ForStmt forStmt1 = null;
    public Cond forCond = null;
    public ForStmt forStmt2 = null;

    public Stmt stmt = null;

    public Stmt(ForStmt forStmt1, Cond forCond, ForStmt forStmt2, Stmt stmt, ArrayList<String> outputList) {
        this.kind = StmtType.For;
        this.forStmt1 = forStmt1;
        this.forCond = forCond;
        this.forStmt2 = forStmt2;
        this.stmt = stmt;
        if (Parser.printFlag) {
            outputList.add("<Stmt>");
        }
    }

    // break | continue
    public Stmt(Token.tokenType tokenType, ArrayList<String> outputList) {
        if(tokenType== Token.tokenType.BREAKTK){
            this.kind = StmtType.Break;
        } else if(tokenType == Token.tokenType.CONTINUETK){
            this.kind = StmtType.Continue;
        }
        if (Parser.printFlag) {
            outputList.add("<Stmt>");
        }
    }
    // 'return' [Exp] ';'
    public Exp returnExp = null;
    public Stmt(Token.tokenType tokenType, Exp returnExp, ArrayList<String> outputList) {
        if(tokenType==Token.tokenType.RETURNTK){
            this.kind = StmtType.Return;
        }
        this.returnExp = returnExp;
        if (Parser.printFlag) {
            outputList.add("<Stmt>");
        }
    }

    // LVal '=' 'getint''('')'';
    public Stmt(LVal lVal ,Token.tokenType tokenType,ArrayList<String> outputList){
        if(tokenType==Token.tokenType.GETINTTK){
            this.kind = StmtType.Getint;
        } else if(tokenType == Token.tokenType.GETCHARTK){
            this.kind = StmtType.GetChar;
        }
        this.lVal = lVal;
        if (Parser.printFlag) {
            outputList.add("<Stmt>");
        }
    }

    // 'printf''('StringConst {','Exp}')'';'
    public String stringConst = null;
    public ArrayList<Exp> printExps = null;
    public Stmt(String stringConst, ArrayList<Exp> printExps, ArrayList<String> outputList) {
        this.kind = StmtType.Print;
        this.stringConst = stringConst;
        this.printExps = printExps;
        if (Parser.printFlag) {
            outputList.add("<Stmt>");
        }
    }
    public StmtType getKind(){
        return kind;
    }

}
