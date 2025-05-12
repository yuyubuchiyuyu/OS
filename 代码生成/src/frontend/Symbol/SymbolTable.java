package frontend.Symbol;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.Stack;

public class SymbolTable {
    Integer scopeNum; // 作用域编号
    Stack<SymbolLayer> scopes; // 当前作用域栈
    static ArrayList<Symbol> symbolList; // 符号表总表
    Symbol lastSymbol = null;

    public SymbolTable() {
        this.scopeNum = 1;
        this.scopes = new Stack<>();
        this.symbolList = new ArrayList<>();
        enterNewScope();
    }

    public ArrayList<Symbol> getSymbolList() {
        symbolList.sort(new Comparator<Symbol>() {
            public int compare(Symbol s1, Symbol s2) {
                return Integer.compare(s1.getScopeId(), s2.getScopeId());
            }
        });
        return symbolList;
    }

    public void enterNewScope() {
        SymbolLayer symbolLayer = new SymbolLayer(scopeNum, lastSymbol);
        scopes.push(symbolLayer);
        scopeNum++;
    }

    public void exitScope() {
        scopes.pop();
    }

    /*
    public Integer type;        // 0 -> var, 1 -> array, 2 -> func
    public Integer bType;    // 0 -> int, 1 -> char
    public Integer con;        // 0 -> const, 1 -> var
     */
    public void addSymbol(String name, Integer type, Integer bType, Integer con) {
        if (layerLookUpSymbol(name)) { // 该作用域上重名函数/变量不填表
            return;
        }

        Integer currentScopeId = scopes.peek().getScopeId();
        Symbol father = null;
        if (scopes.size() > 1) { // 同层父节点相同
            father = scopes.peek().father;
        }
        Symbol symbol = new Symbol(currentScopeId, symbolList.size(), name, type, bType, con, father);
        symbolList.add(symbol);
        scopes.peek().addSymbol(name, symbol);
        lastSymbol = symbol;
    }

    public boolean layerLookUpSymbol(String name) { // 该作用域存在定义的 常量/变量
        Map<String, Symbol> symbols = scopes.peek().getSymbols();
        return symbols.containsKey(name);
    }

    public boolean layerTopLookUpSymbol(String name) { // 该作用域或上级作用域存在定义的 常量/变量
        for (SymbolLayer symbolLayer : scopes) {
            Map<String, Symbol> symbols = symbolLayer.getSymbols();
            if (symbols.containsKey(name)) {
                return true;
            }
        }
        for (Symbol symbol : symbolList) {
            if (symbol.con == 0 && symbol.string.equals(name)) {
                return true;
            }
        }
        return false;
    }

    public boolean allConstLookUpSymbol(String name) { // 查找栈顶同名变量是否为常量
        for(Integer i = scopes.size()-1;i>=0;i--){
            SymbolLayer symbolLayer = scopes.get(i);
            for(Symbol symbol:symbolLayer.symbols.values()){
                if (symbol.string.equals(name) && symbol.con == 0) {
                    return true;
                }
                if (symbol.string.equals(name) && symbol.con != 0) {
                    return false;
                }
            }
        }
        return false;
    }

    public String searchRType(String ident, boolean arrayFlag) {
        for (Integer i = scopes.size() - 1; i >= 0; i--) {
            SymbolLayer symbolLayer = scopes.get(i);
            Map<String, Symbol> symbols = symbolLayer.symbols;
            if (symbols.containsKey(ident)) {
                Symbol symbol = symbols.get(ident);
                // System.out.println(symbol);
                if (symbol.type == 2) {
                    return symbol.getStrType().replace("Void", "").replace("Func", "");
                } else if (!arrayFlag) {
                    return symbol.getStrType().replace("Array", "");
                }
                return symbol.getStrType();
            }
        }
        if (ident.matches("\\d+")) {
            return "Int";
        }
        if (ident.matches("^'.{1}'$")) {
            return "Char";
        }
        return "";
    }

    public String searchFType(String fatherName, String ident) {
        for (Symbol symbol : symbolList) {
            if (symbol.string.equals(ident) && symbol.father != null && symbol.father.string.equals(fatherName)) {
                return symbol.getStrType();
            }
        }
        return "";
    }
}
