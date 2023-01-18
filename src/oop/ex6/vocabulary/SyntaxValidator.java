package oop.ex6.vocabulary;

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
    private static final String END_LINE = ";\\s*";
    private static final String VAR = "(" + INT + "|" + DOUBLE + "|" + LITERAL + "|" + CHAR + "|" + BOOL + "|" + VAR_NAME + ")";
    private static final String VAR_LIST = "\\s*(" + VAR + "(," + VAR + ")*)?";
    private static final String BOOL_OR_DOUBLE = "(" + BOOL + "|" + DOUBLE + ")";
    private static final String IF_WHILE = "\\s*(if|while)\\s*";
    private static final String TYPE = "(int|double|String|boolean|char)";
    private static final String PARAM_LIST = "\\s*(" + TYPE + VAR_NAME + "(,\\s*" + TYPE + VAR_NAME + ")*)?";
    private static final String OP = "(\\|\\||&&)";
    private static final String TERM = "(" + DOUBLE + "|" + VAR_NAME + ")";
    private static final String CONDITION = TERM + "(" + OP + TERM + ")*";;
    private static final String VOID = "\\s*void";
    private static final String CLOSE_BRACKETS = "\\s*}\\s*";
    private static final String OPEN_BRACKETS = "\\s*\\{\\s*";
    private static final String RETURN = "\\s*return\\s*";
    private static final String COMMENT = "\\\\\\\\.*";

    public static List<String> getLine(String line) {
        if (line.matches( IF_WHILE + "\\(" + CONDITION + "\\)" + OPEN_BRACKETS)) {
            return extractStatement(line);
        }
        if (line.matches(VOID + METHOD_NAME + "\\(" + PARAM_LIST + "\\)" + OPEN_BRACKETS)) {
            return extractMethodDeclaration(line);
        }
        if (line.matches(RETURN + END_LINE)) {
            return returnStatement();
        }
        if (line.matches(VAR_NAME + "\\(" + VAR_LIST + "\\)\\s*" + END_LINE)) {
            return extractMethodCall(line);
        }
        if (line.matches("\\s*int(" + VAR_NAME + "(=" + INT + ")?)" +
                "(," + VAR_NAME + "(=" + INT + ")?)*" + END_LINE)) {
            return extractDeclaration(line);
        }
        if (line.matches("\\s*double(" + VAR_NAME + "(=" + DOUBLE + ")?)" +
                "(," + VAR_NAME + "(=" + DOUBLE + ")?)*" + END_LINE)) {
            return extractDeclaration(line);
        }
        if (line.matches("\\s*String(" + VAR_NAME + "(=" + LITERAL + ")?)" +
                "(," + VAR_NAME + "(=" + LITERAL + ")?)*" + END_LINE)) {
            return extractDeclaration(line);
        }
        if (line.matches("\\s*boolean(" + VAR_NAME + "(=" + BOOL_OR_DOUBLE + ")?)" +
                "(," + VAR_NAME + "(=" + BOOL_OR_DOUBLE + ")?)*" + END_LINE)) {
            return extractDeclaration(line);
        }
        if (line.matches("\\s*char(" + VAR_NAME + "(=" + CHAR + ")?)" +
                "(," + VAR_NAME + "(=" + CHAR + ")?)*" + END_LINE)) {
            return extractDeclaration(line);
        }
        if (line.matches("\\s*(" + VAR_NAME + "=" + VAR + ")" +
                "(," + VAR_NAME + "=" + VAR + ")*" + END_LINE)) {
            return extractAssignment(line);
        }
        if (line.matches(CLOSE_BRACKETS)) {
            return extractMethodEnd();
        }
        if (line.matches( COMMENT)) {
            return extractComment();
        }
        return new ArrayList<>();
    }

    private static List<String> extractComment() {
        List<String> list = new ArrayList<>();
        list.add("\\\\");
        return list;
    }

    private static List<String> extractMethodEnd() {
        List<String> list = new ArrayList<>();
        list.add("}");
        return list;
    }

    private static List<String> extractAssignment(String line) {
        String[] parts = line.split("(?==)|(?<==)|(?=,)|(?<=,)|(?=;)");
        return getParsedLine(parts);
    }

    private static List<String> extractDeclaration(String line) {
        String[] parts = line.split("(?<=" + TYPE + ")|(?==)|(?<==)|(?=,)|(?<=,)|(?=;)");
        return getParsedLine(parts);
    }

    private static List<String> extractMethodCall(String line) {
        String[] parts = line.split("(?=\\()|(?<=\\()|(?=,)|(?<=,)|(?=\\))|(?=;)");
        return getParsedLine(parts);
    }

    private static List<String> extractMethodDeclaration(String line) {
        String[] parts = line.split("(?<=void)|(?=\\()|(?<=\\()|(?<=" + TYPE + ")|(?=,)|(?<=,)|(?=\\))|(?=\\{)");
        return getParsedLine(parts);
    }

    private static List<String> extractStatement(String line) {
        String[] parts = line.split("(?<=if|while)|(?<=\\()|(?=(&&))|(?=(\\|\\|))|(?<=(&&))|(?<=(\\|\\|))|(?=\\))|(?=\\{)");
        return getParsedLine(parts);
    }

    private static List<String> getParsedLine(String[] parts) {
        List<String> list = new ArrayList<>();
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
        list.add("return");
        list.add(";");
        return list;
    }

}
