/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sci.parsing;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;

/**
 *
 * @author fostecar000
 */
public class Lexer {
    
    static String removeSpaces(String input) {
        ArrayList<Integer> indices = new ArrayList<>();
        boolean ok = true;
        for (int i = 0; i < input.length(); i++) {
            char t = input.charAt(i);
            if (t == '<') ok = false;
            else if (t == '>') ok = true;
            else if ((t == ' ' || t == '\t') && ok) indices.add(i - indices.size());
        }
        for (int i : indices) {
            input = input.substring(0, (i != 0 ? i : 0)) + input.substring(i + 1);
        }
        
        return input;
    }
    
    public static ArrayList<Token> lex(String input) {
        ArrayList<Token> tokens = new ArrayList<>();
        //input = removeSpaces(input);
        input = input.replaceAll("\t", " ");
        input = input.replaceAll(" {2,}", " ");
        
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
    
    public static int tokenLength(List<Token> x) {
        return x.stream()
                .mapToInt((Token t) -> t.getContents().length())
                .sum();
    }
    
    /*public static void removeWhitespace(List<Token> x) {
        for (int i = 0; i < x.size();) {
            if (x.get(i).getType() == TokenType.WHITESPACE)
                x.remove(i);
            else
                i++;
        }
    }*/
    
    public static void main(String args[]) {
        //String input = "3.5 + 2 - 3.4 * 0 / 2.0";
        String input = "3 ^ (4.0 + <usigma list1>)"; //TODO: make sure to include numbers in list names.
        
        System.out.println(removeSpaces(input));
        ArrayList<Token> t = lex(input);
        System.out.println(t);
        System.out.println(Lexer.tokenLength(t) + " " + input.length());
        //Lexer.removeWhitespace(t);
        System.out.println(t);
    }
}
