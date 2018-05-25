package shared.network;

import shared.Position;
import shared.PositionR;
import shared.TransferObjects.GameManagerT;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface SharedMiddlewareServer extends Remote {
    Boolean deniedAccess(String uuid) throws RemoteException;
    String startGame(String uuid, String ip, Integer port, Boolean isSocket) throws RemoteException;
    void updateView(String uuid, GameManagerT gameManager) throws RemoteException;
    Boolean chooseWindow(String uuid, ArrayList<Integer> windows) throws RemoteException;
    Boolean ping(String uuid) throws RemoteException;
    void aPrioriWin(String uuid) throws RemoteException;
    void enable(String uuid) throws RemoteException;
    void shut(String uuid) throws RemoteException;
    void printScore(String uuid, Integer score) throws RemoteException;
    void setWinner(String uuid) throws RemoteException;
    Boolean chooseWindowBack(String uuid, Integer window) throws RemoteException;
    Boolean startGameViewForced(String uuid) throws RemoteException;
    Boolean placeDice(String uuid, Integer index, Position p) throws RemoteException;
    Boolean useToolC(String uuid, Integer i1, Position p1, Position p2, Position p3, Position p4, PositionR pr, Integer i2, Integer i3) throws RemoteException;
    void exitGame2(String uuid) throws RemoteException;
    public boolean endTurn(String uuid) throws RemoteException;
    public void updateViewFromC(String uuid) throws RemoteException;
}