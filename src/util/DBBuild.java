// DBBuild.java

/**
 * Changelog:
 * 
 * 2016-03-04 : Created file
 * 
 * 2016-03-05 : Got `testDatabase()` working, which will call `buildDatabase()` if the database is not built.
 * 
 * 2016-03-08 : Fixed `buildObjectIdsTable()` so that `PERSON` and `SHIFT_DATE` start with an `ID` of `1`
 * 
 * 2016-03-24 : Replaced debug `System.out` calls with `Logger` calls
 * 2016-03-24 : Formatted to match Google Java Style
 * 
 * 2016-03-25 : Added `EVENT_DESCRIPTION_SIZE`
 * 2016-03-25 : Added `buildPersonEventTable()` method
 * 2016-03-25 : Modified `buildObjectIdsTable()` to include the `PERSON_EVENT` table
 * 
 * 2016-03-26 : Added `buildPersonEventTable()` call to `buildDatabase()`
 * 2016-03-26 : Adjusted `logger` messages and changed from `INFO` to `FINE`
 * 2016-03-26 : Added `buildPersonEventTypeTable()` method
 * 2016-03-26 : Removed the `NOT NULL` identifier from all foreign key declarations
 * 
 * To-Do:
 * TODO: Make ranks added by buildRankTable() user selected
 * TODO: Make shifts added by buildShiftTable() user selected
 * TODO: Make workcenters added by buildWorkcenterTable() user selected
 * TODO: Make skill added by buildSkillTable() user selected
 * TODO: Make event types added by buildPersonEventTypeTable() user selected
 */
package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 *
 * @author alanjohnson
 */
public class DBBuild {

  private static final int PERSON_NAME_SIZE = 20;
  private static final int EVENT_DESCRIPTION_SIZE = 20;
  private static final int EVENT_TYPE_SIZE = 15;
  private static final int CLASS_NAME_SIZE = 15;
  private static final int RANK_NAME_SIZE = 5;
  private static final int SHIFT_NAME_SIZE = 5;
  private static final int WORKCENTER_NAME_SIZE = 8;
  private static final Logger logger = Logger.getLogger(DBBuild.class.getName());

  private static boolean buildDatabase() {
    Connection conn = DBConnectionPool.getPoolConnection();

    //  DEBUG:
    logger.fine("Building schemas...!");

    if (!buildSchemas(conn)) {
      return false;
    }

    //  DEBUG:
    logger.fine("Building OBJECTIDS table...!");

    if (!buildObjectIdsTable(conn)) {
      return false;
    }

    //  DEBUG:
    logger.fine("Building RANK table...!");

    if (!buildRankTable(conn)) {
      return false;
    }

    //  DEBUG:
    logger.fine("Building SHIFT table...!");

    if (!buildShiftTable(conn)) {
      return false;
    }

    //  DEBUG:
    logger.fine("Building SKILL table...!");

    if (!buildSkillTable(conn)) {
      return false;
    }

    //  DEBUG:
    logger.fine("Building WORKCENTER table...!");

    if (!buildWorkcenterTable(conn)) {
      return false;
    }

    //  DEBUG:
    logger.fine("Building PERSON table...!");

    if (!buildPersonTable(conn)) {
      return false;
    }
    
    //  DEBUG:
    logger.fine("Building PERSON_EVENT_TYPE table...!");

    if (!buildPersonEventTypeTable(conn)) {
      return false;
    }
    
     //  DEBUG:
    logger.fine("Building PERSON_EVENT table...!");

    if (!buildPersonEventTable(conn)) {
      return false;
    }

    //  DEBUG:
    logger.fine("Building SHIFT_DATE table...!");

    if (!buildShiftDateTable(conn)) {
      return false;
    }

    //  DEBUG:
    logger.fine("Assigning schemas...!");

    if (!assignTablesToSchemas(conn)) {
      return false;
    }

    //  DEBUG:
    logger.fine("Success!");

    return true;
  }

  private static boolean assignTablesToSchemas(Connection conn) {
    ArrayList<String> schemas = new ArrayList<>();
    schemas.add("manager");
    schemas.add("supervisor");
    schemas.add("any_user");

    ArrayList<String> tables = new ArrayList<>();
    tables.add("objectids");
    tables.add("person");
    tables.add("rank");
    tables.add("shift");
    tables.add("shift_date");
    tables.add("skill");
    tables.add("workcenter");

    ArrayList<String> assignStrings = new ArrayList<>();
    String buildStringTemplate = "CREATE SYNONYM %s.%s for owner.%s";
    for (String schema : schemas) {
      for (String table : tables) {
        assignStrings.add(
            String.format(buildStringTemplate, schema, table, table));
      }
    }

    try {
      for (String assignString : assignStrings) {
        PreparedStatement buildStmt
            = conn.prepareStatement(assignString);
        buildStmt.execute();
        buildStmt.close();
      }
      return true;
    } catch (SQLException sql) {
      logger.log(Level.WARNING, "SQL Exception: " + sql.getMessage());
    }
    return false;
  }

