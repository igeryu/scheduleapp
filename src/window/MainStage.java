// MainStage.java

/**
 * Changelog:
 * 
 * 2016-03-07 : Created file
 * 2016-03-07 : Added a tabbed pane with 'Schedule' and 'Manage' tabs
 * 
 * 2016-03-09 : Schedule Shift View works, shows each person (rows) with their current shift today and next six days.
 * 2016-03-09 : Made shift cells color-coded for easier viewing.
 * 
 * 2016-03-13 : Changed rootLayout to a BorderPane (from StackPane)
 * 2016-03-13 : Grouped class variables by type/function
 * 2016-03-13 : Extracted code to build scheduleTab, manageTab, and filtersBox from display() to buildScheduleTab(), buildManageTab() and buildFiltersBox()
 * 2016-03-13 : Changed name of rebuildTable() to populateShiftViewTable()
 * 2016-03-13 : Changed name of scheduleTable to outputTable
 * 
 * 2016-03-23 : Removed unused imports and grouped remaining imports by root package
 * 2016-03-23 : Removed the private class PersonRow
 * 2016-03-23 : Formatted to match Google Java Style (up to 3.4.1)
 * 
 * 2016-03-24 : Fixed Javadoc and formatting for buildShiftViewTable()
 * 2016-03-24 : Replaced debug System.out calls with Logger calls
 * 2016-03-24 : Grouped and ordered methods into logical groupings
 * 2016-03-24 : Improved the Javadoc for createColumn()
 * 2016-03-24 : Formatted to match Google Java Style
 */
package window;

import domain.Person;
import domain.PersonDAO;
import domain.ShiftDAO;
import domain.ShiftDateDAO;
import domain.WorkcenterDAO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 *
 * @author alanjohnson
 */
public class MainStage {
   
  // ===========================  Inputs  ==========================
  private static Stage window;
  private static VBox fileWindow;
  private static Tab fileTab;
  private static int workcenter = -1;
  private static int shift = -1;

  // ===========================   Misc  ===========================
  private static Logger logger = Logger.getLogger(MainStage.class.getName());
  private static TabPane tabPane;
  private static HBox filtersBox;
  private static TableView outputTable;

  // =========================   Schedule   ========================
  private static Tab scheduleTab;
  private static VBox scheduleWindow;
  private static HBox scheduleOptionsBox;

  // ==========================   Manage   =========================
  private static Tab manageTab;
  private static VBox manageWindow;
  private static HBox manageOptionsBox;

  /**
   * <p>
   * Adapted from <code>https://community.oracle.com/thread/2474328</code></p>
   *
   * @param columnIndex     Indicates which date this column is for (1st, 2nd, etc.)
   * @param columnTitle     Indicates the title of the column being built
   * @return                Factory-made <code>TableColumn</code> built from the inputs
   */
  private TableColumn<ObservableList<StringProperty>, String> createColumn(
          final int columnIndex, String columnTitle) {
    TableColumn<ObservableList<StringProperty>, String> column =
        new TableColumn<>();
    String title = columnTitle;

    column.setText(title);
    column.setCellValueFactory((
        CellDataFeatures<ObservableList<StringProperty>, String>
            cellDataFeatures) -> cellDataFeatures.getValue().get(columnIndex));
    column.setCellFactory(thisColumn -> {
      return new TableCell<ObservableList<StringProperty>, String>() {
        @Override
        protected void updateItem(String item, boolean empty) {
          super.updateItem(item, empty);

          if (item == null || empty) {
            setText(null);
            setStyle("");
          } else {
            setText(item + " Shift");

            switch (item) {
              case "Mid":
                this.setId("mid_style");
                break;

              case "Day":
                this.setId("day_style");
                break;

              case "Swing":
                this.setId("swing_style");
                break;
            }
          }
        }
      };
    });
    return column;
  }

