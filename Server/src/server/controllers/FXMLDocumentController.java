/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.controllers;

import model.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    private ObservableList<Player> data;
    @FXML private Button key;
    @FXML int flag=0;
     @FXML String src="/resources/images/swithon.png";
     @FXML String src2="/resources/images/swithoff.png";
    @FXML  Image img = new Image(getClass().getResourceAsStream(src));
    @FXML  Image img2 = new Image(getClass().getResourceAsStream(src2));


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
        
        data = FXCollections.observableArrayList();
        
    }
    
    @FXML
    protected void handleToggleOnAction(ActionEvent t) {
        if(flag==0)
        {
            key.setGraphic(new ImageView(img));
            System.out.println("on");
            addPlayers();
            tableView.setItems(data);
            
            flag=1;
        }
        else{
        
            System.out.println("off");
            flag=0;
            key.setGraphic(new ImageView(img2));
        }
        
    }

    @FXML
    protected void handleToggleOffAction(ActionEvent t) {
        System.out.println("off");
    }
    public void addPlayers(){
//        Players.getAllPlayers().entrySet().forEach((m) -> {
//            data.add(m.getValue());
//        }); 
 
    }

}
