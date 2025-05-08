import java.util.ArrayList;
import java.util.Set;
// import java.util.HashMap;

public class Parser {

    public ArrayList<Token> tokens;
    public ArrayList<Node> nodes = new ArrayList<Node>();
    private int index = 0;
    private Token token; // Guarda o token atual
    // public HashMap<String, Symbol> symbolTable = new HashMap<String, Symbol>();

    public static Set<String> tipos = Set.of("int", "char", "bool", "float");
    public static Set<String> logicOperators = Set.of("GREATER_OP", "LESS_OP", "EQUAL_OP", "GREATER_EQUAL_OP", "LESS_EQUAL_OP");
    
    public Parser(ArrayList<Token> tokens) {
        this.tokens = tokens;
        token = tokens.get(index);
    }

    public void parseStmt() {
        // Faz o parse do programa inteiro
        if (token.type.equals("EOF")) {
            throw new RuntimeException("Erro!");
        }

        while (!token.type.equals("EOF")) {
            if (token.type.equals("KEYWORD") && token.lexem.equals("var")) {
                Node newNode = parseDeclaration();
                nodes.add(newNode);
            }
            else if (token.type.equals("IDENT")) {
                Node newNode = parseAssignment();
                nodes.add(newNode);
            }
            else if (token.type.equals("KEYWORD") && token.lexem.equals("if")) {
                Node newNode = parseIfStmt();
                nodes.add(newNode);
            }
            else if (token.type.equals("KEYWORD") && token.lexem.equals("repeat")) {
                Node newNode = parseRepeatStmt();
                nodes.add(newNode);
            }
            else {
                int line = token.line;
                Node newNode = parseExpr();

                if (token.type.equals("SEMI_COL")) {
                    eat();
                    nodes.add(newNode);
                } else {
                    throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado ';' ao fim de uma expressaõ", line));
                }
            }
        }
    }

    public ArrayList<Node> parseBlock() {
        // Faz o parse de um bloco
        if (token.type.equals("EOF")) {
            throw new RuntimeException("Erro!");
        }

        ArrayList<Node> nodes = new ArrayList<Node>();
        while (!token.type.equals("EOF") && !token.type.equals("RIGHT_BRACE")) {
            if (token.type.equals("KEYWORD") && token.lexem.equals("var")) {
                Node newNode = parseDeclaration();
                nodes.add(newNode);
            }
            else if (token.type.equals("IDENT")) {
                Node newNode = parseAssignment();
                nodes.add(newNode);
            }
            else if (token.type.equals("KEYWORD") && token.lexem.equals("if")) {
                Node newNode = parseIfStmt();
                nodes.add(newNode);
            }
            else if (token.type.equals("KEYWORD") && token.lexem.equals("repeat")) {
                Node newNode = parseRepeatStmt();
                nodes.add(newNode);
            }
            else {
                int line = token.line;
                Node newNode = parseExpr();

                if (token.type.equals("SEMI_COL")) {
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
        if (token.type.equals("EOF")) {
            throw new RuntimeException("Erro: Esperado a palavra chave 'var' para declaração");
        }
        int line = token.line; // Armazena a linha atual
        Token ident; // Token do nome da variavel
        Token tipo; // Token do tipo da variavel

        // Verifica se a palavra-chave 'var' foi colocada
        if (!token.type.equals("KEYWORD") || !token.lexem.equals("var")) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado a palavra-chave 'var' para declaração", line));
        }
        eat();

        // Verifica se o identificador foi colocado apos 'var'
        if (!token.type.equals("IDENT")) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado um identificador após 'var'", line));
        }
        ident = token;
        eat();
        
        // Verifica se após o identificador existe '=', ex.: 'var x = 5;'
        if (token.type.equals("ASSIGN_OP")) {
            eat();
            Node rightNode = parseExpr();

            // Verifica se ';' foi colocado corretamente
            if (token.type.equals("SEMI_COL")) {
                eat();
            } else {
                throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado ';' ao final da atribuição", line));
            }

            return new Node("decl", null, ident.lexem, null, rightNode, null);
        }

        // Verifica se ':' foi colocado após o identificador
        if (!token.type.equals("COLON")) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado '=' ou ':' após o identificador", line));
        }
        eat();

        // Verifica se um tipo foi colocado após ':'
        if (!token.type.equals("KEYWORD") || !tipos.contains(token.lexem)) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado um tipo após ':'", line));
        }

