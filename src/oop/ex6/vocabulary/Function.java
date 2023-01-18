package oop.ex6.vocabulary;


import java.util.SortedMap;

public class Function {
    private String name;
    private SortedMap<String, VariablesTypes> params; //should be sorted for args order

    public SortedMap<String, VariablesTypes> getParams() {
        return params;
    }

    public String getName() {
        return name;
    }
}
