package pub.utils;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//import com.db.jfoundation.sql.JConnection;

public class JConvertSql {
	static ResourceBundle res = ResourceBundle
			.getBundle("com.pansoft.pub.util.Language");
	private static final String _SQL_HEAD_SYABSE = "_SYBASE_SQL_";
	private static final String _SQL_HEAD_ORACLE = "_ORACLE_SQL_";
	private static final String _SQL_HEAD_OTHERDB = "_OTHERDB_SQL_";
	private static final String _SQL_HEAD_NO_CONVERT = "--_NO_CONVERT_\r\n";
	private static final String _SQL_HEAD_CONVERTED_SYABSE_TO_ORACLE = "--_CONVERTED_SYBASE_TO_ORACLE_\r\n";
	private static String[] beginKeyList = { "SELECT", "UPDATE", "INSERT",
			"DELETE", "DROP" };
	private static final String _SQL_PLACEHOLDER = "__SQL__";
	private static char[] spaceList = { ' ' };
	private static char[] spaceCharList = { ' ', ',', '>', '<', '=', '!', '+',
			'-', '*', '/' };

	private static boolean isOracle = false;

	public JConvertSql(boolean isOra) {
		isOracle = isOra;
	}

	public static boolean isOracle() {
		return isOracle;
	}

	/*public static boolean isOracle(Statement st) {
		if (st == null)
			return isOracle;
		try {
			return isOracle(st.getConnection());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return isOracle;
	}

	public static boolean isOracle(PreparedStatement st) {
		if (st == null)
			return isOracle;
		try {
			return isOracle(st.getConnection());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return isOracle;
	}

	public static boolean isOracle(JConnection con) {
		return con.isOracle();
	}

	public static boolean isOracle(Connection con) {
		if (con == null) {
			return isOracle;
		}

		if (con instanceof JConnection) {
			return ((JConnection) con).isOracle();
		}

		String classname = null;
		try {
			classname = con.getMetaData().getDatabaseProductName();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return ((classname != null) && (classname.toLowerCase().indexOf(
				"oracle") >= 0));
	}

	public static String convertSql(JConnection conn, String sql) {
		if (!(isOracle(conn))) {
			return StringFunction.replaceString(sql, "||", "+");
		}

		return convertSqlToOracle(sql, conn.getDbName());
	}

	public static String[] convertSql(JConnection conn, String sql,
			boolean isCreateTable) {
		String[] info = new String[2];
		if (!(isOracle(conn))) {
			info[0] = StringFunction.replaceString(sql, "||", "+");
		} else {
			info[0] = convertSqlToOracle(sql, conn.getDbName());
		}
		if (isCreateTable) {
			if (sql.indexOf("(") > 0) {
				info[1] = info[0].substring(
						info[0].toLowerCase().indexOf("table") + 6,
						info[0].indexOf("(")).trim();
			} else {
				info[1] = info[0].substring(
						info[0].toLowerCase().indexOf("table") + 6).trim();
			}
		}
		return info;
	}

	public static String convertSql(Statement st, String sql) {
		if (!(isOracle(st))) {
			return StringFunction.replaceString(sql, "||", "+");
		}

		JConnection conn = null;
		String dbName = "";
		try {
			conn = (JConnection) st.getConnection();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			if (conn == null)
				break label54;
		}
		dbName = conn.getDbName();

		label54: return convertSqlToOracle(sql, dbName);
	}*/

	protected static String convertSqlToOracle(String sql) {
		return convertSqlToOracle(sql, "");
	}

