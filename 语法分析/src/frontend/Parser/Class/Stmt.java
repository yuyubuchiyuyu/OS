package frontend.Parser.Class;

import frontend.Lexer.Token;
import frontend.Parser.Parser;

import java.util.ArrayList;

public class Stmt {
    private LVal lVal;
    private Exp exp;
    private Block block;
    private Cond cond;
    private Stmt stmtIf;
    private Stmt stmtElse;
    private ForStmt forStmt1;
    private ForStmt forStmt2;
    private Stmt stmt;
    private Token.tokenType tokenType;
    private String stringConst;
    private ArrayList<Exp> exps;
    public Stmt(LVal lVal,Exp exp,ArrayList<String> outputList){
        this.lVal = lVal;
        this.exp = exp;
        if (Parser.printFlag) {
            outputList.add("<Stmt>");
        }
    }
    public Stmt(LVal lVal,ArrayList<String> outputList){
        this.lVal = lVal;
        if (Parser.printFlag) {
            outputList.add("<Stmt>");
        }
    }
    public Stmt(Block block,ArrayList<String> outputList){
        this.block = block;
        if (Parser.printFlag) {
            outputList.add("<Stmt>");
        }
    }
    public Stmt(Cond cond, Stmt stmtIf, Stmt stmtElse,ArrayList<String> outputList){
        this.cond = cond;
        this.stmtIf = stmtIf;
        this.stmtElse = stmtElse;
        if (Parser.printFlag) {
            outputList.add("<Stmt>");
        }
    }
    public Stmt(Cond cond, Stmt stmtIf,ArrayList<String> outputList){
        this.cond = cond;
        this.stmtIf = stmtIf;
        if (Parser.printFlag) {
            outputList.add("<Stmt>");
        }
    }
    public Stmt(ForStmt forStmt1, Cond cond, ForStmt forStmt2,Stmt stmt,ArrayList<String> outputList){
        this.forStmt1 = forStmt1;
        this.cond = cond;
        this.forStmt2 = forStmt2;
        this.stmt = stmt;
        if (Parser.printFlag) {
            outputList.add("<Stmt>");
        }
    }
    public Stmt(Token.tokenType tokenType,ArrayList<String> outputList){
        this.tokenType = tokenType;
        if (Parser.printFlag) {
            outputList.add("<Stmt>");
        }
    }
    public Stmt(Token.tokenType tokenType,Exp exp,ArrayList<String> outputList){
        this.tokenType = tokenType;
        this.exp = exp;
        if (Parser.printFlag) {
            outputList.add("<Stmt>");
        }
    }
    public Stmt(LVal lVal, Token.tokenType tokenType,ArrayList<String> outputList){
        this.lVal = lVal;
        this.tokenType = tokenType;
        if (Parser.printFlag) {
            outputList.add("<Stmt>");
        }
    }

    public Stmt(String stringConst, ArrayList<Exp> exps,ArrayList<String> outputList){
        this.stringConst = stringConst;
        this.exps = exps;
        if (Parser.printFlag) {
            outputList.add("<Stmt>");
        }
    }

    public Stmt(Exp exp, Token.tokenType tokenType, ArrayList<String> outputList) {
        this.exp = exp;
        this.tokenType = tokenType;
        if (Parser.printFlag) {
            outputList.add("<Stmt>");
        }
    }
}
