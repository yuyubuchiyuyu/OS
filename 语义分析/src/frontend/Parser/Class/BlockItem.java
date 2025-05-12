package frontend.Parser.Class;

import frontend.Lexer.Token;

public class BlockItem {
    private Decl decl;
    private Stmt stmt;

    public BlockItem(Decl decl) {
        this.decl = decl;
    }

    public BlockItem(Stmt stmt) {
        this.stmt = stmt;
    }

    public boolean getResult() { // 是否存在return语句
        if (stmt != null && stmt.getType() == Token.tokenType.RETURNTK) {
            return true;
        }
        return false;
    }


    /*
    public boolean getResult() {
        if (stmt == null || stmt.getType() != Token.tokenType.RETURNTK) {
            return false; // 没有return
        } else if (stmt.getType() == Token.tokenType.RETURNTK && stmt.getExp() == null) {
            return false; // 有空return
        }
        return true; // 有非空 return
    }
    */
}
