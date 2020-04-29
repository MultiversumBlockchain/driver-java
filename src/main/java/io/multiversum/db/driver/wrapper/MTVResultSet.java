package io.multiversum.db.driver.wrapper;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * @see https://docs.oracle.com/javase/7/docs/api/java/sql/ResultSet.html
 * @implNote Last implemented method: getBlob(int)
 */
public class MTVResultSet implements ResultSet {
	private int cursor = -1;
	private List<List<String>> data = null;
	private List<String> columnsSet = null;
	
	// TODO use a more correct structure
	private final HashMap<Integer, Object> updates = new HashMap<Integer, Object>();
	private final List<String> warnings = new ArrayList<String>();

	public MTVResultSet(List<List<String>> data, List<String> columnsSet) {
		this.data = data;
		this.columnsSet = columnsSet;
	}

	public MTVResultSet(List<List<String>> data) {
		this.data = data;
	}

	@Override
	public boolean next() throws SQLException {
		if (this.data != null && (this.cursor + 1) < this.data.size()) {
			this.cursor++;
			
			return true;
		}

		return false;
	}

	@Override
	public void close() throws SQLException {
		this.warnings.clear();
		this.updates.clear();

		if (this.data != null) {
			this.data.clear();
		}
		
		if (this.columnsSet != null) {
			this.columnsSet.clear();
		}
	}

	@Override
	public boolean wasNull() throws SQLException {
		return false;
	}

	@Override
	public String getString(int i) throws SQLException {
		return null;
	}

	@Override
	public boolean getBoolean(int i) throws SQLException {
		return false;
	}

	@Override
	public byte getByte(int i) throws SQLException {
		return 0;
	}

	@Override
	public short getShort(int i) throws SQLException {
		return 0;
	}

	@Override
	public int getInt(int i) throws SQLException {
		return 0;
	}

	@Override
	public long getLong(int i) throws SQLException {
		return 0;
		//return new Long((int) this.data.get(this.cur).get(this.columnsName.get(i - 1))); // TODO
	}

	@Override
	public float getFloat(int i) throws SQLException {
		return 0;
	}

	@Override
	public double getDouble(int i) throws SQLException {
		return 0;
	}

	@Override
	@Deprecated
	public BigDecimal getBigDecimal(int i, int scale) throws SQLException {
		return null;
	}

	@Override
	public byte[] getBytes(int i) throws SQLException {
		return new byte[0];
	}

	@Override
	public Date getDate(int i) throws SQLException {
		return null;
	}

	@Override
	public Time getTime(int i) throws SQLException {
		return null;
	}

	@Override
	public Timestamp getTimestamp(int i) throws SQLException {
		return null;
	}

	@Override
	public InputStream getAsciiStream(int i) throws SQLException {
		if (this.data == null) {
			return null;
		}
		
		return null; // TODO
	}

	@Override
	public InputStream getUnicodeStream(int i) throws SQLException {
		return null;
	}

	@Override
	public InputStream getBinaryStream(int i) throws SQLException {
		if (this.data == null) {
			return null;
		}
		
		return null; // TODO
	}

	@Override
	public String getString(String s) throws SQLException {
		System.out.println("getString(" + s + ")");
		return "";
		//HashMap<String, Object> hashMap = this.data.isEmpty() ? null : this.data.get(this.cur);
		//return Optional.ofNullable(hashMap).map(t -> (String) t.get(s.toUpperCase())).orElse(null);
	}

	@Override
	public boolean getBoolean(String s) throws SQLException {
		return false;
	}

	@Override
	public byte getByte(String s) throws SQLException {
		return 0;
	}

	@Override
	public short getShort(String s) throws SQLException {
		return 0;
	}

	@Override
	public int getInt(String s) throws SQLException {
		return 0;
	}

	@Override
	public long getLong(String s) throws SQLException {
		return 0;
	}

	@Override
	public float getFloat(String s) throws SQLException {
		return 0;
	}

	@Override
	public double getDouble(String s) throws SQLException {
		return 0;
	}

	@Override
	@Deprecated
	public BigDecimal getBigDecimal(String s, int scale) throws SQLException {
		return null;
	}

