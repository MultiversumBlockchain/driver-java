package io.multiversum.db.executor.core.driver;

import static org.junit.Assert.assertTrue;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.Statement;

import org.junit.jupiter.api.Test;
import org.web3j.EVMTest;

import io.multiversum.db.driver.wrapper.MTVResultSet;

@EVMTest
class InsertIntoTest extends BaseDriverTest {

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
		
		Statement insertStatement = conn.createStatement();
		MTVResultSet result = (MTVResultSet) insertStatement.executeQuery("insert into users (id, firstname, lastname) values (10, 'John', 'Doe')");
		
		result.next();
		BigInteger index = result.getRowIndex();
		
		// The index is zero-based so its sign to be valid should be 0 or 1 to be valid
		assertTrue(index.signum() == 1 || index.signum() == 0);
	}

}
