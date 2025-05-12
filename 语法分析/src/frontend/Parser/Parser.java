package frontend.Parser;

import frontend.Lexer.Lexer;
import frontend.Lexer.Token;
import frontend.Parser.Class.Character;
import frontend.Parser.Class.Number;
import frontend.Parser.Class.*;

import java.util.ArrayList;

public class Parser {
    ArrayList<Lexer> lexerList;
    int currentIndex;
    Lexer currentToken;
    Token.tokenType currentType;
    private int currentLineNum;
    private Judge judge;
    private ArrayList<ErrorInfo> errorList = new ArrayList<>();
    private ArrayList<String> outputList = new ArrayList<>();
    public static boolean printFlag = true;

    public Parser(ArrayList<Lexer> lexerList) {
        this.lexerList = lexerList;
        this.currentIndex = 0;
        this.currentToken = lexerList.get(currentIndex);
        this.currentType = this.currentToken.getType();
        this.currentLineNum = this.currentToken.getLineNum();
        this.judge = new Judge(this);
        parseCompUnit();
    }

    public void next() {
        this.currentIndex++;
        if (this.currentIndex < lexerList.size()) {
            this.currentToken = lexerList.get(currentIndex);
            this.currentType = this.currentToken.getType();
            this.currentLineNum = this.currentToken.getLineNum();
        }
    }

    public void back(int savedPos) {
        this.currentIndex = savedPos;
        if (this.currentIndex < lexerList.size()) {
            this.currentToken = lexerList.get(currentIndex);
            this.currentType = this.currentToken.getType();
            this.currentLineNum = this.currentToken.getLineNum();
        }
    }

    public boolean match(Token.tokenType type) {
        if (currentType == type) {
            if (Parser.printFlag) {
                outputList.add(currentType + " " + currentToken.getText());
            }
            return true;
        } else {
            return false;
        }
    }


    public CompUnit parseCompUnit() {
        // 编译单元 CompUnit → {Decl} {FuncDef} MainFuncDef
        ArrayList<Decl> decls = new ArrayList<>();
        ArrayList<FuncDef> funcDefs = new ArrayList<>();
        MainFuncDef mainFuncDef;
        int flag = 0; // 0表示目前正在读取 Decl；1表示目前正在读取 FuncDef；2表示目前正在读取 MainFuncDef
        while (currentIndex < lexerList.size()) {
            if (flag == 0 && judge.isDecl()) {
                Decl decl = parseDecl();
                decls.add(decl);
                next();
            } else if (judge.isFuncDef()) {
                flag = 1;
                FuncDef funcDef = parseFuncDef();
                funcDefs.add(funcDef);
                next();
            } else { // flag = 2;
                break;
            }
        }
        mainFuncDef = parseMainFunDef();
        return new CompUnit(decls, funcDefs, mainFuncDef, outputList);
    }

    public Decl parseDecl() { // 声明 Decl → ConstDecl | VarDecl
        if (judge.isConstDecl()) {
            return parseConstDecl();
        } else if (judge.isVarDecl()) {
            return parseVarDecl();
        } else {
            return null;
        }
    }

    private ConstDecl parseConstDecl() {
        // 常量声明 ConstDecl → 'const' BType ConstDef { ',' ConstDef } ';' // i
        BType bType;
        ArrayList<ConstDef> constDefs = new ArrayList<>();
        if (match(Token.tokenType.CONSTTK)) {
            next();
            if (judge.isBType()) {
                match(currentType);
                bType = new BType(currentType);
                next();
                while (currentIndex < lexerList.size()) {
                    ConstDef constDef = parseConstDef();
                    constDefs.add(constDef);
                    int pos = currentIndex;
                    next();
                    if (match(Token.tokenType.COMMA)) {
                        next();
                    } else if (match(Token.tokenType.SEMICN)) {
                        return new ConstDecl(bType, constDefs, outputList);
                    } else { // error
                        back(pos);
                        Operation.addIError(currentLineNum, errorList);
                        return new ConstDecl(bType, constDefs, outputList);
                    }
                }
            }
        }
        return null;
    }

