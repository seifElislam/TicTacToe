/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.controllers;


import assets.*;
import client.ClientApp;

import static client.ClientApp.session;

import static client.ClientApp.primaryStage;

import client.Player;
import client.network.Session;
import java.io.IOException;
import java.net.URL;

import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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
    @FXML private TableView<Player> allPlayersTable;
    @FXML private TableColumn colUsername;
    @FXML private TableColumn colScore;
    @FXML private TableColumn colStatus;
    private ObservableList<Player> playersData = FXCollections.observableArrayList();
    @FXML private ImageView imgView;
    
    @FXML public ImageView playerImgView;
    @FXML public ImageView opponentImgView;
    @FXML public Image playerImg;
    @FXML public Image opponentImg;
    private Stage primaryStage;
    private String opponent;
      
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
       
        colUsername.setCellValueFactory(
            new PropertyValueFactory<>("username")
        );
        colScore.setCellValueFactory(
            new PropertyValueFactory<>("score")
        );
        colStatus.setCellValueFactory(
            new PropertyValueFactory<>("status")
        );
        primaryStage = ClientApp.primaryStage;
        allPlayersTable.getSelectionModel().selectedIndexProperty().addListener(new RowSelectChangeListener());        
    }   
     
     private class RowSelectChangeListener implements ChangeListener {
        @Override
        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
            System.out.println("seif");
            opponentInfo();
        }
    };

     
     
    @FXML protected void handleButton_invite_Action(ActionEvent event) {
//        opponent=getOpponentFromtable;
        if(allPlayersTable.getSelectionModel().getSelectedItem()!=null&&allPlayersTable.getSelectionModel().getSelectedItem().getStatus().equals(Status.ONLINE)){
        ClientApp.session.requestGame(allPlayersTable.getSelectionModel().getSelectedItem().getUsername());
        }else{
            Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Player not available");
                    alert.setContentText(allPlayersTable.getSelectionModel().getSelectedItem().getUsername()+" not available");
                    alert.showAndWait();
                    
        }
//        allPlayersTable.getSelectionModel().selectedIndexProperty().addListener(new RowSelectChangeListener());

    };
    
//    @FXML protected void handle_table_Action(ActionEvent event) {
//         System.out.println("error");
//        opponentInfo();
//       
//
//    };
    
    @FXML protected void handleButton_logout_Action(ActionEvent event) {
        ClientApp.session.closeConnection();
        primaryStage.setScene(client.ClientApp.signIn);
    }
    @FXML protected void handleButton_arcade_Action(ActionEvent event) {
        ClientApp.session.playWithAI();
        primaryStage.setScene(client.ClientApp.game);
        ClientApp.gameController.resetScene();
        
    }
   
    @FXML public void playerInfo() {
      playerName.setText(ClientApp.session.player.getUsername());
      playerScore.setText(Integer.toString(ClientApp.session.player.getScore()));
     playerImg=new Image(getClass().getResourceAsStream("/resources/images/"+ClientApp.session.player.getPicPath()));
      ClientApp.homeController.playerImgView.setImage(playerImg);
        allPlayersTable.getSelectionModel().selectFirst();
    }
    @FXML protected void opponentInfo() {
        if(allPlayersTable.getSelectionModel().getSelectedItem() != null){
            opponentName.setText(allPlayersTable.getSelectionModel().getSelectedItem().getUsername());
            opponentScore.setText(Integer.toString(allPlayersTable.getSelectionModel().getSelectedItem().getScore()));
            opponentImg=new Image(getClass().getResourceAsStream("/resources/images/"+allPlayersTable.getSelectionModel().getSelectedItem().getPicPath()));
            opponentImgView.setImage(opponentImg);
        }
    }
    public void bindPlayersTable(){
        playersData.clear(); 
        Session.allPlayers.entrySet().forEach((player) -> {
            playersData.add(player.getValue());
        });
        allPlayersTable.setItems(playersData);
    }
    public void showAlert(String playerName){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, playerName+" wants to play with you", ButtonType.NO, ButtonType.YES);
        if (alert.showAndWait().get() == ButtonType.YES) {
            ClientApp.session.sendResponse(true);
            ClientApp.gameController.resetScene();

            ClientApp.primaryStage.setScene(client.ClientApp.game);
            System.out.println("play again");
            ClientApp.gameController.img = new Image(getClass().getResourceAsStream("/resources/images/o.png"));
        }else{
            ClientApp.session.sendResponse(false);
        }
    }
}
