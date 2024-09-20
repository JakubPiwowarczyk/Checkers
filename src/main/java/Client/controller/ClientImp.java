package Client.controller;

import Client.view.MenuLayout;
import Client.view.Window;
import RemoteInterfaces.ClientInterface;
import RemoteInterfaces.GameHandlerInterface;
import RemoteInterfaces.Piece;
import RemoteInterfaces.ServerInterface;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceDialog;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientImp extends UnicastRemoteObject implements ClientInterface {

    private ServerInterface server;
    private GameHandlerInterface gameRoom;

    private String nickname;
    private boolean isLogged = false;
    private boolean isInGame = false;

    private boolean hasTurn = false;
    private List<String> possibleMoves = new ArrayList<>();

    private int fromId;
    private int toId;

    public ClientImp() throws RemoteException, MalformedURLException {
        LocateRegistry.createRegistry(0);
        Naming.rebind("client", this);

        try {
            server = (ServerInterface) Naming.lookup("rmi://localhost:1099/server");
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void clientLog(String msg){
        System.out.println("[SERVER]: " + msg);
    }

    @Override
    public void startGame(GameHandlerInterface gameRoom, String redNick, String blackNick) throws RemoteException {
        this.gameRoom = gameRoom;
        this.isInGame = true;
        Platform.runLater(() -> {
            Window.displayBoard();
            Window.board.setRedNick(redNick);
            Window.board.setBlackNick(blackNick);
        });
    }

    @Override
    public void setTurn(String nickname, String turnSign, List<String> moves) throws RemoteException {
        this.hasTurn = this.nickname.equals(nickname);
        if(Window.board != null)
            Platform.runLater(() -> Window.board.setTurn(turnSign));

        possibleMoves = moves;
    }

    @Override
    public void updateBoard(int from, int to, int killed) throws RemoteException {
        Piece piece = Window.board.getFields().get(from).getPiece();
        Platform.runLater(() -> Window.board.getFields().get(from).setPiece(null));
        Platform.runLater(() -> Window.board.getFields().get(to).setPiece(piece));

        if(killed != -1){
            Platform.runLater(() -> Window.board.getFields().get(killed).setPiece(null));
        }
    }


    /*
    *  METHODS THAT ARE NOT INHERITED FROM INTERFACE
    */

    public void login(String nickname){
        this.nickname = nickname;
        int responseStatus = 0;

        try {
            responseStatus = server.registerClient(nickname, this);
        } catch (RemoteException e) {
            e.printStackTrace();
            System.out.println("Login Error");
        }

        if(responseStatus == 0) {
            isLogged = true;
            Window.window.setScene(new Scene(Window.menu = new MenuLayout(), 500, 500));
        }
    }

    public void logout(){
        if(isLogged){
            try {
                server.logoutClient(nickname);
                if(isInGame) gameRoom.disconnect(nickname);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void enterQueue(boolean isRed){
        try {
            int resp = server.enterQueue(nickname, isRed);

            if(resp == 0){
                Window.menu.getPlayButton().setDisable(true);
                Window.menu.getPlayButton().setText("In Queue");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void sendMoveToServer(int killedId){
        try {
            gameRoom.receiveMove(fromId, toId, killedId);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    //Getters and Setters

    public String getNickname() {
        return nickname;
    }

    public boolean isHasTurn() {
        return hasTurn;
    }

    public List<String> getPossibleMoves() {
        return possibleMoves;
    }

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
        System.out.println(fromId);
    }

    public void setToId(int toId) {
        this.toId = toId;
    }
}
