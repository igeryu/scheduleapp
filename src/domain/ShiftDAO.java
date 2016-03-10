//  ShiftDAO.java

/**
 * Changelog:
 * Added getList() method
 */

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
import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.TableModel;
import net.proteanit.sql.DbUtils;
import util.DBConnectionPool;

public class ShiftDAO {

    private DBConnectionPool connPool;

    private static final String GET_STATEMENT = "SELECT * "
            + "FROM shift";
    public ObservableList<String> getList() {
        
        PreparedStatement request = null;
        Connection conn = null;

        try {
            conn = DBConnectionPool.getPoolConnection();
            request = conn.prepareStatement(GET_STATEMENT);

            ResultSet rset = request.executeQuery();
            ArrayList<String> shiftList = new ArrayList<>();
            
            while (rset.next()) {
                shiftList.add(rset.getString("name"));
            }
            
            return FXCollections.observableArrayList(shiftList);

        } catch (SQLException se) {
            System.out.println("\nA database error occurred. " + se.getMessage());
        } catch (Exception e) {
            System.out.println("\nException: " + e.getMessage());
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

        return null;
    }
    
    
    public ComboBoxModel getComboModel() {
        
        PreparedStatement request = null;
        Connection conn = null;

        ArrayList<Person> personList = new ArrayList<>();
        try {
            conn = DBConnectionPool.getPoolConnection();
            request = conn.prepareStatement(GET_STATEMENT);

            ResultSet rset = request.executeQuery();
            ArrayList<String> shiftList = new ArrayList<>();
            
            while (rset.next()) {
                shiftList.add(rset.getString("name"));
            }
            
            String[] shiftArray = (String[]) shiftList.toArray(new String[0]);
            
            return new DefaultComboBoxModel(shiftArray);

        } catch (SQLException se) {
            System.out.println("\nA database error occurred. " + se.getMessage());
        } catch (Exception e) {
            System.out.println("\nException: " + e.getMessage());
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

        return null;
    }
    
    public Map<Integer, String> getMap() {
        
        PreparedStatement request = null;
        Connection conn = null;

        try {
            conn = DBConnectionPool.getPoolConnection();
            request = conn.prepareStatement(GET_STATEMENT);

            ResultSet rset = request.executeQuery();
            HashMap<Integer, String> list = new HashMap<>();
            
            int number = 1;
            while (rset.next()) {
                list.put(number++, rset.getString("name"));
            }
            
            return list;

        } catch (SQLException se) {
            System.out.println("\nA database error occurred. " + se.getMessage());
        } catch (Exception e) {
            System.out.println("\nException: " + e.getMessage());
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

        return null;
    }
    
    public Map<String, Integer> getMapReversed() {
        Map<Integer, String> orderedMap = getMap();
        Map<String, Integer> reversedMap = new HashMap<>();
        
        for (Integer key : orderedMap.keySet()) {
            reversedMap.put((String)orderedMap.get(key), key);
        }
        
        return reversedMap;
    }

}
