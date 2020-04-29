package io.multiversum.db.executor.core.commands;

import org.junit.jupiter.api.Test;
import org.web3j.EVMTest;

import io.multiversum.db.executor.core.CommandQueueExecutor;
import io.multiversum.db.executor.core.exceptions.DatabaseNotFoundException;
import io.multiversum.db.executor.core.util.StringUtils;

@EVMTest
public class UseCommandTest extends BaseCommandTest {

	@Test()
	public void noTableTest() throws Exception {
		String sql = "use dummy;";
		CommandQueueExecutor exec = Executor(sql);
		
		try {			
			exec.execute();
		} catch (DatabaseNotFoundException ex) {
			// Expected!
			return;
		}
	}
	
	@Test()
	public void tableFoundTest() throws Exception {
		contract.createSchema(StringUtils.toBytes("dummy")).send();
		
		String sql = "use dummy;";
		CommandQueueExecutor exec = Executor(sql);
		
		exec.execute();
	}

}
