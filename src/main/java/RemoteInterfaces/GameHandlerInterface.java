package RemoteInterfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameHandlerInterface extends Remote {

    void receiveMove(int from, int to, int killed) throws RemoteException;

    void disconnect(String nick) throws RemoteException;

}
