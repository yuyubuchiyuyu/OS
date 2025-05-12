package middle.Process;

import frontend.Parser.Class.Exp;
import frontend.Parser.Class.Stmt;
import middle.Build;
import middle.Class.Instruction.OutputInst;
import middle.Class.IrType.IrType;
import middle.Class.StrStatement;
import middle.Count;
import middle.Symbol.LLVMSymbolTable;
import middle.Value;

import java.util.ArrayList;

public class processPrintStmt {
    public static ArrayList<Value> processPrintStmt(Stmt stmt, LLVMSymbolTable LLVMSymbolTable) {
        ArrayList<Value> instructions = new ArrayList<>();
        ArrayList<Value> outputInstructions = new ArrayList<>();    // modify
        String stringConstTemp = stmt.stringConst;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < stringConstTemp.length() - 2; i++) {
            stringBuilder.append(stringConstTemp.charAt(i + 1));
        }
        String stringConst = stringBuilder.toString();
        ArrayList<Exp> exps = stmt.printExps;
        int index = 0;
        int i = 0;
        StringBuilder newStringBuilder = new StringBuilder();
        Integer size = 0;
        while (index < stringConst.length()) {
            if (index + 1 < stringConst.length()
                    && stringConst.charAt(index) == '%'
                    && (stringConst.charAt(index + 1) == 'd' || stringConst.charAt(index + 1) == 'c')) {
                if(!newStringBuilder.toString().isEmpty()){
                    String strName = "@.str." + Count.getStrCount();
                    newStringBuilder.append("\\").append("0").append("0");
                    size = size + 1;
                    OutputInst strOutputInst = new OutputInst(strName, size, newStringBuilder.toString());
                    // instructions.add(strOutputInst);
                    outputInstructions.add(strOutputInst);  // modify
                    Build.strStatements.add(new StrStatement(strName,size,newStringBuilder.toString()));
                    newStringBuilder = new StringBuilder();
                    size = 0;
                }
                Exp exp = exps.get(i);
                i++;
                if (exp.judgeCalculate(LLVMSymbolTable)) {  // 可计算的
                    Integer value = exp.calculate(LLVMSymbolTable);
                    OutputInst outputInst = null;
                    outputInst = new OutputInst(value, 'd', stringConst.charAt(index + 1));
                    instructions.addAll(outputInst.prepareInst());
                    // instructions.add(outputInst);
                    outputInstructions.add(outputInst);     // modify
                } else {
                    instructions.addAll(exp.getCalInstructions(LLVMSymbolTable));
                    IrType irType = instructions.get(instructions.size() - 1).getResIrType();
                    String name = instructions.get(instructions.size() - 1).getResName();
                    OutputInst outputInst = null;
                    if (irType.getNum() == 32) {    // int
                        outputInst = new OutputInst(name, 'd', stringConst.charAt(index + 1));
                    } else {    // char
                        outputInst = new OutputInst(name, 'c', stringConst.charAt(index + 1));
                    }
                    instructions.addAll(outputInst.prepareInst());
                    // instructions.add(outputInst);
                    outputInstructions.add(outputInst);     // modify
                }
                index = index + 2;
            } else if (index + 1 < stringConst.length() &&
                    stringConst.charAt(index) == '\\'&&
                    (stringConst.charAt(index+1)=='a'||stringConst.charAt(index+1)=='b'||
                            stringConst.charAt(index+1)=='t'||stringConst.charAt(index+1)=='n'||
                            stringConst.charAt(index+1)=='v'||stringConst.charAt(index+1)=='f'||
                            stringConst.charAt(index+1)=='\"'||stringConst.charAt(index+1)=='\''||
                            stringConst.charAt(index+1)=='\\'||stringConst.charAt(index+1)=='0')) {
                if(!newStringBuilder.toString().isEmpty()){
                    String strName = "@.str." + Count.getStrCount();
                    newStringBuilder.append("\\").append("0").append("0");
                    size = size + 1;
                    OutputInst strOutputInst = new OutputInst(strName, size, newStringBuilder.toString());
                    // instructions.add(strOutputInst);
                    outputInstructions.add(strOutputInst);      // modify
                    Build.strStatements.add(new StrStatement(strName,size,newStringBuilder.toString()));
                    newStringBuilder = new StringBuilder();
                    size = 0;
                }
                char temp = stringConst.charAt(index + 1);
                OutputInst outputInst = switch (temp) {
                    case 'a' -> new OutputInst(7, 'c', 'c');
                    case 'b' -> new OutputInst(8, 'c', 'c');
                    case 't' -> new OutputInst(9, 'c', 'c');
                    case 'n' -> new OutputInst(10, 'c', 'c');
                    case 'v' -> new OutputInst(11, 'c', 'c');
                    case 'f' -> new OutputInst(12, 'c', 'c');
                    case '\"' -> new OutputInst(34, 'c', 'c');
                    case '\'' -> new OutputInst(39, 'c', 'c');
                    case '\\' -> new OutputInst(92, 'c', 'c');
                    case '0' -> new OutputInst(0, 'c', 'c');
                    default -> null;
                };
                index = index + 2;
                // instructions.add(outputInst);
                outputInstructions.add(outputInst);     // modify
            } else {
                newStringBuilder.append(stringConst.charAt(index));
                size = size + 1;
                index = index + 1;
            }
        }
        if(!newStringBuilder.toString().isEmpty()){
            String strName = "@.str." + Count.getStrCount();
            newStringBuilder.append("\\").append("0").append("0");
            size = size + 1;
            OutputInst strOutputInst = new OutputInst(strName, size, newStringBuilder.toString());
            // instructions.add(strOutputInst);
            outputInstructions.add(strOutputInst);      // modify
            Build.strStatements.add(new StrStatement(strName,size,newStringBuilder.toString()));
        }
        instructions.addAll(outputInstructions);
        return instructions;
    }
}
