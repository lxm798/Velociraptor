grammar VelociraptorParser;

options {
    tokenVocab=VelociraptorLexer;
    superClass=VelociraptorParserBase;
}
program:
     statement;
statement
    :simpleStmt
    | returnStmt
    | block
    | ifStmt
    | switchStmt
    | forStmt
    ;
returnStmt
    : 'return' expression
    ;
switchStmt : 'switch' (simpleStmt ';')? expression? '{' exprCaseClause* '}'
;

exprCaseClause
    : exprSwitchCase ':' statementList?
    ;
exprSwitchCase
    : 'case' expressionList
    | 'default'
    ;

simpleStmt
    : expressionStmt
    | incDecStmt
    | assignment
    | emptyStmt
    ;
incDecStmt
    : expression ('++' | '--')
    ;

assignment
    : IDENTIFIER '=' expression
    ;

expressionStmt
    : expression
    ;

assign_op
    : ('+' | '-' | '|' | '^' | '*' | '/' | '%' | '<<' | '>>' | '&' | '&^')? '='
    ;
emptyStmt
    : ';'
    ;

expression
    : primaryExpr
    | unaryExpr
    | expression ('*' | '/' | '%' | '<<' | '>>' | '&' | '&^') expression
    | expression ('+' | '-' | '|' | '^') expression
    | expression ('==' | '!=' | '<' | '<=' | '>' | '>=') expression
    | expression '&&' expression
    | expression '||' expression
    ;

primaryExpr
    : operand
    ;
expressionList
    : expression
    ;
unaryExpr
    : primaryExpr
    | ('-' | '!') expression
    ;

literal
    : basicLit
    | functionLit
    ;
basicLit
    : integer
    | string_
    | FLOAT_LIT
    ;
operand
    : literal
    | IDENTIFIER
    | '(' expression ')'
    ;

integer
    : DECIMAL_LIT
    | OCTAL_LIT
    | HEX_LIT
    ;
string_
    : RAW_STRING_LIT
    ;
block
    : '{' statementList? '}'
    ;
ifStmt
    : 'if' (simpleStmt)? expression block ('else' (ifStmt | block))?
    ;
forStmt
    : 'for' (simpleStmt? ';' expression? ';' simpleStmt?)? block
    ;
statementList
    : (statement eos)+
    ;
eos
    : ';'
    | EOF
    | {lineTerminatorAhead()}?
    | {checkPreviousTokenText("}")}?
    ;
functionLit
    : 'func' IDENTIFIER '(' identifierList ')'  block // function
    ;
identifierList
          : IDENTIFIER? (',' IDENTIFIER)*
          ;