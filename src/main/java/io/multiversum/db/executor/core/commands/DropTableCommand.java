package io.multiversum.db.executor.core.commands;

import java.math.BigInteger;

import io.multiversum.db.executor.core.CommandQueueExecutor;
import io.multiversum.db.executor.core.commands.results.CommandResult;
import io.multiversum.db.executor.core.commands.util.DatabaseUtility;
import net.sf.jsqlparser.statement.drop.Drop;

public class DropTableCommand extends BaseSqlCommand {

	private Drop statement;
	
	public DropTableCommand(SqlCommand parent, Drop statement) {
		super(parent);
		
		this.statement = statement;
	}

	@Override
	public CommandResult run(CommandQueueExecutor executor) throws Exception {
		BigInteger index = DatabaseUtility.tableIndex(executor, statement.getName().getName());
		
		executor.getContract().dropTable(index).send();
		
		return result();
	}
	
}
