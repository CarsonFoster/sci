package sci.parsing;

public enum TokenType {
    // order doesn't matter
    UNARYOP("(^|[\\(\\+/\\*\\-\\^%])\\-"),      // - (must be at start of expression, after open paren, after operator)
    BINARYOP("[\\+/\\*\\-\\^%]"), // +/*-^%
    LPAREN("\\("), // (
    RPAREN("\\)"), // )
    // order does matter, assume decimal before string and then string before integer
    DECIMAL("[0-9]+\\.[0-9]+"),
    STRING("[a-zA-Z_][a-zA-Z0-9_]*"),
    INTEGER("[0-9]+(?!\\.)");
    //WHITESPACE(" ");
    
    public String pattern;
    TokenType(String p) {
        pattern = p;
    }
    
}
