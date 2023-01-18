package oop.ex6.trackers;

import oop.ex6.vocabulary.Function;
import oop.ex6.vocabulary.VariablesTypes;
import oop.ex6.vocabulary.exceptions.FuncNotExistException;

import java.util.Hashtable;
import java.util.SortedMap;

public class FuncTracker {
    private Hashtable<String, Function> funcDict;

    public void validateFuncExist(String funcName) throws FuncNotExistException {
        if (!funcDict.containsKey(funcName)) {
            throw new FuncNotExistException(funcName);
        }
    }

    public SortedMap<String, VariablesTypes> getFuncArgs(String funcName) throws FuncNotExistException {
        if (!funcDict.containsKey(funcName)) {
            throw new FuncNotExistException(funcName);
        }
        return funcDict.get(funcName).getParams();
    }
}
