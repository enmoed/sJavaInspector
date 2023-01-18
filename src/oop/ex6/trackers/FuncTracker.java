package oop.ex6.trackers;

import oop.ex6.vocabulary.Function;
import oop.ex6.vocabulary.VariablesTypes;
import oop.ex6.vocabulary.exceptions.FuncNotExistException;

import java.util.Hashtable;
import java.util.SortedMap;

public class FuncTracker {
    private static Hashtable<String, Function> funcDict = new Hashtable<>();

    public static void validateFuncExist(String funcName) throws FuncNotExistException {
        if (!funcDict.containsKey(funcName)) {
            throw new FuncNotExistException(funcName);
        }
    }

    public static SortedMap<String, VariablesTypes> getFuncArgs(String funcName) throws FuncNotExistException {
        validateFuncExist(funcName);
        return funcDict.get(funcName).getParams();
    }

    public static void addFunc(Function f) {
        funcDict.put(f.getName(), f);
    }
}