    private ConstDef parseConstDef() {
        // 常量定义 ConstDef → Ident [ '[' ConstExp ']' ] '=' ConstInitVal // k
        String ident;
        ConstExp constExp = null;
        ConstInitVal constInitVal;
        if (match(Token.tokenType.IDENFR)) {
            ident = currentToken.getText();
            next();
            if (match(Token.tokenType.LBRACK)) {
                next();
                constExp = parseConstExp();
                int pos = currentIndex;
                next();
                if (match(Token.tokenType.RBRACK)) {
                    next();
                } else {
                    back(pos);
                    Operation.addKError(currentLineNum, errorList);
                    next();
                }
            }
            if (match(Token.tokenType.ASSIGN)) {
                next();
                constInitVal = parseConstInitVal();
                return new ConstDef(ident, constExp, constInitVal, outputList);
            }
        }
        return null;
    }

    private ConstInitVal parseConstInitVal() {
        // 常量初值 ConstInitVal → ConstExp | '{' [ ConstExp { ',' ConstExp } ] '}' | StringConst
        if (match(Token.tokenType.LBRACE)) {
            next();
            if (match(Token.tokenType.RBRACE)) {
                return new ConstInitVal(outputList);
            } else {
                ArrayList<ConstExp> constExps = new ArrayList<>();
                while (currentIndex < lexerList.size()) {
                    ConstExp constExp = parseConstExp();
                    constExps.add(constExp);
                    next();
                    if (match(Token.tokenType.COMMA)) {
                        next();
                    } else if (match(Token.tokenType.RBRACE)) {
                        return new ConstInitVal(constExps, outputList);
                    } else { // error
                        return new ConstInitVal(constExps, outputList);
                    }
                }
            }
        } else if (match(Token.tokenType.STRCON)) {
            String stringConst = currentToken.getText();
            return new ConstInitVal(stringConst, outputList);
        }
        ConstExp constExp = parseConstExp();
        return new ConstInitVal(constExp, outputList);
    }

    private VarDecl parseVarDecl() {
        // 变量声明 VarDecl → BType VarDef { ',' VarDef } ';' // i
        BType bType;
        ArrayList<VarDef> varDefs = new ArrayList<>();
        if (judge.isBType()) {
            match(currentType);
            bType = new BType(currentType);
            next();
            while (currentIndex < lexerList.size()) {
                VarDef varDef = parseVarDef();
                varDefs.add(varDef);
                int pos = currentIndex;
                next();
                if (match(Token.tokenType.COMMA)) {
                    next();
                } else if (match(Token.tokenType.SEMICN)) {
                    return new VarDecl(bType, varDefs, outputList);
                } else {
                    back(pos);
                    Operation.addIError(currentLineNum, errorList);
                    return new VarDecl(bType, varDefs, outputList);
                }
            }
        }
        return null;
    }

    private VarDef parseVarDef() {
        // 变量定义 VarDef → Ident [ '[' ConstExp ']' ] | Ident [ '[' ConstExp ']' ] '=' InitVal // k
        String ident = null;
        ConstExp constExp = null;
        int flag = 0;
        if (match(Token.tokenType.IDENFR)) {
            ident = currentToken.getText();
        }
        int pos = currentIndex;
        next();
        if (match(Token.tokenType.LBRACK)) {
            flag = 1;
            next();
            constExp = parseConstExp();
            int posTemp = currentIndex;
            next();
            if (match(Token.tokenType.RBRACK)) {
                pos = currentIndex;
                next();
            } else {
                back(posTemp);
                Operation.addKError(currentLineNum, errorList);
                pos = currentIndex;
                next();
            }
        }
        if (match(Token.tokenType.ASSIGN)) {
            next();
            InitVal initVal = parseInitVal();
            if (flag == 1) {
                return new VarDef(ident, constExp, initVal, outputList);
            } else {
                return new VarDef(ident, initVal, outputList);
            }
        } else {
            back(pos);
            if (flag == 1) {
                return new VarDef(ident, constExp, outputList);
            } else {
                return new VarDef(ident, outputList);
            }
        }
    }

    private InitVal parseInitVal() {
        // 变量初值 InitVal → Exp | '{' [ Exp { ',' Exp } ] '}' | StringConst
        if (match(Token.tokenType.LBRACE)) {
            ArrayList<Exp> exps = new ArrayList<Exp>();
            next();
            while (currentIndex < lexerList.size()) {
                Exp exp = parseExp();
                exps.add(exp);
                next();
                if (match(Token.tokenType.COMMA)) {
                    next();
                } else if (match(Token.tokenType.RBRACE)) {
                    return new InitVal(exps, outputList);
                } else { // error
                    return new InitVal(exps, outputList);
                }
            }
        } else if (match(Token.tokenType.STRCON)) {
            String stringConst = currentToken.getText();
            return new InitVal(stringConst, outputList);
        } else {
            Exp exp = parseExp();
            return new InitVal(exp, outputList);
        }
        return null;
    }

