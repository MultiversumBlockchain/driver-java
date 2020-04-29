package io.multiversum.db.executor.core.driver;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Properties;

import org.junit.jupiter.api.Test;

import io.multiversum.db.driver.MTVDriver;

class DriverTest {

	private final String host = "127.0.0.1";
	private final int port = 8545;
	private final String privateKey = "0x8ac6a87224ef11e2be4c4af5f99b1327e8207baf51ce07dddad5cef40370eeae";
	private final String databaseAddress = "0x1268A9f6B326cB7f93D25C31975ff11949f6e29E";
	
	@Test
	void connectionTest() throws Exception {
		this.connection();
		
		// Should not throw an exception
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
