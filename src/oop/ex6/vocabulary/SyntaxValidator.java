package oop.ex6.vocabulary;

import oop.ex6.vocabulary.exceptions.SyntaxException;

import java.util.ArrayList;
import java.util.List;

public class SyntaxValidator {
    private static final String VAR_NAME = "\\s*[_a-zA-z]\\w*\\s*";
    private static final String METHOD_NAME = "\\s*[a-zA-z]\\w*\\s*";
    private static final String INT = "\\s*[-+]?\\d+\\s*";
    private static final String DOUBLE = "\\s*[-+]?(\\d+(\\.\\d*)?|(\\.\\d+))\\s*";
    private static final String STRING = "\\s*.+\\s*";
    private static final String LITERAL = "\\s*\"" + STRING + "\"\\s*";
    private static final String CHAR = "\\s*'\\s*.\\s*'\\s*";
    private static final String BOOL = "\\s*(false|true)\\s*";
    public static final String CONST = INT + "|" + DOUBLE + "|" + LITERAL + "|" + CHAR + "|" + BOOL;
    private static final String VAR = "(" + CONST + "|" + VAR_NAME + ")";
    private static final String VAR_LIST = "\\s*(" + VAR + "(," + VAR + ")*)?";
    private static final String END_LINE = ";\\s*";
    private static final String BOOL_OR_DOUBLE = "(" + BOOL + "|" + DOUBLE + ")";
    private static final String IF = "\\s*if\\s*";
    private static final String WHILE = "\\s*while\\s*";
    private static final String TYPE = "(int|double|String|boolean|char)";
    private static final String PARAM_LIST = "\\s*(" + TYPE + VAR_NAME + "(,\\s*" + TYPE + VAR_NAME + ")*)?";
    private static final String OP = "(\\|\\||&&)";
    private static final String TERM = "(" + DOUBLE + "|" + VAR_NAME + ")";
    private static final String CONDITION = TERM + "(" + OP + TERM + ")*";
    private static final String VOID = "\\s*void";
    private static final String CLOSE_BRACKETS = "\\s*}\\s*";
    private static final String OPEN_BRACKETS = "\\s*\\{\\s*";
    private static final String RETURN = "\\s*return\\s*";
    private static final String COMMENT = "//.*";
    private static final String FINAL = "\\s*(final)?\\s*";

    public static List<String> getLine(String line) throws SyntaxException {
        if (line.matches(IF + "\\(" + CONDITION + "\\)" + OPEN_BRACKETS)) {
            return extractIfStatement(line);
        }
        if (line.matches(WHILE + "\\(" + CONDITION + "\\)" + OPEN_BRACKETS)) {
            return extractWhileStatement(line);
        }
        if (line.matches(VOID + METHOD_NAME + "\\(" + PARAM_LIST + "\\)" + OPEN_BRACKETS)) {
            return extractMethodDeclaration(line);
        }
        if (line.matches(RETURN + END_LINE)) {
            return returnStatement();
        }/**/
        if (line.matches(VAR_NAME + "\\(" + VAR_LIST + "\\)\\s*" + END_LINE)) {
            return extractMethodCall(line);
        }
        if (line.matches(FINAL + "int(" + VAR_NAME + "=(" + INT + "|" + VAR_NAME + "))(," +
                VAR_NAME + "=" + "(" + INT + "|" + VAR_NAME + ")" + "?)*" + END_LINE)) {
            return extractDeclaration(line);
        }
        if (line.matches(FINAL + "double(" + VAR_NAME + "=(" + DOUBLE + "|" + VAR_NAME + "))(," +
                VAR_NAME + "=" + "(" + DOUBLE + "|" + VAR_NAME + ")" + ")*" + END_LINE)) {
            return extractDeclaration(line);
        }
        if (line.matches(FINAL + "String(" + VAR_NAME + "=(" + LITERAL + "|" + VAR_NAME + "))(," +
                VAR_NAME + "=" + "(" + LITERAL + "|" + VAR_NAME + ")" + ")*" + END_LINE)) {
            return extractDeclaration(line);
        }
        if (line.matches(FINAL + "boolean(" + VAR_NAME + "=(" + BOOL_OR_DOUBLE + "|" + VAR_NAME +
                "))(," + VAR_NAME + "=" + "(" + BOOL_OR_DOUBLE + "|" + VAR_NAME + ")" + ")*" + END_LINE)) {
            return extractDeclaration(line);
        }
        if (line.matches(FINAL + "char(" + VAR_NAME + "=(" + CHAR + "|" + VAR_NAME + "))(," +
                VAR_NAME + "=" + "(" + CHAR + "|" + VAR_NAME + ")" + ")*" + END_LINE)) {
            return extractDeclaration(line);
        }
        if (line.matches("\\s*int(" + VAR_NAME + "(=(" + INT + "|" + VAR_NAME + "))?)(," +
                VAR_NAME + "(=" + "(" + INT + "|" + VAR_NAME + ")" + ")?)*" + END_LINE)) {
            return extractDeclaration(line);
        }
        if (line.matches("\\s*double(" + VAR_NAME + "(=(" + DOUBLE + "|" + VAR_NAME + "))?)(," +
                VAR_NAME + "(=" + "(" + DOUBLE + "|" + VAR_NAME + ")" + ")?)*" + END_LINE)) {
            return extractDeclaration(line);
        }
        if (line.matches("\\s*String(" + VAR_NAME + "(=(" + LITERAL + "|" + VAR_NAME + "))?)(," +
                VAR_NAME + "(=" + "(" + LITERAL + "|" + VAR_NAME + ")" + ")?)*" + END_LINE)) {
            return extractDeclaration(line);
        }
        if (line.matches("\\s*boolean(" + VAR_NAME + "(=(" + BOOL_OR_DOUBLE + "|" + VAR_NAME +
                "))?)(," + VAR_NAME + "(=" + "(" + BOOL_OR_DOUBLE + "|" + VAR_NAME + ")" + ")?)*" +
                END_LINE)) {
            return extractDeclaration(line);
        }
        if (line.matches("\\s*char(" + VAR_NAME + "(=" + "(" + CHAR + "|" + VAR_NAME + "))?)(," +
                VAR_NAME + "(=" + "(" + CHAR + "|" + VAR_NAME + ")" + ")?)*" + END_LINE)) {
            return extractDeclaration(line);
        }
        if (line.matches("\\s*(" + VAR_NAME + "=" + VAR + ")(," + VAR_NAME + "=" + VAR + ")*" +
                END_LINE)) {
            return extractAssignment(line);
        }
        if (line.matches(CLOSE_BRACKETS)) {
            return extractBlockEnd();
        }
        if (line.matches(COMMENT)) {
            return extractComment();
        }
        if (line.matches("\\s*") || line.isEmpty()) {
            return extractComment();
        }
        throw new SyntaxException(line);
    }

