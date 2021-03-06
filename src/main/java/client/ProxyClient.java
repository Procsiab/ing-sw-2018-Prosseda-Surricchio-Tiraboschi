package client;

import shared.Cell;
import shared.Logger;
import shared.Position;
import shared.PositionR;
import shared.TransferObjects.GameManagerT;
import shared.network.MethodConnectionException;
import shared.network.SharedProxyClient;
import shared.network.Connection;
import shared.network.rmi.NetworkRmi;
import shared.network.socket.NetworkSocket;

import java.rmi.Remote;
import java.util.ArrayList;

/**
 * <p>This class implements {@code SharedProxyClient} and all its methods, making them interact with the correct <b>view</b>
 * components (if they are local) or routing them with the correct parameters to the server (if they are remote)</p><br>
 * <p>This class implements the <strong>singleton</strong> design pattern, having just one instance of it available per JVM;
 * this instance can be accessed through the static class' methods.</p><br>
 * <p>Before calling {@link ProxyClient#getInstance()}, a client using this class should first set the {@code Connection}
 * attribute by calling {@link ProxyClient#setConnection(Connection)}</p>
 * @see SharedProxyClient
 * @see Connection
 */
public final class ProxyClient implements SharedProxyClient {
    private static final String SERVER_INTERFACE = "ProxyServer";

    private static String uuid = MainClient.getUuid();
    private static Connection connection = null;
    private static Boolean isSocket = false;
    private static ProxyClient instance = new ProxyClient();

    /**
     * Private constructor, prevents external access and uncontrolled instantiation of the class: use the static method
     * {@link ProxyClient#getInstance()} to obtain the reference to the internal instance
     */
    private ProxyClient() {
        super();
    }

    /**
     * Obtain a reference to the class' instance
     * @return always the same instance, saved as a {@code private static} reference in the class
     */
    public static ProxyClient getInstance() {
        return instance;
    }

    /**
     * Set the {@code Connection} attribute held by ths class, to either a {@link NetworkSocket} or a {@link NetworkRmi}
     * instance with transparency, thanks to the implementation of the {@link Connection}. interface<br>
     * If the parameter {@code c} is an instance of {@code NetworkRmi}, the method {@link NetworkRmi#remotize(Object, Integer)}
     * is called on the private class attribute {@code instance}, making it exportable over RMI
     * @param c an instance implementing the {@code Connection} interface; if provided argument is of type {@code NetworkSocket}
     *          then an internal flag {@code isSocket} is set
     */
    public static void setConnection(Connection c) {
        if (connection == null) {
            connection = c;
            isSocket = c instanceof NetworkSocket;
            if (!isSocket) {
                // This static method calls UnicastRemoteObject's exportObject to make the reference to instance
                // exportable over RMI
                NetworkRmi.remotize(instance, 0);
            }
        }
    }

    /**
     * Getter method to obtain the saved {@code Connection} attribute
     * @return internal class' connection, if set; otherwise returns {@code null}
     */
    public static Connection getConnection() {
        return connection;
    }

    /**
     * <strong>Remote</strong><br>
     * When this method is called, it sets up the correct connection environment based on the player's decision (RMI or socket).
     * In practice, the instance of the {@code ProxyClient} class on the client is exported on the server's RMI registry,
     * or on the client's hash map inside the {@link NetworkSocket} class, to let the server call the {@code ProxyClient}'s methods
     * @param nick {@code String}
     * @return {@code String}
     * @see shared.network.SharedProxyClient#startGame(String)
     */
    @Override
    public String startGame(String nick) {
        Remote stub = instance;
        Integer port = -1;
        if (isSocket) {
            // First register the stub for remote calls, starting the consumer
            connection.export(stub, uuid);
            // Get consumer's port after starting the consumer
            port = connection.getListeningPort();
            // Set the stub to null, as without calling exportObject won't be passed as reference
            stub = null;
        }
        Object[] args = {uuid, nick, connection.getLocalIp(), port, isSocket, stub};
        String methodName = "startGame";
        try {
            return (String) connection.invokeMethod(SERVER_INTERFACE, methodName, args);
        } catch (MethodConnectionException mce) {
            return "Connection error";
        }
    }

