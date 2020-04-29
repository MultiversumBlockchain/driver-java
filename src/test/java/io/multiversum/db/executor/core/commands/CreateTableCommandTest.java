package io.multiversum.db.executor.core.commands;

import org.junit.jupiter.api.Test;
import org.web3j.EVMTest;

import io.multiversum.db.executor.core.CommandQueueExecutor;
import io.multiversum.db.executor.core.util.StringUtils;

@EVMTest
class CreateTableCommandTest extends BaseCommandTest {

	@Test
	void test() throws Exception {
		contract.createSchema(StringUtils.toBytes("dummy")).send();
		
		String sql = "use dummy; create table t1 (c1 int, c2 varchar, c3 bool, c4 datetime);";
		CommandQueueExecutor exec = Executor(sql);
		
		exec.execute();
	}

}
