package oop.ex6.grammar.exceptions;

import oop.ex6.grammar.VariablesTypes;

public class VarTypeNotMatchException extends GrammarException {
    public VarTypeNotMatchException(VariablesTypes assign, VariablesTypes assigned){
        super(String.format("can't assign %s to %s", assign.toString(), assigned.toString()));
    }
}
