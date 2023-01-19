package oop.ex6.parser;

import oop.ex6.trackers.FuncTracker;
import oop.ex6.trackers.VarTracker;
import oop.ex6.vocabulary.*;
import oop.ex6.vocabulary.exceptions.VocabularyException;

import java.util.List;

public class Parser {

    public void firstLoop() throws VocabularyException {
        int level = 0;
        String statement = getNextStatement();
        while (statement != null) {
            List<String> statementParse = SyntaxValidator.getLine(statement);
            StatementTypes statementType = StatementTypes.stringMapper.get(statementParse.get(0));
            if (statementType.equals(StatementTypes.END_OF_BLOCK)) {
                level -= 1;
                continue;
            }
            if (statementType.equals(StatementTypes.METHOD_DEC)) {
                // add to funcTracker
                addFunctionToTracker(statementParse.subList(1, statementParse.size()));
                level += 1;
                continue;
            }
            if (statementType.equals(StatementTypes.IF_CALL) || statementType.equals(StatementTypes.WHILE_CALL)) {
                level += 1;
                continue;
            }
            if (statementType.equals(StatementTypes.VAR_DEC) && level == 0) {
                // validate and add to varTracker to global vars
                ScopeValidator.validateGlobalVarDecStatement((String[]) statementParse.subList(1, statementParse.size()).toArray());
            }
            if (statementType.equals(StatementTypes.VAR_ASSIGN) && level == 0) {
                // validate add update varTracker global vars that init.
                ScopeValidator.validateGlobalVarAssignStatement((String[]) statementParse.subList(1, statementParse.size()).toArray());
            }
        }
        VarTracker.setGlobalVarWhichNotAssignOutsideFunctions();
    }

    private void addFunctionToTracker(List<String> statement) throws VocabularyException {
        String name = statement.get(1);
        if (FuncTracker.isExist(name)) {
            throw new VocabularyException(String.format("function %s already declared", name));
        }
        Function f = new Function(name);
        int ind = 3;
        while (!statement.get(ind).equals(")")) {
            if (statement.get(ind).equals(",")) {
                ind += 1;
                continue;
            }
            f.addParam(statement.get(ind), VariablesTypes.stringMapper.get(statement.get(ind + 1)));
            ind += 2;
        }
        FuncTracker.addFunc(f);
    }

    public void secondLoop() throws VocabularyException {
        String statement = getNextStatement();
        while (statement != null) {
            List<String> statementParse = SyntaxValidator.getLine(statement);
            StatementTypes statementType = StatementTypes.stringMapper.get(statementParse.get(0));
            if (statementType.equals(StatementTypes.METHOD_DEC)) {
                VarTracker varTracker = new VarTracker();
                // add all declared args to scopeVars...
                parseBlock(varTracker, true);
            }
            // set all the global vars which didnt init as not init again (to check in the next func that they will init too)
            VarTracker.resetInitOfGlobalVars();
            statement = getNextStatement();
        }
    }

    public void parseBlock(VarTracker prevBlockVarTracker, boolean isFuncBlock) throws VocabularyException {
        VarTracker varTracker = new VarTracker(prevBlockVarTracker);
        ScopeValidator scopeValidator = new ScopeValidator(varTracker);
        StatementTypes lastStatementType = null;
        String statement = getNextStatement();
        List<String> statementParse = SyntaxValidator.getLine(statement);
        StatementTypes statementType = StatementTypes.stringMapper.get(statementParse.get(0));
        while (!statementType.equals(StatementTypes.END_OF_BLOCK)) {
            scopeValidator.validateStatement(statementType, (String[]) statementParse.subList(1, statementParse.size()).toArray());
            if (statementType.equals(StatementTypes.IF_CALL) || statementType.equals(StatementTypes.WHILE_CALL)) {
                parseBlock(varTracker, false);
            }
            lastStatementType = statementType;
            statement = getNextStatement();
            statementParse = SyntaxValidator.getLine(statement);
            statementType = StatementTypes.stringMapper.get(statementParse.get(0));
        }
        if (isFuncBlock && !lastStatementType.equals(StatementTypes.RETURN)) {
            throw new VocabularyException("function end without return statement");
        }
    }
}
