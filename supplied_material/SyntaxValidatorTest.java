
import oop.ex6.vocabulary.SyntaxValidator;
import org.junit.Test;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SyntaxValidatorTest {
    @Test
    public void testGetLine_Statement() {
        String input = " if ( x || y ) { ";
        List<String> expected = List.of("if", "(", "x", "||", "y",")", "{");
        assertEquals(expected, SyntaxValidator.getLine(input));
        input = "if(x||y){";
        expected = List.of("if", "(", "x", "||", "y",")", "{");
        assertEquals(expected, SyntaxValidator.getLine(input));
        input = "if (x&&y) {";
        expected = List.of("if", "(", "x", "&&", "y",")", "{");
        assertEquals(expected, SyntaxValidator.getLine(input));
        input = "if (x) {";
        expected = List.of("if", "(", "x",")", "{");
        assertEquals(expected, SyntaxValidator.getLine(input));
        input = "while (.2) {";
        expected = List.of("while", "(", ".2",")", "{");
        assertEquals(expected, SyntaxValidator.getLine(input));
        input = "if (true) {";
        expected = List.of("if", "(", "true",")", "{");
        assertEquals(expected, SyntaxValidator.getLine(input));
        input = "while (false) {";
        expected = List.of("while", "(", "false",")", "{");
        assertEquals(expected, SyntaxValidator.getLine(input));
        input = " if( x  &&  y ){ ";
        expected = List.of("if", "(", "x", "&&", "y",")", "{");
        assertEquals(expected, SyntaxValidator.getLine(input));
        input = " if( x  &&y|| .2){ ";
        expected = List.of("if", "(", "x", "&&", "y", "||", ".2",")", "{");
        assertEquals(expected, SyntaxValidator.getLine(input));
        input = " if( x  &&|| a){ ";
        expected = List.of();
        assertEquals(expected, SyntaxValidator.getLine(input));
        input = " if( ){ ";
        expected = List.of();
        assertEquals(expected, SyntaxValidator.getLine(input));
        input = " while ( || a)   { ";
        expected = List.of();
        assertEquals(expected, SyntaxValidator.getLine(input));
        input = "if  (a &&){ ";
        expected = List.of();
        assertEquals(expected, SyntaxValidator.getLine(input));
    }

    @Test
    public void testGetLine_functionStatement() {
        String input = " void func(int a,   boolean   a  ,char a,String a , double a){  ";
        List<String> expected = List.of("void", "func", "(", "int", "a", ",", "boolean", "a", ",",
                "char", "a", ",", "String", "a", ",", "double", "a", ")", "{");
        assertEquals(expected, SyntaxValidator.getLine(input));
        input = " void func(){";
        expected = List.of("void", "func", "(", ")", "{");
        assertEquals(expected, SyntaxValidator.getLine(input));
        input = "void func_a ( ) {";
        expected = List.of("void", "func_a", "(", ")", "{");
        assertEquals(expected, SyntaxValidator.getLine(input));
        input = "void func ( int) {";
        expected = List.of();
        assertEquals(expected, SyntaxValidator.getLine(input));
        input = "void func ( var) {";
        expected = List.of();
        assertEquals(expected, SyntaxValidator.getLine(input));
        input = "void func ( int ,) {";
        expected = List.of();
        assertEquals(expected, SyntaxValidator.getLine(input));
        input = "void func ( , int) {";
        expected = List.of();
        assertEquals(expected, SyntaxValidator.getLine(input));
        input = "void func ( int .. ) {";
        expected = List.of();
        assertEquals(expected, SyntaxValidator.getLine(input));
    }
    @Test
    public void testGetLine_return() {
        String input = "  return ;  ";
        List<String> expected = List.of("return", ";");
        assertEquals(expected, SyntaxValidator.getLine(input));
        input = "return;";
        expected = List.of("return", ";");
        assertEquals(expected, SyntaxValidator.getLine(input));
        input = "return  ;";
        expected = List.of("return", ";");
        assertEquals(expected, SyntaxValidator.getLine(input));
        input = "return;  ";
        expected = List.of("return", ";");
        assertEquals(expected, SyntaxValidator.getLine(input));
        input = "return  ";
        expected = List.of();
        assertEquals(expected, SyntaxValidator.getLine(input));
    }
    @Test
    public void testGetLine_closeBrackets() {
        String input = "  }  ";
        List<String> expected = List.of("}");
        assertEquals(expected, SyntaxValidator.getLine(input));
        input = "}";
        expected = List.of("}");
        assertEquals(expected, SyntaxValidator.getLine(input));
        input = " }";
        expected = List.of("}");
        assertEquals(expected, SyntaxValidator.getLine(input));
        input = "} ";
        expected = List.of("}");
        assertEquals(expected, SyntaxValidator.getLine(input));
        input = "; }  ";
        expected = List.of();
        assertEquals(expected, SyntaxValidator.getLine(input));
    }
    @Test
    public void testGetLine_methodStatement() {
        String input = "func ( a, b ) ;  ";
        List<String> expected = List.of("func", "(", "a",",", "b", ")", ";");
        assertEquals(expected, SyntaxValidator.getLine(input));
        input = "func ( a, b ) ;  ";
        expected = List.of("func", "(", "a",",", "b", ")", ";");
        assertEquals(expected, SyntaxValidator.getLine(input));
        input = "func( a, b );";
        expected = List.of("func", "(", "a",",", "b", ")", ";");
        assertEquals(expected, SyntaxValidator.getLine(input));
        input = "func( );";
        expected = List.of("func", "(", ")", ";");
        assertEquals(expected, SyntaxValidator.getLine(input));
        input = "func ( a, ) ;  ";
        expected = List.of();
        assertEquals(expected, SyntaxValidator.getLine(input));
        input = "func ( , b ) ;  ";
        expected = List.of();
        assertEquals(expected, SyntaxValidator.getLine(input));
        input = "func ( , ) ;  ";
        expected = List.of();
        assertEquals(expected, SyntaxValidator.getLine(input));
        input = "func () ;  ";
        expected = List.of("func", "(", ")", ";");
        assertEquals(expected, SyntaxValidator.getLine(input));
    }
    @Test
    public void testGetLine_comment() {
        String input = "\\\\ comment ";
        List<String> expected = List.of("\\\\");
        assertEquals(expected, SyntaxValidator.getLine(input));
        input = "  \\\\  ";
        expected = List.of();
        assertEquals(expected, SyntaxValidator.getLine(input));
    }
    @Test
    public void testGetLine_intDec() {
        String input = "  int   a =  5 ,b=+7, c ; ";
        List<String> expected = List.of("int", "a", "=", "5", ",", "b", "=", "+7", ",", "c", ";");
        assertEquals(expected, SyntaxValidator.getLine(input));
    }
    @Test
    public void testGetLine_doubleDec() {
        String input = "double   a =5.0 ,b = +7, c;";
        List<String> expected = List.of("double", "a", "=", "5.0", ",", "b", "=", "+7", ",", "c", ";");
        assertEquals(expected, SyntaxValidator.getLine(input));
    }
    @Test
    public void testGetLine_StringDec() {
        String input = "  String   a=\"abc\", b = \"+7\" ,c ; ";
        List<String> expected = List.of("String", "a", "=", "\"abc\"", ",", "b", "=", "\"+7\"", ",", "c", ";");
        assertEquals(expected, SyntaxValidator.getLine(input));
    }
    @Test
    public void testGetLine_boolDec() {
        String input = "  boolean   a, a=true, b = 5. , c ; ";
        List<String> expected = List.of("boolean", "a",",", "a", "=", "true", ",", "b", "=", "5.", ",", "c", ";");
        assertEquals(expected, SyntaxValidator.getLine(input));
    }
    @Test
    public void testGetLine_charDec() {
        String input = "  char   a,a='.', b = '!', c ; ";
        List<String> expected = List.of("char", "a",",", "a", "=", "'.'", ",", "b", "=", "'!'", ",", "c", ";");
        assertEquals(expected, SyntaxValidator.getLine(input));
    }
    @Test
    public void testGetLine_assign() {
        String input = "  a='.', b=7; ";
        List<String> expected = List.of( "a", "=", "'.'", ",", "b", "=", "7", ";");
        assertEquals(expected, SyntaxValidator.getLine(input));
    }
    @Test
    public void testGetLine_invalidStatement() {
        String input = "invalid statement";
        assertTrue(SyntaxValidator.getLine(input).isEmpty());
    }

}
