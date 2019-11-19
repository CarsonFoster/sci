/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sci;

import java.util.ArrayList;
import java.util.regex.*;

/**
 *
 * @author fostecar000
 */
public class Lexer {
    public ArrayList<Token> lex(String input) {
        ArrayList<Token> tokens = new ArrayList<>();
        
        String pattern = "";
        for (TokenType t :  TokenType.values()) {
            pattern += "|(?<" + t + "> " + t.pattern + ")";
        }
        pattern = pattern.substring(1);  
        
        return tokens;
    }
}
