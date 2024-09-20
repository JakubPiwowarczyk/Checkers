package Client.view;

import Client.controller.ClientImp;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.Objects;

public class Window extends Application {

    public static ClientImp controller;

    public static Stage window;

    public static MenuLayout menu;
    public static BoardLayout board;

    @Override
    public void start(Stage stage){
        try {
            controller = new ClientImp();
        } catch (RemoteException | MalformedURLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Server Error");
            alert.setHeaderText("Failed to connect to the server");
            alert.showAndWait();
        }

        window = stage;
        window.setTitle("Checkers!");
        window.setOnCloseRequest(windowEvent -> {
            if(!Objects.isNull(controller))
                controller.logout();
            Platform.exit();
            System.exit(0);
        });

        Scene loginScene = new Scene(new LoginLayout(), 300, 250);

        window.setScene(loginScene);
        window.show();
    }

    public static void displayBoard(){
        board = new BoardLayout();
        window.setScene(new Scene(board));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
