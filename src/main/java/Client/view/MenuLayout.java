package Client.view;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class MenuLayout extends VBox {

    private final Button playButton;

    public MenuLayout() {
        Font menuFont = new Font(30);

        Label nicknameLabel = new Label("Hello " + Window.controller.getNickname() + "!");
        nicknameLabel.setFont(menuFont);

        playButton = new Button("Play");
        playButton.setFocusTraversable(false);
        playButton.setFont(menuFont);
        playButton.setMinWidth(300);

        ToggleGroup colorChooserGroup = new ToggleGroup();
        Label colorsLabel = new Label("Choose preferred color");

        RadioButton red = new RadioButton("Red");
        red.setToggleGroup(colorChooserGroup);
        red.setFocusTraversable(false);
        red.setSelected(true);

        RadioButton black = new RadioButton("Black");
        black.setToggleGroup(colorChooserGroup);
        black.setFocusTraversable(false);

        HBox colorsHBox = new HBox(colorsLabel, red, black);
        colorsHBox.setAlignment(Pos.CENTER);
        colorsHBox.setSpacing(10);

        Button exitButton = new Button("Exit");
        exitButton.setFocusTraversable(false);
        exitButton.setFont(menuFont);
        exitButton.setMinWidth(300);
        exitButton.setOnAction(e -> {
            Window.controller.logout();
            Platform.exit();
            System.exit(0);
        });

        playButton.setOnAction(e -> {
            boolean isRed = colorChooserGroup.getSelectedToggle().equals(red);
            Window.controller.enterQueue(isRed);
        });

        this.getChildren().addAll(nicknameLabel, playButton, colorsHBox, exitButton);

        this.setAlignment(Pos.CENTER);
        this.setSpacing(10);
    }

    public Button getPlayButton() {
        return playButton;
    }
}
