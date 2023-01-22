package oop.ex6.grammar.exceptions;

import oop.ex6.grammar.VariablesTypes;

public class VarTypeException extends GrammarException {
    public VarTypeException(VariablesTypes vt, String val){
        super(String.format("try to assign %s to %s variable type",val, vt));
    }
}