    private FuncDef parseFuncDef() {
        // 函数定义 FuncDef → FuncType Ident '(' [FuncFParams] ')' Block // j
        FuncType funcType;
        String ident;
        FuncFParams funcFParams = null;
        Block block;
        if (judge.isFuncType()) {
            match(currentType);
            funcType = new FuncType(currentType, outputList);
            next();
            if (match(Token.tokenType.IDENFR)) {
                ident = currentToken.getText();
                next();
                if (match(Token.tokenType.LPARENT)) {
                    int pos = currentIndex;
                    next();
                    if (match(Token.tokenType.RPARENT)) {
                        next();
                    } else if (judge.isBType()) {
                        funcFParams = parseFuncFParams();
                        pos = currentIndex;
                        next();
                        if (match(Token.tokenType.RPARENT)) {
                            next();
                        } else {
                            back(pos);
                            Operation.addJError(currentLineNum, errorList);
                            next();
                        }
                    } else {
                        back(pos);
                        Operation.addJError(currentLineNum, errorList);
                        next();
                    }
                    block = parseBlock();
                    return new FuncDef(funcType, ident, funcFParams, block, outputList);
                }
            }
        }
        return null;
    }

    private MainFuncDef parseMainFunDef() {
        // 主函数定义 MainFuncDef → 'int' 'main' '(' ')' Block // j
        if (match(Token.tokenType.INTTK)) {
            next();
            if (match(Token.tokenType.MAINTK)) {
                next();
                if (match(Token.tokenType.LPARENT)) {
                    int pos = currentIndex;
                    next();
                    if (match(Token.tokenType.RPARENT)) {
                        next();
                    } else {
                        back(pos);
                        Operation.addJError(currentLineNum, errorList);
                        next();
                    }
                    Block block = parseBlock();
                    return new MainFuncDef(block, outputList);
                }
            }
        }
        return null;
    }

    private FuncFParams parseFuncFParams() {
        // 函数形参表 FuncFParams → FuncFParam { ',' FuncFParam }
        ArrayList<FuncFParam> funcFParams = new ArrayList<>();
        while (currentIndex < lexerList.size()) {
            FuncFParam funcFParam = parseFuncFParam();
            funcFParams.add(funcFParam);
            int pos = currentIndex;
            next();
            if (match(Token.tokenType.COMMA)) {
                next();
            } else {
                back(pos);
                return new FuncFParams(funcFParams, outputList);
            }
        }
        return null;
    }

    private FuncFParam parseFuncFParam() {
        // 函数形参 FuncFParam → BType Ident ['[' ']'] // k
        BType bType;
        String ident;
        if (judge.isBType()) {
            match(currentType);
            bType = new BType(currentType);
            next();
            if (match(Token.tokenType.IDENFR)) {
                ident = currentToken.getText();
                int pos = currentIndex;
                next();
                if (match(Token.tokenType.LBRACK)) {
                    int posTemp = currentIndex;
                    next();
                    if (match(Token.tokenType.RBRACK)) {
                    } else {
                        back(posTemp);
                        Operation.addKError(currentLineNum, errorList);
                    }
                    return new FuncFParam(bType, ident, Token.tokenType.LBRACK, Token.tokenType.RBRACK, outputList);
                } else {
                    back(pos);
                    return new FuncFParam(bType, ident, outputList);
                }
            }
        }
        return null;
    }

    private Block parseBlock() {
        // 语句块 Block → '{' { BlockItem } '}'
        ArrayList<BlockItem> blockItems = new ArrayList<>();
        if (match(Token.tokenType.LBRACE)) {
            next();
            if (match(Token.tokenType.RBRACE)) {
                return new Block(blockItems, outputList);
            }
            while (currentIndex < lexerList.size()) {
                BlockItem blockItem = parseBlockItem();
                blockItems.add(blockItem);
                next();
                if (match(Token.tokenType.RBRACE)) {
                    return new Block(blockItems, outputList);
                }
            }
        }
        return null;
    }

    private BlockItem parseBlockItem() {
        // 语句块项 BlockItem → Decl | Stmt
        if (judge.isDecl()) {
            return new BlockItem(parseDecl());
        } else {
            return new BlockItem(parseStmt());
        }
    }

