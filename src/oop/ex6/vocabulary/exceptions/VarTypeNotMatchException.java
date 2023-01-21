package oop.ex6.vocabulary.exceptions;

import oop.ex6.vocabulary.VariablesTypes;

public class VarTypeNotMatchException extends VocabularyException{
    public VarTypeNotMatchException(String varName){
        super(String.format("variable %s type didn't match",varName));
    }
    public VarTypeNotMatchException(VariablesTypes assign, VariablesTypes assigned){
        super(String.format("can't assign %s to %s", assign.toString(), assigned.toString()));
    }
}