    /**
     * <strong>Local</strong><br>
     * @param gameManager {@link GameManagerT}
     * @see shared.network.SharedProxyClient#updateView(GameManagerT)
     */
    @Override
    public void updateView(GameManagerT gameManager) {
        if (MainClient.isPrompt()) {
            MainClient.getCliController().updateView(gameManager);
        } else {
            if (MainClient.getStartGameController() != null)
                MainClient.getStartGameController().updateView(gameManager);
        }
    }

    /**
     * <strong>Local</strong><br>
     * @param windows {@code ArrayList<Integer>}
     * @param matrices {@code ArrayList<{@link Cell}[][]>}
     * @return {@code Boolean}
     * @see shared.network.SharedProxyClient#chooseWindow(ArrayList, ArrayList)
     */
    @Override
    public Boolean chooseWindow(ArrayList<Integer> windows, ArrayList<Cell[][]> matrices) {
        if (MainClient.isPrompt()) {
            MainClient.getCliController().chooseWindow(windows, matrices);
        } else {
            if (MainClient.getWaitingRoomController() != null)
                MainClient.getWaitingRoomController().chooseWindow(windows);
        }
        return true;
    }

    /**
     * <strong>Local</strong><br>
     * @return {@code Boolean}
     * @see shared.network.SharedProxyClient#startGameViewForced()
     */
    public Boolean startGameViewForced() {
        if (MainClient.isPrompt()) {
            MainClient.getCliController().startGameViewForced();
        } else {
            if (MainClient.getChooseWindowControllerController() != null)
                MainClient.getChooseWindowControllerController().startGameViewForced();
        }
        return true;
    }

    /**
     * <strong>Local</strong><br>
     * @return {@code Boolean}
     * @see shared.network.SharedProxyClient#ping()
     */
    @Override
    public Boolean ping() {
        return true;
    }

    /**
     * <strong>Local</strong><br>
     * @see shared.network.SharedProxyClient#aPrioriWin()
     */
    @Override
    public void aPrioriWin() {
        if (MainClient.isPrompt()) {
            MainClient.getCliController().aPrioriWin();
        } else {
            MainClient.getStartGameController().aPrioriWin();
        }
    }

    /**
     * <strong>Local</strong><br>
     * @see shared.network.SharedProxyClient#enable()
     */
    @Override
    public void enable() {
        if (MainClient.isPrompt()) {
            MainClient.getCliController().enable();
        } else {
            if (MainClient.getStartGameController() != null)
                MainClient.getStartGameController().enable();
        }
    }

    /**
     * <strong>Local</strong><br>
     * @see shared.network.SharedProxyClient#shut()
     */
    @Override
    public void shut() {
        if (MainClient.isPrompt()) {
            MainClient.getCliController().shut();
        } else {
            if (MainClient.getStartGameController() != null)
                MainClient.getStartGameController().shut();
        }
    }

    /**
     * <strong>Local</strong><br>
     * @param nicks {@code Arraylist<String>}
     * @param scores {@code Arraylist<Integer>}
     * @param winner {@code Arraylist<Boolean>}
     * @see shared.network.SharedProxyClient
     */
    @Override
    public void printScore(ArrayList<String> nicks, ArrayList<Integer> scores, ArrayList<Boolean> winner) {
        if (MainClient.isPrompt()) {
            MainClient.getCliController().printScore(nicks, scores, winner);
        } else {
            MainClient.getStartGameController().printScore(nicks, scores);
        }
    }

