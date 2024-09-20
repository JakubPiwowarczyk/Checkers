package Server;

import RemoteInterfaces.ClientInterface;
import RemoteInterfaces.ServerInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ServerImp extends UnicastRemoteObject implements ServerInterface {

    private final Map<String, ClientInterface> players = new HashMap<>();

    private final LinkedList<String> redQueue = new LinkedList<>();
    private final LinkedList<String> blackQueue = new LinkedList<>();

    public ServerImp() throws RemoteException, MalformedURLException {
        LocateRegistry.createRegistry(1099);
        Naming.rebind("server", this);
        System.out.println("Server is running");
    }

    @Override
    public synchronized int registerClient(String nickname, ClientInterface client) {
        System.out.println("Register request");
        if(players.containsKey(nickname)){
            try {
                client.clientLog("Player with this nickname is already registered!");
                System.out.println("Register failed");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            return -1;
        }else{
            players.put(nickname, client);
            try {
                client.clientLog("Player has been successfully registered!");
                System.out.println("'" + nickname + "'" + " registered");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            return 0;
        }
    }

    @Override
    public void logoutClient(String nickname) throws RemoteException {
        players.remove(nickname);
        System.out.println("'" + nickname + "'" + " logged out");
    }

    @Override
    public synchronized int enterQueue(String nickname, boolean isRed) throws RemoteException {
        if(isRed){
            if(blackQueue.isEmpty()){
                redQueue.add(nickname);
            }else{
                String blackNick = blackQueue.getFirst();
                ClientInterface redPlayer = players.get(nickname);
                ClientInterface blackPlayer = players.get(blackNick);
                try {
                    new GameHandlerImp(nickname, blackNick, redPlayer, blackPlayer);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                blackQueue.removeFirst();
            }
        }
        if(!isRed){
            if(redQueue.isEmpty()){
                blackQueue.add(nickname);
            }else{
                String redNick = redQueue.getFirst();
                ClientInterface redPlayer = players.get(redNick);
                ClientInterface blackPlayer = players.get(nickname);
                try {
                    new GameHandlerImp(redNick, nickname, redPlayer, blackPlayer);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                redQueue.removeFirst();
            }
        }
        return 0;
    }
}
