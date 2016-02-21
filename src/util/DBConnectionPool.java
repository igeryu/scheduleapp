//DBConnectionPool.java
/**
 *
 * @author Alan Johnson
 */
package util;

//JDBC imports
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//JNDI imports
import javax.naming.InitialContext;
import javax.naming.Context;
import javax.naming.NamingException;

public class DBConnectionPool extends Object {

    private static DBConnectionPool instance;
    private static DataSource ds = null;
    private static final String dbName = "jdbc/PollDatasource";

    //Keep this package private.
    DBConnectionPool() throws SQLException {
        init();
    }

    public static DBConnectionPool getInstance() throws SQLException {
        if (instance == null) {
            instance = new DBConnectionPool();
        }
        return instance;
    }

    public void init() throws SQLException {

        try {
            Context intContext = new InitialContext();
            Context envContext = (Context) intContext.lookup("java:/comp/env");
            ds = (DataSource) envContext.lookup(dbName);
        } catch (NamingException e) {
            System.err.println(
                    "Problem looking up " + dbName + ": " + e);
        }
    }

    public static Connection getPoolConnectionOLD() throws SQLException {
        Context context = null;
        DataSource ds = null;

        try {
            context = new InitialContext();
            ds = (DataSource) context.lookup("java:comp/env/jdbc/Schedule");
        } catch (Exception e) {
            System.out.println("\nDBConnectionPool.getPoolConnection() : Failed.");
        }

        Connection conn = ds.getConnection();
        if (conn == null) {
            throw new SQLException(
                    "Maximum number of connections in pool exceeded.");
        }
        return conn;
    }

    public static Connection getPoolConnection() {
        String connectionURL = "jdbc:derby://localhost:1527/Schedule";
        Connection conn;
        try {
            conn = DriverManager.getConnection(connectionURL, "Alan", "password");
            System.out.println("Connection successful!");
            //conn.close();
            return conn;
        } catch (SQLException ex) {
            System.out.println("Connection failed!");
        }
        
        return null;
    }

}