        // Armazena o tipo
        tipo = token;
        eat();
        
        // Verifica se após o tipo foi colocado ';', ex.: 'var x: int;'
        if (token.type.equals("SEMI_COL")) {
            eat();
            return new Node("decl", null, ident.lexem, null, null, null);
        }
        
        if (!token.type.equals("ASSIGN_OP")) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado '=' ou ';' após o tipo", line));
        }
        eat();

        Node rightNode = parseExpr();

        // Verifica se ';' foi colocado após a expressão
         if (!token.type.equals("SEMI_COL")) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado ';' após a expressão", line));
        }
        eat();

        return new Node("decl", null, ident.lexem, null, rightNode, null);
    }
    
    public Node parseAssignment() {
        // Verifica se há um token
        if (token.type.equals("EOF")) {
            throw new RuntimeException("Erro: Esperado um identificador para atribuição");
        }
        int line = token.line; // Armazena a linha atual
        Token ident;
        
        // Verifica se o identificador foi colocado
        if (!token.type.equals("IDENT")) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado um identificador", line));
        }
        ident = token;
        eat();

        // Verifica se o proximo token é '='
        if (!token.type.equals("ASSIGN_OP")) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado um '=' após o identificador", line));
        }
        eat();

        // Faz o parse da expressão depois de '='
        Node rightNode = parseExpr();

        if (!token.type.equals("SEMI_COL")) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado ';' ao fim da declaração", line));
        }
        eat();

        return new Node("atrib", null, ident.lexem, null, rightNode, null);
    }

    public Node parseIfStmt() {
        // Verifica se há um token
        if (token.type.equals("EOF")) {
            throw new RuntimeException("Erro: Esperado 'if'");
        }
        int line = token.line; // Armazena a linha atual

        if (!token.type.equals("KEYWORD") || !token.lexem.equals("if")) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado 'if'", line));
        } eat();

        if (!token.type.equals("LEFT_PAREN")) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado '(' após 'if'", line));
        } eat();

        Node logicExpr = parseLogicExpr();

        if (!token.type.equals("RIGHT_PAREN")) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado ')' após a expressão", line));
        } eat();

        if (!token.type.equals("LEFT_BRACE")) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado '{' após o 'if'", line));
        } eat();

        ArrayList<Node> block = parseBlock();

        if (!token.type.equals("RIGHT_BRACE")) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado '}' após o fim do bloco", line));
        } eat();

        return new Node("conditional", null, null, logicExpr, null, block);
    }

    public Node parseLogicExpr() {
        if (token.type.equals("EOF")) {
            throw new RuntimeException("Erro: Esperado uma expressão logica após '('");
        }

        Node leftNode = parseLogicTerm();

        while (token.type.equals("AND_OP") || token.type.equals("OR_OP")) {
            Token operator = token;
            eat();
            Node rightNode = parseLogicTerm();

            leftNode = new Node("logic_operator", operator.lexem, null, leftNode, rightNode, null);
        }

        return leftNode;
    }

    public Node parseLogicTerm() {
        if (token.type.equals("EOF")) {
            throw new RuntimeException("Erro: Esperado uma expressão logica após '('");
        }
        
        Node leftNode = parseExpr();

        if (logicOperators.contains(token.type)) {
            Token operator = token;
            eat();
            Node rightNode = parseExpr();
            return new Node("logic_operator", operator.lexem, null, leftNode, rightNode, null);
        }

        return leftNode;
    }

    public Node parseRepeatStmt() {
        // Verifica se há um token
        if (token == null) {
            throw new RuntimeException("Erro: Esperado um palavra-chave 'repeat'");
        }
        int line = token.line; // Armazena a linha atual
        Token ident;

        if (!token.type.equals("KEYWORD") && !token.lexem.equals("repeat")) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado 'repeat'", line));
        } eat();

        if (!token.type.equals("LEFT_PAREN")) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado '(' após 'repeat'", line));
        } eat();

        if (!token.type.equals("KEYWORD") || !token.lexem.equals("var")) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado 'var' após '('", line));
        } eat();
        
        if (!token.type.equals("IDENT")) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado um identificador após 'var'", line));
        } 
        ident = token;
        eat();

        if (!token.type.equals("SEMI_COL")) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado ';' após o identificador", line));
        } eat();

        Node init = parseExpr();

        if (!token.type.equals("KEYWORD") || !token.lexem.equals("to")) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado 'to' após a expressão", line));
        } eat();

        Node end = parseExpr();

        if (!token.type.equals("RIGHT_PAREN")) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado ')' ao final do 'repeat'", line));
        } eat();

        if (!token.type.equals("LEFT_BRACE")) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado '{' no inicio do bloco", line));
        } eat();

        ArrayList<Node> block = parseBlock();

        if (!token.type.equals("RIGHT_BRACE")) {
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado '}' ao final do bloco", line));
        } eat();

        if (token.type.equals("KEYWORD") && token.type.equals("step")) {
            eat();
            Node step = parseExpr();

            // return new Node("repeat", step, ident.lexem, init, end, block);
        }

        return new Node("repeat", null, ident.lexem, init, end, block);
    }

    public Node parseExpr() {
        // Faz o parse de uma expressão
        Node leftNode = parseTerm(); // Faz o parse do lado esquerdo do sinal

        while (token.type.equals("SUM_OP") || token.type.equals("SUB_OP")) {
            // Se for um operador + ou -, faz o parse do lado direito
            Token operator = token;
            eat();
            Node rightNode = parseTerm();
            // Cria um novo nó
            leftNode = new Node("operator", operator.lexem, null, leftNode, rightNode, null);
        }
        return leftNode;
    }

    public Node parseTerm() {
        // Faz o parse de um termo
        Node leftNode = parseFactor(); // Faz o parse do lado esquerdo do sinal
        while (token.type.equals("MULT_OP") || token.type.equals("DIV_OP")) {
            // Se for um operador * ou /, faz o parse do lado direito
            Token operator = token;
            eat();
            Node rightNode = parseFactor();
            // Cria um novo nó
            leftNode = new Node("operator", operator.lexem, null, leftNode, rightNode, null);
        }
        return leftNode;
    }

    public Node parseFactor() {
        // Verifica se há um token, se não, lança um erro
        if (token.type.equals("EOF")) {
            throw new RuntimeException(String.format("Erro desconhecido!"));
        }
        int line = token.line; // Armazena a linha atual

        // Verifica se é um operador unario
        if (token.type.equals("SUB_OP")) {
            eat();

            // Verifica se apos o operador vem um inteiro, um float ou uma variavel
            if (token.type.equals("INT_LIT") || token.type.equals("FLOAT") || token.type.equals("IDENT")) {
                Node newNode = parseFactor();
                return new Node("unario", "-", null, null, newNode, null);
            }
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado um numero ou identificador após '-'%n", line));
        }

        // Verifica se é um parentese de abertura
        else if (token.type.equals("LEFT_PAREN")) {
            eat();
            Node newNode = parseExpr(); // Faz o parse da expressão
            // Verifica se o parentese de fechamento foi colocado corretamente
            if (token.type.equals("RIGHT_PAREN")) {
                eat();
                return newNode;
            }
            // Lança um erro se nõ encontrar o parenteses
            throw new RuntimeException(String.format("Erro proximo a linha %d: Esperado ')'%n", line));
        }

        // Verifica se é um inteiro
        else if (token.type.equals("INT_LIT")) {
            Node newNode = new Node("int", token.lexem, null, null, null, null);
            eat();
            return newNode;
        }

        // Verifica se é um caractere
        else if (token.type.equals("CHAR_LIT")) {
            Node newNode = new Node("char", token.lexem, null, null, null, null);
            eat();
            return newNode;
        }

        // Verifica se é um float
        else if (token.type.equals("FLOAT")) {
            Node newNode = new Node("float", token.lexem, null, null, null, null);
            eat();
            return newNode;
        }

        // Verifica se é um booleano
        else if (token.lexem.equals("True") || token.lexem.equals("False")) {
            Node newNode = new Node("bool", token.lexem, null, null, null, null);
            eat();
            return newNode;
        }

        // Verifica se é um ponteiro nulo
        else if (token.lexem.equals("null")) {
            Node newNode = new Node("null", null, null, null, null, null);
            eat();
            return newNode;
        }

        // Verifica se é um identificador
        else if (token.type.equals("IDENT")) {
            Node newNode = new Node("ident", null, token.lexem, null, null, null);
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
