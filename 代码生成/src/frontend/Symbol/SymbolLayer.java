package frontend.Symbol;

import java.util.HashMap;
import java.util.Map;

public class SymbolLayer {
    private Integer scopeId;
    Symbol father;
    String fatherName;
    Integer fatherTableId;
    Integer fatherScopeId;
    Map<String, Symbol> symbols;

    public SymbolLayer(Integer scopeId, Symbol father) {
        this.scopeId = scopeId;
        this.father = father;
        if (father != null) {
            this.fatherName = father.string;
            this.fatherTableId = father.tableId;
            this.fatherScopeId = father.scopeId;
        }
        this.symbols = new HashMap<>();
    }

    public Integer getScopeId() {
        return scopeId;
    }

    public void addSymbol(String name, Symbol symbol) {
        symbols.put(name, symbol);
    }

    public Map<String, Symbol> getSymbols() {
        return symbols;
    }

}
