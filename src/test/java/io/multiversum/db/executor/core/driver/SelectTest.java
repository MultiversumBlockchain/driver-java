package io.multiversum.db.executor.core.driver;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;

import org.junit.jupiter.api.Test;
import org.web3j.EVMTest;

import io.multiversum.db.driver.wrapper.MTVResultSet;

@EVMTest
class SelectTest extends BaseDriverTest {

	@Test
	void test() throws Exception {
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

}
