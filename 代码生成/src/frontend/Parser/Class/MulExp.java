package frontend.Parser.Class;

import frontend.Lexer.Token;
import frontend.Parser.Parser;
import middle.Class.Instruction.BinaryOperator;
import middle.Class.Instruction.TransferInst;
import middle.Class.IrType.IrType;
import middle.Count;
import middle.Symbol.Symbol;
import middle.Symbol.SymbolTable;
import middle.Value;

import java.util.ArrayList;

public class MulExp {
    // MulExp → UnaryExp MulExp'
    // MulExp' → ('*' | '/' | '%') UnaryExp MulExp' | ε
    public UnaryExp unaryExp = null;
    public MulExpTemp mulExpTemp = null;

    public MulExp(UnaryExp unaryExp, ArrayList<String> outputList) {
        this.unaryExp = unaryExp;
        if (Parser.printFlag) {
            outputList.add("<MulExp>");
        }
    }

    public MulExp(UnaryExp unaryExp, MulExpTemp mulExpTemp, ArrayList<String> outputList) {
        this.unaryExp = unaryExp;
        this.mulExpTemp = mulExpTemp;
        if (Parser.printFlag) {
            outputList.add("<MulExp>");
        }
    }

    public Integer calculate(SymbolTable symbolTable) {
        Integer res = unaryExp.calculate(symbolTable);

        MulExpTemp mulTemp = this.mulExpTemp;
        while (mulTemp != null) {
            Token.tokenType character = mulTemp.getCharacterType();
            UnaryExp unaryTemp = mulTemp.getUnaryTemp();
            if (character == Token.tokenType.MULT) {
                res = res * unaryTemp.calculate(symbolTable);
            } else if (character == Token.tokenType.DIV) {
                res = res / unaryTemp.calculate(symbolTable);
            } else if (character == Token.tokenType.MOD) {
                res = res % unaryTemp.calculate(symbolTable);
            } else {
                System.out.println("mulExp_error");
            }
            mulTemp = mulTemp.getMulExpTemp();
        }
        return res;
    }

    public boolean judgeCalculate(SymbolTable symbolTable) {
        if (!unaryExp.judgeCalculate(symbolTable)) {
            return false;
        }
        MulExpTemp mulTemp = this.mulExpTemp;
        while (mulTemp != null) {

            UnaryExp unaryTemp = mulTemp.getUnaryTemp();
            if (!unaryTemp.judgeCalculate(symbolTable)) {
                return false;
            }
            mulTemp = mulTemp.getMulExpTemp();
        }
        return true;
    }

    public ArrayList<Value> getCalInstructions(SymbolTable symbolTable) {
        ArrayList<Value> instructions = new ArrayList<>();
        Integer num1 = null;
        String name1 = null;
        String resName = null;
        if (unaryExp.judgeCalculate(symbolTable)) {
            num1 = unaryExp.calculate(symbolTable);
        } else {
            instructions.addAll(unaryExp.getCalInstructions(symbolTable));
            name1 = instructions.get(instructions.size() - 1).getResName();
        }


        MulExpTemp mulTemp = this.mulExpTemp;
        while (mulTemp != null) {
            IrType irType = new IrType(IrType.TypeID.IntegerTyID, 32);
            if(name1!=null){
                TransferInst.typeTransfer(instructions,irType);
                name1 = instructions.get(instructions.size()-1).getResName();
            }

            Integer num2 = null;
            String name2 = null;

            Token.tokenType character = mulTemp.getCharacterType();
            UnaryExp unaryTemp = mulTemp.getUnaryTemp();
            if (unaryTemp.judgeCalculate(symbolTable)) {
                num2 = unaryTemp.calculate(symbolTable);
            } else {
                instructions.addAll(unaryTemp.getCalInstructions(symbolTable));
                TransferInst.typeTransfer(instructions,irType);
                name2 = instructions.get(instructions.size() - 1).getResName();
            }

            resName = "%LocalVariable_" + Count.getFuncInner();

            BinaryOperator.BinaryOperatorType type = null;
            if (character == Token.tokenType.MULT) {
                type = BinaryOperator.BinaryOperatorType.mul;
            } else if (character == Token.tokenType.DIV) {
                type = BinaryOperator.BinaryOperatorType.sdiv;
            } else if (character == Token.tokenType.MOD) {
                type = BinaryOperator.BinaryOperatorType.srem;
            } else {
                System.out.println("mulExp_error");
            }
            BinaryOperator binaryOperator;
            if (name1 != null && name2 != null) {
                binaryOperator = new BinaryOperator(resName, type, irType, name1, name2);
            } else if (name1 != null && num2 != null) {
                binaryOperator = new BinaryOperator(resName, type, irType, name1, num2);
            } else if (num1 != null && name2 != null) {
                binaryOperator = new BinaryOperator(resName, type, irType, num1, name2);
            } else {
                binaryOperator = new BinaryOperator(resName, type, irType, num1, num2);
            }
            instructions.add(binaryOperator);
            name1 = instructions.get(instructions.size() - 1).getResName();
            num1 = null;

            mulTemp = mulTemp.getMulExpTemp();

        }
        return instructions;
    }

    public Symbol judgeSymbolPure(SymbolTable symbolTable) {
        if (this.mulExpTemp == null) {
            return this.unaryExp.judgeSymbolPure(symbolTable);
        } else {
            return null;
        }
    }

}
