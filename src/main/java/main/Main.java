package main;

import fxml.ControllerContext;
import jdbc.JDBC;

public class Main {
    public static void main(String[] args) {
        if (JDBC.run_jdbc()) ControllerContext.run();
    }
}