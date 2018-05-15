package client;

import client.gui.LogInScreenController;
import client.gui.StartGameController;
import client.gui.ChooseWindowController;
import client.gui.WaitingRoomController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import client.threads.GameHelper;
import org.fusesource.jansi.AnsiConsole;
import shared.Logger;
import shared.network.rmi.NetworkRmi;
import shared.network.socket.NetworkSocket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.Console;

public class MainClient extends Application {
    public static GameHelper game; //resetta scelte utente
    public static String uuid = null;
    private static Console cnsl;
    private static String connection;
    private static String interfaccia;
    private static boolean isPrompt;

    public static LogInScreenController logInScreenController;
    public static ChooseWindowController chooseWindowController;
    public static StartGameController startGameController;
    public static WaitingRoomController waitingRoomController;
    public static ArrayList<Integer> choosenCards;

    /*
    dice dado1
    dice dado2
    position posizione 1
    ...
    */

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/LogInScreen.fxml"));
        Scene logIn = new Scene(root);
        primaryStage.setScene(logIn);
        primaryStage.show();
    }

    public static void main(String[] args) {

        AnsiConsole.systemInstall();
        String LOGO = "";
        AnsiConsole.out().println(LOGO);

        uuid = "774778658";

        Logger.log(uuid);

        //uuid = getUuid();

        Logger.log("~ Choose the connection type ('Rmi' | 'Socket') ~");
        Scanner inConnection = new Scanner(System.in);
        connection = inConnection.nextLine().toLowerCase();

        while(!connection.equals("rmi") && !connection.equals("socket") ){
            Logger.log("Please provide a valid choice");
            connection = inConnection.nextLine();
        }
        if (connection.equals("rmi")){
            MiddlewareClient.setConnection(new NetworkRmi("", 0));
        }
        else if (connection.equals("socket")){
            MiddlewareClient.setConnection(new NetworkSocket("", 0));
        }

        Logger.log("~ Choose the input interface ('GUI' | 'CMD') ~");
        Scanner inInterface = new Scanner(System.in);
        interfaccia = inInterface.nextLine().toLowerCase();
        while(!interfaccia.equals("cmd") && !interfaccia.equals("gui") ){
            Logger.log("Please provide a valid choice");
            interfaccia = inInterface.nextLine();
        }
        if (interfaccia.equals("cmd")){
            isPrompt= true ;
        }
        else if (interfaccia.equals("gui")){
            launch(args);
        }
    }

    private static String getUuid() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            Process process;
            String cmd = "wmic csproduct get UUID";
            try {
                process = Runtime.getRuntime().exec(cmd);
                process.waitFor();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                int i = 0;
                while ((line = reader.readLine()) != null && i < 3) {
                    uuid = line;
                    i++;
                }
                uuid = uuid.substring(0,uuid.length()-2); // Remove blanks on Windows
            } catch (Exception e) {
                Logger.strace(e);
            }
        } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
            Logger.log("~ Please provide your password (to let this program read your UUID) ~");
            /*cnsl = System.console();
            char[] pwd = cnsl.readPassword("Password: ");

            String pass = new String(pwd);
            StringBuilder output = new StringBuilder();
            Process process;
            String[] cmd = {"/bin/sh", "-c", "echo " + pass + " | sudo -S cat /sys/class/dmi/id/product_uuid"};

            try {
                process = Runtime.getRuntime().exec(cmd);
                process.waitFor();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append('\n');
                }
            } catch (Exception e) {
                Logger.strace(e);
            }
            uuid = output.toString();*/
            uuid = "AAAAA-BBBBB-CCCCC-DDDDD";
        } else if (os.contains("mac")) {
            Logger.log("~ Please provide your password (to let this program read your UUID) ~");
            StringBuilder output = new StringBuilder();
            Process process;
            Scanner scanner = new Scanner(System.in);
            String pass = scanner.nextLine();
            //https://www.infoworld.com/article/3029204/macs/10-essential-os-x-command-line-tips-for-power-users.html
            String[] cmd = {};
            try {
                process = Runtime.getRuntime().exec(cmd);
                process.waitFor();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append('\n');
                }
            } catch (Exception e) {
                Logger.strace(e);
            }
            uuid = output.toString();
        }
        return uuid;
    }
}