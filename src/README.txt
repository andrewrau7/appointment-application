TITLE: Database App Ver.1
PURPOSE: To read from and write to a local database information regarding customers and appointments using a GUI.
AUTHOR: Andrew Rau ID: 010266752
EMAIL: arau13@wgu.edu
PHONE: 951-290-9893
APP VERSION: 1
DATE: 23/02/2023

IDE: IntelliJ IDEA 2022.2.3 (Community Edition)
JDK: Oracle OpenJDK version 17.0.5, JavaFX 17.0.2


DIRECTIONS:
Login: Enter a valid username/password combination to gain access to the application.

Main Menu: Choose either Appointments or Customers to view respective info tables.

Customer Table View: Choose between updating, deleting, or adding a customer to the database. For updating
or deleting, a selection must be made. Deleting a customer also deletes any appointment associated with them.

Add/Update Customer: Ensure every text field contains input and a selection is made for Country and Division.

Appointment Table View: Choose between updating, deleting, or adding a customer to the database. The TableView
can also be filtered with the RadioButtons directly above the table. "This Week" returns all appointments in the
current week number of the year. "This Month" returns all appointments taking place in the current month.
"Contact" returns appointments assigned to the specified contact. "Date" returns all appointments taking place
on the specified date. Two ComboBoxes on the bottom of the page allow for the user to determine the number
appointments by either type or month depending on the selection.

Add/Update Appointment: Ensure every text field contains input and selections are made for every ComboBox
and DatePicker. Time fields have been designed with functionality to prevent any invalid appointment times
from being scheduled.


ADDITIONAL REPORT: A filter option has been added to the Customer Table page, allowing the user to view either all
customer records or filter the customer records by the number of appointments they have in the database.
RadioButtons are implemented for choosing which filter to use, while a ComboBox is utilized to select the number
of appointments that customers would at least have to have scheduled in order to be viewed in the filter list.

DRIVER: mysql-connector-java-8.0.29


SOURCES:
For datePicker String conversion:
https://stackoverflow.com/questions/36846776/how-to-use-custom-date-format-for-a-datepicker
For datePicker date restrictions:
https://stackoverflow.com/questions/41001703/how-to-customize-datepicker-in-javafx-so-that-i-can-restrict-the-user-to-not-be