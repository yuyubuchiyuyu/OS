package frontend.Parser.Class;

import frontend.Lexer.Token;

public class EqExpTemp {
    // EqExp' → ('==' | '!=') RelExp EqExp' | ε
    private Token.tokenType type;
    private RelExp relExp;
    private EqExpTemp eqExpTemp;

    public EqExpTemp(Token.tokenType type, RelExp relExp, EqExpTemp eqExpTemp) {
        this.type = type;
        this.relExp = relExp;
        this.eqExpTemp = eqExpTemp;
    }

    public EqExpTemp(Token.tokenType type, RelExp relExp) {
        this.type = type;
        this.relExp = relExp;
    }
}
