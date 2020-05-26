package io.multiversum.db.executor.core.driver;

import java.sql.Connection;
import java.util.Properties;

import org.junit.jupiter.api.BeforeEach;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import io.multiversum.db.driver.MTVDriver;
import io.multiversum.db.executor.core.contracts.Database;

public class BaseDriverTest {

	protected Web3j web3;
	protected String schemaAddress;
	protected Credentials credentials;
	
	@BeforeEach
	public void begin(Web3j web3, TransactionManager transactionManager, ContractGasProvider gasProvider) throws Exception {
		this.web3 = web3;
		
		// Private key used by EVMTest
		credentials = Credentials.create("0x8f2a55949038a9610f50fb23b5883af3b4ecb3c3bb792cbcefbd1542c692be63");
		schemaAddress = Database.deploy(web3, transactionManager, gasProvider).send().getContractAddress();
	}
	
	protected Connection connection() throws Exception {
		return new MTVDriver().connect(web3, credentials, schemaAddress, new Properties());
	}

}
