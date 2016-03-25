//ObjectIdDAO.java
/**
 * Changelog:
 * 2016-03-23 : Grouped imports by root package
 * 2016-03-23 : Formatted to match Google Java Style (up to 4.5.1)
 * 2016-03-23 : Renamed getNextObjectID() to getNextObjectId()
 */
package domain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.logging.Level;

import util.DBConnectionPool;

/**
 *
 * @author Alan Johnson
 */
class ObjectIdDAO {
  private static final Logger logger = Logger.getLogger(ObjectIdDAO.class.getName());
  public static final String PERSON = "Person";
  public static final String SHIFT_DATE = "Shift_Date";

  public ObjectIdDAO() { }

  public int getNextObjectId(String objectClassName) {
    Connection conn = null;
    PreparedStatement queryStmt = null;
    PreparedStatement incrStmt = null;
    ResultSet rset = null;
    int id = -1;
    
    try {
      conn = DBConnectionPool.getPoolConnection();
      queryStmt = conn.prepareStatement(NEXT_ID_QUERY);
      queryStmt.setString(1, objectClassName);
      rset = queryStmt.executeQuery();
      
      if (rset.next()) {
        id = rset.getInt("IDNumber");
        incrStmt = conn.prepareStatement(UPDATE_ID_CMD);
        incrStmt.setInt(1, id + 1);
        incrStmt.setString(2, objectClassName);
        incrStmt.executeUpdate();
      } else {
        throw new RuntimeException(
                "No ObjectID entry for class type: " + objectClassName);
      }
    } catch (SQLException se) {
      throw new RuntimeException("A database error occurred. "
              + se.getMessage());
    } //  TODO:  Get more specific exception catches (if applicable)
    catch (Exception e) {
      throw new RuntimeException("Exception: " + e.getMessage());
    } // Clean up JDBC resources
    finally {
      if (rset != null) {
        try {
          rset.close();
        } catch (SQLException se) {
          logger.log(Level.WARNING, se.getMessage());
        }
      }
      if (queryStmt != null) {
        try {
          queryStmt.close();
        } catch (SQLException se) {
          logger.log(Level.WARNING, se.getMessage());
        }
      }
      if (incrStmt != null) {
        try {
          incrStmt.close();
        } catch (SQLException se) {
          logger.log(Level.WARNING, se.getMessage());
        }
      }
      if (conn != null) {
        try {
          conn.close();
        } catch (SQLException se) {
          logger.log(Level.WARNING, se.getMessage());
        }
      }
    }
    return id;
  }
  
  private static final String NEXT_ID_QUERY
          = "SELECT IDNumber FROM ObjectIDs WHERE className = ?";
  private static final String UPDATE_ID_CMD
          = "UPDATE ObjectIDs SET IDNumber = ? WHERE className = ?";
}
