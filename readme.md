# Schedule Manager Application

This application allows the user to connect to an existing Apache Derby database and add/edit people.

Functionality will be added to allow for the following features (not all inclusive):
- Add/View/Edit leave
- Add/View/Edit appointments
- Add/View/Edit organizational events
- Check for conflicts between leave/appointments/events
- Verify personnel and events are scheduled out for the next 30/60/90/etc. days

## Change Log

### 2016-03-01

[**DBConnectionPool.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/util/DBConnectionPool.java)

- Added two final Strings to represent the connection URL from a localhost and remote context
- Changed the catch clause of getPoolConnection() to display the exception's message

[**AddPersonStage.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/window/AddPersonStage.java)

- Added Javadoc for: addPerson(), closeWindow(), display(), updateAllTables(), updateTables()
- Updated addPerson() so that the ChoiceBoxes are cleared out after a successful add
- Reordered method declarations so that they are in alphabetic order

[**EditPersonStage.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/window/EditPersonStage.java)

- Extracted action handler code from deleteButton to a deletePerson() method  (Also added extra line break to confirmation message)
- Refactoring: Removed unused imports, removed unuse switch-statement in setupButton()
- Added Javadoc for: both confirmChangeItem() methods, deletePerson(), display(), saveChanges(), setupButton()
- Adjusted popup window title for both confirmChangeItem() methods
- Added Person parameter to display() method
- Reordered method declarations so that they are in alphabetic order

[**AlertBox.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/window/modal/AlertBox.java)

- Removed unused imports
- Added Javadoc for display()

[**AnswerBox.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/window/modal/AnswerBox.java)

- Removed unused imports
- Changed display(title, message, defaultAnswer) to use the defaultAnswer as the answerField.promptText rather than the initial value
- Added Javadoc for all three display() methods and init()
- Changed layout, scene, and window fields from protected to private

[**ConfirmBox.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/window/modal/ConfirmBox.java)

- Corrected formatting/layout for better presentation
- Added Javadoc to display()

### 2016-02-29

[**Person.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/domain/Person.java)

- Added setAll() method
- Modified setWorkcenter() to match other setters (updates workcenter as well as workcenter_id)

[**PersonDAO.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/domain/PersonDAO.java)

- Updated addPerson() to accept shift date and properly update the SHIFT_DATE table
- Updated update() method to make changes to all PERSON attributes

[**ShiftDateDAO.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/domain/ShiftDateDAO.java)

- Corrected insert() so that it works

[**AddPersonStage.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/window/AddPersonStage.java)

- Extracted logic from addButton's action handler to a new addPerson() method
- The addPerson() method now also adds a start date for the added person
- Added closeWindow() method to consult user whenever entered data will be lost
- Added cancelButton that calls closeWindow()

[**EditPersonStage.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/window/EditPersonStage.java)

- Attempting to save a change to workcenter but with no start date chosen will result in an error message
- Saving a change to workcenter **with** a start date will successfully change the workcenter and add a new start date

[**ConfirmBox.java**](https://github.com/igeryu/scheduleapp/blob/develop/src/window/modal/ConfirmBox.java)
- Corrected formatting/layout for better presentation

> Derby is property of the Apache Software Foundation.