	protected static String convertSqlToOracle(String sql, String dbName) {
		if (!(isNeedConvert(sql))) {
			return sql;
		}

		String vsSql = sql.trim().toUpperCase();
		if (vsSql.startsWith("UPDATE")) {
			sql = processUpdate(sql);
		} else if (vsSql.startsWith("DELETE")) {
			sql = processDelete(sql);
		} else if (vsSql.startsWith("INSERT")) {
			sql = processInsert(sql);
		} else if (vsSql.startsWith("CREATE")) {
			sql = convertType(sql);
			sql = processCreate(sql);
			sql = StringFunction.replaceString(sql, "#", "");
		} else if (vsSql.startsWith("ALTER")) {
			sql = convertType(sql);
		} else if (vsSql.startsWith("SELECT")) {
			sql = processSelectInto(sql);
		} else if (vsSql.startsWith("DROP")) {
			sql = convertDropSql(vsSql, sql);
		}

		sql = convertLRRelation(sql);

		sql = processCaseWhen(sql);

		sql = processConvertFUN(sql);

		sql = sql.replaceAll("''''", "@#@#");
		sql = sql.replaceAll("'''", "#@#");

		sql = sql.replaceAll("''s", "#@@@#");

		sql = sql.replaceAll("''", "' '");
		sql = sql.replaceAll("@#@#", "''''");
		sql = sql.replaceAll("#@#", "'''");

		sql = sql.replaceAll("#@@@#", "''s");

		sql = Pattern.compile("char\\(127\\)", 2).matcher(sql)
				.replaceAll("chr(127)");
		sql = Pattern.compile("char\\(39\\)", 2).matcher(sql)
				.replaceAll("chr(39)");
		sql = Pattern.compile("datediff\\(day,", 2).matcher(sql)
				.replaceAll("datediff('day',");
		sql = Pattern.compile("datediff\\(month,", 2).matcher(sql)
				.replaceAll("datediff('month',");
		sql = Pattern.compile("datediff\\(year,", 2).matcher(sql)
				.replaceAll("datediff('year',");
		sql = Pattern.compile("substring\\(", 2).matcher(sql)
				.replaceAll("substrb(");
		sql = Pattern.compile("isnull\\(", 2).matcher(sql).replaceAll("nvl(");
		sql = Pattern.compile("datalength\\(", 2).matcher(sql)
				.replaceAll("lengthb(");

		if (!("".equals(dbName))) {
			if (sql.indexOf("dbo.") > -1) {
				sql = StringFunction.replaceString(sql, "dbo.", dbName
						+ "_9999.");
			}
			if (sql.indexOf("DBO.") > -1) {
				sql = StringFunction.replaceString(sql, "DBO.", dbName
						+ "_9999.");
			}

		}

		if ((vsSql.indexOf("SELECT") > -1) && (vsSql.indexOf("DATEDIFF") > -1)
				&& (vsSql.indexOf("FROM") == -1)) {
			sql = sql + " from dual ";
		}

		//Debug.PrintlnMessageToSystem(sql); wutp

		sql = setHeadTag(sql, "--_CONVERTED_SYBASE_TO_ORACLE_\r\n");
		return sql;
	}

	private static String convertDropSql(String vsSql, String sql) {
		if ((vsSql.indexOf("TABLE") == -1) || (vsSql.indexOf("#") == -1)) {
			return sql;
		}
		String sqlCopy = vsSql;
		String drop = sqlCopy.substring(0, sqlCopy.indexOf(" "));
		sqlCopy = sqlCopy.substring(sqlCopy.indexOf(" ")).trim();
		String table = sqlCopy.substring(0, sqlCopy.indexOf(" "));
		if (!("TABLE".equals(table))) {
			return sql;
		}
		sqlCopy = sqlCopy.substring(sqlCopy.indexOf(" ")).trim();
		String tableName = sqlCopy;
		if (tableName.startsWith("#")) {
			tableName = tableName.substring(1);
		}
		String str = drop + " " + table + " " + tableName;
		return str;
	}

	private static boolean checkFromTable(String sFrom, String sTable) {
		int ib = sFrom.indexOf(sTable);
		if (ib >= 0) {
			String sRem = sFrom.substring(ib + sTable.length(), sFrom.length());
			if ((sRem != null) && (!(sRem.trim().equals("")))) {
				if ((sRem.startsWith(" ")) || (sRem.startsWith(",")))
					return true;
			} else {
				return true;
			}
		}
		return false;
	}

