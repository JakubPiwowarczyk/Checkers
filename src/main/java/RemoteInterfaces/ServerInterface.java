package RemoteInterfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {

    int registerClient(String nickname, ClientInterface client) throws RemoteException;

    void logoutClient(String nickname) throws RemoteException;

    int enterQueue(String nickname, boolean isRed) throws RemoteException;
}
