package middle.Class;

import java.util.ArrayList;

public class Module {
    private ArrayList<GlobalVariable> globalVariables;
    private ArrayList<StrStatement> strStatements;
    private ArrayList<Function> functions;
    private Function main;

    public Module(ArrayList<GlobalVariable> globalVariables, ArrayList<StrStatement> strStatements,ArrayList<Function> functions, Function main) {
        this.globalVariables = globalVariables;
        this.strStatements = strStatements;
        this.functions = functions;
        this.main = main;
    }

    public String getOutput() {
        StringBuilder res = new StringBuilder();
        res.append("declare i32 @getint()\n" +
                "declare i32 @getchar()\n" +
                "declare void @putint(i32)\n" +
                "declare void @putch(i32)\n" +
                "declare void @putstr(i8*)\n");
        res.append("\n");
        for (GlobalVariable globalVariable : globalVariables) {
            res.append(globalVariable.getOutput());
            res.append("\n");
        }
        res.append("\n");
        for(StrStatement strStatement :strStatements){
            res.append(strStatement.getOutput());
            res.append("\n");
        }
        res.append("\n");
        for (Function function : functions) {
            res.append(function.getOutput());
            res.append("\n");
        }
        res.append("\n");
        res.append(main.getOutput());
        return res.toString();
    }

}
