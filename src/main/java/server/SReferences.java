package server;

import server.threads.GameManager;

import java.util.ArrayList;

public class SReferences {
    //MatchManager is singleton.
    public static Integer activePlayers;
    //given the id of a player
    public static ArrayList<String> uUIDRef = new ArrayList<>();
    public static ArrayList<Integer> gameIdRef = new ArrayList<>();
    public static ArrayList<Player> playerRef = new ArrayList<>();
    public static ArrayList <GameManager> gameRef = new ArrayList<>();
    public static ArrayList<NetworkRmiClientI> rmiClientRef = new ArrayList<>();
    //further Arraylist if needed by socket.
}
