package oop.ex6.parser;

import oop.ex6.trackers.FuncTracker;
import oop.ex6.trackers.VarTracker;
import oop.ex6.vocabulary.*;
import oop.ex6.vocabulary.exceptions.SyntaxException;
import oop.ex6.vocabulary.exceptions.VocabularyException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Parser {

    public static void run(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            firstLoop(reader);
            secondLoop(reader);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (VocabularyException e) {
            throw new RuntimeException(e);
        } catch (SyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static void firstLoop(BufferedReader reader) throws VocabularyException, IOException, SyntaxException {
        int level = 0;
        String statement;
        while ((statement = reader.readLine()) != null) {
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
                ScopeValidator.validateGlobalVarDecStatement(statementParse.subList(1, statementParse.size()).toArray(new String[0]));
            }
            if (statementType.equals(StatementTypes.VAR_ASSIGN) && level == 0) {
                // validate add update varTracker global vars that init.
                ScopeValidator.validateGlobalVarAssignStatement(statementParse.subList(1, statementParse.size()).toArray(new String[0]));
            }
        }
        VarTracker.setGlobalVarWhichNotAssignOutsideFunctions();
    }

    private static void addFunctionToTracker(List<String> statement) throws VocabularyException {
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

    private static void secondLoop(BufferedReader reader) throws VocabularyException, IOException, SyntaxException {
        String statement;
        while ((statement = reader.readLine()) != null) {
            List<String> statementParse = SyntaxValidator.getLine(statement);
            StatementTypes statementType = StatementTypes.stringMapper.get(statementParse.get(0));
            if (statementType.equals(StatementTypes.METHOD_DEC)) {
                VarTracker varTracker = new VarTracker();
                // add all declared args to scopeVars...
                parseBlock(varTracker, true, reader);
            }
            // set all the global vars which didnt init as not init again (to check in the next func that they will init too)
            VarTracker.resetInitOfGlobalVars();
        }
    }

    private static void parseBlock(VarTracker prevBlockVarTracker, boolean isFuncBlock, BufferedReader reader) throws VocabularyException, IOException, SyntaxException {
        VarTracker varTracker = new VarTracker(prevBlockVarTracker);
        ScopeValidator scopeValidator = new ScopeValidator(varTracker);
        StatementTypes lastStatementType = null;
        String statement = reader.readLine();
        List<String> statementParse = SyntaxValidator.getLine(statement);
        StatementTypes statementType = StatementTypes.stringMapper.get(statementParse.get(0));
        while (!statementType.equals(StatementTypes.END_OF_BLOCK)) {
            scopeValidator.validateStatement(statementType, statementParse.subList(1, statementParse.size()).toArray(new String[0]));
            if (statementType.equals(StatementTypes.IF_CALL) || statementType.equals(StatementTypes.WHILE_CALL)) {
                parseBlock(varTracker, false, reader);
            }
            lastStatementType = statementType;
            statement = reader.readLine();
            statementParse = SyntaxValidator.getLine(statement);
            statementType = StatementTypes.stringMapper.get(statementParse.get(0));
        }
        if (isFuncBlock && !lastStatementType.equals(StatementTypes.RETURN)) {
            throw new VocabularyException("function end without return statement");
        }
    }
}
