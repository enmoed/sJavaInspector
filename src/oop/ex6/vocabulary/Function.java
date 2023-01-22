package oop.ex6.vocabulary;


import oop.ex6.vocabulary.exceptions.VocabularyException;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * a function object for contain the function information: name and args.
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

    public void addParam(Variable param) throws VocabularyException {
        if(params.containsKey(param.getName())){throw new VocabularyException(
                String.format("function %s gets 2 or more params with name %s", name,param.getName()));
        }
        params.put(param.getName(),param);
    }

    public String getName() {
        return name;
    }
}
