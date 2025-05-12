package frontend.Parser.Class;

import frontend.Lexer.Token;

public class BlockItem {
    private Decl decl = null;
    private Stmt stmt = null;

    public BlockItem(Decl decl) {
        this.decl = decl;
    }

    public BlockItem(Stmt stmt) {
        this.stmt = stmt;
    }

    public boolean getResult() { // 是否存在return语句
        if (stmt != null && stmt.getKind() == Stmt.StmtType.Return) {
            return true;
        }
        return false;
    }

    public Decl getDecl() {
        return decl;
    }

    public Stmt getStmt() {
        return stmt;
    }
}
