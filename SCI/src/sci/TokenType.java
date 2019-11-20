package sci;

public enum TokenType {
    DECIMAL("[0-9]+\\.[0-9]+"),
    INTEGER("[0-9]+(?!\\.)"),
    BINARYOP("[\\+/\\*\\-\\^%]"),
    UNARYOP("\\-"),
    PAREN("[\\(\\)]"),
    STRING("[a-zA-Z_]+");
    //WHITESPACE("[ \t]");
    
    public String pattern;
    TokenType(String p) {
        pattern = p;
    }
    
}
