package io.multiversum.db.executor.core.commands.util;

import java.math.BigInteger;

import io.multiversum.db.executor.core.contracts.ColumnType;

public class ColumnInfo {

	private String name;
	private BigInteger index;
	private ColumnType type;
	
	public ColumnInfo(String name, BigInteger index, ColumnType type) {
		this.name = name;
		this.index = index;
		this.type = type;
	}
	
	public String getName() {
		return name;
	}
	
	public BigInteger getIndex() {
		return index;
	}
	
	public ColumnType getType() {
		return type;
	}
	
}
