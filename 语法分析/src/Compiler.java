import frontend.Lexer.Lexer;
import frontend.Lexer.ProcessInput;
import frontend.Parser.ErrorInfo;
import frontend.Parser.Parser;

import java.io.*;
import java.util.ArrayList;

public class Compiler {
    private static boolean isOutput = true;

    public static void main(String[] args) throws IOException {
        String inputFileName = "testfile.txt";
        String outputFileName = "parser.txt";
        String errorFileName = "error.txt";

        InputStream input = new FileInputStream(inputFileName);
        ProcessInput processInput = new ProcessInput(input);
        ArrayList<Lexer> lexer = processInput.getLexer();
        ArrayList<ErrorInfo> errorLexer = processInput.getErrorList();

        /*
        for(Lexer lexerTemp:lexer){
            System.out.println(lexerTemp);
        }
         */

        Parser parser = new Parser(lexer);
        ArrayList<String> parseOutput = parser.getOutput();
        ArrayList<ErrorInfo> errorParse = parser.getError();

        /*

        for(String str:parseOutput){
            System.out.println(str);
        }

         */


        ArrayList<ErrorInfo> newErrorList = merge(errorLexer,errorParse);

        /*
        for (ErrorInfo errorInfo : newErrorList) {
            System.out.println(errorInfo);
        }
         */

        if (isOutput) {
            if (errorLexer.isEmpty() && errorParse.isEmpty()) {
                PrintWriter outputWriter = new PrintWriter(new FileWriter(outputFileName));
                for (String s : parseOutput) {
                    outputWriter.println(s);
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
