package oop.ex6.grammar.exceptions;

public class VarNotExistException extends GrammarException {
    public VarNotExistException(String varName){
        super(String.format("variable %s didn't declared",varName));
    }
}
