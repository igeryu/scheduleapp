// EditPersonStage.java

/**
 * Changelog:
 * 2016-02-25 : Created class using AddPersonStage as template
 * 2016-02-25 : Added confirmChangeItem(Item) and confirmChangeItem(Item, ObservableList<String>)
 * 
 * 2016-02-27 : Added functionality for changing: workcenter, shift, rank, last name and skill level
 * 2016-02-27 : Added deleteButton and implemented functionality
 * 
 * 2016-02-28 : Attempting to save a change to workcenter but with no start date chosen will result in an error message
 * 2016-02-28 : Saving a change to workcenter WITH a start date will successfully change the workcenter and add a new start date
 * 
 * 2016-02-29 : Extracted action handler code from deleteButton to a deletePerson() method  (Also added extra line break to confirmation message)
 * 2016-02-29 : Refactoring: Removed unused imports, removed unuse switch-statement in setupButton()
 * 2016-02-29 : Added Javadoc for: both confirmChangeItem() methods, deletePerson(), display(), saveChanges(), setupButton()
 * 2016-02-29 : Adjusted popup window title for both confirmChangeItem() methods
 * 2016-02-29 : Added Person parameter to display() method
 * 2016-02-29 : Reordered method declarations so that they are in alphabetic order
 * 
 * 2016-03-01 : Removed code block from addButton's setOnAction() lambda expression
 * 2016-03-01 : Added cancelButton
 * 2016-03-01 : Changed saveCancelButton to an HBox
 */

package window;

import window.modal.AnswerBox;
import window.modal.AlertBox;
import domain.Person;
import domain.PersonDAO;
import domain.RankDAO;
import domain.ShiftDAO;
import domain.ShiftDateDAO;
import domain.SkillDAO;
import domain.WorkcenterDAO;
import java.sql.Date;
import java.time.LocalDate;
import window.modal.AnswerBox.Orientation;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author alanjohnson
 */
public class EditPersonStage {
    
    // ===========================  Inputs  ==========================
    private static Button lastNameButton;
    private static Button firstNameButton;
    
    private static DatePicker startDateBox;
    private static Button skillButton;
    private static Button rankButton;
    private static Button shiftButton;
    private static Button workcenterButton;
    
    // ============================  Misc  ===========================
    private static Stage window;
    private static Person person;
    private static boolean shiftChanged = false;
    private enum Shift { MIDS, DAYS, SWINGS};
    private enum Item {
        FIRST_NAME, LAST_NAME, RANK, SHIFT, SKILL, WORKCENTER;
        
        @Override
        public String toString() {
            switch (this) {
                case FIRST_NAME: return "first name";
                case LAST_NAME: return "last name";
                case RANK: return "rank";
                case SHIFT: return "shift";
                case SKILL: return "skill level";
                case WORKCENTER: return "workcenter";
                default: throw new IllegalArgumentException();
            }
        }
    };
    
    
    // ===========================  Methods  =========================
    /**
     * <p>Calls an <code>AnswerBox</code> instance to retrieve user's new value
     * for the given person record item.  The item to be modified is indicated
     * by the <code>Item</code> parameter.</p>
     * 
     * @param item     type of item being modified
     * 
     * @return         new value for the item being modified
     */
    private static String confirmChangeItem (Item item) {
        String title = String.format("Change %s?", item);
        
        String message = String.format("Change %s %s %s's %s to?",
                                     person.getRank(),
                                     person.getFirstName(),
                                     person.getLastName(),
                                     item);
        
        String newValue = (new AnswerBox()).display(title, message);
        
        // DEBUG:
//        System.out.printf("\n[EditPersonStage.confirmChangeItem()] answer: %s\n", newValue);
        
        return newValue;
    }  //  end method confirmChangeItem(Item)
    
    /**
     * <p>Calls an <code>AnswerBox</code> instance to retrieve user's new value
     * for the given person record item.  The item to be modified is indicated
     * by the <code>Item</code> parameter.</p>
     * 
     * <p>The list of available options is given to the <code>AnswerBox</code>
     * instance via the <code>ObservableList<String></code> parameter</p>
     * 
     * @param item            type of item being modified
     * @param options         option choices supplied to the
     *                        <code>AnswerBox</code> instance
     * @param orientation     preferred orientation of pop-up input window
     * @return                new value for the item being modified
     */
    private static String confirmChangeItem (Item item, ObservableList<String> options, Orientation orientation) {
        String title = String.format("Change %s?", item);
        
        String message = String.format("Change %s %s %s's %s to?",
                                     person.getRank(),
                                     person.getFirstName(),
                                     person.getLastName(),
                                     item);
        
        String newValue = (new AnswerBox()).display(title, message, options, orientation);
        
        // DEBUG:
//        System.out.printf("\n[EditPersonStage.confirmChangeItem()] answer: %s\n", newValue);
        
        return newValue;
    }
    
