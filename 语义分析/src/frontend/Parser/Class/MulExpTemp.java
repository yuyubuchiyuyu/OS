package frontend.Parser.Class;

import frontend.Lexer.Token;

public class MulExpTemp {
    // MulExp' → ('*' | '/' | '%') UnaryExp MulExp' | ε
    private Token.tokenType tokenType;
    private UnaryExp unaryExp;
    private MulExpTemp mulExpTemp;

    public MulExpTemp(Token.tokenType tokenType, UnaryExp unaryExp) {
        this.tokenType = tokenType;
        this.unaryExp = unaryExp;
    }
    public MulExpTemp(Token.tokenType tokenType, UnaryExp unaryExp,MulExpTemp mulExpTemp) {
        this.tokenType = tokenType;
        this.unaryExp = unaryExp;
        this.mulExpTemp = mulExpTemp;
    }
}
