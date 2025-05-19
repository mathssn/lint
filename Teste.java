import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import src.Lexer;
import src.Token;
import src.Parser;
import src.ast.Node;
import src.semantic.*;

public class Teste {

    public static void main(String[] args) {

        String filePath = "teste.lt";
        StringBuilder file = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String l = line.replace("\t", "    ");
                file.append(l).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // for (char c: file.toString().toCharArray()) {
        //     if (c != '\n') {
        //         System.out.println(c);
        //     } else {
        //         System.out.println((int) c);
        //     }
        // }

        Lexer lexer = new Lexer(file.toString());
        lexer.tokenize();

        try {
            FileWriter file2 = new FileWriter("tokens.txt");
            for (Token t: lexer.tokens) {
                file2.write(t.toString() + "\n");
            }
            file2.close();
        } catch (IOException e) {

        }

        Parser parser = new Parser(lexer.tokens);
        parser.parseProgram();
        ArrayList<Node> tree = parser.nodes;
        for (Node node: tree) {
            PrintTree.printTree(node, 0);
        }

        // ScopeChecker.scopeChecker(tree);
    }

}

