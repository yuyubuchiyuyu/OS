package frontend;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static frontend.Token.patterns;

public class ProcessInput {
    private final InputStream input;
    private int line = 1;
    private final List<Map.Entry<Token.tokenType, String>> sortedPatterns;
    private boolean sign = true;
    private ArrayList<Lexer> lexer = new ArrayList<>();
    private int errorLine = 0;
    private char errorSign = '\0';

    public ProcessInput(InputStream input) {
        this.input = input;
        this.sortedPatterns = new ArrayList<>(patterns.entrySet());
    }

    private boolean judgeSingle(char ch) {
        return ch == ';' || ch == ',' || ch == '(' || ch == ')' || ch == '[' || ch == ']' || ch == '{' || ch == '}' ||
                ch == '-' || ch == '+' || ch == '/' || ch == '*' || ch == '%';
    }

    private boolean judgeDouble(char ch) {
        return ch == '<' || ch == '>' || ch == '=' || ch == '&' || ch == '|' || ch == '!';
    }

    private void matchWord(StringBuilder currentWord) {
        if (!currentWord.isEmpty()) {
            sign = matchToken(currentWord.toString(), line);
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
                data = input.read(); // 逐字符读取，直至遇到匹配引号
                character = (char) data;
                while (data != -1 && !((character == '"' || character == '\'') && (lastCharacter != '\\'))) {
                    currentWord.append(character);
                    lastCharacter = character;
                    data = input.read();
                    character = (char) data;
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
                    while (data!=-1 && character != '\n') {
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
                matchWord(currentWord);
                currentWord.append(character);
                matchWord(currentWord);
                data = input.read();
            } else if (judgeDouble(character)) {
                matchWord(currentWord);
                currentWord.append(character);
                data = input.read(); // 检查后面是否是可能的多字符
                character = (char) data;
                if (judgeDouble(character)) {
                    currentWord.append(character);
                    matchWord(currentWord);
                    data = input.read();
                } else {
                    matchWord(currentWord);
                }
            } else {
                currentWord.append(character);
                data = input.read();
            }
            if (!sign) {
                return false;
            }
        }
        input.close();
        return true;
    }

    private boolean matchToken(String text, int line) {
        for (Map.Entry<Token.tokenType, String> entry : sortedPatterns) {
            String pattern = entry.getValue();
            Pattern regex = Pattern.compile(pattern);
            Matcher matcher = regex.matcher(text);
            if (matcher.matches()) {
                System.out.println(entry.getKey() + " " + text);
                lexer.add(new Lexer(entry.getKey(), text));
                return true;
            }
        }
        errorLine = line;
        errorSign = 'a';
        System.out.println("Unrecognized token: " + text + " on line " + line);
        return false;
    }

    public ArrayList<Lexer> getLexer() {
        return lexer;
    }

    public int getErrorLine() {
        return errorLine;
    }

    public char getErrorSign() {
        return errorSign;
    }
}

