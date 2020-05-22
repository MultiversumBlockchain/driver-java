package io.multiversum.db.executor.core.driver;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.Test;
import org.web3j.EVMTest;

import io.multiversum.db.driver.wrapper.MTVResultSet;

@EVMTest
class DeleteTest extends BaseDriverTest {

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
		MTVResultSet insertResult = (MTVResultSet) insertStatement.executeQuery("insert into users (id, firstname, lastname) values (10, 'John', 'Doe')");
		
		insertResult.next();
		BigInteger index = insertResult.getRowIndex();
		
		int prevCount = tableSize(conn, "users");
		
		Statement deleteStatement = conn.createStatement();
		deleteStatement.executeQuery(String.format("delete from users where index = %s", index));
		
		int currCount = tableSize(conn, "users");
		
		assertEquals(prevCount - 1, currCount);
	}
	
	private int tableSize(Connection connection, String table) throws SQLException {
		Statement selectStatement = connection.createStatement();
		MTVResultSet result = (MTVResultSet) selectStatement.executeQuery("select * from " + table);
		
		return result.getSetSize();
	}

}
