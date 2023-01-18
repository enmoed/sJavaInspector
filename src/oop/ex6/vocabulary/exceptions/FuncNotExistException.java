package oop.ex6.vocabulary.exceptions;

public class FuncNotExistException extends Exception{
    public FuncNotExistException(String funcName){
        super(String.format("call %s but this function didn't declared",funcName));
    }
}