	private static String processUpdate(String sql)
  {
    String astable = "";
    String setCol = ""; String seleCol = ""; String setclause = "";

    String upperSql = sql.toUpperCase();

    int index = upperSql.lastIndexOf(" FROM ");

    if (index < 0) {
      return sql;
    }
    int whereIndex = upperSql.lastIndexOf(" WHERE ");
    if (whereIndex < 0) {
      return sql;
    }

    String temp = upperSql.substring(index + 4, whereIndex);
    int nValue = temp.indexOf("'");
    if (nValue >= 0) {
      return sql;
    }
    nValue = temp.indexOf("\"");
    if (nValue >= 0) {
      return sql;
    }

    if (upperSql.indexOf("EXISTS") > 0) {
      return sql;
    }

    setclause = upperSql.substring(upperSql.indexOf(" SET ") + 5);
    setclause = setclause.substring(setclause.indexOf("=") + 1);
    setclause = setclause.replaceAll(" ", "");
    if ((setclause.startsWith("(SELECT")) || 
      (setclause.startsWith("ISNULL((SELECT")) || 
      (setclause.startsWith("ISNULL(SELECT"))) {
      return sql;
    }

    String tabn = sql.substring(upperSql.indexOf("UPDATE") + 6, upperSql.indexOf(" SET "))
      .trim();
    String set = sql.substring(upperSql.indexOf(" SET ") + 5, index).trim();
    String from = sql.substring(upperSql.indexOf(" FROM ") + 6, upperSql.indexOf(" WHERE "))
      .trim();
    boolean ib = checkFromTable(from, tabn);

    if ((ib) && (from.indexOf(",") > 0))
    {
      String vsTable = from.substring(from.indexOf(tabn) + tabn.length()).trim();
      if (vsTable.indexOf(",") >= 0)
      {
        astable = vsTable.substring(0, vsTable.indexOf(","));
        if (!(astable.trim().equals(""))) {
          astable = astable.trim();
        }
        vsTable = vsTable.substring(vsTable.indexOf(",") + 1);
      } else {
        astable = vsTable.trim();
        vsTable = "";
      }
      from = from.substring(0, from.indexOf(tabn)) + vsTable;
    }
    String where = sql.substring(upperSql.indexOf(" WHERE ") + 7).trim();
    index = set.indexOf("=");
    setCol = setCol + set.substring(0, index);
    set = set.substring(index + 1);
    while (set.trim().length() > 0) {
        index = set.indexOf("=");
        if (index >= 0) {
          temp = set.substring(0, index);
          set = set.substring(index + 1);
          label667: for (int i = temp.length(); i > 0; --i)
            if (temp.substring(i - 1, i).equals(",")) {
              setCol = setCol + "," + temp.substring(i).trim();
              seleCol = seleCol + "," + temp.substring(0, i - 1).trim();
             // break label667: wutp
            }
      }
      else
      {
        seleCol = seleCol + "," + set;
        set = "";
      }
    }
    if (setCol.startsWith(",")) {
      setCol = setCol.substring(1);
    }

    if (!(setCol.trim().startsWith("("))) {
      setCol = "(" + setCol + ")";
    }
    if (seleCol.startsWith(",")) {
      seleCol = seleCol.substring(1);
    }
    temp = from;
    from = "";
    String alias = "";

    while (temp.trim().length() > 0) {
      index = temp.indexOf(",");
      String temp1;
      if (index >= 0) {
        temp1 = temp.substring(0, index);
        temp = temp.substring(index + 1);
      }
      else {
        temp1 = temp;
        temp = "";
      }
      if ((temp1.trim().length() > tabn.length()) && 
        (tabn.equals(temp1.trim().substring(0, tabn.length()))) && 
        (temp1.substring(tabn.length(), tabn.length() + 1).equals(" "))) {
        alias = temp1.substring(tabn.length()).trim();
      }
      else {
        from = from + "," + temp1;
      }
    }
    if ((from.trim().length() > 0) && (from.startsWith(","))) {
      from = from.substring(1);
    }

    String where1 = where;
    if (upperSql.indexOf(" GROUP ") > 0) {
      where1 = sql.substring(upperSql.indexOf(" WHERE ") + 7, upperSql.indexOf(" GROUP ")).trim();
    }
    sql = "update " + tabn + " " + alias + " set " + setCol + 
      "  = ( select " + seleCol + " from " + from + " where " + 
      where + " ) where exists ( select 1 from " + from + 
      " where " + where1 + " ) ";

    if (!(astable.equals(""))) {
      sql = StringFunction.replaceString(sql, astable + ".", tabn + ".");
    }
    return sql;
  }

