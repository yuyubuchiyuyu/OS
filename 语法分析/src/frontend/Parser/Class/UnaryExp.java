package frontend.Parser.Class;

import frontend.Parser.Parser;

import java.util.ArrayList;

public class UnaryExp {
    // 一元表达式 UnaryExp → PrimaryExp | Ident '(' [FuncRParams] ')' | UnaryOp UnaryExp // j
    private UnaryOp unaryOp;
    private UnaryExp unaryExp;
    private String ident;
    private FuncRParams funcRParams;
    private PrimaryExp primaryExp;

    public UnaryExp(UnaryOp unaryOp, UnaryExp unaryExp, ArrayList<String> outputList) {
        this.unaryOp = unaryOp;
        this.unaryExp = unaryExp;
        if (Parser.printFlag) {
            outputList.add("<UnaryExp>");
        }
    }

    public UnaryExp(String ident, FuncRParams funcRParams, ArrayList<String> outputList) {
        this.ident = ident;
        this.funcRParams = funcRParams;
        if (Parser.printFlag) {
            outputList.add("<UnaryExp>");
        }
    }

    public UnaryExp(PrimaryExp primaryExp, ArrayList<String> outputList) {
        this.primaryExp = primaryExp;
        if (Parser.printFlag) {
            outputList.add("<UnaryExp>");
        }
    }
}
