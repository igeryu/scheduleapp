/**
 * Changelog:
 * 2016-02-20 : Got function to add people working.  Table displays existing people, based on current workcenter/shift selection.
 */
package forms;

import domain.PersonDAO;
import domain.RankDAO;
import domain.ShiftDAO;
import domain.SkillDAO;
import domain.WorkcenterDAO;
import java.util.Calendar;
import java.util.Date;
import javax.swing.ComboBoxModel;
import javax.swing.table.TableModel;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author alanjohnson
 */
public class AddPersonFrame extends javax.swing.JFrame {
    
//    private Integer workcenter = 1;
//    private Integer shift = 1;
//    private Integer rank = 1;
    
    
    private void updatePeopleTable(JTable table) {
        table.setModel(getMidsTableModel());
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        TableColumnModel columnModel = table.getColumnModel();
        
        columnModel.getColumn(0).setPreferredWidth(50);  //  Rank
        columnModel.getColumn(1).setPreferredWidth(150); //  First Name
        columnModel.getColumn(2).setPreferredWidth(150); //  Last Name
        columnModel.getColumn(3).setPreferredWidth(50); //  Shift
        columnModel.getColumn(4).setPreferredWidth(50); //  Skill   
    }
    
    
    
    private void updatePeopleTables() {
        TableColumnModel columnModel;
        
        ///////////////////  MIDS
        midsTable.setModel(getMidsTableModel());
        midsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        columnModel = midsTable.getColumnModel();
        
        columnModel.getColumn(0).setPreferredWidth(50);  //  Rank
        columnModel.getColumn(1).setPreferredWidth(150); //  First Name
        columnModel.getColumn(2).setPreferredWidth(150); //  Last Name
        columnModel.getColumn(3).setPreferredWidth(50); //  Shift
        columnModel.getColumn(4).setPreferredWidth(50); //  Skill
        
        
        ///////////////////  DAYS
        daysTable.setModel(getDaysTableModel());
        daysTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        columnModel = daysTable.getColumnModel();
        
        columnModel.getColumn(0).setPreferredWidth(50);  //  Rank
        columnModel.getColumn(1).setPreferredWidth(150); //  First Name
        columnModel.getColumn(2).setPreferredWidth(150); //  Last Name
        columnModel.getColumn(3).setPreferredWidth(50); //  Shift
        columnModel.getColumn(4).setPreferredWidth(50); //  Skill
        
        
        ///////////////////  SWINGS
        swingsTable.setModel(getSwingsTableModel());
        swingsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        columnModel = swingsTable.getColumnModel();
        
        columnModel.getColumn(0).setPreferredWidth(50);  //  Rank
        columnModel.getColumn(1).setPreferredWidth(150); //  First Name
        columnModel.getColumn(2).setPreferredWidth(150); //  Last Name
        columnModel.getColumn(3).setPreferredWidth(50); //  Shift
        columnModel.getColumn(4).setPreferredWidth(50); //  Skill
    }
    
    
    
    /**
     * Returns the mid-shift model for the table.
     * @return 
     */
    private TableModel getMidsTableModel () {
        PersonDAO personDao = new PersonDAO();
        Integer workcenter = workcenterBox.getSelectedIndex() + 1;
        Integer shift      = 1;
        TableModel tableModel = personDao.getPeople(workcenter, shift);
        
        
        return tableModel;
    }
    
    
    
    /**
     * Returns the day-shift model for the table.
     * @return 
     */
    private TableModel getDaysTableModel () {
        PersonDAO personDao = new PersonDAO();
        Integer workcenter = workcenterBox.getSelectedIndex() + 1;
        Integer shift      = 2;
        TableModel tableModel = personDao.getPeople(workcenter, shift);
        
        
        return tableModel;
    }
    
    
    /**
     * Returns the swing-shift model for the table.
     * @return 
     */
    private TableModel getSwingsTableModel () {
        PersonDAO personDao = new PersonDAO();
        Integer workcenter = workcenterBox.getSelectedIndex() + 1;
        Integer shift      = 3;
        TableModel tableModel = personDao.getPeople(workcenter, shift);
        
        
        return tableModel;
    }
    
    
    
    /**
     * 
     * @return 
     */
    private ComboBoxModel getRankBoxModel () {
        RankDAO rankDao = new RankDAO();
        
        return rankDao.getRanksBox();
    }
    
    
    
