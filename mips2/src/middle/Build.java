package middle;

import backend.Optimize;
import frontend.Lexer.Token;
import frontend.Parser.Class.*;
import middle.Class.*;
import middle.Class.Instruction.AllocaInst;
import middle.Class.Instruction.BranchInst;
import middle.Class.Instruction.StoreInst;
import middle.Class.Module;
import middle.Class.IrType.IrType;
import middle.Process.FunctionThing;
import middle.Process.processForStmt;
import middle.Symbol.LLVMSymbol;
import middle.Symbol.LLVMSymbolTable;

import java.util.ArrayList;

import static middle.Process.processConstDef.processConstDef;
import static middle.Process.processExpStmt.processExpStmt;
import static middle.Process.processFuncFParam.processFuncFParam;
import static middle.Process.processGetCharStmt.processGetCharStmt;
import static middle.Process.processGetIntStmt.processGetIntStmt;
import static middle.Process.processGlobalConstDef.processGlobalConstDef;
import static middle.Process.processGlobalVarDef.processGlobalVarDef;
import static middle.Process.processIfElseStmt.processIfElseStmt;
import static middle.Process.processLValStmt.processLValStmt;
import static middle.Process.processPrintStmt.processPrintStmt;
import static middle.Process.processReturnStmt.processReturnStmt;
import static middle.Process.processVarDef.processVarDef;


public class Build {
    public static ArrayList<StrStatement> strStatements = new ArrayList<>();

    public static middle.Class.Module buildModule(CompUnit compUnit, LLVMSymbolTable LLVMSymbolTable) {
        // 全局变量
        ArrayList<GlobalVariable> globalVariables = new ArrayList<>();
        for (Decl decl : compUnit.getDecls()) {
            ArrayList<GlobalVariable> globalVariablesTemp = buildGlobalVariables(decl, LLVMSymbolTable);
            globalVariables.addAll(globalVariablesTemp);
        }
        // 函数
        ArrayList<Function> functions = new ArrayList<>();
        for (FuncDef funcDef : compUnit.getFuncDefs()) {
            LLVMSymbolTable newTable = new LLVMSymbolTable(LLVMSymbolTable);
            functions.add(buildFunction(funcDef, newTable, LLVMSymbolTable));
        }

        // Main函数
        LLVMSymbolTable newTable = new LLVMSymbolTable(LLVMSymbolTable);
        Function main = buildMainFunction(compUnit.getMainFunc(), newTable, LLVMSymbolTable);
        return new Module(globalVariables, strStatements, functions, main);
    }


    private static ArrayList<GlobalVariable> buildGlobalVariables(Decl decl, LLVMSymbolTable LLVMSymbolTable) {
        ArrayList<GlobalVariable> globalVariables = new ArrayList<>();
        if (decl instanceof ConstDecl constDecl) {
            BType bType = constDecl.getBType();
            for (ConstDef constDef : constDecl.getDefs()) {
                globalVariables.add(processGlobalConstDef(constDef, bType, LLVMSymbolTable));
            }
        } else if (decl instanceof VarDecl varDecl) {
            BType bType = varDecl.getBType();
            for (VarDef varDef : varDecl.getDefs()) {
                globalVariables.add(processGlobalVarDef(varDef, bType, LLVMSymbolTable));
            }
        }
        return globalVariables;
    }

    public static Function buildFunction(FuncDef funcDef, LLVMSymbolTable newTable, LLVMSymbolTable oldTable) {
        // 把 func 写入 oldSymbol 表
        IrType resultType;
        Integer bType;    // 填表用
        if (funcDef.getFuncType().getFuncType() == Token.tokenType.VOIDTK) {
            resultType = new IrType(IrType.TypeID.VoidTyID);
            bType = -1;
        } else if (funcDef.getFuncType().getFuncType() == Token.tokenType.INTTK) {
            resultType = new IrType(IrType.TypeID.IntegerTyID, 32);
            bType = 0;
        } else {    // char
            resultType = new IrType(IrType.TypeID.IntegerTyID, 8);
            bType = 1;
        }
        String ident = funcDef.getName();
        String name = "func_" + Count.getFuncCount();
        FunctionThing.funcIrType.put(name, resultType);
        FunctionThing.initFuncReturn();
        LLVMSymbol LLVMSymbol = new LLVMSymbol(ident, name, resultType, 2, bType, 1);
        oldTable.addSymbol(LLVMSymbol);

        // 处理函数参数
        ArrayList<Argument> argumentTIES = new ArrayList<>();
        if (funcDef.getFuncParams() != null) {
            int count = 0;
            for (FuncFParam funcFParam : funcDef.getFuncParams().getFuncFParams()) {
                argumentTIES.add(processFuncFParam(funcFParam, newTable, count));
                count++;
            }
        }
        FunctionThing.arguments.put(name, argumentTIES);
        ArrayList<Value> instructions = new ArrayList<>();
        for (Argument argument : argumentTIES) {
            LLVMSymbol = newTable.search(argument.ident);
            String addressName = "%LocalVariable_" + Count.getFuncInner();
            AllocaInst allocaInst = new AllocaInst(addressName, LLVMSymbol.irType);
            instructions.add(allocaInst);
            StoreInst storeInst = new StoreInst(LLVMSymbol.irType, LLVMSymbol.irName, addressName);
            instructions.add(storeInst);
            LLVMSymbol.irName = addressName;
        }
        // 处理基本块
        ArrayList<BasicBlock> basicBlocks = buildBasicBlocks(funcDef.getBlock(), newTable, name, null, null);
        return new Function(resultType, name, argumentTIES, instructions, basicBlocks, FunctionThing.getFuncReturn());
    }