  private static boolean buildPersonTable(Connection conn) {
    String buildString = String.format("CREATE TABLE owner.person ("
                                       + "id INTEGER NOT NULL UNIQUE, "
                                       + "first_name    VARCHAR(%d) NOT NULL, "
                                       + "last_name     VARCHAR(%d) NOT NULL, "
                                       + "rank_id       INTEGER, "
                                       + "workcenter_id INTEGER, "
                                       + "shift_id      INTEGER, "
                                       + "skill_id      INTEGER)",
                                       PERSON_NAME_SIZE,
                                       PERSON_NAME_SIZE);

    ArrayList<String> alterStrings = new ArrayList<>();
    String alterStringTemplate = "ALTER TABLE owner.person "
                                 + "ADD CONSTRAINT person_fk_%sid "
                                 + "FOREIGN KEY (%s_id) "
                                 + "REFERENCES %s(id)";

    alterStrings.add(String.format(alterStringTemplate,
                                   "rank", "rank", "rank"));
    alterStrings.add(String.format(alterStringTemplate,
                                   "workcenter", "workcenter", "workcenter"));
    alterStrings.add(String.format(alterStringTemplate,
                                   "shift", "shift", "shift"));
    alterStrings.add(String.format(alterStringTemplate,
                                   "skill", "skill", "skill"));
    try {
      String checkString =
          "SELECT tablename FROM sys.systables WHERE tablename = 'person'";
      PreparedStatement checkStatement = conn.prepareStatement(checkString);

      if (!checkStatement.executeQuery().next()) {
        PreparedStatement buildStmt = conn.prepareStatement(buildString);
        buildStmt.execute();
        buildStmt.close();

        for (String alterString : alterStrings) {
          PreparedStatement alterStmt = conn.prepareStatement(alterString);
          alterStmt.execute();
          alterStmt.close();
        }
      }
      checkStatement.close();

      return true;
    } catch (SQLException sql) {
      if (sql.getMessage().contains("already exists in Schema")) {
        return true;
      }
      logger.log(Level.SEVERE, "SQL Exception: " + sql.getMessage());
    }
    return false;
  }
  
  private static boolean buildPersonEventTypeTable(Connection conn) {
    String buildString = String.format("CREATE TABLE owner.person_event_type ("
                                       + "id INTEGER NOT NULL UNIQUE, "
                                       + "name VARCHAR(%d) NOT NULL UNIQUE)",
                                       EVENT_TYPE_SIZE);
    
    String insertString = "INSERT INTO person_event_type VALUES "
                          + "(1, 'ALS'),   (2, 'Appointment'),   (3, 'CTO'), "
                          + "(4, 'Leave'),  (5, 'PT'),  (6, 'PT Test'), "
                          + "(7, 'TDY')";
    try {
      PreparedStatement buildStmt = conn.prepareStatement(buildString);
      buildStmt.execute();
      buildStmt.close();

      PreparedStatement insertStmt = conn.prepareStatement(insertString);
      insertStmt.execute();
      insertStmt.close();
      
      return true;
    } catch (SQLException sql) {
      if (sql.getMessage().contains("already exists in Schema")) {
        return true;
      }
      logger.log(Level.WARNING, "SQL Exception: " + sql.getMessage());
    }
    return false;
  }
  
