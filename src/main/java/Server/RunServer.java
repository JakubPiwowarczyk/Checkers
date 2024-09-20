package Server;

import java.net.MalformedURLException;
import java.rmi.RemoteException;

public class RunServer {
    public static void main(String[] args) throws RemoteException, MalformedURLException {
        new ServerImp();
    }
}
