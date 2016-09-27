package efounder.sql;

//import com.core.xml.PackageStub;
//import com.core.xml.StubObject;
//import com.efounder.eai.service.ParameterManager;
import java.io.PrintStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.naming.Context;
import javax.naming.InitialContext;
//import org.openide.util.Lookup;

public abstract class JConnection implements Connection {
	public static final String ORACLE = "ORACLE";
	public static final String MYSQL = "MYSQL";
	public static final String SQLSERVER = "SQLSERVER";
	public static final String SYBASE = "SYBASE";
	public static final String _SQL_HEAD_NO_CONVERT = "--_NO_CONVERT_\r\n";
	protected static boolean overload_protection = true;
	private static int threshold_default = 2000;

	protected int threshold = threshold_default;

	protected String dataBaseType = null;

	protected Class DriverClass = null;
	public Connection connection = null;
	protected Context dbContext = null;

	private char QuoteChar = ' ';
	protected static JConnection defaultConn;
	List sqlLogList = null;

	static {
		/*Vector ClassList = PackageStub.getContentVector("OVERLOAD_PROTECTION");
		if ((ClassList != null) && (ClassList.size() > 0)) {
			StubObject SO = (StubObject) ClassList.get(0);

			String open = SO.getString("OPEN", "1").trim();
			overload_protection = !("0".equals(open));

			String vsThreshold = SO.getString("ROW_COUNT_THRESHOLD", "0")
					.trim();

			int viThreshold = 0;
			try {
				viThreshold = Integer.parseInt(vsThreshold);
			} catch (Exception localException) {
			}
			if (viThreshold > 0) {
				threshold_default = viThreshold;
			}

		}*/

		defaultConn = null;
	}

	public String getDataBaseType() {
		return this.dataBaseType;
	}

	public void setDataBaseType(String dbt) {
		this.dataBaseType = dbt;
	}

	public Context getDBContext() throws Exception {
		return getDBContext(null);
	}

	public Context getDBContext(Hashtable env) throws Exception {
		if (this.dbContext == null)
			this.dbContext = new InitialContext(env);
		return this.dbContext;
	}

	public static String setSQLNoConvert(String sql) {
		if (!(sql.startsWith("--_NO_CONVERT_\r\n"))) {
			sql = "--_NO_CONVERT_\r\n" + sql;
		}
		return sql;
	}

	public void setConnection(Connection conn) {
		this.connection = conn;
	}

	public Connection getConnection() {
		return this.connection;
	}

	public void setIdentifierQuoteChar(char qc) {
		this.QuoteChar = qc;
	}

	public char getIdentifierQuoteChar() {
		return this.QuoteChar;
	}

	public String getDialectSql(String SQL) {
		return null;
	}

	/*public JSQLToolkit getSQLToolkit() {
		if (this.connection == null)
			return null;
		String productName = null;
		try {
			productName = this.connection.getMetaData()
					.getDatabaseProductName();
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSQLToolkit sqlToolkit = (JSQLToolkit) Lookup.getDefault().lookup(
				JSQLToolkit.class, productName);
		return sqlToolkit;
	}*/

	public int CheckDatabase(Object PO) {
		return 0;
	}

