package oop.ex6.grammar.exceptions;

public class SyntaxException extends Exception{
    public SyntaxException(String statement){
        super(String.format("Syntax error: %s",statement));
    }
}
