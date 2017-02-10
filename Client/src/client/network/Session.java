    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.network;

import assets.*;
import client.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author Ehab
 */
public class Session {
    public static HashMap<String, Player> allPlayers = new HashMap<String, Player>();
    public Player player;
    private  String player1;
    private  String  player2;
    private Socket socket;
    private final int portNumber;
    private final String ipAddress;
    private ObjectInputStream downLink;
    private ObjectOutputStream upLink;
    public boolean connected = false;
    private boolean loggedin = false;
    public boolean IAmX=false;
    public boolean myTurn;
    private Button[][] btns = {
                {ClientApp.gameController.b1,ClientApp.gameController.b2,ClientApp.gameController.b3},
                {ClientApp.gameController.b4,ClientApp.gameController.b5,ClientApp.gameController.b6},
                {ClientApp.gameController.b7,ClientApp.gameController.b8,ClientApp.gameController.b9}};
    
    public Session(String ipAddress, int portNumber){
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
    }
    public void openConnection(){
        try {
            socket = new Socket(ipAddress, portNumber);
            upLink = new ObjectOutputStream(socket.getOutputStream());
            downLink = new ObjectInputStream(socket.getInputStream());
            connected = true;
        } catch (IOException ex) {
            connected = false;
        }
    }
    public void closeConnection(){
        sendMessage(new Message(MsgType.LOGOUT));
        connected = false;
        try {
            upLink.close();
            downLink.close();
            socket.close();
        } catch (IOException ex) {
        }
    }
    public void terminateConnection(){
        closeConnection();
        Platform.runLater(() -> {
            ClientApp.primaryStage.setScene(ClientApp.signIn);
            ClientApp.loginController.terminateConnectino();
        });
    }
    private void startCommunication(){
        new Thread(() -> {
            while(connected){
                try {
                    Message message = (Message) downLink.readObject();
                    messageHandler(message);
                } catch (IOException ex) {
                    connected = false;
                    break;
                } catch(ClassNotFoundException cnfex){
                }
            }
            try{
                socket.close();
                downLink.close();
                upLink.close();
            }catch(IOException ex){
            }
        }).start();
    }
    private void messageHandler(Message message){
        switch(message.getType()){
            case INIT:
            case NOTIFY:
                updatePlayersList(message);
                break;
            case GAME_REQ:
                respondToRequest(message);
                break;
            case GAME_RES:
                handleResponse(message);
                break;
            case MOVE:
                handleMove(message);
                break;
            case GAME_OVER:
                handleGameOver(message);
                break;
            case CHAT:
                chatHandler(message);
                break;
            case TERM:
                terminateConnection();
                break;
            default:
                // unhandled message type
                break;
        }
    }
    public boolean loginToServer(String username, String password){
        Message message = new Message(MsgType.LOGIN);
        message.setData("username", username);
        message.setData("password", password);
        if(connected){
            sendMessage(message);
            while(connected){
                try{
                    Message response = (Message)downLink.readObject();
                    if(response.getType() == MsgType.LOGIN){
                        if(response.getData("signal").equals(MsgSignal.SUCCESS)){
                            loggedin = true;
                            player = new Player();
                            player.setUsername(response.getData("username"));
                            player.setFname(response.getData("fname"));
                            player.setLname(response.getData("lname"));
                            player.setPicPath(response.getData("picpath"));
                            player.setScore(Integer.parseInt(response.getData("score")));
                            startCommunication();
                        }
                        break;
                    }else
                        messageHandler(response);
                }catch(IOException ioex){
                }catch(ClassNotFoundException cnfex){
                }
            }
        }
        return loggedin;
    }
    public boolean playerSignup(String fname, String lname, String username, String password, String picpath) {
        boolean regResult = false;
        Message message = new Message(MsgType.REGISTER);
        message.setData("username", username);
        message.setData("password", password);
        message.setData("fname",fname);
        message.setData("lname",lname);
        message.setData("picpath",picpath);
        if(connected){
            sendMessage(message);
            while(connected){
                try{
                    Message response = (Message)downLink.readObject();
                    if(response.getType() == MsgType.REGISTER){
                        if(response.getData("signal").equals(MsgSignal.SUCCESS)){
                            regResult = true;
                        }
                        break;
                    }
                }catch(IOException ioex){
                }catch(ClassNotFoundException cnfex){
                }
            }
        }
        return regResult;
    }
    private void sendMessage(Message message){
        try{
            upLink.writeObject(message);
        }catch(IOException ioex){
        }
    }
    public void updatePlayersList(Message message){
        if(!message.getData("username").equals(this.player.getUsername())){
            if(message.getType() == MsgType.INIT){
                Player newPlayer = new Player();
                newPlayer.setUsername(message.getData("username"));
                newPlayer.setStatus(message.getData("status"));
                newPlayer.setScore(Integer.parseInt(message.getData("score")));
                newPlayer.setPicPath(message.getData("picpath"));
                allPlayers.put(message.getData("username"), newPlayer);
            }else if(message.getType() == MsgType.NOTIFY){
                switch(message.getData("key")){
                    case "status":
                        allPlayers.get(message.getData("username")).setStatus(message.getData("value"));
                        break;
                    case "score":
                        allPlayers.get(message.getData("username")).setScore(Integer.parseInt(message.getData("value")));
                        break;
                }
            }
            Platform.runLater(ClientApp.homeController::bindPlayersTable);
        }else{
            if(message.getType() == MsgType.NOTIFY && message.getData("key").equals("score")){
                player.setScore(Integer.parseInt(message.getData("value")));
                Platform.runLater(ClientApp.homeController::playerInfo);
            }
        }
    }
    public void chatHandler(Message message){
        Platform.runLater(() -> {
            String msg = "@"+message.getData("sender")+": "+message.getData("text")+"\n";
            ClientApp.gameController.txt_area.appendText(msg);
        });
    }
    public void sendChatMessage(String text){
        if(!text.equals("")){
            Message message = new Message(MsgType.CHAT);
            String receiver;
            if(player1 == null)
                receiver = player2;
            else
                receiver = player1;
            message.setData("sender", player.getUsername());
            message.setData("receiver", receiver);
            message.setData("text", ClientApp.gameController.txt_field.getText());
            sendMessage(message);
        }
    }
    public void requestGame(String secondPlayerName){
        //**ALERT** waiting for other player response with cancel button
        Message message=new Message(MsgType.GAME_REQ,"destination",secondPlayerName);
        sendMessage(message);
    }
    public void respondToRequest(Message incoming){
        //**Alert** with the request from **playerRequestingGame** returns boolean **accept**
        player1=incoming.getData("source");
        Platform.runLater(() -> {
            ClientApp.homeController.showAlert(player1);
        });
    }
    public void sendResponse(boolean response){
        IAmX=false;
        Message outgoing=new Message(MsgType.GAME_RES,"destination",player1);
        outgoing.setData("response",response?"accept":"deny");
        sendMessage(outgoing);
    }
    public void handleResponse(Message incoming){
        if(incoming.getData("response").equals("accept")){
            IAmX = true;
            myTurn = true;
            player2 = incoming.getData("source");
            Platform.runLater(() -> {
                ClientApp.primaryStage.setScene(client.ClientApp.game);
                ClientApp.gameController.resetScene();
                ClientApp.gameController.img = new Image(Session.this.getClass().getResourceAsStream("/resources/images/x.png"));
            });
        }else{
            //other player rejected request
        }
    }
    public void playWithAI(){
        ClientApp.gameController.resetScene();
        sendMessage(new Message(MsgType.AIGAME_REQ));
        player1 = player.getUsername();
        player2 = "computer";
        IAmX = true;
        myTurn = true;
        ClientApp.gameController.img = new Image(getClass().getResourceAsStream("/resources/images/x.png"));
    }
    public void makeAMove(String x,String y) {
        myTurn = false;
        Message message=new Message(MsgType.MOVE);
        message.setData("x", x);
        message.setData("y", y);
        message.setData("target", player2);
        sendMessage(message);
    }
    private void handleMove(Message message) {
        myTurn = true;
        Platform.runLater(() -> {
            btns[Integer.parseInt(message.getData("x"))][Integer.parseInt(message.getData("y"))].setGraphic(new ImageView(IAmX?"/resources/images/o.png":"/resources/images/x.png"));
            if(Integer.parseInt(message.getData("x")) == 0){
                if(Integer.parseInt(message.getData("y")) == 0){
                    ClientApp.gameController.flag1 = 1;
                }else if(Integer.parseInt(message.getData("y")) == 1){
                    ClientApp.gameController.flag2 = 1;
                }
                else{
                    ClientApp.gameController.flag3 = 1;
                }
            }else if(Integer.parseInt(message.getData("x")) == 1){
                if(Integer.parseInt(message.getData("y")) == 0){
                    ClientApp.gameController.flag4 = 1;
                }
                else if(Integer.parseInt(message.getData("y")) == 1){
                    ClientApp.gameController.flag5 = 1;
                }
                else{
                    ClientApp.gameController.flag6 = 1;
                }
            }else{
                if(Integer.parseInt(message.getData("y")) == 0){
                    ClientApp.gameController.flag7 = 1;
                }
                else if(Integer.parseInt(message.getData("y"))==1){
                    ClientApp.gameController.flag1 = 8;
                }
                else{
                    ClientApp.gameController.flag9=1;
                }
            }
        });
    }
    private void handleGameOver(Message message) {
        //**ALERT**win msg **play again(GAME_REQ) **home scene.
        Platform.runLater(() -> {
            if(message.getData("line").equals("You lose !")||message.getData("line").equals("Draw !")){
                btns[Integer.parseInt(message.getData("x"))][Integer.parseInt(message.getData("y"))].setGraphic(new ImageView(IAmX?"/resources/images/o.png":"/resources/images/x.png"));
            }
        });
        String msg = message.getData("line");
        Platform.runLater(() -> {
            if(player2!=null&&player2.equals("computer")){
                Alert alert = new Alert(AlertType.CONFIRMATION, msg, new ButtonType("Play again", ButtonData.OK_DONE), new ButtonType("cancel", ButtonData.NO));
                alert.setTitle("Game over");
                alert.showAndWait();
                if (alert.getResult().getButtonData() == ButtonData.OK_DONE) {
                    for(int i=0;i<3;i++){
                        for(int j=0;j<3;j++){
                            btns[i][j].setGraphic(new ImageView("/resources/images/empty.png"));
                        }
                    }
                    playWithAI();
                }else{
                    for(int i=0;i<3;i++){
                        for(int j=0;j<3;j++){
                            btns[i][j].setGraphic(new ImageView("/resources/images/empty.png"));
                        }
                    }
                    ClientApp.primaryStage.hide();
                    ClientApp.primaryStage.setScene(client.ClientApp.home);
                    ClientApp.primaryStage.show();
                }
            }else{
                Alert alert = new Alert(AlertType.INFORMATION, msg, new ButtonType("Ok", ButtonData.OK_DONE));
                alert.setTitle("Game over");
                alert.setHeaderText("Game over");
                alert.setContentText(msg);
                alert.showAndWait();
                if (alert.getResult().getButtonData() == ButtonData.OK_DONE){
                    for(int i=0;i<3;i++){
                        for(int j=0;j<3;j++){
                            btns[i][j].setGraphic(new ImageView("/resources/images/empty.png"));
                        }
                    }
                    ClientApp.primaryStage.hide();
                    ClientApp.primaryStage.setScene(client.ClientApp.home);
                    ClientApp.primaryStage.show();
                }
            }
        });
        myTurn=false;
    }
    public String getOpponentName(){
        if(player2 == null)
            return player1;
        return player2;
    }
}