    public Stmt parseStmt() {
        /* 语句 Stmt → LVal '=' Exp ';' // i
                | [Exp] ';' // i
                | Block
                | 'if' '(' Cond ')' Stmt [ 'else' Stmt ] // j
                | 'for' '(' [ForStmt] ';' [Cond] ';' [ForStmt] ')' Stmt
                | 'break' ';' | 'continue' ';' // i
                | 'return' [Exp] ';' // i
                | LVal '=' 'getint''('')'';' // i j
                | LVal '=' 'getchar''('')'';' // i j
                | 'printf''('StringConst {','Exp}')'';' // i j
         */
        if (match(Token.tokenType.IFTK)) {
            next();
            if (match(Token.tokenType.LPARENT)) {
                next();
                Cond cond = parseCond();
                int pos = currentIndex;
                next();
                if (match(Token.tokenType.RPARENT)) {
                    next();
                } else {
                    back(pos);
                    Operation.addJError(currentLineNum, errorList);
                    next();
                }
                Stmt stmtIf = parseStmt();
                pos = currentIndex;
                next();
                if (match(Token.tokenType.ELSETK)) {
                    next();
                    Stmt stmtElse = parseStmt();
                    return new Stmt(cond, stmtIf, stmtElse, outputList);
                } else {
                    back(pos);
                    return new Stmt(cond, stmtIf, outputList);
                }
            }
            return null;
        }
        if (match(Token.tokenType.FORTK)) {
            next();
            if (match(Token.tokenType.LPARENT)) {
                next();
                ForStmt forStmt1 = null;
                Cond cond = null;
                ForStmt forStmt2 = null;
                Stmt stmt = null;
                if (!match(Token.tokenType.SEMICN)) {
                    forStmt1 = parseForStmt();
                    next();
                    if (match(Token.tokenType.SEMICN)) {
                        next();
                    } else {
                        return null;
                    }
                } else {
                    next();
                }
                if (!match(Token.tokenType.SEMICN)) {
                    cond = parseCond();
                    next();
                    if (match(Token.tokenType.SEMICN)) {
                        next();
                    } else {
                        return null;
                    }
                } else {
                    next();
                }

                if (!match(Token.tokenType.RPARENT)) {
                    forStmt2 = parseForStmt();
                    next();
                    if (match(Token.tokenType.RPARENT)) {
                        next();
                    } else {
                        return null;
                    }
                } else {
                    next();
                }
                stmt = parseStmt();
                return new Stmt(forStmt1, cond, forStmt2, stmt, outputList);
            }
            return null;
        }
        if (match(Token.tokenType.BREAKTK)) {
            int pos = currentIndex;
            next();
            if (match(Token.tokenType.SEMICN)) {
            } else {
                back(pos);
                Operation.addIError(currentLineNum, errorList);
            }
            return new Stmt(Token.tokenType.BREAKTK, outputList);
        }
        if (match(Token.tokenType.CONTINUETK)) {
            int pos = currentIndex;
            next();
            if (match(Token.tokenType.SEMICN)) {
            } else {
                back(pos);
                Operation.addIError(currentLineNum, errorList);
            }
            return new Stmt(Token.tokenType.CONTINUETK, outputList);
        }
        if (match(Token.tokenType.RETURNTK)) {
            Exp exp = null;
            int pos = currentIndex;
            next();
            if (match(Token.tokenType.SEMICN)) {
                return new Stmt(Token.tokenType.RETURNTK, exp, outputList);
            } else {
                exp = parseExp();
                if (exp == null) {
                    back(pos);
                } else {
                    pos = currentIndex;
                    next();
                }
                if (match(Token.tokenType.SEMICN)) {
                } else {
                    back(pos);
                    Operation.addIError(currentLineNum, errorList);
                }
                return new Stmt(Token.tokenType.RETURNTK, exp, outputList);
            }
        }
        if (match(Token.tokenType.PRINTFTK)) {
            ArrayList<Exp> exps = new ArrayList<>();
            next();
            if (match(Token.tokenType.LPARENT)) {
                next();
                if (match(Token.tokenType.STRCON)) {
                    String stringConst = currentToken.getText();
                    int pos1 = currentIndex;
                    int pos2 = currentIndex;
                    next();
                    while (match(Token.tokenType.COMMA)) {
                        next();
                        Exp exp = parseExp();
                        exps.add(exp);
                        pos1 = currentIndex;
                        pos2 = currentIndex;
                        next();
                    }
                    if (match(Token.tokenType.RPARENT)) {
                        pos2 = currentIndex;
                        next();
                    } else {
                        back(pos1);
                        Operation.addJError(currentLineNum, errorList);
                        pos2 = currentIndex;
                        next();
                    }
                    if (match(Token.tokenType.SEMICN)) {
                    } else {
                        back(pos2);
                        Operation.addIError(currentLineNum, errorList);
                    }
                    return new Stmt(stringConst, exps, outputList);
                }
            }
            return null;
        }
        if (currentType == Token.tokenType.LBRACE) {
            Block block = parseBlock();
            return new Stmt(block, outputList);
        }
        int pos = currentIndex;
        printFlag = false;
        parseExp();
        printFlag = true;
        next();
        int flag = -1; // 1表示有等号
        if (currentType == Token.tokenType.ASSIGN) {
            flag = 1;
        }
        back(pos);

        if (flag == 1) { // LVal
            LVal lVal = parseLVal();
            next();
            if (match(Token.tokenType.ASSIGN)) {
                next();
                if (match(Token.tokenType.GETINTTK)) {
                    next();
                    if (match(Token.tokenType.LPARENT)) {
                        int pos1 = currentIndex;
                        int pos2 = currentIndex;
                        next();
                        if (match(Token.tokenType.RPARENT)) {
                            pos2 = currentIndex;
                            next();
                        } else {
                            back(pos1);
                            Operation.addJError(currentLineNum, errorList);
                            next();
                        }
                        if (match(Token.tokenType.SEMICN)) {
                        } else {
                            back(pos2);
                            Operation.addIError(currentLineNum, errorList);
                        }
                        return new Stmt(lVal, Token.tokenType.GETINTTK, outputList);
                    }
                    return null;
                } else if (match(Token.tokenType.GETCHARTK)) {
                    next();
                    if (match(Token.tokenType.LPARENT)) {
                        int pos1 = currentIndex;
                        int pos2 = currentIndex;
                        next();
                        if (match(Token.tokenType.RPARENT)) {
                            pos2 = currentIndex;
                            next();
                        } else {
                            back(pos1);
                            Operation.addJError(currentLineNum, errorList);
                            next();
                        }
                        if (match(Token.tokenType.SEMICN)) {
                        } else {
                            back(pos2);
                            Operation.addIError(currentLineNum, errorList);
                        }
                        return new Stmt(lVal, Token.tokenType.GETCHARTK, outputList);
                    }
                    return null;
                } else {
                    Exp exp = parseExp();
                    int posTemp = currentIndex;
                    next();
                    if (match(Token.tokenType.SEMICN)) {
                    } else {
                        back(posTemp);
                        Operation.addIError(currentLineNum, errorList);
                    }
                    return new Stmt(lVal, exp, outputList);
                }
            }
        }
        if (match(Token.tokenType.SEMICN)) {
            return new Stmt(currentType, outputList);
        }
        Exp exp = parseExp();
        // System.out.println(currentToken);
        if (exp != null) {
            pos = currentIndex;
            next();
        } else {
            pos = currentIndex;
        }
        if (match(Token.tokenType.SEMICN)) {
        } else {
            back(pos);
            Operation.addIError(currentLineNum, errorList);
        }
        return new Stmt(exp, currentType, outputList);
    }

