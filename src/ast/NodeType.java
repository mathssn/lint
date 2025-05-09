package src.ast;

public enum NodeType {
    // Statements
    DECL, ATRIB, CONDITIONAL, REPEAT, STEP, WHILE, RETURN, STOP, SKIP, FUNC,

    // Operadores
    LOGIC_OPERATOR, BINARY_OPERATOR, UNARY_OPERATOR, 

    // Identificadores
    IDENT,
    
    // Literais
    INT, CHAR, FLOAT, BOOL, NULL
}