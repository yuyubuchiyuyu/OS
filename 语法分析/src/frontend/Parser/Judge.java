package frontend.Parser;

import frontend.Lexer.Token;

public class Judge {
    private Parser parser;

    public Judge(Parser parser) {
        this.parser = parser;
    }

    public boolean isBType() { // 基本类型 BType → 'int' | 'char'
        return parser.currentType == Token.tokenType.INTTK ||
                parser.currentType == Token.tokenType.CHARTK;
    }

    public boolean isFuncType() { // 函数类型 FuncType → 'void' | 'int' | 'char'
        return parser.currentType == Token.tokenType.VOIDTK ||
                parser.currentType == Token.tokenType.INTTK ||
                parser.currentType == Token.tokenType.CHARTK;
    }

    public boolean isDecl() { // 声明 Decl → ConstDecl | VarDecl
        return isConstDecl() || isVarDecl();
    }

    public boolean isConstDecl() { // 常量声明 ConstDecl → 'const' BType ConstDef { ',' ConstDef } ';' // i
        int savedPos = parser.currentIndex; // 保存当前的位置
        if (parser.currentType == Token.tokenType.CONSTTK) {
            parser.back(savedPos); // 恢复位置
            return true;
        }
        parser.back(savedPos); // 恢复位置
        return false;
    }

    public boolean isVarDecl() {
        // 变量声明 VarDecl → BType VarDef { ',' VarDef } ';' // i
        // 变量定义 VarDef → Ident [ '[' ConstExp ']' ] | Ident [ '[' ConstExp ']' ] '=' InitVal // k
        int savedPos = parser.currentIndex; // 保存当前的位置
        if (isBType()) {
            parser.next();
            if (parser.currentType == Token.tokenType.IDENFR) {
                parser.next();
                if (parser.currentType != Token.tokenType.LPARENT) {
                    parser.back(savedPos); // 恢复位置
                    return true;
                }
            }
        }
        parser.back(savedPos); // 恢复位置
        return false;
    }


    public boolean isFuncDef() {
        // 函数定义 FuncDef → FuncType Ident '(' [FuncFParams] ')' Block // j
        int savedPos = parser.currentIndex; // 保存当前的位置
        if (isFuncType()) {
            parser.next();
            if (parser.currentType == Token.tokenType.IDENFR) {
                parser.next();
                if (parser.currentType == Token.tokenType.LPARENT) {
                    parser.back(savedPos); // 恢复位置
                    return true;
                }
            }
        }
        parser.back(savedPos); // 恢复位置
        return false;
    }

    /*
    public boolean isConstExp() {
        // 常量表达式 ConstExp → AddExp 注：使用的 Ident 必须是常量
        int savedPos = parser.currentIndex;
        while (!isUnaryExp()) {
            parser.next();
        }
        int end = parser.currentIndex;
        parser.back(savedPos);
        for (int i = savedPos; i <= end; i++) {
            String string = parser.currentToken.getText();
            for (char ch : string.toCharArray()) {
                if (java.lang.Character.isLetter(ch)) {
                    return false;
                }
            }
            parser.next();
        }
        return true;
    }

     */

    public boolean isLVal() {
        return parser.currentType == Token.tokenType.IDENFR;
    }

    public boolean isUnaryExp() {
        // 一元表达式 UnaryExp → PrimaryExp | Ident '(' [FuncRParams] ')' | UnaryOp UnaryExp // j
        if (parser.currentType == Token.tokenType.LPARENT
                || parser.currentType == Token.tokenType.IDENFR
                || parser.currentType == Token.tokenType.INTTK
                || parser.currentType == Token.tokenType.CHARTK) {
            return true;
        }
        if (parser.currentType == Token.tokenType.PLUS
                || parser.currentType == Token.tokenType.MINU
                || parser.currentType == Token.tokenType.NOT) {
            return true;
        }
        if (parser.currentType == Token.tokenType.IDENFR
                && parser.lexerList.get(parser.currentIndex + 1).getType() == Token.tokenType.LPARENT) {
            return true;
        }
        return false;
    }

}
