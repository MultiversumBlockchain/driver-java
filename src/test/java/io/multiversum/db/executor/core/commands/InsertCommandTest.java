package io.multiversum.db.executor.core.commands;

import org.junit.jupiter.api.Test;
import org.web3j.EVMTest;

import io.multiversum.db.executor.core.CommandQueueExecutor;

@EVMTest
class InsertCommandTest extends BaseCommandTest {

	@Test
	void test() throws Exception {
		String sql = "create table t1 (id int, str varchar); insert into t1 (id, str) values (123, 'foo');";
		CommandQueueExecutor exec = Executor(sql);
		
		exec.execute();
	}

}
