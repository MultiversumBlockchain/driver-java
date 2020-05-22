package io.multiversum.db.executor.core.driver;

import static org.junit.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.Statement;

import org.junit.jupiter.api.Test;
import org.web3j.EVMTest;

@EVMTest
class CreateTableTest extends BaseDriverTest {

	@Test
	void test() throws Exception {
		Connection conn = this.connection();
		
		Statement statement = conn.createStatement();
		statement.executeQuery(
				"create table if not exists users ("
				+ "id int,"
				+ "firstname varchar,"
				+ "lastname varchar,"
				+ "email varchar,"
				+ "is_admin bool,"
				+ "created_at datetime,"
				+ "balance double"
				+ ")"
			);
		
		assertNotNull(statement.getResultSet());
	}

}