    /**
     * 
     * @return 
     */
    private ComboBoxModel getShiftBoxModel () {
        ShiftDAO shiftDao = new ShiftDAO();
        
        return shiftDao.getShifts();
    }
    
    
    
    /**
     * 
     * @return 
     */
    private ComboBoxModel getSkillBoxModel () {
        SkillDAO skillDao = new SkillDAO();
        
        return skillDao.getSkillsBox();
    }
    
    
    
    /**
     * 
     * @return 
     */
    private ComboBoxModel getWorkcenterBoxModel () {
        WorkcenterDAO workcenterDao = new WorkcenterDAO();
        
        return workcenterDao.getWorkcenters();
    }
    
    
    
    /**
     * Creates new form AddPersonFrame
     */
    public AddPersonFrame() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        titleLabel = new javax.swing.JLabel();
        workcenterBox = new javax.swing.JComboBox();
        workcenterLabel = new javax.swing.JLabel();
        shiftLabel = new javax.swing.JLabel();
        shiftBox = new javax.swing.JComboBox();
        startDateLabel = new javax.swing.JLabel();
        startDatePicker = new org.jdesktop.swingx.JXDatePicker();
        rankLabel = new javax.swing.JLabel();
        rankBox = new javax.swing.JComboBox();
        firstNameLabel = new javax.swing.JLabel();
        firstNameField = new javax.swing.JTextField();
        lastNameLabel = new javax.swing.JLabel();
        lastNameField = new javax.swing.JTextField();
        skillLabel = new javax.swing.JLabel();
        skillBox = new javax.swing.JComboBox();
        addButton = new javax.swing.JButton();
        midsLabel = new javax.swing.JLabel();
        midsScrollPane = new javax.swing.JScrollPane();
        midsTable = new javax.swing.JTable();
        daysLabel = new javax.swing.JLabel();
        daysScrollPane = new javax.swing.JScrollPane();
        daysTable = new javax.swing.JTable();
        swingsLabel = new javax.swing.JLabel();
        swingsScrollPane = new javax.swing.JScrollPane();
        swingsTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        titleLabel.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        titleLabel.setText("Add Person");

