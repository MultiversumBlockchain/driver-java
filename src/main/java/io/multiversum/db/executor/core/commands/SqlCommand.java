package io.multiversum.db.executor.core.commands;

import io.multiversum.db.executor.core.CommandQueueExecutor;

public interface SqlCommand {

	public SqlCommand parent();
	
	public CommandResult<?> run(CommandQueueExecutor executor) throws Exception;
	
}
