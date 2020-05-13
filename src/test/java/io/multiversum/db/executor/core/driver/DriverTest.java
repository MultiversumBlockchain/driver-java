package io.multiversum.db.executor.core.driver;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Properties;

import org.junit.jupiter.api.Test;

import io.multiversum.db.driver.MTVDriver;
import io.multiversum.db.driver.wrapper.MTVResultSet;

class DriverTest {

	private final String host = "127.0.0.1";
	private final int port = 8545;
	private final String privateKey = "0x8ac6a87224ef11e2be4c4af5f99b1327e8207baf51ce07dddad5cef40370eeae";
	private final String databaseAddress = "0x1268A9f6B326cB7f93D25C31975ff11949f6e29E";
	
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
	
	@Test
	void insertIntoTest() throws Exception {
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
		assertTrue(index.signum() == 1 || index.signum() == 1);
	}
	
	@Test
	void plainSelectTest() throws Exception {
		Connection conn = this.connection();
		
		Statement createStatement = conn.createStatement();
		createStatement.executeQuery(
				"create table if not exists users_select_test ("
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
		insertStatement.executeQuery("insert into users_select_test (id, firstname, lastname, created_at) values (10, 'John', 'Doe', '2020-01-21 13:45:50')");
		insertStatement.executeQuery("insert into users_select_test (id, firstname, lastname, created_at) values (20, 'Andrew', 'Anderson', '2020-04-16 08:30:00')");
		
		Statement selectStatement = conn.createStatement();
		MTVResultSet result = (MTVResultSet) selectStatement.executeQuery("select * from users_select_test");
		
		try {
			result.next();
		} catch (SQLException e) {
			fail("Result set is empty");
		}

		long id = result.getLong("id");
		String firstname = result.getString("firstname");
		
		String stringCreatedAt = result.getString("created_at");
		Date dateCreatedAt = result.getDate("created_at");
		Timestamp tsCreatedAt = result.getTimestamp("created_at");
		Time timeCreatedAt = result.getTime("created_at");
		
		assertEquals(10, id);
		assertEquals("John", firstname);
		
		/**
		 * "1579610750000" is the UNIX timestamp representation of the date and time "2020-01-21 13:45:50"
		 * used in this test
		 */
		assertEquals("1579610750000", stringCreatedAt);
		assertEquals(new Date(1579610750000l), dateCreatedAt);
		assertEquals(new Timestamp(1579610750000l), tsCreatedAt);
		assertEquals(new Time(1579610750000l), timeCreatedAt);
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
