parser grammar VelociraptorParser;

options {
    tokenVocab=VelociraptorLexer;
    superClass=VelociraptorParserBase;
}
program:
     statementList;
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
    : IDENTIFIER('++' | '--')
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
    : primaryExpr                                                              # primaryExpression
    | unaryExpr                                                                # unaryExpression
    | expression op=('*' | '/' | '%' | '<<' | '>>' | '&' | '&^') expression       # multiExpression
    | expression op=('+' | '-' | '|' | '^') expression                            # addSubExpression
    | expression op=('==' | '!=' | '<' | '<=' | '>' | '>=') expression            # compareExpression
    | expression '&&' expression                                               # andExpression
    | expression '||' expression                                               # orExpression
    ;

primaryExpr
    : funcCall
    | operand
    ;
funcCall: IDENTIFIER '(' expressionList ')';
expressionList
    : expression (',' expression)*
    ;
arguments: expressionList;
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
    : 'if' '(' expression ')' block ('else' (ifStmt | block))?
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