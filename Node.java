import java.util.ArrayList;


public class Node {

    public String nome;
    public String valor;
    public String tipo;
    public Node left;
    public Node right;
    public ArrayList<Node> block;

    public Node(String tipo, String valor, String nome, Node left, Node right, ArrayList<Node> block) {
        this.nome = nome;
        this.valor = valor;
        this.tipo = tipo;
        this.left = left;
        this.right = right;
        this.block = block;
    }
}

/*
 * Atribuição: atrib
 * Declaração: decl
 * Operador: operator
 * 
 */
