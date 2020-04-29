package io.multiversum.db.executor.core;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.Statements;

public class Parser {
	private static SqlStatementVisitor statementVisitor = new SqlStatementVisitor();
	
	public static void parse(String sql) {
		Statements statements = null;
		try {
			statements = CCJSqlParserUtil.parseStatements(sql);
		} catch (JSQLParserException e) {
			// TODO throw custom exception
			e.printStackTrace();
		}
		
		if (statements == null) {
			return;
		}
		
		for (Statement statement : statements.getStatements()) {
			statement.accept(statementVisitor);
		}
	}
}