    private ForStmt parseForStmt() {
        // 语句 ForStmt → LVal '=' Exp
        if (judge.isLVal()) {
            LVal lVal = parseLVal();
            next();
            if (match(Token.tokenType.ASSIGN)) {
                next();
                Exp exp = parseExp();
                return new ForStmt(lVal, exp, outputList);
            }
        }
        return null;
    }

    private Exp parseExp() {
        // 表达式 Exp → AddExp
        AddExp addExp = parseAddExp();
        if (addExp == null) {
            return null;
        }
        return new Exp(addExp, outputList);
    }

    private Cond parseCond() {
        // 条件表达式 Cond → LOrExp
        return new Cond(parseLOrExp(), outputList);
    }

    private LVal parseLVal() {
        // 左值表达式 LVal → Ident ['[' Exp ']'] // k
        if (match(Token.tokenType.IDENFR)) {
            String ident = currentToken.getText();
            int pos = currentIndex;
            next();
            if (match(Token.tokenType.LBRACK)) {
                next();
                Exp exp = parseExp();
                int posTemp = currentIndex;
                next();
                if (match(Token.tokenType.RBRACK)) {
                } else {
                    back(posTemp);
                    Operation.addKError(currentLineNum, errorList);
                }
                return new LVal(ident, exp, outputList);
            } else {
                back(pos);
                return new LVal(ident, outputList);
            }
        }
        return null;
    }

