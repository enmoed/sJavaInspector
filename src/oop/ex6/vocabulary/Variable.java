package oop.ex6.vocabulary;

import oop.ex6.vocabulary.exceptions.VarTypeException;

public class Variable {
    private VariablesTypes type;
    private String name;
    private boolean isFinal;

    public String getName() {
        return name;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void validateValue(String value) throws VarTypeException {
        try {
            switch (type) {
                case INT: {
                    VariablesTypes.validateInt(value);
                }
                case CHAR: {
                    VariablesTypes.validateChar(value);
                }
                case BOOLEAN: {
                    VariablesTypes.validateBoolean(value);
                }
                case DOUBLE: {
                    VariablesTypes.validateDouble(value);
                }
                case STRING: {
                    break; //always good
                }
            }
        } catch (VarTypeException e) {
            throw new VarTypeException(type, value, name);
        }
    }
}
