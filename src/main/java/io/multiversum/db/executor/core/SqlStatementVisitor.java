package io.multiversum.db.executor.core;

import java.util.List;

import io.multiversum.db.executor.core.commands.CommandStack;
import io.multiversum.db.executor.core.commands.CreateTableCommand;
import io.multiversum.db.executor.core.commands.DeleteFromCommand;
import io.multiversum.db.executor.core.commands.DropTableCommand;
import io.multiversum.db.executor.core.commands.InsertCommand;
import io.multiversum.db.executor.core.commands.SelectCommand;
import io.multiversum.db.executor.core.commands.ShowTablesCommand;
import io.multiversum.db.executor.core.commands.SqlCommand;
import io.multiversum.db.executor.core.commands.UpdateCommand;
import io.multiversum.db.executor.core.commands.util.InsertValuesVisitor;
import io.multiversum.db.executor.core.commands.util.SelectResolver;
import net.sf.jsqlparser.statement.Block;
import net.sf.jsqlparser.statement.Commit;
import net.sf.jsqlparser.statement.DeclareStatement;
import net.sf.jsqlparser.statement.DescribeStatement;
import net.sf.jsqlparser.statement.ExplainStatement;
import net.sf.jsqlparser.statement.SetStatement;
import net.sf.jsqlparser.statement.ShowColumnsStatement;
import net.sf.jsqlparser.statement.ShowStatement;
import net.sf.jsqlparser.statement.StatementVisitor;
import net.sf.jsqlparser.statement.Statements;
import net.sf.jsqlparser.statement.UseStatement;
import net.sf.jsqlparser.statement.alter.Alter;
import net.sf.jsqlparser.statement.comment.Comment;
import net.sf.jsqlparser.statement.create.index.CreateIndex;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.create.view.AlterView;
import net.sf.jsqlparser.statement.create.view.CreateView;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.drop.Drop;
import net.sf.jsqlparser.statement.execute.Execute;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.merge.Merge;
import net.sf.jsqlparser.statement.replace.Replace;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.truncate.Truncate;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.statement.upsert.Upsert;
import net.sf.jsqlparser.statement.values.ValuesStatement;

public class SqlStatementVisitor implements StatementVisitor {

	private SqlCommand lastCommand = null;
	
	@Override
	public void visit(Comment comment) {
		// Don't do anything
	}

	@Override
	public void visit(Commit commit) {
		// Don't do anything
	}

	@Override
	public void visit(Delete delete) {
		push(new DeleteFromCommand(parent(), delete));
	}

	@Override
	public void visit(Update update) {
		push(new UpdateCommand(parent(), update));
	}

	@Override
	public void visit(Insert insert) {
		if (insert.getSelect() != null) {
			insert.getSelect().accept(this);
		} else {
			InsertValuesVisitor visitor = new InsertValuesVisitor();
			insert.getItemsList().accept(visitor);
		}

		push(new InsertCommand(parent(), insert));
	}

	@Override
	public void visit(Replace replace) {
		// Don't do anything
	}

	@Override
	public void visit(Drop drop) {
		push(new DropTableCommand(parent(), drop));
	}

	@Override
	public void visit(Truncate truncate) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(CreateIndex createIndex) {
		// Don't do anything
	}

	@Override
	public void visit(CreateTable createTable) {
		String name = createTable.getTable().getName();
		List<ColumnDefinition> columns = createTable.getColumnDefinitions();
		
		push(new CreateTableCommand(parent(), name, createTable.isIfNotExists(), columns));
	}

	@Override
	public void visit(CreateView createView) {
		// Don't do anything		
	}

	@Override
	public void visit(AlterView alterView) {
		// Don't do anything		
	}

	@Override
	public void visit(Alter alter) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(Statements stmts) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(Execute execute) {
		// Don't do anything
	}

	@Override
	public void visit(SetStatement set) {
		// Don't do anything
	}

	@Override
	public void visit(ShowColumnsStatement set) {
		// Don't do anything
	}

	@Override
	public void visit(Merge merge) {
		// Don't do anything
	}

	@Override
	public void visit(Select select) {
		select.getSelectBody().accept(new SelectResolver());
		
		push(new SelectCommand(parent(), select));
	}

	@Override
	public void visit(Upsert upsert) {
		// Don't do anything
	}

	@Override
	public void visit(UseStatement use) {
		// Don't do anything
	}

	@Override
	public void visit(Block block) {
		// Don't do anything		
	}

	@Override
	public void visit(ValuesStatement values) {
		// Don't do anything
	}

	@Override
	public void visit(DescribeStatement describe) {
		// Don't do anything
	}

	@Override
	public void visit(ExplainStatement aThis) {
		// Don't do anything
	}

	@Override
	public void visit(ShowStatement show) {
		if (!show.getName().equalsIgnoreCase("tables")) {
			return;
		}
		
		push(new ShowTablesCommand(parent()));
	}

	@Override
	public void visit(DeclareStatement aThis) {
		// Don't do anything
	}
	
	private SqlCommand parent() {
		return lastCommand;
	}
	
	private void push(SqlCommand command) {
		CommandStack.push(command);
		
		lastCommand = command;
	}

}
