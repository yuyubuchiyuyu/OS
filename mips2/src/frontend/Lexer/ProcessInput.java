package frontend.Lexer;

import frontend.ErrorInfo;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static frontend.Lexer.Token.patterns;

public class ProcessInput {
    private final InputStream input;
    private Integer line = 1;
    private final List<Map.Entry<Token.tokenType, String>> sortedPatterns;
    private boolean sign = true;
    private ArrayList<Lexer> lexer = new ArrayList<>();
    private ArrayList<ErrorInfo> errorList = new ArrayList<>();

    public ProcessInput(InputStream input) throws IOException {
        this.input = input;
        this.sortedPatterns = new ArrayList<>(patterns.entrySet());
        this.process();
    }

    private boolean judgeSingle(char ch) {
        return ch == ';' || ch == ',' || ch == '(' || ch == ')' || ch == '[' || ch == ']' || ch == '{' || ch == '}' ||
                ch == '-' || ch == '+' || ch == '/' || ch == '*' || ch == '%';
    }

    private boolean judgeDouble(char ch) {
        return ch == '<' || ch == '>' || ch == '=' || ch == '&' || ch == '|' || ch == '!';
    }

    private void matchWord(StringBuilder currentWord) {
        if (currentWord.length()>0) {
            matchToken(currentWord.toString(), line);
            currentWord.setLength(0);
        }
    }

    public boolean process() throws IOException {
        StringBuilder currentWord = new StringBuilder();
        int data = this.input.read();
        char character;
        while (data != -1) {
            character = (char) data;

            if (character == '"' || character == '\'') { // 在引号内
                currentWord.append(character);
                char lastCharacter = character;
                boolean transfer = false;
                data = input.read(); // 逐字符读取，直至遇到匹配引号
                character = (char) data;
                while (data != -1 &&(!((character == '"' || character == '\'') && !transfer))) {
                    currentWord.append(character);
                    lastCharacter = character;
                    data = input.read();
                    character = (char) data;
                    if (lastCharacter == '\\') {
                        transfer = !transfer;
                    }else {
                        transfer = false;
                    }
                }
                if (data != -1) {
                    currentWord.append(character); // 读取匹配引号
                    matchWord(currentWord);
                    data = input.read();
                }
            } else if (character == '\n') { // 处理换行符
                matchWord(currentWord);
                line++;
                data = input.read();
            } else if (character == '/') { // 处理可能的注释
                matchWord(currentWord);
                currentWord.append(character);
                char lastCharacter = character;
                data = input.read(); // 读取'/'的下一个字符
                character = (char) data;
                if (character == '/') { // '//'单行注释
                    currentWord.setLength(0);
                    data = input.read(); // 逐字符读取，直至换行符
                    character = (char) data;
                    while (data != -1 && character != '\n') {
                        data = input.read();
                        character = (char) data;
                    }
                    // 当前字符是换行符
                } else if (character == '*') { // '/*'多行注释
                    currentWord.setLength(0);
                    lastCharacter = character;
                    data = input.read(); // 逐字符读取，直至遇到'*/'
                    character = (char) data;
                    while (data != -1 && !(lastCharacter == '*' && character == '/')) {
                        lastCharacter = character;
                        data = input.read();
                        character = (char) data;
                    }
                    if (data != -1) {
                        // 当前字符是'/'
                        data = input.read();
                    }
                } else { // 普通除号'/'
                    matchWord(currentWord);
                }
            } else if (Character.isWhitespace(character)) {
                matchWord(currentWord);
                data = input.read();
            } else if (judgeSingle(character)) {
                matchWord(currentWord); // 把原来的字符串匹配了
                currentWord.append(character);  // append该字符
                matchWord(currentWord);     // 匹配该字符
                data = input.read();
            } else if (judgeDouble(character)) {
                matchWord(currentWord);
                currentWord.append(character);
                char lastCharacter = character;
                data = input.read(); // 检查后面是否是可能的多字符
                character = (char) data;
                if (lastCharacter == '<' && character == '=') {
                    currentWord.append(character);
                    matchWord(currentWord);
                    data = input.read();
                } else if (lastCharacter == '>' && character == '=') {
                    currentWord.append(character);
                    matchWord(currentWord);
                    data = input.read();
                } else if (lastCharacter == '=' && character == '=') {
                    currentWord.append(character);
                    matchWord(currentWord);
                    data = input.read();
                } else if (lastCharacter == '!' && character == '=') {
                    currentWord.append(character);
                    matchWord(currentWord);
                    data = input.read();
                } else if (lastCharacter == '&' && character == '&') {
                    currentWord.append(character);
                    matchWord(currentWord);
                    data = input.read();
                } else if (lastCharacter == '|' && character == '|') {
                    currentWord.append(character);
                    matchWord(currentWord);
                    data = input.read();
                }  else {
                    matchWord(currentWord);
                }
            } else {
                currentWord.append(character);
                data = input.read();
            }
        }
        input.close();
        return true;
    }

    private boolean matchToken(String text, Integer line) {
        for (Map.Entry<Token.tokenType, String> entry : sortedPatterns) {
            String pattern = entry.getValue();
            Pattern regex = Pattern.compile(pattern);
            Matcher matcher = regex.matcher(text);
            if (matcher.matches()) {
                // System.out.println(entry.getKey() + " " + text);
                lexer.add(new Lexer(entry.getKey(), text, line));
                return true;
            }
        }
        System.out.println(text);
        ErrorInfo errorInfo = new ErrorInfo(line, "a");
        errorList.add(errorInfo);
        if (Objects.equals(text, "&")) {
            lexer.add(new Lexer(Token.tokenType.AND, "&&", line));
        } else if (Objects.equals(text, "|")) {
            lexer.add(new Lexer(Token.tokenType.OR, "||", line));
        }
        // System.out.println("Unrecognized token: " + text + " on line " + line);
        return false;
    }

    public ArrayList<Lexer> getLexer() {
        return lexer;
    }

    public ArrayList<ErrorInfo> getErrorList() {
        return errorList;
    }
}

