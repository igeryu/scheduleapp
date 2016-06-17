//  PersonDAO.java

/**
 * Changelog:
 * 2016-02-24 : Added getPeopleByShift() method to assist with new JavaFX layout
 * 
 * 2016-02-25 : Added workcenter to GET_BY_SHIFT_STMT WHERE clause, and modified getPeopleByShift() to use that new argument
 * 
 * 2016-02-29 : Updated addPerson() to accept shift date and properly update the SHIFT_DATE table
 * 2016-02-29 : Updated update() method to make changes to all PERSON attributes
 * 
 * 2016-03-02 : Changed getPeopleByShift() to getPeopleListByShift()
 * 2016-03-02 : Changed getPeople() to getPeopleTableByShift()
 * 2016-03-02 : Added getPeopleListByShift(workcenter, shift, date).  The existing getPeopleListByShift() now calls this new overload using today's date.
 * 
 * 2016-03-07 : Renamed both getPeopleListByShift() methods to getPeopleObsListByShift()
 * 2016-03-07 : Added both getPeopleArrayListByShift() methods
 * 2016-03-07 : Added getAllPeople()
 * 
 * 2016-03-13 : Modified getPeopleArrayListByShift(shift, workcenter, date) to allow for the shift or date parameters to be wildcards (< 1)
 * 2016-03-13 : Changed all method parameters from having an Integer parameter to primitive int parameters
 * 
 * 2016-03-25 : Formatted to match Google Java Style
 * 2016-03-25 : Replaced debug System.out calls with Logger calls
 * 
 * 2016-06-17 : Added `getPerson(String, String, int)` method to help `MainStage.java` find the appropriate person to edit
 */

/**
 *
 * @author Alan Johnson
 */
package domain;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.swing.table.TableModel;

import net.proteanit.sql.DbUtils;

import util.DBConnectionPool;

public class PersonDAO {

//    private DBConnectionPool connPool;
  private static final Logger logger = Logger.getLogger(PersonDAO.class.getName());
    
    //  TODO:  Build an arraylist of all people at initialization, then make methods to return inviduals that match criteria (shift, workcenter, etc)
    //  TODO:  Refactor other classes that use PersonDAO, to only use one instance, so that the population of the arraylist mentioned above happens only when necessary
    
    public void delete(Person person) {
        //  DEBUG:
        logger.fine("[PersonDAO_New.delete()] Entering method...");
        
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBConnectionPool.getPoolConnection();
            stmt = conn.prepareStatement(DELETE_STMT);
            
            stmt.setInt(1, person.getObjectID());
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
        logger.fine("[PersonDAO_New.delete()] Exiting method...");
    }
    private static final String DELETE_STMT = "DELETE FROM person "
            + "WHERE id = ?";
    
