/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author mhesham
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private ToggleGroup myToggleGroup;
    @FXML
    private TableView<Players> tableView;
    @FXML
    private TableColumn idColumn;
    @FXML
    private TableColumn fNameColumn;
    @FXML
    private TableColumn lNameColumn;
    @FXML
    private TableColumn loginColumn;
    @FXML
    private TableColumn scoreColumn;
    @FXML
    private ObservableList<Players> data;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        idColumn.setCellValueFactory(
            new PropertyValueFactory<Players,Integer>("id")
        );
        fNameColumn.setCellValueFactory(
            new PropertyValueFactory<Players,String>("fname")
        );
        lNameColumn.setCellValueFactory(
            new PropertyValueFactory<Players,String>("lname")
        );
        loginColumn.setCellValueFactory(
            new PropertyValueFactory<Players,String>("username")
        );
        scoreColumn.setCellValueFactory(
            new PropertyValueFactory<Players,String>("score")
        );
        data = FXCollections.observableArrayList();
        addPlayers();
        tableView.setItems(data);
    }
    
    @FXML
    protected void handleToggleOnAction(ActionEvent t) {
        System.out.println("on");
    }

    @FXML
    protected void handleToggleOffAction(ActionEvent t) {
        System.out.println("off");
    }
    public void addPlayers(){
        TicTacToeDataBase db=new TicTacToeDataBase();
        db.getallplayers().forEach((players) -> {
            data.add(players);
        });
 
    }

}
