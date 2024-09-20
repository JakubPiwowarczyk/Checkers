package Client.view;

import RemoteInterfaces.Piece;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;

public class FieldLayout extends StackPane {

    private Piece piece;
    private Circle circle;

    private List<String> destinations;

    public FieldLayout() {
        this.setStyle("-fx-background-color: #40ed71");
        this.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        this.setOnMouseEntered(mouseEvent -> {
            if(!Window.controller.isHasTurn()) return;

            boolean isInList = false;
            destinations = new ArrayList<>();
            List<String> moves = Window.controller.getPossibleMoves();
            for(String move : moves){
                String[] moveInfo = move.split(";");
                if(moveInfo[0].equals(String.valueOf(Window.board.getFields().indexOf(this)))){
                    isInList = true;
                    destinations.add(moveInfo[1]);
                }
            }

            if(isInList) {
                this.setStyle("-fx-background-color: #fcba03");
                for(String destination : destinations){
                    Window.board.getFields().get(Integer.parseInt(destination)).setStyle("-fx-background-color: #fcba03");
                }
            }
        });

        this.setOnMouseClicked(mouseEvent -> {
            if(!Window.controller.isHasTurn()) return;

            boolean isInFromList = false;
            boolean isInToList = false;
            int killedId = -1;
            List<String> moves = Window.controller.getPossibleMoves();

            for(String move : moves){
                String[] moveInfo = move.split(";");
                if(moveInfo[0].equals(String.valueOf(Window.board.getFields().indexOf(this)))){
                    isInFromList = true;
                }else if(moveInfo[0].equals(String.valueOf(Window.controller.getFromId()))){
                    if(moveInfo[1].equals(String.valueOf(Window.board.getFields().indexOf(this)))){
                        isInToList = true;
                        if(!moveInfo[2].equals("null")){
                            killedId = Integer.parseInt(moveInfo[2]);
                        }
                    }
                }
            }

            if(isInFromList) Window.controller.setFromId(Window.board.getFields().indexOf(this));
            if(isInToList) {
                Window.controller.setToId(Window.board.getFields().indexOf(this));
                Window.controller.sendMoveToServer(killedId);
            }
        });

        this.setOnMouseExited(mouseEvent -> {
            this.setStyle("-fx-background-color: #40ed71");
            if(destinations == null) return;
            for(String destination : destinations){
                Window.board.getFields().get(Integer.parseInt(destination)).setStyle("-fx-background-color: #40ed71");
            }
        });

        this.setMinWidth(100);
        this.setMinHeight(100);
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
        if(piece == Piece.RED){
            circle = new Circle();
            circle.setRadius(40);
            circle.setFill(Color.RED);
            this.getChildren().add(circle);
        }else if(piece == Piece.BLACK){
            circle = new Circle();
            circle.setRadius(40);
            circle.setFill(Color.BLACK);
            this.getChildren().add(circle);
        }else{
            this.getChildren().remove(circle);
        }
    }
}
