package io.multiversum.db.executor.core.driver;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.junit.jupiter.api.Test;
import org.web3j.EVMTest;

import io.multiversum.db.driver.wrapper.MTVResultSet;

@EVMTest
class UpdateTest extends BaseDriverTest {

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
		
		Statement updateStatement = conn.createStatement();
		updateStatement.executeQuery(String.format("update users set firstname = 'Andrew' where index = %s", index));
		
		Statement selectStatement = conn.createStatement();
		ResultSet result = selectStatement.executeQuery("select * from users");
		
		result.next();
		String firstname = result.getString("firstname");
		
		assertEquals("Andrew", firstname);
	}

}
