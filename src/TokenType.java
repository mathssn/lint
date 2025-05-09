package src;
public enum TokenType {
    // Operadores
    SUM_OP, SUB_OP, MULT_OP, DIV_OP, ASSIGN_OP, COLON, GREATER_OP, LESS_OP, EQUAL_OP, GREATER_EQUAL_OP, LESS_EQUAL_OP, AND_OP, OR_OP, RIGHT_ARROW,

    // Demilitadores
    LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE, SEMI_COL, COMMA,

    // Literais
    INT_LIT, FLOAT, CHAR_LIT,

    // Palavras chave e identificadores
    KEYWORD, IDENT,

    // Outros
    EOF

}
