package frontend.Parser.Class;

import frontend.Lexer.Token;

public class MulExpTemp {
    // MulExp' → ('*' | '/' | '%') UnaryExp MulExp' | ε
    private Token.tokenType tokenType = null;
    private UnaryExp unaryExp = null;
    private MulExpTemp mulExpTemp = null;

    public MulExpTemp(Token.tokenType tokenType, UnaryExp unaryExp) {
        this.tokenType = tokenType;
        this.unaryExp = unaryExp;
    }

    public MulExpTemp(Token.tokenType tokenType, UnaryExp unaryExp, MulExpTemp mulExpTemp) {
        this.tokenType = tokenType;
        this.unaryExp = unaryExp;
        this.mulExpTemp = mulExpTemp;
    }
    public Token.tokenType getCharacterType() {
        return tokenType;
    }
    public UnaryExp getUnaryTemp() {
        return unaryExp;
    }

    public MulExpTemp getMulExpTemp() {
        return mulExpTemp;
    }

}
