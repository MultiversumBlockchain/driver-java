# Mutliversum's JDBC driver

#### Note: this software is still a work in progress and is subject to heavy refactoring and modifications. It is not advised to use it in a production environment and to rely on its public interfaces.

## Current state of the project

The driver should be able to create and fetch tables. Insert new rows and performing basic selects.
Please note that not all JDBC interfaces are currently implemented, many operations may fail or throw exceptions and returned results may not reliable.

## Contributing

Pull requests are disabled but feel free to report any issue or feature request.


## Usage

#### Installation

Installing the driver is as simple as cloning this repository into your project. 

`git clone https://github.com/MultiversumBlockchain/driver-java.git`


#### Example

```java
import java.sql.Statement;
import java.sql.Connection;
import java.util.Properties;

import io.multiversum.db.driver.MTVDriver;

public class Foo {
	private final String host = "127.0.0.1";
	private final int port = 8545;
	private final String privateKey = "0x...";
	private final String databaseAddress = "0x...";
	private final String connectionUri = "jdbc:mtv://%s:%d/%s?key=%s";

	public static void main(String[] args) {
    	String uri = String.format(connectionUri, host, port, databaseAddress, privateKey);
        Connection conn = new MTVDriver().connect(uri, new Properties());
        
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
}
```