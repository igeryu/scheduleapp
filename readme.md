# Schedule Manager Application

This application allows the user to connect to an existing Apache Derby database and add/edit people.

Functionality will be added to allow for the following features (not all inclusive):
- Add/View/Edit leave
- Add/View/Edit appointments
- Add/View/Edit organizational events
- Check for conflicts between leave/appointments/events
- Verify personnel and events are scheduled out for the next 30/60/90/etc. days

![Shift View - All](https://github.com/igeryu/scheduleapp/blob/develop/Screen%20Captures/2016-03-25_19-27%20Shift%20View%20A.png)

![Shift View - Filtered](https://github.com/igeryu/scheduleapp/blob/develop/Screen%20Captures/2016-03-25_19-27%20Shift%20View%20B.png)

## Change Log

### 2016-06-17

[**PersonDAO.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/domain/PersonDAO.java)

- Added [`getPerson(String, String, int)`](https://github.com/igeryu/scheduleapp/blob/develop/src/domain/PersonDAO.java#L149) method to help `MainStage.java` find the appropriate person to edit

[**EditPersonStage.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/window/EditPersonStage.java)

- Changed [`display()`](https://github.com/igeryu/scheduleapp/blob/develop/src/window/EditPersonStage.java#L229) to take the `Person` input.  It still creates a 'dummy' object if the input is null.

[**MainStage.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/window/MainStage.java)

- Changed `display()` to set `logger` to `INFO` level
- The [`editPersonButton`](https://github.com/igeryu/scheduleapp/blob/develop/src/window/MainStage.java#L316) now sends the proper person to be found

### 2016-03-26

[**DBBuild.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/util/DBBuild.java)

- Added `buildPersonEventTable()` call to `buildDatabase()`
- Adjusted `logger` messages and changed from `INFO` to `FINE`
- Added `buildPersonEventTypeTable()` method
- Removed the `NOT NULL` identifier from all foreign key declarations

[**PersonEventDAO.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/domain/PersonEventDAO.java)

- Removed unused imports and grouped remaining imports by root package
- Included the event type identifier in the `StringProperties` returned by `getWeek()`

[**PersonEventTypeDAO.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/domain/PersonEventTypeDAO.java)

- Created file from `ShiftDAO` template

### 2016-03-25

[**PersonDAO.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/domain/PersonDAO.java)

- Formatted to match Google Java Style
- Replaced debug `System.out` calls with `Logger` calls

[**PersonEventDAO.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/domain/PersonEventDAO.java)

- Created from `ShiftDateDAO` template
- Got both `getWeekEvents()` methods and the `getEvent()` method working

[**DBBuild.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/util/DBBuild.java)

- Added `EVENT_DESCRIPTION_SIZE`
- Added `buildPersonEventTable()` method
- Modified `buildObjectIdsTable()` to include the `PERSON_EVENT` table

[**MainStage.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/window/MainStage.java)

- Added createEventColumn() method
- Added columnOffset to populateShiftViewTable() method
- Updated populateShiftViewTable() to handle both shift view and event view

### 2016-03-24

[**Person.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/domain/Person.java)

- Changed all variables from `Integer` to `int` where appropriate

[**RankDAO.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/domain/RankDAO.java)

- Grouped and ordered methods into logical groupings
- Formatted to match Google Java Style

[**ShiftDAO.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/domain/ShiftDAO.java)

- Formatted to match Google Java Style

[**ShiftDateDAO.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/domain/ShiftDateDAO.java)

- Changed all variables from `Integer` to `int` where appropriate
- Replaced debug `System.out` calls with `Logger` calls
- Formatted to match Google Java Style

[**SkillDAO.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/domain/SkillDAO.java)

- Formatted to match Google Java Style

[**WorkcenterDAO.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/domain/WorkcenterDAO.java)

- Formatted to match Google Java Style

[**DBBuild.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/util/DBBuild.java)

- Replaced debug `System.out` calls with `Logger` calls
- Formatted to match Google Java Style

[**DBConnectionPool.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/util/DBConnectionPool.java)

- Replaced debug `System.out` calls with `Logger` calls
- Formatted to match Google Java Style

[**AddPersonStage.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/window/AddPersonStage.java)

- Grouped imports by root package
- Formatted to match Google Java Style

[**EditPersonStage.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/window/EditPersonStage.java)

- Grouped imports by root package
- Removed instantiation of classes with only `static` methods
- Formatted to match Google Java Style

[**MainStage.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/window/MainStage.java)

- Fixed Javadoc and formatting for `buildShiftViewTable()`
- Replaced debug `System.out` calls with `Logger` calls
- Grouped and ordered methods into logical groupings
- Improved the Javadoc for `createColumn()`
- Formatted to match Google Java Style

[**AlertBox.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/window/modal/AlertBox.java)

- Formatted to match Google Java Style

[**AnswerBox.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/window/modal/AnswerBox.java)

- Made all members `static`
- Formatted to match Google Java Style
- Replaced debug `System.out` calls with `Logger` calls

[**ConfirmBox.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/window/modal/ConfirmBox.java)

- Formatted to match Google Java Style

### 2016-03-23

[**ObjectDAO.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/domain/ObjectDAO.java)

- Grouped imports by root package
- Formatted to match Google Java Style (up to 4.5.1)
- Renamed `getNextObjectID()` to `getNextObjectId()`

[**MainStage.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/window/MainStage.java)

- Removed unused imports and grouped remaining imports by root package
- Removed the private class `PersonRow`
- Formatted to match Google Java Style (up to 3.4.1)

### 2016-03-13

[**PersonDAO.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/domain/PersonDAO.java)

- Modified `getPeopleArrayListByShift(shift, workcenter, date)` to allow for the shift or date parameters to be wildcards (`< 1`)
- Changed all method parameters from having an `Integer` parameter to primitive `int` parameters

[**MainStage.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/window/MainStage.java)

- Changed `rootLayout` to a `BorderPane` (from `StackPane`)
- Grouped class variables by type/function
- Extracted code to build `scheduleTab`, `manageTab`, and `filtersBox` from `display()` to `buildScheduleTab()`, `buildManageTab()` and `buildFiltersBox()`
- Changed name of `rebuildTable()` to `populateShiftViewTable()`
- Changed name of `scheduleTable` to `outputTable`

### 2016-03-09

[**mainStage.css**](https://github.com/igeryu/scheduleapp/blob/develop/src/css/mainStage.css)

- Added file, to manage styling for `MainStage` class

[**Person.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/domain/Person.java)

- Added constructor that takes `int` values for rank, workcenter and skill level (IDs)
- Added `setRankID()` and `setSkillID()` to work in parallel with `setWorkcenterID()`

[**PersonDAO.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/domain/PersonDAO.java)

- Renamed both `getPeopleListByShift()` methods to `getPeopleObsListByShift()`
- Added both `getPeopleArrayListByShift()` methods
- Added `getAllPeople()`

[**ShiftDateDAO.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/domain/ShiftDateDAO.java)

- Added both `getWeek()` methods

[**DBBuild.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/util/DBBuild.java)

- Fixed `buildObjectIdsTable()` so that `PERSON` and `SHIFT_DATE` start with an `ID` of `1`

[**MainStage.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/window/MainStage.java)

- Created file
- Added a tabbed pane with 'Schedule' and 'Manage' tabs
- Schedule Shift View works, shows each person (rows) with their current shift today and next six days.
- Made shift cells color-coded for easier viewing.

### 2016-03-02

[**PersonDAO.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/domain/PersonDAO.java)

- Changed `getPeopleByShift()` to `getPeopleListByShift()`
- Changed `getPeople()` to `getPeopleTableByShift()`
- Added `getPeopleListByShift(workcenter, shift, date)`.  The existing `getPeopleListByShift()` now calls this new overload using today's date.

[**ShiftDateDAO.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/domain/ShiftDateDAO.java)

- Added `getCurrentShift(person_id)` and changed `getCurrentShift(person)` to call the former method using `person.getObjectID()`
- Added `getCurrentShift(person_id, date)` and changed `getCurrentShift(person_id)` to call the former method using `LocalDate.now()`

[**AddPersonStage.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/window/AddPersonStage.java)

- Added an `AlertBox` message to the latter part of `addPerson()`
- Created `dateChoice` field to hold the current date selected by `startDateBox`
- Commented out the nullification of `startDateBox`’s date after a person is added
- Made `startDateBox` default to today's date when the window is opened

[**EditPersonStage.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/window/EditPersonStage.java)

- Removed code block from `addButton`’s `setOnAction()` lambda expression
- Added `cancelButton`
- Changed `saveCancelButton` to an `HBox`

[**AnswerBox.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/window/modal/AnswerBox.java)

- Updated `display(title, message, answers, orientation)` so that clicking 'OK' with no `RadioButton` selected does not throw an exception

### 2016-03-01

[**DBConnectionPool.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/util/DBConnectionPool.java)

- Added two `final String`s to represent the connection URL from a localhost and remote context
- Changed the `catch` clause of `getPoolConnection()` to display the exception's message

[**AddPersonStage.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/window/AddPersonStage.java)

- Added Javadoc for: `addPerson()`, `closeWindow()`, `display()`, `updateAllTables()`, `updateTables()`
- Updated `addPerson()` so that the `ChoiceBoxe`s are cleared out after a successful add
- Reordered method declarations so that they are in alphabetic order

[**EditPersonStage.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/window/EditPersonStage.java)

- Extracted action handler code from `deleteButton` to a `deletePerson()` method  (Also added extra line break to confirmation message)
- Refactoring: Removed unused imports, removed unused `switch`-statement in `setupButton()`
- Added Javadoc for: both `confirmChangeItem()` methods, `deletePerson()`, `display()`, `saveChanges()`, `setupButton()`
- Adjusted pop-up window title for both `confirmChangeItem()` methods
- Added `Person` parameter to `display()` method
- Reordered method declarations so that they are in alphabetic order

[**AlertBox.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/window/modal/AlertBox.java)

- Removed unused imports
- Added Javadoc for `display()`

[**AnswerBox.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/window/modal/AnswerBox.java)

- Removed unused imports
- Changed `display(title, message, defaultAnswer)` to use the `defaultAnswer` as the `answerField.promptText` rather than the initial value
- Added Javadoc for all three `display()` methods and `init()`
- Changed `layout`, `scene`, and `window` fields from `protected` to `private`

[**ConfirmBox.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/window/modal/ConfirmBox.java)

- Corrected formatting/layout for better presentation
- Added Javadoc to `display()`

### 2016-02-29

[**Person.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/domain/Person.java)

- Added `setAll()` method
- Modified `setWorkcenter()` to match other setters (updates `workcenter` as well as `workcenter_id`)

[**PersonDAO.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/domain/PersonDAO.java)

- Updated `addPerson()` to accept shift date and properly update the `SHIFT_DATE` table
- Updated `update()` method to make changes to all `PERSON` attributes

[**ShiftDateDAO.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/domain/ShiftDateDAO.java)

- Corrected `insert()` so that it works

[**AddPersonStage.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/window/AddPersonStage.java)

- Extracted logic from `addButton`’s action handler to a new `addPerson()` method
- The addPerson()` method now also adds a start date for the added person
- Added `closeWindow()` method to consult user whenever entered data will be lost
- Added `cancelButton` that calls `closeWindow()`

[**EditPersonStage.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/window/EditPersonStage.java)

- Attempting to save a change to workcenter but with no start date chosen will result in an error message
- Saving a change to workcenter **with** a start date will successfully change the workcenter and add a new start date

[**ConfirmBox.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/window/modal/ConfirmBox.java)
- Corrected formatting/layout for better presentation

> Derby is property of the Apache Software Foundation.