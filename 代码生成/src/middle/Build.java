package middle;

import frontend.Lexer.Token;
import frontend.Parser.Class.*;
import middle.Class.*;
import middle.Class.Instruction.AllocaInst;
import middle.Class.Instruction.StoreInst;
import middle.Class.IrType.IrType;
import middle.Class.Module;
import middle.Process.FunctionThing;
import middle.Symbol.Symbol;
import middle.Symbol.SymbolTable;

import java.util.ArrayList;

import static middle.Process.processConstDef.processConstDef;
import static middle.Process.processExpStmt.processExpStmt;
import static middle.Process.processFuncFParam.processFuncFParam;
import static middle.Process.processGetCharStmt.processGetCharStmt;
import static middle.Process.processGetIntStmt.processGetIntStmt;
import static middle.Process.processGlobalConstDef.processGlobalConstDef;
import static middle.Process.processGlobalVarDef.processGlobalVarDef;
import static middle.Process.processLValStmt.processLValStmt;
import static middle.Process.processPrintStmt.processPrintStmt;
import static middle.Process.processReturnStmt.processReturnStmt;
import static middle.Process.processStmt.processStmt;
import static middle.Process.processVarDef.processVarDef;

public class Build {
    public static ArrayList<StrStatement> strStatements = new ArrayList<>();
    public static middle.Class.Module buildModule(CompUnit compUnit, SymbolTable symbolTable) {
        // 全局变量
        ArrayList<GlobalVariable> globalVariables = new ArrayList<>();
        for (Decl decl : compUnit.getDecls()) {
            ArrayList<GlobalVariable> globalVariablesTemp = buildGlobalVariables(decl, symbolTable);
            globalVariables.addAll(globalVariablesTemp);
        }
        // 函数
        ArrayList<Function> functions = new ArrayList<>();
        for (FuncDef funcDef : compUnit.getFuncDefs()) {
            SymbolTable newTable = new SymbolTable(symbolTable);
            functions.add(buildFunction(funcDef, newTable, symbolTable));
        }

        // Main函数
        SymbolTable newTable = new SymbolTable(symbolTable);
        Function main = buildMainFunction(compUnit.getMainFunc(),newTable,symbolTable);
        return new Module(globalVariables, strStatements, functions, main);
    }


    private static ArrayList<GlobalVariable> buildGlobalVariables(Decl decl, SymbolTable symbolTable) {
        ArrayList<GlobalVariable> globalVariables = new ArrayList<>();
        if (decl instanceof ConstDecl constDecl) {
            BType bType = constDecl.getBType();
            for (ConstDef constDef : constDecl.getDefs()) {
                globalVariables.add(processGlobalConstDef(constDef, bType, symbolTable));
            }
        } else if (decl instanceof VarDecl varDecl) {
            BType bType = varDecl.getBType();
            for (VarDef varDef : varDecl.getDefs()) {
                globalVariables.add(processGlobalVarDef(varDef, bType, symbolTable));
            }
        }
        return globalVariables;
    }

    public static Function buildFunction(FuncDef funcDef, SymbolTable newTable, SymbolTable oldTable) {
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
        FunctionThing.funcIrType.put(name,resultType);
        FunctionThing.initFuncReturn();
        Symbol symbol = new Symbol(ident,name, resultType,2, bType, 1);
        oldTable.addSymbol(symbol);

        // 处理函数参数
        ArrayList<Argument> argumentTIES = new ArrayList<>();
        if (funcDef.getFuncParams() != null) {
            int count = 0;
            for (FuncFParam funcFParam : funcDef.getFuncParams().getFuncFParams()) {
                argumentTIES.add(processFuncFParam(funcFParam, newTable,count));
                count++;
            }
        }
        FunctionThing.arguments.put(name,argumentTIES);
        ArrayList<Value> instructions = new ArrayList<>();
        for(Argument argument:argumentTIES){
            symbol = newTable.search(argument.ident);
            String addressName = "%LocalVariable_" + Count.getFuncInner();
            AllocaInst allocaInst = new AllocaInst(addressName, symbol.irType);
            instructions.add(allocaInst);
            StoreInst storeInst = new StoreInst(symbol.irType, symbol.irName, addressName);
            instructions.add(storeInst);
            symbol.irName = addressName;
        }
        // 处理基本块
        ArrayList<BasicBlock> basicBlocks = buildBasicBlocks(funcDef.getBlock(), newTable,name,null,null);
        return new Function(resultType, name, argumentTIES, instructions, basicBlocks,FunctionThing.getFuncReturn());
    }


    public static ArrayList<BasicBlock> buildBasicBlocks(Block block, SymbolTable symbolTable,String funcName,String continueName,String breakName) {
        ArrayList<BasicBlock> basicBlocks = new ArrayList<>();
        if (block == null || block.getBlockItems().isEmpty()) {
            return basicBlocks;
        }
        for (BlockItem blockItem : block.getBlockItems()) {
            ArrayList<Value> instructions = new ArrayList<>();
            if(blockItem.getDecl()!=null){
                Decl decl = blockItem.getDecl();
                if (decl instanceof ConstDecl constDecl) {
                    BType bType = constDecl.getBType();
                    for (ConstDef constDef : constDecl.getDefs()) {
                        instructions.addAll(processConstDef(constDef, bType, symbolTable));
                    }
                } else if (decl instanceof VarDecl varDecl) {
                    BType bType = varDecl.getBType();
                    for (VarDef varDef : varDecl.getDefs()) {
                        instructions.addAll(processVarDef(varDef, bType, symbolTable));
                    }
                }
            } else if(blockItem.getStmt()!=null){
                Stmt stmt = blockItem.getStmt();
                instructions.addAll(processStmt(stmt,symbolTable,funcName,continueName,breakName));
            }
            BasicBlock basicBlock = new BasicBlock(instructions);
            basicBlocks.add(basicBlock);
        }
        return basicBlocks;
    }
    private static Function buildMainFunction(MainFuncDef mainFuncDef,  SymbolTable newTable, SymbolTable oldTable) {
        IrType resultType = new IrType(IrType.TypeID.IntegerTyID, 32);
        Integer bType = 0;    // 填表用

        String ident = "Main";
        String name = "main";
        Count.getFuncCount();
        FunctionThing.funcIrType.put(name,resultType);
        FunctionThing.initFuncReturn();
        Symbol symbol = new Symbol(ident,name, resultType,2, bType, 1, null);
        oldTable.addSymbol(symbol);

        // 处理基本块
        ArrayList<BasicBlock> basicBlocks = buildBasicBlocks(mainFuncDef.getBlock(), newTable,name,null,null);
        return new Function(resultType, name, new ArrayList<>(), basicBlocks,FunctionThing.getFuncReturn());
    }
}
