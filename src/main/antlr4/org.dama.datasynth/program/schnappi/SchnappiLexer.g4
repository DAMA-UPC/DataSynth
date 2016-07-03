lexer grammar SchnappiLexer;
LPAR : '(';
RPAR : ')';
COMA : ',';
POINT : '.';
COLON : ':';
LBRA : '{';
RBRA : '}';
MAPKW : 'map';
REDUCEKW : 'reduce';
INIT : 'new';
EQJOIN : 'eqjoin';
UNION : 'union';
GENID : 'gId';
EQ : '=';
PLUS : '+' ;
MINUS : '-' ;
STAR : '*';
FSLASH : '/';
VTYPE : ATTR | ENTITY | RELATION;
ATTR : 'attribute';
ENTITY : 'entity';
RELATION : 'relation';
SOURCE : '@source';
TARGET : '@target';
SIGNATURE : 'signature';
BINDINGS : 'bindings';
PROGRAM : 'program';
ID  : [a-zA-Z][a-zA-Z0-9]* ;
NUM : [1-9][0-9]* ;
WS  : [ \t\r\n]+ -> skip ;