/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.controllers;


import java.net.URL;

import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author seif
 */

public class HomeController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML private Button invite,logout,easy,meduim,hard; 
    @FXML private Label opponentName,opponentWins,opponentLoses,opponentDraws;
    @FXML private Label playerName,playerWins,playerLoses,playerDraws;
  //  @FXML private TableView<Person> allPlayersTable;
    @FXML private ImageView imgView;
     @FXML private String src="/resources/images/o.png";
    @FXML private Image playerImg= new Image(getClass().getResourceAsStream(src)); ;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        opponentInfo() ;
        playerInfo() ;
    }    
    @FXML protected void handleButton_invite_Action(ActionEvent event) {
      System.out.println("invite");
      playerImg.getClass().getResourceAsStream("../images/o.png");
      
       imgView.setImage(playerImg);
       System.out.println(playerImg.impl_getUrl());
       System.out.println(imgView.getImage());
    }
    @FXML protected void handleButton_logout_Action(ActionEvent event) {
       System.out.println("logout");
    }
    @FXML protected void handleButton_easy_Action(ActionEvent event) {
      System.out.println("easy");
    }
    @FXML protected void handleButton_medium_Action(ActionEvent event) {
      System.out.println("medium");
    }
    @FXML protected void handleButton_hard_Action(ActionEvent event) {
      System.out.println("hard");
    }
    @FXML protected void playerInfo() {
      playerName.setText("seif");
      playerWins.setText("1000");
      playerLoses.setText("1000");
      playerDraws.setText("1000");
    }
    @FXML protected void opponentInfo() {
      opponentName.setText("ehab");
     opponentWins.setText("1000");
      opponentLoses.setText("1000");
      opponentDraws.setText("1000");
    }
    
}
