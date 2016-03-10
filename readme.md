# Schedule Manager Application

This application allows the user to connect to an existing Apache Derby database and add/edit people.

Functionality will be added to allow for the following features (not all inclusive):
- Add/View/Edit leave
- Add/View/Edit appointments
- Add/View/Edit organizational events
- Check for conflicts between leave/appointments/events
- Verify personnel and events are scheduled out for the next 30/60/90/etc. days

## Change Log

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