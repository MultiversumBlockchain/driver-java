package io.multiversum.db.driver.wrapper;

import java.io.IOException;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.*;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;


public class MTVConnection implements Connection {
	private String host;
	private int port;
	private String url;
	private Web3j web3;
	private Credentials credentials;
	
	private static int sid = 0;
	private List<String> sqlCommands;
	private boolean readOnly;
	private boolean autoCommit;
	private String catalog;
	private String schema;
	private int transactionIsolationLevel;
	
	private static final Logger log = LoggerFactory.getLogger(MTVConnection.class);
	
	public MTVConnection(String host, int port, String schema, Credentials credentials)  throws Exception {
		this.host = host;
		this.port = port;
		this.schema = schema;
		this.credentials = credentials;

		this.url = String.format("%s:%d", this.host, this.port);
		
		this.web3 = connectToNode();
	}
	
	public Web3j getWeb3() {
		return this.web3;
	}
	
	public Credentials getCredentials() {
		return this.credentials;
	}
	
	private Web3j connectToNode() throws Exception {
		// Create a web3 interface connected to the provided node
		Web3j web3 = null;
		if (this.url.startsWith("https:") || this.url.startsWith("http:")) {
			web3 = Web3j.build(new HttpService(this.url));
			
			try {
				web3.web3ClientVersion().send();
			} catch (Exception e) {
				throw new Exception("Host unreachable");
			}
		} else {
			// First try with https
			String url = "https://" + this.url;
			web3 = Web3j.build(new HttpService(url));
			
			boolean success = false;
			try {
				web3.web3ClientVersion().send();
				success = true;
			} catch (Exception e) {}
			
			if (!success) {
				// Try with http
				url = "http://" + this.url;
				web3 = Web3j.build(new HttpService(url));
				
				try {
					web3.web3ClientVersion().send();
					success = true;
				} catch (Exception e) {}
			}
			
			if (!success) {
				throw new Exception("Host unreachable");
			}
		}
		
		log.debug("Connected to web3 provider");
		
		return web3;
	}

	@Override
	public Statement createStatement() throws SQLException {
		try {
			// TODO: this is temporary!
			int id = sid++;
			return new MTVStatement(this, id, ResultSet.TYPE_FORWARD_ONLY, 0, false);
		} catch (Exception e) {
			e.printStackTrace();
			throw e; // TODO Throw SQLexcepition
		}
	}

	@Override
	public String nativeSQL(String sql) throws SQLException {
		return null;
	}

	@Override
	public void setAutoCommit(boolean autoCommit) throws SQLException {
		this.autoCommit = autoCommit;
	}

	@Override
	public boolean getAutoCommit() throws SQLException {
		return autoCommit;
	}

	@Override
	public void commit() throws SQLException {
	}

	@Override
	public void rollback() throws SQLException {
	}

	@Override
	public void close() throws SQLException {
	}

	@Override
	public boolean isClosed() throws SQLException {
		return false; // TODO
	}

	@Override
	public DatabaseMetaData getMetaData() throws SQLException {
		return null;
	}

	@Override
	public void setReadOnly(boolean readOnly) throws SQLException {
		this.readOnly = readOnly;
	}

	@Override
	public boolean isReadOnly() throws SQLException {
		return readOnly;
	}

	@Override
	public void setCatalog(String catalog) throws SQLException {
		this.catalog = catalog;
	}

	@Override
	public String getCatalog() throws SQLException {
		return this.catalog;
	}

	@Override
	public void setTransactionIsolation(int level) throws SQLException {
		this.transactionIsolationLevel = level;
	}

	@Override
	public int getTransactionIsolation() throws SQLException {
		return transactionIsolationLevel;
	}

	@Override
	public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
		return new MTVStatement(this, resultSetConcurrency, resultSetConcurrency, resultSetConcurrency, autoCommit); 
	}

	@Override
	public Properties getClientInfo() throws SQLException {
		Properties clientInfo = new Properties();

		try {
			Web3ClientVersion version = this.web3.web3ClientVersion().send();
			String clientVersion = version.getWeb3ClientVersion();
			
			clientInfo.put("version", clientVersion);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return clientInfo;
	}

	@Override
	public boolean isWrapperFor(Class<?> arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <T> T unwrap(Class<T> arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void abort(Executor executor) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void clearWarnings() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Blob createBlob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Clob createClob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NClob createNClob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SQLXML createSQLXML() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getClientInfo(String name) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getHoldability() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNetworkTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getSchema() throws SQLException {
		return this.schema;
	}

	@Override
	public Map<String, Class<?>> getTypeMap() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isValid(int timeout) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public CallableStatement prepareCall(String sql) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency,
			int resultSetHoldability) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency,
			int resultSetHoldability) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void rollback(Savepoint savepoint) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setClientInfo(Properties properties) throws SQLClientInfoException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setClientInfo(String name, String value) throws SQLClientInfoException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setHoldability(int holdability) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public Savepoint setSavepoint() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Savepoint setSavepoint(String name) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSchema(String schema) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
		// TODO Auto-generated method stub

	}

	public List<String> getSqlCommands() {
		return sqlCommands;
	}

	public void setSqlCommands(List<String> sqlCommands) {
		this.sqlCommands = sqlCommands;
	}
}