    /**
     * <strong>Remote</strong><br>
     * @param window {@code Integer}
     * @return {@code Boolean}
     * @see shared.network.SharedProxyClient#chooseWindowBack(Integer)
     */
    public Boolean chooseWindowBack(Integer window) {
        Object[] args = {uuid, window};
        String methodName = "chooseWindowBack";
        try {
            return (Boolean) connection.invokeMethod(SERVER_INTERFACE, methodName, args);
        } catch (MethodConnectionException mce) {
            return false;
        }
    }

    /**
     * <strong>Remote</strong><br>
     * @param index {@code Integer}
     * @param p {@link Position}
     * @return {@code Boolean}
     * @see shared.network.SharedProxyClient
     */
    public Boolean placeDice(Integer index, Position p) {
        Object[] args = {uuid, index, p};
        String methodName = "placeDice";
        try {
            return (Boolean) connection.invokeMethod(SERVER_INTERFACE, methodName, args);
        } catch (MethodConnectionException mce) {
            return false;
        }
    }

    /**
     * <strong>Remote</strong><br>
     * @param i1 {@code Integer}
     * @param p1 {@link Position}
     * @param p2 {@link Position}
     * @param p3 {@link Position}
     * @param p4 {@link Position}
     * @param pr {@link PositionR}
     * @param i2 {@code Integer}
     * @param i3 {@code Integer}
     * @return {@code Boolean}
     * @see shared.network.SharedProxyClient
     */
    @Override
    public Boolean useToolC(Integer i1, Position p1, Position p2, Position p3, Position p4, PositionR pr, Integer i2, Integer i3) {
        Object[] args = {uuid, i1, p1, p2, p3, p4, pr, i2, i3};
        String methodName = "useToolC";
        try {
            return (Boolean) connection.invokeMethod(SERVER_INTERFACE, methodName, args);
        } catch (MethodConnectionException mce) {
            return false;
        }
    }

    /**
     * <strong>Remote</strong><br>
     * @see shared.network.SharedProxyClient#exitGame2()
     */
    @Override
    public void exitGame2() {
        Object[] args = {uuid};
        String methodName = "exitGame2";
        try {
            connection.invokeMethod(SERVER_INTERFACE, methodName, args);
        } catch (MethodConnectionException mce) {
            Logger.log("Unable to inform server of the log out");
        } finally {
            connection.close();
        }
    }

    /**
     * <strong>Remote</strong><br>
     * @see shared.network.SharedProxyClient#endTurn()
     */
    @Override
    public void endTurn() {
        Object[] args = {uuid};
        String methodName = "endTurn";
        try {
            connection.invokeMethod(SERVER_INTERFACE, methodName, args);
        } catch (MethodConnectionException mce) {
            Logger.strace(mce);
        }
    }

    /**
     * <strong>Remote</strong><br>
     * @see shared.network.SharedProxyClient#updateViewFromC()
     */
    @Override
    public void updateViewFromC() {
        Object[] args = {uuid};
        String methodName = "updateViewFromC";
        try {
            connection.invokeMethod(SERVER_INTERFACE, methodName, args);
        } catch (MethodConnectionException mce) {
            Logger.strace(mce);
        }
    }

    /**
     * <strong>Remote</strong><br>
     * @return {@code Boolean}
     * @see shared.network.SharedProxyClient#exitGame1()
     */
    @Override
    public Boolean exitGame1() {
        Object[] args = {uuid};
        String methodName = "exitGame1";
        try {
            return (Boolean) connection.invokeMethod(SERVER_INTERFACE, methodName, args);
        } catch (MethodConnectionException mce) {
            Logger.log("Unable to inform server of the log out");
        } finally {
            connection.close();
        }
        return false;
    }

    /**
     * <strong>Local</strong><br>
     * @param s1 {@code String}
     * @param s2 {@code String}
     * @see shared.network.SharedProxyClient#onTimeStatus(String, String)
     */
    @Override
    public void onTimeStatus(String s1, String s2) {
        if (!MainClient.isPrompt()) {
            MainClient.getStartGameController().onTimeStatus(s1, s2);
        }
    }
}