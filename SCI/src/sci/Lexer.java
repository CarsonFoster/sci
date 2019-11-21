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
    public static ArrayList<Token> lex(String input) {
        ArrayList<Token> tokens = new ArrayList<>();
        
        String pattern = "";
        for (TokenType t :  TokenType.values()) {
            pattern += "|(?<" + t + ">" + t.pattern + ")";
        }
        pattern = pattern.substring(1);
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(input);
        
        while (m.find()) {
            for (TokenType tt : TokenType.values()) {
                String matched = m.group(tt.toString());
                if (matched != null) {
                    tokens.add(new Token(tt, matched));
                }
            }
        }
        
        return tokens;
    }
    
    public static void main(String args[]) {
        //String input = "3.5 + 2 - 3.4 * 0 / 2.0";
        String input = "3^     (4 + usigma list1)"; //TODO: make sure to include numbers in list names.
        
        System.out.println(lex(input));
    }
}