    private PrimaryExp parsePrimaryExp() {
        // 基本表达式 PrimaryExp → '(' Exp ')' | LVal | Number | Character// j
        if (match(Token.tokenType.LPARENT)) {
            next();
            Exp exp = parseExp();
            int pos = currentIndex;
            next();
            if (match(Token.tokenType.RPARENT)) {
            } else {
                back(pos);
                Operation.addJError(currentLineNum, errorList);
            }
            return new PrimaryExp(exp, outputList);
        }
        if (judge.isLVal()) {
            LVal lVal = parseLVal();
            return new PrimaryExp(lVal, outputList);
        }
        if (match(Token.tokenType.INTCON)) {
            Number number = parseNumber();
            return new PrimaryExp(number, outputList);
        }
        if (match(Token.tokenType.CHRCON)) {
            Character character = parseCharacter();
            return new PrimaryExp(character, outputList);
        }
        return null;
    }

    private Number parseNumber() {
        // 数值 Number → IntConst
        return new Number(currentToken.getText(), outputList);
    }

    private Character parseCharacter() {
        // 字符 Character → CharConst
        return new Character(currentToken.getText(), outputList);
    }

    private UnaryExp parseUnaryExp() {
        // 一元表达式 UnaryExp → PrimaryExp | Ident '(' [FuncRParams] ')' | UnaryOp UnaryExp // j
        if (match(Token.tokenType.PLUS) || match(Token.tokenType.MINU) || match(Token.tokenType.NOT)) {
            UnaryOp unaryOp = parseUnaryOp();
            next();
            UnaryExp unaryExp = parseUnaryExp();
            return new UnaryExp(unaryOp, unaryExp, outputList);
        }
        if (currentType == Token.tokenType.IDENFR && lexerList.get(currentIndex + 1).getType() == Token.tokenType.LPARENT) {
            match(Token.tokenType.IDENFR);
            String ident = currentToken.getText();
            next();
            match(Token.tokenType.LPARENT);
            int pos = currentIndex;
            next();
            FuncRParams funcRParams = null;
            if (match(Token.tokenType.RPARENT)) {
                return new UnaryExp(ident, funcRParams, outputList);
            } else {
                funcRParams = parseFuncRParams();
                if (funcRParams != null) {
                    pos = currentIndex;
                }
                next();
                if (match(Token.tokenType.RPARENT)) {
                } else {
                    back(pos);
                    Operation.addJError(currentLineNum, errorList);
                }
                return new UnaryExp(ident, funcRParams, outputList);
            }
        }
        if (currentType == Token.tokenType.LPARENT || currentType == Token.tokenType.IDENFR
                || currentType == Token.tokenType.INTCON || currentType == Token.tokenType.CHRCON) {
            PrimaryExp primaryExp = parsePrimaryExp();
            return new UnaryExp(primaryExp, outputList);
        }
        return null;
    }

    private UnaryOp parseUnaryOp() {
        // 单目运算符 UnaryOp → '+' | '−' | '!' 注：'!'仅出现在条件表达式中
        return new UnaryOp(currentType, outputList);
    }

    private FuncRParams parseFuncRParams() {
        // 函数实参表 FuncRParams → Exp { ',' Exp }
        if (!Operation.judgeExpContains(currentType)) {
            return null;
        }
        ArrayList<Exp> exps = new ArrayList<>();
        while (currentIndex < lexerList.size()) {
            Exp exp = parseExp();
            exps.add(exp);
            int pos = currentIndex;
            next();
            if (match(Token.tokenType.COMMA)) {
                next();
            } else {
                back(pos);
                return new FuncRParams(exps, outputList);
            }
        }
        return null;
    }


    /*
    // 乘除模表达式 MulExp → UnaryExp | MulExp ('*' | '/' | '%') UnaryExp
    MulExp → UnaryExp MulExp'
    MulExp' → ('*' | '/' | '%') UnaryExp MulExp' | ε
     */
    private MulExp parseMulExp() {
        // MulExp → UnaryExp MulExp'
        UnaryExp unaryExp = parseUnaryExp();
        if (unaryExp == null) {
            return null;
        }
        int pos = currentIndex;
        next();
        MulExpTemp mulExpTemp = parseMulExpTemp();
        if (mulExpTemp == null) {
            back(pos);
            return new MulExp(unaryExp, outputList);
        } else {
            return new MulExp(unaryExp, mulExpTemp, outputList);
        }
    }

