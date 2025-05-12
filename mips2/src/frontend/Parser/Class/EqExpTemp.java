package frontend.Parser.Class;

import frontend.Lexer.Token;

public class EqExpTemp {
    // EqExp' → ('==' | '!=') RelExp EqExp' | ε
    private Token.tokenType type = null;
    private RelExp relExp = null;
    private EqExpTemp eqExpTemp = null;

    public EqExpTemp(Token.tokenType type, RelExp relExp, EqExpTemp eqExpTemp) {
        this.type = type;
        this.relExp = relExp;
        this.eqExpTemp = eqExpTemp;
    }

    public EqExpTemp(Token.tokenType type, RelExp relExp) {
        this.type = type;
        this.relExp = relExp;
    }

    public RelExp getRelTemp() {
        return relExp;
    }

    public EqExpTemp getEqExpTemp() {
        return eqExpTemp;
    }

    public Token.tokenType getCharacterType() {
        return type;
    }
}
