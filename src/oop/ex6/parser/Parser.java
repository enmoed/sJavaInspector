package oop.ex6.parser;

import oop.ex6.trackers.FuncTracker;
import oop.ex6.trackers.VarTracker;
import oop.ex6.vocabulary.*;
import oop.ex6.vocabulary.exceptions.SyntaxException;
import oop.ex6.vocabulary.exceptions.VocabularyException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Parser Class is the main object which validate a file. It contains the run method.
 */
public class Parser {

    /**
     * the main method of the program. Read the file and deals with exceptions.
     *
     * @param filePath
     */
    public static void run(String filePath) {
        int code;
        // open 2 BufferedReader for the first and second loops.
        try (BufferedReader readerFirstLoop = new BufferedReader(new FileReader(filePath));
             BufferedReader readerSecondLoop = new BufferedReader(new FileReader(filePath))) {
            firstLoop(readerFirstLoop);
            secondLoop(readerSecondLoop);
            code = 0;
        } catch (IOException e) {
            System.err.println(e.getMessage());
            code = 2;
        } catch (VocabularyException | SyntaxException e) {
            System.err.println(e.getMessage());
            code = 1;
        }
        System.out.println(code);
    }

    /**
     * the first loop validate all the global variables declarations and assignments (outside a function).
     * it also add the functions and the global variables to the FuncTracker and the VarTracker.
     * also verify that there is no if/while outside a function.
     *
     * @param reader
     * @throws VocabularyException
     * @throws IOException
     * @throws SyntaxException
     */
    private static void firstLoop(BufferedReader reader) throws VocabularyException, IOException, SyntaxException {
        // the level variable track after the scope. when it 0 it means we in the global scope.
        int level = 0;
        String statement;
        while ((statement = reader.readLine()) != null) {
            List<String> statementParse = SyntaxValidator.getLine(statement);
            StatementTypes statementType = StatementTypes.stringMapper.get(statementParse.get(0));
            switch (statementType) {
                case END_OF_BLOCK: {
                    if(level==0){throw new VocabularyException("there is extra closing bracket");}
                    level -= 1;
                    break;
                }
                case METHOD_DEC: {
                    if (level > 0) {
                        throw new VocabularyException("can't declare a nested function.");
                    }
                    // add to funcTracker
                    addFunctionToTracker(statementParse.subList(1, statementParse.size()));
                    level += 1;
                    break;
                }
                case IF_CALL:
                case WHILE_CALL: {
                    if (level == 0) {
                        throw new VocabularyException("while and if statements are only allow inside a function.");
                    }
                    level += 1;
                    break;
                }
                case VAR_DEC: {
                    if (level == 0) {
                        // validate and add to varTracker to global vars
                        ScopeValidator.validateGlobalVarDecStatement(statementParse.subList(1, statementParse.size()).
                                toArray(new String[0]));
                    }
                    break;
                }
                case VAR_ASSIGN: {
                    if (level == 0) {
                        // validate add update varTracker global vars that init.
                        ScopeValidator.validateGlobalVarAssignStatement(statementParse.subList(1, statementParse.size()).
                                toArray(new String[0]));
                    }
                    break;
                }
                case RETURN:
                case METHOD_CALL: {
                    if (level == 0) {
                        throw new VocabularyException(
                                String.format("%s statements are only allow inside a function.", statementType));
                    }
                    break;
                }
            }
        }
        // need to track the global variable which didn't assign,
        // because each function that uses them need to assign them.
        VarTracker.setGlobalVarWhichNotAssignOutsideFunctions();
    }

    /**
     * add a function to FuncTracker. throw VocabularyException if function already declared.
     *
     * @param statement
     * @throws VocabularyException
     */
    private static void addFunctionToTracker(List<String> statement) throws VocabularyException {
        String name = statement.get(1);
        if (FuncTracker.isExist(name)) {
            throw new VocabularyException(String.format("function %s already declared.", name));
        }
        Function f = new Function(name);
        int ind = 3;
        while (!statement.get(ind).equals(")")) {
            if (statement.get(ind).equals(",")) {
                ind += 1;
                continue;
            }
            if (statement.get(ind).equals("final")) {
                Variable param = new Variable(statement.get(ind + 2),
                        VariablesTypes.stringMapper.get(statement.get(ind) + 1),
                        true);
            }
            Variable param = (statement.get(ind).equals("final")) ? new Variable(statement.get(ind + 2),
                    VariablesTypes.stringMapper.get(statement.get(ind) + 1),
                    true) :
                    new Variable(statement.get(ind + 1),
                            VariablesTypes.stringMapper.get(statement.get(ind)));
            f.addParam(param);
            ind += 2;
        }
        FuncTracker.addFunc(f);
    }

    /**
     * iterate over all the functions and validate its block.
     *
     * @param reader
     * @throws VocabularyException
     * @throws IOException
     * @throws SyntaxException
     */
    private static void secondLoop(BufferedReader reader) throws VocabularyException, IOException, SyntaxException {
        String statement;
        while ((statement = reader.readLine()) != null) {
            List<String> statementParse = SyntaxValidator.getLine(statement);
            StatementTypes statementType = StatementTypes.stringMapper.get(statementParse.get(0));
            if (statementType.equals(StatementTypes.METHOD_DEC)) {
                VarTracker varTracker = new VarTracker();
                // add all declared args to scopeVars...
                var argsDict = FuncTracker.getFuncArgs(statementParse.get(2));
                for (var argName : argsDict.keySet()) {
                    varTracker.addScopeVar(argsDict.get(argName));
                }
                parseBlock(varTracker, true, reader);
            }
            // set all the global vars which didn't init as not init again
            // (to check in the next func that they will init too)
            VarTracker.resetInitOfGlobalVars();
        }
    }

    /**
     * validate a block - if/while/func.
     * create a new VarTracker based on the previous block one.
     * create its own ScopeValidator.
     *
     * @param prevBlockVarTracker
     * @param isFuncBlock
     * @param reader
     * @throws VocabularyException
     * @throws IOException
     * @throws SyntaxException
     */
    private static void parseBlock(VarTracker prevBlockVarTracker, boolean isFuncBlock, BufferedReader reader)
            throws VocabularyException, IOException, SyntaxException {
        VarTracker varTracker = new VarTracker(prevBlockVarTracker, isFuncBlock);
        ScopeValidator scopeValidator = new ScopeValidator(varTracker);
        StatementTypes lastStatementType = null;
        String statement = reader.readLine();
        List<String> statementParse = SyntaxValidator.getLine(statement);
        StatementTypes statementType = StatementTypes.stringMapper.get(statementParse.get(0));
        while (!statementType.equals(StatementTypes.END_OF_BLOCK)) {
            scopeValidator.validateStatement(statementType,
                    statementParse.subList(1, statementParse.size()).toArray(new String[0]));
            if (statementType.equals(StatementTypes.IF_CALL) || statementType.equals(StatementTypes.WHILE_CALL)) {
                parseBlock(varTracker, false, reader);
            }
            lastStatementType = statementType;
            statement = reader.readLine();
            if (statement == null) {
                throw new SyntaxException("missing closing brackets.");
            }
            statementParse = SyntaxValidator.getLine(statement);
            statementType = StatementTypes.stringMapper.get(statementParse.get(0));
        }
        if (isFuncBlock && !lastStatementType.equals(StatementTypes.RETURN)) {
            throw new VocabularyException("function end without return statement");
        }
    }
}