    private MulExpTemp parseMulExpTemp() {
        // MulExp' → ('*' | '/' | '%') UnaryExp MulExp' | ε
        if (currentType == Token.tokenType.MULT
                || currentType == Token.tokenType.DIV
                || currentType == Token.tokenType.MOD) {
            if (Parser.printFlag) {
                outputList.add("<MulExp>");
            }
            match(currentType);
            Token.tokenType tokenType = currentType;
            next();
            UnaryExp unaryExp = parseUnaryExp();
            if (unaryExp == null) {
                return null;
            }
            int pos = currentIndex;
            next();
            MulExpTemp mulExpTemp = parseMulExpTemp();
            if (mulExpTemp == null) {
                back(pos);
                return new MulExpTemp(tokenType, unaryExp);
            } else {
                return new MulExpTemp(tokenType, unaryExp, mulExpTemp);
            }
        } else {
            return null;
        }
    }

    /*
    加减表达式 AddExp → MulExp | AddExp ('+' | '−') MulExp
    AddExp → MulExp AddExp'
    AddExp' → ('+' | '−') MulExp AddExp' | ε
     */
    private AddExp parseAddExp() {
        // AddExp → MulExp AddExp'
        MulExp mulExp = parseMulExp();
        if (mulExp == null) {
            return null;
        }
        int pos = currentIndex;
        next();
        AddExpTemp addExpTemp = parseAddExpTemp();
        if (addExpTemp == null) {
            back(pos);
            return new AddExp(mulExp, outputList);
        } else {
            return new AddExp(mulExp, addExpTemp, outputList);
        }
    }

    private AddExpTemp parseAddExpTemp() {
        // AddExp' → ('+' | '−') MulExp AddExp' | ε
        if (currentType == Token.tokenType.PLUS || currentType == Token.tokenType.MINU) {
            if (Parser.printFlag) {
                outputList.add("<AddExp>");
            }
            match(currentType);
            Token.tokenType tokenType = currentType;
            next();
            MulExp mulExp = parseMulExp();
            if (mulExp == null) {
                return null;
            }
            int pos = currentIndex;
            next();
            AddExpTemp addExpTemp = parseAddExpTemp();
            if (addExpTemp == null) {
                back(pos);
                return new AddExpTemp(tokenType, mulExp);
            } else {
                return new AddExpTemp(tokenType, mulExp, addExpTemp);
            }
        } else {
            return null;
        }
    }
    /*
    关系表达式 RelExp → AddExp | RelExp ('<' | '>' | '<=' | '>=') AddExp
    RelExp → AddExp RelExp'
    RelExp' → ('<' | '>' | '<=' | '>=') AddExp RelExp' | ε
     */

    private RelExp parseRelExp() {
        // RelExp → AddExp RelExp'
        AddExp addExp = parseAddExp();
        if (addExp == null) {
            return null;
        }
        int pos = currentIndex;
        next();
        RelExpTemp relExpTemp = parseRelExpTemp();
        if (relExpTemp == null) {
            back(pos);
            return new RelExp(addExp, outputList);
        } else {
            return new RelExp(addExp, relExpTemp, outputList);
        }
    }

    private RelExpTemp parseRelExpTemp() {
        // RelExp' → ('<' | '>' | '<=' | '>=') AddExp RelExp' | ε
        if (currentType == Token.tokenType.LSS
                || currentType == Token.tokenType.LEQ
                || currentType == Token.tokenType.GRE
                || currentType == Token.tokenType.GEQ) {
            if (Parser.printFlag) {
                outputList.add("<RelExp>");
            }
            match(currentType);
            Token.tokenType tokenType = currentType;
            next();
            AddExp addExp = parseAddExp();
            if (addExp == null) {
                return null;
            }
            int pos = currentIndex;
            next();
            RelExpTemp relExpTemp = parseRelExpTemp();
            if (relExpTemp == null) {
                back(pos);
                return new RelExpTemp(tokenType, addExp);
            } else {
                return new RelExpTemp(tokenType, addExp, relExpTemp);
            }
        } else {
            return null;
        }
    }

    /*
    相等性表达式 EqExp → RelExp | EqExp ('==' | '!=') RelExp
    EqExp → RelExp EqExp'
    EqExp' → ('==' | '!=') RelExp EqExp' | ε
     */
    private EqExp parseEqExp() {
        // EqExp → RelExp EqExp'
        RelExp relExp = parseRelExp();
        if (relExp == null) {
            return null;
        }
        int pos = currentIndex;
        next();
        EqExpTemp eqExpTemp = parseEqExpTemp();
        if (eqExpTemp == null) {
            back(pos);
            return new EqExp(relExp, outputList);
        } else {
            return new EqExp(relExp, eqExpTemp, outputList);
        }
    }

