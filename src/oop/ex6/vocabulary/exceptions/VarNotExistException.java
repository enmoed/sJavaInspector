package oop.ex6.vocabulary.exceptions;

public class VarNotExistException extends VocabularyException{
    public VarNotExistException(String varName){
        super(String.format("variable %s didn't declared",varName));
    }
}
