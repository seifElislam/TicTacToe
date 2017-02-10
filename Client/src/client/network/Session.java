    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.network;

import assets.*;
import client.*;
import client.controllers.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
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
    private Button[][] btns={
                {ClientApp.gameController.b1,ClientApp.gameController.b2,ClientApp.gameController.b3},
                {ClientApp.gameController.b4,ClientApp.gameController.b5,ClientApp.gameController.b6},
                {ClientApp.gameController.b7,ClientApp.gameController.b8,ClientApp.gameController.b9}};
//    private int[][] btnFlags={
//                {ClientApp.gameController.flag1,ClientApp.gameController.flag2,ClientApp.gameController.flag3},
//                {ClientApp.gameController.flag4,ClientApp.gameController.flag5,ClientApp.gameController.flag6},
//                {ClientApp.gameController.flag7,ClientApp.gameController.flag8,ClientApp.gameController.flag9}};
//    
    
    public Session(String ipAddress, int portNumber){
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
        //openConnection();
    }
    public void openConnection(){
        try {
            socket = new Socket(ipAddress, portNumber);
            upLink = new ObjectOutputStream(socket.getOutputStream());
            downLink = new ObjectInputStream(socket.getInputStream());
            connected = true;
            System.out.println("Connected to server successfully!");
            //startCommunication();
        } catch (IOException ex) {
            connected = false;
            System.out.println("Connection to server failed!");
        }
    }
    public void closeConnection(){
        sendMessage(new Message(MsgType.LOGOUT));
        connected = false;
        try {
            upLink.close();
            downLink.close();
            socket.close();
            System.out.println("Connection to server closed successfully!");
        } catch (IOException ex) {
            System.out.println("Connection to server already closed!");
        }
    }
    private void startCommunication(){
        new Thread(() -> {
            while(connected){
                try {
                    Message message = (Message) downLink.readObject();
                    MessageHandler(message);
                } catch (IOException ex) {
                    connected = false;
                    System.out.println("input stream failed!");
                    break;
                } catch(ClassNotFoundException cnfex){
                    System.out.println("Class not found exception");
                }
            }
            try{
                socket.close();
                downLink.close();
                upLink.close();
            }catch(IOException ex)
            {
                System.out.println("Client cannot close I/O stream");
            }
            System.out.println("Connection with server closed");
        }).start();
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
                        MessageHandler(response);
                }catch(IOException ioex){
                    
                }catch(ClassNotFoundException cnfex){
                    
                }
            }
        }
        return loggedin;
    }
    private void MessageHandler(Message message){
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
                System.out.println("server sent unhandled message "+message.getType());
                break;
        }
    }
    private void sendMessage(Message message){
        try{
            upLink.writeObject(message);
        }catch(IOException ioex){
            ioex.printStackTrace();
            System.out.println("Cannot send message to server");
        }
    }

    public boolean signUpToServer(String fname, String lname, String username, String password, String picpath) {
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
    public void requestGame(String secondPlayerName){
        //**ALERT** waiting for other player response with cancel button
        Message message=new Message(MsgType.GAME_REQ,"destination",secondPlayerName);
        sendMessage(message);
    }
    public void respondToRequest(Message incoming){
        //**Alert** with the request from **playerRequestingGame** returns boolean **accept**
        player1=incoming.getData("source");        
//        Message outgoing=new Message(MsgType.GAME_RES,"destination",playerRequestingGame);
//        System.out.println("client request game");
        Platform.runLater(new Runnable(){
           public void run(){
               ClientApp.homeController.showAlert(player1);
           }});
//        outgoing.setData("response","accept");
//        sendMessage(outgoing);
    }
    public void sendResponse(boolean response){
                IAmX=false;
        Message outgoing=new Message(MsgType.GAME_RES,"destination",player1);
        outgoing.setData("response",response?"accept":"deny");
        sendMessage(outgoing);
        
    }
    
    public void handleResponse(Message incoming){
        if(incoming.getData("response").equals("accept")){
            
            IAmX=true;         
            myTurn=true;
            player2=incoming.getData("source");
             //incoming.setData("target", player2);
            Platform.runLater(() -> {
                ClientApp.primaryStage.setScene(client.ClientApp.game);
                ClientApp.gameController.resetScene();
                ClientApp.gameController.img = new Image(Session.this.getClass().getResourceAsStream("/resources/images/x.png"));
            });
        }else{
            System.out.println("player 2 denied game request");
        }
    }
    
    public void playWithAI(){
        ClientApp.gameController.resetScene();
        sendMessage(new Message(MsgType.AIGAME_REQ));
        player1=player.getUsername();
        player2="computer";
        IAmX=true;
        myTurn=true;
        
        ClientApp.gameController.img = new Image(getClass().getResourceAsStream("/resources/images/x.png"));
    }
    public void makeAMove(String x,String y) {
        myTurn=false;
        Message message=new Message(MsgType.MOVE);
        message.setData("x", x);
        message.setData("y", y);
            
        message.setData("target", player2);
//      message.setData("target", player1)
        sendMessage(message);
        
        
    }
    private void handleMove(Message message) {
        
        myTurn=true;
        System.out.println("client recieved a move");
        Platform.runLater(new Runnable(){
            public void run(){
                btns[Integer.parseInt(message.getData("x"))][Integer.parseInt(message.getData("y"))].setGraphic(new ImageView(IAmX?"/resources/images/o.png":"/resources/images/x.png"));
                if(Integer.parseInt(message.getData("x"))==0){
                    if(Integer.parseInt(message.getData("y"))==0){ClientApp.gameController.flag1=1;}
                    else if(Integer.parseInt(message.getData("y"))==1){ClientApp.gameController.flag2=1;}
                    else{ClientApp.gameController.flag3=1;}
                }else if(Integer.parseInt(message.getData("x"))==1){
                    if(Integer.parseInt(message.getData("y"))==0){ClientApp.gameController.flag4=1;}
                    else if(Integer.parseInt(message.getData("y"))==1){ClientApp.gameController.flag5=1;}
                    else{ClientApp.gameController.flag6=1;}
                }else{
                    if(Integer.parseInt(message.getData("y"))==0){ClientApp.gameController.flag7=1;}
                    else if(Integer.parseInt(message.getData("y"))==1){ClientApp.gameController.flag1=8;}
                    else{ClientApp.gameController.flag9=1;}
                }
//                System.out.println(ClientApp.gameController.flag1+""+ClientApp.gameController.flag2+""+ClientApp.gameController.flag3+""+ClientApp.gameController.flag4+""+ClientApp.gameController.flag5+""+ClientApp.gameController.flag6+""+ClientApp.gameController.flag7+""+ClientApp.gameController.flag8+""+ClientApp.gameController.flag9);
            }
        });
        
    }
    
    private void handleGameOver(Message message) {
        //add score
        //**ALERT**win msg **play again(GAME_REQ) **home scene.
        
        Platform.runLater(new Runnable(){
           public void run(){
               if(message.getData("line").equals("You lose !")||message.getData("line").equals("Draw !")){
                   btns[Integer.parseInt(message.getData("x"))][Integer.parseInt(message.getData("y"))].setGraphic(new ImageView(IAmX?"/resources/images/o.png":"/resources/images/x.png"));
               }
        }});        
        String msg=message.getData("line");
        
        
        Platform.runLater(new Runnable(){
            public void run(){
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
                        ClientApp.primaryStage.setScene(client.ClientApp.home);
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
                        ClientApp.primaryStage.setScene(client.ClientApp.home);
                    }
                }
            }});
//        player1=null;
//        player2=null;
        myTurn=false;
        
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
                System.out.println("init "+message.getData("username"));
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
    public String getOpponentName(){
        if(player2 == null)
            return player1;
        return player2;
    }
    public void terminateConnection(){
        closeConnection();
        Platform.runLater(() -> {
            ClientApp.primaryStage.setScene(ClientApp.signIn);
            ClientApp.loginController.terminateConnectino();
        });
    }
}
