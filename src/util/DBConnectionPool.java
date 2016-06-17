//DBConnectionPool.java

/**
 * Changelog:
 * 
 * 2016-03-01 : Added two final Strings to represent the connection URL from a localhost and remote context
 * 2016-03-01 : Changed the catch clause of getPoolConnection() to display the exception's message
 * 
 * 2016-03-05 : Changed the name of the database in CONNECTION_URL and CONNECTION_URL_REMOTE
 * 2016-03-05 : Changed the login username used in getPoolConnection()
 * 
 * 2016-03-24 : Replaced debug System.out calls with Logger calls
 * 2016-03-24 : Formatted to match Google Java Style
 */

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
import java.util.logging.Logger;
import java.util.logging.Level;

//JNDI imports
import javax.naming.InitialContext;
import javax.naming.Context;
import javax.naming.NamingException;

public class DBConnectionPool extends Object {

  private static final String CONNECTION_URL =
      "jdbc:derby://localhost:1527/Lobo_AMU";
  private static final String CONNECTION_URL_REMOTE =
      "jdbc:derby://192.168.0.198:1527/Lobo_AMU";

  private static DBConnectionPool instance;
  private static DataSource ds = null;
  private static final String dbName = "jdbc/PollDatasource";
  private static final Logger logger = Logger.getLogger(DBConnectionPool.class.getName());

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
      logger.log(Level.WARNING, "Problem looking up " + dbName + ": " + e);
    }
  }

  public static Connection getPoolConnectionOLD() throws SQLException {
    Context context;

    try {
      context = new InitialContext();
      ds = (DataSource) context.lookup("java:comp/env/jdbc/Schedule");
    } catch (Exception e) {
      logger.log(Level.WARNING, "DBConnectionPool.getPoolConnection() : Failed.");
    }

    Connection conn = ds.getConnection();
    if (conn == null) {
      throw new SQLException(
          "Maximum number of connections in pool exceeded.");
    }
    return conn;
  }

  public static Connection getPoolConnection() {
    String connectionURL = CONNECTION_URL;
    Connection conn;
    try {
      conn = DriverManager.getConnection(connectionURL, "Owner", "password");
      logger.fine("Connection successful!");
      return conn;
    } catch (SQLException ex) {
      String errorMessage = ex.getMessage();
      logger.log(Level.WARNING, "A database error occurred. " + errorMessage);

//            throw ex;
    }

    return null;
  }

}
