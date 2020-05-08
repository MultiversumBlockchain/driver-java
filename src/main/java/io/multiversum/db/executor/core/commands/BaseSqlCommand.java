package io.multiversum.db.executor.core.commands;

import io.multiversum.db.executor.core.CommandQueueExecutor;
import io.multiversum.db.executor.core.commands.results.CommandResult;

public class BaseSqlCommand implements SqlCommand {

	protected SqlCommand parent = null;
	
	public BaseSqlCommand(SqlCommand parent) {
		this.parent = parent;
	}
	
	@Override
	public SqlCommand parent() {
		return parent;
	}
	
	@Override
	public CommandResult run(CommandQueueExecutor executor) throws Exception {
		return result();
	}
	
	protected CommandResult result() {
		return new CommandResult();
	}
	
}
