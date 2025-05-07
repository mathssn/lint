import java.util.ArrayList;

public class PrintTree {

    public static void printTree(Node node, int nivel) {
        if (node == null) {
            return;
        }

        if (node.tipo.equals("operator")) {
            printSpaces(nivel);
            System.out.printf("Operador: %s%n", node.valor);
            printTree(node.left, nivel+4);
            printTree(node.right, nivel+4);
        } else if (node.tipo.equals("int") || node.tipo.equals("bool") || node.tipo.equals("char") || node.tipo.equals("float")) {
            printSpaces(nivel);
            System.out.printf("Valor: %s%n", node.valor);
        } else if (node.tipo.equals("null")) {
            printSpaces(nivel);
            System.out.println("Valor: null");
        }
        else if (node.tipo.equals("ident")) {
            printSpaces(nivel);
            System.out.printf("Variavel: %s%n", node.nome);
        } else if (node.tipo.equals("unario")) {
            printSpaces(nivel);
            System.out.printf("Operador: %s%n", node.valor);
            printTree(node.right, nivel);
        } else if (node.tipo.equals("decl")) {
            printSpaces(nivel);
            System.out.printf("Declaração: %s%n", node.nome);
            printTree(node.right, nivel+4);
        } else if (node.tipo.equals("atrib")) {
            printSpaces(nivel);
            System.out.printf("Atribuição: %s%n", node.nome);
            printTree(node.right, nivel+4);
        } else if (node.tipo.equals("conditional")) {
            printSpaces(nivel);
            System.out.println("Condicional:");
            printTree(node.left, nivel+4);
            printTree(node.block, nivel+4);
        } else if (node.tipo.equals("logic_operator")) {
            printSpaces(nivel);
            System.out.printf("Expressão: %s%n", node.valor);
            printTree(node.left, nivel+4);
            printTree(node.right, nivel+4);
        }
    }

    public static void printTree(ArrayList<Node> block, int nivel) {
        if (block == null) {
            return;
        }

        for (Node statement: block) {
            printTree(statement, nivel);
        }
    }

    public static void printSpaces(int qntd) {
        for (int i = 0; i < qntd; i++) {
            System.out.print(" ");
        }
    }
}