	private static String processDelete(String sql) {
		int index = sql.toUpperCase().indexOf("DELETE");
		if (index >= 0) {
			String temp = sql.substring(index + 6);
			if (!(temp.trim().toUpperCase().startsWith("FROM"))) {
				sql = "delete from " + temp;
			}
		}
		return sql;
	}

	private static String processInsert(String sql) {
		int index = sql.toUpperCase().indexOf("INSERT");
		if (index >= 0) {
			String temp = sql.substring(index + 6);
			if (!(temp.trim().toUpperCase().startsWith("INTO"))) {
				sql = "insert into " + temp;
			}
		}
		return sql;
	}

	private static String processCaseWhen(String sql) {
		return sql;
	}

	private static String processLeftRight_old(String sql) {
		if (sql.toUpperCase().indexOf("WHERE") <= 0) {
			return sql;
		}

		ArrayList tableList = new ArrayList();

		int index = sql.indexOf("*=");
		while (index > 0) {
			int pos = sql.indexOf(".", index);
			if (pos > 0) {
				String table = sql.substring(index + 2, pos).trim();

				if (tableList.indexOf(table) < 0) {
					tableList.add(table);
				}
			}
			index = sql.indexOf("*=", index + 2);
		}

		index = sql.indexOf("=*");
		while (index > 0) {
			String strSql = sql.substring(0, index);
			int pos = strSql.lastIndexOf(".");
			if (pos > 0) {
				strSql = strSql.substring(0, pos);
				int space = strSql.lastIndexOf(" ");
				if (space > 0) {
					String table = sql.substring(space + 1, pos).trim();

					if (tableList.indexOf(table) < 0) {
						tableList.add(table);
					}
				}
			}
			index = sql.indexOf("=*", index + 2);
		}

		sql = StringFunction.replaceString(sql, "=*", "=");
		sql = StringFunction.replaceString(sql, "*=", "=");

		int size = tableList.size();
		for (int i = 0; i < size; ++i) {
			String table = tableList.get(i).toString();
			index = sql.toUpperCase().indexOf(" WHERE ");
			int indexend = sql.toUpperCase().indexOf(" GROUP ");
			int indexorder = sql.toUpperCase().indexOf(" ORDER ");
			if ((indexorder < indexend) && (indexorder > 0))
				indexend = indexorder;
			if (indexend < 0)
				indexend = 999999999;

			index = sql.indexOf(table + ".", index);
			while (index > 0) {
				int space = getOperPos(sql, index);
				if (space > 0) {
					sql = sql.substring(0, space) + " (+)"
							+ sql.substring(space);
				} else {
					sql = sql + "(+)";
					break;
				}

				index = sql.indexOf(table + ".", index + table.length());
				if (index > indexend) {
					break;
				}
			}
		}

		return sql;
	}

