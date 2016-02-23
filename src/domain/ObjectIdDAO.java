//ObjectIdDAO.java 

//  Adapted from the Module 4 Example

/**
 *
 * @author Alan Johnson
 */
package domain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import util.DBConnectionPool;

class ObjectIdDAO {
    
   //public static final String COURSE="Course";
   public static final String PERSON="Person";
   public static final String SHIFT_DATE="Shift_Date";
   private DBConnectionPool connPool;
   
   public ObjectIdDAO() {}
   
   public int getNextObjectID(String objectClassName) {
      Connection conn = null;
      PreparedStatement queryStmt=null;
      PreparedStatement incrStmt=null;
      ResultSet rset = null;
      int id=-1;
      try {
         conn=connPool.getPoolConnection();
         queryStmt=conn.prepareStatement(NEXT_ID_QUERY);
         queryStmt.setString(1,objectClassName);
         rset=queryStmt.executeQuery();
         if (rset.next()) {
            id=rset.getInt("IDNumber");
            incrStmt=conn.prepareStatement(UPDATE_ID_CMD);
            incrStmt.setInt(1,id+1);
            incrStmt.setString(2,objectClassName);
            incrStmt.executeUpdate();
         } 
         else throw new RuntimeException(
            "No ObjectID entry for class type: "+ objectClassName);
      } 
      catch (SQLException se) {
         throw new RuntimeException("A database error occurred. " 
            + se.getMessage());}
      catch (Exception e) {
         throw new RuntimeException("Exception: "+e.getMessage());}
      // Clean up JDBC resources
      finally {
        if (rset!=null) {
           try {rset.close();} catch (SQLException se) {}}
        if (queryStmt!=null) {
           try {queryStmt.close();} catch (SQLException se) {}}
        if (incrStmt!=null) {
           try {incrStmt.close();} catch (SQLException se) {}}
        if (conn!=null) {try {conn.close();} catch (SQLException se) {}}
      }
      return id;
   }
   private static final String NEXT_ID_QUERY=
      "SELECT IDNumber FROM alan.ObjectIDs WHERE className=?";
   private static final String UPDATE_ID_CMD=
      "UPDATE alan.ObjectIDs SET IDNumber=? WHERE className=?";
}