//  PersonDAO.java
/**
 *
 * @author Alan Johnson
 */
package domain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.table.TableModel;
import net.proteanit.sql.DbUtils;
import util.DBConnectionPool;

public class PersonDAO {

    private DBConnectionPool connPool;

    public int countPeople() {
        return getPeople().size();
    }

    public void delete(Person person) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = connPool.getPoolConnection();
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
    }
    private static final String DELETE_STMT = "DELETE FROM alan.person "
            + "WHERE id = ?";
    
    public Person getPerson(int personID) {
        Person person = null;
        PreparedStatement request = null;
        Connection conn = null;

        try {
            conn = DBConnectionPool.getPoolConnection();
            request = conn.prepareStatement(GET_STMT);
            request.setInt(1, personID);

            ResultSet rset = request.executeQuery();

            if (rset.next()) {
                int id = rset.getInt("id");
                String firstName = rset.getString("first_name");
                String lastName = rset.getString("last_name");

                //  TODO:  Fix this:
                person = new Person(id, firstName, lastName, 0, 0, 0);
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
    private static final String GET_STMT = "SELECT * FROM alan.person "
            + "WHERE id = ?";

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
                Person person = new Person(id, firstName, lastName, 0, 0, 0);

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
    private static final String SEARCH_STMT = "SELECT * FROM alan.person "
            + "WHERE (UPPER(first_name) LIKE ?) "
            + "OR    (UPPER(last_name)  LIKE ?)";

    public ArrayList<Person> getPeople() {

        ArrayList<Person> personList = new ArrayList<>();
        Statement request = null;
        Connection conn = null;

        try {
            conn = DBConnectionPool.getPoolConnection();
            String requestString = "SELECT * FROM alan.person";
            request = conn.createStatement();
            ResultSet rset = null;

            rset = request.executeQuery(requestString);

            while (rset.next()) {
                int id = rset.getInt("id");
                String firstName = rset.getString("First_Name");
                String lastName = rset.getString("Last_Name");

                //  TODO:  Fix this:
                Person person = new Person(id, firstName, lastName, 0, 0, 0);

                personList.add(person);
            }

        } catch (SQLException se) {
            throw new RuntimeException(
                    "A database error occurred. " + se.getMessage());
        } catch (Exception e) {
            System.out.println("getPeople() ~ Exception: " + e.getMessage());
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

    
    
    private static final String GET_STATEMENT = "SELECT rank.name AS \"Rank\", "
            + "last_name AS \"Last Name\", first_name AS \"First Name\", "
            + "shift.name AS \"Shift\", skill.level AS \"Skill Lv\" "
            + "FROM alan.person, rank, workcenter, shift, skill "
            + "WHERE workcenter.id = ? "
            + "AND   person.workcenter_id = workcenter.id "
            + "AND   shift.id = ? "
            + "AND   person.shift_id = shift.id "
            + "AND   person.rank_id = rank.id "
            + "AND   person.skill_id = skill.id";
    public TableModel getPeople(Integer workcenter, Integer shift) {
        
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
            System.out.println("\nA database error occurred. " + se.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Exception: " + e.getMessage());
        } finally {
            
            // DEBUG:
            System.out.println("PersonDAO.getPeople() : finally block");
            
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
    
    private static final String INSERT_STMT = "INSERT INTO alan.person "
            + "VALUES (?, ?, ?, ?, ?, ?)";
    
    public void insert(Person person) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;

        //  DEBUG:
        System.out.println("\nInserting Person:\n" + person);
        
        
        try {
            
            //  DEBUG:
            //System.out.println("\nPersonDAO.insert() Checkpoint A");
            
            conn = connPool.getPoolConnection();
            
            //  DEBUG:
            //System.out.println("\nPersonDAO.insert() Checkpoint B");
            
            stmt = conn.prepareStatement(INSERT_STMT);
            ObjectIdDAO objIdDAO = new ObjectIdDAO();
            int personID = objIdDAO.getNextObjectID(ObjectIdDAO.PERSON);
            stmt.setInt(1, personID);
            stmt.setString(2, person.getFirstName());
            stmt.setString(3, person.getLastName());
            stmt.setInt(4, person.getRankID());
            stmt.setInt(5, person.getWorkcenterID());
            stmt.setInt(6, person.getShiftID());
            stmt.executeUpdate();
            person.setObjectID(personID);
            
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
     * TODO:  Need to add Rank, Skill, Work Center
     * 
     * @param fn
     * @param ln
     * @param ph
     * @return 
     */
    public boolean addPerson(String fn, String ln,
                             Integer rank, Integer workcenter, Integer shift) {
        //  TODO:  Fix this:
        Person person = new Person(-1, fn, ln, rank, workcenter, shift);

        if (person == null) {
            return false;
        }

        insert(person);

        return true;
    }

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
    private static final String UPDATE_STMT = "UPDATE alan.person "
            + "SET first_name = ?, last_name = ?, phone = ?"
            + "WHERE id = ?";

}
