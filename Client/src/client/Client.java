/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author seif
 */
public class Client extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
//        Parent root = FXMLLoader.load(getClass().getResource("FXMLClient.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("Game.fxml"));
        Scene scene = new Scene(root,700,500);
//        stage.setTitle("Sign In window");
        stage.setTitle("Game");
        stage.setScene(scene);
        scene.getStylesheets().add(Client.class.getResource("Game.css").toExternalForm());
        stage.show();
        stage.setMinWidth(800);
        stage.setMaxWidth(800);
        stage.setMinHeight(600);
        stage.setMaxHeight(600);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
