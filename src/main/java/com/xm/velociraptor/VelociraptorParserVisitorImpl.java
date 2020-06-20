package com.xm.velociraptor;

import antlr4.VelociraptorParser;
import antlr4.VelociraptorParserBaseVisitor;
import com.xm.velociraptor.enums.TypeEnums;
import com.xm.velociraptor.utils.Asserts;
import com.xm.velociraptor.utils.ScopeContextUtils;

import java.util.List;
import java.util.Stack;

public class VelociraptorParserVisitorImpl extends VelociraptorParserBaseVisitor<VelociraptorValue> {
    private Stack<ScopeContext> scopeContexts = new Stack<ScopeContext>();

    public VelociraptorValue visitProgram(VelociraptorParser.ProgramContext ctx) {
        scopeContexts.push(new ScopeContext());
        return visitStatementList(ctx.statementList());
    }

    @Override
    public VelociraptorValue visitStatementList(VelociraptorParser.StatementListContext ctx) {
        List<VelociraptorParser.StatementContext> contexts = ctx.statement();
        VelociraptorValue ret = null;
        for (VelociraptorParser.StatementContext context : contexts) {
            ret = visitStatement(context);
            if (ret.isReturn()) {
                return ret;
            }
        }
        return ret;
    }

    public VelociraptorValue visitStatement(VelociraptorParser.StatementContext ctx) {
        if (ctx.block() != null) {
            return visitBlock(ctx.block());
        } else if (ctx.forStmt() != null) {
            return visitForStmt(ctx.forStmt());
        } else if (ctx.ifStmt() != null) {
            return visitIfStmt(ctx.ifStmt());
        } else if (ctx.switchStmt() != null) {
            return visitSwitchStmt(ctx.switchStmt());
        } else if (ctx.returnStmt() != null) {
            return visitReturnStmt(ctx.returnStmt());
        } else if (ctx.simpleStmt() != null) {
            return visitSimpleStmt(ctx.simpleStmt());
        }
        return visitChildren(ctx);
    }

    @Override
    public VelociraptorValue visitIfStmt(VelociraptorParser.IfStmtContext ctx) {
        VelociraptorBool value = (VelociraptorBool)
                visit(ctx.expression());
        if (!value.getValue()) {
            if (ctx.ifStmt() != null) {
                return visitIfStmt(ctx.ifStmt());
            } else if (ctx.block(1) != null) {
                return visitBlock(ctx.block(1));
            }
        }
        return visitBlock(ctx.block(0));
    }

    public VelociraptorValue visitReturnStmt(VelociraptorParser.ReturnStmtContext ctx) {
        VelociraptorValue ret = visit(ctx.expression());
        ret.setReturn(true);
        return ret;
    }

    public VelociraptorValue visitSwitchStmt(VelociraptorParser.SwitchStmtContext ctx) {
        return visitChildren(ctx);
    }

    public VelociraptorValue visitExprCaseClause(VelociraptorParser.ExprCaseClauseContext ctx) {
        return visitChildren(ctx);
    }

    public VelociraptorValue visitExprSwitchCase(VelociraptorParser.ExprSwitchCaseContext ctx) {
        return visitChildren(ctx);
    }

    public VelociraptorValue visitSimpleStmt(VelociraptorParser.SimpleStmtContext ctx) {
        if (ctx.emptyStmt() != null) {
            return visitChildren(ctx);
        } else if (ctx.assignment() != null) {
            return visitAssignment(ctx.assignment());
        } else if (ctx.incDecStmt() != null) {
            return visitIncDecStmt(ctx.incDecStmt());
        } else if (ctx.expressionStmt() != null) {
            return visitExpressionStmt(ctx.expressionStmt());
        }
        return visitChildren(ctx);
    }

    public VelociraptorValue visitIncDecStmt(VelociraptorParser.IncDecStmtContext ctx) {
        String var = ctx.IDENTIFIER().getText();
        VelociraptorValue value = ScopeContextUtils.getValue(scopeContexts, var);
        if (value == null) {
            value = new VelociraptorInteger();
            value.setValue(1);
            ScopeContextUtils.addValue(scopeContexts, var, value);
        } else {
            if (value.getType() == TypeEnums.Null) {
                value.setValue(1);
            } else {
                Asserts.assertEqual(value.getType(), TypeEnums.Integer);
                value.setValue((Integer) value.getValue() + 1);
            }
        }
        return value;
    }

