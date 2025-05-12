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

public class AddExp {
    // AddExp → MulExp AddExp'
    // AddExp' → ('+' | '−') MulExp AddExp' | ε
    public MulExp mulExp = null;
    public AddExpTemp addExpTemp = null;

    public AddExp(MulExp mulExp, ArrayList<String> outputList) {
        this.mulExp = mulExp;
        if (Parser.printFlag) {
            outputList.add("<AddExp>");
        }

    }

    public AddExp(MulExp mulExp, AddExpTemp addExpTemp, ArrayList<String> outputList) {
        this.mulExp = mulExp;
        this.addExpTemp = addExpTemp;
        if (Parser.printFlag) {
            outputList.add("<AddExp>");
        }
    }

    public Integer calculate(SymbolTable symbolTable) {
        Integer res = mulExp.calculate(symbolTable);

        AddExpTemp addTemp = this.addExpTemp;
        while (addTemp != null) {
            Token.tokenType character = addTemp.getCharacterType();
            MulExp mulTemp = addTemp.getMulTemp();

            if (character == Token.tokenType.PLUS) {
                res = res + mulTemp.calculate(symbolTable);
            } else if (character == Token.tokenType.MINU) {
                res = res - mulTemp.calculate(symbolTable);
            } else {
                System.out.println("addExp_error");
            }
            addTemp = addTemp.getAddExpTemp();
        }
        return res;
    }

    public boolean judgeCalculate(SymbolTable symbolTable) {
        if (!mulExp.judgeCalculate(symbolTable)) {
            return false;
        }

        AddExpTemp addTemp = this.addExpTemp;
        while (addTemp != null) {
            MulExp mulTemp = addTemp.getMulTemp();
            if (!mulTemp.judgeCalculate(symbolTable)) {
                return false;
            }
            addTemp = addTemp.getAddExpTemp();
        }
        return true;
    }

    public ArrayList<Value> getCalInstructions(SymbolTable symbolTable) {
        ArrayList<Value> instructions = new ArrayList<>();
        Integer num1 = null;
        String name1 = null;
        String resName = null;
        if (mulExp.judgeCalculate(symbolTable)) {
            num1 = mulExp.calculate(symbolTable);
        } else {
            instructions.addAll(mulExp.getCalInstructions(symbolTable));
            name1 = instructions.get(instructions.size() - 1).getResName();
        }

        AddExpTemp addTemp = this.addExpTemp;
        while (addTemp != null) {
            IrType irType = new IrType(IrType.TypeID.IntegerTyID, 32);
            if(name1!=null){
                TransferInst.typeTransfer(instructions,irType);
                name1 = instructions.get(instructions.size()-1).getResName();
            }
            Integer num2 = null;
            String name2 = null;

            Token.tokenType character = addTemp.getCharacterType();
            MulExp mulTemp = addTemp.getMulTemp();
            if(mulTemp.judgeCalculate(symbolTable)){
                num2 = mulTemp.calculate(symbolTable);
            } else {
                instructions.addAll(mulTemp.getCalInstructions(symbolTable));
                TransferInst.typeTransfer(instructions,irType);
                name2 = instructions.get(instructions.size() - 1).getResName();
            }

            resName = "%LocalVariable_" + Count.getFuncInner();

            BinaryOperator.BinaryOperatorType type = null;
            if (character == Token.tokenType.PLUS) {
                type = BinaryOperator.BinaryOperatorType.add;
            } else if (character == Token.tokenType.MINU) {
                type = BinaryOperator.BinaryOperatorType.sub;
            } else {
                System.out.println("addExp_error");
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

            addTemp = addTemp.getAddExpTemp();
        }
        return instructions;
    }



    public Symbol judgeSymbolPure(SymbolTable symbolTable) {
        if (this.addExpTemp == null) {
            return this.mulExp.judgeSymbolPure(symbolTable);
        } else {
            return null;
        }
    }

}
