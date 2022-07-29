module fxml.p10 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;


    opens fxml to javafx.fxml;
    exports fxml;
}