package io.multiversum.db.executor.core.commands;

import java.math.BigInteger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.web3j.protocol.Web3j;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import io.multiversum.db.executor.core.CommandQueueExecutor;
import io.multiversum.db.executor.core.Parser;
import io.multiversum.db.executor.core.contracts.Database;

public class BaseCommandTest {

	protected Database contract;
	
	@BeforeEach
	public void begin(Web3j web3, TransactionManager transactionManager, ContractGasProvider gasProvider) throws Exception {
		contract = Database.deploy(web3, transactionManager, gasProvider).send();
		contract.initialize(new BigInteger("1"), new BigInteger("0")).send();
	}
	
	protected CommandQueueExecutor Executor(String sql) {
		Parser.parse(sql);
		
		return new CommandQueueExecutor(contract);
	}
	
	@AfterEach
	public void cleanup() {
		CommandStack.clearAll();
	}
	
}
