package io.multiversum.db.driver.wrapper;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * @see https://docs.oracle.com/javase/7/docs/api/java/sql/ResultSet.html
 */
public class MTVResultSet implements ResultSet {
	private static final int INSERT_ROW = Integer.MIN_VALUE;
	
	private int cursor = -1;
	private int prevCursor = -1;
	private List<List<String>> data = null;
	private List<String> columnsSet = null;
	
	// TODO use a more correct structure
	private final HashMap<Integer, Object> updates = new HashMap<Integer, Object>();
	private SQLWarning warnings = null;
	
	private boolean isClosed = false;
	private boolean wasLastColumnNull = false;
	private Statement statement;

	public MTVResultSet(Statement statement, List<List<String>> data, List<String> columnsSet) {
		this.data = data;
		this.columnsSet = columnsSet;
		this.statement = statement;
	}

	public MTVResultSet(Statement statement, List<List<String>> data) {
		this.data = data;
		this.statement = statement;
	}

	@Override
	public boolean next() throws SQLException {
		if (this.isClosed) {
			throw new SQLException("Result set is closed");
		}
		
		if (this.cursor == this.data.size()) {
			throw new SQLException("Cannot increment the cursor beyond after the last row");
		}
		
		if (this.data != null && (this.cursor + 1) <= this.data.size()) {
			this.cursor++;
			
			this.clearWarnings();
			
			return true;
		}

		return false;
	}

	@Override
	public void close() throws SQLException {
		this.warnings = null;
		this.updates.clear();

		if (this.data != null) {
			this.data.clear();
		}
		
		if (this.columnsSet != null) {
			this.columnsSet.clear();
		}
		
		this.isClosed = true;
	}

	@Override
	public boolean wasNull() throws SQLException {
		if (this.isClosed) {
			throw new SQLException("Result set is closed");
		}
		
		return this.wasLastColumnNull;
	}

	@Override
	public String getString(int i) throws SQLException {
		return this.getDataAt(i);
	}

	@Override
	public boolean getBoolean(int i) throws SQLException {
		String value = this.getDataAt(i);
		
		return !value.equalsIgnoreCase("0") && !value.equalsIgnoreCase("false");
	}

