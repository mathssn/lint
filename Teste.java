import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class Teste {

    public static void main(String[] args) {
        Lexer lexer = new Lexer("repeat (var x; 0 to 5) {if (x == 4) {var y: int = 8;}}");
        lexer.tokenize();

        try {
            FileWriter file = new FileWriter("tokens.txt");
            for (Token t: lexer.tokens) {
                file.write(t.toString() + "\n");
            }
            file.close();
        } catch (IOException e) {

        }

        Parser parser = new Parser(lexer.tokens);
        parser.parseStmt();
        ArrayList<Node> tree = parser.nodes;
        for (Node node: tree) {
            PrintTree.printTree(node, 0);
        }
    }
    
}