	private static String processLeftRight(String sql) {
		if (sql.toUpperCase().indexOf("WHERE") <= 0) {
			return sql;
		}

		if ((sql.indexOf("*=") < 0) && (sql.indexOf("=*") < 0)) {
			return sql;
		}

		ArrayList tableListl = new ArrayList();
		ArrayList tableListr = new ArrayList();

		int index = sql.indexOf("*=");

		while (index > 0) {
			String tablel = "";
			String strSql = sql.substring(0, index);
			int pos = strSql.lastIndexOf(".");
			if (pos > 0) {
				strSql = strSql.substring(0, pos);
				int space = strSql.lastIndexOf(" ");
				if (space > 0) {
					tablel = sql.substring(space + 1, pos).trim();
				}
			}

			String tabler = "";
			pos = sql.indexOf(".", index);
			if (pos > 0) {
				tabler = sql.substring(index + 2, pos).trim();
			}

			String table = tablel + "*=" + tabler;
			if (tableListl.indexOf(table) < 0) {
				tableListl.add(table);
			}

			index = sql.indexOf("*=", index + 2);
		}

		index = sql.indexOf("=*");

		while (index > 0) {
			String tablel = "";
			String strSql = sql.substring(0, index);
			int pos = strSql.lastIndexOf(".");
			if (pos > 0) {
				strSql = strSql.substring(0, pos);
				int space = strSql.lastIndexOf(" ");
				if (space > 0) {
					tablel = sql.substring(space + 1, pos).trim();
				}
			}

			String tabler = "";
			pos = sql.indexOf(".", index);
			if (pos > 0) {
				tabler = sql.substring(index + 2, pos).trim();
			}

			String table = tablel + "=*" + tabler;
			if (tableListr.indexOf(table) < 0) {
				tableListr.add(table);
			}

			index = sql.indexOf("=*", index + 2);
		}

		sql = StringFunction.replaceString(sql, "=*", "=");
		sql = StringFunction.replaceString(sql, "*=", "=");

		String swhereand = "";
		String stabletotal = "";
		String sWhere = "";

		for (int i = 0; i < tableListl.size(); ++i) {
			stabletotal = stabletotal + ";" + tableListl.get(i).toString();
		}
		for (int i = 0; i < tableListr.size(); ++i) {
			stabletotal = stabletotal + ";" + tableListr.get(i).toString();
		}

		index = sql.toUpperCase().indexOf(" WHERE ");
		int indexend = sql.toUpperCase().indexOf(" GROUP ");
		int indexorder = sql.toUpperCase().indexOf(" ORDER ");
		if (indexend < 0)
			indexend = sql.length();
		if ((indexorder < indexend) && (indexorder > 0))
			indexend = indexorder;

		while ((index > 0) && (index < indexend)) {
			int indexand = sql.toUpperCase().indexOf(" AND ", index + 5);
			if (indexand < 0)
				indexand = indexend;
			swhereand = sql.substring(index, indexand).trim();

			int space = getOperPosM(swhereand, 0);
			String tablel = "";
			String tabler = "";
			if (space > 0) {
				int indexl = swhereand.indexOf(".");
				int indexr = swhereand.lastIndexOf(".");
				if ((indexl < space) && (indexl > 0)) {
					int spacel = getOperPosL(swhereand, 0);
					if ((spacel < 0) || (spacel > space))
						spacel = 0;
					tablel = swhereand.substring(spacel + 1, indexl).trim();
				}
				if ((indexr > space) && (indexr > 0)) {
					int spacer = getOperPosL(swhereand, space);
					if (spacer < space)
						spacer = space;
					tabler = swhereand.substring(spacer + 1, indexr).trim();
				}

			}

			if ((tablel.length() > 0) || (tabler.length() > 0)) {
				String table;
				if (tabler.length() == 0) {
					table = "*=" + tablel;
				} else {
					table = tablel + "*=" + tabler;
				}

				if (stabletotal.indexOf(table) > 0) {
					table = table.substring(table.indexOf("*=") + 2);
					int pos = swhereand.indexOf(table + ".");
					space = getOperPos(swhereand, pos);
					if (space > 0) {
						swhereand = swhereand.substring(0, space) + " (+)"
								+ swhereand.substring(space);
					} else {
						swhereand = swhereand + "(+)";
					}

				}

				if (tablel.length() == 0) {
					table = tabler + "=*";
				} else {
					table = tablel + "=*" + tabler;
				}

				if (stabletotal.indexOf(table) > 0) {
					table = table.substring(0, table.indexOf("=*"));
					int pos = swhereand.indexOf(table + ".");
					space = getOperPos(swhereand, pos);
					if (space > 0) {
						swhereand = swhereand.substring(0, space) + " (+)"
								+ swhereand.substring(space);
					} else {
						swhereand = swhereand + "(+)";
					}
				}

			}

			sWhere = sWhere + " " + swhereand;
			index = indexand;
		}

		index = sql.toUpperCase().indexOf(" WHERE ");
		sql = sql.substring(0, index) + sWhere + sql.substring(indexend);

		return sql;
	}