  private static boolean buildPersonEventTable(Connection conn) {
    String buildString =
        String.format("CREATE TABLE owner.person_event ("
                      + "id                   INTEGER NOT NULL UNIQUE, "
                      + "start_date           DATE NOT NULL, "
                      + "start_time           TIME NOT NULL, "
                      + "end_date             DATE NOT NULL, "
                      + "end_time             TIME NOT NULL, "
                      + "description          VARCHAR(%D) NOT NULL, "
                      + "person_event_type_id INTEGER, "
                      + "person_id            INTEGER)",
                      EVENT_DESCRIPTION_SIZE);

    ArrayList<String> alterStrings = new ArrayList<>();
    String alterStringTemplate = "ALTER TABLE owner.person_event "
                                 + "ADD CONSTRAINT personevent_fk_%sid "
                                 + "FOREIGN KEY (%s_id) "
                                 + "REFERENCES %s(id)";

    alterStrings.add(String.format(alterStringTemplate,
                                   "person_event_type",
                                   "person_event_type",
                                   "person_event_type"));
    alterStrings.add(String.format(alterStringTemplate,
                                   "person", "person", "person"));
    
    try {
      String checkString =
          "SELECT tablename "
          + "FROM sys.systables "
          + "WHERE tablename = 'person_event_type'";
      
      PreparedStatement checkStatement = conn.prepareStatement(checkString);

      if (!checkStatement.executeQuery().next()) {
        PreparedStatement buildStmt = conn.prepareStatement(buildString);
        buildStmt.execute();
        buildStmt.close();

        for (String alterString : alterStrings) {
          PreparedStatement insertStmt = conn.prepareStatement(alterString);
          insertStmt.execute();
          insertStmt.close();
        }
      }
      checkStatement.close();

      return true;
    } catch (SQLException sql) {
      if (sql.getMessage().contains("already exists in Schema")) {
        return true;
      }
      logger.log(Level.SEVERE, "SQL Exception: " + sql.getMessage());
    }
    return false;
  }

  private static boolean buildObjectIdsTable(Connection conn) {
    String buildString =
        String.format("CREATE TABLE owner.objectids ("
                      + "classname VARCHAR(%d) NOT NULL UNIQUE, "
                      + "idnumber INTEGER)",
                      CLASS_NAME_SIZE);
    
    //  TODO:  Make this not hard-coded:
    String insertString = "INSERT INTO objectids VALUES "
        + "('Person', 1), ('Shift_Date', 1), ('Person_Event', 1)";
    try {
      PreparedStatement buildStmt = conn.prepareStatement(buildString);
      buildStmt.execute();
      buildStmt.close();

      PreparedStatement insertStmt = conn.prepareStatement(insertString);
      insertStmt.execute();
      insertStmt.close();
      return true;
    } catch (SQLException sql) {
      if (sql.getMessage().contains("already exists in Schema")) {
        return true;
      }
      logger.log(Level.WARNING, "SQL Exception: " + sql.getMessage());
    }
    return false;
  }

  private static boolean buildSchemas(Connection conn) {
    ArrayList<String> schemas = new ArrayList<>();

    schemas.add("owner");
    schemas.add("manager");
    schemas.add("supervisor");
    schemas.add("any_user");

    String createSchemaTemplate = "CREATE SCHEMA %s";
    String checkSchemaTemplate =
        "SELECT schemaname FROM sys.sysschemas WHERE schemaname = '%s'";

    try {
      for (String schema : schemas) {
        String checkString = String.format(checkSchemaTemplate, schema);
        String createString = String.format(createSchemaTemplate, schema);

        PreparedStatement checkStatement = conn.prepareStatement(checkString);
        ResultSet checkSet = checkStatement.executeQuery();

        if (checkSet.next()) {
          PreparedStatement createStmt = conn.prepareStatement(createString);
          createStmt.execute();
          createStmt.close();
        }
        checkStatement.close();
      }
      return true;
    } catch (SQLException sql) {
      logger.log(Level.WARNING, "SQL Exception: " + sql.getMessage());
    }
    return false;
  }

  private static boolean buildRankTable(Connection conn) {
    String buildString = String.format("CREATE TABLE owner.rank ("
                                       + "id INTEGER NOT NULL UNIQUE, "
                                       + "name VARCHAR(%d) NOT NULL UNIQUE)",
                                       RANK_NAME_SIZE);
    
    String insertString = "INSERT INTO rank VALUES "
                          + "(1, 'AB'),   (2, 'Amn'),   (3, 'A1C'), "
                          + "(4, 'SrA'),  (5, 'SSgt'),  (6, 'TSgt'), "
                          + "(7, 'MSgt'), (8, 'SMSgt'), (9, 'CMSgt')";
    try {
      PreparedStatement buildStmt = conn.prepareStatement(buildString);
      buildStmt.execute();
      buildStmt.close();

      PreparedStatement insertStmt = conn.prepareStatement(insertString);
      insertStmt.execute();
      insertStmt.close();
      
      return true;
    } catch (SQLException sql) {
      if (sql.getMessage().contains("already exists in Schema")) {
        return true;
      }
      logger.log(Level.WARNING, "SQL Exception: " + sql.getMessage());
    }
    return false;
  }

