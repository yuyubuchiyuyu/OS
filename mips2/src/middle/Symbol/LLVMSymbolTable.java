package middle.Symbol;

import java.util.*;

public class LLVMSymbolTable {


    private ArrayList<LLVMSymbol> LLVMSymbolList;
    private LLVMSymbolTable father;

    public LLVMSymbolTable(LLVMSymbolTable father) {
        this.LLVMSymbolList = new ArrayList<>();
        this.father = father;
    }

    public void addSymbol(LLVMSymbol LLVMSymbol) {
        LLVMSymbolList.add(LLVMSymbol);
    }

    public LLVMSymbol search(String ident) {
        LLVMSymbolTable LLVMSymbolTable = this;
        while(LLVMSymbolTable !=null){
            for(LLVMSymbol LLVMSymbol : LLVMSymbolTable.LLVMSymbolList){
                if(Objects.equals(LLVMSymbol.name, ident)){
                    return LLVMSymbol;
                }
            }
            LLVMSymbolTable = LLVMSymbolTable.father;
        }
        return null;
    }

    public LLVMSymbolTable getFather() {
        return father;
    }
}
