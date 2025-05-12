import frontend.Lexer.Lexer;
import frontend.Lexer.ProcessInput;
import frontend.ErrorInfo;
import frontend.Parser.Parser;
import frontend.Symbol.Symbol;
import frontend.Symbol.SymbolTable;

import java.io.*;
import java.util.ArrayList;

public class Compiler {
    private static boolean isOutput = true;

    public static void main(String[] args) throws IOException {
        String inputFileName = "testfile.txt";
        String outputFileName = "symbol.txt";
        String errorFileName = "error.txt";

        InputStream input = new FileInputStream(inputFileName);

        // 词法分析
        ProcessInput processInput = new ProcessInput(input);
        ArrayList<Lexer> lexer = processInput.getLexer();
        ArrayList<ErrorInfo> errorLexer = processInput.getErrorList();

        // 语法分析
        Parser parser = new Parser(lexer);
        ArrayList<String> parseOutput = parser.getOutput();
        ArrayList<ErrorInfo> errorParse = parser.getError();

        // 语义分析
        ArrayList<Symbol> symbolList = parser.getSymbol();

        // 错误输出
        ArrayList<ErrorInfo> newErrorList = merge(errorLexer,errorParse);

        if (isOutput) {
            if (errorLexer.isEmpty() && errorParse.isEmpty()) {
                System.out.println("successsssss");
                PrintWriter outputWriter = new PrintWriter(new FileWriter(outputFileName));
                for (Symbol s : symbolList) {
                    outputWriter.println(s.toString());
                }
                outputWriter.close();
            } else {
                PrintWriter errorWriter = new PrintWriter(new FileWriter(errorFileName));
                for (ErrorInfo e : newErrorList) {
                    errorWriter.println(e.toString());
                }
                errorWriter.close();
            }
        }
    }

    public static ArrayList<ErrorInfo> merge(ArrayList<ErrorInfo> errorList1, ArrayList<ErrorInfo> errorList2) {
        ArrayList<ErrorInfo> newErrorList = new ArrayList<>();
        int i = 0, j = 0;
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
        while(i<errorList1.size()){
            newErrorList.add(errorList1.get(i));
            i++;
        }
        while (j<errorList2.size()){
            newErrorList.add(errorList2.get(j));
            j++;
        }
        return newErrorList;
    }
}
