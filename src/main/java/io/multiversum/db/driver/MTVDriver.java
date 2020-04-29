package io.multiversum.db.driver;

import java.io.File;
import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import org.slf4j.LoggerFactory;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import io.multiversum.db.driver.wrapper.MTVConnection;

public class MTVDriver implements java.sql.Driver {

	private static final MTVDriver INSTANCE = new MTVDriver();
	private static final ThreadLocal<Connection> DEFAULT_CONNECTION = new ThreadLocal<Connection>();
	private static volatile boolean registered;

	private static final String PROTOCOL = "jdbc";
	private static final String PROTOCOL_LONG = PROTOCOL + ":";

	private static final String MTV_PROTOCOL = "mtv";
	private static final String MTV_PREFIX = MTV_PROTOCOL + ":" + "//";
	private static final String MTV_PREFIX_LONG = PROTOCOL_LONG + MTV_PREFIX;
	
	private static final org.slf4j.Logger log = LoggerFactory.getLogger(MTVDriver.class);
	
	public Connection connect(String paramString, Properties paramProperties) throws SQLException {
		String uriString = paramString;
		// Remote JDBC protocol prefix if present
		if (paramString.startsWith(PROTOCOL_LONG)) {
			uriString = uriString.substring(PROTOCOL_LONG.length());
		}
		
		URI uri = URI.create(uriString);
		
		log.debug("Connecting to url: " + uriString);
		log.debug("-- Scheme: " + uri.getScheme());
		log.debug("-- Host: " + uri.getHost());
		log.debug("-- Port: " + uri.getPort());

		String host = uri.getHost();
		int port = uri.getPort();
		
        StringTokenizer multiTokenizer = new StringTokenizer(uri.getQuery(), "&");
        
        // Parse uri parameters
        File wallet = null;
        String password = "";
        String privateKey = null;
        boolean isFileWallet = false;
        while (multiTokenizer.hasMoreElements()) {
        	String tk = multiTokenizer.nextToken();
        	
        	if (tk.startsWith("wallet=")) {
        		isFileWallet = true;
        		wallet = new File(tk.substring("wallet=".length()));
        	} else if (tk.startsWith("password=")) {
        		isFileWallet = true;
        		password = tk.substring("password=".length());
        	} else if (tk.startsWith("key=")) {
        		privateKey = tk.substring("key=".length());
        	}
        }
        
        if ((isFileWallet && wallet == null) || (!isFileWallet && privateKey == null)) {
        	throw new SQLException("Invalid connection paramaters");
        }
        
		try {
			// Create credentials
			Credentials credentials = null;
			if (isFileWallet) {
				log.debug("-- Using stored wallet");
				credentials = WalletUtils.loadCredentials(password, wallet);
			} else {
				log.debug("-- Using private key");
				credentials = Credentials.create(privateKey);
			}

			// Create new connection
			return new MTVConnection(host, port, uri.getPath().substring(1), credentials);
		} catch (Exception e) {
			e.printStackTrace();
			
			throw new SQLException("Failed to initialize connection");
		}
	}

	public boolean acceptsURL(String url) {
		return url.startsWith(MTV_PREFIX) || url.startsWith(MTV_PREFIX_LONG);
	}

	public int getMajorVersion() {
		return 1;
	}

	public int getMinorVersion() {
		return 4;
	}

	public DriverPropertyInfo[] getPropertyInfo(String paramString, Properties paramProperties) {
		return new DriverPropertyInfo[0];
	}

	public boolean jdbcCompliant() {
		return true;
	}

	public Logger getParentLogger() {
		return null;
	}

	public static synchronized MTVDriver load() {
		try {
			if (!(registered)) {
				registered = true;
				DriverManager.registerDriver(INSTANCE);
			}
		} catch (SQLException localSQLException) {
			localSQLException.printStackTrace();
		}
		return INSTANCE;
	}

	public static synchronized void unload() {
		try {
			if (registered) {
				registered = false;
				DriverManager.deregisterDriver(INSTANCE);
			}
		} catch (SQLException localSQLException) {
			// TODO: should we handle this?
		}
	}

	public static void setDefaultConnection(Connection paramConnection) {
		if (paramConnection == null) {
			DEFAULT_CONNECTION.remove();
		} else {
			DEFAULT_CONNECTION.set(paramConnection);
		}
	}

	public static void setThreadContextClassLoader(Thread paramThread) {
		try {
			paramThread.setContextClassLoader(MTVDriver.class.getClassLoader());
		} catch (Throwable localThrowable) {
			// TODO: should we handle this?
		}
	}

	static {
		load();
	}

}