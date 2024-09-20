package Server;

import RemoteInterfaces.ClientInterface;
import RemoteInterfaces.GameHandlerInterface;
import RemoteInterfaces.Piece;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GameHandlerImp extends UnicastRemoteObject implements GameHandlerInterface {

    private static int roomNumber = 0;

    private final String redNick;
    private final String blackNick;
    private final ClientInterface redPlayer;
    private final ClientInterface blackPlayer;
    private final ArrayList<Field> fields = new ArrayList<>(32);

    private boolean isRedsTurn = true;
    private boolean isRedAlive = true;
    private boolean isBlackAlive = true;

    public GameHandlerImp(String redNick, String blackNick, ClientInterface redPlayer, ClientInterface blackPlayer) throws RemoteException, MalformedURLException {
        LocateRegistry.createRegistry(0);
        Naming.rebind("game", this);

        roomNumber++;
        this.redNick = redNick;
        this.blackNick = blackNick;
        this.redPlayer = redPlayer;
        this.blackPlayer = blackPlayer;

        createFields();

        redPlayer.startGame(this, redNick, blackNick);
        blackPlayer.startGame(this, redNick, blackNick);

        redPlayer.setTurn(redNick, "<", possibleMovesForRed());
        blackPlayer.setTurn(redNick, "<", possibleMovesForRed());

        redPlayer.clientLog("Game has started in Room#"+roomNumber+". Your opponent is "+blackNick);
        blackPlayer.clientLog("Game has started in Room#"+roomNumber+". Your opponent is "+redNick);
    }


    @Override
    public void receiveMove(int from, int to, int killed) throws RemoteException {
        checkStatus();
        if(!isRedAlive) blackPlayer.clientLog("You have won!");
        if(!isBlackAlive) redPlayer.clientLog("You have won!");

        Piece piece = fields.get(from).getPiece();
        fields.get(from).setOccupied(false);
        fields.get(from).setPiece(null);

        fields.get(to).setOccupied(true);
        fields.get(to).setPiece(piece);

        if(killed != -1){
            fields.get(killed).setOccupied(false);
            fields.get(killed).setPiece(null);
        }

        redPlayer.updateBoard(from, to, killed);
        blackPlayer.updateBoard(from, to, killed);

        if(isRedsTurn){
            isRedsTurn = false;
            redPlayer.setTurn(blackNick, ">", possibleMovesForBlack());
            blackPlayer.setTurn(blackNick, ">", possibleMovesForBlack());
        }else{
            isRedsTurn = true;
            redPlayer.setTurn(redNick, "<", possibleMovesForRed());
            blackPlayer.setTurn(redNick, "<", possibleMovesForRed());
        }
    }

    @Override
    public void disconnect(String nick) throws RemoteException {
        if(nick.equals(redNick)){
            blackPlayer.clientLog("Your opponent has disconnected! You have won!");
        }else{
            redPlayer.clientLog("Your opponent has disconnected! You have won!");
        }

    }


    /*
     *  METHODS THAT ARE NOT INHERITED FROM INTERFACE
     */

    private void checkStatus(){
        boolean blackAlive = false;
        boolean redAlive = false;

        for(Field field : fields){
            if(field.isOccupied()){
                if(field.getPiece() == Piece.RED) redAlive = true;
                if(field.getPiece() == Piece.BLACK) blackAlive = true;
            }
        }

        this.isRedAlive = redAlive;
        this.isBlackAlive = blackAlive;
    }

    private List<String> possibleMovesForRed(){ //it makes a list of possible moves as a string in form 'from_id;to_id;killed_id'
        List<String> moves = new LinkedList<>();

        for(Field field : fields){
            if(field.getPiece() != Piece.RED) continue;

            if(field.getLeftUp() != null && !field.getLeftUp().isOccupied()) {
                moves.add(fields.indexOf(field) + ";" + fields.indexOf(field.getLeftUp()) + ";null");
            }else if(field.getLeftUp() != null
                    && field.getLeftUp().getLeftUp() != null
                    && !field.getLeftUp().getLeftUp().isOccupied()
                    && field.getLeftUp().getPiece() == Piece.BLACK){
                moves.add(fields.indexOf(field) + ";" + fields.indexOf(field.getLeftUp().getLeftUp()) + ";"+ fields.indexOf(field.getLeftUp()));
            }

            if(field.getRightUp() != null && !field.getRightUp().isOccupied()) {
                moves.add(fields.indexOf(field) + ";" + fields.indexOf(field.getRightUp()) + ";null");
            }else if(field.getRightUp() != null
                    && field.getRightUp().getRightUp() != null
                    && !field.getRightUp().getRightUp().isOccupied()
                    && field.getRightUp().getPiece() == Piece.BLACK){
                moves.add(fields.indexOf(field) + ";" + fields.indexOf(field.getRightUp().getRightUp()) + ";"+ fields.indexOf(field.getRightUp()));
            }
        }
        return moves;
    }

    private List<String> possibleMovesForBlack(){ //it makes a list of possible moves as a string in form 'from_id;to_id;killed_id'
        List<String> moves = new LinkedList<>();

        for(Field field : fields){
            if(field.getPiece() != Piece.BLACK) continue;

            if(field.getLeftDown() != null && !field.getLeftDown().isOccupied()) {
                moves.add(fields.indexOf(field) + ";" + fields.indexOf(field.getLeftDown()) + ";null");
            }else if(field.getLeftDown() != null
                    && field.getLeftDown().getLeftDown() != null
                    && !field.getLeftDown().getLeftDown().isOccupied()
                    && field.getLeftDown().getPiece() == Piece.RED){
                moves.add(fields.indexOf(field) + ";" + fields.indexOf(field.getLeftDown().getLeftDown()) + ";"+ fields.indexOf(field.getLeftDown()));
            }

            if(field.getRightDown() != null && !field.getRightDown().isOccupied()) {
                moves.add(fields.indexOf(field) + ";" + fields.indexOf(field.getRightDown()) + ";null");
            }else if(field.getRightDown() != null
                    && field.getRightDown().getRightDown() != null
                    && !field.getRightDown().getRightDown().isOccupied()
                    && field.getRightDown().getPiece() == Piece.RED){
                moves.add(fields.indexOf(field) + ";" + fields.indexOf(field.getRightDown().getRightDown()) + ";"+ fields.indexOf(field.getRightDown()));
            }
        }
        return moves;
    }

    private void createFields(){
        for(int i = 0; i < 32; i++){
            if(i < 12) fields.add(new Field(Piece.BLACK));
            if(i >= 12 && i <= 19) fields.add(new Field());
            if(i > 19) fields.add(new Field(Piece.RED));
        }

        fields.get(0).setFields(null, null, fields.get(4), fields.get(5));
        fields.get(1).setFields(null, null, fields.get(5), fields.get(6));
        fields.get(2).setFields(null, null, fields.get(6), fields.get(7));
        fields.get(3).setFields(null, null, fields.get(7), null);
        fields.get(4).setFields(null, fields.get(0), null, fields.get(8));
        fields.get(5).setFields(fields.get(0), fields.get(1), fields.get(8), fields.get(9));
        fields.get(6).setFields(fields.get(1), fields.get(2), fields.get(9), fields.get(10));
        fields.get(7).setFields(fields.get(2), fields.get(3), fields.get(10), fields.get(11));
        fields.get(8).setFields(fields.get(4), fields.get(5), fields.get(12), fields.get(13));
        fields.get(9).setFields(fields.get(5), fields.get(6), fields.get(13), fields.get(14));
        fields.get(10).setFields(fields.get(6), fields.get(7), fields.get(14), fields.get(15));
        fields.get(11).setFields(fields.get(7), null, fields.get(15), null);
        fields.get(12).setFields(null, fields.get(8), null, fields.get(16));
        fields.get(13).setFields(fields.get(8), fields.get(9), fields.get(16), fields.get(17));
        fields.get(14).setFields(fields.get(9), fields.get(10), fields.get(17), fields.get(18));
        fields.get(15).setFields(fields.get(10), fields.get(11), fields.get(18), fields.get(19));
        fields.get(16).setFields(fields.get(12), fields.get(13), fields.get(20), fields.get(21));
        fields.get(17).setFields(fields.get(13), fields.get(14), fields.get(21), fields.get(22));
        fields.get(18).setFields(fields.get(14), fields.get(15), fields.get(22), fields.get(23));
        fields.get(19).setFields(fields.get(15), null, fields.get(23), null);
        fields.get(20).setFields(null, fields.get(16), null, fields.get(24));
        fields.get(21).setFields(fields.get(16), fields.get(17), fields.get(24), fields.get(25));
        fields.get(22).setFields(fields.get(17), fields.get(18), fields.get(25), fields.get(26));
        fields.get(23).setFields(fields.get(18), fields.get(19), fields.get(26), fields.get(27));
        fields.get(24).setFields(fields.get(20), fields.get(21), fields.get(28), fields.get(29));
        fields.get(25).setFields(fields.get(21), fields.get(22), fields.get(29), fields.get(30));
        fields.get(26).setFields(fields.get(22), fields.get(23), fields.get(30), fields.get(31));
        fields.get(27).setFields(fields.get(23), null, fields.get(31), null);
        fields.get(28).setFields(null, fields.get(24), null, null);
        fields.get(29).setFields(fields.get(24), fields.get(25), null, null);
        fields.get(30).setFields(fields.get(25), fields.get(26), null, null);
        fields.get(31).setFields(fields.get(26), fields.get(27), null, null);
    }
}
