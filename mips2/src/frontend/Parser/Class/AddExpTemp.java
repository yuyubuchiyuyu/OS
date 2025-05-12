package frontend.Parser.Class;

import frontend.Lexer.Token;

public class AddExpTemp {
    // AddExp' → ('+' | '−') MulExp AddExp' | ε
    private Token.tokenType tokenType = null;
    private MulExp mulExp = null;
    private AddExpTemp addExpTemp = null;

    public AddExpTemp(Token.tokenType tokenType, MulExp mulExp) {
        this.tokenType = tokenType;
        this.mulExp = mulExp;
    }

    public AddExpTemp(Token.tokenType tokenType, MulExp mulExp, AddExpTemp addExpTemp) {
        this.tokenType = tokenType;
        this.mulExp = mulExp;
        this.addExpTemp = addExpTemp;
    }
    public Token.tokenType getCharacterType() {
        return tokenType;
    }
    public MulExp getMulTemp() {
        return mulExp;
    }

    public AddExpTemp getAddExpTemp() {
        return addExpTemp;
    }

}
