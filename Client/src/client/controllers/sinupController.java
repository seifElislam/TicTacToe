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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

/**
 *
 * @author khaledgamal
 */
public class sinupController implements Initializable {
    
    @FXML
    private Label label=new Label();
    @FXML
    private Text heolow = new Text();
    @FXML
    private TextField userName = new TextField();
    @FXML
    private TextField userPassword = new TextField();
    @FXML
    private TextField firstName = new TextField();
    @FXML
    private TextField lastName = new TextField();
    @FXML
    private ImageView userPic = new ImageView();
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText("you signed in ");
        heolow.setText(userName.getText()+"  :->"  +userPassword.getText() );
        
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
