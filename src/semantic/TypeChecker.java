package src.semantic;

import java.util.ArrayList;
import java.util.HashMap;

import src.ast.*;
import src.utils.Symbol;;

public class TypeChecker {

    public static HashMap<String, Symbol> symbolTable;

    public static void setSymbolTable(HashMap<String, Symbol> symbolTb) {
        symbolTable = symbolTb;
    }

    public static String typeChecker(Node node) {
        if (node == null || node.type == null) {
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
            if (symbolTable.get(node.nome) == null) {
                throw new RuntimeException(String.format("Variavel sendo usada antes da declaração: %s", node.nome));
            } else if (!symbolTable.get(node.nome).initialized) {
                throw new RuntimeException(String.format("Variavel sendo usada antes da inicialização: %s", node.nome));
            }
            // System.out.println(node.nome);
            return symbolTable.get(node.nome).type;
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

        else if (node.type == NodeType.DECL) {
            if (symbolTable.get(node.nome) == null) {
                String right = typeChecker(node.right);
                if (right.equals("") || right.equals("null")) {
                    throw new RuntimeException(String.format("Declaração de variavel invalida: %s", node.nome));
                }
                
                // symbolTable.put(node.nome, new Symbol(right, null, true, false));
                return right;
            }

            if (!symbolTable.get(node.nome).initialized) {
                return symbolTable.get(node.nome).type;
            }

            String right = typeChecker(node.right);

            if (!right.equals(symbolTable.get(node.nome).type)) {
                if (right.equals("int") && symbolTable.get(node.nome).type.equals("float")) {    
                    return "float";
                }
                throw new RuntimeException(String.format("Declaração invalida: variavel do tipo %s não pode receber valores do tipo %s", symbolTable.get(node.nome).type, right));
            }

            return right;
        }

        else if (node.type == NodeType.ATRIB) {
            if (!symbolTable.keySet().contains(node.nome)) {
                throw new RuntimeException(String.format("Atribuição a variavel não declarada %s", node.nome));
            }

            String right = typeChecker(node.right);

            if (!right.equals(symbolTable.get(node.nome).type)) {
                if (right.equals("int") && symbolTable.get(node.nome).type.equals("float")) {    
                    return "float";
                }
                throw new RuntimeException(String.format("Atribuição invalida: variavel do tipo %s não pode receber valores do tipo %s", symbolTable.get(node.nome).type, right));
            }
        }

        else if (node.type == NodeType.REPEAT) {
            
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
