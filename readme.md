# Schedule Manager Application

This application allows the user to connect to an existing Apache Derby database and add/edit people.

Functionality will be added to allow for the following features (not all inclusive):
- Add/View/Edit leave
- Add/View/Edit appointments
- Add/View/Edit organizational events
- Check for conflicts between leave/appointments/events
- Verify personnel and events are scheduled out for the next 30/60/90/etc. days

## Change Log

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