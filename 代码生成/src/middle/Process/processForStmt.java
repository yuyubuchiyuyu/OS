package middle.Process;

import frontend.Parser.Class.Stmt;
import middle.Class.Instruction.BranchInst;
import middle.Class.Instruction.LabelInst;
import middle.Count;
import middle.Symbol.SymbolTable;
import middle.Value;

import java.util.ArrayList;

public class processForStmt {
    public static ArrayList<Value> processForStmt(Stmt stmt, SymbolTable symbolTable, String funcName) {
        ArrayList<Value> instructions = new ArrayList<>();
        String cond_name = "%ForCond_" + Count.getForCondLabel();
        String forStmt2_name = "%ForStmt2_" + Count.getForStmt2Label();
        String mainStmt_name = "%mainStmt_" + Count.getForMainStmtLabel();
        String forEnd_name = "%ForEnd_" + Count.getForEndLabel();

        if (stmt.forStmt1 != null && stmt.forCond != null && stmt.forStmt2 != null) {
            return processFor1(stmt, symbolTable, funcName, cond_name, forStmt2_name, mainStmt_name, forEnd_name);
        } else if (stmt.forStmt1 == null && stmt.forCond != null && stmt.forStmt2 != null) {
            return processFor2(stmt, symbolTable, funcName, cond_name, forStmt2_name, mainStmt_name, forEnd_name);
        } else if (stmt.forStmt1 != null && stmt.forCond == null && stmt.forStmt2 != null) {
            return processFor3(stmt, symbolTable, funcName, cond_name, forStmt2_name, mainStmt_name, forEnd_name);
        } else if (stmt.forStmt1 != null && stmt.forCond != null && stmt.forStmt2 == null) {
            return processFor4(stmt, symbolTable, funcName, cond_name, forStmt2_name, mainStmt_name, forEnd_name);
        } else if (stmt.forStmt1 == null && stmt.forCond == null && stmt.forStmt2 != null) {
            return processFor5(stmt, symbolTable, funcName, cond_name, forStmt2_name, mainStmt_name, forEnd_name);
        }else if (stmt.forStmt1 == null && stmt.forCond != null && stmt.forStmt2 == null) {
            return processFor6(stmt, symbolTable, funcName, cond_name, forStmt2_name, mainStmt_name, forEnd_name);
        }else if (stmt.forStmt1 != null && stmt.forCond == null && stmt.forStmt2 == null) {
            return processFor7(stmt, symbolTable, funcName, cond_name, forStmt2_name, mainStmt_name, forEnd_name);
        } else if (stmt.forStmt1 == null && stmt.forCond == null && stmt.forStmt2 == null) {
            return processFor8(stmt, symbolTable, funcName, cond_name, forStmt2_name, mainStmt_name, forEnd_name);
        }

        return instructions;
    }

    // 全有
    public static ArrayList<Value> processFor1(Stmt stmt, SymbolTable symbolTable, String funcName,
                                               String cond_name, String forStmt2_name,
                                               String mainStmt_name, String forEnd_name) {
        ArrayList<Value> instructions = new ArrayList<>();

        // forStmt1
        Stmt forStmt1 = new Stmt(stmt.forStmt1.lVal, stmt.forStmt1.exp, new ArrayList<>());
        instructions.addAll(processLValStmt.processLValStmt(forStmt1, symbolTable));
        instructions.add(new BranchInst(cond_name));

        // cond
        instructions.add(new LabelInst(cond_name));
        instructions.addAll(processIfElseStmt.processCond(stmt.forCond, symbolTable, mainStmt_name, forEnd_name));

        // main
        instructions.add(new LabelInst(mainStmt_name));
        instructions.addAll(processStmt.processStmt(stmt.stmt, symbolTable, funcName, forStmt2_name, forEnd_name));
        instructions.add(new BranchInst(forStmt2_name));

        // forStmt2
        instructions.add(new LabelInst(forStmt2_name));
        Stmt forStmt2 = new Stmt(stmt.forStmt2.lVal, stmt.forStmt2.exp, new ArrayList<>());
        instructions.addAll(processLValStmt.processLValStmt(forStmt2, symbolTable));
        instructions.add(new BranchInst(cond_name));

        // end
        instructions.add(new LabelInst(forEnd_name));
        return instructions;
    }


