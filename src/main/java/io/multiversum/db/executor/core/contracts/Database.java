package io.multiversum.db.executor.core.contracts;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint32;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.5.16.
 */
@SuppressWarnings("rawtypes")
public class Database extends Contract {
    public static final String BINARY = "0x608060405234801561001057600080fd5b50611513806100206000396000f3fe608060405234801561001057600080fd5b50600436106100d1576000357c01000000000000000000000000000000000000000000000000000000009004806398b8c2b01161008e57806398b8c2b0146101625780639a55de5214610182578063a963d9c31461018a578063b974445a1461019d578063bdafcae7146101bd578063f899e4dc146101c5576100d1565b8063028c0907146100d65780630d7017fc146100f4578063389704f9146101095780633ecc0f5e1461011c5780637a49b7a01461012f5780638b2f8a381461014f575b600080fd5b6100de6101d8565b6040516100eb9190611485565b60405180910390f35b6101076101023660046110b5565b6101ec565b005b61010761011736600461107c565b610330565b61010761012a366004611160565b61040a565b61014261013d366004611032565b61043e565b6040516100eb919061146e565b61014261015d36600461107c565b6105c7565b61017561017036600461107c565b610627565b6040516100eb9190611281565b6100de6107ca565b610142610198366004611032565b6107d6565b6101b06101ab366004611135565b610914565b6040516100eb91906111df565b610175610bb8565b6101076101d3366004611094565b610cdb565b600054640100000000900463ffffffff1681565b600086815260026020526040902054869060ff16151560011461022d5760405160e560020a62461bcd02815260040161022490611437565b60405180910390fd5b83821461024f5760405160e560020a62461bcd028152600401610224906112d8565b60006001888154811061025e57fe5b9060005260206000209060050201600201600201878154811061027d57fe5b6000918252602082200191505b858110156102ee5784848281811061029e57fe5b90506020028101906102b09190611496565b838989858181106102bd57fe5b90506020020135815481106102ce57fe5b9060005260206000200191906102e5929190610e2e565b5060010161028a565b507f5f2a718c96e7b2904604386dddcd78e091d467a410be171ac9562cf58332065c8760405161031e919061146e565b60405180910390a15050505050505050565b600081815260026020526040902054819060ff1615156001146103685760405160e560020a62461bcd02815260040161022490611437565b6001828154811061037557fe5b6000918252602082206005909102018181556001810182905560028101828155909190816103a66003850182610eac565b6103b4600283016000610ed0565b50505060008381526002602052604090819020805460ff19169055517f1f021955ea3ca7b0b75fb7d837e741808206bfe020592ea2e3f9cc461cd2e6c591506103fe90849061146e565b60405180910390a15050565b6000805463ffffffff9283166401000000000267ffffffff00000000199490931663ffffffff199091161792909216179055565b6000816104605760405160e560020a62461bcd02815260040161022490611400565b600180548082018083556000838152929190811061047a57fe5b6000918252602080832060016005909302018281018990556003548082558452600290915260408320805460ff191690921790915591505b8381101561056c578160020160010160405180604001604052808787858181106104d857fe5b905060200201356001900460068111156104ee57fe5b60068111156104f957fe5b815260200187878560010181811061050d57fe5b602090810292909201359092528354600181810186556000958652919094208351600290950201805493949093909250839160ff199091169083600681111561055257fe5b0217905550602091909101516001909101556002016104b2565b5060035461058181600163ffffffff610d9116565b6003556040517f4944ea4e168fc6b752b8443ae22fcfcbdcfbcaa1a21623a65585cc8d5a87d65c906105b69083908990611477565b60405180910390a195945050505050565b600081815260026020526040812054829060ff1615156001146105ff5760405160e560020a62461bcd02815260040161022490611437565b6001838154811061060c57fe5b60009182526020909120600590910201600401549392505050565b600081815260026020526040902054606090829060ff1615156001146106625760405160e560020a62461bcd02815260040161022490611437565b60606001848154811061067157fe5b600091825260209091206003600590920201015460020267ffffffffffffffff8111801561069e57600080fd5b506040519080825280602002602001820160405280156106c8578160200160208202803683370190505b50905060005b600185815481106106db57fe5b60009182526020909120600360059092020101548110156107c2576001858154811061070357fe5b9060005260206000209060050201600201600101818154811061072257fe5b600091825260209091206002909102015460ff16600681111561074157fe5b60010282826002028151811061075357fe5b6020026020010181815250506001858154811061076c57fe5b9060005260206000209060050201600201600101818154811061078b57fe5b9060005260206000209060020201600101548282600202600101815181106107af57fe5b60209081029190910101526001016106ce565b509392505050565b60005463ffffffff1681565b600083815260026020526040812054849060ff16151560011461080e5760405160e560020a62461bcd02815260040161022490611437565b6001858154811061081b57fe5b600091825260208220600460059092020101805460019081018255908252805481908890811061084757fe5b600091825260208220600460059092020101549190910391505b848110156108e4576001878154811061087657fe5b9060005260206000209060050201600201600201828154811061089557fe5b906000526020600020018686838181106108ab57fe5b90506020028101906108bd9190611496565b8254600181018455600093845260209093206108db93019190610e2e565b50600101610861565b507f2d07fea1c1dda468753e4667157fc4b0f62d5ac0a65b6531b079c243ee938cb2816040516105b6919061146e565b600083815260026020526040902054606090849060ff16151560011461094f5760405160e560020a62461bcd02815260040161022490611437565b60328311156109735760405160e560020a62461bcd0281526004016102249061136c565b6001858154811061098057fe5b600091825260209091206004600590920201015484106109b55760405160e560020a62461bcd028152600401610224906113a3565b60006109c7858563ffffffff610d9116565b9050600081600188815481106109d957fe5b6000918252602090912060046005909202010154106109f85781610a1a565b60018781548110610a0557fe5b60009182526020909120600460059092020101545b905060608167ffffffffffffffff81118015610a3557600080fd5b50604051908082528060200260200182016040528015610a6957816020015b6060815260200190600190039081610a545790505b509050865b82811015610bac5760018981548110610a8357fe5b90600052602060002090600502016002016002018181548110610aa257fe5b90600052602060002001805480602002602001604051908101604052809291908181526020016000905b82821015610b775760008481526020908190208301805460408051601f6002600019610100600187161502019094169390930492830185900485028101850190915281815292830182828015610b635780601f10610b3857610100808354040283529160200191610b63565b820191906000526020600020905b815481529060010190602001808311610b4657829003601f168201915b505050505081526020019060010190610acc565b5050505082610b8f8a84610dc290919063ffffffff16565b81518110610b9957fe5b6020908102919091010152600101610a6e565b50979650505050505050565b600154606090819060020267ffffffffffffffff81118015610bd957600080fd5b50604051908082528060200260200182016040528015610c03578160200160208202803683370190505b50905060005b600154811015610cd4576002600060018381548110610c2457fe5b60009182526020808320600590920290910154835282019290925260400190205460ff16610c5157610ccc565b60018181548110610c5e57fe5b906000526020600020906005020160000154600102828260020281518110610c8257fe5b60200260200101818152505060018181548110610c9b57fe5b906000526020600020906005020160010154828260020260010181518110610cbf57fe5b6020026020010181815250505b600101610c09565b5090505b90565b600082815260026020526040902054829060ff161515600114610d135760405160e560020a62461bcd02815260040161022490611437565b60018381548110610d2057fe5b90600052602060002090600502016002016002018281548110610d3f57fe5b906000526020600020016000610d559190610eee565b7fea7b1d2bbe9f9538f0f58f4192a22a507badb377e854a0df05f827c57e65a4ae82604051610d84919061146e565b60405180910390a1505050565b600082820183811015610db95760405160e560020a62461bcd02815260040161022490611335565b90505b92915050565b6000610db983836040518060400160405280601e81526020017f536166654d6174683a207375627472616374696f6e206f766572666c6f77000081525060008184841115610e265760405160e560020a62461bcd02815260040161022491906112c5565b505050900390565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10610e6f5782800160ff19823516178555610e9c565b82800160010185558215610e9c579182015b82811115610e9c578235825591602001919060010190610e81565b50610ea8929150610f0c565b5090565b5080546000825560020290600052602060002090810190610ecd9190610f26565b50565b5080546000825590600052602060002090810190610ecd9190610f4b565b5080546000825590600052602060002090810190610ecd9190610f6e565b610cd891905b80821115610ea85760008155600101610f12565b610cd891905b80821115610ea857805460ff1916815560006001820155600201610f2c565b610cd891905b80821115610ea8576000610f658282610eee565b50600101610f51565b610cd891905b80821115610ea8576000610f888282610f91565b50600101610f74565b50805460018160011615610100020316600290046000825580601f10610fb75750610ecd565b601f016020900490600052602060002090810190610ecd9190610f0c565b60008083601f840112610fe6578182fd5b50813567ffffffffffffffff811115610ffd578182fd5b602083019150836020808302850101111561101757600080fd5b9250929050565b803563ffffffff81168114610dbc57600080fd5b600080600060408486031215611046578283fd5b83359250602084013567ffffffffffffffff811115611063578283fd5b61106f86828701610fd5565b9497909650939450505050565b60006020828403121561108d578081fd5b5035919050565b600080604083850312156110a6578182fd5b50508035926020909101359150565b600080600080600080608087890312156110cd578182fd5b8635955060208701359450604087013567ffffffffffffffff808211156110f2578384fd5b6110fe8a838b01610fd5565b90965094506060890135915080821115611116578384fd5b5061112389828a01610fd5565b979a9699509497509295939492505050565b600080600060608486031215611149578283fd5b505081359360208301359350604090920135919050565b60008060408385031215611172578182fd5b61117c848461101e565b915061118b846020850161101e565b90509250929050565b60008151808452815b818110156111b95760208185018101518683018201520161119d565b818111156111ca5782602083870101525b50601f01601f19169290920160200192915050565b6000602080830181845280855180835260408601915060408482028701019250838701855b8281101561127457878503603f19018452815180518087528891888801919089820289018a01908a015b8285101561125e57601f198a830301845261124a828251611194565b60019590950194938b019391508a0161122e565b5097505050938601935090850190600101611204565b5092979650505050505050565b6020808252825182820181905260009190848201906040850190845b818110156112b95783518352928401929184019160010161129d565b50909695505050505050565b600060208252610db96020830184611194565b60208082526031908201527f436f6c756d6e7320616e642076616c75657320617272617973206d757374206260408201527f65206f6620657175616c206c656e677468000000000000000000000000000000606082015260800190565b6020808252601b908201527f536166654d6174683a206164646974696f6e206f766572666c6f770000000000604082015260600190565b6020808252601e908201527f4c696d6974206d757374206265206265747765656e203020616e642035300000604082015260600190565b6020808252602e908201527f4f6666736574206d75737420626520736d616c6c65722074686174207468652060408201527f6e756d626572206f6620726f7773000000000000000000000000000000000000606082015260800190565b60208082526013908201527f456d70747920636f6c756d6e7320617272617900000000000000000000000000604082015260600190565b60208082526014908201527f5461626c6520646f6573206e6f74206578697374000000000000000000000000604082015260600190565b90815260200190565b918252602082015260400190565b63ffffffff91909116815260200190565b6000808335601e198436030181126114ac578283fd5b8084018035925067ffffffffffffffff8311156114c7578384fd5b6020019250503681900382131561101757600080fdfea26469706673582212206f89e9ef5be7860d3c7476cf33f63cd472b0543abe86e6ef08ab5a811e1e238264736f6c63430006060033";

