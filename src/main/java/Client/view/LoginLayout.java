package Client.view;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.util.regex.Pattern;

public class LoginLayout extends HBox {

    public LoginLayout() {
        TextField nickname = new TextField("Nickname");
        nickname.setFocusTraversable(false);

        Button login = new Button("Login");
        login.setFocusTraversable(false);
        login.setOnAction(e -> {
            String str = nickname.getText();
            Pattern pattern = Pattern.compile("\\s");
            boolean hasWhitespace = pattern.matcher(str).find();
            Alert alert = new Alert(Alert.AlertType.ERROR);

            if(hasWhitespace){
                alert.setTitle("Nickname Error!");
                alert.setHeaderText("No whitespaces allowed in the nickname!");
                alert.showAndWait();
            }else if(str.length() > 20){
                alert.setTitle("Nickname Error!");
                alert.setHeaderText("The nickname cannot be longer than 20 characters!");
                alert.showAndWait();
            }else{
                Window.controller.login(str);
            }
        });

        this.getChildren().addAll(nickname, login);

        this.setSpacing(10);
        this.setAlignment(Pos.CENTER);
    }
}
