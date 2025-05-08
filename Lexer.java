import java.util.ArrayList;


public class Lexer {

    private char[] program; // Codigo fonte
    private int index = 0; // Index da string do programa
    private int line = 1; // Linha atual
    private int collumn = 1; // Coluna atual
    private String buffer = "";
    private char c; // Guarda o caractere atual
    public ArrayList<Token> tokens = new ArrayList<Token>(); // Lista de tokens

    public Lexer(String program) {
        this.program = program.toCharArray();
        c = this.program[index];
    }

    public void tokenize() {
        while (c != '\0') {
            // Verifica se é uma quebra de linha
            if (c == '\n') {
                eat();
                collumn = 1;
                line++;
                continue;
            }
            // Verifica se é um espaço em branco
            else if (c == ' ') {
                eat();
                continue;
            }
            // Verifica se é um número
            else if (Character.isDigit(c)) {
                tokenizeNumbers();
            } 
            // Verifica se é uma palavra
            else if (Character.isAlphabetic(c) || c == '_') {
                tokenizeWords();
            } 
            // Verifica operadores logicos
            else if (c == '>' || c == '<' || c == '=') {
                buffer = Character.toString(c);
                eat();
                if (c == '=') {
                    buffer += c;
                    eat();
                }
            }
            // Verifica o operador AND
            else if (c == '&') {
                buffer = Character.toString(c);
                eat();
                if (c == '&') {
                    buffer += c;
                    eat();
                } else {
                    System.out.printf("Caractere invalido '&' na linha %d%n", line);
                    continue;
                }
            }
            // Verifica o operador OR
            else if (c == '|') {
                buffer = Character.toString(c);
                eat();
                if (c == '|') {
                    buffer += c;
                    eat();
                }
                else {
                    System.out.printf("Caractere invalido '|' na linha %d%n", line);
                    continue;
                }
            }
            // Verifica caracteres literais
            else if (c == '\'') {
                buffer = Character.toString(c);
                eat();
                if (c != '\0' && c != '\'') {
                    buffer += c;
                    eat();
                } else {
                    System.out.printf("Caractere literal mal formado %s%n", c);
                    continue;
                }
                if (c == '\'') {
                    buffer += c;
                    eat();
                } else {
                    System.out.printf("Caractere literal mal formado %s%n", c);
                    continue;
                }
            }
            // Verifica se é um operador, ponto-e-virgula, dois pontos, parenteses ou chaves
            else {
                switch (c) {
                    case '+': case '-': case '/': case '*':
                    case '(': case ')': case '{': case '}':
                    case ':': case ';': case ',':
                        buffer = Character.toString(c);
                        eat();
                        break;
                    default:
                        System.out.printf("Caractere invalido %s%n", c);
                        eat();
                        continue;
                }
            }

            // Adiciona um novo token na lista
            Token newToken = new Token(buffer, line, collumn - buffer.length());
            addToken(newToken);
        }
    }

    private char peek() {
        // Retorna o caractere atual
        if (index < program.length) {
            return program[index];
        }
        // Se ja houver passado do index, retorna um caractere de fim de linha
        return '\0';
    }

    private void eat() {
        // Passa para o proximo caractere
        index++;
        collumn++;
        c = peek();
    }

    private void tokenizeNumbers() {
        // Enquanto o caractere for um numero e não for vazio, adiciona o caractere no buffer
        int points = 0;
        while ((c != '\0' && Character.isDigit(c)) || c == '.') {
            buffer += c;
            eat();
            // Verifica se é um ponto
            if (c == '.' && points == 0) { // Se ainda não possuir nenhum ponto, contabiliza
                points++;
            } else if (c == '.' && points > 0) { // Se ja houver, para
                System.out.printf("Numero de ponto flutuante com mais de um ponto na linha %d%n", line);
                eat();
                break;
            }
        }
    }

    private void tokenizeWords() {
        // Enquanto o caractere for uma letra, numero ou _ e não for vazio, adiciona o caractere no buffer
        while ((c != '\0' && Utils.isAlphaNum(c)) || c == '_') {
            buffer += c;
            eat();
        }
    }

    private void addToken(Token newToken) {
        // Adiciona um token na lista e reseta o buffer
        tokens.add(newToken);
        buffer = "";
    }
}