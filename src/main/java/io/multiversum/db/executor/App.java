package io.multiversum.db.executor;


import java.math.BigInteger;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.StaticGasProvider;

import io.multiversum.db.executor.core.CommandQueueExecutor;
import io.multiversum.db.executor.core.Parser;
import io.multiversum.db.executor.core.contracts.Database;

public class App 
{
    public static void main( String[] args ) throws Exception
    {
        //Parser.parse("use Eggs; show tables; create table t1 (a int, b varchar); show tables;");
        //Parser.parse("use test_db;create table if not exists t3 (a int, b varchar); insert into t3 (a, b) select '125', '777';");
        Parser.parse("use test_db;create table if not exists t3 (a int, b varchar); select * from t3 where a > 1 and (b = '125' or a > 25 - 1);");
        
        Web3j web3 = Web3j.build(new HttpService("http://127.0.0.1:8545"));
        Credentials credentials = Credentials.create("0x8ac6a87224ef11e2be4c4af5f99b1327e8207baf51ce07dddad5cef40370eeae");
        StaticGasProvider gasProvider = new StaticGasProvider(new BigInteger("20000000000"), new BigInteger("6721975"));
        Database database = Database.load("0x1268A9f6B326cB7f93D25C31975ff11949f6e29E", web3, credentials, gasProvider);
        
        CommandQueueExecutor cmdExecutor 
        	= new CommandQueueExecutor(database);
        cmdExecutor.execute();
    }
 
}
