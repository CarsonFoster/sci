package sci.parsing;

public class Node {
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