        workcenterBox.setModel(getWorkcenterBoxModel());
        workcenterBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                workcenterBoxActionPerformed(evt);
            }
        });

        workcenterLabel.setText("Workcenter");

        shiftLabel.setText("Shift");

        shiftBox.setModel(getShiftBoxModel());
        shiftBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                shiftBoxActionPerformed(evt);
            }
        });

        startDateLabel.setText("Start Date");

        Calendar calendar = startDatePicker.getMonthView().getCalendar();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -30);
        startDatePicker.getMonthView().setLowerBound(calendar.getTime());
        calendar.add(Calendar.DATE, +60);
        startDatePicker.getMonthView().setUpperBound(calendar.getTime());
        startDatePicker.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startDatePickerActionPerformed(evt);
            }
        });

        rankLabel.setText("Rank");

        rankBox.setModel(getRankBoxModel());
        rankBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rankBoxActionPerformed(evt);
            }
        });

        firstNameLabel.setText("First Name");

        firstNameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                firstNameFieldActionPerformed(evt);
            }
        });

        lastNameLabel.setText("Last Name");

        lastNameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lastNameFieldActionPerformed(evt);
            }
        });

        skillLabel.setText("Skill");

        skillBox.setModel(getSkillBoxModel());
        skillBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                skillBoxActionPerformed(evt);
            }
        });

        addButton.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        addButton.setText("Add");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        midsLabel.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        midsLabel.setText("Mids");

        midsTable.setModel(getMidsTableModel());
        midsScrollPane.setViewportView(midsTable);

        daysLabel.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        daysLabel.setText("Days");

        daysTable.setModel(getDaysTableModel());
        daysScrollPane.setViewportView(daysTable);

        swingsLabel.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        swingsLabel.setText("Swings");

        swingsTable.setModel(getSwingsTableModel());
        updatePeopleTables();
        swingsScrollPane.setViewportView(swingsTable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(21, 21, 21)
                                        .addComponent(workcenterLabel))
                                    .addComponent(workcenterBox, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(33, 33, 33)
                                        .addComponent(shiftLabel))
                                    .addComponent(shiftBox, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(38, 38, 38)
                                        .addComponent(rankLabel)
                                        .addGap(51, 51, 51))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(rankBox, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(titleLabel)
                                .addGap(51, 51, 51)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(startDatePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(startDateLabel)
                                .addGap(53, 53, 53)))
                        .addContainerGap(21, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(38, 38, 38)
                                .addComponent(firstNameLabel)
                                .addGap(86, 86, 86)
                                .addComponent(lastNameLabel))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(firstNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lastNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(33, 33, 33)
                                .addComponent(skillLabel))
                            .addComponent(skillBox, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(150, Short.MAX_VALUE))))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(230, 230, 230)
                        .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(daysScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(217, 217, 217)
                                .addComponent(daysLabel))
                            .addComponent(midsScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(213, 213, 213)
                                .addComponent(midsLabel))
                            .addComponent(swingsScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(217, 217, 217)
                                .addComponent(swingsLabel)))))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(titleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(startDateLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(startDatePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(rankLabel)
                        .addGap(33, 33, 33))
                    .addComponent(rankBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(workcenterLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(workcenterBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(shiftLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(shiftBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(firstNameLabel)
                            .addComponent(lastNameLabel))
                        .addGap(3, 3, 3)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(firstNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lastNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(skillLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(skillBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(midsLabel)
                .addGap(4, 4, 4)
                .addComponent(midsScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(daysLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(daysScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(swingsLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(swingsScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void firstNameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_firstNameFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_firstNameFieldActionPerformed

    private void lastNameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lastNameFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lastNameFieldActionPerformed

    private void startDatePickerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startDatePickerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_startDatePickerActionPerformed

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        String firstName = firstNameField.getText();
        String lastName  = lastNameField.getText();
        if (firstName.equals("") || lastName.equals("")) {
            return;
        }
        
        PersonDAO personDao = new PersonDAO();
        
        Integer workcenterID = workcenterBox.getSelectedIndex() + 1;
        Integer shiftID      = shiftBox.getSelectedIndex() + 1;
        Integer rankID       = rankBox.getSelectedIndex() + 1;
        Integer skillID      = skillBox.getSelectedIndex() + 1;
        
        //  DEBUG:
        System.out.printf("\nworkcenterID = %s", workcenterID);
        System.out.printf("\nshiftID = %s", shiftID);
        System.out.printf("\nrankID = %s\n", rankID);
        
        if (personDao.addPerson(firstName, lastName,
                                rankID, workcenterID,
                                shiftID, skillID)) {
            firstNameField.setText("");
            lastNameField.setText("");
            updatePeopleTables();
        }
        
        
    }//GEN-LAST:event_addButtonActionPerformed

    private void workcenterBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_workcenterBoxActionPerformed
        // TODO add your handling code here:
        updatePeopleTables();
    }//GEN-LAST:event_workcenterBoxActionPerformed

    private void shiftBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shiftBoxActionPerformed
        // TODO add your handling code here:
        updatePeopleTables();
    }//GEN-LAST:event_shiftBoxActionPerformed

    private void rankBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rankBoxActionPerformed
        // TODO add your handling code here:
        updatePeopleTables();
    }//GEN-LAST:event_rankBoxActionPerformed

    private void skillBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_skillBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_skillBoxActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AddPersonFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddPersonFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddPersonFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddPersonFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AddPersonFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JLabel daysLabel;
    private javax.swing.JScrollPane daysScrollPane;
    private javax.swing.JTable daysTable;
    private javax.swing.JTextField firstNameField;
    private javax.swing.JLabel firstNameLabel;
    private javax.swing.JTextField lastNameField;
    private javax.swing.JLabel lastNameLabel;
    private javax.swing.JLabel midsLabel;
    private javax.swing.JScrollPane midsScrollPane;
    private javax.swing.JTable midsTable;
    private javax.swing.JComboBox rankBox;
    private javax.swing.JLabel rankLabel;
    private javax.swing.JComboBox shiftBox;
    private javax.swing.JLabel shiftLabel;
    private javax.swing.JComboBox skillBox;
    private javax.swing.JLabel skillLabel;
    private javax.swing.JLabel startDateLabel;
    private org.jdesktop.swingx.JXDatePicker startDatePicker;
    private javax.swing.JLabel swingsLabel;
    private javax.swing.JScrollPane swingsScrollPane;
    private javax.swing.JTable swingsTable;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JComboBox workcenterBox;
    private javax.swing.JLabel workcenterLabel;
    // End of variables declaration//GEN-END:variables
}
