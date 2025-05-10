import java.util.ArrayList;

import src.ast.Node;
import src.ast.NodeType;

public class PrintTree {

    public static void printTree(Node node, int nivel) {
        if (node == null) {
            return;
        }

        if (node.type == NodeType.BINARY_OPERATOR) {
            printSpaces(nivel);
            System.out.printf("Operador: %s%n", node.valor);
            printTree(node.left, nivel+4);
            printTree(node.right, nivel+4);
        } else if (node.type == NodeType.INT || node.type == NodeType.BOOL || node.type == NodeType.CHAR || node.type == NodeType.FLOAT) {
            printSpaces(nivel);
            System.out.printf("Valor: %s%n", node.valor);
        } else if (node.type == NodeType.NULL) {
            printSpaces(nivel);
            System.out.println("Valor: null");
        }
        else if (node.type == NodeType.IDENT) {
            printSpaces(nivel);
            System.out.printf("Variavel: %s%n", node.nome);
        } else if (node.type == NodeType.UNARY_OPERATOR) {
            printSpaces(nivel);
            System.out.printf("Operador: %s%n", node.valor);
            printTree(node.right, nivel);
        } else if (node.type == NodeType.DECL) {
            printSpaces(nivel);
            System.out.printf("Declaração: %s%n", node.nome);
            printTree(node.right, nivel+4);
        } else if (node.type == NodeType.ATRIB) {
            printSpaces(nivel);
            System.out.printf("Atribuição: %s%n", node.nome);
            printTree(node.right, nivel+4);
        } else if (node.type == NodeType.CONDITIONAL) {
            printSpaces(nivel);
            System.out.println("Condicional:");
            printTree(node.left, nivel+4);
            printTree(node.block, nivel+4);
        } else if (node.type == NodeType.LOGIC_OPERATOR) {
            printSpaces(nivel);
            System.out.printf("Expressão: %s%n", node.valor);
            printTree(node.left, nivel+4);
            printTree(node.right, nivel+4);
        } else if (node.type == NodeType.REPEAT) {
            printSpaces(nivel);
            System.out.println("Repetição Controlada:");
            printSpaces(nivel+4);
            System.out.printf("Controle: %s%n", node.nome);
            printSpaces(nivel+4);
            System.out.println("Intervalo:");
            printTree(node.left, nivel+8);
            printTree(node.right, nivel+8);
            printTree(node.block, nivel+4);
        } else if (node.type == NodeType.STEP) {
            printTree(node.left, nivel);
            printTree(node.right, nivel);
        } else if (node.type == NodeType.WHILE) {
            printSpaces(nivel);
            System.out.println("Repetição condicional:");
            printTree(node.left, nivel+4);
            printTree(node.block, nivel+4);
        } else if (node.type == NodeType.RETURN) {
            printSpaces(nivel);
            System.out.println("Comando: return");
            printTree(node.left, nivel+4);
        } else if (node.type == NodeType.STOP) {
            printSpaces(nivel);
            System.out.println("Comando: stop");
        } else if (node.type == NodeType.SKIP) {
            printSpaces(nivel);
            System.out.println("Comando: skip");
        } else if (node.type == NodeType.FUNC) {
            printSpaces(nivel);
            System.out.printf("Função: %s%n", node.nome);
            printTree(node.left, nivel+4);
            printTree(node.block, nivel+4);
        } else if (node.type == NodeType.PARAMS) {
            printSpaces(nivel);
            System.out.println("Parametros:");
            printTree(node.block, nivel+4);
        } else if (node.type == NodeType.PARAM) {
            printSpaces(nivel);
            System.out.printf("Nome: %s%n", node.nome);
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
