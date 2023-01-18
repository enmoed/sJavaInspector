package oop.ex6.vocabulary;

import oop.ex6.vocabulary.exceptions.VarTypeException;

public enum VariablesTypes {
    INT,
    DOUBLE,
    BOOLEAN,
    STRING,
    CHAR;
    public static void validateInt(String s) throws VarTypeException {
        try{Integer.parseInt(s);}
        catch(NumberFormatException e){throw new VarTypeException(INT,s);}
    }
    public static void validateChar(String s) throws VarTypeException {
        if(s.length()!=1){
            throw new VarTypeException(CHAR,s);
        }
    }
    public static void validateDouble(String s) throws VarTypeException{
        try{Double.parseDouble(s);}
        catch(NumberFormatException e){throw new VarTypeException(INT,s);}
    }
    public static void validateBoolean(String s) throws VarTypeException {
        if(s.equals("true")||s.equals("false")){
            return;
        }
        try{validateInt(s);}
        catch(VarTypeException e){
            try{validateDouble(s);}
            catch (VarTypeException e2){
                throw new VarTypeException(BOOLEAN,s);
            }
        }
    }
}