  /**
   * <p>
   * Displays the main stage(window).</p>
   */
  public void display() {
    window = new Stage();
    window.setTitle("Schedule Application");
    BorderPane rootLayout = new BorderPane();
    int width = 650;
    int height = 300;
    Scene scene = new Scene(rootLayout, width, height);
    height = 0;
    scene.getStylesheets().add("css/mainStage.css");
    window.setScene(scene);

    window.setMinWidth(width);
    window.setMinHeight(height);
    buildShiftViewTable();

    // ===========================   Tab Area  ============================
    fileTab = new Tab("File");
    fileTab.setClosable(false);
    fileTab.getStyleClass().add("file");
    fileWindow = new VBox();
    fileTab.setContent(fileWindow);

    //      =====================  Schedule Tab  =====================
    buildScheduleTab();

    //      ======================  Manage Tab  ======================
    buildManageTab();

    // ==========================  Filters Row   ==========================
    buildFiltersBox();

    //      ====================  Finalize Tabs   ====================
    tabPane = new TabPane();
    tabPane.getTabs().addAll(fileTab, scheduleTab, manageTab);
    tabPane.getSelectionModel().select(scheduleTab);
    rootLayout.setTop(tabPane);
    VBox displayBox = new VBox();
    displayBox.getChildren().addAll(filtersBox, outputTable);
    rootLayout.setCenter(displayBox);

        // =============================  Finish   ============================
    //  TODO: Change modality once this is actually the main window:
    window.initModality(Modality.APPLICATION_MODAL);
    populateShiftViewTable();
    resize();
//        window.setMaximized(true);
    window.showAndWait();

  }  //  end method display(Person)
  
  // =============================   Tabs  ==============================
  private void buildManageTab() {
    manageWindow = new VBox();

    manageTab = new Tab("Manage");
    manageTab.setClosable(false);
    manageTab.getStyleClass().add("manage");
    manageTab.setContent(manageWindow);

    //           ================  Options Row   ================
    manageOptionsBox = new HBox(10);

    Button addPersonButton = new Button("Add Person");
    addPersonButton.setOnAction(e -> AddPersonStage.display());

    Button editPersonButton = new Button("Edit Person");
    editPersonButton.setOnAction(e -> EditPersonStage.display(null));

    manageOptionsBox.getChildren().addAll(addPersonButton, editPersonButton);
    manageOptionsBox.getStyleClass().addAll("options");

    //           ===========  Finalize Schedule Tab   ===========
    manageWindow.getChildren().addAll(manageOptionsBox);
  }

  private void buildScheduleTab() {
    scheduleWindow = new VBox();

    scheduleTab = new Tab("Schedule");
    scheduleTab.setClosable(false);
    scheduleTab.getStyleClass().add("schedule");
    scheduleTab.setContent(scheduleWindow);

    //      =====================  Options Row   =====================
    scheduleOptionsBox = new HBox(10);
    scheduleOptionsBox.getChildren().addAll(new Label("Options..."));
    scheduleOptionsBox.getStyleClass().addAll("options");

    //           ===========  Finalize Schedule Tab   ===========
    scheduleWindow.getChildren().addAll(scheduleOptionsBox);

  }
  
  // ===========================   Controls  ============================
  private void buildFiltersBox() {
    filtersBox = new HBox();
    double scaleFactor = .8;
//        filtersBox.getChildren().add(new Label("Filters..."));

    // ========================  Workcenters   =======================
    filtersBox.getChildren().add(new Label("Workcenters:"));
    ObservableList<String> workcenterList = (new WorkcenterDAO()).getList();
    ToggleGroup workcenterGroup = new ToggleGroup();
    workcenterGroup.selectedToggleProperty().addListener(
        (ov, oldToggle, newToggle) -> {
          workcenter = workcenterGroup.getToggles().indexOf(newToggle);
          populateShiftViewTable();
        });

    RadioButton newButton = new RadioButton("All");
    newButton.setToggleGroup(workcenterGroup);
    filtersBox.getChildren().add(newButton);
    workcenterGroup.selectToggle(newButton);
    newButton.setScaleX(scaleFactor);
    newButton.setScaleY(scaleFactor);

    for (String item : workcenterList) {
      newButton = new RadioButton(item);
      newButton.setToggleGroup(workcenterGroup);
      newButton.setScaleX(scaleFactor);
      newButton.setScaleY(scaleFactor);
      filtersBox.getChildren().add(newButton);
    }

    Separator separator = new Separator();
//        separator.setValignment(VPos.CENTER);
    separator.setOrientation(Orientation.VERTICAL);
    filtersBox.getChildren().add(separator);

    // ==========================  Shifts   ==========================
    filtersBox.getChildren().add(new Label("Shifts:"));
    ObservableList<String> shiftList = (new ShiftDAO()).getList();
    ToggleGroup shiftGroup = new ToggleGroup();
    shiftGroup.selectedToggleProperty().addListener(
        (ov, oldToggle, newToggle) -> {
          shift = shiftGroup.getToggles().indexOf(newToggle);
          populateShiftViewTable();
        });

    newButton = new RadioButton("All");
    newButton.setToggleGroup(shiftGroup);
    filtersBox.getChildren().add(newButton);
    shiftGroup.selectToggle(newButton);
    newButton.setScaleX(scaleFactor);
    newButton.setScaleY(scaleFactor);

    for (String item : shiftList) {
      newButton = new RadioButton(item);
      newButton.setToggleGroup(shiftGroup);
      newButton.setScaleX(scaleFactor);
      newButton.setScaleY(scaleFactor);
      filtersBox.getChildren().add(newButton);
    }

    filtersBox.getStyleClass().add("pane");
  }

