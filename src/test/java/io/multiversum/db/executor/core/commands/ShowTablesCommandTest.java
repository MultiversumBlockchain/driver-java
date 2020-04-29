package io.multiversum.db.executor.core.commands;

import org.junit.jupiter.api.Test;
import org.web3j.EVMTest;

import io.multiversum.db.executor.core.CommandQueueExecutor;
import io.multiversum.db.executor.core.util.StringUtils;

@EVMTest
class ShowTablesCommandTest extends BaseCommandTest {

	@Test
	void test() throws Exception {
		contract.createSchema(StringUtils.toBytes("dummy")).send();
		
		String sql = "use dummy; show tables;";
		CommandQueueExecutor exec = Executor(sql);
		
		exec.execute();
	}

}
