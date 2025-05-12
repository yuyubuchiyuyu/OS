package frontend.Symbol;

import frontend.Parser.Class.Exp;

public class Symbol {
    public Integer scopeId;     // 作用域id
    public Integer tableId;    // 当前单词所在的符号表编号。

    public String string;    // 当前单词所对应的字符串。

    public Integer type;        // 0 -> var, 1 -> array, 2 -> func
    public Integer bType;    // 0 -> int, 1 -> char, -1 -> void
    public Integer con;        // 0 -> const, 1 -> var
    public Symbol father;

    public Symbol(Integer scopeId, Integer tableId, String string, Integer type, Integer bType, Integer con,Symbol father) {
        this.scopeId = scopeId;
        this.tableId = tableId;
        this.string = string;
        this.type = type;
        this.bType = bType;
        this.con = con;
        this.father=father;
    }


    public String toString() {
        if(string.equals("main")){
            return null;
        }
        return scopeId + " " + string + " " + getStrType();
    }

    public Integer getScopeId() {
        return scopeId;
    }

    public String getStrType() {
        String str = "";
        if (con == 0) {
            str = str.concat("Const");
        }
        if (bType == 0) {
            str = str.concat("Int");
        } else if(bType == 1){
            str = str.concat("Char");
        } else{
            str = str.concat("Void");
        }

        if (type == 1) {
            str = str.concat("Array");
        } else if (type == 2) {
            str = str.concat("Func");
        }
        return str;
    }
}
