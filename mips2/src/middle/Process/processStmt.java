package middle.Process;

import frontend.Parser.Class.Stmt;
import middle.Class.BasicBlock;
import middle.Class.Instruction.BranchInst;
import middle.Symbol.LLVMSymbolTable;
import middle.Value;

import java.util.ArrayList;

import static middle.Build.buildBasicBlocks;
import static middle.Process.processIfElseStmt.processIfElseStmt;
import static middle.Process.processExpStmt.processExpStmt;
import static middle.Process.processGetCharStmt.processGetCharStmt;
import static middle.Process.processGetIntStmt.processGetIntStmt;
import static middle.Process.processLValStmt.processLValStmt;
import static middle.Process.processPrintStmt.processPrintStmt;
import static middle.Process.processReturnStmt.processReturnStmt;

public class processStmt {
    public static ArrayList<Value> processStmt(Stmt stmt, LLVMSymbolTable LLVMSymbolTable, String funcName, String continueName, String breakName) {
        ArrayList<Value> instructions = new ArrayList<>();
        if (stmt.getKind() == Stmt.StmtType.LVal) {
            instructions.addAll(processLValStmt(stmt, LLVMSymbolTable));
        } else if (stmt.getKind() == Stmt.StmtType.Exp) {
            instructions.addAll(processExpStmt(stmt, LLVMSymbolTable));
        } else if (stmt.getKind() == Stmt.StmtType.Block) {
            LLVMSymbolTable newTable = new LLVMSymbolTable(LLVMSymbolTable);
            ArrayList<BasicBlock> basicBlocksTemp = buildBasicBlocks(stmt.block, newTable, funcName,continueName,breakName);
            for (BasicBlock basicBlock : basicBlocksTemp) {
                instructions.addAll(basicBlock.getInstructions());
            }
        } else if (stmt.getKind() == Stmt.StmtType.IfElse) {
            instructions.addAll(processIfElseStmt(stmt, LLVMSymbolTable, funcName,continueName,breakName));
        } else if (stmt.getKind() == Stmt.StmtType.For) {
            instructions.addAll(processForStmt.processForStmt(stmt, LLVMSymbolTable,funcName));
        } else if(stmt.getKind() == Stmt.StmtType.Break){
            BranchInst branchInst = new BranchInst(breakName);
            instructions.add(branchInst);
        } else if (stmt.getKind() == Stmt.StmtType.Continue) {
            BranchInst branchInst = new BranchInst(continueName);
            instructions.add(branchInst);
        } else if (stmt.getKind() == Stmt.StmtType.Return) {
            FunctionThing.setFuncReturn();
            instructions.addAll(processReturnStmt(stmt, LLVMSymbolTable, funcName));
        } else if (stmt.getKind() == Stmt.StmtType.Getint) {
            instructions.addAll(processGetIntStmt(stmt, LLVMSymbolTable));
        } else if (stmt.getKind() == Stmt.StmtType.GetChar) {
            instructions.addAll(processGetCharStmt(stmt, LLVMSymbolTable));
        } else if (stmt.getKind() == Stmt.StmtType.Print) {
            instructions.addAll(processPrintStmt(stmt, LLVMSymbolTable));
        }
        return instructions;
    }
}
