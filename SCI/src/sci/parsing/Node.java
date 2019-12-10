package sci.parsing;

public class Node {
    private int level = 0;
    private Token t;
    private Node left, right;
    boolean isLeft, isRight;
    
    public Node(Token tt) {
        t = tt;
    }
    
    public void setLevel(int i) {
        level = i;
    }
    
    public int getLevel() {
        return level;
    }
    
    public void setLeft(Node l) {
        left = l;
        if (l != null) {
            left.recursiveIncrementLevel();
            left.isLeft = true;
        }
    }
    
    public void setRight(Node r) {
        right = r;
        if (r != null) {
            right.recursiveIncrementLevel();
            right.isRight = true;
        }
    }
    
    public void recursiveIncrementLevel() {
        level += 1;
        if (left != null)
            left.recursiveIncrementLevel();
        if (right != null)
            right.recursiveIncrementLevel();
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
    
    public String toString() {
        String tabs = "";
        for (int i = 0; i < level; i++)
            tabs += "  ";
        String ret = String.format("%s%d (%s): %s%n", tabs, level, (isLeft ? "L" : (isRight ? "R" : "ROOT")), t.toString());
        ret += (left == null ? "" : left.toString());
        ret += (right == null ? "": right.toString());
        return ret;
    }
}
