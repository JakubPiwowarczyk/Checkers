package RemoteInterfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ClientInterface extends Remote {

    void clientLog(String msg) throws RemoteException;

    void startGame(GameHandlerInterface gameRoom, String redNick, String blackNick) throws RemoteException;

    void setTurn(String nickname, String turnSign, List<String> moves) throws RemoteException;

    void updateBoard(int from, int to, int killed) throws RemoteException;
}
