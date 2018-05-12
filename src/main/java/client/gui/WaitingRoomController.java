package client.gui;

import client.MainClient;
import client.MiddlewareClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import shared.Logger;

import java.awt.*;
import java.io.IOException;

public class WaitingRoomController {
    @FXML private AnchorPane paneTest;
    @FXML private Button buttonTest;


    public void nextView() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ChooseWindow.fxml"));
        Parent root1 = loader.load();

            Scene startedGame = new Scene(root1, 1280, 800, Color.WHITE);
            Stage window = (Stage) paneTest.getScene().getWindow();
            window.setScene(startedGame);
            window.show();

    }
    @FXML
    private void testLoad(ActionEvent event) throws IOException{
        nextView();



    }

}
