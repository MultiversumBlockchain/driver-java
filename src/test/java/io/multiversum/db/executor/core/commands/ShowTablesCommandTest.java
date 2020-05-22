package io.multiversum.db.executor.core.commands;

import org.junit.jupiter.api.Test;
import org.web3j.EVMTest;

import io.multiversum.db.executor.core.CommandQueueExecutor;

@EVMTest
class ShowTablesCommandTest extends BaseCommandTest {

	@Test
	void test() throws Exception {
		String sql = "show tables;";
		CommandQueueExecutor exec = Executor(sql);
		
		exec.execute();
	}

}
