package frontend;

import java.util.*;

public class Token {
    String name;
    tokenType type;

    public Token(String name, tokenType type) {
        this.name = name;
        this.type = type;
    }
    public enum tokenType {
        IDENFR, INTCON, STRCON, CHRCON, MAINTK, CONSTTK, INTTK, CHARTK, BREAKTK, CONTINUETK, IFTK,
        ELSETK, NOT, AND, OR, FORTK, GETINTTK, GETCHARTK, PRINTFTK, RETURNTK, PLUS, MINU,
        VOIDTK, MULT, DIV, MOD, LSS, LEQ, GRE, GEQ, EQL, NEQ, ASSIGN,
        SEMICN, COMMA, LPARENT, RPARENT, LBRACK, RBRACK, LBRACE, RBRACE
    }
    public static final Map<tokenType, String> patterns = new LinkedHashMap<>();
    static {
        patterns.put(tokenType.MAINTK, "main");
        patterns.put(tokenType.CONSTTK, "const");
        patterns.put(tokenType.INTTK, "int");
        patterns.put(tokenType.CHARTK, "char");
        patterns.put(tokenType.BREAKTK, "break");
        patterns.put(tokenType.CONTINUETK, "continue");
        patterns.put(tokenType.IFTK, "if");
        patterns.put(tokenType.ELSETK, "else");
        patterns.put(tokenType.FORTK, "for");
        patterns.put(tokenType.GETINTTK, "getint");
        patterns.put(tokenType.GETCHARTK, "getchar");
        patterns.put(tokenType.PRINTFTK, "printf");
        patterns.put(tokenType.RETURNTK, "return");
        patterns.put(tokenType.VOIDTK, "void");

        patterns.put(tokenType.LEQ, "<=");
        patterns.put(tokenType.LSS, "<");
        patterns.put(tokenType.GEQ, ">=");
        patterns.put(tokenType.GRE, ">");
        patterns.put(tokenType.EQL, "==");
        patterns.put(tokenType.ASSIGN, "=");
        patterns.put(tokenType.NEQ, "!=");


        patterns.put(tokenType.NOT, "!");
        patterns.put(tokenType.AND, "&&");
        patterns.put(tokenType.OR, "\\|\\|");
        patterns.put(tokenType.PLUS, "\\+");
        patterns.put(tokenType.MINU, "-");
        patterns.put(tokenType.MULT, "\\*");
        patterns.put(tokenType.DIV, "/");
        patterns.put(tokenType.MOD, "%");

        patterns.put(tokenType.SEMICN, ";");
        patterns.put(tokenType.COMMA, ",");
        patterns.put(tokenType.LPARENT, "\\(");
        patterns.put(tokenType.RPARENT, "\\)");
        patterns.put(tokenType.LBRACK, "\\[");
        patterns.put(tokenType.RBRACK, "\\]");
        patterns.put(tokenType.LBRACE, "\\{");
        patterns.put(tokenType.RBRACE, "\\}");

        patterns.put(tokenType.IDENFR, "[a-zA-Z_][a-zA-Z0-9_]*");
        patterns.put(tokenType.INTCON, "\\d+");
        patterns.put(tokenType.STRCON, "\"[^\"]*\"");
        patterns.put(tokenType.CHRCON, "'(\\\\[0nrtbf\\\\']|[^\\\\'])'");
        // patterns.put(tokenType.CHRCON, "'(\\\\'|[^'])'");
        // patterns.put(tokenType.CHRCON, "'[^\']*'");
    }

}
