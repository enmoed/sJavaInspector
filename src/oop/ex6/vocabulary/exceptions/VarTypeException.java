package oop.ex6.vocabulary.exceptions;

import oop.ex6.vocabulary.VariablesTypes;

public class VarTypeException extends VocabularyException {
    public VarTypeException(VariablesTypes vt, String val){
        super(String.format("try to assign %s to %s variable type",vt,val));
    }
    public VarTypeException(VariablesTypes vt, String val, String name){
        super(String.format("try to assign %s to %s which of type %s",vt,name,val));
    }
}
