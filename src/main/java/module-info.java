module fxml.p10 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;


    opens fxml to javafx.fxml;
    exports fxml;
    exports fxml.post;
    opens fxml.post to javafx.fxml;
    exports fxml.message;
    opens fxml.message to javafx.fxml;
    exports fxml.user;
    opens fxml.user to javafx.fxml;
}