module app.task03 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;


    opens Client.view to javafx.fxml;
    exports Client.view;
    exports Client.controller;
    exports RemoteInterfaces to java.rmi;
}