    public Person getPerson(int personID) {
        Person person = null;
        PreparedStatement request = null;
        Connection conn = null;

        try {
            conn = DBConnectionPool.getPoolConnection();
            request = conn.prepareStatement(GET_BY_ID_STMT);
            request.setInt(1, personID);

            ResultSet rset = request.executeQuery();

            if (rset.next()) {
                int id = rset.getInt("id");
                String firstName = rset.getString("first_name");
                String lastName = rset.getString("last_name");

                //  TODO:  Fix this:
                person = new Person(id, firstName, lastName, 0, 0, 0, 0);
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

        return person;
    }
    private static final String GET_BY_ID_STMT = "SELECT * FROM person "
            + "WHERE id = ?";
    
    public Person getPerson(String firstName, String lastName, int rankId) {
        Person person = null;
        PreparedStatement request = null;
        Connection conn = null;

        try {
            conn = DBConnectionPool.getPoolConnection();
            request = conn.prepareStatement(GET_STMT);
            request.setString(1, firstName);
            request.setString(2, lastName);
            request.setInt(3, rankId);

            ResultSet rset = request.executeQuery();

            
            if (rset.next()) {
                int id = rset.getInt("id");
                int wcId = rset.getInt("workcenter_id");
                int shId = rset.getInt("shift_id");
                int skId = rset.getInt("skill_id");

                //  TODO:  Fix this:
                person = new Person(id, firstName, lastName, rankId, wcId, shId, skId);
            }
            if (rset.next()) {
              return null;
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

        return person;
    }
    private static final String GET_STMT = "SELECT * FROM person "
            + "WHERE first_name = ? "
            + "AND   last_name  = ? "
            + "AND   rank_id    = ?";

    public ArrayList<Person> findPeople(String search) {

        PreparedStatement request = null;
        Connection conn = null;

        ArrayList<Person> personList = new ArrayList<>();
        try {
            conn = DBConnectionPool.getPoolConnection();
            request = conn.prepareStatement(SEARCH_STMT);
            search = search.toUpperCase();
            request.setString(1, "%" + search + "%");
            request.setString(2, "%" + search + "%");

            ResultSet rset = request.executeQuery();

            while (rset.next()) {
                int id = rset.getInt("id");
                String firstName = rset.getString("first_name");
                String lastName = rset.getString("last_name");

                //  TODO:  Fix this:
                Person person = new Person(id, firstName, lastName, 0, 0, 0, 0);

                personList.add(person);
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

        return personList;
    }
    private static final String SEARCH_STMT = "SELECT * FROM person "
            + "WHERE (UPPER(first_name) LIKE ?) "
            + "OR    (UPPER(last_name)  LIKE ?)";

    public ArrayList<Person> getAllPeople() {

        PreparedStatement request = null;
        Connection conn = null;
        
        ArrayList<Person> personList = new ArrayList<>();
        
        try {
            conn = DBConnectionPool.getPoolConnection();
            request = conn.prepareStatement(GET_ALL_STMT);

            ResultSet rset = request.executeQuery();
            
            while (rset.next()) {
                int id = rset.getInt("id");
                
                String firstName = rset.getString("first_name");
                String lastName = rset.getString("last_name");
                int rank = rset.getInt("rank_id");
                int skill = rset.getInt("skill_id");
                int workcenter = rset.getInt("workcenter_id");
                
                
                logger.fine(String.format("PersonDAO_New.getPeopleByShift()\n"
                                + "rank: %s\nskill: %s", rank, skill));

                //  TODO:  Fix this:
                Person person = new Person(firstName, lastName,
                                           rank, workcenter, skill);
                person.setObjectID(id);
                
                logger.fine(String.format("PersonDAO_New.getPeopleByShift()\n"
                                + "person.rank: %s\n\"person.skill: %s",
                                person.getRank(), person.getSkill()));

                personList.add(person);
            }

            logger.log(Level.FINE, "Test Point A");
            
            return personList;

        } catch (SQLException se) {
            throw new RuntimeException(
                    "[PersonDAO_New.getPeopleByShift()] A database error occurred. " + se.getMessage());
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

//        return null;
    }
    private static final String GET_ALL_STMT = "SELECT * FROM person";
    
    public ArrayList<Person> getPeopleArrayListByShift(int shift,
                                                       int workcenter,
                                                       LocalDate date) {
      logger.fine("Entering PersonDAO.getPeopleArrayListByShift\n"
                + "  shift      = " + shift + "\n"
                + "  workcenter = " + workcenter + "\n"
                + "  date       = " + date + "\n");
      PreparedStatement request = null;
      Connection conn = null;
      if (date == null) date = LocalDate.now();
      
      ObservableList<Person> personData = null;
      ArrayList<Person> personList = new ArrayList<>();
      
      try {
          conn = DBConnectionPool.getPoolConnection();
          request = conn.prepareStatement(GET_BY_SHIFT_STMT);
          
          if (workcenter >= 1)
              request.setInt(1, workcenter);
          else
              request.setString(1, "%");

          ResultSet rset = request.executeQuery();
          ShiftDateDAO shiftDateDao = new ShiftDateDAO();
          
          while (rset.next()) {
              int id = rset.getInt("id");
              int shift_id = shiftDateDao.getCurrentShift(id, date);
              
              //  DEBUG:
              logger.fine("shift_id = " + shift_id + "\n");
              
              if (shift >= 1 && shift_id != shift) {
                  continue;
              }
              
              String firstName = rset.getString("first_name");
              String lastName = rset.getString("last_name");
              int rank_id = rset.getInt("rank_id");
              int skill_id = rset.getInt("skill_id");
              int workcenter_id = rset.getInt("workcenter_id");
              
              logger.log(Level.FINE,
                  String.format("PersonDAO_New.getPeopleByShift()\n"
                              + "rank: %s\nskill: %s", rank_id, skill_id));

              //  TODO:  Fix this:
              Person person = new Person(firstName, lastName,
                                         rank_id,   workcenter_id, skill_id);
              person.setObjectID(id);
              
              logger.log(Level.FINE,
                  String.format("PersonDAO.getPeopleByShift()\n"
                              + "person.rank: %s\n\"person.skill: %s",
                              person.getRank(), person.getSkill()));

              personList.add(person);
          }
          
          logger.log(Level.FINE, String.format("Test Point A"));
          
          return personList;

      } catch (SQLException se) {
          throw new RuntimeException(
                  "[PersonDAO_New.getPeopleByShift()] A database error occurred. " + se.getMessage());
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

//        return null;
    }
    
    public ArrayList<Person> getPeopleArrayListByShift(int shift,
                                                       int workcenter) {
        return getPeopleArrayListByShift(shift, workcenter, LocalDate.now());
    }
    
    public ObservableList<Person> getPeopleObsListByShift(int shift,
                                                          int workcenter) {
        return getPeopleObsListByShift(shift, workcenter, LocalDate.now());
    }
    
    public ObservableList<Person> getPeopleObsListByShift(int shift,
                                                          int workcenter,
                                                          LocalDate date) {
        return FXCollections.observableList(getPeopleArrayListByShift(shift, workcenter, date));
    }
    private static final String GET_BY_SHIFT_STMT = "SELECT * FROM person "
            + "WHERE CAST (workcenter_id AS CHAR) LIKE ?";
    
   /**
    * Depreciated
    * @param workcenter
    * @param shift
    * @return 
    */
    public TableModel getPeopleTableByShift(int workcenter, int shift) {
        
        PreparedStatement request = null;
        Connection conn = null;

        ArrayList<Person> personList = new ArrayList<>();
        try {
            conn = DBConnectionPool.getPoolConnection();
            request = conn.prepareStatement(GET_STATEMENT);
            request.setInt(1, workcenter);
            request.setInt(2, shift);

            ResultSet rset = request.executeQuery();
            
            return DbUtils.resultSetToTableModel(rset);

        } catch (SQLException se) {
            logger.log(Level.WARNING, String.format("A database error occurred. " + se.getMessage()));
        } catch (Exception e) {
            throw new RuntimeException("Exception: " + e.getMessage());
        } finally {
            
            // DEBUG:
            logger.log(Level.FINE, String.format("PersonDAO.getPeople() : finally block"));
            
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

        return null;
    }
    private static final String GET_STATEMENT = "SELECT rank.name AS \"Rank\", "
            + "last_name AS \"Last Name\", first_name AS \"First Name\", "
            + "shift.name AS \"Shift\", skill.level AS \"Skill Lv\" "
            + "FROM person, rank, workcenter, shift, skill "
            + "WHERE workcenter.id = ? "
            + "AND   person.workcenter_id = workcenter.id "
            + "AND   shift.id = ? "
            + "AND   person.shift_id = shift.id "
            + "AND   person.rank_id = rank.id "
            + "AND   person.skill_id = skill.id";
    
    private static final String INSERT_STMT = "INSERT INTO person "
            + "VALUES (?, ?, ?, ?, ?, ?, ?)";
    
    private void insert(Person person) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;

        //  DEBUG:
        logger.log(Level.FINE, String.format("Inserting Person:\n" + person));
        
        
        try {
            
            //  DEBUG:
          logger.log(Level.FINE, String.format("PersonDAO.insert() Checkpoint A"));
            
            conn = DBConnectionPool.getPoolConnection();
            
            //  DEBUG:
            logger.log(Level.FINE, String.format("PersonDAO.insert() Checkpoint B"));
            
            stmt = conn.prepareStatement(INSERT_STMT);
            ObjectIdDAO objIdDAO = new ObjectIdDAO();
            int personID = objIdDAO.getNextObjectId(ObjectIdDAO.PERSON);
            stmt.setInt(1, personID);
            stmt.setString(2, person.getFirstName());
            stmt.setString(3, person.getLastName());
            stmt.setInt(4, person.getRankID());
            stmt.setInt(5, person.getWorkcenterID());
            stmt.setInt(6, person.getShiftID());
            stmt.setInt(7, person.getSkillID());
            stmt.executeUpdate();
            person.setObjectID(personID);
            
            //  DEBUG:
            logger.log(Level.FINE, String.format("insert() successful."));
        } catch (SQLException se) {
            throw new RuntimeException(
                    "A database error occurred. " + se.getMessage());
        } catch (Exception e) {
          logger.log(Level.FINE, String.format("insert() ~ Exception: %s", e.getMessage()));
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
     * TODO:  Need to add Rank, Skill, Work Center
     * 
     * @param fn
     * @param ln
     * @param ph
     * @return 
     */
    public boolean addPerson(String  fn,    String ln,
                             int rank,  int workcenter,
                             int shift, int skill,
                             Date startDate) {
        //  TODO:  Fix this:
        Person person = new Person(-1, fn, ln, rank, workcenter, shift, skill);
        
        if (person == null) {
            return false;
        }
        
        insert(person);
        (new ShiftDateDAO()).addStartDate(person, startDate);

        return true;
    }  // end method addPerson()
    
      private static final String UPDATE_STMT = "UPDATE person "
            + "SET first_name = ?, last_name = ?, "
            + "rank_id = ?, workcenter_id = ?, "
            + "shift_id = ?, skill_id = ?"
            + "WHERE id = ?";

    private void update(Person person) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBConnectionPool.getPoolConnection();
            stmt = conn.prepareStatement(UPDATE_STMT);
            stmt.setString(1, person.getFirstName());
            stmt.setString(2, person.getLastName());
            stmt.setInt(3, person.getRankID());
            stmt.setInt(4, person.getWorkcenterID());
            stmt.setInt(5, person.getShiftID());
            stmt.setInt(6, person.getSkillID());
            stmt.setInt(7, person.getObjectID());
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
    
    public boolean updatePerson(Person person) {
        if (person == null) {
            return false;
        }
        return true;
    }  //  end method updatePerson()

}
