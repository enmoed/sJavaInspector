package oop.ex6.trackers;

import oop.ex6.grammar.Function;
import oop.ex6.grammar.Variable;
import oop.ex6.grammar.exceptions.FuncNotExistException;

import java.util.Hashtable;
import java.util.SortedMap;

/**
 * Static class that maps between a function name and function object. It contains all the
 * .sjava file functions.
 */
public class FuncTracker {
    private static Hashtable<String, Function> funcDict = new Hashtable<>();

    public static void validateFuncExist(String funcName) throws FuncNotExistException {
        if (!funcDict.containsKey(funcName)) {
            throw new FuncNotExistException(funcName);
        }
    }

    /**
     * return the func params if exists. else throw an error.
     * @param funcName
     * @return
     * @throws FuncNotExistException
     */
    public static SortedMap<String, Variable> getFuncArgs(String funcName) throws FuncNotExistException {
        validateFuncExist(funcName);
        return funcDict.get(funcName).getParams();
    }

    public static void addFunc(Function f) {
        funcDict.put(f.getName(), f);
    }
    public static boolean isExist(String funcName) {
        return funcDict.containsKey(funcName);
    }

    /**
     * a function for tests - when parse multiple files, need to reset the funcDict because it is static one.
     */
    public static void reset(){ //for tests
        funcDict = new Hashtable<>();
    }
}