	private static int getOperPos(String strSql, int begin) {
		String[] operList = { " ", ",", ")", "=", ">", "<" };
		int pos = -1;

		String strValue = "";
		int length = strSql.length();
		for (int i = begin; i < strSql.length(); ++i) {
			strValue = strSql.substring(i, i + 1);
			for (int j = 0; j < operList.length; ++j) {
				if (strValue.equals(operList[j])) {
					return i;
				}
			}
		}
		return pos;
	}

	private static int getOperPosM(String strSql, int begin) {
		String[] operList = { "=", ">", "<" };
		int pos = -1;

		String strValue = "";
		int length = strSql.length();
		for (int i = begin; i < strSql.length(); ++i) {
			strValue = strSql.substring(i, i + 1);
			for (int j = 0; j < operList.length; ++j) {
				if (strValue.equals(operList[j])) {
					return i;
				}
			}
		}
		return pos;
	}

	private static int getOperPosL(String strSql, int begin) {
		String[] operList = { "(", "=", ">", "<", " " };
		int pos = -1;

		String strValue = "";
		int length = strSql.length();
		for (int i = begin; i < strSql.length(); ++i) {
			strValue = strSql.substring(i, i + 1);
			for (int j = 0; j < operList.length; ++j) {
				if (strValue.equals(operList[j])) {
					return i;
				}
			}
		}
		return pos;
	}

	private static String processCreate(String sql) {
		String sqlUP = sql.toUpperCase();

		int index = sqlUP.indexOf("#");
		if ((index > 0) && (sqlUP.indexOf("TABLE") > 0)) {
			sql = " create table " + sql.substring(index + 1);
		}
		return sql;
	}

	private static String processConvertFUN(String sql) {
		String upperSql = sql.toUpperCase();

		int index = upperSql.indexOf("CONVERT");
		while (index > 0) {
			int leftBraceIndex = upperSql.indexOf("(", index);
			if (leftBraceIndex < 0) {
				index = upperSql.indexOf("CONVERT", index + 1);
			} else {
				String temp = upperSql.substring(leftBraceIndex + 1);
				int rightBraceIndex = ala(temp);

				String body = temp.substring(0, rightBraceIndex);

				String newBody = tranBody(body);

				String newFunction = " cast" + newBody;

				String before = sql.substring(0, index);
				String after = temp.substring(rightBraceIndex + 1);
				sql = before + newFunction + after;

				upperSql = sql.toUpperCase();
				index = upperSql.indexOf("CONVERT");
			}
		}
		return sql;
	}

	private static String tranBody(String body) {
		String afterBody = "";

		int index = -1;
		char[] array = body.toCharArray();
		int count = 0;
		for (int i = 0; i < array.length; ++i) {
			if ('(' == array[i])
				++count;
			else if (')' == array[i]) {
				--count;
			}
			if ((',' == array[i]) && (count == 0)) {
				index = i;
				break;
			}
		}

		String p1 = body.substring(0, index);

		String p2 = body.substring(index + 1);

		afterBody = "(" + p2 + " as " + p1 + ")";
		return afterBody;
	}

	private static int ala(String sqlString) {
		int index = -1;
		char[] array = sqlString.toCharArray();

		int count = 1;
		for (int i = 0; i < array.length; ++i) {
			if ('(' == array[i])
				++count;
			else if (')' == array[i]) {
				--count;
			}
			if (count == 0) {
				index = i;
				break;
			}
		}

		return index;
	}

