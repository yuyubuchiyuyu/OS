package frontend.Parser.Class;

import frontend.Lexer.Token;
import frontend.Parser.Parser;
import middle.Class.Instruction.*;
import middle.Class.IrType.IrType;
import middle.Count;
import middle.Symbol.Symbol;
import middle.Symbol.SymbolTable;
import middle.Value;

import java.util.ArrayList;

public class UnaryExp {
    // 一元表达式 UnaryExp → PrimaryExp | Ident '(' [FuncRParams] ')' | UnaryOp UnaryExp // j
    public UnaryOp unaryOp = null;
    public UnaryExp unaryExp = null;

    public String ident = null;
    public FuncRParams funcRParams = null;
    public PrimaryExp primaryExp = null;

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

    public Integer calculate(SymbolTable symbolTable) {
        if (this.primaryExp != null) {
            return this.primaryExp.calculate(symbolTable);
        } else if (this.ident != null) {
            return null;
        } else if (this.unaryOp != null) {
            Token.tokenType character = this.unaryOp.getType();
            if (character == Token.tokenType.PLUS) {
                return this.unaryExp.calculate(symbolTable);
            } else if (character == Token.tokenType.MINU) {
                return -this.unaryExp.calculate(symbolTable);
            } else if (character == Token.tokenType.NOT) {  // !a
                if (this.unaryExp.calculate(symbolTable) != 0) {
                    return 0;
                } else {
                    return 1;
                }
            }
        }
        return 0;
    }

    public boolean judgeCalculate(SymbolTable symbolTable) {
        if (this.primaryExp != null) {
            return this.primaryExp.judgeCalculate(symbolTable);
        } else if (this.ident != null) {
            return false;
        } else if (this.unaryOp != null) {
            return this.unaryExp.judgeCalculate(symbolTable);
        }
        return true;
    }

    public ArrayList<Value> getCalInstructions(SymbolTable symbolTable) {
        ArrayList<Value> instructions = new ArrayList<>();
        if (this.primaryExp != null) {
            return this.primaryExp.getCalInstructions(symbolTable);
        } else if (this.ident != null) {
            Symbol symbol = symbolTable.search(ident);
            String funcName = symbol.irName;
            IrType funcIrType = symbol.irType;
            String resName = "%LocalVariable_" + Count.getFuncInner();
            CallInst callInst = new CallInst(resName, funcIrType, funcName, funcRParams, symbolTable);
            instructions.addAll(callInst.prepareInst());
            instructions.add(callInst);
        } else if (this.unaryOp != null) {
            Token.tokenType character = this.unaryOp.getType();
            if (character == Token.tokenType.NOT) {
                instructions.addAll(unaryExp.getCalInstructions(symbolTable));
                IrType irType = instructions.get(instructions.size() - 1).getResIrType();
                String name = instructions.get(instructions.size() - 1).getResName();
                if(irType.getNum()!=1){
                    String compareName = "%LocalVariable_" + Count.getFuncInner();
                    CompareInst compareInst = new CompareInst(compareName, CompareInst.CompareType.ne,irType,name,0);
                    instructions.add(compareInst);
                }
                String resName = "%LocalVariable_" + Count.getFuncInner();
                UnaryOperator unaryOperator = new UnaryOperator(resName, UnaryOperator.UnaryOperatorType.xor,
                        instructions.get(instructions.size()-1).getResIrType(),
                        instructions.get(instructions.size()-1).getResName());
                instructions.add(unaryOperator);
            } else {
                instructions.addAll(unaryExp.getCalInstructions(symbolTable));
                IrType irType = instructions.get(instructions.size() - 1).getResIrType();
                String name2 = instructions.get(instructions.size() - 1).getResName();
                if (character == Token.tokenType.PLUS) {
                } else if (character == Token.tokenType.MINU) {
                    String resName = "%LocalVariable_" + Count.getFuncInner();
                    BinaryOperator binaryOperator = new BinaryOperator(resName, BinaryOperator.BinaryOperatorType.sub, irType, 0, name2);
                    instructions.add(binaryOperator);
                }

            }
        }
        return instructions;
    }

    public IrType getIrType(SymbolTable symbolTable) {
        if (this.primaryExp != null) {
            return this.primaryExp.getIrType(symbolTable);
        } else if (this.ident != null) {
            return symbolTable.search(ident).irType;
        } else {
            return unaryExp.getIrType(symbolTable);
        }
    }

    public Symbol judgeSymbolPure(SymbolTable symbolTable) {
        if (this.primaryExp != null) {
            return this.primaryExp.judgeSymbolPure(symbolTable);
        } else if (this.ident != null) {
            return symbolTable.search(ident);
        } else {
            return null;
        }
    }


}