    private EqExpTemp parseEqExpTemp() {
        // EqExp' → ('==' | '!=') RelExp EqExp' | ε
        if (currentType == Token.tokenType.EQL
                || currentType == Token.tokenType.NEQ) {
            if (Parser.printFlag) {
                outputList.add("<EqExp>");
            }
            match(currentType);
            Token.tokenType tokenType = currentType;
            next();
            RelExp relExp = parseRelExp();
            if (relExp == null) {
                return null;
            }
            int pos = currentIndex;
            next();
            EqExpTemp eqExpTemp = parseEqExpTemp();
            if (eqExpTemp == null) {
                back(pos);
                return new EqExpTemp(tokenType, relExp);
            } else {
                return new EqExpTemp(tokenType, relExp, eqExpTemp);
            }
        } else {
            return null;
        }
    }

    /*
    逻辑与表达式 LAndExp → EqExp | LAndExp '&&' EqExp
    LAndExp → EqExp LAndExp'
    LAndExp' → '&&' EqExp LAndExp' | ε
     */
    private LAndExp parseLAndExp() {
        // LAndExp → EqExp LAndExp'
        EqExp eqExp = parseEqExp();
        if (eqExp == null) {
            return null;
        }
        int pos = currentIndex;
        next();
        LAndExpTemp lAndExpTemp = parseLAndExpTemp();
        if (lAndExpTemp == null) {
            back(pos);
            return new LAndExp(eqExp, outputList);
        } else {
            return new LAndExp(eqExp, lAndExpTemp, outputList);
        }
    }

    private LAndExpTemp parseLAndExpTemp() {
        // LAndExp' → '&&' EqExp LAndExp' | ε
        if (currentType == Token.tokenType.AND) {
            if (Parser.printFlag) {
                outputList.add("<LAndExp>");
            }
            match(currentType);
            Token.tokenType tokenType = currentType;
            next();
            EqExp eqExp = parseEqExp();
            if (eqExp == null) {
                return null;
            }
            int pos = currentIndex;
            next();
            LAndExpTemp lAndExpTemp = parseLAndExpTemp();
            if (lAndExpTemp == null) {
                back(pos);
                return new LAndExpTemp(tokenType, eqExp);
            } else {
                return new LAndExpTemp(tokenType, eqExp, lAndExpTemp);
            }
        } else {
            return null;
        }
    }

    /*
    逻辑或表达式 LOrExp → LAndExp | LOrExp '||' LAndExp
    LOrExp → LAndExp LOrExp'
    LOrExp' → '||' LAndExp LOrExp' | ε
     */
    private LOrExp parseLOrExp() {
        // LOrExp → LAndExp LOrExp'
        LAndExp lAndExp = parseLAndExp();
        if (lAndExp == null) {
            return null;
        }
        int pos = currentIndex;
        next();
        LOrExpTemp lOrExpTemp = parseLOrExpTemp();
        if (lOrExpTemp == null) {
            back(pos);
            return new LOrExp(lAndExp, outputList);
        } else {
            return new LOrExp(lAndExp, lOrExpTemp, outputList);
        }
    }

    private LOrExpTemp parseLOrExpTemp() {
        // LOrExp' → '||' LAndExp LOrExp' | ε
        if (currentType == Token.tokenType.OR) {
            if (Parser.printFlag) {
                outputList.add("<LOrExp>");
            }
            match(currentType);
            Token.tokenType tokenType = currentType;
            next();
            LAndExp lAndExp = parseLAndExp();
            if (lAndExp == null) {
                return null;
            }
            int pos = currentIndex;
            next();
            LOrExpTemp lOrExpTemp = parseLOrExpTemp();
            if (lOrExpTemp == null) {
                back(pos);
                return new LOrExpTemp(tokenType, lAndExp);
            } else {
                return new LOrExpTemp(tokenType, lAndExp, lOrExpTemp);
            }
        } else {
            return null;
        }
    }

    private ConstExp parseConstExp() {
        // 常量表达式 ConstExp → AddExp 注：使用的 Ident 必须是常量
        AddExp addExp = parseAddExp();
        return new ConstExp(addExp, outputList);
    }

    public ArrayList<String> getOutput() {
        return outputList;
    }

    public ArrayList<ErrorInfo> getError() {
        return errorList;
    }
}
