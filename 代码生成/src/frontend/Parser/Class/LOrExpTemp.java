package frontend.Parser.Class;

import frontend.Lexer.Token;

public class LOrExpTemp {
    // LOrExp' → '||' LAndExp LOrExp' | ε
    public Token.tokenType type = null;
    public LAndExp lAndExp = null;
    public LOrExpTemp lOrExpTemp = null;

    public LOrExpTemp(Token.tokenType type, LAndExp lAndExp, LOrExpTemp lOrExpTemp) {
        this.type = type;
        this.lAndExp = lAndExp;
        this.lOrExpTemp = lOrExpTemp;
    }

    public LOrExpTemp(Token.tokenType type, LAndExp lAndExp) {
        this.type = type;
        this.lAndExp = lAndExp;
    }

    public LAndExp getLAndTemp() {
        return lAndExp;
    }

    public LOrExpTemp getLOrExpTemp() {
        return lOrExpTemp;
    }

    public Token.tokenType getCharacterType() {
        return type;
    }
}
