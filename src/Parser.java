package src;
import java.util.ArrayList;
import java.util.Set;
// import java.util.HashMap;

import src.ast.Node;
import src.ast.NodeType;

public class Parser {

    public ArrayList<Token> tokens;
    public ArrayList<Node> nodes = new ArrayList<Node>();
    private int index = 0;
    private Token token; // Guarda o token atual
    // public HashMap<String, Symbol> symbolTable = new HashMap<String, Symbol>();

    public static Set<String> tipos = Set.of("int", "char", "bool", "float");
    public static Set<TokenType> logicOperators = Set.of(TokenType.GREATER_OP, TokenType.LESS_OP, TokenType.EQUAL_OP, TokenType.GREATER_EQUAL_OP, TokenType.LESS_EQUAL_OP);
    
    public Parser(ArrayList<Token> tokens) {
        this.tokens = tokens;
        token = tokens.get(index);
    }

    public void parseProgram() {
        // Faz o parse do programa inteiro
        if (token.type == TokenType.EOF) {
            throw new RuntimeException("Erro!");
        }

        while (token.type != TokenType.EOF) {
            if (token.type == TokenType.KEYWORD && token.lexem.equals("func")) {
                Node newNode = parseFunc();
                nodes.add(newNode);
            }
        }
    }

    public ArrayList<Node> parseBlock() {
        // Faz o parse de um bloco
        if (token.type == TokenType.EOF) {
            throw new RuntimeException("Erro!");
        }

        ArrayList<Node> nodes = new ArrayList<Node>();
        while (token.type != TokenType.EOF && token.type != TokenType.RIGHT_BRACE) {
            if (token.type == TokenType.KEYWORD && token.lexem.equals("var")) {
                Node newNode = parseDeclaration();
                nodes.add(newNode);
            }
            else if (token.type == TokenType.IDENT) {
                Node newNode = parseAssignment();
                nodes.add(newNode);
            }
            else if (token.type == TokenType.KEYWORD && token.lexem.equals("if")) {
                Node newNode = parseIfStmt();
                nodes.add(newNode);
            }
            else if (token.type == TokenType.KEYWORD && token.lexem.equals("repeat")) {
                Node newNode = parseRepeatStmt();
                nodes.add(newNode);
            }
            else if (token.type == TokenType.KEYWORD && token.lexem.equals("return")) {
                Node newNode = parseReturnStmt();
                nodes.add(newNode);
            }
            else if (token.type == TokenType.KEYWORD && token.lexem.equals("stop")) {
                Node newNode = parseStopStmt();
                nodes.add(newNode);
            }
            else if (token.type == TokenType.KEYWORD && token.lexem.equals("skip")) {
                Node newNode = parseSkipStmt();
                nodes.add(newNode);
            }
            else {
                int line = token.line;
                Node newNode = parseExpr();

                if (token.type == TokenType.SEMI_COL) {
                    eat();
                    nodes.add(newNode);
                } else {
                    throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado ';' ao fim de uma expressaõ", line));
                }
            }
        }

        return nodes;
    }

    public Node parseDeclaration() { // Faz o parse de uma declaração de variavel
        // Verifica se há um token
        if (token.type == TokenType.EOF) {
            throw new RuntimeException("Erro: Esperado a palavra chave 'var' para declaração");
        }
        int line = token.line; // Armazena a linha atual
        Token ident; // Token do nome da variavel
        Token tipo; // Token do tipo da variavel

        // Verifica se a palavra-chave 'var' foi colocada
        if (token.type != TokenType.KEYWORD  || !token.lexem.equals("var")) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado a palavra-chave 'var' para declaração", line));
        }
        eat();