    public static final String FUNC_VERSIONMAJOR = "VersionMajor";

    public static final String FUNC_VERSIONMINOR = "VersionMinor";

    public static final String FUNC_INITIALIZE = "initialize";

    public static final String FUNC_SHOWTABLES = "showTables";

    public static final String FUNC_CREATETABLE = "createTable";

    public static final String FUNC_DROPTABLE = "dropTable";

    public static final String FUNC_DESC = "desc";

    public static final String FUNC_INSERT = "insert";

    public static final String FUNC_DELETEDIRECT = "deleteDirect";

    public static final String FUNC_UPDATEDIRECT = "updateDirect";

    public static final String FUNC_SELECTALL = "selectAll";

    public static final String FUNC_ROWSCOUNT = "rowsCount";

    public static final Event ROWCREATED_EVENT = new Event("RowCreated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
    ;

    public static final Event ROWDELETED_EVENT = new Event("RowDeleted", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
    ;

    public static final Event ROWUPDATED_EVENT = new Event("RowUpdated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
    ;

    public static final Event TABLECREATED_EVENT = new Event("TableCreated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Bytes32>() {}));
    ;

    public static final Event TABLEDROPPED_EVENT = new Event("TableDropped", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
    ;

    protected static final HashMap<String, String> _addresses;

    static {
        _addresses = new HashMap<String, String>();
        _addresses.put("1584827102800", "0x91F265fB96104A71AceC67FadA13beBda49867c5");
        _addresses.put("1589391892769", "0x1268A9f6B326cB7f93D25C31975ff11949f6e29E");
        _addresses.put("1585260607749", "0x3a150E091fEBf1C2b1B979cD2a8A19B4813366E8");
        _addresses.put("1589030416923", "0x50A304106D991ce61694da9a99C1f021269e71BA");
        _addresses.put("1587724203880", "0x1268A9f6B326cB7f93D25C31975ff11949f6e29E");
        _addresses.put("1590138758289", "0x933391bdAc907a6414753D7F9C745943Ad50a5C4");
        _addresses.put("1587044770094", "0x721F6BD06654eb58D6366EaED0D0d296cd543EaE");
    }

    @Deprecated
    protected Database(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Database(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Database(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Database(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public List<RowCreatedEventResponse> getRowCreatedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(ROWCREATED_EVENT, transactionReceipt);
        ArrayList<RowCreatedEventResponse> responses = new ArrayList<RowCreatedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            RowCreatedEventResponse typedResponse = new RowCreatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.index = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<RowCreatedEventResponse> rowCreatedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, RowCreatedEventResponse>() {
            @Override
            public RowCreatedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(ROWCREATED_EVENT, log);
                RowCreatedEventResponse typedResponse = new RowCreatedEventResponse();
                typedResponse.log = log;
                typedResponse.index = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<RowCreatedEventResponse> rowCreatedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ROWCREATED_EVENT));
        return rowCreatedEventFlowable(filter);
    }

    public List<RowDeletedEventResponse> getRowDeletedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(ROWDELETED_EVENT, transactionReceipt);
        ArrayList<RowDeletedEventResponse> responses = new ArrayList<RowDeletedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            RowDeletedEventResponse typedResponse = new RowDeletedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.index = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<RowDeletedEventResponse> rowDeletedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, RowDeletedEventResponse>() {
            @Override
            public RowDeletedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(ROWDELETED_EVENT, log);
                RowDeletedEventResponse typedResponse = new RowDeletedEventResponse();
                typedResponse.log = log;
                typedResponse.index = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<RowDeletedEventResponse> rowDeletedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ROWDELETED_EVENT));
        return rowDeletedEventFlowable(filter);
    }

    public List<RowUpdatedEventResponse> getRowUpdatedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(ROWUPDATED_EVENT, transactionReceipt);
        ArrayList<RowUpdatedEventResponse> responses = new ArrayList<RowUpdatedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            RowUpdatedEventResponse typedResponse = new RowUpdatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.index = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<RowUpdatedEventResponse> rowUpdatedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, RowUpdatedEventResponse>() {
            @Override
            public RowUpdatedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(ROWUPDATED_EVENT, log);
                RowUpdatedEventResponse typedResponse = new RowUpdatedEventResponse();
                typedResponse.log = log;
                typedResponse.index = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<RowUpdatedEventResponse> rowUpdatedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ROWUPDATED_EVENT));
        return rowUpdatedEventFlowable(filter);
    }

    public List<TableCreatedEventResponse> getTableCreatedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(TABLECREATED_EVENT, transactionReceipt);
        ArrayList<TableCreatedEventResponse> responses = new ArrayList<TableCreatedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            TableCreatedEventResponse typedResponse = new TableCreatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.index = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.name = (byte[]) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<TableCreatedEventResponse> tableCreatedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, TableCreatedEventResponse>() {
            @Override
            public TableCreatedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(TABLECREATED_EVENT, log);
                TableCreatedEventResponse typedResponse = new TableCreatedEventResponse();
                typedResponse.log = log;
                typedResponse.index = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.name = (byte[]) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<TableCreatedEventResponse> tableCreatedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TABLECREATED_EVENT));
        return tableCreatedEventFlowable(filter);
    }

    public List<TableDroppedEventResponse> getTableDroppedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(TABLEDROPPED_EVENT, transactionReceipt);
        ArrayList<TableDroppedEventResponse> responses = new ArrayList<TableDroppedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            TableDroppedEventResponse typedResponse = new TableDroppedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.index = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<TableDroppedEventResponse> tableDroppedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, TableDroppedEventResponse>() {
            @Override
            public TableDroppedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(TABLEDROPPED_EVENT, log);
                TableDroppedEventResponse typedResponse = new TableDroppedEventResponse();
                typedResponse.log = log;
                typedResponse.index = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<TableDroppedEventResponse> tableDroppedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TABLEDROPPED_EVENT));
        return tableDroppedEventFlowable(filter);
    }

    public RemoteFunctionCall<BigInteger> VersionMajor() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_VERSIONMAJOR, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint32>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> VersionMinor() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_VERSIONMINOR, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint32>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> initialize(BigInteger _major, BigInteger _minor) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_INITIALIZE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint32(_major), 
                new org.web3j.abi.datatypes.generated.Uint32(_minor)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<List> showTables() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_SHOWTABLES, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Bytes32>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<TransactionReceipt> createTable(byte[] _name, List<byte[]> _columns) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_CREATETABLE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_name), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Bytes32>(
                        org.web3j.abi.datatypes.generated.Bytes32.class,
                        org.web3j.abi.Utils.typeMap(_columns, org.web3j.abi.datatypes.generated.Bytes32.class))), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> dropTable(BigInteger _table) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_DROPTABLE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_table)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<List> desc(BigInteger _table) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_DESC, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_table)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Bytes32>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<TransactionReceipt> insert(BigInteger _table, List<String> _values) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_INSERT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_table), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Utf8String>(
                        org.web3j.abi.datatypes.Utf8String.class,
                        org.web3j.abi.Utils.typeMap(_values, org.web3j.abi.datatypes.Utf8String.class))), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> deleteDirect(BigInteger _table, BigInteger _index) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_DELETEDIRECT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_table), 
                new org.web3j.abi.datatypes.generated.Uint256(_index)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> updateDirect(BigInteger _table, BigInteger _index, List<BigInteger> _columns, List<String> _values) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_UPDATEDIRECT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_table), 
                new org.web3j.abi.datatypes.generated.Uint256(_index), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Uint256>(
                        org.web3j.abi.datatypes.generated.Uint256.class,
                        org.web3j.abi.Utils.typeMap(_columns, org.web3j.abi.datatypes.generated.Uint256.class)), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Utf8String>(
                        org.web3j.abi.datatypes.Utf8String.class,
                        org.web3j.abi.Utils.typeMap(_values, org.web3j.abi.datatypes.Utf8String.class))), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public List<List<String>> selectAll(BigInteger _table, BigInteger _offset, BigInteger _limit) throws Exception {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_SELECTALL, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_table), 
                new org.web3j.abi.datatypes.generated.Uint256(_offset), 
                new org.web3j.abi.datatypes.generated.Uint256(_limit)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<DynamicArray<Utf8String>>>() {}));
        
        String encodedFunction = FunctionEncoder.encode(function);
        
		String value = call(contractAddress, encodedFunction, defaultBlockParameter);
		
		return Decoder.decodeArrayOfArrayOfStrings(value);
    }

    public RemoteFunctionCall<BigInteger> rowsCount(BigInteger _table) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ROWSCOUNT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_table)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    @Deprecated
    public static Database load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Database(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Database load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Database(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Database load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Database(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Database load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Database(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Database> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Database.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Database> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Database.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<Database> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Database.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Database> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Database.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    protected String getStaticDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public static String getPreviouslyDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public static class RowCreatedEventResponse extends BaseEventResponse {
        public BigInteger index;
    }

    public static class RowDeletedEventResponse extends BaseEventResponse {
        public BigInteger index;
    }

    public static class RowUpdatedEventResponse extends BaseEventResponse {
        public BigInteger index;
    }

    public static class TableCreatedEventResponse extends BaseEventResponse {
        public BigInteger index;

        public byte[] name;
    }

    public static class TableDroppedEventResponse extends BaseEventResponse {
        public BigInteger index;
    }
}