    // 没 forStmt1
    public static ArrayList<Value> processFor2(Stmt stmt, SymbolTable symbolTable, String funcName,
                                               String cond_name, String forStmt2_name,
                                               String mainStmt_name, String forEnd_name) {

        ArrayList<Value> instructions = new ArrayList<>();

        instructions.add(new BranchInst(cond_name));

        // cond
        instructions.add(new LabelInst(cond_name));
        instructions.addAll(processIfElseStmt.processCond(stmt.forCond, symbolTable, mainStmt_name, forEnd_name));

        // main
        instructions.add(new LabelInst(mainStmt_name));
        instructions.addAll(processStmt.processStmt(stmt.stmt, symbolTable, funcName, forStmt2_name, forEnd_name));
        instructions.add(new BranchInst(forStmt2_name));

        // forStmt2
        instructions.add(new LabelInst(forStmt2_name));
        Stmt forStmt2 = new Stmt(stmt.forStmt2.lVal, stmt.forStmt2.exp, new ArrayList<>());
        instructions.addAll(processLValStmt.processLValStmt(forStmt2, symbolTable));
        instructions.add(new BranchInst(cond_name));

        // end
        instructions.add(new LabelInst(forEnd_name));
        return instructions;
    }

    // 没 cond
    public static ArrayList<Value> processFor3(Stmt stmt, SymbolTable symbolTable, String funcName,
                                               String cond_name, String forStmt2_name,
                                               String mainStmt_name, String forEnd_name) {
        ArrayList<Value> instructions = new ArrayList<>();

        // forStmt1
        Stmt forStmt1 = new Stmt(stmt.forStmt1.lVal, stmt.forStmt1.exp, new ArrayList<>());
        instructions.addAll(processLValStmt.processLValStmt(forStmt1, symbolTable));
        instructions.add(new BranchInst(mainStmt_name));

        // main
        instructions.add(new LabelInst(mainStmt_name));
        instructions.addAll(processStmt.processStmt(stmt.stmt, symbolTable, funcName, forStmt2_name, forEnd_name));
        instructions.add(new BranchInst(forStmt2_name));

        // forStmt2
        instructions.add(new LabelInst(forStmt2_name));
        Stmt forStmt2 = new Stmt(stmt.forStmt2.lVal, stmt.forStmt2.exp, new ArrayList<>());
        instructions.addAll(processLValStmt.processLValStmt(forStmt2, symbolTable));
        instructions.add(new BranchInst(mainStmt_name));

        // end
        instructions.add(new LabelInst(forEnd_name));
        return instructions;
    }

