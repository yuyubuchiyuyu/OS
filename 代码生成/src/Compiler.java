import frontend.ErrorInfo;
import frontend.Lexer.Lexer;
import frontend.Lexer.ProcessInput;
import frontend.Parser.Class.CompUnit;
import frontend.Parser.Parser;
import frontend.Symbol.Symbol;
import middle.Class.Module;
import middle.Build;

import java.io.*;
import java.util.ArrayList;

public class Compiler {
    private static final Integer flag = 4; // 1 词法分析；2 语法分析；3 语义分析； 4 llvm ir
    private static boolean isOutput = true;

    public static void main(String[] args) throws IOException {
        String inputFileName = "testfile.txt";
        InputStream input = new FileInputStream(inputFileName);

        // 词法分析
        ProcessInput processInput = new ProcessInput(input);
        ArrayList<Lexer> lexerOutput = processInput.getLexer();
        ArrayList<ErrorInfo> errorLexer = processInput.getErrorList();

        // 语法分析
        Parser parser = new Parser(lexerOutput);
        ArrayList<String> parseOutput = parser.getOutput();
        ArrayList<ErrorInfo> errorParse = parser.getError();

        CompUnit compUnit = parser.getCompUnit();

        // 语义分析
        ArrayList<Symbol> symbolOutput = parser.getSymbol();

        // llvm ir
        middle.Symbol.SymbolTable llvmSymbolTable = new middle.Symbol.SymbolTable(null);
        Module module = Build.buildModule(compUnit,llvmSymbolTable);
        String llvmOutput = module.getOutput();

        // 错误输出
        ArrayList<ErrorInfo> newErrorList = merge(errorLexer, errorParse);

        if (isOutput) {
            if (flag == 1) {     // 词法分析
                PrintWriter outputWriter = new PrintWriter(new FileWriter("lexer.txt"));
                for (Lexer lexerOut : lexerOutput) {
                    outputWriter.println(lexerOut);
                    // System.out.println(lexerTemp);
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

            }
            PrintWriter errorWriter = new PrintWriter(new FileWriter("error.txt"));
            for (ErrorInfo e : newErrorList) {
                errorWriter.println(e.toString());
            }
            errorWriter.close();
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
