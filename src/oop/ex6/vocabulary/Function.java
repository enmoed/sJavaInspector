package oop.ex6.vocabulary;


import java.util.SortedMap;
import java.util.TreeMap;

public class Function {
    private String name;
    private SortedMap<String, VariablesTypes> params; //should be sorted for args order

    public Function(String name){
        this.name = name;
        params = new TreeMap<String, VariablesTypes>();
    }

    public SortedMap<String, VariablesTypes> getParams() {
        return params;
    }

    public void addParam(String paramName, VariablesTypes type){
        params.put(paramName,type);
    }

    public String getName() {
        return name;
    }
}