    /**
     * <p>Deletes the person represented by the <code>person</code> static field
     * from the database.</p>
     */
    private static void deletePerson() {
        String title = "Delete";
        
        String message = String.format("Delete %s %s %s?", person.getRank(),
                person.getFirstName(),
                person.getLastName());
        
        message += "\n\nPlease type 'delete' in the box to confirm, and "
                + "click OK.";
        
        String answer = (new AnswerBox()).display(title, message);
        
        if (answer == null) {
            return;
        } else if(answer.equals("delete")) {
            (new PersonDAO()).delete(person);
            
            title = "Person deleted";
            message = String.format("%s %s %s was deleted.",
                    person.getRank(),
                    person.getFirstName(),
                    person.getLastName());
            
            AlertBox.display(title, message);
            window.close();
        } else {
            title = "Deletion unsuccessful";
            message = String.format("%s %s %s was not deleted.",
                    person.getRank(),
                    person.getFirstName(),
                    person.getLastName());
            
            AlertBox.display(title, message);
        }
    }  //  end deletePerson() method
    
    /**
     * <p>Displays the 'Edit Person' stage (window) using the given
     * <code>Person</code> parameter.</p>
     */
    public static void display (Person inpuPerson) {
        //  DEBUG/TESTING:
        person = new Person("First_Name", "Last_Name", "SSgt", "5");
        
        window = new Stage();
        //  TODO: [maybe] Change this line to have the person's name:
        window.setTitle("Edit Person");
        Pane rootLayout = new VBox(20);
        int width = 650;
        int height = 300;
        Scene scene = new Scene(rootLayout, width, height);
        window.setScene(scene);
        
        window.setMinWidth(width);
        window.setMinHeight(height);
        window.setMaxWidth(width);
        window.setMaxHeight(height);
        
        // ===========================  Title Label  ==========================
        Pane titlePane = new StackPane();
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
        
        Label skillLabel = new Label("Skill Lv");
        GridPane.setConstraints(skillLabel, 2, 2);
        
        inputPane.getChildren().addAll(workcenterLabel, shiftLabel,
                                        rankLabel, startDateLabel,
                                        firstNameLabel, lastNameLabel,
                                        skillLabel);
        
        //      ========================  Inputs  ========================
        
        //           ==================  1st  Row  ==================
        workcenterButton = new Button(person.getWorkcenter());
        workcenterButton.setMinWidth(100);
        setupButton(workcenterButton, Item.WORKCENTER, Orientation.PORTRAIT);
        GridPane.setConstraints(workcenterButton, 0, 1);
        
        shiftButton = new Button(person.getShift());
        shiftButton.setMinWidth(50);
        setupButton(shiftButton, Item.SHIFT, Orientation.PORTRAIT);
        GridPane.setConstraints(shiftButton, 1, 1);
        
        rankButton = new Button(person.getRank());
        rankButton.setMinWidth(20);
        setupButton(rankButton, Item.RANK, Orientation.PORTRAIT);
        GridPane.setConstraints(rankButton, 2, 1);
        
        startDateBox = new DatePicker();
        GridPane.setConstraints(startDateBox, 3, 1);
        
        //           ==================  2nd  Row  ==================
        firstNameButton = new Button(person.getFirstName());
        firstNameButton.setOnAction(e -> {
            String newValue = confirmChangeItem(Item.FIRST_NAME);
            
            if (newValue != null && !newValue.equals("")) {
                person.setFirstName(newValue);
                firstNameButton.setText(newValue);
            }
                });
        GridPane.setConstraints(firstNameButton, 0, 3);
        
        lastNameButton = new Button(person.getLastName());
        lastNameButton.setOnAction(e -> {
            String newValue = confirmChangeItem(Item.LAST_NAME);
            
            if (newValue != null && !newValue.equals("")) {
                person.setLastName(newValue);
                lastNameButton.setText(newValue);
            }
                });
        GridPane.setConstraints(lastNameButton, 1, 3);
        
        skillButton = new Button(person.getSkill());
        skillButton.setMinWidth(10);
        setupButton(skillButton, Item.SKILL, Orientation.PORTRAIT);
        GridPane.setConstraints(skillButton, 2, 3);
        
        //           ==================  3rd  Row  ==================
        HBox saveCancelPane = new HBox(20);
        saveCancelPane.setAlignment(Pos.CENTER);
        
        //                ===========  Save  Button  ===========
        Button saveButton = new Button("Save");
        //  TODO:  Fix functionality for saveButton
        saveButton.setOnAction(e -> saveChanges() );
        GridPane.setConstraints(saveCancelPane, 0, 4, 4, 1);
        saveCancelPane.setPadding(new Insets(20, 0, 0, 0));
        saveCancelPane.getChildren().add(saveButton);
        
        //                ===========  Cancel Button  ==========
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> window.close());
        GridPane.setConstraints(saveCancelPane, 0, 4, 4, 1);
        saveCancelPane.getChildren().add(cancelButton);
        
