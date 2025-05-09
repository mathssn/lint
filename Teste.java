// import java.io.FileWriter;
// import java.io.IOException;
import java.util.ArrayList;

import src.Lexer;
import src.Parser;
import src.ast.Node;


public class Teste {

    public static void main(String[] args) {
        Lexer lexer = new Lexer("func main() -> int {var x: int = 9;}");
        lexer.tokenize();

        // try {
        //     FileWriter file = new FileWriter("tokens.txt");
        //     for (Token t: lexer.tokens) {
        //         file.write(t.toString() + "\n");
        //     }
        //     file.close();
        // } catch (IOException e) {

        // }

        Parser parser = new Parser(lexer.tokens);
        parser.parseProgram();
        ArrayList<Node> tree = parser.nodes;
        for (Node node: tree) {
            PrintTree.printTree(node, 0);
        }
    }
    
}
