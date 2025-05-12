import backend.Mips;
import frontend.ErrorInfo;
import frontend.Lexer.Lexer;
import frontend.Lexer.ProcessInput;
import frontend.Parser.Class.CompUnit;
import frontend.Parser.Parser;
import frontend.Symbol.Symbol;
import middle.Build;
import middle.Class.Module;
import middle.Symbol.LLVMSymbolTable;

import java.io.*;
import java.util.ArrayList;

public class Compiler {
    private static final Integer flag = 5; // 1 词法分析；2 语法分析；3 语义分析； 4 llvm ir；5 mips
    private static boolean isOutput = true;


    public static void main(String[] args) throws IOException {
        String inputFileName = "testfile.txt";
        InputStream input = new FileInputStream(inputFileName);

        // 词法分析
        ProcessInput processInput = new ProcessInput(input);
        ArrayList<Lexer> lexerOutput = processInput.getLexer();
        ArrayList<ErrorInfo> errorLexer = processInput.getErrorList();

        /*
        for (Lexer lexerOut : lexerOutput) {
            System.out.println(lexerOut);
        }
        */

        // 语法分析
        Parser parser = new Parser(lexerOutput);
        ArrayList<String> parseOutput = parser.getOutput();
        ArrayList<ErrorInfo> errorParse = parser.getError();

        /*
        for (String parseOut : parseOutput) {
            System.out.println(parseOut);
        }
        */

        CompUnit compUnit = parser.getCompUnit();

        // 语义分析
        ArrayList<Symbol> symbolOutput = parser.getSymbol();

        // 错误输出
        ArrayList<ErrorInfo> newErrorList = merge(errorLexer, errorParse);

        if (!newErrorList.isEmpty() && flag >= 2) {
            PrintWriter errorWriter = new PrintWriter(new FileWriter("error.txt"));
            for (ErrorInfo e : newErrorList) {
                errorWriter.println(e.toString());
                System.out.println(e.toString());
            }
            errorWriter.close();
            return;
        }

        // llvm ir
        LLVMSymbolTable llvmSymbolTable = new LLVMSymbolTable(null);
        Module module = Build.buildModule(compUnit, llvmSymbolTable);
        String llvmOutput = module.getOutput();

        /*
        PrintWriter outputWriterLLVM = new PrintWriter(new FileWriter("llvm_ir.txt"));
        outputWriterLLVM.println(llvmOutput);
        System.out.println(llvmOutput);
        outputWriterLLVM.close();
        */

        // mips
        Mips mips = new Mips(module);
        String mipsOutput = mips.getOutput();
        // System.out.println(mipsOutput);

        if (isOutput) {
            if (flag == 1) {     // 词法分析
                PrintWriter outputWriter = new PrintWriter(new FileWriter("lexer.txt"));
                for (Lexer lexerOut : lexerOutput) {
                    outputWriter.println(lexerOut);
                    // System.out.println(lexerOut);
                }
                outputWriter.close();
            } else if (flag == 2) {     // 语法分析
                PrintWriter outputWriter = new PrintWriter(new FileWriter("parser.txt"));
                for (String parseOut : parseOutput) {
                    outputWriter.println(parseOut);
                    // System.out.println(parseOut);
                }
                outputWriter.close();
            } else if (flag == 3) {     // 语义分析
                PrintWriter outputWriter = new PrintWriter(new FileWriter("symbol.txt"));
                for (Symbol symbolOut : symbolOutput) {
                    outputWriter.println(symbolOut);
                    // System.out.println(symbolOut);
                }
                outputWriter.close();
            } else if (flag == 4) {     // llvm
                PrintWriter outputWriter = new PrintWriter(new FileWriter("llvm_ir.txt"));
                outputWriter.println(llvmOutput);
                // System.out.println(llvmOutput);
                outputWriter.close();
            } else if (flag == 5) {     // mips
                PrintWriter outputWriter = new PrintWriter(new FileWriter("mips.txt"));
                outputWriter.println(mipsOutput);
                // System.out.println(mipsOutput);
                outputWriter.close();
            }
        }
    }

    public static ArrayList<ErrorInfo> merge(ArrayList<ErrorInfo> errorList1, ArrayList<ErrorInfo> errorList2) {
        ArrayList<ErrorInfo> newErrorList = new ArrayList<>();
        Integer i = 0, j = 0;
        while (i < errorList1.size() && j < errorList2.size()) {
            ErrorInfo error1 = errorList1.get(i);
            ErrorInfo error2 = errorList2.get(j);
            if (error1.getLine() <= error2.getLine()) {
                newErrorList.add(error1);
                i++;
            } else {
                newErrorList.add(error2);
                j++;
            }
        }
        while (i < errorList1.size()) {
            newErrorList.add(errorList1.get(i));
            i++;
        }
        while (j < errorList2.size()) {
            newErrorList.add(errorList2.get(j));
            j++;
        }
        return newErrorList;
    }
}
