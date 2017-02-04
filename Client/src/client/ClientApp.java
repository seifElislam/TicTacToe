/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;


import client.network.Session;

import java.util.Scanner;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author seif
 */
public class ClientApp extends Application {
    
     @Override
    public void start(Stage stage) throws Exception {
       //Parent root = FXMLLoader.load(getClass().getResource("/resources/views/FXMLClient.fxml"));
   
      //Parent root = FXMLLoader.load(getClass().getResource("/resources/views/sinup.fxml"));

     //Parent root = FXMLLoader.load(getClass().getResource("/resources/views/Game.fxml"));
      //  Parent root = FXMLLoader.load(getClass().getResource("/resources/views/home.fxml"));

       Parent root = FXMLLoader.load(getClass().getResource("/resources/views/FXMLClient.fxml"));
        // Parent root = FXMLLoader.load(getClass().getResource("/resources/views/home.fxml"));

        Scene scene = new Scene(root,700,500);
//        stage.setTitle("Sign In window");
        stage.setTitle("Game");
        stage.setScene(scene);
       scene.getStylesheets().add(ClientApp.class.getResource("/resources/style/Login.css").toExternalForm());
         // scene.getStylesheets().add(ClientApp.class.getResource("/resources/style/signup.css").toExternalForm());

      // scene.getStylesheets().add(ClientApp.class.getResource("/resources/style/Game.css").toExternalForm());
    // scene.getStylesheets().add(ClientApp.class.getResource("/resources/style/home.css").toExternalForm());

//       scene.getStylesheets().add(ClientApp.class.getResource("/resources/style/Game.css").toExternalForm());
     // scene.getStylesheets().add(ClientApp.class.getResource("/resources/style/home.css").toExternalForm());

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