	private static String processSelectInto(String sql) {
		String upperSql = sql.toUpperCase();
		if (upperSql.indexOf(" INTO ") > 0) {
			sql = " create table "
					+ sql.substring(upperSql.indexOf(" INTO ") + 6,
							upperSql.indexOf("FROM")) + " as "
					+ sql.substring(0, upperSql.indexOf(" INTO ")) + " "
					+ sql.substring(upperSql.indexOf("FROM"));
		}
		return sql;
	}

	public static String setSQLHeadTagOfNoConvert(String sql) {
		return setHeadTag(sql, "--_NO_CONVERT_\r\n");
	}

	public static String takeOffHeadTag(String sql) {
		int index = -1;
		String[] head = { "--_NO_CONVERT_\r\n",
				"--_CONVERTED_SYBASE_TO_ORACLE_\r\n" };
		for (int i = 0; i < head.length; ++i) {
			if (sql.startsWith(head[i])) {
				index = head[i].length();
				if (index > -1) {
					sql = sql.substring(index);
					break;
				}
			}
		}
		return sql;
	}

	private static String setHeadTag(String sql, String headTag) {
		if (!(sql.startsWith(headTag))) {
			sql = headTag + sql;
		}
		return sql;
	}

	private static boolean isNeedConvert(String sql) {
		boolean tag = true;

		String[] head = { "--_NO_CONVERT_\r\n",
				"--_CONVERTED_SYBASE_TO_ORACLE_\r\n" };
		for (int i = 0; i < head.length; ++i) {
			if (sql.startsWith(head[i])) {
				tag = false;
				break;
			}
		}
		return tag;
	}

	private static String convertLRRelation(String SQL) {
		if ((SQL.indexOf("*=") < 0) && (SQL.indexOf("=*") < 0)) {
			return SQL;
		}

		if ((SQL.toUpperCase().trim().startsWith("INSERT"))
				&& (SQL.toUpperCase().indexOf("VALUES") > 0)) {
			return SQL;
		}

		SQL = "(" + SQL + ")";

		Stack sqlStack = new Stack();

		Stack selectStack = new Stack();

		Stack braceStack = new Stack();
		int count = 0;

		char[] array = SQL.toCharArray();

		for (int i = 0; i < array.length; ++i) {
			if (isSQLStart(array, i)) {
				selectStack.push(new Integer(i));
			}

			if (array[i] == '(') {
				braceStack.push(new Integer(i));
			}

			if (array[i] == ')') {
				int temp = ((Integer) braceStack.pop()).intValue();

				if (temp != ((Integer) selectStack.peek()).intValue())
					continue;
				selectStack.pop();

				SQL = convert(sqlStack, SQL, temp, i, count);
				++count;
			}

		}

		int i = 1;

		String parentSQL = SQL;
		while (!(sqlStack.empty())) {
			String subSQL = (String) sqlStack.pop();
			int subSQLLen = subSQL.length();

			subSQL = processLeftRight(subSQL);

			parentSQL = insertSubSQL2ParentSQL(subSQL, parentSQL, count - i,
					subSQLLen);
			++i;
		}

		parentSQL = parentSQL.substring(1, parentSQL.length() - 1);
		return parentSQL;
	}

	private static String insertSubSQL2ParentSQL(String subSQL,
			String parentSQL, int num, int subSQLLen) {
		int index = parentSQL.indexOf("__SQL__" + num);

		parentSQL = parentSQL.substring(0, index) + subSQL
				+ parentSQL.substring(index + subSQLLen);
		return parentSQL;
	}

	private static String convert(Stack sqlStack, String SQL, int start,
			int end, int count) {
		String subSQL = SQL.substring(start + 1, end);
		sqlStack.push(subSQL);

		int len = subSQL.length();
		int spaceCount = len - ("__SQL__".length() + 1);
		SQL = SQL.substring(0, start + 1) + "__SQL__" + count
				+ space("_", spaceCount) + SQL.substring(end);
		return SQL;
	}

