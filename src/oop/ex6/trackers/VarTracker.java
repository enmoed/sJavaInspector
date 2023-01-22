package oop.ex6.trackers;

import oop.ex6.grammar.Variable;
import oop.ex6.grammar.exceptions.VarNotExistException;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

/**
 * Class to track the program variables. It has a static mapper for global variables, and instances can be
 * used to track block variables.
 */
public class VarTracker {
    private static Hashtable<String, Variable> globalVarDict = new Hashtable<>();
    private Hashtable<String, Variable> scopeVarDict;

    // because cant be 2 scope var with same name, and the scopeVarDict include prev scope.
    private Set<String> scopeVariableNames;

    private static Set<String> globalVarWhichNotAssignOutsideFunctions = new HashSet<>();

    /**
     * default constructor. init empty scopeVarDict and scopeVariableNames.
     */
    public VarTracker() {
        scopeVarDict = new Hashtable<>();
        scopeVariableNames = new HashSet<>();
    }

    /**
     * constructor for a block Tracker. receive the previous block VarTracker for know its variables as well.
     * if its a function block, the args include in the prevScope scopeVariableNames,
     * so it takes them to the scopeVariableNames.
     *
     * @param prevScope
     * @param isFuncBlock
     */
    public VarTracker(VarTracker prevScope, boolean isFuncBlock) {
        scopeVarDict = new Hashtable<>();
        scopeVarDict.putAll(prevScope.scopeVarDict);
        scopeVariableNames = (isFuncBlock) ? prevScope.getScopeVariableNames() : new HashSet<>();
    }

    public static void setGlobalVarWhichNotAssignOutsideFunctions() {
        for (String name : globalVarDict.keySet()) {
            if (!globalVarDict.get(name).isInit()) {
                globalVarWhichNotAssignOutsideFunctions.add(name);
            }
        }
    }

    public static void resetInitOfGlobalVars() {
        for (String name : globalVarDict.keySet()) {
            if (globalVarWhichNotAssignOutsideFunctions.contains(name)) {
                globalVarDict.get(name).resetAssign();
            }
        }
    }

    public static void addGlobalVar(Variable v) {
        globalVarDict.put(v.getName(), v);
    }

    public void addScopeVar(Variable v) {
        scopeVarDict.put(v.getName(), v);
        scopeVariableNames.add(v.getName());
    }

    public Set<String> getScopeVariableNames() {
        return scopeVariableNames;
    }

    public static boolean isGlobalVarExist(String varName) {
        return globalVarDict.containsKey(varName);
    }

    public void validateVarExist(String varName) throws VarNotExistException {
        if (!scopeVarDict.containsKey(varName) && !globalVarDict.containsKey(varName)) {
            throw new VarNotExistException(varName);
        }
    }

    public static void validateGlobalVarExist(String varName) throws VarNotExistException {
        if (!globalVarDict.containsKey(varName)) {
            throw new VarNotExistException(varName);
        }
    }

    public Variable getVar(String varName) throws VarNotExistException {
        validateVarExist(varName);
        return (scopeVarDict.containsKey(varName)) ? scopeVarDict.get(varName) : globalVarDict.get(varName);
    }

    public static Variable getGlobalVar(String varName) throws VarNotExistException {
        validateGlobalVarExist(varName);
        return globalVarDict.get(varName);
    }

    public static void reset() { //for tests
        globalVarDict = new Hashtable<>();
        globalVarWhichNotAssignOutsideFunctions = new HashSet<>();
    }
}
