package oop.ex6.vocabulary;

import oop.ex6.trackers.FuncTracker;
import oop.ex6.trackers.VarTracker;
import oop.ex6.vocabulary.exceptions.VocabularyException;

import java.util.Arrays;
import java.util.Iterator;

public class ScopeValidator {
    private VarTracker varTracker;

    public ScopeValidator(VarTracker varTracker) {
        this.varTracker = varTracker;
    }

    public void validateStatement(StatementTypes statementType, String[] statement) throws VocabularyException {
        switch (statementType) {
            case METHOD_CALL: {
                validateCallFuncCallStatement(statement);
            }
            case IF_CALL: {
                validateIfWhileCallStatement(statement);
            }
            case WHILE_CALL: {
                validateIfWhileCallStatement(statement);
            }
            case VAR_DEC: {
                validateVarDecStatement(statement);
            }
            case VAR_ASSIGN: {
                validateVarAssignStatement(statement);
            }
        }
    }

    public void validateCallFuncCallStatement(String[] statement) throws VocabularyException {
        String funcName = statement[0];
        var params = FuncTracker.getFuncArgs(funcName);
        Iterator<String> paramsNamesIterator = params.keySet().iterator();
        for (int i = 2; i < statement.length - 2; i += 2) {
            if (!paramsNamesIterator.hasNext()) {
                throw new VocabularyException(String.format("to many args to func", funcName));
            }
            String currParamName = paramsNamesIterator.next();
            validateExpression(statement[i], params.get(currParamName), true);
        }

    }

    public void validateExpression(String expression, VariablesTypes expectedType, boolean withCasting) throws VocabularyException {
        if (isVariable(expression)) {
            Variable expressionVar = varTracker.getVar(expression);
            //check if init
            if (!expressionVar.isInit()) {
                throw new VocabularyException("param didnt init");
            }
            //check if valid variable
            VariablesTypes.validateVarType(expectedType, expressionVar.getType(), withCasting);
        } else {
            // validate const
            VariablesTypes.validateConstType(expectedType, expression);
        }
    }

    public void validateVarAssignStatement(String[] statement) throws VocabularyException {
        for (int i = 0; i < statement.length; i += 4) {
            String varName = statement[i];
            Variable var = varTracker.getVar(varName);
            if (var.isFinal()) {
                throw new VocabularyException("var is final and can not assigned again");
            }
            String expression = statement[i + 2];
            validateExpression(expression, var.getType(), true);
            var.hasAssign(); //TODO if it is global var and didnt init outside the function, should be init in each function
        }
    }

    public void validateVarDecStatement(String[] statement) throws VocabularyException {
        boolean isFinal = statement[0].equals("final"); //TODO check that there is init
        int start = (isFinal) ? 1 : 0;
        VariablesTypes type = VariablesTypes.stringMapper.get(statement[start]);
        int ind = start + 1;
        boolean isVarName = true;
        int varNamesStart = ind;
        while (!statement[ind].equals(";")) {
            if (statement[ind].equals("=")) {
                isVarName = false;
                ind += 1;
                continue;
            }
            if (statement[ind].equals(",") && !isVarName) {
                isVarName = true;
                ind += 1;
                varNamesStart = ind;
                continue;
            }
            if (statement[ind].equals(",") && isVarName) {
                ind += 1;
                continue;
            }
            if (isVarName) {
                Variable var = new Variable(statement[ind], type, isFinal);
                if (varTracker.getScopeVariableNames().contains(var.getName())) {
                    throw new VocabularyException(String.format("var with name %s already declared in the current block.", var.getName()));
                }
                varTracker.addScopeVar(var);
            } else {//const init
                String constTerm = statement[ind];
                VariablesTypes.validateConstType(type, constTerm);
                //update that varNames assign
                while (!statement[varNamesStart].equals("=")) {
                    if (!statement[varNamesStart].equals(",")) {
                        varTracker.getVar(statement[varNamesStart]).hasAssign();
                    }
                    varNamesStart += 1;
                }
            }
            ind += 1;
        }
    }

    public void validateIfWhileCallStatement(String[] statement) throws VocabularyException {
        String[] condition = Arrays.copyOfRange(statement, 3, statement.length - 2);
        validateCondition(condition);
    }

    public void validateCondition(String[] condition) throws VocabularyException {
        for (int i = 0; i < condition.length; i += 2) {
            String term = condition[i];
            if (isVariable(term)) {
                //check if valid variable
                Variable termVar = varTracker.getVar(term);
                //has assign
                if (!termVar.isInit()) {
                    throw new VocabularyException("var didnt assign");
                }
                //check if valid variable
                VariablesTypes.validateVarType(VariablesTypes.BOOLEAN, termVar.getType());
            } else {
                // validate const
                VariablesTypes.validateBoolean(term);
            }
        }
    }
}
