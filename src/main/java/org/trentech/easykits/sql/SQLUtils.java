package org.trentech.easykits.sql;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public abstract class SQLUtils {

    protected static Connection connection = null;
    private static Map<String, PreparedStatement> statementCache = new HashMap<String, PreparedStatement>();
    private static boolean useStatementCache = true;

    public static boolean connect() throws SQLException, IllegalAccessException, InstantiationException, ClassNotFoundException{
        if (connection != null) {
            return true;
        }
        
        Driver driver = (Driver) Class.forName("org.sqlite.JDBC").newInstance();
        Properties properties = new Properties();
        
        connection = driver.connect("jdbc:sqlite:plugins/EasyKits/data.db", properties);
        
        if (connection == null) {
            throw new NullPointerException("Connecting to database failed!");
        }
        
        return true;
    }

    public static void dispose() {
        statementCache.clear();
        
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
        	e.printStackTrace();
        }
        
        connection = null;
    }

    public static Connection getConnection() {
        if (connection == null) {
            throw new NullPointerException("No connection!");
        }
        
        return connection;
    }

    public static PreparedStatement prepare(String sql) throws SQLException {
        return prepare(sql, false);
    }

    public static PreparedStatement prepare(String sql, boolean returnGeneratedKeys) throws SQLException {
        if (connection == null) {
            throw new SQLException("No connection");
        }
        
        if (useStatementCache && statementCache.containsKey(sql)) {
            return statementCache.get(sql);
        }

        PreparedStatement preparedStatement = returnGeneratedKeys ? connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS) : connection.prepareStatement(sql);
        statementCache.put(sql, preparedStatement);
        
        return preparedStatement;
    }

    public static boolean useStatementCache() {
        return useStatementCache;
    }

    public static void setUseStatementCache(boolean useStatementCache) {
    	SQLUtils.useStatementCache = useStatementCache;
    }
}