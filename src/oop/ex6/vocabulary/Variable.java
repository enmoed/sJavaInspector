package oop.ex6.vocabulary;

import oop.ex6.vocabulary.exceptions.VarTypeException;

/**
 * variable object for contain all the variable information.
 */
public class Variable {
    private VariablesTypes type;
    private String name;
    private boolean isFinal;
    private boolean isInit;

    public Variable(String name, VariablesTypes type) {
        this.name = name;
        this.type = type;
        this.isFinal = false;
        this.isInit = false;
    }

    public Variable(String name, VariablesTypes type, Boolean isFinal) {
        this.name = name;
        this.type = type;
        this.isFinal = isFinal;
        this.isInit = false;
    }

    public String getName() {
        return name;
    }

    public VariablesTypes getType() {
        return type;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public boolean isInit() {
        return isInit;
    }

    public void hasAssign() {
        isInit = true;
    }

    public void resetAssign() {
        isInit = false;
    }
}
