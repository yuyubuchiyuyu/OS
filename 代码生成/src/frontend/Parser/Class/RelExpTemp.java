package frontend.Parser.Class;

import frontend.Lexer.Token;

public class RelExpTemp {
    //  // RelExp' → ('<' | '>' | '<=' | '>=') AddExp RelExp' | ε
    private Token.tokenType type = null;
    private AddExp addExp = null;
    private RelExpTemp relExpTemp = null;

    public RelExpTemp(Token.tokenType type, AddExp addExp, RelExpTemp relExpTemp) {
        this.type = type;
        this.addExp = addExp;
        this.relExpTemp = relExpTemp;
    }

    public RelExpTemp(Token.tokenType type, AddExp addExp) {
        this.type = type;
        this.addExp = addExp;
    }

    public AddExp getAddTemp() {
        return addExp;
    }

    public RelExpTemp getRelExpTemp() {
        return relExpTemp;
    }

    public Token.tokenType getCharacterType() {
        return type;
    }
}