	@Override
	public byte getByte(int i) throws SQLException {
		String value = this.getDataAt(i);
		
		try {
			return Byte.parseByte(value);
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public short getShort(int i) throws SQLException {
		String value = this.getDataAt(i);
		
		try {
			return Short.parseShort(value);
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public int getInt(int i) throws SQLException {
		String value = this.getDataAt(i);
		
		try {
			return Integer.parseInt(value);
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public long getLong(int i) throws SQLException {
		String value = this.getDataAt(i);
		
		try {
			return Long.parseLong(value);
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public float getFloat(int i) throws SQLException {
		String value = this.getDataAt(i);
		
		try {
			return Float.parseFloat(value);
		} catch (Exception e) {
			return 0.0f;
		}
	}

	@Override
	public double getDouble(int i) throws SQLException {
		String value = this.getDataAt(i);
		
		try {
			return Double.parseDouble(value);
		} catch (Exception e) {
			return 0.0;
		}
	}

	@Override
	@Deprecated
	public BigDecimal getBigDecimal(int i, int scale) throws SQLException {
		throw new SQLFeatureNotSupportedException("getBigDecimal is deprecated and unsupported");
	}

	@Override
	public byte[] getBytes(int i) throws SQLException {
		this.validateColumnIndex(i);
		
		String value = this.getDataAt(i);
		
		if (value == null) {
			return null;
		}
		
		return value.getBytes();
	}

	@Override
	public Date getDate(int i) throws SQLException {
		long ts = this.getLong(i);
		
		return new Date(ts);
	}

	@Override
	public Time getTime(int i) throws SQLException {
		long ts = this.getLong(i);
		
		return new Time(ts);
	}

	@Override
	public Timestamp getTimestamp(int i) throws SQLException {
		long ts = this.getLong(i);
		
		return new Timestamp(ts);
	}

	@Override
	public InputStream getAsciiStream(int i) throws SQLException {
		throw new SQLFeatureNotSupportedException("getAsciiStream is not currently supported"); // TODO
	}

	@Override
	public InputStream getUnicodeStream(int i) throws SQLException {
		throw new SQLFeatureNotSupportedException("getUnicodeStream is not currently supported"); // TODO
	}

	@Override
	public InputStream getBinaryStream(int i) throws SQLException {
		throw new SQLFeatureNotSupportedException("getBinaryStream is not currently supported"); // TODO
	}

	@Override
	public String getString(String s) throws SQLException {
		int index = this.columnNameToIndex(s);
		
		return this.getString(index);
	}

	@Override
	public boolean getBoolean(String s) throws SQLException {
		int index = this.columnNameToIndex(s);
		
		return this.getBoolean(index);
	}

	@Override
	public byte getByte(String s) throws SQLException {
		int index = this.columnNameToIndex(s);
		
		return this.getByte(index);
	}

	@Override
	public short getShort(String s) throws SQLException {
		int index = this.columnNameToIndex(s);
		
		return this.getShort(index);
	}

	@Override
	public int getInt(String s) throws SQLException {
		int index = this.columnNameToIndex(s);
		
		return this.getInt(index);
	}

	@Override
	public long getLong(String s) throws SQLException {
		int index = this.columnNameToIndex(s);
		
		return this.getLong(index);
	}

	@Override
	public float getFloat(String s) throws SQLException {
		int index = this.columnNameToIndex(s);
		
		return this.getFloat(index);
	}

	@Override
	public double getDouble(String s) throws SQLException {
		int index = this.columnNameToIndex(s);
		
		return this.getDouble(index);
	}

	@Override
	@Deprecated
	public BigDecimal getBigDecimal(String s, int scale) throws SQLException {
		throw new SQLFeatureNotSupportedException("getBigDecimal is deprecated and unsupported");
	}

	@Override
	public byte[] getBytes(String s) throws SQLException {
		int index = this.columnNameToIndex(s);
		
		return this.getBytes(index);
	}

	@Override
	public Date getDate(String s) throws SQLException {
		int index = this.columnNameToIndex(s);
		
		return this.getDate(index);
	}

	@Override
	public Time getTime(String s) throws SQLException {
		int index = this.columnNameToIndex(s);
		
		return this.getTime(index);
	}

	@Override
	public Timestamp getTimestamp(String s) throws SQLException {
		int index = this.columnNameToIndex(s);
		
		return this.getTimestamp(index);
	}

	@Override
	public InputStream getAsciiStream(String s) throws SQLException {
		int index = this.columnNameToIndex(s);
		
		return this.getAsciiStream(index);
	}

	@Override
	public InputStream getUnicodeStream(String s) throws SQLException {
		int index = this.columnNameToIndex(s);
		
		return this.getUnicodeStream(index);
	}

	@Override
	public InputStream getBinaryStream(String s) throws SQLException {
		int index = this.columnNameToIndex(s);
		
		return this.getBinaryStream(index);
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		if (this.isClosed) {
			throw new SQLException("Result set is closed");
		}
		
		return this.warnings;
	}

	@Override
	public void clearWarnings() throws SQLException {
		if (this.isClosed) {
			throw new SQLException("Result set is closed");
		}
		
		this.warnings = null;
	}

	@Override
	public String getCursorName() throws SQLException {
		throw new SQLFeatureNotSupportedException("getCursorName is not currently supported"); // TODO
	}

	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		throw new SQLFeatureNotSupportedException("getMetaData is not currently supported"); // TODO
	}

	@Override
	public Object getObject(int i) throws SQLException {
		return (Object) this.getDataAt(i);
	}

	@Override
	public Object getObject(String s) throws SQLException {
		int index = this.columnNameToIndex(s);
		
		return this.getDataAt(index);
	}

	@Override
	public int findColumn(String s) throws SQLException {
		if (this.isClosed) {
			throw new SQLException("Result set is closed");
		}
		
		int index = this.columnNameToIndex(s);
		if (index == -1) {
			throw new SQLException("Column " + s + " not found");
		}
		
		return index;
	}

	@Override
	public Reader getCharacterStream(int i) throws SQLException {
		throw new SQLFeatureNotSupportedException("getCharacterStream is not currently supported"); // TODO
	}

	@Override
	public Reader getCharacterStream(String s) throws SQLException {
		throw new SQLFeatureNotSupportedException("getCharacterStream is not currently supported"); // TODO
	}

	@Override
	public BigDecimal getBigDecimal(int i) throws SQLException {
		String value = this.getDataAt(i);
		
		try {
			return new BigDecimal(value);
		} catch (Exception e) {
			return new BigDecimal(0);
		}
	}

	@Override
	public BigDecimal getBigDecimal(String s) throws SQLException {
		int index = this.columnNameToIndex(s);
		
		return this.getBigDecimal(index);
	}

	@Override
	public boolean isBeforeFirst() throws SQLException {
		if (this.isClosed) {
			throw new SQLException("Result set is closed");
		}
		
		return this.cursor == -1;
	}

	@Override
	public boolean isAfterLast() throws SQLException {
		if (this.isClosed) {
			throw new SQLException("Result set is closed");
		}
		
		if (this.data == null || this.data.isEmpty() || this.cursor != this.data.size()) {
			return false;
		}
		
		return true;
	}

	@Override
	public boolean isFirst() throws SQLException {
		if (this.isClosed) {
			throw new SQLException("Result set is closed");
		}
		
		return this.cursor == 0;
	}

	@Override
	public boolean isLast() throws SQLException {
		if (this.isClosed) {
			throw new SQLException("Result set is closed");
		}
		
		return this.cursor == this.data.size() - 1;
	}

	@Override
	public void beforeFirst() throws SQLException {
		throw new SQLException("beforeFirst is not supported");
	}

	@Override
	public void afterLast() throws SQLException {
		throw new SQLException("afterLast is not supported");
	}

	@Override
	public boolean first() throws SQLException {
		throw new SQLException("first is not supported");
	}

	@Override
	public boolean last() throws SQLException {
		throw new SQLException("last is not supported");
	}

	@Override
	public int getRow() throws SQLException {
		if (this.isClosed) {
			throw new SQLException("Result set is closed");
		}
		
		return this.cursor + 1;
	}

	@Override
	public boolean absolute(int i) throws SQLException {
		throw new SQLException("absolute is not supported");
	}

	@Override
	public boolean relative(int i) throws SQLException {
		throw new SQLException("relative is not supported");
	}

	@Override
	public boolean previous() throws SQLException {
		throw new SQLException("previous is not supported");
	}

	@Override
	public void setFetchDirection(int i) throws SQLException {
		if (this.isClosed) {
			throw new SQLException("Result set is closed");
		}
		
		if (i != ResultSet.FETCH_FORWARD) {
			throw new SQLException("Other fetch directions other than FETCH_FORWARD are not supported");
		}
	}

	@Override
	public int getFetchDirection() throws SQLException {
		if (this.isClosed) {
			throw new SQLException("Result set is closed");
		}
		
		return ResultSet.FETCH_FORWARD;
	}

	@Override
	public void setFetchSize(int i) throws SQLException {
		if (this.isClosed) {
			throw new SQLException("Result set is closed");
		}
		
		// The provided value is ignored
	}

	@Override
	public int getFetchSize() throws SQLException {
		if (this.isClosed) {
			throw new SQLException("Result set is closed");
		}
		
		return 0;
	}

	@Override
	public int getType() throws SQLException {
		if (this.isClosed) {
			throw new SQLException("Result set is closed");
		}
		
		return ResultSet.TYPE_FORWARD_ONLY;
	}

	@Override
	public int getConcurrency() throws SQLException {
		if (this.isClosed) {
			throw new SQLException("Result set is closed");
		}
		
		return ResultSet.CONCUR_UPDATABLE;
	}

	@Override
	public boolean rowUpdated() throws SQLException {
		// TODO: how do we detect this?
		return false;
	}

	@Override
	public boolean rowInserted() throws SQLException {
		// TODO: how do we detect this?
		return false;
	}

	@Override
	public boolean rowDeleted() throws SQLException {
		// TODO: how do we detect this?
		return false;
	}

	@Override
	public void updateNull(int i) throws SQLException {
		// TODO: create an update command
	}

	@Override
	public void updateBoolean(int i, boolean b) throws SQLException {
		// TODO: create an update command
	}

	@Override
	public void updateByte(int i, byte b) throws SQLException {
		// TODO: create an update command
	}

	@Override
	public void updateShort(int i, short i1) throws SQLException {
		// TODO: create an update command
	}

	@Override
	public void updateInt(int i, int i1) throws SQLException {
		// TODO: create an update command
	}

	@Override
	public void updateLong(int i, long l) throws SQLException {
		// TODO: create an update command
	}

	@Override
	public void updateFloat(int i, float v) throws SQLException {
		// TODO: create an update command
	}

	@Override
	public void updateDouble(int i, double v) throws SQLException {
		// TODO: create an update command
	}

	@Override
	public void updateBigDecimal(int i, BigDecimal bigDecimal) throws SQLException {
		// TODO: create an update command
	}

	@Override
	public void updateString(int i, String s) throws SQLException {
		// TODO: create an update command
	}

	@Override
	public void updateBytes(int i, byte[] bytes) throws SQLException {
		// TODO: create an update command
	}

	@Override
	public void updateDate(int i, Date date) throws SQLException {
		// TODO: create an update command
	}

	@Override
	public void updateTime(int i, Time time) throws SQLException {
		// TODO: create an update command
	}

	@Override
	public void updateTimestamp(int i, Timestamp timestamp) throws SQLException {
		// TODO: create an update command
	}

	@Override
	public void updateAsciiStream(int i, InputStream inputStream, int i1) throws SQLException {
		// TODO: create an update command
	}

	@Override
	public void updateBinaryStream(int i, InputStream inputStream, int i1) throws SQLException {
		// TODO: create an update command
	}

	@Override
	public void updateCharacterStream(int i, Reader reader, int i1) throws SQLException {
		// TODO: create an update command
	}

	@Override
	public void updateObject(int i, Object o, int i1) throws SQLException {
		// TODO: create an update command
	}

	@Override
	public void updateObject(int i, Object o) throws SQLException {
		// TODO: create an update command
	}

	@Override
	public void updateNull(String s) throws SQLException {
		// TODO: create an update command
	}

	@Override
	public void updateBoolean(String s, boolean b) throws SQLException {
		// TODO: create an update command
	}

	@Override
	public void updateByte(String s, byte b) throws SQLException {
		// TODO: create an update command
	}

	@Override
	public void updateShort(String s, short i) throws SQLException {
		// TODO: create an update command
	}

	@Override
	public void updateInt(String s, int i) throws SQLException {
		// TODO: create an update command
	}

	@Override
	public void updateLong(String s, long l) throws SQLException {
		// TODO: create an update command
	}

	@Override
	public void updateFloat(String s, float v) throws SQLException {
		// TODO: create an update command
	}

	@Override
	public void updateDouble(String s, double v) throws SQLException {
		// TODO: create an update command
	}

	@Override
	public void updateBigDecimal(String s, BigDecimal bigDecimal) throws SQLException {
		// TODO: create an update command
	}

	@Override
	public void updateString(String s, String s1) throws SQLException {
		// TODO: create an update command
	}

	@Override
	public void updateBytes(String s, byte[] bytes) throws SQLException {
		// TODO: create an update command
	}

	@Override
	public void updateDate(String s, Date date) throws SQLException {
		// TODO: create an update command
	}

	@Override
	public void updateTime(String s, Time time) throws SQLException {
		// TODO: create an update command
	}

	@Override
	public void updateTimestamp(String s, Timestamp timestamp) throws SQLException {
		// TODO: create an update command
	}

	@Override
	public void updateAsciiStream(String s, InputStream inputStream, int i) throws SQLException {
		// TODO: create an update command
	}

	@Override
	public void updateBinaryStream(String s, InputStream inputStream, int i) throws SQLException {
		// TODO: create an update command
	}

	@Override
	public void updateCharacterStream(String s, Reader reader, int i) throws SQLException {
		// TODO: create an update command
	}

	@Override
	public void updateObject(String s, Object o, int i) throws SQLException {
		// TODO: create an update command
	}

	@Override
	public void updateObject(String s, Object o) throws SQLException {
		// TODO: create an update command
	}

	@Override
	public void insertRow() throws SQLException {
		// TODO: create an insert command
	}

	@Override
	public void updateRow() throws SQLException {
		// TODO: create an update command
	}

	@Override
	public void deleteRow() throws SQLException {
		// TODO: create a delete command
	}

	@Override
	public void refreshRow() throws SQLException {
		throw new SQLFeatureNotSupportedException("refreshRow is not supported");
	}

	@Override
	public void cancelRowUpdates() throws SQLException {
		if (this.isClosed) {
			throw new SQLException("Result set is closed");
		}
		
		if (this.updates.containsKey(this.cursor)) {
			this.updates.remove(this.cursor);
		}
	}

	@Override
	public void moveToInsertRow() throws SQLException {
		if (this.isClosed) {
			throw new SQLException("Result set is closed");
		}
		
		if (this.cursor == INSERT_ROW) {
			return;
		}
		
		this.prevCursor = this.cursor;
		this.cursor = INSERT_ROW;
	}

	@Override
	public void moveToCurrentRow() throws SQLException {
		if (this.isClosed) {
			throw new SQLException("Result set is closed");
		}
		
		if (this.cursor != INSERT_ROW) {
			return;
		}
		
		this.cursor = this.prevCursor;
		this.prevCursor = this.cursor;
	}

	@Override
	public Statement getStatement() throws SQLException {
		if (this.isClosed) {
			throw new SQLException("Result set is closed");
		}
		
		return this.statement;
	}

	@Override
	public Object getObject(int i, Map<String, Class<?>> map) throws SQLException {
		throw new SQLFeatureNotSupportedException("getObject is not currently supported"); // TODO
	}

	@Override
	public Ref getRef(int i) throws SQLException {
		throw new SQLFeatureNotSupportedException("getRef is not currently supported"); // TODO
	}

	@Override
	public Blob getBlob(int i) throws SQLException {
		throw new SQLFeatureNotSupportedException("getBlob is not currently supported"); // TODO
	}

	@Override
	public Clob getClob(int i) throws SQLException {
		throw new SQLFeatureNotSupportedException("getClob is not currently supported"); // TODO
	}

	@Override
	public Array getArray(int i) throws SQLException {
		throw new SQLFeatureNotSupportedException("getArray is not supported");
	}

	@Override
	public Object getObject(String s, Map<String, Class<?>> map) throws SQLException {
		throw new SQLFeatureNotSupportedException("getObject is not currently supported"); // TODO
	}

	@Override
	public Ref getRef(String s) throws SQLException {
		throw new SQLFeatureNotSupportedException("getRef is not currently supported"); // TODO
	}

	@Override
	public Blob getBlob(String s) throws SQLException {
		throw new SQLFeatureNotSupportedException("getBlob is not currently supported"); // TODO
	}

	@Override
	public Clob getClob(String s) throws SQLException {
		throw new SQLFeatureNotSupportedException("getClob is not currently supported"); // TODO
	}

	@Override
	public Array getArray(String s) throws SQLException {
		throw new SQLFeatureNotSupportedException("getArray is not supported");
	}

	@Override
	public Date getDate(int i, Calendar calendar) throws SQLException {
		// TODO: don't ignore calendar
		return this.getDate(i);
	}

	@Override
	public Date getDate(String s, Calendar calendar) throws SQLException {
		// TODO: don't ignore calendar
		return this.getDate(s);
	}

	@Override
	public Time getTime(int i, Calendar calendar) throws SQLException {
		// TODO: don't ignore calendar
		return this.getTime(i);
	}

	@Override
	public Time getTime(String s, Calendar calendar) throws SQLException {
		// TODO: don't ignore calendar
		return this.getTime(s);
	}

	@Override
	public Timestamp getTimestamp(int i, Calendar calendar) throws SQLException {
		// TODO: don't ignore calendar
		return this.getTimestamp(i);
	}

	@Override
	public Timestamp getTimestamp(String s, Calendar calendar) throws SQLException {
		// TODO: don't ignore calendar
		return this.getTimestamp(s);
	}

	@Override
	public URL getURL(int i) throws SQLException {
		String value = this.getString(i);
		
		if (value == null || value.length() == 0) {
			return null;
		}
		
		try {
			return new URL(value);
		} catch (MalformedURLException e) {
			return null;
		}
	}

	@Override
	public URL getURL(String s) throws SQLException {
		int index = this.columnNameToIndex(s);
		
		return this.getURL(index);
	}

	@Override
	public void updateRef(int i, Ref ref) throws SQLException {
		throw new SQLFeatureNotSupportedException("updateRef is not currently supported"); // TODO
	}

	@Override
	public void updateRef(String s, Ref ref) throws SQLException {
		throw new SQLFeatureNotSupportedException("updateRef is not currently supported"); // TODO
	}

	@Override
	public void updateBlob(int i, Blob blob) throws SQLException {
		throw new SQLFeatureNotSupportedException("updateBlob is not currently supported"); // TODO
	}

	@Override
	public void updateBlob(String s, Blob blob) throws SQLException {
		throw new SQLFeatureNotSupportedException("updateBlob is not currently supported"); // TODO
	}

	@Override
	public void updateClob(int i, Clob clob) throws SQLException {
		throw new SQLFeatureNotSupportedException("updateClob is not currently supported"); // TODO
	}

	@Override
	public void updateClob(String s, Clob clob) throws SQLException {
		throw new SQLFeatureNotSupportedException("updateClob is not currently supported"); // TODO
	}

	@Override
	public void updateArray(int i, Array array) throws SQLException {
		throw new SQLFeatureNotSupportedException("updateArray is not supported");
	}

	@Override
	public void updateArray(String s, Array array) throws SQLException {
		throw new SQLFeatureNotSupportedException("updateArray is not supported");
	}

	@Override
	public RowId getRowId(int i) throws SQLException {
		// TODO: will it ever be?
		throw new SQLFeatureNotSupportedException("getRowId is not currently supported"); // TODO
	}

	@Override
	public RowId getRowId(String s) throws SQLException {
		// TODO: will it ever be?
		throw new SQLFeatureNotSupportedException("getRowId is not currently supported"); // TODO
	}

	@Override
	public void updateRowId(int i, RowId rowId) throws SQLException {
		// TODO: will it ever be?
		throw new SQLFeatureNotSupportedException("updateRowId is not currently supported"); // TODO
	}

	@Override
	public void updateRowId(String s, RowId rowId) throws SQLException {
		// TODO: will it ever be?
		throw new SQLFeatureNotSupportedException("updateRowId is not currently supported"); // TODO
	}

	@Override
	public int getHoldability() throws SQLException {
		if (this.isClosed) {
			throw new SQLException("Result set is closed");
		}
		
		return ResultSet.CLOSE_CURSORS_AT_COMMIT;
	}

	@Override
	public boolean isClosed() throws SQLException {
		return this.isClosed;
	}

	@Override
	public void updateNString(int i, String s) throws SQLException {
		// TODO: create an update command
	}

	@Override
	public void updateNString(String s, String s1) throws SQLException {
		// TODO: create an update command
	}

	@Override
	public void updateNClob(int i, NClob nClob) throws SQLException {
		throw new SQLFeatureNotSupportedException("updateNClob is not currently supported"); // TODO
	}

	@Override
	public void updateNClob(String s, NClob nClob) throws SQLException {
		throw new SQLFeatureNotSupportedException("updateNClob is not currently supported"); // TODO
	}

	@Override
	public NClob getNClob(int i) throws SQLException {
		throw new SQLFeatureNotSupportedException("getNClob is not currently supported"); // TODO
	}

	@Override
	public NClob getNClob(String s) throws SQLException {
		throw new SQLFeatureNotSupportedException("getNClob is not currently supported"); // TODO
	}

	@Override
	public SQLXML getSQLXML(int i) throws SQLException {
		throw new SQLFeatureNotSupportedException("getSQLXML is not currently supported"); // TODO
	}

	@Override
	public SQLXML getSQLXML(String s) throws SQLException {
		throw new SQLFeatureNotSupportedException("getSQLXML is not currently supported"); // TODO
	}

	@Override
	public void updateSQLXML(int i, SQLXML sqlxml) throws SQLException {
		throw new SQLFeatureNotSupportedException("updateSQLXML is not currently supported"); // TODO
	}

	@Override
	public void updateSQLXML(String s, SQLXML sqlxml) throws SQLException {
		throw new SQLFeatureNotSupportedException("updateSQLXML is not currently supported"); // TODO
	}

	@Override
	public String getNString(int i) throws SQLException {
		return this.getString(i);
	}

	@Override
	public String getNString(String s) throws SQLException {
		return this.getString(s);
	}

	@Override
	public Reader getNCharacterStream(int i) throws SQLException {
		throw new SQLFeatureNotSupportedException("getNCharacterStream is not currently supported"); // TODO
	}

	@Override
	public Reader getNCharacterStream(String s) throws SQLException {
		throw new SQLFeatureNotSupportedException("getNCharacterStream is not currently supported"); // TODO
	}

	@Override
	public void updateNCharacterStream(int i, Reader reader, long l) throws SQLException {
		throw new SQLFeatureNotSupportedException("updateNCharacterStream is not currently supported"); // TODO
	}

	@Override
	public void updateNCharacterStream(String s, Reader reader, long l) throws SQLException {
		throw new SQLFeatureNotSupportedException("updateNCharacterStream is not currently supported"); // TODO
	}

	@Override
	public void updateAsciiStream(int i, InputStream inputStream, long l) throws SQLException {
		throw new SQLFeatureNotSupportedException("updateAsciiStream is not currently supported"); // TODO
	}

	@Override
	public void updateBinaryStream(int i, InputStream inputStream, long l) throws SQLException {
		throw new SQLFeatureNotSupportedException("updateAsciiStream is not currently supported"); // TODO
	}

	@Override
	public void updateCharacterStream(int i, Reader reader, long l) throws SQLException {
		throw new SQLFeatureNotSupportedException("updateCharacterStream is not currently supported"); // TODO
	}

	@Override
	public void updateAsciiStream(String s, InputStream inputStream, long l) throws SQLException {
		throw new SQLFeatureNotSupportedException("updateCharacterStream is not currently supported"); // TODO
	}

	@Override
	public void updateBinaryStream(String s, InputStream inputStream, long l) throws SQLException {
		throw new SQLFeatureNotSupportedException("updateBinaryStream is not currently supported"); // TODO
	}

	@Override
	public void updateCharacterStream(String s, Reader reader, long l) throws SQLException {
		throw new SQLFeatureNotSupportedException("updateBinaryStream is not currently supported"); // TODO
	}

	@Override
	public void updateBlob(int i, InputStream inputStream, long l) throws SQLException {
		throw new SQLFeatureNotSupportedException("updateBlob is not currently supported"); // TODO
	}

	@Override
	public void updateBlob(String s, InputStream inputStream, long l) throws SQLException {
		throw new SQLFeatureNotSupportedException("updateBlob is not currently supported"); // TODO
	}

	@Override
	public void updateClob(int i, Reader reader, long l) throws SQLException {
		throw new SQLFeatureNotSupportedException("updateClob is not currently supported"); // TODO
	}

	@Override
	public void updateClob(String s, Reader reader, long l) throws SQLException {
		throw new SQLFeatureNotSupportedException("updateClob is not currently supported"); // TODO
	}

	@Override
	public void updateNClob(int i, Reader reader, long l) throws SQLException {
		throw new SQLFeatureNotSupportedException("updateNClob is not currently supported"); // TODO
	}

	@Override
	public void updateNClob(String s, Reader reader, long l) throws SQLException {
		throw new SQLFeatureNotSupportedException("updateNClob is not currently supported"); // TODO
	}

	@Override
	public void updateNCharacterStream(int i, Reader reader) throws SQLException {
		throw new SQLFeatureNotSupportedException("updateNCharacterStream is not currently supported"); // TODO
	}

	@Override
	public void updateNCharacterStream(String s, Reader reader) throws SQLException {
		throw new SQLFeatureNotSupportedException("updateNCharacterStream is not currently supported"); // TODO
	}

	@Override
	public void updateAsciiStream(int i, InputStream inputStream) throws SQLException {
		throw new SQLFeatureNotSupportedException("updateAsciiStream is not currently supported"); // TODO
	}

	@Override
	public void updateBinaryStream(int i, InputStream inputStream) throws SQLException {
		throw new SQLFeatureNotSupportedException("updateAsciiStream is not currently supported"); // TODO
	}

	@Override
	public void updateCharacterStream(int i, Reader reader) throws SQLException {
		throw new SQLFeatureNotSupportedException("updateCharacterStream is not currently supported"); // TODO
	}

	@Override
	public void updateAsciiStream(String s, InputStream inputStream) throws SQLException {
		throw new SQLFeatureNotSupportedException("updateCharacterStream is not currently supported"); // TODO
	}

	@Override
	public void updateBinaryStream(String s, InputStream inputStream) throws SQLException {
		throw new SQLFeatureNotSupportedException("updateBinaryStream is not currently supported"); // TODO
	}

	@Override
	public void updateCharacterStream(String s, Reader reader) throws SQLException {
		throw new SQLFeatureNotSupportedException("updateBinaryStream is not currently supported"); // TODO
	}

	@Override
	public void updateBlob(int i, InputStream inputStream) throws SQLException {
		throw new SQLFeatureNotSupportedException("updateBlob is not currently supported"); // TODO
	}

	@Override
	public void updateBlob(String s, InputStream inputStream) throws SQLException {
		throw new SQLFeatureNotSupportedException("updateBlob is not currently supported"); // TODO
	}

	@Override
	public void updateClob(int i, Reader reader) throws SQLException {
		throw new SQLFeatureNotSupportedException("updateClob is not currently supported"); // TODO
	}

	@Override
	public void updateClob(String s, Reader reader) throws SQLException {
		throw new SQLFeatureNotSupportedException("updateClob is not currently supported"); // TODO
	}

	@Override
	public void updateNClob(int i, Reader reader) throws SQLException {
		throw new SQLFeatureNotSupportedException("updateNClob is not currently supported"); // TODO
	}

	@Override
	public void updateNClob(String s, Reader reader) throws SQLException {
		throw new SQLFeatureNotSupportedException("updateNClob is not currently supported"); // TODO
	}

	@Override
	public <T> T getObject(int i, Class<T> aClass) throws SQLException {
		throw new SQLFeatureNotSupportedException("getObject is not currently supported"); // TODO
	}

	@Override
	public <T> T getObject(String s, Class<T> aClass) throws SQLException {
		throw new SQLFeatureNotSupportedException("getObject is not currently supported"); // TODO
	}

	@Override
	public <T> T unwrap(Class<T> aClass) throws SQLException {
		return null;
	}

	@Override
	public boolean isWrapperFor(Class<?> aClass) throws SQLException {
		return false;
	}
	
	private void validateColumnIndex(int columnIndex) throws SQLException {
		if (this.columnsSet == null || columnIndex <= 0 || columnIndex > this.columnsSet.size()) {
			throw new SQLException("Invalid column index: " + columnIndex);
		}
	}
	
	private String getDataAt(int columnIndex) throws SQLException {
		this.validateColumnIndex(columnIndex);
		
		if (this.isClosed || this.data == null || this.cursor < 0 || this.cursor >= this.data.size()) {
			throw new SQLException("Row data at cursor: " + this.cursor + " is not accessible");
		}
		
		try {
			return this.data.get(this.cursor).get(columnIndex - 1);
		} catch (Exception e) {
			return null;
		}
	}
	
	private int columnNameToIndex(String columnName) {
		return this.columnsSet.indexOf(columnName) + 1;
	}
}
