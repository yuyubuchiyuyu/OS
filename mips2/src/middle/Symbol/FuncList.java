package middle.Symbol;

import java.util.HashMap;

public class FuncList {
    private HashMap<String, LLVMSymbolTable> functions;

    public FuncList() {
        this.functions = new HashMap<>();
    }

    public void addFunc(String ident, LLVMSymbolTable LLVMSymbolTable) {
        functions.put(ident, LLVMSymbolTable);
    }
}
