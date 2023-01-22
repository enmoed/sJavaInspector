package oop.ex6.vocabulary;

import oop.ex6.trackers.FuncTracker;
import oop.ex6.trackers.VarTracker;
import oop.ex6.vocabulary.exceptions.VocabularyException;

import java.util.Arrays;
import java.util.Iterator;

import static oop.ex6.vocabulary.SyntaxValidator.CONST;

/**
 * class for validate a scope.
 * uses the FuncTracker class and has a VarTracker instance.
 */
public class ScopeValidator {
    private VarTracker varTracker;

    public ScopeValidator(VarTracker varTracker) {
        this.varTracker = varTracker;
    }

    /**
     * the main method of the class - get a statementType and a parsed statement and
     * if there is a problem it throws a VocabularyException.
     *
     * @param statementType
     * @param statement
     * @throws VocabularyException
     */
    public void validateStatement(StatementTypes statementType, String[] statement) throws VocabularyException {
        switch (statementType) {
            case METHOD_CALL: {
                validateCallFuncCallStatement(statement);
                break;
            }
            case IF_CALL:
            case WHILE_CALL: {
                validateIfWhileCallStatement(statement);
                break;
            }
            case VAR_DEC: {
                validateVarDecStatement(statement);
                break;
            }
            case VAR_ASSIGN: {
                validateVarAssignStatement(statement);
                break;
            }
        }
    }

    /**
     * check that function declared and the params are legal (type and amount wise).
     *
     * @param statement
     * @throws VocabularyException
     */
    public void validateCallFuncCallStatement(String[] statement) throws VocabularyException {
        String funcName = statement[0];
        var params = FuncTracker.getFuncArgs(funcName);
        Iterator<String> paramsNamesIterator = params.keySet().iterator();
        for (int i = 2; i < statement.length - 2; i += 2) {
            if (!paramsNamesIterator.hasNext()) {
                throw new VocabularyException(String.format("function %s received too many arguments.", funcName));
            }
            String currParamName = paramsNamesIterator.next();
            validateExpression(statement[i], params.get(currParamName).getType(), true);
        }
        if (paramsNamesIterator.hasNext()) {
            throw new VocabularyException(
                    String.format("%s didn't gets enough params.", funcName));
        }

    }

    public void validateExpression(String expression, VariablesTypes expectedType, boolean withCasting)
            throws VocabularyException {
        if (isVariable(expression)) {
            Variable expressionVar = varTracker.getVar(expression);
            //check if init
            if (!expressionVar.isInit()) {
                throw new VocabularyException(String.format("Try to assign %s but this variable didn't init yet",
                        expression));
            }
            //check if valid variable
            VariablesTypes.validateVarType(expectedType, expressionVar.getType(), withCasting);
        } else {
            // validate const
            VariablesTypes.validateConstType(expectedType, expression);
        }
    }

    public static void StaticvalidateExpression(String expression, VariablesTypes expectedType, boolean withCasting)
            throws VocabularyException {
        if (isVariable(expression)) {
            Variable expressionVar = VarTracker.getGlobalVar(expression);
            //check if init
            if (!expressionVar.isInit()) {
                throw new VocabularyException(String.format("Try to assign %s but this variable didn't init yet",
                        expression));
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
                throw new VocabularyException(String.format("var %s is final and can not be assigned again", varName));
            }
            String expression = statement[i + 2];
            validateExpression(expression, var.getType(), true);
            var.hasAssign();
        }
    }

    public static void validateGlobalVarAssignStatement(String[] statement) throws VocabularyException {
        for (int i = 0; i < statement.length; i += 4) {
            String varName = statement[i];
            Variable var = VarTracker.getGlobalVar(varName);
            if (var.isFinal()) {
                throw new VocabularyException(String.format("var %s is final and can not be assigned again", varName));
            }
            String expression = statement[i + 2];
            StaticvalidateExpression(expression, var.getType(), true);
            var.hasAssign();
        }
    }

    public void validateVarDecStatement(String[] statement) throws VocabularyException {
        boolean isFinal = statement[0].equals("final");
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
                    throw new VocabularyException(
                            String.format("Variable with name %s already declared in the current block.",
                                    var.getName()));
                }
                varTracker.addScopeVar(var);
            } else {
                validateExpression(statement[ind], type, true);
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

    public static void validateGlobalVarDecStatement(String[] statement) throws VocabularyException {
        boolean isFinal = statement[0].equals("final");
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
                if (VarTracker.isGlobalVarExist(var.getName())) {
                    throw new VocabularyException(
                            String.format("Global variable with name %s already declared in the current block.",
                                    var.getName()));
                }
                VarTracker.addGlobalVar(var);
            } else {
                StaticvalidateExpression(statement[ind], type, true);
                //update that varNames assign
                while (!statement[varNamesStart].equals("=")) {
                    if (!statement[varNamesStart].equals(",")) {
                        VarTracker.getGlobalVar(statement[varNamesStart]).hasAssign();
                    }
                    varNamesStart += 1;
                }
            }
            ind += 1;
        }
    }

    public void validateIfWhileCallStatement(String[] statement) throws VocabularyException {
        String[] condition = Arrays.copyOfRange(statement, 2, statement.length - 2);
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
                    throw new VocabularyException(String.format("Variable %s didnt assign", term));
                }
                //check if valid variable
                VariablesTypes.validateVarType(VariablesTypes.BOOLEAN, termVar.getType(), true);
            } else {
                // validate const
                VariablesTypes.validateBoolean(term);
            }
        }
    }

    private static boolean isVariable(String s) {
        return !s.matches(CONST);
    }
}
