/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;


import client.network.Session;
import client.controllers.*;
import java.util.Scanner;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 *
 * @author seif
 */
public class ClientApp extends Application {
    
    public static Stage primaryStage ;
    public static Scene signIn;
    public static Scene signUp;
    public static Scene home;
    public static Scene game;
    public static GameController gameController;
    public static HomeController homeController;
    public static Session session;
   
    @Override
    public void start(Stage stage) throws Exception {
//        setPrimaryStage(stage);
        primaryStage = stage;

        //sign in
        FXMLLoader signInLoader=new FXMLLoader();
        signInLoader.setLocation(getClass().getResource("/resources/views/FXMLClient.fxml"));
        Parent signInParent = signInLoader.load();
        signIn = new Scene(signInParent,700,500);
        signIn.getStylesheets().add(ClientApp.class.getResource("/resources/style/Login.css").toExternalForm());
        //sign up
        FXMLLoader signUpLoader=new FXMLLoader();
        signUpLoader.setLocation(getClass().getResource("/resources/views/sinup.fxml"));
        Parent signUpParent = signUpLoader.load();
        signUp = new Scene(signUpParent,700,500);
        signUp.getStylesheets().add(ClientApp.class.getResource("/resources/style/signup.css").toExternalForm());
        //home
        FXMLLoader homeLoader=new FXMLLoader();
        homeLoader.setLocation(getClass().getResource("/resources/views/home.fxml"));
        Parent homeParent = homeLoader.load();
        home = new Scene(homeParent,700,500);
        home.getStylesheets().add(ClientApp.class.getResource("/resources/style/home.css").toExternalForm());
        homeController = (HomeController)homeLoader.getController();
        //game
        FXMLLoader gameLoader=new FXMLLoader();
        gameLoader.setLocation(getClass().getResource("/resources/views/Game.fxml"));
        Parent gameParent = gameLoader.load();
        game = new Scene(gameParent,700,500);
        game.getStylesheets().add(ClientApp.class.getResource("/resources/style/Game.css").toExternalForm());
        gameController=(GameController)gameLoader.getController();
        
        stage.setTitle("Game");
        stage.setScene(signIn);
        stage.show();
        stage.setMinWidth(800);
        stage.setMaxWidth(800);
        stage.setMinHeight(600);
        stage.setMaxHeight(600);
        primaryStage.setOnCloseRequest((event) -> {
            if(session != null && session.connected)
                session.closeConnection();
                //ClientApp.gameController.resetScene();
        });
    }
    
//    public void setPrimaryStage(Stage primaryStage){
//        this.primaryStage = primaryStage;
//    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
