package frontend.Parser.Class;

public class BlockItem {
    private Decl decl;
    private Stmt stmt;
    public BlockItem(Decl decl){
        this.decl = decl;
    }
    public BlockItem(Stmt stmt){
        this.stmt = stmt;
    }
}