	public Object InitConnection(Object ParamObject, Object DataObject,
			Object CustomObject, Object AdditiveObject) throws Exception {
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
			this.sqlLogList = null;
			if (!(this.connection.isClosed()))
				this.connection.close();
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

	/*public Statement createStatement() {
		try {
			return DelegateStatement.getInstance(this,
					this.connection.createStatement());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}*/

	/*public Statement GetStatement(JConnection connection) {
		try {
			return DelegateStatement.getInstance(connection, connection
					.getConnection().createStatement());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}*/

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

	/*public Statement createStatement(int resultSetType, int resultSetConcurrency) {
		try {
			return DelegateStatement.getInstance(this, this.connection
					.createStatement(resultSetType, resultSetConcurrency));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}*/

	public Statement createStatement(int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		return this.connection.createStatement(resultSetType,
				resultSetConcurrency, resultSetHoldability);
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

	public int getHoldability() throws SQLException {
		return this.connection.getHoldability();
	}

	public DatabaseMetaData getMetaData() throws SQLException {
		return this.connection.getMetaData();
	}

	public int getTransactionIsolation() throws SQLException {
		return this.connection.getTransactionIsolation();
	}

	public Map getTypeMap() throws SQLException {
		return this.connection.getTypeMap();
	}

	public SQLWarning getWarnings() throws SQLException {
		return this.connection.getWarnings();
	}

	public boolean isClosed() {
		try {
			return this.connection.isClosed();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean isReadOnly() throws SQLException {
		return this.connection.isReadOnly();
	}

	/*public String nativeSQL(String sql) throws SQLException {
		SQLContext context = new SQLContext();
		context.setValue("ConvertType", "SIMPLE");
		context.setValue("DbType", getDataBaseType());
		return this.connection.nativeSQL(SQLManager.getDefault().convertSQL(
				sql, context));
	}

	public CallableStatement prepareCall(String sql) throws SQLException {
		SQLContext context = new SQLContext();
		context.setValue("ConvertType", "SIMPLE");
		context.setValue("DbType", getDataBaseType());
		sql = SQLManager.getDefault().convertSQL(sql, context);
		return DelegateCallableStatement.getInstance(this, sql,
				this.connection.prepareCall(sql));
	}

	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		SQLContext context = new SQLContext();
		context.setValue("ConvertType", "SIMPLE");
		context.setValue("DbType", getDataBaseType());
		sql = SQLManager.getDefault().convertSQL(sql, context);
		return DelegateCallableStatement.getInstance(this, sql, this.connection
				.prepareCall(sql, resultSetType, resultSetConcurrency));
	}

	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		SQLContext context = new SQLContext();
		context.setValue("ConvertType", "SIMPLE");
		context.setValue("DbType", getDataBaseType());
		sql = SQLManager.getDefault().convertSQL(sql, context);
		return DelegateCallableStatement.getInstance(this, sql, this.connection
				.prepareCall(sql, resultSetType, resultSetConcurrency,
						resultSetHoldability));
	}

	public PreparedStatement prepareStatement(String sql) throws SQLException {
		SQLContext context = new SQLContext();
		context.setValue("ConvertType", "SIMPLE");
		context.setValue("DbType", getDataBaseType());
		sql = SQLManager.getDefault().convertSQL(sql, context);
		logInfo(sql, 4);
		return DelegatePreparedStatement.getInstance(this, sql,
				this.connection.prepareStatement(sql));
	}

	public void logInfo(String sql, int mask, boolean bLine) {
		if (this.sqlLogList != null)
			this.sqlLogList.add(sql);
		String value = ParameterManager.getDefault().getSystemParam(
				"SQL_MONITOR");
		if ((value == null) || (value.trim().length() == 0))
			value = "0";
		int iv = Integer.parseInt(value);
		if ((iv & mask) != 0)
			if (bLine)
				System.out.println(sql);
			else
				System.out.print(sql);
	}
*/
	public void logInfo(String sql, int mask) {
		//logInfo(sql, mask, true);
	}

	/*public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
			throws SQLException {
		SQLContext context = new SQLContext();
		context.setValue("ConvertType", "SIMPLE");
		context.setValue("DbType", getDataBaseType());
		sql = SQLManager.getDefault().convertSQL(sql, context);
		return DelegatePreparedStatement.getInstance(this, sql,
				this.connection.prepareStatement(sql, autoGeneratedKeys));
	}

	public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
			throws SQLException {
		SQLContext context = new SQLContext();
		context.setValue("ConvertType", "SIMPLE");
		context.setValue("DbType", getDataBaseType());
		sql = SQLManager.getDefault().convertSQL(sql, context);
		return DelegatePreparedStatement.getInstance(this, sql,
				this.connection.prepareStatement(sql, columnIndexes));
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		SQLContext context = new SQLContext();
		context.setValue("ConvertType", "SIMPLE");
		context.setValue("DbType", getDataBaseType());
		sql = SQLManager.getDefault().convertSQL(sql, context);
		return DelegatePreparedStatement.getInstance(this, sql, this.connection
				.prepareStatement(sql, resultSetType, resultSetConcurrency));
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		SQLContext context = new SQLContext();
		context.setValue("ConvertType", "SIMPLE");
		context.setValue("DbType", getDataBaseType());
		sql = SQLManager.getDefault().convertSQL(sql, context);
		return DelegatePreparedStatement.getInstance(this, sql, this.connection
				.prepareStatement(sql, resultSetType, resultSetConcurrency,
						resultSetHoldability));
	}

	public PreparedStatement prepareStatement(String sql, String[] columnNames)
			throws SQLException {
		SQLContext context = new SQLContext();
		context.setValue("ConvertType", "SIMPLE");
		context.setValue("DbType", getDataBaseType());
		sql = SQLManager.getDefault().convertSQL(sql, context);
		return DelegatePreparedStatement.getInstance(this, sql,
				this.connection.prepareStatement(sql, columnNames));
	}
*/
	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		this.connection.releaseSavepoint(savepoint);
	}

	public void rollback() {
		try {
			System.out.println("rollback=" + this);
			this.connection.rollback();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void rollback(Savepoint savepoint) throws SQLException {
		this.connection.rollback(savepoint);
	}

	public void setAutoCommit(boolean autoCommit) {
		try {
			this.connection.setAutoCommit(autoCommit);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setCatalog(String catalog) throws SQLException {
		this.connection.setCatalog(catalog);
	}

	public void setHoldability(int holdability) throws SQLException {
		this.connection.setHoldability(holdability);
	}

	public void setReadOnly(boolean readOnly) throws SQLException {
		this.connection.setReadOnly(readOnly);
	}

	public Savepoint setSavepoint() throws SQLException {
		return this.connection.setSavepoint();
	}

	public Savepoint setSavepoint(String name) throws SQLException {
		return this.connection.setSavepoint(name);
	}

	public void setTransactionIsolation(int level) throws SQLException {
		this.connection.setTransactionIsolation(level);
	}

	public void setTypeMap(Map map) throws SQLException {
		this.connection.setTypeMap(map);
	}

	/*public boolean ExecuteSQL(String SQL) {
		boolean Res = false;
		long t1 = System.currentTimeMillis();
		Statement st = GetStatement(this);
		long t2;
		try {
			Res = st.execute(SQL);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			long t2;
			long t2 = System.currentTimeMillis();
			logInfo(SQL + " execute time:" + (t2 - t1), 2);
			BackStatement(st, null);
		}
		return Res;
	}

	public boolean ExecuteUpdate(String SQL) {
		boolean Res = true;
		long t1 = System.currentTimeMillis();
		Statement st = GetStatement(this);
		long t2;
		try {
			st.executeUpdate(SQL);
		} catch (Exception e) {
			Res = false;
			e.printStackTrace();
		} finally {
			long t2;
			long t2 = System.currentTimeMillis();
			logInfo(SQL + " execute time:" + (t2 - t1), 2);
			BackStatement(st, null);
		}
		return Res;
	}*/

	public void changePassword(Object PO) throws Exception {
	}

	public ResultSet executeQuery(Statement st, String sql) throws SQLException {
		long t1 = System.currentTimeMillis();
		try {
			long t2;
			return st.executeQuery(sql);
		} finally {
			long t2 = System.currentTimeMillis();
			logInfo(sql + " execute time:" + (t2 - t1), 1);
		}
	}

	public int executeUpdate(Statement st, String sql) throws SQLException {
		return st.executeUpdate(sql);
	}

	public boolean execute(Statement st, String sql) throws SQLException {
		return st.execute(sql);
	}

	public void addBatch(Statement st, String sql) throws SQLException {
		st.addBatch(sql);
	}

	public int executeUpdate(Statement st, String sql, int autoGeneratedKeys)
			throws SQLException {
		return st.executeUpdate(sql, autoGeneratedKeys);
	}

	public int executeUpdate(Statement st, String sql, int[] columnIndexes)
			throws SQLException {
		return st.executeUpdate(sql, columnIndexes);
	}

	public int executeUpdate(Statement st, String sql, String[] columnNames)
			throws SQLException {
		return st.executeUpdate(sql, columnNames);
	}

	public boolean execute(Statement st, String sql, int autoGeneratedKeys)
			throws SQLException {
		return st.execute(sql, autoGeneratedKeys);
	}

	public boolean execute(Statement st, String sql, int[] columnIndexes)
			throws SQLException {
		return st.execute(sql, columnIndexes);
	}

	public boolean execute(Statement st, String sql, String[] columnNames)
			throws SQLException {
		return st.execute(sql, columnNames);
	}

	/*public static JConnection getInstance(Connection conn) {
		JConnection jconn = new DelegateConnection(conn);
		return jconn;
	}*/

	/*public static JConnection getInstance(Object PO) throws Exception {
		if (defaultConn == null) {
			defaultConn = (JConnection) Lookup.getDefault().lookup(
					JConnection.class);
		}
		return defaultConn.createInstance(PO);
	}

	public static jfoundation.sql.classes.JConnection getInstanceOld(Object PO)
			throws Exception {
		jfoundation.dataobject.classes.JParamObject FPO = ((com.efounder.eai.data.JParamObject) PO)
				.getOLDParamObject();
		jfoundation.sql.classes.JConnection conn = (jfoundation.sql.classes.JConnection) Class
				.forName(
						"jservice.jdal.classes.DALDBManagerObject.oracle.JDALOracleDBObject")
				.newInstance();

		conn.setDbtype("ORACLE");
		conn.setDbName(FPO.GetValueByEnvName("DataBaseName", ""));
		conn.setConnection(getInstance(PO));
		return conn;
	}
*/
	protected abstract JConnection createInstance(Object paramObject)
			throws Exception;

	public void setSqlLogList(List list) {
		this.sqlLogList = list;
	}

	/*protected void setThreshold(com.efounder.eai.data.JParamObject PO) {
		if (PO == null)
			return;
		String vsThreshold = PO.GetValueByParamName("ROW_COUNT_THRESHOLD", "0");
		int viThreshold = 0;
		try {
			viThreshold = Integer.parseInt(vsThreshold);
		} catch (Exception localException) {
		}
		if (viThreshold <= 0)
			return;
		this.threshold = viThreshold;
	}*/
}