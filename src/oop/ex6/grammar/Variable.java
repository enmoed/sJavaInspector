package oop.ex6.grammar;

/**
 * A class to hold variable properties such as it's name and type as well as if has been initialized or
 * declared final
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
