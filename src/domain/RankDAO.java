//  RankDAO.java

/**
 * Changelog:
 * 2016-02-24 : Added getList() method
 * 2016-02-24 : Added getMap() method
 * 
 * 2016-02-27 : Added getMapReversed() method
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

public class RankDAO {

    private DBConnectionPool connPool;

    private static final String GET_STATEMENT = "SELECT * "
            + "FROM rank";
    public ObservableList<String> getList() {
        
        PreparedStatement request = null;
        Connection conn = null;

        try {
            conn = DBConnectionPool.getPoolConnection();
            request = conn.prepareStatement(GET_STATEMENT);

            ResultSet rset = request.executeQuery();
            ArrayList<String> rankList = new ArrayList<>();
            
            while (rset.next()) {
                rankList.add(rset.getString("name"));
            }
            
            return FXCollections.observableArrayList(rankList);

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
            HashMap<Integer, String> rankList = new HashMap<>();
            
            int number = 1;
            while (rset.next()) {
                rankList.put(number++, rset.getString("name"));
            }
            
            return rankList;

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
    
    
    public ComboBoxModel getComboModel() {
        
        PreparedStatement request = null;
        Connection conn = null;

        ArrayList<Person> personList = new ArrayList<>();
        try {
            conn = DBConnectionPool.getPoolConnection();
            request = conn.prepareStatement(GET_STATEMENT);

            ResultSet rset = request.executeQuery();
            ArrayList<String> rankList = new ArrayList<>();
            
            while (rset.next()) {
                rankList.add(rset.getString("name"));
            }
            
            String[] rankArray = (String[]) rankList.toArray(new String[0]);
            
            return new DefaultComboBoxModel(rankArray);

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
    
    

}
