/**
 * Changelog:
 * 2016-02-23 : Built basic layout from top down to drop-down menus.  Need to fix ordering of labels
 * 
 * 2016-02-24 : Fixed ordering of input area first row.
 * 2016-02-24 : Added rest of components and fixed spacing/alignment
 * 2016-02-24 : Added updateAllTables() and updateTable(Shift) methods
 * 2016-02-24 : Got three-shift view working
 * 2016-02-24 : Got rank/shift/skill/workcenter choiceboxes populated
 * 
 * 2016-02-25 : Got workcenter choicebox to change what is shown in the tables
 * 2016-02-25 : Implemented 'add person' functionality, it verifies that all data has been entered (except date), and only updates the affected shift table
 * 2016-02-25 : Added Skill Level column to people tables, moved Rank column to far left
 */

package forms;

import domain.Person;
import domain.PersonDAO_New;
import domain.RankDAO;
import domain.ShiftDAO;
import domain.SkillDAO;
import domain.WorkcenterDAO;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author alanjohnson
 */
public class AddPersonStage {
    
    // ===========================  Inputs  ==========================
    private static TableView swingsTable;
    private static TableView daysTable;
    private static TableView midsTable;
    private static ChoiceBox skillBox;
    private static TextField lastNameField;
    private static TextField firstNameField;
    private static DatePicker startDateBox;
    private static ChoiceBox rankBox;
    private static ChoiceBox shiftBox;
    private static ChoiceBox workcenterBox;
    
    // ============================  Misc  ===========================
    private enum Shift { MIDS, DAYS, SWINGS};
    