	@Override
	public byte[] getBytes(String s) throws SQLException {
		return new byte[0];
	}

	@Override
	public Date getDate(String s) throws SQLException {
		return null;
	}

	@Override
	public Time getTime(String s) throws SQLException {
		return null;
	}

	@Override
	public Timestamp getTimestamp(String s) throws SQLException {
		return null;
		//HashMap<String, Object> hashMap = this.mblResultSet.isEmpty() ? null : this.mblResultSet.get(this.cur);
		//return Optional.ofNullable(hashMap).map(t -> new Timestamp((Long) t.get(s.toUpperCase()))).orElse(null);
	}

	@Override
	public InputStream getAsciiStream(String s) throws SQLException {
		if (this.data == null) {
			return null;
		}
		
		return null; // TODO
	}

	@Override
	public InputStream getUnicodeStream(String s) throws SQLException {
		return null;
	}

	@Override
	public InputStream getBinaryStream(String s) throws SQLException {
		if (this.data == null) {
			return null;
		}
		
		return null; // TODO
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		return null;
	}

	@Override
	public void clearWarnings() throws SQLException {
		this.warnings.clear();
	}

	@Override
	public String getCursorName() throws SQLException {
		throw new UnsupportedOperationException("to be implemented");
	}

	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		throw new UnsupportedOperationException("to be implemented");
	}

	@Override
	public Object getObject(int i) throws SQLException {
		return null;
	}

	@Override
	public Object getObject(String s) throws SQLException {
		return null;
	}

	@Override
	public int findColumn(String s) throws SQLException {
		if (this.columnsSet == null) {
			throw new SQLException("Find column not permitted without a column set");
		}
		
		if (this.columnsSet.isEmpty()) {
			throw new SQLException("Could not find column in empty column set");
		}
		
		for (int i = 0; i < this.columnsSet.size(); i++) {
			if (this.columnsSet.get(i).equals(s)) {
				return i;
			}
		}

		throw new SQLException("Column " + s + " not found");
	}

	@Override
	public Reader getCharacterStream(int i) throws SQLException {
		return null;
	}

	@Override
	public Reader getCharacterStream(String s) throws SQLException {
		return null;
	}

	@Override
	public BigDecimal getBigDecimal(int i) throws SQLException {
		if (this.data == null || this.cursor < 0 || this.cursor >= this.data.size()) {
			return null;
		}
		
		String value = this.data.get(this.cursor).get(i);

		return new BigDecimal(value);
	}

	@Override
	public BigDecimal getBigDecimal(String s) throws SQLException {
		if (this.data == null || this.cursor < 0 || this.cursor >= this.data.size()) {
			return null;
		}
		
		int index = this.columnsSet.indexOf(s);
		String value = this.data.get(this.cursor).get(index);

		return new BigDecimal(value);
	}

	@Override
	public boolean isBeforeFirst() throws SQLException {
		return false;
	}

	@Override
	public boolean isAfterLast() throws SQLException {
		if (this.data != null) {
			this.cursor = this.data.size();
		} else {
			this.cursor = 0;
		}
		
		return true;
	}

	@Override
	public boolean isFirst() throws SQLException {
		return this.cursor == -1;
	}

	@Override
	public boolean isLast() throws SQLException {
		return this.cursor == this.data.size() - 1;
	}

	@Override
	public void beforeFirst() throws SQLException {
		this.cursor = -1;
	}

	@Override
	public void afterLast() throws SQLException {

	}

	@Override
	public boolean first() throws SQLException {
		if (this.data == null) {
			throw new SQLException("Empty result set");
		}
		
		this.cursor = 0;
		
		return true;
	}

	@Override
	public boolean last() throws SQLException {
		return false;
	}

	@Override
	public int getRow() throws SQLException {
		return this.cursor;
	}

	@Override
	public boolean absolute(int i) throws SQLException {
		if (this.data != null && i < this.data.size() && i >= 0) {
			this.cursor = i;
			
			return true;
		}
		
		return false;
	}

	@Override
	public boolean relative(int i) throws SQLException {
		return false;
	}

	@Override
	public boolean previous() throws SQLException {
		return false;
	}

	@Override
	public void setFetchDirection(int i) throws SQLException {

	}

	@Override
	public int getFetchDirection() throws SQLException {
		return 0;
	}

	@Override
	public void setFetchSize(int i) throws SQLException {

	}

	@Override
	public int getFetchSize() throws SQLException {
		return 0;
	}

	@Override
	public int getType() throws SQLException {
		return 0;
	}

	@Override
	public int getConcurrency() throws SQLException {
		return 0;
	}

	@Override
	public boolean rowUpdated() throws SQLException {
		return false;
	}

	@Override
	public boolean rowInserted() throws SQLException {
		return false;
	}

	@Override
	public boolean rowDeleted() throws SQLException {
		return false;
	}

	@Override
	public void updateNull(int i) throws SQLException {

	}

	@Override
	public void updateBoolean(int i, boolean b) throws SQLException {

	}

	@Override
	public void updateByte(int i, byte b) throws SQLException {

	}

	@Override
	public void updateShort(int i, short i1) throws SQLException {

	}

	@Override
	public void updateInt(int i, int i1) throws SQLException {

	}

	@Override
	public void updateLong(int i, long l) throws SQLException {

	}

	@Override
	public void updateFloat(int i, float v) throws SQLException {

	}

	@Override
	public void updateDouble(int i, double v) throws SQLException {

	}

	@Override
	public void updateBigDecimal(int i, BigDecimal bigDecimal) throws SQLException {

	}

	@Override
	public void updateString(int i, String s) throws SQLException {
		System.out.println();
	}

	@Override
	public void updateBytes(int i, byte[] bytes) throws SQLException {

	}

	@Override
	public void updateDate(int i, Date date) throws SQLException {

	}

	@Override
	public void updateTime(int i, Time time) throws SQLException {

	}

	@Override
	public void updateTimestamp(int i, Timestamp timestamp) throws SQLException {

	}

	@Override
	public void updateAsciiStream(int i, InputStream inputStream, int i1) throws SQLException {

	}

	@Override
	public void updateBinaryStream(int i, InputStream inputStream, int i1) throws SQLException {

	}

	@Override
	public void updateCharacterStream(int i, Reader reader, int i1) throws SQLException {

	}

	@Override
	public void updateObject(int i, Object o, int i1) throws SQLException {

	}

	@Override
	public void updateObject(int i, Object o) throws SQLException {

	}

	@Override
	public void updateNull(String s) throws SQLException {

	}

	@Override
	public void updateBoolean(String s, boolean b) throws SQLException {

	}

	@Override
	public void updateByte(String s, byte b) throws SQLException {

	}

	@Override
	public void updateShort(String s, short i) throws SQLException {

	}

	@Override
	public void updateInt(String s, int i) throws SQLException {

	}

	@Override
	public void updateLong(String s, long l) throws SQLException {

	}

	@Override
	public void updateFloat(String s, float v) throws SQLException {

	}

	@Override
	public void updateDouble(String s, double v) throws SQLException {

	}

	@Override
	public void updateBigDecimal(String s, BigDecimal bigDecimal) throws SQLException {

	}

	@Override
	public void updateString(String s, String s1) throws SQLException {
		System.out.println();
	}

	@Override
	public void updateBytes(String s, byte[] bytes) throws SQLException {

	}

	@Override
	public void updateDate(String s, Date date) throws SQLException {

	}

	@Override
	public void updateTime(String s, Time time) throws SQLException {

	}

	@Override
	public void updateTimestamp(String s, Timestamp timestamp) throws SQLException {

	}

	@Override
	public void updateAsciiStream(String s, InputStream inputStream, int i) throws SQLException {

	}

	@Override
	public void updateBinaryStream(String s, InputStream inputStream, int i) throws SQLException {

	}

	@Override
	public void updateCharacterStream(String s, Reader reader, int i) throws SQLException {

	}

	@Override
	public void updateObject(String s, Object o, int i) throws SQLException {

	}

	@Override
	public void updateObject(String s, Object o) throws SQLException {

	}

	@Override
	public void insertRow() throws SQLException {

	}

	@Override
	public void updateRow() throws SQLException {

	}

	@Override
	public void deleteRow() throws SQLException {
		if (this.data == null) {
			return;
		}
		
		try {
			this.data.remove(this.cursor);
		} catch (Exception e) {
			throw new SQLException("No row at current cursor position: " +  this.cursor);
		}
		
		// TODO perform a "delete" operation on the database
	}

	@Override
	public void refreshRow() throws SQLException {

	}

	@Override
	public void cancelRowUpdates() throws SQLException {
		if (this.updates.containsKey(this.cursor)) {
			this.updates.remove(this.cursor);
		}
	}

	@Override
	public void moveToInsertRow() throws SQLException {

	}

	@Override
	public void moveToCurrentRow() throws SQLException {

	}

	@Override
	public Statement getStatement() throws SQLException {
		return null;
	}

	@Override
	public Object getObject(int i, Map<String, Class<?>> map) throws SQLException {
		return null;
	}

	@Override
	public Ref getRef(int i) throws SQLException {
		return null;
	}

	@Override
	public Blob getBlob(int i) throws SQLException {
		if (this.data == null || this.cursor < 0 || this.cursor >= this.data.size()) {
			return null;
		}
		
		return null; // TODO
	}

	@Override
	public Clob getClob(int i) throws SQLException {
		return null;
	}

	@Override
	public Array getArray(int i) throws SQLException {
		if (this.data == null) {
			return null;
		}
		
		return null; // TODO
	}

	@Override
	public Object getObject(String s, Map<String, Class<?>> map) throws SQLException {
		return null;
	}

	@Override
	public Ref getRef(String s) throws SQLException {
		return null;
	}

	@Override
	public Blob getBlob(String s) throws SQLException {
		return null;
	}

	@Override
	public Clob getClob(String s) throws SQLException {
		return null;
	}

	@Override
	public Array getArray(String s) throws SQLException {
		if (this.data == null) {
			return null;
		}
		
		return null; // TODO
	}

	@Override
	public Date getDate(int i, Calendar calendar) throws SQLException {
		return null;
	}

	@Override
	public Date getDate(String s, Calendar calendar) throws SQLException {
		return null;
	}

	@Override
	public Time getTime(int i, Calendar calendar) throws SQLException {
		return null;
	}

	@Override
	public Time getTime(String s, Calendar calendar) throws SQLException {
		return null;
	}

	@Override
	public Timestamp getTimestamp(int i, Calendar calendar) throws SQLException {
		return null;
	}

	@Override
	public Timestamp getTimestamp(String s, Calendar calendar) throws SQLException {
		return null;
	}

	@Override
	public URL getURL(int i) throws SQLException {
		return null;
	}

	@Override
	public URL getURL(String s) throws SQLException {
		return null;
	}

	@Override
	public void updateRef(int i, Ref ref) throws SQLException {

	}

	@Override
	public void updateRef(String s, Ref ref) throws SQLException {

	}

	@Override
	public void updateBlob(int i, Blob blob) throws SQLException {

	}

	@Override
	public void updateBlob(String s, Blob blob) throws SQLException {

	}

	@Override
	public void updateClob(int i, Clob clob) throws SQLException {

	}

	@Override
	public void updateClob(String s, Clob clob) throws SQLException {

	}

	@Override
	public void updateArray(int i, Array array) throws SQLException {

	}

	@Override
	public void updateArray(String s, Array array) throws SQLException {

	}

	@Override
	public RowId getRowId(int i) throws SQLException {
		return null;
	}

	@Override
	public RowId getRowId(String s) throws SQLException {
		return null;
	}

	@Override
	public void updateRowId(int i, RowId rowId) throws SQLException {

	}

	@Override
	public void updateRowId(String s, RowId rowId) throws SQLException {

	}

	@Override
	public int getHoldability() throws SQLException {
		return 0;
	}

	@Override
	public boolean isClosed() throws SQLException {
		return false;
	}

	@Override
	public void updateNString(int i, String s) throws SQLException {

	}

	@Override
	public void updateNString(String s, String s1) throws SQLException {

	}

	@Override
	public void updateNClob(int i, NClob nClob) throws SQLException {

	}

	@Override
	public void updateNClob(String s, NClob nClob) throws SQLException {

	}

	@Override
	public NClob getNClob(int i) throws SQLException {
		return null;
	}

	@Override
	public NClob getNClob(String s) throws SQLException {
		return null;
	}

	@Override
	public SQLXML getSQLXML(int i) throws SQLException {
		return null;
	}

	@Override
	public SQLXML getSQLXML(String s) throws SQLException {
		return null;
	}

	@Override
	public void updateSQLXML(int i, SQLXML sqlxml) throws SQLException {

	}

	@Override
	public void updateSQLXML(String s, SQLXML sqlxml) throws SQLException {

	}

	@Override
	public String getNString(int i) throws SQLException {
		return null;
	}

	@Override
	public String getNString(String s) throws SQLException {
		return null;
	}

	@Override
	public Reader getNCharacterStream(int i) throws SQLException {
		return null;
	}

	@Override
	public Reader getNCharacterStream(String s) throws SQLException {
		return null;
	}

	@Override
	public void updateNCharacterStream(int i, Reader reader, long l) throws SQLException {

	}

	@Override
	public void updateNCharacterStream(String s, Reader reader, long l) throws SQLException {

	}

	@Override
	public void updateAsciiStream(int i, InputStream inputStream, long l) throws SQLException {

	}

	@Override
	public void updateBinaryStream(int i, InputStream inputStream, long l) throws SQLException {

	}

	@Override
	public void updateCharacterStream(int i, Reader reader, long l) throws SQLException {

	}

	@Override
	public void updateAsciiStream(String s, InputStream inputStream, long l) throws SQLException {

	}

	@Override
	public void updateBinaryStream(String s, InputStream inputStream, long l) throws SQLException {

	}

	@Override
	public void updateCharacterStream(String s, Reader reader, long l) throws SQLException {

	}

	@Override
	public void updateBlob(int i, InputStream inputStream, long l) throws SQLException {

	}

	@Override
	public void updateBlob(String s, InputStream inputStream, long l) throws SQLException {

	}

	@Override
	public void updateClob(int i, Reader reader, long l) throws SQLException {

	}

	@Override
	public void updateClob(String s, Reader reader, long l) throws SQLException {

	}

	@Override
	public void updateNClob(int i, Reader reader, long l) throws SQLException {

	}

	@Override
	public void updateNClob(String s, Reader reader, long l) throws SQLException {

	}

	@Override
	public void updateNCharacterStream(int i, Reader reader) throws SQLException {

	}

	@Override
	public void updateNCharacterStream(String s, Reader reader) throws SQLException {

	}

	@Override
	public void updateAsciiStream(int i, InputStream inputStream) throws SQLException {

	}

	@Override
	public void updateBinaryStream(int i, InputStream inputStream) throws SQLException {

	}

	@Override
	public void updateCharacterStream(int i, Reader reader) throws SQLException {

	}

	@Override
	public void updateAsciiStream(String s, InputStream inputStream) throws SQLException {

	}

	@Override
	public void updateBinaryStream(String s, InputStream inputStream) throws SQLException {

	}

	@Override
	public void updateCharacterStream(String s, Reader reader) throws SQLException {

	}

	@Override
	public void updateBlob(int i, InputStream inputStream) throws SQLException {

	}

	@Override
	public void updateBlob(String s, InputStream inputStream) throws SQLException {

	}

	@Override
	public void updateClob(int i, Reader reader) throws SQLException {

	}

	@Override
	public void updateClob(String s, Reader reader) throws SQLException {

	}

	@Override
	public void updateNClob(int i, Reader reader) throws SQLException {

	}

	@Override
	public void updateNClob(String s, Reader reader) throws SQLException {

	}

	@Override
	public <T> T getObject(int i, Class<T> aClass) throws SQLException {
		return null;
	}

	@Override
	public <T> T getObject(String s, Class<T> aClass) throws SQLException {
		return null;
	}

	@Override
	public <T> T unwrap(Class<T> aClass) throws SQLException {
		return null;
	}

	@Override
	public boolean isWrapperFor(Class<?> aClass) throws SQLException {
		return false;
	}
}
