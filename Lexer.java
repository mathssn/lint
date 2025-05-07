import java.util.ArrayList;


public class Lexer {

    private String program; // Codigo fonte
    private int index = 0; // Index da string do programa
    private int line = 1; // Linha atual
    private int collumn = 1; // Coluna atual
    private String buffer = "";
    public ArrayList<Token> tokens = new ArrayList<Token>(); // Lista de tokens

    public Lexer(String program) {
        this.program = program;
    }

    public void tokenize() {
        while (!peek().isEmpty()) {
            // Verifica se é uma quebra de linha
            if (peek().equals("\n")) {
                eat();
                collumn = 1;
                line++;
                continue;
            }
            // Verifica se é um espaço em branco
            else if (peek().isBlank()) {
                eat();
                continue;
            }
            // Verifica se é um número
            else if (Utils.isDigit(peek())) {
                tokenizeNumbers();
            } 
            // Verifica se é uma palavra
            else if (Utils.isAlphabetic(peek()) || peek().equals("_")) {
                tokenizeWords();
            } 
            // Verifica operadores logicos
            else if (peek().equals(">") || peek().equals("<") || peek().equals("=")) {
                buffer = peek();
                eat();
                if (!peek().isEmpty() && peek().equals("=")) {
                    buffer += peek();
                    eat();
                }
            }
            // Verifica o operador AND
            else if (peek().equals("&")) {
                buffer = peek();
                eat();
                if (!peek().isEmpty() && peek().equals("&")) {
                    buffer += peek();
                    eat();
                } else {
                    System.out.printf("Caractere invalido '&' na linha %d%n", line);
                    continue;
                }
            }
            // Verifica o operador OR
            else if (peek().equals("|")) {
                eat();
                if (!peek().isEmpty() && peek().equals("|")) {
                    buffer = peek() + peek();
                    eat();
                }
                else {
                    System.out.printf("Caractere invalido '|' na linha %d%n", line);
                    continue;
                }
            }
            // Verifica caracteres literais
            else if (peek().equals("'")) {
                buffer = peek();
                eat();
                if (!peek().isEmpty()) {
                    buffer += peek();
                    eat();
                } else {
                    System.out.printf("Caractere literal mal formado %s%n", peek());
                    continue;
                }
                if (!peek().isEmpty() && peek().equals("'")) {
                    buffer += peek();
                    eat();
                } else {
                    System.out.printf("Caractere literal mal formado %s%n", peek());
                    continue;
                }
            }
            // Verifica se é um operador, ponto-e-virgula, dois pontos, parenteses ou chaves
            else {
                switch (peek()) {
                    case "+": case "-": case "/": case "*":
                    case "(": case ")": case "{": case "}":
                    case ":": case ";": case ",":
                        buffer = peek();
                        eat();
                        break;
                    default:
                        System.out.printf("Caractere invalido %s%n", peek());
                        eat();
                        continue;
                }
            }

            // Adiciona um novo token na lista
            Token newToken = new Token(buffer, line, collumn - buffer.length());
            add_token(newToken);
        }
    }

    private String peek() {
        // Retorna o caractere atual
        if (index < program.length()) {
            return program.substring(index, index + 1);
        }
        // Se ja houver passado do index, retorna uma string vazia
        return "";
    }

    private void eat() {
        // Passa para o proximo caractere
        index++;
        collumn++;
    }

    private void tokenizeNumbers() {
        // Enquanto o caractere for um numero e não for vazio, adiciona o caractere no buffer
        int points = 0;
        while ((!peek().isEmpty() && Utils.isDigit(peek())) || peek().equals(".")) {
            buffer += peek();
            eat();
            // Verifica se é um ponto
            if (peek().equals(".") && points == 0) { // Se ainda não possuir nenhum ponto, contabiliza
                points++;
            } else if (peek().equals(".") && points > 0) { // Se ja houver, para
                System.out.printf("Numero de ponto flutuante com mais de um ponto na linha %d%n", line);
                eat();
                break;
            }
        }
    }

    private void tokenizeWords() {
        // Enquanto o caractere for uma letra, numero ou _ e não for vazio, adiciona o caractere no buffer
        while ((!peek().isEmpty() && Utils.isAlphaNum(peek())) || peek().equals("_")) {
            buffer += peek();
            eat();
        }
    }

    private void add_token(Token newToken) {
        // Adiciona um token na lista e reseta o buffer
        tokens.add(newToken);
        buffer = "";
    }
}