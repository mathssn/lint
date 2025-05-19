package src.semantic;

import java.util.HashMap;
import java.util.ArrayList;

import src.utils.Symbol;
import src.ast.Node;
import src.ast.NodeType;


public class ScopeChecker {
    
    public static ArrayList<HashMap<String, Symbol>> symbolTables = new ArrayList<HashMap<String, Symbol>>();

    public static Object scopeChecker(Node node) {
        if (node == null) {
            return null;
        }

        if (node.type == NodeType.INT) {
            return Integer.parseInt(node.valor);
        }
        else if (node.type == NodeType.FLOAT) {
            return Float.parseFloat(node.valor);
        }
        else if (node.type == NodeType.BOOL)  {
            return Boolean.parseBoolean(node.valor);
        }
        else if (node.type == NodeType.CHAR) {
            return node.valor.charAt(1);
        }
        else if (node.type == NodeType.NULL) {
            return null;
        }
        else if (node.type == NodeType.DECL) {
            declareVar(node.nome);
        }
        else if (node.type == NodeType.ATRIB) {
            resolveVar(node.nome);
        }

        else if (node.type == NodeType.FUNC) {
            addScope();
            scopeChecker(node.block);
            // printCurrentScope();
            exitScope();
        }

        return null;
    }

    public static void scopeChecker(ArrayList<Node> block) {
        if (block == null) {
            return;
        }
        for (Node node: block) {
            scopeChecker(node);
        }
    }

    private static void addScope() {
        symbolTables.add(
            new HashMap<String, Symbol>()
        );
    }

    private static void exitScope() {
        symbolTables.remove(symbolTables.size() - 1);
    }

    private static void declareVar(String name) {
        if (symbolTables.size() <= 0) {
            return;
        }

        symbolTables.get(symbolTables.size() - 1).put(
            name,
            new Symbol(null, symbolTables.size() - 1, false, false)
        );
    }

    private static void resolveVar(String name) {
        for (HashMap<String, Symbol> vars: symbolTables) {
            if (vars.containsKey(name)) {
                return;
            }
        }
        throw new RuntimeException(String.format("Variável sendo usada antes da declaração: %s", name));
    }

    private static void printCurrentScope() {
        for (HashMap<String, Symbol> vars: symbolTables) {
            for (String var: vars.keySet()) {
                System.out.println(var);
            }
        }
    }
}
