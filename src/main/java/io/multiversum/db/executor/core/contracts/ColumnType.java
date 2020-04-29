package io.multiversum.db.executor.core.contracts;

import java.math.BigInteger;

import org.web3j.utils.Numeric;

public enum ColumnType {

	Int("0"),
	Usigned("1"), // TODO: remove
	String("2"),
	Double("3"),
	Date("4"),
	Datetime("5"),
	Boolean("6");
	
	private final String value;
	
	private ColumnType(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	public byte[] toBytes() {
		BigInteger bn = new BigInteger(value);
		return Numeric.toBytesPadded(bn, 32);
	}
	
}
