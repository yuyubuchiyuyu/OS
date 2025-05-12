package frontend.Parser.Class;

import frontend.Lexer.Token;

public class RelExpTemp {
    //  // RelExp' → ('<' | '>' | '<=' | '>=') AddExp RelExp' | ε
    private Token.tokenType type;
    private AddExp addExp;
    private RelExpTemp relExpTemp;

    public RelExpTemp(Token.tokenType type, AddExp addExp, RelExpTemp relExpTemp) {
        this.type = type;
        this.addExp = addExp;
        this.relExpTemp = relExpTemp;
    }

    public RelExpTemp(Token.tokenType type, AddExp addExp) {
        this.type = type;
        this.addExp = addExp;
    }
}
