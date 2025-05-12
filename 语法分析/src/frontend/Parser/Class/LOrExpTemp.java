package frontend.Parser.Class;

import frontend.Lexer.Token;

public class LOrExpTemp {
    // LOrExp' → '||' LAndExp LOrExp' | ε
    private Token.tokenType type;
    private LAndExp lAndExp;
    private LOrExpTemp lOrExpTemp;

    public LOrExpTemp(Token.tokenType type, LAndExp lAndExp, LOrExpTemp lOrExpTemp) {
        this.type = type;
        this.lAndExp = lAndExp;
        this.lOrExpTemp = lOrExpTemp;
    }

    public LOrExpTemp(Token.tokenType type, LAndExp lAndExp) {
        this.type = type;
        this.lAndExp = lAndExp;
    }
}
