package oop.ex6.trackers;

import oop.ex6.vocabulary.Variable;
import oop.ex6.vocabulary.VariablesTypes;
import oop.ex6.vocabulary.exceptions.VarNotExistException;
import oop.ex6.vocabulary.exceptions.VarTypeNotMatchException;
import oop.ex6.vocabulary.exceptions.VocabularyException;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

public class VarTracker {
    private static Hashtable<String, Variable> globalVarDict = new Hashtable<>();
    private Hashtable<String, Variable> scopeVarDict;
    private Set<String> scopeVariableNames; // because cant be 2 scope var with same name, and the scopeVarDict include prev scope

    private static Set<String> globalVarWhichNotAssignOutsideFunctions = new HashSet<>();
    public VarTracker() {
        scopeVarDict = new Hashtable<>();
        scopeVariableNames = new HashSet<>();
    }

    public VarTracker(VarTracker prevScope) {
        scopeVarDict = new Hashtable<>();
        scopeVarDict.putAll(prevScope.scopeVarDict);
        scopeVariableNames = new HashSet<>();
    }

    public static void setGlobalVarWhichNotAssignOutsideFunctions(){
        for(String name:globalVarDict.keySet()){
            if(!globalVarDict.get(name).isInit()){globalVarWhichNotAssignOutsideFunctions.add(name);}
        }
    }

    public static void resetInitOfGlobalVars(){
        for(String name:globalVarDict.keySet()){
            if(globalVarWhichNotAssignOutsideFunctions.contains(name)){
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
    public Set<String> getScopeVariableNames(){return scopeVariableNames;}

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
    public VariablesTypes getVarType(String varName) throws VarNotExistException {
        validateVarExist(varName);
        Variable v = (!scopeVarDict.containsKey(varName)) ? scopeVarDict.get(varName) : globalVarDict.get(varName);
        return v.getType();
    }
    public Variable getVar(String varName) throws VarNotExistException {
        validateVarExist(varName);
        return (!scopeVarDict.containsKey(varName)) ? scopeVarDict.get(varName) : globalVarDict.get(varName);
    }
    public static Variable getGlobalVar(String varName) throws VarNotExistException {
        validateGlobalVarExist(varName);
        return globalVarDict.get(varName);
    }
}
