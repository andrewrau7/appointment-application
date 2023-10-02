module com.example.c195_project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens models to javafx.base;
    opens arau.C195_project to javafx.fxml;
    exports arau.C195_project;
    //exports arau.C195_project.customer_controllers;
    opens arau.C195_project.customer_controllers to javafx.fxml;
    //exports arau.C195_project.appointment_controllers;
    opens arau.C195_project.appointment_controllers to javafx.fxml;
    //exports arau.C195_project.menu_controllers;
    opens arau.C195_project.menu_controllers to javafx.fxml;
    opens helper to javafx.base;
    opens sql_models to javafx.base;
    exports helper;
    exports models;
    exports sql_models;
}