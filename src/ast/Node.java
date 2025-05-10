package src.ast;
import java.util.ArrayList;


public class Node {

    public String nome;
    public String valor;
    public NodeType type;
    public Node left;
    public Node right;
    public ArrayList<Node> block;

    public Node(NodeType type, String valor, String nome, Node left, Node right, ArrayList<Node> block) {
        this.nome = nome;
        this.valor = valor;
        this.type = type;
        this.left = left;
        this.right = right;
        this.block = block;
    }
}
