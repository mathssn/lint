import java.util.ArrayList;

import src.ast.Node;
import src.ast.NodeType;

public class PrintTree {

    public static void printTree(Node node, int nivel) {
        if (node == null) {
            return;
        }

        if (node.tipo == NodeType.BINARY_OPERATOR) {
            printSpaces(nivel);
            System.out.printf("Operador: %s%n", node.valor);
            printTree(node.left, nivel+4);
            printTree(node.right, nivel+4);
        } else if (node.tipo == NodeType.INT || node.tipo == NodeType.BOOL || node.tipo == NodeType.CHAR || node.tipo == NodeType.FLOAT) {
            printSpaces(nivel);
            System.out.printf("Valor: %s%n", node.valor);
        } else if (node.tipo == NodeType.NULL) {
            printSpaces(nivel);
            System.out.println("Valor: null");
        }
        else if (node.tipo == NodeType.IDENT) {
            printSpaces(nivel);
            System.out.printf("Variavel: %s%n", node.nome);
        } else if (node.tipo == NodeType.UNARY_OPERATOR) {
            printSpaces(nivel);
            System.out.printf("Operador: %s%n", node.valor);
            printTree(node.right, nivel);
        } else if (node.tipo == NodeType.DECL) {
            printSpaces(nivel);
            System.out.printf("Declaração: %s%n", node.nome);
            printTree(node.right, nivel+4);
        } else if (node.tipo == NodeType.ATRIB) {
            printSpaces(nivel);
            System.out.printf("Atribuição: %s%n", node.nome);
            printTree(node.right, nivel+4);
        } else if (node.tipo == NodeType.CONDITIONAL) {
            printSpaces(nivel);
            System.out.println("Condicional:");
            printTree(node.left, nivel+4);
            printTree(node.block, nivel+4);
        } else if (node.tipo == NodeType.LOGIC_OPERATOR) {
            printSpaces(nivel);
            System.out.printf("Expressão: %s%n", node.valor);
            printTree(node.left, nivel+4);
            printTree(node.right, nivel+4);
        } else if (node.tipo == NodeType.REPEAT) {
            printSpaces(nivel);
            System.out.println("Repetição Controlada:");
            printSpaces(nivel+4);
            System.out.printf("Controle: %s%n", node.nome);
            printSpaces(nivel+4);
            System.out.println("Intervalo:");
            printTree(node.left, nivel+8);
            printTree(node.right, nivel+8);
            printTree(node.block, nivel+4);
        } else if (node.tipo == NodeType.STEP) {
            printTree(node.left, nivel);
            printTree(node.right, nivel);
        } else if (node.tipo == NodeType.WHILE) {
            printSpaces(nivel);
            System.out.println("Repetição condicional:");
            printTree(node.left, nivel+4);
            printTree(node.block, nivel+4);
        } else if (node.tipo == NodeType.RETURN) {
            printSpaces(nivel);
            System.out.println("Comando: return");
            printTree(node.left, nivel+4);
        } else if (node.tipo == NodeType.STOP) {
            printSpaces(nivel);
            System.out.println("Comando: stop");
        } else if (node.tipo == NodeType.SKIP) {
            printSpaces(nivel);
            System.out.println("Comando: skip");
        } else if (node.tipo == NodeType.FUNC) {
            printSpaces(nivel);
            System.out.printf("Função: %s%n", node.nome);
            printTree(node.left, nivel+4);
            printTree(node.block, nivel+4);
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
