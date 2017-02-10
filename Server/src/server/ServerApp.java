/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import server.controllers.ServerController;
import server.network.Server;

/**
 *
 * @author Ehab
 */
public class ServerApp extends Application {
    public static Server server = new Server();
    public static Stage primaryStage ;
    public static Scene serverScene;
    public static ServerController serverController;
    
    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        FXMLLoader serverLoader = new FXMLLoader();
        serverLoader.setLocation(getClass().getResource("/resources/views/ServerView.fxml"));
        Parent serverParent = serverLoader.load();
        serverScene = new Scene(serverParent);
        serverController = (ServerController)serverLoader.getController();
        stage.setTitle("TicTacToe Server");
        stage.setScene(serverScene);
        stage.show();

        primaryStage.setOnCloseRequest((event) -> {
            if(server.running)
                server.stopServer();
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
