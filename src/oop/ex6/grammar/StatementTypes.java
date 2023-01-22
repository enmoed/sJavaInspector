package oop.ex6.grammar;

import java.util.Hashtable;

/**
 * all the statement types.
 */
public enum StatementTypes {
    VAR_DEC,
    METHOD_CALL,
    METHOD_DEC,
    IF_CALL,
    WHILE_CALL,
    VAR_ASSIGN,
    RETURN,
    END_OF_BLOCK,
    COMMENT;
    public static Hashtable<String, StatementTypes> stringMapper = new Hashtable<>() {
        {
            put("VAR_DEC", VAR_DEC);
            put("METHOD_CALL", METHOD_CALL);
            put("IF_CALL", IF_CALL);
            put("WHILE_CALL", WHILE_CALL);
            put("VAR_ASSIGN", VAR_ASSIGN);
            put("RETURN", RETURN);
            put("METHOD_DEC", METHOD_DEC);
            put("COMMENT", COMMENT);
            put("END_OF_BLOCK", END_OF_BLOCK);
        }
    };
}
