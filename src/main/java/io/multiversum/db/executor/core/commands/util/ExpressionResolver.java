package io.multiversum.db.executor.core.commands.util;

import java.util.Stack;

import io.multiversum.db.executor.core.util.StringUtils;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.arithmetic.Addition;
import net.sf.jsqlparser.expression.operators.arithmetic.Division;
import net.sf.jsqlparser.expression.operators.arithmetic.Multiplication;
import net.sf.jsqlparser.expression.operators.arithmetic.Subtraction;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.ComparisonOperator;
import net.sf.jsqlparser.expression.operators.relational.OldOracleJoinBinaryExpression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;

public class ExpressionResolver extends ExpressionDeParser {
	final Stack<ColumnResult> stack = new Stack<ColumnResult>();
	
	private int depth = 0;
	private final boolean isWhere;
	
	public ExpressionResolver() {
		isWhere = true;
	}
	
	public ExpressionResolver(boolean isWhere) {
		this.isWhere = isWhere;
	}
	
	public ColumnResult getResult() {
		return stack.pop();
	}
	
	@Override
	public void visit(Addition addition) {
		super.visit(addition);
		
		double first = Double.parseDouble(stack.pop().getValue());
		double second = Double.parseDouble(stack.pop().getValue());

		String value = "" + (first + second);
		
		stack.push(new ColumnResult(value, null, false));
	}
	
	@Override
	public void visit(Subtraction subtraction) {
		super.visit(subtraction);
		
		double first = Double.parseDouble(stack.pop().getValue());
		double second = Double.parseDouble(stack.pop().getValue());
		
		String value = "" + (second - first);
		
		stack.push(new ColumnResult(value, null, false));
	}
	
	@Override
	public void visit(Multiplication multiplication) {
		super.visit(multiplication);
		
		double first = Double.parseDouble(stack.pop().getValue());
		double second = Double.parseDouble(stack.pop().getValue());
		
		String value = "" + (first * second);
		
		stack.push(new ColumnResult(value, null, false));
	}
	
	@Override
	public void visit(Division division) {
		super.visit(division);
		
		double first = Double.parseDouble(stack.pop().getValue());
		double second = Double.parseDouble(stack.pop().getValue());
		
		String value = "" + (second / first);
		
		stack.push(new ColumnResult(value, null, false));
	}
	
	@Override
	public void visit(LongValue longValue) {
		super.visit(longValue);
		
		String value = "" + longValue.getValue();
		
		stack.push(new ColumnResult(value, null, false));
	}
	
	@Override
	public void visit(StringValue stringValue) {
		super.visit(stringValue);
		
		String value = stringValue.getValue();
		
		stack.push(new ColumnResult(value, null, false));
	}
	
	@Override
	public void visit(DoubleValue doubleValue) {
		super.visit(doubleValue);
		
		String value = "" + doubleValue.getValue();
		
		stack.push(new ColumnResult(value, null, false));
	}
	
	@Override
	public void visit(SubSelect subSelect) {
		super.visit(subSelect);
		
		throw new UnsupportedOperationException("sub select in expression list in not supported");
	}
	
	@Override
	public void visit(Column tableColumn) {
		super.visit(tableColumn);
		
		String name = tableColumn.getName(true);
		
		stack.push(new ColumnResult(null, name, true));
	}
	
	@Override
	protected void visitBinaryExpression(BinaryExpression expr, String operator) {
		if (expr instanceof ComparisonOperator) {
			System.out.println(StringUtils.repeat("-", depth) + 
	                "left: " + expr.getLeftExpression() + 
	                "  op: " +  expr.getStringExpression() +
	                "  right: " + expr.getRightExpression() );
		}
		
		super.visitBinaryExpression(expr, operator);
	}
	
	@Override
	public void visitOldOracleJoinBinaryExpression(OldOracleJoinBinaryExpression expr, String operator) {
		if (expr instanceof ComparisonOperator) {
			System.out.println(StringUtils.repeat("-", depth) + 
	                "left: " + expr.getLeftExpression() + 
	                "  op: " +  expr.getStringExpression() +
	                "  right: " + expr.getRightExpression() );
		}
		
		super.visitOldOracleJoinBinaryExpression(expr, operator);
	}
	
	@Override
	public void visit(AndExpression andExpression) {
		processLogicalExpression(andExpression, "AND");
	}
	
	@Override
	public void visit(OrExpression orExpression) {
		processLogicalExpression(orExpression, "OR");
	}
	
	@Override
	public void visit(Parenthesis parenthesis) {
		if (isWhere) {
			parenthesis.getExpression().accept(this);
		} else {
			super.visit(parenthesis);
		}
	}
	
	private void processLogicalExpression(BinaryExpression expr, String operator) {
		System.out.println(StringUtils.repeat("-", depth) + operator);
		
		depth++;
		expr.getLeftExpression().accept(this);
		expr.getRightExpression().accept(this);
		if (depth > 0) {
			depth--;
		}
	}
}
