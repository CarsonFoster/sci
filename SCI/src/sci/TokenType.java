package sci;

public enum TokenType {
    INTEGER("[0-9]+(?!\\.)"),
    DECIMAL("[0-9]+\\.[0-9]+"),
    BINARYOP("[\\+/\\*\\-\\^%]");
    //WHITESPACE("[ \t]");
    
    public String pattern;
    TokenType(String p) {
        pattern = p;
    }
    
}