	private static String space(String seed, int n) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < n; ++i) {
			buffer.append(seed);
		}
		return buffer.toString();
	}

	private static boolean isSQLStart(char[] array, int postion) {
		boolean tag = false;
		if (array[postion] == '(') {
			String token = getNextToken(array, postion + 1);

			for (int i = 0; i < beginKeyList.length; ++i) {
				if (token.toUpperCase().equals(beginKeyList[i])) {
					tag = true;
					break;
				}
			}
		}
		return tag;
	}

	private static int getNextTokenStartPosition(char[] array, int begin) {
		for (int i = begin; i < array.length; ++i) {
			int j = 0;
			for (; j < spaceList.length; ++j) {
				if (array[i] == spaceList[j]) {
					++begin;
					break;
				}
			}

			if (j == spaceList.length) {
				break;
			}
		}
		return begin;
	}

	private static String getNextToken(char[] array, int postion) {
		StringBuffer token = new StringBuffer();

		postion = getNextTokenStartPosition(array, postion);
		for (int i = postion; i < array.length; ++i) {
			for (int k = 0; k < spaceCharList.length; ++k) {
				if (array[i] == spaceCharList[k]) {
					return token.toString().trim();
				}
			}

			token.append(array[i]);
		}
		return token.toString().trim();
	}

	public static void main1(String[] args) {
		String strSql = "select * into A from B where F_A = ' INTO '";
		String vlaue = processSelectInto(strSql);
		System.out.println(vlaue);
	}

	public static void main(String[] args) {
		String sql = "create table #test(a char(1),b char(2))";

		System.out.print(convertSqlToOracle(sql));
	}

	private static String convertType(String sql) {
		sql = Pattern.compile("\\bU001\\b", 2).matcher(sql)
				.replaceAll("number(38,16)");

		sql = Pattern.compile("\\bVARCHAR\\b", 2).matcher(sql)
				.replaceAll("VARCHAR2");
		sql = Pattern.compile("\\bVARCHAR\\(", 2).matcher(sql)
				.replaceAll("VARCHAR2(");

		sql = Pattern.compile("\\bCHAR\\b", 2).matcher(sql)
				.replaceAll("VARCHAR2");
		sql = Pattern.compile("\\bCHAR\\(", 2).matcher(sql)
				.replaceAll("VARCHAR2(");

		sql = Pattern.compile("\\bTINYINT\\(", 2).matcher(sql)
				.replaceAll("INTEGER(");
		sql = Pattern.compile("\\bTINYINT\\b", 2).matcher(sql)
				.replaceAll("INTEGER");

		sql = Pattern.compile("\\bSMALLINT\\(", 2).matcher(sql)
				.replaceAll("INTEGER(");
		sql = Pattern.compile("\\bSMALLINT\\b", 2).matcher(sql)
				.replaceAll("INTEGER");

		sql = Pattern.compile("\\bINT\\(", 2).matcher(sql)
				.replaceAll("INTEGER(");
		sql = Pattern.compile("\\bINT\\b", 2).matcher(sql)
				.replaceAll("INTEGER");

		sql = Pattern.compile("\\bNONCLUSTERED\\b", 2).matcher(sql)
				.replaceAll(" ");
		sql = Pattern.compile("\\bCLUSTERED\\b", 2).matcher(sql)
				.replaceAll(" ");

		sql = Pattern.compile("\\bIMAGE\\b", 2).matcher(sql).replaceAll("BLOB");

		sql = Pattern.compile("\\bTEXT\\b", 2).matcher(sql).replaceAll("BLOB");

		sql = Pattern.compile("\\bDATETIME\\b", 2).matcher(sql)
				.replaceAll("DATE");

		sql = Pattern.compile("\\bSMALLDATETIME\\b", 2).matcher(sql)
				.replaceAll("DATE");
		return sql;
	}
}