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
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.TableModel;
import net.proteanit.sql.DbUtils;
import util.DBConnectionPool;

public class RankDAO {

    private DBConnectionPool connPool;

    private static final String GET_STATEMENT = "SELECT * "
            + "FROM rank";
    public ComboBoxModel getRanks() {
        
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
