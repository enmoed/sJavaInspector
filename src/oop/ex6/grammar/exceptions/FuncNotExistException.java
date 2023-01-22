package oop.ex6.grammar.exceptions;

public class FuncNotExistException extends GrammarException {
    public FuncNotExistException(String funcName){
        super(String.format("call %s but this function didn't declared",funcName));
    }
}
