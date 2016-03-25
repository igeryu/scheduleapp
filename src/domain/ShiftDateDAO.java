//  ShiftDateDAO.java

/**
 * Changelog:
 * 2016-02-28 : Corrected insert() so that it works
 * 
 * 2016-03-02 : Added getCurrentShift(person_id) and changed getCurrentShift(person) to call the former method using person.getObjectID()
 * 2016-03-02 : Added getCurrentShift(person_id, date) and changed getCurrentShift(person_id) to call the former method using LocalDate.now()
 * 
 * 2016-03-08 : Added both getWeek() methods
 * 
 * 2016-03-24 : Changed all variables from Integer to int where appropriate
 * 2016-03-24 : Replaced debug `System.out` calls with `Logger` calls
 * 2016-03-24 : Formatted to match Google Java Style
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
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.Level;

import util.DBConnectionPool;

public class ShiftDateDAO {

//  private DBConnectionPool connPool;
  private static final Logger logger = Logger.getLogger(ShiftDateDAO.class.getName());

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

  private static final String GET_CURRENT_SHIFT_STMT = "SELECT * FROM shift_date "
      + "WHERE person_id = ?";

  public int getCurrentShift(Person person) {
    return getCurrentShift(person.getObjectID());
  }
  //  end method getCurrentShift(Person)

  public int getCurrentShift(int person_id) {
    return getCurrentShift(person_id, LocalDate.now());
  }
  //  end method getCurrentShift(person_id)

  /**
   *
   * @param person_id
   * @param shift_id
   * @param date
   *
   * @return
   */
  public int getCurrentShift(int person_id, LocalDate date) {

    PreparedStatement request = null;
    Connection conn = null;

    try {
      conn = DBConnectionPool.getPoolConnection();
      request = conn.prepareStatement(GET_CURRENT_SHIFT_STMT);
      request.setInt(1, person_id);

      ResultSet rset = request.executeQuery();

      //   Find the shift change date that is:
      //       A) Earlier than 'date'
      //       B) The most recent
      //   The above two conditions will yield the current shift.
      Date shiftDate = Date.valueOf("1900-01-01");
      int shift = 0;
      while (rset.next()) {
        Date tempDate = rset.getDate("change_date");

        LocalDate tempLocalDate = tempDate.toLocalDate();
        LocalDate shiftLocalDate = shiftDate.toLocalDate();
        LocalDate nextDayLocalDate = date.plus(1, ChronoUnit.DAYS);

        if (tempLocalDate.isAfter(shiftLocalDate)
            && tempLocalDate.isBefore(nextDayLocalDate)) {
          shiftDate = tempDate;
          shift = rset.getInt("shift_id");
        }  // check if next date is more current
      }  // loop through shift changes

      if (shift != 0) {
        return shift;
      }

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

  public ArrayList<String> getWeek(int person_id, LocalDate firstDay) {
    ArrayList<String> shifts = new ArrayList<>();
    Map<Integer, String> shiftMap = (new ShiftDAO()).getMap();

    for (int x = 0; x < 7; x++) {
      LocalDate date = firstDay.plusDays(x);
      int shift = getCurrentShift(person_id, date);
      shifts.add(shiftMap.get(shift));
    }

    return shifts;
  }  // end getWeek(Person, LocalDate)

  public ArrayList<String> getWeek(Person person, LocalDate firstDay) {
    int person_id = person.getObjectID();
    return getWeek(person_id, firstDay);
  }  // end getWeek(Person, LocalDate)

  private static final String INSERT_STMT = "INSERT INTO shift_date "
      + "VALUES (?, ?, ?, ?)";

  public void insert(int person_id, int shift_id, Date date) {
    Connection conn = null;
    PreparedStatement stmt = null;

    try {
      conn = DBConnectionPool.getPoolConnection();

      stmt = conn.prepareStatement(INSERT_STMT);
      ObjectIdDAO objIdDAO = new ObjectIdDAO();
      int shiftDateID = objIdDAO.getNextObjectId(ObjectIdDAO.SHIFT_DATE);
      stmt.setInt(1, shiftDateID);
      stmt.setInt(2, person_id);
      stmt.setDate(3, date);
      stmt.setInt(4, shift_id);
      stmt.executeUpdate();

      //  DEBUG:
      logger.info("\ninsert() successful.");
    } catch (SQLException se) {
      throw new RuntimeException(
          "A database error occurred. " + se.getMessage());
    } catch (Exception e) {
      logger.log(Level.WARNING, "insert() ~ Exception: " + e.getMessage());
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
   * <p>
   * Removes the entry in <code>SHIFT_DATE</code> that matches the
   * <code>Person</code> and <code>Date</code> parameters.</p>
   *
   * <p>
   * PRE: The person and date given are represented by one, and only one, entry
   * in the <code>SHIFT_DATE</code> table.</p>
   *
   * <p>
   * POST: There is no entry in the <code>SHIFT_DATE</code> table that matches
   * the given person and date.</p>
   *
   * @param person
   * @param date
   */
  public void removeStartDate(Person person, Date date) {
    Connection conn = null;
    PreparedStatement stmt = null;

    try {
      conn = DBConnectionPool.getPoolConnection();
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
  
  private static final String REMOVE_STMT = "DELETE FROM shift_date "
      + "WHERE  person_id = ? "
      + "AND   start_date = ?";
  
  /**
   * TODO: Fix this
   *
   * @param person
   */
  public void update(Person person) {
    Connection conn = null;
    PreparedStatement stmt = null;

    try {
      conn = DBConnectionPool.getPoolConnection();
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