        //                ==========  Delete  Button  ==========
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> deletePerson() );
        deleteButton.setAlignment(Pos.CENTER);
        StackPane deleteButtonPane = new StackPane();
        GridPane.setConstraints(deleteButtonPane, 0, 5, 4, 1);
        deleteButtonPane.setPadding(new Insets(20, 0, 0, 0));
        deleteButtonPane.getChildren().add(deleteButton);
        
        inputPane.getChildren().addAll(workcenterButton, shiftButton,
                                       rankButton, startDateBox,
                                       firstNameButton, lastNameButton,
                                       skillButton, saveCancelPane,
                                       deleteButtonPane);
        
        // =============================  Finish  =============================
        window.initModality(Modality.APPLICATION_MODAL);
        window.showAndWait();
    
    }  //  end method display(Person)
    
    /**
     * <p>After verifying data inputs, this method saves the changes made to the
     * person by calling the <code>PersonDAO.updatePerson()</code> and
     * <code>ShiftDateDAO.addStartDate()</code> methods.</p>
     */
    private static void saveChanges() {
        
        // ====================  Check for  Shift Changes  ====================
        if (shiftChanged && startDateBox.getValue() == null) {
            String title = "New shift start date";
            String message = "Please select a start date.";
            (new AlertBox()).display(title, message);
            return;
        }
        
        // ===========================  Get Inputs  ===========================
        String    firstName      =  firstNameButton.getText();
        String    lastName       =   lastNameButton.getText();
        String    rank           =       rankButton.getText();
        String    workcenter     = workcenterButton.getText();
        String    shift          =      shiftButton.getText();
        String    skill          =      skillButton.getText();
        LocalDate startLocalDate =     startDateBox.getValue();
        
        // =====================  Make Changes to Person  =====================
        person.setAll(firstName, lastName, rank, workcenter, shift,  skill);
        (new PersonDAO()).updatePerson(person);
        if (shiftChanged) {
            (new ShiftDateDAO()).addStartDate(person, Date.valueOf(startLocalDate));
//            shiftChanged = false;  //  This is only needed if the window remains open
        }
        //  TODO:  Should the Edit Person window stay open after a successful save?
        window.close();
    }  // end method saveChanges()
    
    /**
     * <p>Sets up the given button according to a template and based on the
     * given <code>Item</code> and <code>Orientation</code> parameters</p>
     * 
     * @param button          button being setup
     * @param item            type of item being retrieved from the user
     * @param orientation     preferred orientation of modal dialog window
     */
    private static void setupButton(Button button, Item item, Orientation orientation) {
        button.setOnAction(e -> {
            ObservableList<String> list = null;
            
            switch(item) {
                case RANK:
                    list = (new RankDAO()).getList();
                    break;
                
                case SHIFT:
                    list = (new ShiftDAO()).getList();
                    shiftChanged = true;
                    break;
                
                case SKILL:
                    list = (new SkillDAO()).getList();
                    break;
                
                case WORKCENTER:
                    list = (new WorkcenterDAO()).getList();
                    break;
                
                default: throw new IllegalArgumentException();
            }
            
            String newValue = confirmChangeItem(item, list, orientation);
            
            if (newValue != null && !newValue.equals("")) {
                button.setText(newValue);
            } else {
                String title = "Error";
                String message = String.format("No %s was selected", item);
                (new AlertBox()).display(title, message);
            }
                });
    }  //  end method setupButton()
    
}
