/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.controllers;


import client.ClientApp;
import java.io.IOException;
import java.net.URL;

import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author seif
 */

public class HomeController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML private Button invite,logout; 
    @FXML private Label opponentName,opponentScore;
    @FXML private Label playerName,playerScore;
  //  @FXML private TableView<Person> allPlayersTable;
    @FXML private ImageView imgView;
     @FXML private String src="/resources/images/o.png";
    @FXML private Image playerImg= new Image(getClass().getResourceAsStream(src)); 
    private Stage primaryStage;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        primaryStage = ClientApp.primaryStage;
        opponentInfo() ;
        playerInfo() ;
    }    
    @FXML protected void handleButton_invite_Action(ActionEvent event) {
      System.out.println("invite");
       
            primaryStage.setScene(client.ClientApp.game);

    };
    @FXML protected void handleButton_logout_Action(ActionEvent event) {
       System.out.println("logout");
       
    
            primaryStage.setScene(client.ClientApp.signIn);
    
    }
    @FXML protected void handleButton_arcade_Action(ActionEvent event) {
      System.out.println("arcade");
     
            primaryStage.setScene(client.ClientApp.game);
    
    }
   
    @FXML protected void playerInfo() {
      playerName.setText("seif eleslam");
      playerScore.setText("1000");

    }
    @FXML protected void opponentInfo() {
      opponentName.setText("ehab gamal");
     opponentScore.setText("1000");

    }
    
}
