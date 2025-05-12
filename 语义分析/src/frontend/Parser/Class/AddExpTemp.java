package frontend.Parser.Class;

import frontend.Lexer.Token;

public class AddExpTemp {
    // AddExp' → ('+' | '−') MulExp AddExp' | ε
    private Token.tokenType tokenType;
    private MulExp mulExp;
    private AddExpTemp addExpTemp;

    public AddExpTemp(Token.tokenType tokenType, MulExp mulExp) {
        this.tokenType = tokenType;
        this.mulExp = mulExp;
    }

    public AddExpTemp(Token.tokenType tokenType, MulExp mulExp, AddExpTemp addExpTemp) {
        this.tokenType = tokenType;
        this.mulExp = mulExp;
        this.addExpTemp = addExpTemp;
    }
}
