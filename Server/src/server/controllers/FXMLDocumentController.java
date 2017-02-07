/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.controllers;

import model.*;
import server.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author mhesham
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private ToggleGroup myToggleGroup;
    @FXML
    private TableView<Player> tableView;
    @FXML
    private TableColumn fNameColumn;
    @FXML
    private TableColumn lNameColumn;
    @FXML
    private TableColumn loginColumn;
    @FXML
    private TableColumn scoreColumn;
    @FXML
    private TableColumn statusColumn;
    @FXML
    private ObservableList<Player> data;
    private ObservableList<Player> playersList = FXCollections.observableArrayList();
    @FXML private Button key;
    //@FXML String src="/resources/images/swithon.png";
    //@FXML String src2="/resources/images/swithoff.png";
    @FXML  Image switchOn = new Image(getClass().getResourceAsStream("/resources/images/swithon.png"));
    @FXML  Image switchOff = new Image(getClass().getResourceAsStream("/resources/images/swithoff.png"));


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    
        fNameColumn.setCellValueFactory(
            new PropertyValueFactory<>("fname")
        );
        lNameColumn.setCellValueFactory(
            new PropertyValueFactory<>("lname")
        );
        loginColumn.setCellValueFactory(
            new PropertyValueFactory<>("username")
        );
        scoreColumn.setCellValueFactory(
            new PropertyValueFactory<>("score")
        );
        statusColumn.setCellValueFactory(
            new PropertyValueFactory<>("status")
        );
        
        data = FXCollections.observableArrayList();
        
    }
    
    @FXML
    protected void handleToggleOnAction(ActionEvent t) {
        if(!ServerApp.server.running)
        {
            if(ServerApp.server.startServer(5555)){
                key.setGraphic(new ImageView(switchOn));
                bindPlayersTable();
            }else{
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error starting the server");
                alert.setContentText("Cannot start the server, try using another port number");
                alert.showAndWait();
            }
        }else{
            ServerApp.server.stopServer();
            bindPlayersTable();
            key.setGraphic(new ImageView(switchOff));
        }
        
    }

    public void bindPlayersTable(){
        playersList.clear();
        ServerApp.server.allPlayers.entrySet().forEach((player) -> {
            playersList.add(player.getValue());
        });
        tableView.setItems(playersList);
    }
}