    public static void display () {
        Stage window = new Stage();
        window.setTitle("Add Person");
        Pane rootLayout = new VBox(20);
        Scene scene = new Scene(rootLayout, 600, 750);
        window.setScene(scene);
        
        // ===========================  Title Label  ==========================
        Pane titlePane = new StackPane();
        titlePane.getChildren().add(new Label("Add Person"));
        rootLayout.getChildren().add(titlePane);
        
        // ===========================  Input Area  ===========================
        GridPane inputPane = new GridPane();
        inputPane.setAlignment(Pos.CENTER);
        inputPane.setHgap(60);
        inputPane.setVgap(10);
        inputPane.setPadding(new Insets(0, 20, 0, 20));
        rootLayout.getChildren().add(inputPane);
        
        //      ========================  Labels  ========================
        
        //           ==================  1st  Row  ==================
        Label workcenterLabel = new Label("Workcenter");
        GridPane.setConstraints(workcenterLabel, 0, 0);
        
        Label shiftLabel = new Label("Shift");
        GridPane.setConstraints(shiftLabel, 1, 0);
        
        Label rankLabel = new Label("Rank");
        GridPane.setConstraints(rankLabel, 2, 0);
        
        Label startDateLabel = new Label("Start Date");
        GridPane.setConstraints(startDateLabel, 3, 0);
        
        //           ==================  2nd  Row  ==================
        Label firstNameLabel = new Label("First Name");
        GridPane.setConstraints(firstNameLabel, 0, 2);
        
        Label lastNameLabel = new Label("Last Name");
        GridPane.setConstraints(lastNameLabel, 1, 2);
        
        Label skillLabel = new Label("Skill Level");
        GridPane.setConstraints(skillLabel, 2, 2);
        
        inputPane.getChildren().addAll(workcenterLabel, shiftLabel,
                                        rankLabel, startDateLabel,
                                        firstNameLabel, lastNameLabel,
                                        skillLabel);
        
        //      ========================  Inputs  ========================
        
        //           ==================  1st  Row  ==================
        workcenterBox = new ChoiceBox((new WorkcenterDAO()).getWorkcentersList());
        workcenterBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue ov, Number oldValue, Number newValue) {
                updateAllTables();
            }
        });
        GridPane.setConstraints(workcenterBox, 0, 1);
        
        shiftBox = new ChoiceBox((new ShiftDAO()).getShiftsList());
        GridPane.setConstraints(shiftBox, 1, 1);
        
        rankBox = new ChoiceBox((new RankDAO()).getRanksList());
        GridPane.setConstraints(rankBox, 2, 1);
        
        startDateBox = new DatePicker();
        GridPane.setConstraints(startDateBox, 3, 1);
        
        //           ==================  2nd  Row  ==================
        firstNameField = new TextField();
        GridPane.setConstraints(firstNameField, 0, 3);
        
        lastNameField = new TextField();
        GridPane.setConstraints(lastNameField, 1, 3);
        
        skillBox = new ChoiceBox((new SkillDAO()).getSkillsList());
        GridPane.setConstraints(skillBox, 2, 3);
        
        //           ==================  3rd  Row  ==================
        Button addButton = new Button("Add");
        addButton.setOnAction(e -> {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            Integer workcenterID = workcenterBox.getSelectionModel().getSelectedIndex() + 1;
            Integer shiftID = shiftBox.getSelectionModel().getSelectedIndex() + 1;
            Integer rankID = rankBox.getSelectionModel().getSelectedIndex() + 1;
            Integer skillID = skillBox.getSelectionModel().getSelectedIndex() + 1;
            
            //  TODO:  Acquire start date data and use it as part of input requirements
            
            if (firstName.equals("") || lastName.equals("")
             || workcenterID < 1 || shiftID < 1 || rankID < 1 || skillID < 1) {
                // DEBUG:
                System.out.println("\n[AddPersonStage.display()] not enough data to add person...\n");
                
                return;
            }

            PersonDAO_New personDao = new PersonDAO_New();
            
            //  DEBUG:
            System.out.printf("\nworkcenterID = %s", workcenterID);
            System.out.printf("\nshiftID = %s", shiftID);
            System.out.printf("\nrankID = %s\n", rankID);

            if (personDao.addPerson(firstName, lastName,
                    rankID, workcenterID,
                    shiftID, skillID)) {
                firstNameField.setText("");
                lastNameField.setText("");
                
                //  TODO: Make this line update only the shift affected
                Shift shiftAffected;
                switch (shiftID) {
                    case 1:
                        updateTable(Shift.MIDS);
                        break;

                    case 2:
                        updateTable(Shift.DAYS);
                        break;

                    case 3:
                        updateTable(Shift.SWINGS);
                        break;
                }
            }
        });
        addButton.setAlignment(Pos.CENTER);
        StackPane addButtonPane = new StackPane();
        GridPane.setConstraints(addButtonPane, 0, 4, 4, 1);
        addButtonPane.getChildren().add(addButton);
        
        inputPane.getChildren().addAll(workcenterBox, shiftBox,
                                       rankBox, startDateBox,
                                       firstNameField, lastNameField,
                                       skillBox, addButtonPane);
        
        // ===========================  Table Area  ===========================
        VBox tableBox = new VBox(10);
        tableBox.setAlignment(Pos.CENTER);
        tableBox.setPadding(new Insets(10, 30, 30, 30));
        rootLayout.getChildren().add(tableBox);
        
        Label midsLabel = new Label("Mids");
        midsTable = new TableView();
        
        Label daysLabel = new Label("Days");
        daysTable = new TableView();
        
        Label swingsLabel = new Label("Swings");
        swingsTable = new TableView();
        
        tableBox.getChildren().addAll(midsLabel,   midsTable,
                                      daysLabel,   daysTable,
                                      swingsLabel, swingsTable);
        
        // =============================  Finish  =============================
        updateAllTables();
        window.initModality(Modality.APPLICATION_MODAL);
        window.showAndWait();
    
    }
    
    static private void updateAllTables () {
        updateTable(Shift.MIDS);
        updateTable(Shift.DAYS);
        updateTable(Shift.SWINGS);
    }
    
    static private void updateTable(Shift shift) {
        
        //  1. Build model/data
        //  2. Turn off auto resize (if applicable/necessary)
        //  3. Set column widths
        
        //  Create Columns
        TableColumn rankColumn = new TableColumn("Rank");
        TableColumn firstNameColumn = new TableColumn("First Name");
        TableColumn lastNameColumn = new TableColumn("Last Name");
        TableColumn skillColumn = new TableColumn("Skill Lv");
        
        //  Bind Person properties to table columns
        rankColumn.setCellValueFactory(
                new PropertyValueFactory<Person, String>("rank")
        );
        firstNameColumn.setCellValueFactory(
                new PropertyValueFactory<Person, String>("firstName")
        );
        lastNameColumn.setCellValueFactory(
                new PropertyValueFactory<Person, String>("lastName")
        );
        skillColumn.setCellValueFactory(
                new PropertyValueFactory<Person, String>("skill")
        );

        ObservableList<Person> data = null;
        TableView table = null;
        PersonDAO_New personDao = new PersonDAO_New();
        Integer workcenterID = workcenterBox.getSelectionModel().getSelectedIndex() + 1;
        
        //  DEBUG:
        System.out.println("\nSelected Workcenter: " + workcenterID);
        
        switch (shift) {
            case MIDS:
                table = midsTable;
                data = personDao.getPeopleByShift(1, workcenterID);
                break;
                
            case DAYS:
                table = daysTable;
                data = personDao.getPeopleByShift(2, workcenterID);
                break;
                
            case SWINGS:
                table = swingsTable;
                data = personDao.getPeopleByShift(3, workcenterID);
                break;
        }
        
        table.getColumns().clear();
        table.getColumns().addAll(rankColumn, firstNameColumn, lastNameColumn, skillColumn);
        table.setItems(data);
    }
    
}
