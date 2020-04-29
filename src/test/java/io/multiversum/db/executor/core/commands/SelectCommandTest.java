package io.multiversum.db.executor.core.commands;

import org.junit.jupiter.api.Test;
import org.web3j.EVMTest;

import io.multiversum.db.executor.core.CommandQueueExecutor;
import io.multiversum.db.executor.core.util.StringUtils;

@EVMTest
class SelectCommandTest extends BaseCommandTest {

	@Test
	void test() throws Exception {
		contract.createSchema(StringUtils.toBytes("test_db")).send();
		
		String sql = "use test_db;" +
				"create table test_table (id int, name varchar, lastname varchar, salary double);" +
				"insert into test_table (id, name, lastname, salary) values (1, 'john', 'doe', 3500.5);";
		
		CommandQueueExecutor exec = Executor(sql);
		exec.execute();
		
		CommandStack.clearAll();
		
		String[] queries = new String[] {
			"select * from test_table;",
			"select id from test_table;",
			"select name, lastname from test_table;",
//			"select count(*) from test_table;",
//			"select count(name) from test_table;",
//			"select sum(salary) from test_table;",
//			"select avg(salaray) from test_table;",
//			"select min(salaray) from test_table;",
//			"select max(salary) from test_table;",
		};
		
		for (String query : queries) {
			CommandQueueExecutor executor = Executor(query);
			executor.execute();
			
			CommandStack.clearAll();
		}
	}

}
