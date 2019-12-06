package sci.parsing;

import java.util.ArrayList;
import java.util.List;

/* Grammar:
expression = expression BINARY_OP expression | UNARY_OP expression | LPAREN expression RPAREN | number | command
number = integer | decimal
integer = [0-9]+(?!\.)
decimal = [0-9]+\.[0-9]+
command = [STRING, number]+
*/


// Operators: (BI) +-*/%^ (U)-
// Precedence: Paren;-(u);^;*/%;+-

/* Grammar 2.0
unit = number | command
unary_group = unit | unary number | unary lparen expression rparen
factor = unary_group | lparen expression rparen
exponent_group = factor | factor ^ exponent_group
term = exponent_group | term [/%*] exponent_group
expression = term | expression [+-] term
*/

/*
replace + and – with ))+(( and ))-((, respectively;
replace * and / with )*( and )/(, respectively;
add (( at the beginning of each expression and after each left parenthesis in the original expression; and
add )) at the end of the expression and before each right parenthesis in the original expression.
*/

// 3 + 4 * 6
    // 3))+((4*6
    // 3))+((4)*(6
    // ((3))+((4)*(6))
// (3 + 4) * 6
    // (3))+((4)*6
    // (3))+((4))*(6
    // (((((3))+((4))*(6
    // (((((3))+((4))))*(6))


public class Parser {
    private List<Token> tokens;
    private int index = 0;
    
    /*
    public static void main(String[] args) {
        String test = "(3+4)*6";
        ArrayList<Token> start = Lexer.lex(test);
        start.forEach(x -> System.out.print(x.getContents()));
        System.out.println("");
        tokens = start;
        ArrayList<Token> end = parenthesize();
        end.forEach(x -> System.out.print(x.getContents()));
        //3 + 4 * 6: ((((3)))+(((4))*((6))))
        //(3 + 4) * 6: ((((((((3)))+(((4))))))*((6))))
    }*/
    
    public Parser(List<Token> t) {
        tokens = t;
    }
    
    private boolean test(TokenType tt) {
        if (tokens.get(index).getType() == tt) {
            index++;
            return true;
        }
        return false;
    }
    
    private boolean safeTest(String content, int i) {
        return (tokens.size() > index + i && tokens.get(index + i).getContents().equals(content));
    }
    
    private boolean safeTest(TokenType tt, int i) {
        return (tokens.size() > index + i && tokens.get(index + i).getType() == tt);
    }
    
    private void addParen(List<Token> t, int n, boolean left) {
        for (int i = 0; i < n; i++)
            t.add(new Token(left ? TokenType.LPAREN : TokenType.RPAREN, left ? "(" : ")"));
    }
    
    private ArrayList<Token> parenthesize() {
        ArrayList<Token> ret = new ArrayList<>();
        addParen(ret, 4, true);
        
        for (int i = 0; i < tokens.size(); i++) {
            TokenType tt = tokens.get(i).getType();
            String content = tokens.get(i).getContents();
            if (tt == TokenType.LPAREN)
                addParen(ret, 4, true);
            else if (tt == TokenType.RPAREN)
                addParen(ret, 4, false);
            else if (tt == TokenType.BINARYOP) {
                if (content.equals("^")) {
                    addParen(ret, 1, false);
                    ret.add(tokens.get(i));
                    addParen(ret, 1, true);
                } else if ("*/%".contains(content)) {
                    addParen(ret, 2, false);
                    ret.add(tokens.get(i));
                    addParen(ret, 2, true);
                } else if ("+-".contains(content)) {
                    addParen(ret, 3, false);
                    ret.add(tokens.get(i));
                    addParen(ret, 3, true);
                }
            } else {
                ret.add(tokens.get(i));
            }
        }
        
        addParen(ret, 4, false);
        
        return ret;
    }
    
    public Node parseExpression() throws SyntaxException {
        return null;
    }
    
    private Node parseParentheticalExpression(int start, int end) { // inclusive, exclusive, including parentheses: (, (, 1, ), +, (, 2, ), ) referring to (1) would be 1, 4
        int lcount = 0, rcount = 0, lstart = -1, rend = -1;
        for (int i = start + 1; i < end - 1; i++) { // disregarding outer parens
            Token u = tokens.get(i);
            TokenType tt = u.getType();
            String content = u.getContents();
            
            Node left = null, middle = null, right = null;
            if (tt == TokenType.LPAREN) {
                lcount++;
                if (lcount == 1) lstart = i;
            } else if (tt == TokenType.RPAREN) {
                rcount++;
                if (rcount == lcount) {
                    rend = i;
                    Node temp = parseParentheticalExpression(lstart, rend);
                    if (left == null)
                        left = temp;
                    else if (middle == null)
                        middle = temp;
                    else
                        right = temp;
                    lcount = 0;
                    rcount = 0;
                    lstart = -1;
                    rend = -1;
                }
            } else if (lcount == 0) {
                
            }
        }
        return null;
    }
    
    private Node parseCommand() {
        Node lastParsed;
        ArrayList<Node> list = new ArrayList<>();
        do {
            lastParsed = parseStringOrNumber();
            if (lastParsed != null)
                list.add(lastParsed);
        } while (lastParsed != null);
        if (list.isEmpty())
            return null;
        Node base = list.get(0);
        for (int i = 0; i < list.size() - 1; i++) {
            list.get(i).setLeft(list.get(i + 1));
        }
        return base;
    }
    
    private Node parseStringOrNumber() {
        Node number = parseNumber();
        if (number != null)
            return number;
        Node string = parseType(TokenType.STRING);
        if (string != null)
            return string;
        return null;
    }
    
    private Node parseNumber() {
        Node integer = parseType(TokenType.INTEGER);
        if (integer != null)
            return integer;
        Node decimal = parseType(TokenType.DECIMAL);
        if (decimal != null)
            return decimal;
        return null;
    }
    
    private Node parseType(TokenType tt) {
        if (test(tt))
            return new Node(tokens.get(index - 1));
        return null;
    }
}
