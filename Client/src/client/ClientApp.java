/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import client.network.Session;
import client.controllers.*;
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

    public static Stage primaryStage;
    public static Scene signIn;
    public static Scene signUp;
    public static Scene home;
    public static Scene game;
    public static GameController gameController;
    public static HomeController homeController;
    public static LoginController loginController;
    public static Session session;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;

        //sign in
        FXMLLoader signInLoader = new FXMLLoader();
        signInLoader.setLocation(getClass().getResource("/resources/views/LoginView.fxml"));
        Parent signInParent = signInLoader.load();
        signIn = new Scene(signInParent, 700, 500);
        loginController = (LoginController) signInLoader.getController();
        //sign up
        FXMLLoader signUpLoader = new FXMLLoader();
        signUpLoader.setLocation(getClass().getResource("/resources/views/SignupView.fxml"));
        Parent signUpParent = signUpLoader.load();
        signUp = new Scene(signUpParent, 700, 500);
        //home
        FXMLLoader homeLoader = new FXMLLoader();
        homeLoader.setLocation(getClass().getResource("/resources/views/HomeView.fxml"));
        Parent homeParent = homeLoader.load();
        home = new Scene(homeParent, 700, 500);
        homeController = (HomeController) homeLoader.getController();
        //game
        FXMLLoader gameLoader = new FXMLLoader();
        gameLoader.setLocation(getClass().getResource("/resources/views/GameView.fxml"));
        Parent gameParent = gameLoader.load();
        game = new Scene(gameParent, 700, 500);
        gameController = (GameController) gameLoader.getController();

        stage.setTitle("Game");
        stage.setScene(signIn);
        stage.show();
        stage.setMinWidth(800);
        stage.setMaxWidth(800);
        stage.setMinHeight(600);
        stage.setMaxHeight(600);
        primaryStage.setOnCloseRequest((event) -> {
            if (session != null && session.connected) {
                session.closeConnection();
            }
        });
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
