
import oop.ex6.vocabulary.SyntaxValidator;
import org.junit.Test;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SyntaxValidatorTest {
    @Test
    public void testGetLine_ifStatement() {
        String input = "   if (  x < y) {";
        List<String> expected = List.of("if", "(", "x < y",")", "{");
        assertEquals(expected, SyntaxValidator.getLine(input));
    }
    @Test
    public void testGetLine_whileStatement() {
        String input = "  while (  x > y  )   {  ";
        List<String> expected = List.of("while", "(", "x > y",")", "{");
        assertEquals(expected, SyntaxValidator.getLine(input));
    }
    @Test
    public void testGetLine_functionStatement() {
        String input = " void a_funcname    ( happy   happy ) {  ";
        List<String> expected = List.of("void", "a_funcname", "(", "happy   happy", ")", "{");
        assertEquals(expected, SyntaxValidator.getLine(input));
    }
    @Test
    public void testGetLine_methodStatement() {
        String input = "  func   ( blah, blah ) ;  ";
        List<String> expected = List.of("func", "(", "blah",",", "blah", ")", ";");
        assertEquals(expected, SyntaxValidator.getLine(input));
    }
    @Test
    public void testGetLine_intDec() {
        String input = "  int   a =  5, b = +7, c ; ";
        List<String> expected = List.of("int", "a", "=", "5", ",", "b", "=", "+7", ",", "c", ";");
        assertEquals(expected, SyntaxValidator.getLine(input));
    }
    @Test
    public void testGetLine_doubleDec() {
        String input = "  double   a =  5.0, b = +7, c ; ";
        List<String> expected = List.of("double", "a", "=", "5.0", ",", "b", "=", "+7", ",", "c", ";");
        assertEquals(expected, SyntaxValidator.getLine(input));
    }
    @Test
    public void testGetLine_StringDec() {
        String input = "  String   a =  \"abc\", b = \"+7\", c ; ";
        List<String> expected = List.of("String", "a", "=", "\"abc\"", ",", "b", "=", "\"+7\"", ",", "c", ";");
        assertEquals(expected, SyntaxValidator.getLine(input));
    }
    @Test
    public void testGetLine_boolDec() {
        String input = "  boolean   a, a =  true, b = 5., c ; ";
        List<String> expected = List.of("boolean", "a",",", "a", "=", "true", ",", "b", "=", "5.", ",", "c", ";");
        assertEquals(expected, SyntaxValidator.getLine(input));
    }
    @Test
    public void testGetLine_charDec() {
        String input = "  char   a, a =  '.', b = '!', c ; ";
        List<String> expected = List.of("char", "a",",", "a", "=", "'.'", ",", "b", "=", "'!'", ",", "c", ";");
        assertEquals(expected, SyntaxValidator.getLine(input));
    }
    @Test
    public void testGetLine_assign() {
        String input = "  a =  '.', b = '!'; ";
        List<String> expected = List.of( "a", "=", "'.'", ",", "b", "=", "'!'", ";");
        assertEquals(expected, SyntaxValidator.getLine(input));
    }
    @Test
    public void testGetLine_invalidStatement() {
        String input = "invalid statement";
        assertTrue(SyntaxValidator.getLine(input).isEmpty());
    }
}