  // ============================   Output  =============================
  private void populateShiftViewTable() {
    ArrayList<Person> people;
    //  TODO: Add date parameter and use that for the following value:
    LocalDate today = LocalDate.now();
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d MMM uuuu");
    ObservableList columns = outputTable.getColumns();
    if (columns != null) {
      columns.clear();
    }

    // =============================  Columns  ============================
    //      =========================  Name  =========================
    outputTable.getColumns().add(createColumn(0, "Name"));

    //      ========================  Shifts  ========================
    LocalDate currentDate;
    String dateString;
    for (int x = 0; x < 7; x++) {
      currentDate = today.plusDays(x);
      dateString = currentDate.format(dateFormatter);
      outputTable.getColumns().add(createColumn(x + 1, dateString));
    }

    // ===========================  Build  List  ==========================
    ObservableList<ObservableList<StringProperty>> data =
            FXCollections.observableArrayList();
    PersonDAO personDao = new PersonDAO();
    ShiftDateDAO shiftDateDao = new ShiftDateDAO();
    people = personDao.getPeopleArrayListByShift(shift, workcenter);

    //  DEBUG:
    logger.info("\n[MainStage.rebuildTable()] people = " + people);
    
    for (Person p : people) {
      ArrayList<String> shifts = shiftDateDao.getWeek(p, LocalDate.now());

      //  DEBUG:
      logger.info("\n[MainStage.rebuildTable()] shifts = " + shifts);
      
      ObservableList<StringProperty> row = FXCollections.observableArrayList();
      for (String s : shifts) {
        row.add(new SimpleStringProperty(s));
      }

      String personName = p.getRank()      + " "
                        + p.getFirstName() + " "
                        + p.getLastName();
      row.add(0, new SimpleStringProperty(personName));
      data.add(row);
    }

    outputTable.setItems(data);
  }

  /**
   * <p>Creates the <code>outputTable</code> and sets its width, height,
   * <code>CSS</code> style, and selection mode.<p>
   */
  private void buildShiftViewTable() {
    outputTable = new TableView();
    logger.info("outputTable has been set");
    int width = 500;
    int height = 500;
    outputTable.setMinSize(width, height);
    outputTable.getStyleClass().add("pane");
    outputTable.getSelectionModel().setCellSelectionEnabled(true);
    outputTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    outputTable.setId("schedule_table");
  }
    
  // =============================   Misc  ==============================
  private void resize() {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                double width;
                double height;
                // ======================   Schedule Tab  ======================
                width = scheduleWindow.getWidth();
                height = scheduleWindow.getHeight();

                //  DEBUG:
                logger.info("\n[MainStage.display()] scheduleWindow's width: "
                    + scheduleWindow.getWidth());
                logger.info("\n[MainStage.display()] scheduleWindow's height: "
                    + scheduleWindow.getHeight());

                // ======================   Manage Tab  =======================
//        width = scheduleWindow.getPrefWidth();
//        height = scheduleWindow.getPrefHeight();
                // ========================   Set Size  ========================
//        window.setMinWidth(width);
                window.setMinHeight(height);
            }
        });

    }
}

