package sci;

public class Token {
    private TokenType t;
    private String contents;
    
    public Token(TokenType token, String s) {
        t = token;
        contents = s;
    }
    
    public TokenType getType() {
        return t;
    }
    
    public String getContents() {
        return contents;
    }
}