  private static boolean buildShiftDateTable(Connection conn) {
    String buildString = "CREATE TABLE owner.shift_date ("
                         + "id INTEGER NOT NULL UNIQUE, "
                         + "person_id   INTEGER, "
                         + "change_date DATE    NOT NULL, "
                         + "shift_id    INTEGER)";

    ArrayList<String> alterStrings = new ArrayList<>();
    String alterStringTemplate = "ALTER TABLE owner.shift_date "
                                 + "ADD CONSTRAINT shiftdate_fk_%sid "
                                 + "FOREIGN KEY (%s_id) "
                                 + "REFERENCES %s(id)";

    alterStrings.add(String.format(alterStringTemplate,
                                   "person", "person", "person"));
    alterStrings.add(String.format(alterStringTemplate,
                                   "shift", "shift", "shift"));
    try {
      PreparedStatement buildStmt = conn.prepareStatement(buildString);
      buildStmt.execute();
      buildStmt.close();

      for (String alterString : alterStrings) {
        PreparedStatement insertStmt = conn.prepareStatement(alterString);
        insertStmt.execute();
        insertStmt.close();
      }
      return true;
    } catch (SQLException sql) {
      if (sql.getMessage().contains("already exists in Schema")) {
        return true;
      }
      logger.log(Level.WARNING, "SQL Exception: " + sql.getMessage());
    }
    return false;
  }

  private static boolean buildShiftTable(Connection conn) {
    String buildString = String.format("CREATE TABLE owner.shift ("
                                       + "id INTEGER NOT NULL UNIQUE, "
                                       + "name VARCHAR(%d) NOT NULL UNIQUE)",
                                       SHIFT_NAME_SIZE);
    
    String insertString = "INSERT INTO shift VALUES "
                          + "(1, 'Mid'), (2, 'Day'), (3, 'Swing')";
    try {
      PreparedStatement buildStmt = conn.prepareStatement(buildString);
      buildStmt.execute();
      buildStmt.close();

      PreparedStatement insertStmt = conn.prepareStatement(insertString);
      insertStmt.execute();
      insertStmt.close();
      return true;
    } catch (SQLException sql) {
      if (sql.getMessage().contains("already exists in Schema")) {
        return true;
      }
      logger.log(Level.WARNING, "SQL Exception: " + sql.getMessage());
    }
    return false;
  }

  private static boolean buildSkillTable(Connection conn) {
    String buildString = "CREATE TABLE owner.skill ("
                         + "id INTEGER NOT NULL UNIQUE, "
                         + "level INTEGER NOT NULL UNIQUE)";
    
    String insertString = "INSERT INTO skill VALUES "
                          + "(1, 3), (2, 5), (3, 7), (4, 9)";
    
    try {
      PreparedStatement buildStmt = conn.prepareStatement(buildString);
      buildStmt.execute();
      buildStmt.close();

      PreparedStatement insertStmt = conn.prepareStatement(insertString);
      insertStmt.execute();
      insertStmt.close();
      return true;
    } catch (SQLException sql) {
      if (sql.getMessage().contains("already exists in Schema")) {
        return true;
      }
      logger.log(Level.WARNING, "SQL Exception: " + sql.getMessage());
    }
    return false;
  }

  private static boolean buildWorkcenterTable(Connection conn) {
    String buildString = String.format("CREATE TABLE owner.workcenter ("
                                       + "id INTEGER NOT NULL UNIQUE, "
                                       + "name VARCHAR(%d) NOT NULL UNIQUE)",
                                       WORKCENTER_NAME_SIZE);
    
    String insertString = "INSERT INTO workcenter VALUES "
                          + "(1, 'APG'),  (2, 'Comm Nav'), "
                          + "(3, 'E&E'),  (4, 'Engines'), "
                          + "(5, 'GACS'), (6, 'Hydro')";
    try {
      PreparedStatement buildStmt = conn.prepareStatement(buildString);
      buildStmt.execute();
      buildStmt.close();

      PreparedStatement insertStmt = conn.prepareStatement(insertString);
      insertStmt.execute();
      insertStmt.close();
      return true;
    } catch (SQLException sql) {
      if (sql.getMessage().contains("already exists in Schema")) {
        return true;
      }
      logger.log(Level.WARNING, "SQL Exception: " + sql.getMessage());
    }
    return false;
  }

  public static void testDatabase() {
    Connection conn = DBConnectionPool.getPoolConnection();
    
    try {
      conn.prepareStatement("SELECT * FROM shift_date").execute();
    } catch (SQLException sql) {
      String errorMessage = sql.getMessage();

      if (errorMessage.endsWith("was not found.")
          || errorMessage.endsWith("does not exist.")) {
        logger.log(Level.INFO, "Database does not exist, building database...");
        if (buildDatabase()) {
          logger.log(Level.INFO, "Database build successful.");
        }
      }
    }
  }

}