        // Verifica se o identificador foi colocado apos 'var'
        if (token.type != TokenType.IDENT) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado um identificador após 'var'", line));
        }
        ident = token;
        eat();
        
        // Verifica se após o identificador existe '=', ex.: 'var x = 5;'
        if (token.type == TokenType.ASSIGN_OP) {
            eat();
            Node rightNode = parseExpr();

            // Verifica se ';' foi colocado corretamente
            if (token.type == TokenType.SEMI_COL) {
                eat();
            } else {
                throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado ';' ao final da atribuição", line));
            }

            return new Node(NodeType.DECL, null, ident.lexem, null, rightNode, null);
        }

        // Verifica se ':' foi colocado após o identificador
        if (token.type != TokenType.COLON) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado '=' ou ':' após o identificador", line));
        }
        eat();

        // Verifica se um tipo foi colocado após ':'
        if (token.type != TokenType.KEYWORD  || !tipos.contains(token.lexem)) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado um tipo após ':'", line));
        }

        // Armazena o tipo
        tipo = token;
        eat();
        
        // Verifica se após o tipo foi colocado ';', ex.: 'var x: int;'
        if (token.type == TokenType.SEMI_COL) {
            eat();
            return new Node(NodeType.DECL, null, ident.lexem, null, null, null);
        }
        
        if (token.type != TokenType.ASSIGN_OP) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado '=' ou ';' após o tipo", line));
        }
        eat();

        Node rightNode = parseExpr();

        // Verifica se ';' foi colocado após a expressão
         if (token.type != TokenType.SEMI_COL) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado ';' após a expressão", line));
        }
        eat();

        return new Node(NodeType.DECL, null, ident.lexem, null, rightNode, null);
    }
    
    public Node parseAssignment() {
        // Verifica se há um token
        if (token.type == TokenType.EOF) {
            throw new RuntimeException("Erro: Esperado um identificador para atribuição");
        }
        int line = token.line; // Armazena a linha atual
        Token ident;
        
        // Verifica se o identificador foi colocado
        if (token.type != TokenType.IDENT) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado um identificador", line));
        }
        ident = token;
        eat();

        // Verifica se o proximo token é '='
        if (token.type != TokenType.ASSIGN_OP) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado um '=' após o identificador", line));
        }
        eat();

        // Faz o parse da expressão depois de '='
        Node rightNode = parseExpr();

        if (token.type != TokenType.SEMI_COL) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado ';' ao fim da declaração", line));
        }
        eat();

        return new Node(NodeType.ATRIB, null, ident.lexem, null, rightNode, null);
    }

    public Node parseIfStmt() {
        // Verifica se há um token
        if (token.type == TokenType.EOF) {
            throw new RuntimeException("Erro: Esperado 'if'");
        }
        int line = token.line; // Armazena a linha atual

        if (token.type != TokenType.KEYWORD  || !token.lexem.equals("if")) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado 'if'", line));
        } eat();

        if (token.type != TokenType.LEFT_PAREN) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado '(' após 'if'", line));
        } eat();

        Node logicExpr = parseLogicExpr();

        if (token.type != TokenType.RIGHT_PAREN) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado ')' após a expressão", line));
        } eat();

        if (token.type != TokenType.LEFT_BRACE) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado '{' após o 'if'", line));
        } eat();

        ArrayList<Node> block = parseBlock();

        if (token.type != TokenType.RIGHT_BRACE) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado '}' após o fim do bloco", line));
        } eat();

        return new Node(NodeType.CONDITIONAL, null, null, logicExpr, null, block);
    }

    public Node parseLogicExpr() {
        if (token.type == TokenType.EOF) {
            throw new RuntimeException("Erro: Esperado uma expressão logica após '('");
        }

        Node leftNode = parseLogicTerm();

        while (token.type == TokenType.AND_OP || token.type == TokenType.OR_OP) {
            Token operator = token;
            eat();
            Node rightNode = parseLogicTerm();

            leftNode = new Node(NodeType.LOGIC_OPERATOR, operator.lexem, null, leftNode, rightNode, null);
        }

        return leftNode;
    }

    public Node parseLogicTerm() {
        if (token.type == TokenType.EOF) {
            throw new RuntimeException("Erro: Esperado uma expressão logica após '('");
        }
        
        Node leftNode = parseExpr();

        if (logicOperators.contains(token.type)) {
            Token operator = token;
            eat();
            Node rightNode = parseExpr();
            return new Node(NodeType.LOGIC_OPERATOR, operator.lexem, null, leftNode, rightNode, null);
        }

        return leftNode;
    }

    public Node parseRepeatStmt() {
        // Verifica se há um token
        if (token.type == TokenType.EOF) {
            throw new RuntimeException("Erro: Esperado um palavra-chave 'repeat'");
        }
        int line = token.line; // Armazena a linha atual
        Token ident;

        if (token.type != TokenType.KEYWORD  && !token.lexem.equals("repeat")) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado 'repeat'", line));
        } eat();

        if (token.type == TokenType.KEYWORD  && token.lexem.equals("while")) {
            return parseWhileStmt();
        }

        if (token.type != TokenType.LEFT_PAREN) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado '(' após 'repeat'", line));
        } eat();

        if (token.type != TokenType.KEYWORD  || !token.lexem.equals("var")) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado 'var' após '('", line));
        } eat();
        
        if (token.type != TokenType.IDENT) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado um identificador após 'var'", line));
        } 
        ident = token;
        eat();

        if (token.type != TokenType.SEMI_COL) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado ';' após o identificador", line));
        } eat();

        Node init = parseExpr();

        if (token.type != TokenType.KEYWORD  || !token.lexem.equals("to")) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado 'to' após a expressão", line));
        } eat();

        Node end = parseExpr();
        Node step = null;

        if (token.type == TokenType.SEMI_COL) {
            eat();
            
            if (token.type != TokenType.KEYWORD  || !token.lexem.equals("step")) {
                throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado 'step' após ';'", line));
            }
            eat();

            step = parseExpr();
            step = new Node(NodeType.STEP, null, null, end, step, null);
        } else if (token.type == TokenType.KEYWORD && token.lexem.equals("step")) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado ';' entre 'step' e o intervalo", line));
        }

        if (token.type != TokenType.RIGHT_PAREN) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado ')' ao final do 'repeat'", line));
        } eat();

        if (token.type != TokenType.LEFT_BRACE) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado '{' no inicio do bloco", line));
        } eat();

        ArrayList<Node> block = parseBlock();

        if (token.type != TokenType.RIGHT_BRACE) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado '}' ao final do bloco", line));
        } eat();

        if (step == null) {   
            step = new Node(NodeType.STEP, null, null, end, new Node(NodeType.INT, "1", null, null, null, null), null);
        }

        return new Node(NodeType.REPEAT, null, ident.lexem, init, step, block);
    }

    public Node parseWhileStmt() {
        // Verifica se há um token
        if (token.type == TokenType.EOF) {
            throw new RuntimeException("Erro: Esperado um palavra-chave 'while'");
        }
        int line = token.line; // Armazena a linha atual

        if (token.type != TokenType.KEYWORD  && !token.lexem.equals("while")) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado 'while'", line));
        } eat();

        if (token.type != TokenType.LEFT_PAREN) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado '(' após 'repeat'", line));
        } eat();

        Node logicExpr = parseLogicExpr();

        if (token.type != TokenType.RIGHT_PAREN) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado ')' ao final do 'repeat'", line));
        } eat();

        if (token.type != TokenType.LEFT_BRACE) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado '{' no inicio do bloco", line));
        } eat();

        ArrayList<Node> block = parseBlock();

        if (token.type != TokenType.RIGHT_BRACE) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado '}' ao final do bloco", line));
        } eat();

        return new Node(NodeType.WHILE, null, null, logicExpr, null, block);
    }

    public Node parseReturnStmt() {
        // Verifica se há um token
        if (token.type == TokenType.EOF) {
            throw new RuntimeException("Erro: Esperado um palavra-chave 'while'");
        }
        int line = token.line; // Armazena a linha atual

        if (token.type != TokenType.KEYWORD  && !token.lexem.equals("return")) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado 'return'", line));
        } eat();

        Node expr = parseExpr();

        if (token.type != TokenType.SEMI_COL) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado ';' após a expressão", line));
        } eat();

        return new Node(NodeType.RETURN, null, null, expr, null, null);
    }

    public Node parseStopStmt() {
        // Verifica se há um token
        if (token.type == TokenType.EOF) {
            throw new RuntimeException("Erro: Esperado um palavra-chave 'stop'");
        }
        int line = token.line; // Armazena a linha atual

        if (token.type != TokenType.KEYWORD  && !token.lexem.equals("stop")) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado 'stop'", line));
        } eat();

        if (token.type != TokenType.SEMI_COL) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado ';' após 'stop'", line));
        } eat();

        return new Node(NodeType.STOP, null, null, null, null, null);
    }

    
    public Node parseSkipStmt() {
        // Verifica se há um token
        if (token.type == TokenType.EOF) {
            throw new RuntimeException("Erro: Esperado um palavra-chave 'skip'");
        }
        int line = token.line; // Armazena a linha atual
        
        if (token.type != TokenType.KEYWORD  && !token.lexem.equals("skip")) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado 'skip'", line));
        } eat();
        
        if (token.type != TokenType.SEMI_COL) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado ';' após 'skip'", line));
        } eat();
        
        return new Node(NodeType.SKIP, null, null, null, null, null);
    }

    public Node parseFunc() {
        if (token.type == TokenType.EOF) {
            throw new RuntimeException("Erro: Esperado um palavra-chave 'func'");
        }
        int line = token.line; // Armazena a linha atual
        Token ident;
        Node params = null;

        if (token.type != TokenType.KEYWORD  && !token.lexem.equals("func")) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado 'func'", line));
        } eat();

        if (token.type != TokenType.IDENT) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado um identificador após 'func'", line));
        }
        ident = token; 
        eat();

        if (token.type != TokenType.LEFT_PAREN) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado '(' após o identificador", line));
        } eat();

        if (token.type == TokenType.IDENT) {
            params = parseParams();
        }

        if (token.type != TokenType.RIGHT_PAREN) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado ')'", line));
        } eat();

        if (token.type != TokenType.RIGHT_ARROW) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado '->' após a função", line));
        } eat();

        if (!tipos.contains(token.lexem) && !token.lexem.equals("void")) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado um tipo após '->'", line));
        } eat();

        if (token.type != TokenType.LEFT_BRACE) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado '{' no inicio do bloco", line));
        } eat();

        ArrayList<Node> block = parseBlock();

        if (token.type != TokenType.RIGHT_BRACE) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado '}' ao final do bloco", line));
        } eat();

        return new Node(NodeType.FUNC, null, ident.lexem, params, null, block);
    }

    private Node parseParams() {
        if (token.type == TokenType.EOF) {
            throw new RuntimeException("Erro: Esperado um identificador");
        }
        int line = token.line;

        ArrayList<Node> params = new ArrayList<Node>();

        while (token.type != TokenType.EOF && token.type == TokenType.IDENT) {
            Node param = parseParam();
            
            if (token.type == TokenType.COMMA) {
                eat();
                if (token.type != TokenType.IDENT) {
                    throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado um parametro após ','", line));
                }
            }

            params.add(param);
        }

        return new Node(NodeType.PARAMS, null, null, null, null, params);
    }

    private Node parseParam() {
        if (token.type == TokenType.EOF) {
            throw new RuntimeException("Erro: Esperado um identificador");
        }
        int line = token.line;
        Token ident;
        Token tipo;

        if (token.type != TokenType.IDENT) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado um identificador como parametro", line));
        } 
        ident = token;
        eat();

        if (token.type != TokenType.COLON) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado um ':' após o identificador", line));
        } eat();

        if (token.type != TokenType.KEYWORD || !tipos.contains(token.lexem)) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Tipo do parametro não definido", line));
        } 
        tipo = token;
        eat();

        return new Node(NodeType.PARAM, null, ident.lexem, null, null, null);
    }
    
    public Node parseExpr() {
        // Faz o parse de uma expressão
        Node leftNode = parseTerm(); // Faz o parse do lado esquerdo do sinal

        while (token.type == TokenType.SUM_OP || token.type == TokenType.SUB_OP) {
            // Se for um operador + ou -, faz o parse do lado direito
            Token operator = token;
            eat();
            Node rightNode = parseTerm();
            // Cria um novo nó
            leftNode = new Node(NodeType.BINARY_OPERATOR, operator.lexem, null, leftNode, rightNode, null);
        }
        return leftNode;
    }

    public Node parseTerm() {
        // Faz o parse de um termo
        Node leftNode = parseFactor(); // Faz o parse do lado esquerdo do sinal
        while (token.type == TokenType.MULT_OP || token.type == TokenType.DIV_OP) {
            // Se for um operador * ou /, faz o parse do lado direito
            Token operator = token;
            eat();
            Node rightNode = parseFactor();
            // Cria um novo nó
            leftNode = new Node(NodeType.BINARY_OPERATOR, operator.lexem, null, leftNode, rightNode, null);
        }
        return leftNode;
    }

    public Node parseFactor() {
        // Verifica se há um token, se não, lança um erro
        if (token.type == TokenType.EOF) {
            throw new RuntimeException(String.format("Erro desconhecido!"));
        }
        int line = token.line; // Armazena a linha atual

        // Verifica se é um operador unario
        if (token.type == TokenType.SUB_OP) {
            eat();

            // Verifica se apos o operador vem um inteiro, um float ou uma variavel
            if (token.type == TokenType.INT_LIT || token.type == TokenType.FLOAT || token.type == TokenType.IDENT) {
                Node newNode = parseFactor();
                return new Node(NodeType.UNARY_OPERATOR, "-", null, null, newNode, null);
            }
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado um numero ou identificador após '-'%n", line));
        }

        // Verifica se é um parentese de abertura
        else if (token.type == TokenType.LEFT_PAREN) {
            eat();
            Node newNode = parseExpr(); // Faz o parse da expressão
            // Verifica se o parentese de fechamento foi colocado corretamente
            if (token.type == TokenType.RIGHT_PAREN) {
                eat();
                return newNode;
            }
            // Lança um erro se nõ encontrar o parenteses
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado ')'%n", line));
        }

        // Verifica se é um inteiro
        else if (token.type == TokenType.INT_LIT) {
            Node newNode = new Node(NodeType.INT, token.lexem, null, null, null, null);
            eat();
            return newNode;
        }

        // Verifica se é um caractere
        else if (token.type == TokenType.CHAR_LIT) {
            Node newNode = new Node(NodeType.CHAR, token.lexem, null, null, null, null);
            eat();
            return newNode;
        }

        // Verifica se é um float
        else if (token.type == TokenType.FLOAT) {
            Node newNode = new Node(NodeType.FLOAT, token.lexem, null, null, null, null);
            eat();
            return newNode;
        }

        // Verifica se é um booleano
        else if (token.lexem.equals("True") || token.lexem.equals("False")) {
            Node newNode = new Node(NodeType.BOOL, token.lexem, null, null, null, null);
            eat();
            return newNode;
        }

        // Verifica se é um ponteiro nulo
        else if (token.lexem.equals("null")) {
            Node newNode = new Node(NodeType.NULL, null, null, null, null, null);
            eat();
            return newNode;
        }

        // Verifica se é um identificador
        else if (token.type == TokenType.IDENT) {
            Node newNode = new Node(NodeType.IDENT, null, token.lexem, null, null, null);
            eat();
            return newNode;
        }

        // System.out.println(token);
        throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado um numero, identificador, parenteses ou caractere, mas nenhum foi encontrado%n", line));
    }

    public Token peek() {
        if (index < tokens.size()) {
            return tokens.get(index);
        }
        return new Token("EOF", 0, 0);
    }

    public void eat() {
        index++;
        token = peek();
    }
}
