package src.semantic;

import java.util.ArrayList;

import src.ast.*;;

public class TypeChecker {
    
    public ArrayList<Node> ast;

    public TypeChecker(ArrayList<Node> ast) {
        this.ast = ast;
    }

    public String typeChecker(Node node) {
        // Verifica se é um literal
        if (node.type == NodeType.INT) {
            return "int";
        } else if (node.type == NodeType.CHAR) {
            return "char";
        } else if (node.type == NodeType.FLOAT) {
            return "float";
        } else if (node.type == NodeType.CHAR) {
            return "null";
        } else if (node.type == NodeType.NULL) {
            return "null";
        }
        
        // Verifica se é um identificador e retorna seu nome
        else if (node.type == NodeType.IDENT) {
            return node.nome;
        }
        
        // Verifica se é um operador binario
        else if (node.type == NodeType.BINARY_OPERATOR) {
            if (node.valor.equals("+")) {

            }
        }

        // Verifica se é uma função
        else if (node.type == NodeType.FUNC) {
            typeChecker(node.block)
        }
    }
}