    public VelociraptorValue visitAssignment(VelociraptorParser.AssignmentContext ctx) {
        String var = ctx.IDENTIFIER().getText();
        VelociraptorValue expressionValue = visit(ctx.expression());
        VelociraptorValue value = ScopeContextUtils.getValue(scopeContexts, var);
        if (value != null) {
            value.setValue(expressionValue.getValue());
        } else {
            value = expressionValue;
            ScopeContextUtils.addValue(scopeContexts, var, value);
        }
        return value;
    }

    public VelociraptorValue visitExpressionStmt(VelociraptorParser.ExpressionStmtContext ctx) {
        return visit(ctx.expression());
    }

    public VelociraptorValue visitAssign_op(VelociraptorParser.Assign_opContext ctx) {
        return visitChildren(ctx);
    }

    public VelociraptorValue visitEmptyStmt(VelociraptorParser.EmptyStmtContext ctx) {
        return visitChildren(ctx);
    }

    public VelociraptorValue visitPrimaryExpression(VelociraptorParser.PrimaryExpressionContext ctx) {
        return visitPrimaryExpr(ctx.primaryExpr());
    }

    @Override
    public VelociraptorValue visitCompareExpression(VelociraptorParser.CompareExpressionContext ctx) {
        VelociraptorValue left = visit(ctx.expression(0));
        VelociraptorValue right = visit(ctx.expression(1));

        if (ctx.op.getText().equals("==")) {
            return left.equals(right);
        }
        return null;
    }

    public VelociraptorValue visitOrExpression(VelociraptorParser.OrExpressionContext ctx) {
        VelociraptorBool ret = new VelociraptorBool();
        VelociraptorBool left = (VelociraptorBool) visit(ctx.expression(0));
        if (left.getValue()) {
            ret.setValue(true);
            return ret;
        }
        VelociraptorBool right = (VelociraptorBool) visit(ctx.expression(1));
        ret.setValue(right.getValue());
        return ret;
    }

    public VelociraptorValue visitAndExpression(VelociraptorParser.AndExpressionContext ctx) {
        VelociraptorBool ret = new VelociraptorBool();
        VelociraptorBool left = (VelociraptorBool) visit(ctx.expression(0));
        Asserts.assertEqual(left.getType(), TypeEnums.Boolean);
        if (!left.getValue()) {
            ret.setValue(false);
            return ret;
        }
        VelociraptorBool right = (VelociraptorBool) visit(ctx.expression(1));
        ret.setValue(right.getValue());
        return ret;
    }

    public VelociraptorValue visitAddSubExpression(VelociraptorParser.AddSubExpressionContext ctx) {
        VelociraptorValue left = visit(ctx.expression(0));
        VelociraptorValue right = visit(ctx.expression(1));

        if (ctx.op.getText().equals("+")) {
            return left.add(right);
        } else if (ctx.op.getText().equals("-")) {
            return left.sub(right);
        } else if (ctx.op.getText().equals("|")) {
            return left.bitOr(right);
        } else if (ctx.op.getText().equals("^")) {
            return left.bitAnd(right);
        }
        return visitChildren(ctx);
    }

    public VelociraptorValue visitMultiExpression(VelociraptorParser.MultiExpressionContext ctx) {
        VelociraptorValue left = visit(ctx.expression(0));
        VelociraptorValue right = visit(ctx.expression(1));

        if (ctx.op.getText().equals("*")) {
            return left.multi(right);
        } else if (ctx.op.getText().equals("/")) {
            return left.div(right);
        } else if (ctx.op.getText().equals("%")) {
            return left.mod(right);
        }
        return visitChildren(ctx);
    }

    public VelociraptorValue visitUnaryExpression(VelociraptorParser.UnaryExpressionContext ctx) {
        return visitChildren(ctx);
    }

