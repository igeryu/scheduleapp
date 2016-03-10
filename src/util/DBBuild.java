// DBBuild.java

/**
 * Changelog:
 * 
 * 2016-03-04 : Created file
 * 
 * 2016-03-05 : Got testDatabase() working, which will call buildDatabase() if the database is not built.
 * 
 * 2016-03-08 : Fixed buildObjectIdsTable() so that PERSON and SHIFT_DATE start with an ID of 1
 */
package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author alanjohnson
 */
public class DBBuild {
    private static final int PERSON_NAME_SIZE = 20;
    private static final int CLASS_NAME_SIZE = 15;
    private static final int RANK_NAME_SIZE = 5;
    private static final int SHIFT_NAME_SIZE = 5;
    private static final int WORKCENTER_NAME_SIZE = 8;
    
    private static boolean buildDatabase() {
        Connection conn;
        conn = DBConnectionPool.getPoolConnection();
        
        //  DEBUG:
        System.out.println("\n[DBBuild.buildDatabase()] Building schemas...!");
        
        if (!buildSchemas(conn))
            return false;
        
        //  DEBUG:
        System.out.println("\n[DBBuild.buildDatabase()] Building objectids...!");
        
        if (!buildObjectIdsTable(conn))
            return false;
        
        //  DEBUG:
        System.out.println("\n[DBBuild.buildDatabase()] Building rank...!");
        
        if (!buildRankTable(conn))
            return false;
        
        //  DEBUG:
        System.out.println("\n[DBBuild.buildDatabase()] Building shift...!");
        
        if (!buildShiftTable(conn))
            return false;
        
        //  DEBUG:
        System.out.println("\n[DBBuild.buildDatabase()] Building skill...!");
        
        if (!buildSkillTable(conn))
            return false;
        
        //  DEBUG:
        System.out.println("\n[DBBuild.buildDatabase()] Building workcenter...!");
        
        if (!buildWorkcenterTable(conn))
            return false;
        
        //  DEBUG:
        System.out.println("\n[DBBuild.buildDatabase()] Building person...!");
        
        if (!buildPersonTable(conn))
            return false;
        
        //  DEBUG:
        System.out.println("\n[DBBuild.buildDatabase()] Building shift_date...!");
        
        if (!buildShiftDateTable(conn))
            return false;
        
        //  DEBUG:
        System.out.println("\n[DBBuild.buildDatabase()] Assigning schemas...!");
        
        if (!assignTablesToSchemas(conn))
            return false;
        
        //  DEBUG:
        System.out.println("\n[DBBuild.buildDatabase()] Success!");
        
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
                assignStrings.add(String.format(buildStringTemplate, schema, table, table));
            }
        }
                
        try {
            for (String assignString : assignStrings) {
                PreparedStatement buildStmt = conn.prepareStatement(assignString);
                buildStmt.execute();
                buildStmt.close();
            }
            return true;
        } catch (SQLException sql) {
            System.out.println("SQL Exception: " + sql.getMessage());
        }
        return false;
    }
    
    private static boolean buildPersonTable(Connection conn) {
        String buildString = String.format("CREATE TABLE owner.person ("
                                         + "id INTEGER NOT NULL UNIQUE, "
                                         + "first_name    VARCHAR(%d) NOT NULL, "
                                         + "last_name     VARCHAR(%d) NOT NULL, "
                                         + "rank_id       INTEGER NOT NULL, "
                                         + "workcenter_id INTEGER NOT NULL, "
                                         + "shift_id      INTEGER NOT NULL, "
                                         + "skill_id      INTEGER NOT NULL)",
                                           PERSON_NAME_SIZE,
                                           PERSON_NAME_SIZE);
        
        ArrayList<String> alterStrings = new ArrayList<>();
        String alterStringTemplate = "ALTER TABLE owner.person "
                                       + "ADD CONSTRAINT person_fk_%sid "
                                       + "FOREIGN KEY (%s_id) "
                                       + "REFERENCES %s(id)";
        
        alterStrings.add(String.format(alterStringTemplate, "rank", "rank", "rank"));
        alterStrings.add(String.format(alterStringTemplate, "workcenter", "workcenter", "workcenter"));
        alterStrings.add(String.format(alterStringTemplate, "shift", "shift", "shift"));
        alterStrings.add(String.format(alterStringTemplate, "skill", "skill", "skill"));
        try {
            String checkString = "SELECT tablename FROM sys.systables WHERE tablename = 'person'";
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
            System.out.println("SQL Exception: " + sql.getMessage());
        }
        return false;
    }
    
    private static boolean buildObjectIdsTable(Connection conn) {
        String buildString = String.format("CREATE TABLE owner.objectids ("
                                         + "classname VARCHAR(%d) NOT NULL UNIQUE, "
                                         + "idnumber INTEGER)",
                                           CLASS_NAME_SIZE);
        String insertString = "INSERT INTO objectids VALUES "
                            + "('Person', 1), ('Shift_Date', 1)";
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
            System.out.println("SQL Exception: " + sql.getMessage());
        }
        return false;
    }

    private static boolean buildSchemas(Connection conn) {
        ArrayList<String> schemas      = new ArrayList<>();
        
        schemas.add("owner");
        schemas.add("manager");
        schemas.add("supervisor");
        schemas.add("any_user");
        
        String createSchemaTemplate = "CREATE SCHEMA %s";
        String  checkSchemaTemplate = "SELECT schemaname FROM sys.sysschemas WHERE schemaname = '%s'";
        
        
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
            System.out.println("SQL Exception: " + sql.getMessage());
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
            System.out.println("SQL Exception: " + sql.getMessage());
        }
        return false;
    }
    
    private static boolean buildShiftDateTable(Connection conn) {
        String buildString = "CREATE TABLE owner.shift_date ("
                               + "id INTEGER NOT NULL UNIQUE, "
                               + "person_id   INTEGER NOT NULL, "
                               + "change_date DATE        NOT NULL, "
                               + "shift_id    INTEGER NOT NULL)";
        
        ArrayList<String> alterStrings = new ArrayList<>();
        String alterStringTemplate = "ALTER TABLE owner.shift_date "
                                        + "ADD CONSTRAINT person_fk_%sid "
                                        + "FOREIGN KEY (%s_id) "
                                        + "REFERENCES %s(id)";
        
        alterStrings.add(String.format(alterStringTemplate, "person", "person", "person"));
        alterStrings.add(String.format(alterStringTemplate, "shift", "shift", "shift"));
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
            System.out.println("SQL Exception: " + sql.getMessage());
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
            System.out.println("SQL Exception: " + sql.getMessage());
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
            System.out.println("SQL Exception: " + sql.getMessage());
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
            System.out.println("SQL Exception: " + sql.getMessage());
        }
        return false;
    }

    public static void testDatabase() {
        Connection conn = null;
        conn = DBConnectionPool.getPoolConnection();
        try {
            conn.prepareStatement("SELECT * FROM shift_date").execute();
        } catch (SQLException sql) {
            String errorMessage = sql.getMessage();
            
            if (errorMessage.endsWith("was not found.") ||
                errorMessage.endsWith("does not exist.")) {
                System.out.println("Database does not exist, building database...");
                if(buildDatabase())
                    System.out.println("Database build successful.");
            }
        }
    }

}
