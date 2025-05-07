import java.util.HashMap;

public class Token {

    String lexem; // Valor do token
    String type; // Tipo do token
    int line;
    int collumn;

    private final static HashMap<String, String> TOKENTYPES = new HashMap<String, String>();
    private final static String[] KEYWORDS = {"var", "int", "float", "bool", "char", "null", "return", "break", "continue", "if", "else", "repeat", "to", "step", "loop", "func", "class", "True", "False", "void", "inst"};
    
    static {
        // Adiciona os tipos de tokens
        TOKENTYPES.put("+", "SUM_OP");
        TOKENTYPES.put("-", "SUB_OP");
        TOKENTYPES.put("*", "MULT_OP");
        TOKENTYPES.put("/", "DIV_OP");
        TOKENTYPES.put("=", "ASSIGN_OP");
        TOKENTYPES.put(";", "SEMI_COL");
        TOKENTYPES.put("(", "LEFT_PAREN");
        TOKENTYPES.put(")", "RIGHT_PAREN");
        TOKENTYPES.put("{", "LEFT_BRACE");
        TOKENTYPES.put("}", "RIGHT_BRACE");
        TOKENTYPES.put(":", "COLON");
        TOKENTYPES.put(">", "GREATER_OP");
        TOKENTYPES.put("<" , "LESS_OP");
        TOKENTYPES.put("==", "EQUAL_OP");
        TOKENTYPES.put(">=", "GREATER_EQUAL_OP");
        TOKENTYPES.put("<=", "LESS_EQUAL_OP");
        TOKENTYPES.put("&&", "AND_OP");
        TOKENTYPES.put("||", "OR_OP");
        TOKENTYPES.put(",", "COMMA");
    }

    public Token(String lexem, int line, int collumn) {
        // Inicializa o token
        this.lexem = lexem;
        this.line = line;
        this.collumn = collumn;

        // Verifica se o lexema está em tokentypes
        type = TOKENTYPES.get(lexem);
        if (type != null) {
            return;
        }

        for (String keyWord: KEYWORDS) {
            if (keyWord.equals(lexem)) {
                type = "KEYWORD";
                return;
            }
        }

        // Verifica o lexema é um inteiro
        if (Utils.isDigit(lexem)) {
            type = "INT_LIT";
            return;
        }

        // Verifica o lexema é um float
        boolean float_ = true;
        int points = 0;
        for (int i = 0; i < lexem.length(); i++) {
            if (!Utils.isDigit(lexem.charAt(i))) {
                if (lexem.charAt(i) == '.' && points == 0) {
                    points++;
                } else {
                    float_ = false;
                }
            }
        }

        if (float_) {
            type = "FLOAT";
            return;
        }

        if (lexem.length() == 3 && lexem.charAt(0) == '\'' && lexem.charAt(2) == '\'') {
                type = "CHAR_LIT";
                return;
        }

        if (Utils.isAlphabetic(lexem.charAt(0)) || lexem.charAt(0) == '_') {
            type = "IDENT";
            return;
        }

        System.out.printf("Lexema invalido: %s na linha %d, coluna %d%n", lexem, line, collumn);
    }

    @Override
    public String toString() {
        return String.format("%s - %s - %d - %d", type, lexem, line, collumn);
    }
}