    public VelociraptorValue visitPrimaryExpr(VelociraptorParser.PrimaryExprContext ctx) {
        if (ctx.operand() != null) {
            return visitOperand(ctx.operand());
        } else if (ctx.funcCall() != null) {
            return visitFuncCall(ctx.funcCall());
        }
        return null;
    }
    @Override
    public VelociraptorValue visitFuncCall(VelociraptorParser.FuncCallContext ctx) {
        String funcName = ctx.IDENTIFIER().getText();
        VelociraptorFunc func =
                (VelociraptorFunc)ScopeContextUtils.getValue(scopeContexts, funcName);
        ScopeContext scopeContext = new ScopeContext();
        scopeContexts.push(scopeContext);
        List<String> idList = func.getIdList();
        VelociraptorParser.ExpressionListContext expressionListContext
                = ctx.expressionList();

        if (idList.size() != expressionListContext.expression().size()) {
            throw new RuntimeException("parameter length not equal with sign");
        }
        int i = 0;
        for (String id : idList) {
            ScopeContextUtils
                    .addValue(scopeContexts, id,
                            visit(expressionListContext.expression(i)));
            ++i;
        }
        VelociraptorValue ret = func.call();
        scopeContexts.pop();
        return ret;
    }

    public VelociraptorValue visitExpressionList(VelociraptorParser.ExpressionListContext ctx) {
        return visitChildren(ctx);
    }

    public VelociraptorValue visitUnaryExpr(VelociraptorParser.UnaryExprContext ctx) {
        return visitChildren(ctx);
    }

    public VelociraptorValue visitLiteral(VelociraptorParser.LiteralContext ctx) {
        if (ctx.basicLit() != null) {
            return visitBasicLit(ctx.basicLit());
        } else if (ctx.functionLit() != null) {
            return visitFunctionLit(ctx.functionLit());
        }
        return visitChildren(ctx);
    }

    public VelociraptorValue visitBasicLit(VelociraptorParser.BasicLitContext ctx) {
        if (ctx.FLOAT_LIT() != null) {
            VelociraptorFloat velociraptorValue = new VelociraptorFloat();
            Float v = Float.parseFloat(ctx.getText());
            velociraptorValue.setValue(v);
            return velociraptorValue;
        } else if (ctx.integer() != null) {
            VelociraptorInteger velociraptorValue = new VelociraptorInteger();
            Integer v = Integer.parseInt(ctx.getText());
            velociraptorValue.setValue(v);
            return velociraptorValue;
        } else if (ctx.string_() != null) {
            return visitString_(ctx.string_());
        }
        return visitChildren(ctx);
    }

    @Override
    public VelociraptorValue visitFunctionLit(VelociraptorParser.FunctionLitContext ctx) {
        String funcName = ctx.IDENTIFIER().getText();
        VelociraptorParser.BlockContext blockContext = ctx.block();
        VelociraptorFunc func = new VelociraptorFunc(this,
                ctx.block(), ctx.identifierList());
        ScopeContextUtils.addValue(scopeContexts, funcName, func);
        return func;
    }

    public VelociraptorValue visitOperand(VelociraptorParser.OperandContext ctx) {
        try {
            String s = ctx.getText();
            System.out.println(ctx.getText());
        } catch (Throwable t) {
            t.printStackTrace();
        }
        if (ctx.literal() != null) {
            return visitLiteral(ctx.literal());
        } else if (ctx.IDENTIFIER() != null) {
            return ScopeContextUtils.getValue(scopeContexts, ctx.IDENTIFIER().getText());
        } else {
            return visit(ctx.expression());
        }
    }

    public VelociraptorValue visitInteger(VelociraptorParser.IntegerContext ctx) {
        return visitChildren(ctx);
    }

    public VelociraptorValue visitString_(VelociraptorParser.String_Context ctx) {
        VelociraptorString velociraptorString = new VelociraptorString();
        velociraptorString.setValue(ctx.RAW_STRING_LIT().getText());
        return velociraptorString;
    }

    public VelociraptorValue visitBlock(VelociraptorParser.BlockContext ctx) {
        return visitStatementList(ctx.statementList());
    }
}