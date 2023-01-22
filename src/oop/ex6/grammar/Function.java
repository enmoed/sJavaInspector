package oop.ex6.grammar;


import oop.ex6.grammar.exceptions.GrammarException;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * A class to hold function properties such as it's name and arguments
 */
public class Function {
    private String name;
    private SortedMap<String, Variable> params; //should be sorted for args order

    public Function(String name){
        this.name = name;
        params = new TreeMap<String, Variable>();
    }

    public SortedMap<String, Variable> getParams() {
        return params;
    }

    public void addParam(Variable param) throws GrammarException {
        if(params.containsKey(param.getName())){throw new GrammarException(
                String.format("function %s gets 2 or more params with name %s", name,param.getName()));
        }
        params.put(param.getName(),param);
    }

    public String getName() {
        return name;
    }
}
