/* Grammar Rules */
parser grammar WMLParser;

options { tokenVocab=WMLLexer; }

document    :   misc* tag misc* EOF;

content     :   chardata?
                 ((tag | COMMENT) chardata?)* ;


tag         :   OPEN Name CLOSE content OPEN SLASH Name CLOSE ;

chardata    :   TEXT | SEA_WS ;

misc        :   COMMENT | SEA_WS ;

