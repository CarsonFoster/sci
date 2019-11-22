package sci.parsing;

class Node {
    private Token t;
    private Node left, right;
    
    public Node(Token tt) {
        t = tt;
    }
    
    public void setLeft(Node l) {
        left = l;
    }
    
    public void setRight(Node r) {
        right = r;
    }
    
    public Node getLeft() {
        return left;
    }
    
    public Node getRight() {
        return right;
    }
    
    public Token getToken() {
        return t;
    }
}

public class AST {
    private Node root;
    
    public AST(Node r) {
        root = r;
    }
    public AST() {}
    
    public void setRoot(Node n) {
        root = n;
    }
    
    public Node getRoot() {
        return root;
    }
}
