package src.semantic;

import java.util.ArrayList;

import src.ast.*;;

public class TypeChecker {

    public static String typeChecker(Node node) {
        if (node.type == null) {
            return null;
        }
        // Verifica se é um literal
        if (node.type == NodeType.INT) {
            return "int";
        } else if (node.type == NodeType.CHAR) {
            return "char";
        } else if (node.type == NodeType.FLOAT) {
            return "float";
        } else if (node.type == NodeType.NULL) {
            return "null";
        } else if (node.type == NodeType.BOOL) {
            return "bool";
        }
        
        // Verifica se é um identificador e retorna seu nome
        else if (node.type == NodeType.IDENT) {
            return node.nome;
        }
        
        // Verifica se é um operador binario
        else if (node.type == NodeType.BINARY_OPERATOR) {
            String left = typeChecker(node.left);
            String right = typeChecker(node.right);
            
            if (left.equals("float") && right.equals("int") || left.equals("int") && right.equals("float") || left.equals("float") && right.equals("float")) {
                System.out.println("Passou");
                return "float";
            } else if (left.equals("char") && right.equals("char")) {
                return "char";
            } else if (left.equals("int") && right.equals("int")) {
                return "int";
            } else if (left.equals("bool") && right.equals("bool")) {
                return "bool";
            } else {
                throw new RuntimeException(String.format("Operação entre tipos não permitida: %s %s %s", left, node.valor, right));
            }
        }

        // Verifica se é uma função
        else if (node.type == NodeType.FUNC) {
            typeChecker(node.block);
            return node.nome;
        }

        return "";
    }

    public static void typeChecker(ArrayList<Node> block ) {
        for (Node node: block) {
            typeChecker(node);
        }
    }
}
