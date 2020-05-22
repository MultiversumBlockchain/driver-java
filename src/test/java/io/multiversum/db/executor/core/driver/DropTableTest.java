package io.multiversum.db.executor.core.driver;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Connection;
import java.sql.Statement;

import org.junit.jupiter.api.Test;
import org.web3j.EVMTest;

import io.multiversum.db.driver.wrapper.MTVResultSet;

@EVMTest
class DropTableTest extends BaseDriverTest {

	@Test
	void test() throws Exception {
		Connection conn = this.connection();
		
		Statement createStatement = conn.createStatement();
		createStatement.executeQuery(
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
		
		Statement showStatement = conn.createStatement();
		MTVResultSet showResult = (MTVResultSet) showStatement.executeQuery("show tables");
		
		assertEquals(1, showResult.getSetSize());
		
		Statement dropStatement = conn.createStatement();
		dropStatement.executeQuery("drop table users");
		
		Statement currShowStatement = conn.createStatement();
		MTVResultSet currShowResult = (MTVResultSet) currShowStatement.executeQuery("show tables");
		
		assertEquals(0, currShowResult.getSetSize());
	}

}
