//  ShiftDateDAO.java

/**
 * Changelog:
 * 2016-02-28 : Corrected insert() so that it works
 */

/**
 * @author Alan Johnson
 */
package domain;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import util.DBConnectionPool;

public class ShiftDateDAO {

    private DBConnectionPool connPool;
    
    
    
    private static final String REMOVE_STMT = "DELETE FROM shift_date "
            + "WHERE  person_id = ? "
            + "AND   start_date = ?";
    /**
     * <p>Removes the entry in <code>SHIFT_DATE</code> that matches the
     * <code>Person</code> and <code>Date</code> parameters.</p>
     * 
     * <p>PRE: The person and date given are represented by one, and only one,
     *         entry in the <code>SHIFT_DATE</code> table.</p>
     * 
     * <p>POST: There is no entry in the <code>SHIFT_DATE</code> table that
     *          matches the given person and date.</p>
     * @param person
     * @param date 
     */
    public void removeStartDate(Person person, Date date) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = connPool.getPoolConnection();
            stmt = conn.prepareStatement(REMOVE_STMT);
            
            stmt.setInt(1, person.getObjectID());
            stmt.setDate(2, date);
            stmt.executeUpdate();

        } catch (SQLException se) {
            throw new RuntimeException(
                    "A database error occurred. " + se.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Exception: " + e.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException se) {
                    se.printStackTrace(System.err);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                }
            }
        }
    }
    
    
    
    private static final String GET_CURRENT_SHIFT_STMT = "SELECT * FROM shift_date "
            + "WHERE person_id = ?";
    public Integer getCurrentShift(Person person) {
        
        PreparedStatement request = null;
        Connection conn = null;

        try {
            conn = DBConnectionPool.getPoolConnection();
            request = conn.prepareStatement(GET_CURRENT_SHIFT_STMT);
            request.setInt(1, person.getObjectID());

            ResultSet rset = request.executeQuery();

            
            //   Find the shift change date that is:
            //       A) Today earlier
            //       B) The most recent
            //   The above two conditions will yield the current shift.
            Date shiftDate = Date.valueOf("1900-01-01");
            Integer shift = 0;
            while (rset.next()) {
                Date tempDate = rset.getDate("change_date");
                
                LocalDate tempLocalDate = tempDate.toLocalDate();
                LocalDate shiftLocalDate = shiftDate.toLocalDate();
                LocalDate tomorrowLocalDate = LocalDate.now().plus(1, ChronoUnit.DAYS);
                
                if (tempLocalDate.isAfter(shiftLocalDate)
                        && tempLocalDate.isBefore(tomorrowLocalDate)) {
                    shiftDate = tempDate;
                    shift = rset.getInt("shift_id");
                }  // check if next date is more current
            }  // loop through shift changes
            
            if (shift != 0)
                return shift;
            
        } catch (SQLException se) {
            throw new RuntimeException(
                    "A database error occurred. " + se.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Exception: " + e.getMessage());
        } finally {
            if (request != null) {
                try {
                    request.close();
                } catch (SQLException se) {
                    se.printStackTrace(System.err);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                }
            }
        }

        return -1;
    }
    

    
    
    private static final String INSERT_STMT = "INSERT INTO shift_date "
            + "VALUES (?, ?, ?, ?)";
    /**
     * 
     * @param person_id
     * @param shift_id
     * @param date 
     */
    public void insert(Integer person_id, Integer shift_id, Date date) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        
        try {
            conn = connPool.getPoolConnection();
            
            stmt = conn.prepareStatement(INSERT_STMT);
            ObjectIdDAO objIdDAO = new ObjectIdDAO();
            int shiftDateID = objIdDAO.getNextObjectID(ObjectIdDAO.SHIFT_DATE);
            stmt.setInt(1, shiftDateID);
            stmt.setInt(2, person_id);
            stmt.setDate(3, date);
            stmt.setInt(4, shift_id);
            stmt.executeUpdate();
            
            //  DEBUG:
            System.out.println("\ninsert() successful.");
        } catch (SQLException se) {
            throw new RuntimeException(
                    "A database error occurred. " + se.getMessage());
        } catch (Exception e) {
            System.out.printf("insert() ~ Exception: %s", e.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException se) {
                    se.printStackTrace(System.err);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                }
            }
            
            
        }
        
        
        
    }  //  end method insert()
    
    

    
    /**
     * 
     * @param fn
     * @param ln
     * @param ph
     * @return 
     */
    public boolean addStartDate(Person person, Date date) {
        if (person == null || date == null) {
            return false;
        }

        insert(person.getObjectID(), person.getShiftID(), date);

        return true;
    }
    
    

    /**
     * TODO:  Fix this
     * @param person 
     */
    public void update(Person person) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = connPool.getPoolConnection();
            stmt = conn.prepareStatement(UPDATE_STMT);
            stmt.setString(1, person.getFirstName());
            stmt.setString(2, person.getLastName());
            stmt.setInt(4, person.getObjectID());
            stmt.executeUpdate();

        } catch (SQLException se) {
            throw new RuntimeException(
                    "A database error occurred. " + se.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Exception: " + e.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException se) {
                    se.printStackTrace(System.err);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                }
            }
        }
    }
    private static final String UPDATE_STMT = "UPDATE shift_date "
            + "SET first_name = ?, last_name = ?, phone = ?"
            + "WHERE id = ?";

}
