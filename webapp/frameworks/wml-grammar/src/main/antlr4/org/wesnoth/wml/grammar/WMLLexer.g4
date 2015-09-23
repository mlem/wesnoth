lexer grammar WMLLexer;

tokens { STRING }

/* Lexer Rules */

COMMENT     :   '# ' .+? ('\n'|EOF)     -> skip ;

SEA_WS      :   (' '|'\t'|'\r'? '\n')+ ;

OPEN        :   '['                     -> pushMode(INSIDE) ;

TEXT        :   ~[\[]+ ;                // match any char other than [

EQUALS      :   '=' ;

STRING      :   '"' ('\\' ('b'|'t'|'n'|'f'|'r'|'"'|'\''|'\\') | ~['\\'|'"'] )* '"';


mode INSIDE;

CLOSE       :   ']'                     -> popMode ;

SLASH       :   '/' ;

S           :   [ \t\r\n]               -> skip ;

Name : ('a'..'z'|'A'..'Z'|'0'..'9'|'_'|'-'|',')+;