    private static List<String> extractComment() {
        List<String> list = new ArrayList<>();
        list.add(StatementTypes.COMMENT.toString());
        list.add("\\\\");
        return list;
    }

    private static List<String> extractBlockEnd() {
        List<String> list = new ArrayList<>();
        list.add(StatementTypes.END_OF_BLOCK.toString());
        list.add("}");
        return list;
    }

    private static List<String> extractAssignment(String line) {
        List<String> list = new ArrayList<>();
        String[] parts = line.split("(?==)|(?<==)|(?=,)|(?<=,)|(?=;)");
        list.add(StatementTypes.VAR_ASSIGN.toString());
        return getParsedLine(parts, list);
    }

    private static List<String> extractDeclaration(String line) {
        List<String> list = new ArrayList<>();
        String[] parts = line.split("(?<=final)|(?<=" + TYPE + ")|(?==)|(?<==)|(?=,)|(?<=,)|(?=;)");
        list.add(StatementTypes.VAR_DEC.toString());
        return getParsedLine(parts, list);
    }

    private static List<String> extractMethodCall(String line) {
        List<String> list = new ArrayList<>();
        String[] parts = line.split("(?=\\()|(?<=\\()|(?=,)|(?<=,)|(?=\\))|(?=;)");
        list.add(StatementTypes.METHOD_CALL.toString());
        return getParsedLine(parts, list);
    }

    private static List<String> extractMethodDeclaration(String line) {
        List<String> list = new ArrayList<>();
        String[] parts = line.split("(?<=void)|(?=\\()|(?<=\\()|(?<=" + TYPE +
                ")|(?=,)|(?<=,)|(?=\\))|(?=\\{)");
        list.add(StatementTypes.METHOD_DEC.toString());
        return getParsedLine(parts, list);
    }

    private static List<String> extractIfStatement(String line) {
        List<String> list = new ArrayList<>();
        String[] parts = line.split("(?<=if)|(?<=\\()|(?=(&&))|(?=(\\|\\|))|(?<=(&&))|" +
                "(?<=(\\|\\|))|(?=\\))|(?=\\{)");
        list.add(StatementTypes.IF_CALL.toString());
        return getParsedLine(parts, list);
    }

    private static List<String> extractWhileStatement(String line) {
        List<String> list = new ArrayList<>();
        String[] parts = line.split("(?<=while)|(?<=\\()|(?=(&&))|(?=(\\|\\|))|(?<=(&&))|" +
                "(?<=(\\|\\|))|(?=\\))|(?=\\{)");
        list.add(StatementTypes.WHILE_CALL.toString());
        return getParsedLine(parts, list);
    }

    private static List<String> getParsedLine(String[] parts, List<String> list) {
        for (String part : parts) {
            part = part.trim();
            if (!part.isEmpty()) {
                list.add(part);
            }
        }
        return list;
    }

    private static List<String> returnStatement() {
        List<String> list = new ArrayList<>();
        list.add(StatementTypes.RETURN.toString());
        list.add("return");
        list.add(";");
        return list;
    }

}
