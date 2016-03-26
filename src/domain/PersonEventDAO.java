//  PersonEventDAO.java

/**
 * Changelog:
 * 2016-03-25 : Created from ShiftDateDAO template
 * 2016-03-25 : Got both getWeekEvents() methods and the getEvent() method working
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
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import util.DBConnectionPool;

public class PersonEventDAO {
  
  private static final Logger logger = Logger.getLogger(PersonEventDAO.class.getName());

  private ObservableList<ObservableList<StringProperty>> getWeekEvents(int person_id, LocalDate firstDay) {
    ObservableList<ObservableList<StringProperty>> weekEventsDescriptions = FXCollections.observableArrayList();
//    Map<Integer, String> shiftMap = (new ShiftDAO()).getMap();

    for (int x = 0; x < 7; x++) {
      LocalDate date = firstDay.plusDays(x);
      weekEventsDescriptions.add( getEvent(person_id, date) );
    }

    return weekEventsDescriptions;
  }  // end getWeek(Person, LocalDate)
  
  public ObservableList<ObservableList<StringProperty>> getWeekEvents(Person person, LocalDate firstDay) {
    int person_id = person.getObjectID();
    return getWeekEvents(person_id, firstDay);
  }  // end getWeekShifts(Person, LocalDate)
  
  private ObservableList<StringProperty> getEvent(int person_id,
                                     /*int shift_id,*/
                                     LocalDate date) {
    PreparedStatement request = null;
    Connection conn = null;
    
    LocalDate yesterday = date.minusDays(1);
    LocalDate tomorrow = date.plusDays(1);
    
    try {
      conn = DBConnectionPool.getPoolConnection();
      request = conn.prepareStatement(GET_STMT);
      request.setInt(1, person_id);

      ResultSet rset = request.executeQuery();

      //   Find the events that:
      //       A) Starts on or before today
      //       B) Ends on or after today
      //   The above two conditions will yield a valid event.
      ObservableList<StringProperty> descriptions = FXCollections.observableArrayList();
      
      while (rset.next()) {
        Date tempStartDate = rset.getDate("start_date");
        Date tempEndDate = rset.getDate("end_date");
        
        logger.info("id = " + rset.getInt("id") +
                   "\n      start_date = " + tempStartDate + 
                   "\n      end_date = "   + tempEndDate);
        
        String description;

        LocalDate tempStartLocalDate = tempStartDate.toLocalDate();
        LocalDate tempEndLocalDate = tempEndDate.toLocalDate();
        
        if (tempStartLocalDate.isBefore(tomorrow)
            && tempEndLocalDate.isAfter(yesterday)) {
          description = rset.getString("description");
          
//          if (shift_id < 1) {
//            descriptions.add(description);
//          }
          
          descriptions.add(new SimpleStringProperty(description));
          
        }  // check if event includes today
      }  // loop through person events
      
      return descriptions;
      
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

  }
  private static final String GET_STMT = "SELECT * "
                                         + "FROM person_event "
                                         + "WHERE person_id = ? ";

}
