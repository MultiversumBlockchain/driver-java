package io.multiversum.db.executor;

import java.math.BigInteger;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tx.gas.StaticGasProvider;

import io.multiversum.db.executor.core.CommandQueueExecutor;
import io.multiversum.db.executor.core.Parser;
import io.multiversum.db.executor.core.commands.CommandStack;
import io.multiversum.db.executor.core.commands.results.CommandResult;
import io.multiversum.db.executor.core.contracts.Database;

public class Executor {

	public static class Options {
		public Web3j web3;
		// Account's credentials, used to sign transactions
		public Credentials credentials;
		// Gas price (in wei)
		public String gasPrice;
		// Gas limit (in wei)
		public String gasLimit;
		// Contract address
		public String address;
		
		public Options(Web3j web3, Credentials credentials, String gasPrice, String gasLimit, String address) {
			this.web3 = web3;
			this.credentials = credentials;
			this.gasPrice = gasPrice;
			this.gasLimit = gasLimit;
			this.address = address;
		}
	}
	
	private static Database database = null;
	
	public static CommandResult singleExecution(String sql, Options options) throws Exception {
		// Parse input sql query, this throws an exception an operations is unsupported.
		// Generates a static command stack that will be executed
		Parser.parse(sql);
		
		// Initialize the contract interface
		Executor.newDatabseIstance(options);
		
		// Create a command executor based on the generate command stack
		CommandQueueExecutor cmdExecutor = new CommandQueueExecutor(Executor.database);
		
		// Execute the command stack
		cmdExecutor.execute();
		
		CommandResult result = cmdExecutor.getLastResult();
		
		// Clean the command stack for future executions
		CommandStack.clearAll();
		
		return result;
	}
	
	private static void newDatabseIstance(Options options) throws Exception {
		// Ensures we only create one instance of the contract interface
		if (Executor.database != null) {
			return;
		}
		
		// Gas parameters
		BigInteger gasPrice = new BigInteger(options.gasPrice);
		BigInteger gasLimit = new BigInteger(options.gasLimit);
		StaticGasProvider gasProvider = new StaticGasProvider(gasPrice, gasLimit);
		
		// Instantiate contract interface from the provided address
        Executor.database = Database.load(options.address, options.web3, options.credentials, gasProvider);
	}
	
	private Executor() {}
	
}