    public static ArrayList<BasicBlock> buildBasicBlocks(Block block, LLVMSymbolTable LLVMSymbolTable, String funcName, String continueName, String breakName) {
        ArrayList<BasicBlock> basicBlocks = new ArrayList<>();
        if (block == null || block.getBlockItems().isEmpty()) {
            return basicBlocks;
        }
        for (BlockItem blockItem : block.getBlockItems()) {
            ArrayList<Value> instructions = new ArrayList<>();
            boolean flag = false;
            boolean isFor = false;
            if (blockItem.getDecl() != null) {
                Decl decl = blockItem.getDecl();
                if (decl instanceof ConstDecl constDecl) {
                    BType bType = constDecl.getBType();
                    for (ConstDef constDef : constDecl.getDefs()) {
                        instructions.addAll(processConstDef(constDef, bType, LLVMSymbolTable));
                    }
                } else if (decl instanceof VarDecl varDecl) {
                    BType bType = varDecl.getBType();
                    for (VarDef varDef : varDecl.getDefs()) {
                        instructions.addAll(processVarDef(varDef, bType, LLVMSymbolTable));
                    }
                }
            } else if (blockItem.getStmt() != null) {
                Stmt stmt = blockItem.getStmt();
                if (stmt.getKind() == Stmt.StmtType.LVal) {
                    instructions.addAll(processLValStmt(stmt, LLVMSymbolTable));
                } else if (stmt.getKind() == Stmt.StmtType.Exp) {
                    instructions.addAll(processExpStmt(stmt, LLVMSymbolTable));
                } else if (stmt.getKind() == Stmt.StmtType.Block) {
                    LLVMSymbolTable newTable = new LLVMSymbolTable(LLVMSymbolTable);
                    ArrayList<BasicBlock> basicBlocksTemp = buildBasicBlocks(stmt.block, newTable, funcName, continueName, breakName);
                    for (BasicBlock basicBlock : basicBlocksTemp) {
                        instructions.addAll(basicBlock.getInstructions());
                    }
                } else if (stmt.getKind() == Stmt.StmtType.IfElse) {
                    instructions.addAll(processIfElseStmt(stmt, LLVMSymbolTable, funcName, continueName, breakName));
                } else if (stmt.getKind() == Stmt.StmtType.For) {
                    isFor = true;
                    instructions.addAll(processForStmt.processForStmt(stmt, LLVMSymbolTable, funcName));
                } else if (stmt.getKind() == Stmt.StmtType.Break) {
                    BranchInst branchInst = new BranchInst(breakName);
                    instructions.add(branchInst);
                    flag = true;
                } else if (stmt.getKind() == Stmt.StmtType.Continue) {
                    BranchInst branchInst = new BranchInst(continueName);
                    instructions.add(branchInst);
                    flag = true;
                } else if (stmt.getKind() == Stmt.StmtType.Return) {
                    FunctionThing.setFuncReturn();
                    instructions.addAll(processReturnStmt(stmt, LLVMSymbolTable, funcName));
                    flag = true;
                } else if (stmt.getKind() == Stmt.StmtType.Getint) {
                    instructions.addAll(processGetIntStmt(stmt, LLVMSymbolTable));
                } else if (stmt.getKind() == Stmt.StmtType.GetChar) {
                    instructions.addAll(processGetCharStmt(stmt, LLVMSymbolTable));
                } else if (stmt.getKind() == Stmt.StmtType.Print) {
                    instructions.addAll(processPrintStmt(stmt, LLVMSymbolTable));
                }
            }
            BasicBlock basicBlock = new BasicBlock(instructions);
            basicBlock.isFor = isFor;
            basicBlocks.add(basicBlock);
            if (Optimize.optimize2 && flag) {
                break;
            }
        }
        return basicBlocks;
    }

    private static Function buildMainFunction(MainFuncDef mainFuncDef, LLVMSymbolTable newTable, LLVMSymbolTable oldTable) {
        IrType resultType = new IrType(IrType.TypeID.IntegerTyID, 32);
        Integer bType = 0;    // 填表用

        String ident = "Main";
        String name = "main";
        Count.getFuncCount();
        FunctionThing.funcIrType.put(name, resultType);
        FunctionThing.initFuncReturn();
        LLVMSymbol LLVMSymbol = new LLVMSymbol(ident, name, resultType, 2, bType, 1, null);
        oldTable.addSymbol(LLVMSymbol);

        // 处理基本块
        ArrayList<BasicBlock> basicBlocks = buildBasicBlocks(mainFuncDef.getBlock(), newTable, name, null, null);
        return new Function(resultType, name, new ArrayList<>(), basicBlocks, FunctionThing.getFuncReturn());
    }
}
