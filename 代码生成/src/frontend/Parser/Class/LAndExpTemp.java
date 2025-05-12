package frontend.Parser.Class;

import frontend.Lexer.Token;

public class LAndExpTemp {
    // // LAndExp' → '&&' EqExp LAndExp' | ε
    private Token.tokenType type = null;
    private EqExp eqExp = null;
    private LAndExpTemp lAndExpTemp = null;

    public LAndExpTemp(Token.tokenType type, EqExp eqExp, LAndExpTemp lAndExpTemp) {
        this.type = type;
        this.eqExp = eqExp;
        this.lAndExpTemp = lAndExpTemp;
    }

    public LAndExpTemp(Token.tokenType type, EqExp eqExp) {
        this.type = type;
        this.eqExp = eqExp;
    }

    public EqExp getEqTemp() {
        return eqExp;
    }

    public LAndExpTemp getLAndExpTemp() {
        return lAndExpTemp;
    }
}
