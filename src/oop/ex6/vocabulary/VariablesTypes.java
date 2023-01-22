package oop.ex6.vocabulary;

import oop.ex6.vocabulary.exceptions.VarTypeException;
import oop.ex6.vocabulary.exceptions.VarTypeNotMatchException;

import java.util.Hashtable;

/**
 * all the variables types.
 * also has static methods for comparison between types.
 */
public enum VariablesTypes {
    INT,
    DOUBLE,
    BOOLEAN,
    STRING,
    CHAR;

    public static Hashtable<String, VariablesTypes> stringMapper = new Hashtable<>() {
        {
            put("int", INT);
            put("double", DOUBLE);
            put("boolean", BOOLEAN);
            put("String", STRING);
            put("char", CHAR);
        }
    };

    public static void validateConstType(VariablesTypes type, String value) throws VarTypeException {
        try {
            switch (type) {
                case INT: {
                    VariablesTypes.validateInt(value);
                    break;
                }
                case CHAR: {
                    VariablesTypes.validateChar(value);
                    break;
                }
                case BOOLEAN: {
                    VariablesTypes.validateBoolean(value);
                    break;
                }
                case DOUBLE: {
                    VariablesTypes.validateDouble(value);
                    break;
                }
                case STRING: {
                    if(!(value.charAt(0)=='"'&&value.charAt(value.length()-1)=='"')){
                        throw new VarTypeException(VariablesTypes.STRING,value);
                    }
                }
            }
        } catch (VarTypeException e) {
            throw new VarTypeException(type, value);
        }
    }

    public static void validateInt(String s) throws VarTypeException {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            throw new VarTypeException(INT, s);
        }
    }

    public static void validateChar(String s) throws VarTypeException {
        if (!(s.startsWith("'") && s.endsWith("'") && s.length() == 3)) {
            throw new VarTypeException(CHAR, s);
        }
    }

    public static void validateDouble(String s) throws VarTypeException {
        try {
            Double.parseDouble(s);
        } catch (NumberFormatException e) {
            throw new VarTypeException(INT, s);
        }
    }

    public static void validateBoolean(String s) throws VarTypeException {
        if (s.equals("true") || s.equals("false")) {
            return;
        }
        try {
            validateInt(s);
        } catch (VarTypeException e) {
            try {
                validateDouble(s);
            } catch (VarTypeException e2) {
                throw new VarTypeException(BOOLEAN, s);
            }
        }
    }

    public static void validateVarType(VariablesTypes assigned, VariablesTypes assign, boolean withCasting)
            throws VarTypeNotMatchException {
        if (assigned.equals(assign)) {
            return;
        }
        if (withCasting) {
            switch (assigned) {
                case BOOLEAN: {
                    if (assign.equals(INT) || assign.equals(DOUBLE)) {
                        return;
                    }
                    break;
                }
                case DOUBLE: {
                    if (assign.equals(INT)) {
                        return;
                    }
                    break;
                }
            }
        }
        throw new VarTypeNotMatchException(assign, assigned);
    }
}
