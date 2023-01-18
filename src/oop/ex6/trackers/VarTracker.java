package oop.ex6.trackers;

import oop.ex6.vocabulary.Variable;
import oop.ex6.vocabulary.exceptions.VarNotExistException;

import java.util.Hashtable;

public class VarTracker {
    private static Hashtable<String, Variable> globalVarDict = new Hashtable<>();
    private Hashtable<String, Variable> scopeVarDict;

    public VarTracker() {
        scopeVarDict = new Hashtable<>();
    }

    public VarTracker(VarTracker prevScope) {
        scopeVarDict = new Hashtable<>();
        scopeVarDict.putAll(prevScope.scopeVarDict);
    }

    public static void addGlobalVar(Variable v) {
        globalVarDict.put(v.getName(), v);
    }

    public void addScopeVar(Variable v) {
        scopeVarDict.put(v.getName(), v);
    }

    public void validateVarExist(String varName) throws VarNotExistException {
        if (!scopeVarDict.containsKey(varName) && !globalVarDict.containsKey(varName)) {
            throw new VarNotExistException(varName);
        }
    }
}
