package io.multiversum.db.executor.core;

import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.multiversum.db.executor.core.commands.CommandStack;
import io.multiversum.db.executor.core.commands.SqlCommand;
import io.multiversum.db.executor.core.commands.results.CommandResult;
import io.multiversum.db.executor.core.contracts.Database;

public class CommandQueueExecutor {

	private Database contract;
	private final Stack<CommandResult> resultStack = new Stack<CommandResult>();
	
	private static final Logger log = LoggerFactory.getLogger(CommandQueueExecutor.class);
	
	public CommandQueueExecutor(Database contract) {
		this.contract = contract;
	}
	
	public Database getContract() {
		return contract;
	}
	
	public void execute() throws Exception {
		SqlCommand cmd = CommandStack.pop();
		while (cmd != null) {
			CommandResult result = cmd.run(this);
			
			log.debug(String.format("\n(%s): RESULT %s", cmd.getClass(), result));
			
			resultStack.push(result);
			
			cmd = CommandStack.pop();
		}
	}
	
	public CommandResult getLastResult() {
		try {
			return resultStack.pop();
		} catch (Exception e) {
			return null;
		}
	}
	
}
