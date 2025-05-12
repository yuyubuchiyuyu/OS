import frontend.Lexer;
import frontend.ProcessInput;

import java.util.ArrayList;
import java.io.*;
public class Compiler {
    private static boolean isOutput = true;

    public static void main(String[] args) throws IOException {
        String inputFileName = "testfile.txt";
        String outputFileName = "lexer.txt";
        String errorFileName = "error.txt";

        InputStream input = new FileInputStream(inputFileName);
        ProcessInput processInput = new ProcessInput(input);
        boolean result = processInput.process();

        if(isOutput){
            if (result) {
                PrintWriter outputWriter = new PrintWriter(new FileWriter(outputFileName));
                ArrayList<Lexer> lexer = processInput.getLexer();
                for (Lexer lex : lexer) {
                    outputWriter.println(lex.toString());
                }
                outputWriter.close();
            } else {
                PrintWriter errorWriter = new PrintWriter(new FileWriter(errorFileName));
                int errorLine = processInput.getErrorLine();
                char errorSign = processInput.getErrorSign();
                errorWriter.println(errorLine + " " + errorSign);
                errorWriter.close();
            }
        }
    }
}