    // 没 forStmt2
    public static ArrayList<Value> processFor4(Stmt stmt, SymbolTable symbolTable, String funcName,
                                               String cond_name, String forStmt2_name,
                                               String mainStmt_name, String forEnd_name) {
        ArrayList<Value> instructions = new ArrayList<>();

        // forStmt1
        Stmt forStmt1 = new Stmt(stmt.forStmt1.lVal, stmt.forStmt1.exp, new ArrayList<>());
        instructions.addAll(processLValStmt.processLValStmt(forStmt1, symbolTable));
        instructions.add(new BranchInst(cond_name));

        // cond
        instructions.add(new LabelInst(cond_name));
        instructions.addAll(processIfElseStmt.processCond(stmt.forCond, symbolTable, mainStmt_name, forEnd_name));

        // main
        instructions.add(new LabelInst(mainStmt_name));
        instructions.addAll(processStmt.processStmt(stmt.stmt, symbolTable, funcName, cond_name, forEnd_name));
        instructions.add(new BranchInst(cond_name));


        // end
        instructions.add(new LabelInst(forEnd_name));
        return instructions;
    }
    // 没 forStmt1 cond
    public static ArrayList<Value> processFor5(Stmt stmt, SymbolTable symbolTable, String funcName,
                                               String cond_name, String forStmt2_name,
                                               String mainStmt_name, String forEnd_name) {
        ArrayList<Value> instructions = new ArrayList<>();


        instructions.add(new BranchInst(mainStmt_name));

        // main
        instructions.add(new LabelInst(mainStmt_name));
        instructions.addAll(processStmt.processStmt(stmt.stmt, symbolTable, funcName, forStmt2_name, forEnd_name));
        instructions.add(new BranchInst(forStmt2_name));

        // forStmt2
        instructions.add(new LabelInst(forStmt2_name));
        Stmt forStmt2 = new Stmt(stmt.forStmt2.lVal, stmt.forStmt2.exp, new ArrayList<>());
        instructions.addAll(processLValStmt.processLValStmt(forStmt2, symbolTable));
        instructions.add(new BranchInst(mainStmt_name));

        // end
        instructions.add(new LabelInst(forEnd_name));
        return instructions;
    }
    // 没有forStmt1 forStmt2
    public static ArrayList<Value> processFor6(Stmt stmt, SymbolTable symbolTable, String funcName,
                                               String cond_name, String forStmt2_name,
                                               String mainStmt_name, String forEnd_name) {
        ArrayList<Value> instructions = new ArrayList<>();

        instructions.add(new BranchInst(cond_name));

        // cond
        instructions.add(new LabelInst(cond_name));
        instructions.addAll(processIfElseStmt.processCond(stmt.forCond, symbolTable, mainStmt_name, forEnd_name));

        // main
        instructions.add(new LabelInst(mainStmt_name));
        instructions.addAll(processStmt.processStmt(stmt.stmt, symbolTable, funcName, cond_name, forEnd_name));
        instructions.add(new BranchInst(cond_name));

        // end
        instructions.add(new LabelInst(forEnd_name));
        return instructions;
    }
    // 没有 cond forStmt2
    public static ArrayList<Value> processFor7(Stmt stmt, SymbolTable symbolTable, String funcName,
                                               String cond_name, String forStmt2_name,
                                               String mainStmt_name, String forEnd_name) {
        ArrayList<Value> instructions = new ArrayList<>();

        // forStmt1
        Stmt forStmt1 = new Stmt(stmt.forStmt1.lVal, stmt.forStmt1.exp, new ArrayList<>());
        instructions.addAll(processLValStmt.processLValStmt(forStmt1, symbolTable));
        instructions.add(new BranchInst(mainStmt_name));

        // main
        instructions.add(new LabelInst(mainStmt_name));
        instructions.addAll(processStmt.processStmt(stmt.stmt, symbolTable, funcName, mainStmt_name, forEnd_name));
        instructions.add(new BranchInst(mainStmt_name));

        // end
        instructions.add(new LabelInst(forEnd_name));
        return instructions;
    }
    // 全没
    public static ArrayList<Value> processFor8(Stmt stmt, SymbolTable symbolTable, String funcName,
                                               String cond_name, String forStmt2_name,
                                               String mainStmt_name, String forEnd_name) {
        ArrayList<Value> instructions = new ArrayList<>();

        instructions.add(new BranchInst(mainStmt_name));

        // main
        instructions.add(new LabelInst(mainStmt_name));
        instructions.addAll(processStmt.processStmt(stmt.stmt, symbolTable, funcName, mainStmt_name, forEnd_name));
        instructions.add(new BranchInst(mainStmt_name));

        // end
        instructions.add(new LabelInst(forEnd_name));
        return instructions;
    }
}
