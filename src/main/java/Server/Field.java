package Server;

import RemoteInterfaces.Piece;

public class Field {

    private Field leftUp, rightUp, leftDown, rightDown;

    private boolean isOccupied;
    private Piece piece = null;

    public Field(Piece piece) {
        this.piece = piece;
        isOccupied = true;
    }

    public Field() {
        isOccupied = false;
    }


    public void setFields(Field leftUp, Field rightUp, Field leftDown, Field rightDown) {
        this.leftUp = leftUp;
        this.rightUp = rightUp;
        this.leftDown = leftDown;
        this.rightDown = rightDown;
    }

    public Field getLeftUp() {
        return leftUp;
    }

    public Field getRightUp() {
        return rightUp;
    }

    public Field getLeftDown() {
        return leftDown;
    }

    public Field getRightDown() {
        return rightDown;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public Piece getPiece() {
        return piece;
    }
}
