package jfoundation.sql;
/*package com.db.jfoundation.sql;

import com.pansoft.pub.util.DateFunction;
import com.pansoft.redis.RedisClientSyn;
import com.pansoft.sql.JGetCachedTables;
import com.pansoft.sql.JSqlUtil;
import com.sybase.jdbc2.utils.Debug;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import redis.clients.jedis.ShardedJedis;

public class JConnection implements Connection {
	public static final String DBTYPE_SYBASE = "SYBASE";
	public static final String DBTYPE_ORACLE = "ORACLE";
	public static final String DBTYPE_OTHER = "OTHER";
	public static final boolean ISCACHED = 0;
	public static Class DriverClass = null;

	private Connection connection = null;
	private String dbtype;
	private String dbName;
	private char QuoteChar = ' ';

	private String mapKey = null;

	private static int mCreateConnCount = 0;
	private static int mDropConnCount = 0;
	private String cachedSqlConn;
	private String sqlLxConn;
	private String isCachedConn = "0";
	private String sql;
	private List isNeedCacheTableList = new ArrayList();

	Map cachedTableTimestamp = new HashMap();

	public static String beginTime = "0";

	protected com.efounder.sql.JConnection espConnection = null;

	public void setConnection(Connection conn) {
		this.connection = conn;
	}

	public void setDbtype(String dbtype) {
		this.dbtype = dbtype;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public Map getCachedTableTimestamp() {
		return this.cachedTableTimestamp;
	}

	public String getSql() {
		return this.sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public List getIsNeedCacheTableList() {
		return this.isNeedCacheTableList;
	}

	public void setIsNeedCacheTableList(List isNeedCacheTableList) {
		this.isNeedCacheTableList = isNeedCacheTableList;
	}

	public String getCachedSqlConn() {
		return this.cachedSqlConn;
	}

	public void setCachedSqlConn(String cachedSqlConn) {
		this.cachedSqlConn = cachedSqlConn;
	}

	public String getSqlLxConn() {
		return this.sqlLxConn;
	}

	public void setSqlLxConn(String sqlLxConn) {
		this.sqlLxConn = sqlLxConn;
	}

	public String getIsCachedConn() {
		return this.isCachedConn;
	}

	public void setIsCachedConn(String isCachedConn) {
		this.isCachedConn = isCachedConn;
	}

	public Connection getConnection() {
		if (this.connection == null) {
			return null;
		}
		return this;
	}

	public Connection getOrialConnection() {
		return this.connection;
	}

	public String getDbtype() {
		return this.dbtype;
	}

	public String getDbName() {
		return this.dbName;
	}

	public void setIdentifierQuoteChar(char qc) {
		this.QuoteChar = qc;
	}

	public char getIdentifierQuoteChar() {
		return this.QuoteChar;
	}

	public void setMapKey(String key) {
		this.mapKey = key;
	}

	public String getMapKey() {
		return this.mapKey;
	}

	public JSQLToolkit getSQLToolkit() {
		return null;
	}

	public int CheckDatabase(Object PO) {
		return 0;
	}

	public Object InitConnection(Object ParamObject, Object DataObject,
			Object CustomObject, Object AdditiveObject) {
		mCreateConnCount += 1;
		return null;
	}

	public Object InitConnection(Object ParamObject) {
		return null;
	}

	public void clearWarnings() {
		try {
			this.connection.clearWarnings();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			if ((this.connection != null) && (!(this.connection.isClosed()))) {
				this.connection.close();

				mDropConnCount += 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void commit() {
		try {
			this.connection.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Statement createStatement() {
		try {
			return new JStatement(this, this.connection.createStatement());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Statement GetStatement(JConnection connection) {
		try {
			return new JStatement(connection, connection.createStatement());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void BackStatement(Statement stmt, ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (stmt == null)
			return;
		try {
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void BackPreparedStatement(PreparedStatement stmt,
			ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (stmt == null)
			return;
		try {
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Statement createStatement(int resultSetType, int resultSetConcurrency) {
		try {
			return new JStatement(this, this.connection.createStatement(
					resultSetType, resultSetConcurrency));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void compareCached(String sql)
  {
    ResultSet rs = null;

    List haveUpdateTables = new ArrayList();

    List tableList = new ArrayList();

    List cachedTablesList = new ArrayList();

    String msString = JSqlUtil.getSqlms(sql);
    setSqlLxConn(msString);

    tableList = JSqlUtil.getTablesBySql(sql);

    setCachedSqlConn(sql);

    JGetCachedTables tables = new JGetCachedTables();
    cachedTablesList = tables.getCachedTables();

    for (int i = 0; i < cachedTablesList.size(); ++i) {
      if (tableList.contains(cachedTablesList.get(i))) {
        setIsCachedConn("1");
        this.isNeedCacheTableList.add(cachedTablesList.get(i));
      }
    }

    RedisClientSyn client = new RedisClientSyn();
    ShardedJedis shardedJedis = client.getShardedJedis();
    for (int i = 0; i < this.isNeedCacheTableList.size(); ++i) {
      String timeString = shardedJedis.get("timestamp" + this.isNeedCacheTableList.get(i));

      if (timeString == null) {
        timeString = "19000101000000";
        shardedJedis.set("timestamp" + this.isNeedCacheTableList.get(i), timeString);
      }
      this.cachedTableTimestamp.put("timestamp" + this.isNeedCacheTableList.get(i), timeString);
    }

    if ((this.isNeedCacheTableList != null) && (this.isNeedCacheTableList.size() > 0)) {
      for (int i = 0; i < this.isNeedCacheTableList.size(); ++i) {
        Statement stmts = null;
        try {
          stmts = this.connection.createStatement();
          rs = stmts.executeQuery("SELECT max(to_char(EXTENDED_timestamp,'yyyymmddhh24miss')) updatetime from sys.dba_audit_object where obj_name='" + ((String)this.isNeedCacheTableList.get(i)).toUpperCase() + "'");
          if (!(rs.next())) break label592;
          String updateTime = rs.getString("updatetime");
          if (updateTime == null) {
            updateTime = "19000101000001";
            Debug.println("表" + this.isNeedCacheTableList.get(i) + "未启用审计");
          }

          if (updateTime.compareTo((String)this.cachedTableTimestamp.get("timestamp" + this.isNeedCacheTableList.get(i))) > 0) {
            shardedJedis.del("timestamp" + this.isNeedCacheTableList.get(i));
            shardedJedis.set("timestamp" + this.isNeedCacheTableList.get(i), updateTime);
            haveUpdateTables.add(this.isNeedCacheTableList.get(i));

            setIsCachedConn("1");
            break label693: }
          setIsCachedConn("2");
          label693: label592: this.cachedSqlConn = sql;
        }
        catch (SQLException e)
        {
          e.printStackTrace();
        } finally {
          if (stmts != null) {
            try {
              stmts.close();
            }
            catch (SQLException e) {
              e.printStackTrace();
            }
          }
          if (rs != null) {
            try {
              rs.close();
            }
            catch (SQLException e) {
              e.printStackTrace();
            }
          }
        }
      }

    }

    if (getIsCachedConn().equals("1"))
    {
      for (int i = 0; i < haveUpdateTables.size(); ++i) {
        List list = shardedJedis.lrange("redissql" + haveUpdateTables.get(i), 0L, -1L);
        for (int j = 0; j < list.size(); ++j) {
          shardedJedis.lpop("redissql" + haveUpdateTables.get(i));
        }
      }

      if (shardedJedis.exists(getCachedSqlConn()).booleanValue()) {
        shardedJedis.del(getCachedSqlConn());
      }

    }

    client.destorySharedPool();
  }

	public boolean getAutoCommit() {
		try {
			return this.connection.getAutoCommit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public String getCatalog() {
		try {
			return this.connection.getCatalog();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public DatabaseMetaData getMetaData() {
		try {
			return this.connection.getMetaData();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public int getTransactionIsolation() {
		try {
			return this.connection.getTransactionIsolation();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public Map getTypeMap() {
		try {
			return this.connection.getTypeMap();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public SQLWarning getWarnings() {
		try {
			return this.connection.getWarnings();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean isClosed() {
		try {
			return this.connection.isClosed();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public boolean isReadOnly() {
		try {
			return this.connection.isReadOnly();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public String nativeSQL(String sql) {
		try {
			sql = ConvertSQL.convertSQL(this.dbName, this.dbtype, sql);
			return this.connection.nativeSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public CallableStatement prepareCall(String sql) {
		try {
			sql = ConvertSQL.convertSQL(this.dbName, this.dbtype, sql);
			return this.connection.prepareCall(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency) {
		try {
			sql = ConvertSQL.convertSQL(this.dbName, this.dbtype, sql);
			return this.connection.prepareCall(sql, resultSetType,
					resultSetConcurrency);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public PreparedStatement prepareStatement(String sql) {
		try {
			beginTime = DateFunction.getCurrentTime();
			sql = ConvertSQL.convertSQL(this.dbName, getDbtype(), sql);

			return new JPreparedStatement(this,
					this.connection.prepareStatement(sql));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency) {
		try {
			sql = ConvertSQL.convertSQL(this.dbName, getDbtype(), sql);
			return new JPreparedStatement(this,
					this.connection.prepareStatement(sql, resultSetType,
							resultSetConcurrency));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void rollback() {
		try {
			this.connection.rollback();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setAutoCommit(boolean autoCommit) {
		try {
			this.connection.setAutoCommit(autoCommit);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setCatalog(String catalog) {
		try {
			this.connection.setCatalog(catalog);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setReadOnly(boolean readOnly) {
		try {
			this.connection.setReadOnly(readOnly);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setTransactionIsolation(int level) {
		try {
			this.connection.setTransactionIsolation(level);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean ExecuteSQL(String SQL) {
		boolean Res = false;
		Statement st = GetStatement(this);
		try {
			Res = st.execute(SQL);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			BackStatement(st, null);
		}
		return Res;
	}

	public boolean ExecuteUpdate(String SQL) {
		boolean Res = true;
		Statement st = GetStatement(this);
		try {
			st.executeUpdate(SQL);
		} catch (Exception e) {
			Res = false;
			e.printStackTrace();
		} finally {
			BackStatement(st, null);
		}
		return Res;
	}

	public Object changePassword(Object PO) {
		return null;
	}

	public PreparedStatement prepareStatement(String sql, String[] columnIndexes)
			throws SQLException {
		sql = ConvertSQL.convertSQL(this.dbName, this.dbtype, sql);
		return this.connection.prepareStatement(sql, columnIndexes);
	}

	public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
			throws SQLException {
		sql = ConvertSQL.convertSQL(this.dbName, this.dbtype, sql);
		return this.connection.prepareStatement(sql, columnIndexes);
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		sql = ConvertSQL.convertSQL(this.dbName, this.dbtype, sql);
		return this.connection.prepareStatement(sql, resultSetType,
				resultSetConcurrency, resultSetHoldability);
	}

	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
			throws SQLException {
		sql = ConvertSQL.convertSQL(this.dbName, this.dbtype, sql);
		return this.connection.prepareStatement(sql, autoGeneratedKeys);
	}

	public Savepoint setSavepoint() throws SQLException {
		return this.connection.setSavepoint();
	}

	public Savepoint setSavepoint(String sql) throws SQLException {
		sql = ConvertSQL.convertSQL(this.dbName, this.dbtype, sql);
		return this.connection.setSavepoint(sql);
	}

	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		sql = ConvertSQL.convertSQL(this.dbName, this.dbtype, sql);
		return this.connection.prepareCall(sql, resultSetType,
				resultSetConcurrency, resultSetHoldability);
	}

	public Statement createStatement(int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		return this.connection.createStatement(resultSetType,
				resultSetConcurrency, resultSetHoldability);
	}

	public void rollback(Savepoint savepoint) throws SQLException {
		this.connection.rollback();
	}

	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		this.connection.releaseSavepoint(savepoint);
	}

	public void setHoldability(int holdability) throws SQLException {
		this.connection.setHoldability(holdability);
	}

	public int getHoldability() throws SQLException {
		return this.connection.getHoldability();
	}

	public boolean isOracle() {
		return this.dbtype.equals("ORACLE");
	}

	public boolean isSybase() {
		return this.dbtype.equals("SYBASE");
	}

	public static int getCreateConnCount() {
		return mCreateConnCount;
	}

	public static int getDropConnCount() {
		return mDropConnCount;
	}

	public void setESPConnection(com.efounder.sql.JConnection conn) {
		this.espConnection = conn;
		setConnection(this.espConnection.getConnection());
	}

	public com.efounder.sql.JConnection getESPConnection() {
		return this.espConnection;
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		return null;
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}

	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
	}

	public Clob createClob() throws SQLException {
		return null;
	}

	public Blob createBlob() throws SQLException {
		return null;
	}

	public NClob createNClob() throws SQLException {
		return null;
	}

	public SQLXML createSQLXML() throws SQLException {
		return null;
	}

	public boolean isValid(int timeout) throws SQLException {
		return false;
	}

	public void setClientInfo(String name, String value)
			throws SQLClientInfoException {
	}

	public void setClientInfo(Properties properties)
			throws SQLClientInfoException {
	}

	public String getClientInfo(String name) throws SQLException {
		return null;
	}

	public Properties getClientInfo() throws SQLException {
		return null;
	}

	public Array createArrayOf(String typeName, Object[] elements)
			throws SQLException {
		return null;
	}

	public Struct createStruct(String typeName, Object[] attributes)
			throws SQLException {
		return null;
	}
}*/