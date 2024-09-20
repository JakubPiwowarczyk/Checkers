package Client.view;

import RemoteInterfaces.Piece;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;

public class BoardLayout extends GridPane {

    private final ArrayList<FieldLayout> fields = new ArrayList<>(32);

    private final Label redNick = new Label("Red");
    private final Label blackNick = new Label("Black");
    private final Label turn = new Label("<");


    public BoardLayout() {
        int col = 0;
        int row = 0;
        int i = 0;

        while(i < 32){
            int sum = col + row;
            if(!(sum%2==0)) {
                fields.add(new FieldLayout());
                this.add(fields.get(i), col, row);
                i++;
            }
            col++;
            if(col == 8){
                col = 0;
                row++;
            }
        }

        for(int j = 0; j < 32; j++){
            if(j < 12) fields.get(j).setPiece(Piece.BLACK);
            if(j > 19) fields.get(j).setPiece(Piece.RED);
        }

        HBox gameInfoBox = new HBox(redNick, turn, blackNick);
        gameInfoBox.setBorder(new Border(
                new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        gameInfoBox.setAlignment(Pos.CENTER);
        gameInfoBox.setSpacing(200);
        Font font = new Font(40);
        redNick.setFont(font);
        redNick.setTextFill(Color.RED);
        turn.setFont(font);
        blackNick.setFont(font);

        this.add(gameInfoBox, 0, 8, 8, 1);
    }

    public ArrayList<FieldLayout> getFields() {
        return fields;
    }

    public void setRedNick(String redNick) {
        this.redNick.setText(redNick);
    }

    public void setBlackNick(String blackNick) {
        this.blackNick.setText(blackNick);
    }

    public void setTurn(String turn) {
        this.turn.setText(turn);
    }
}
