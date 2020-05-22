package io.multiversum.db.executor.core.commands;

import java.math.BigInteger;

import io.multiversum.db.executor.core.CommandQueueExecutor;
import io.multiversum.db.executor.core.commands.results.CommandResult;
import io.multiversum.db.executor.core.commands.util.DatabaseUtility;
import io.multiversum.db.executor.core.commands.util.RowIndexResolver;
import net.sf.jsqlparser.statement.delete.Delete;

public class DeleteFromCommand extends BaseSqlCommand {

	private Delete statement;
	
	public DeleteFromCommand(SqlCommand parent, Delete statement) {
		super(parent);
		
		this.statement = statement;
	}

	@Override
	public CommandResult run(CommandQueueExecutor executor) throws Exception {
		RowIndexResolver indexResolver = new RowIndexResolver();
		
		statement.getWhere().accept(indexResolver);
		
		if (indexResolver.getIndex() == null) {
			throw new UnsupportedOperationException("You must specify a row index");
		}
		
		BigInteger rowIndex = indexResolver.getIndex();
		BigInteger tableIndex = DatabaseUtility.tableIndex(executor, statement.getTable().getName());
		
		executor.getContract().deleteDirect(tableIndex, rowIndex).send();
		
		return result();
	}
	
}
