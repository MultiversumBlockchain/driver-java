package io.multiversum.db.executor.core.driver;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Properties;

import org.junit.jupiter.api.Test;

import io.multiversum.db.driver.MTVDriver;
import io.multiversum.db.driver.wrapper.MTVResultSet;

class DriverTest {

	private final String host = "127.0.0.1";
	private final int port = 8545;
	private final String privateKey = "0x8ac6a87224ef11e2be4c4af5f99b1327e8207baf51ce07dddad5cef40370eeae";
	private final String databaseAddress = "0x96C51d0cF5E35A56D6Dfc8AD25955e26f4131f25";
	
	@Test
	void connectionTest() throws Exception {
		this.connection();
		
		// Should not throw an exception
		
		assertTrue(true);
	}
	
	@Test
	void createTableTest() throws Exception {
		Connection conn = this.connection();
		
		Statement statement = conn.createStatement();
		statement.executeQuery(
				"create table users ("
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
	
	@Test
	void insertIntoTest() throws Exception {
		Connection conn = this.connection();
		
		Statement createStatement = conn.createStatement();
		createStatement.executeQuery(
				"create table users ("
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
		
		// index's sign is '1' if it's greater than zero, which is the success condition
		assertEquals(1, index.signum());
	}
	
	private Connection connection() throws Exception {
		String params = String.format(
				"jdbc:mtv://%s:%d/%s?key=%s",
				host,
				port,
				databaseAddress,
				privateKey
			);
		
		return new MTVDriver().connect(params, new Properties());
	}

}
