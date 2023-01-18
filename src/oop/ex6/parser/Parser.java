package oop.ex6.parser;

import oop.ex6.trackers.VarTracker;
import oop.ex6.vocabulary.ScopeValidator;
import oop.ex6.vocabulary.StatementTypes;
import oop.ex6.vocabulary.SyntaxValidator;
import oop.ex6.vocabulary.exceptions.VocabularyException;

public class Parser {

    public void firstLoop() {
        int level = 0;
        String statement = getNextStatement();
        while (statement != null) {
            if (statement.equals("}")) {
                level -= 1;
                continue;
            }
            String[] statementParse = SyntaxValidator.parse(statement);
            StatementTypes statementType = StatementTypes.stringMapper.get(statementParse[0]);
            if (statementType.equals(StatementTypes.METHOD_DEC)) {
                //#TODO add to funcTracker

                level += 1;
                continue;
            }
            if (statementType.equals(StatementTypes.IF_CALL) || statementType.equals(StatementTypes.WHILE_CALL)) {
                level += 1;
                continue;
            }
            if (statementType.equals(StatementTypes.VAR_DEC) && level == 0) {
                //TODO validate and add to varTracker to global vars
            }
            if (statementType.equals(StatementTypes.VAR_ASSIGN) && level == 0) {
                //TODO validate add update varTracker global vars that init.
            }

        }
    }

    public void secondLoop() {
        // TODO set of all the global vars which didnt init
        String statement = getNextStatement();
        while (statement != null) {
            String[] statementParse = SyntaxValidator.parse(statement);
            StatementTypes statementType = StatementTypes.stringMapper.get(statementParse[0]);
            if (statementType.equals(StatementTypes.METHOD_DEC)) {
                VarTracker varTracker = new VarTracker();
                // TODO add all declared args to scopeVars...
                parseBlock(varTracker);
            }
            // TODO set all the global vars which didnt init as not init again (to check in the next func that they will init too)
            statement = getNextStatement();
        }
    }

    public void parseBlock(VarTracker prevBlockVarTracker) throws VocabularyException {
        VarTracker varTracker = new VarTracker(prevBlockVarTracker);
        ScopeValidator scopeValidator = new ScopeValidator(varTracker);
        String statement = getNextStatement();
        while (!statement.equals("}")) {
            String[] statementParse = SyntaxValidator.parse(statement);
            StatementTypes statementType = StatementTypes.stringMapper.get(statementParse[0]);
            scopeValidator.validateStatement(statementType, statementParse);
            if (statementType.equals(StatementTypes.IF_CALL) || statementType.equals(StatementTypes.WHILE_CALL)) {
                parseBlock(varTracker);
            }
        }
    }
}
