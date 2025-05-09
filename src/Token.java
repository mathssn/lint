package src;
import java.util.HashMap;

import src.utils.Utils;

public class Token {

    public final String lexem; // Valor do token
    public final TokenType type; // Tipo do token
    public int line;
    public int collumn;

    private final static HashMap<String, TokenType> TOKENTYPES = new HashMap<String, TokenType>();
    private final static String[] KEYWORDS = {"var", "int", "float", "bool", "char", "null", "return", "stop", "skip", "if", "else", "repeat", "to", "step", "while", "func", "class", "True", "False", "void", "inst"};
    
    static {
        // Adiciona os tipos de tokens
        TOKENTYPES.put("+", TokenType.SUM_OP);
        TOKENTYPES.put("-", TokenType.SUB_OP);
        TOKENTYPES.put("*", TokenType.MULT_OP);
        TOKENTYPES.put("/", TokenType.DIV_OP);
        TOKENTYPES.put("=", TokenType.ASSIGN_OP);
        TOKENTYPES.put(";", TokenType.SEMI_COL);
        TOKENTYPES.put("(", TokenType.LEFT_PAREN);
        TOKENTYPES.put(")", TokenType.RIGHT_PAREN);
        TOKENTYPES.put("{", TokenType.LEFT_BRACE);
        TOKENTYPES.put("}", TokenType.RIGHT_BRACE);
        TOKENTYPES.put(":", TokenType.COLON);
        TOKENTYPES.put(">", TokenType.GREATER_OP);
        TOKENTYPES.put("<" ,TokenType.LESS_OP);
        TOKENTYPES.put(",", TokenType.COMMA);
        TOKENTYPES.put("==", TokenType.EQUAL_OP);
        TOKENTYPES.put(">=", TokenType.GREATER_EQUAL_OP);
        TOKENTYPES.put("<=", TokenType.LESS_EQUAL_OP);
        TOKENTYPES.put("&&", TokenType.AND_OP);
        TOKENTYPES.put("||", TokenType.OR_OP);
        TOKENTYPES.put("->", TokenType.RIGHT_ARROW);
    }

    public Token(String lexem, int line, int collumn) {
        // Inicializa o token
        this.lexem = lexem;
        this.line = line;
        this.collumn = collumn;

        if (lexem.equals("EOF")) {
            type = TokenType.EOF;
            return;
        }

        // Verifica se o lexema está em tokentypes
        TokenType t = TOKENTYPES.get(lexem);
        if (t != null) {
            type = t;
            return;
        }

        for (String keyWord: KEYWORDS) {
            if (keyWord.equals(lexem)) {
                type = TokenType.KEYWORD;
                return;
            }
        }

        // Verifica o lexema é um inteiro
        if (Utils.isDigit(lexem)) {
            type = TokenType.INT_LIT;
            return;
        }

        // Verifica o lexema é um float
        boolean float_ = true;
        int points = 0;
        for (int i = 0; i < lexem.length(); i++) {
            if (!Character.isDigit(lexem.charAt(i))) {
                if (lexem.charAt(i) == '.' && points == 0) {
                    points++;
                } else {
                    float_ = false;
                }
            }
        }

        if (float_) {
            type = TokenType.FLOAT;
            return;
        }

        if (lexem.length() == 3 && lexem.charAt(0) == '\'' && lexem.charAt(2) == '\'') {
                type = TokenType.CHAR_LIT;
                return;
        }

        if (Character.isAlphabetic(lexem.charAt(0)) || lexem.charAt(0) == '_') {
            type = TokenType.IDENT;
            return;
        }

        type = TokenType.EOF;
        System.out.printf("Lexema invalido: %s na linha %d, coluna %d%n", lexem, line, collumn);
    }

    @Override
    public String toString() {
        return String.format("%s - %s - %d - %d", type, lexem, line, collumn);
    }
}
