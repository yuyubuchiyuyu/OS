package backend;

import backend.Symbol.MipsSymbol;
import backend.Symbol.MipsSymbolTable;
import middle.Class.GlobalVariable;
import middle.Class.StrStatement;
import middle.Symbol.LLVMSymbol;

import java.util.ArrayList;

public class Data {
    public static String buildData(ArrayList<GlobalVariable> globalVariables, ArrayList<StrStatement> strStatements, MipsSymbolTable mipsSymbolTable) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(".data\n");
        stringBuilder.append(buildGlobalVariables(globalVariables, mipsSymbolTable));
        stringBuilder.append(buildStr(strStatements, mipsSymbolTable));
        return stringBuilder.toString();
    }

    public static String buildGlobalVariables(ArrayList<GlobalVariable> globalVariables, MipsSymbolTable mipsSymbolTable) {
        StringBuilder stringBuilder = new StringBuilder();
        for (GlobalVariable globalVariable : globalVariables) {
            LLVMSymbol LLVMSymbol = globalVariable.LLVMSymbol;
            String type = "word";
            stringBuilder.append("\t" + LLVMSymbol.irName.replace("@", "") + ": ." + type + " ");
            MipsSymbol mipsSymbol = null;
            if (LLVMSymbol.getDimension() == 0) {
                stringBuilder.append(LLVMSymbol.value + "\n");
                mipsSymbol = new MipsSymbol(LLVMSymbol.irName);
            } else {
                mipsSymbol = new MipsSymbol(LLVMSymbol.irName,LLVMSymbol.size);
                if (LLVMSymbol.ifArrayValueZero) {
                    stringBuilder.append("0:" + LLVMSymbol.size + "\n");
                } else {
                    for (int i = 0; i < LLVMSymbol.size; i++) {
                        if (i != LLVMSymbol.size - 1) {
                            stringBuilder.append(LLVMSymbol.arrayValue.get(i) + ", ");
                        } else {
                            stringBuilder.append(LLVMSymbol.arrayValue.get(i) + "\n");
                        }
                    }
                }
            }
            mipsSymbol.isGlobal = true;
            mipsSymbolTable.addSymbol(mipsSymbol);
        }
        return stringBuilder.toString();
    }

    public static String buildStr(ArrayList<StrStatement> strStatements, MipsSymbolTable mipsSymbolTable) {
        StringBuilder stringBuilder = new StringBuilder();
        for (StrStatement strStatement : strStatements) {
            MipsSymbol mipsSymbol = new MipsSymbol(strStatement.name);
            mipsSymbol.isGlobal = true;
            mipsSymbolTable.addSymbol(mipsSymbol);
            stringBuilder.append("\t" + strStatement.name.replace("@", "") +
                    ": .asciiz \"" + strStatement.text + "\" \n");
        }
        return stringBuilder.toString();
    }
}
