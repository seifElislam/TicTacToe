/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.network;

import model.Player;
import assets.*;
import assets.MsgType;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import server.Game;
import static server.network.Server.allPlayers;

/**
 *
 * @author Ehab
 */
public class Session extends Thread{
    public static HashMap<String,Session> connectedPlayers = new HashMap<String,Session>();
    private Player player;
    private boolean connected = false;
    private Socket socket;
    private ObjectInputStream downLink;
    private ObjectOutputStream upLink;
    private Game game;
    
    public Session(Socket socket){
        this.socket = socket;
        connected = openConnection();
        start();
    }
    private boolean openConnection(){
        try{
            downLink = new ObjectInputStream(socket.getInputStream());
            upLink = new ObjectOutputStream(socket.getOutputStream());
            return true;
        }catch(IOException ex){
            //error server cannot connect to client
            return false;
        }
    }
    private void closeConnection(){
        try{
            upLink.close();
            downLink.close();
            socket.close();
            connected = false;
        }catch(IOException ioex){
            //error connection already closed
        }
    }
    private void playerLogin(Message message){
        Message loginResult = new Message(MsgType.LOGIN);
        boolean playerAuth = model.Players.playerAuth(message.getData("username"), message.getData("password"));
        if(playerAuth){
            player = model.Players.getPlayerInfo(message.getData("username"));
            loginResult.setData("signal", MsgSignal.SUCCESS);
            loginResult.setData("id", String.valueOf(player.getID()));
            loginResult.setData("username", player.getUsername());
            loginResult.setData("fullname", player.getFullName());
            loginResult.setData("picpath", player.getPicPath());
            loginResult.setData("score", String.valueOf(player.getScore()));
            Server.allPlayers.get(player.getUsername()).setStatus(Status.ONLINE);
            initConnection();
            pushNotification();
        }else{
            loginResult.setData("signal", MsgSignal.FAILURE);
            connected = false;
        }
        SendMessage(loginResult);
    }
    private void playerLogout(){
        connectedPlayers.remove(this);
        Server.allPlayers.get(player.getUsername()).setStatus(Status.OFFLINE);
        pushNotification();
        closeConnection();
    }
    private void MessageHandler(Message message){
        switch(message.getType()){
            case LOGIN:
                playerLogin(message);
                break;
            case LOGOUT:
                playerLogout();
                break;
            case REGISTER : 
                playerRegister(message.getData("username"), message.getData("password"),message.getData("fname"),message.getData("lname"),message.getData("picpath"));
            break;
            case GAME_REQ :
                requestGame(message);
            break;
            case GAME_RES :
                respondGame(message);
            break;
            default:
                SendMessage(new Message(MsgType.UNKNOWN));
                break;
        }
    }
    private void SendMessage(Message message){
        try{
            this.upLink.writeObject(message);
        }catch(IOException ioex){
            //error cannot send message to client
        }
    }
    public void run(){
        while(connected){
            try{
                Message message = (Message)downLink.readObject();
                MessageHandler(message);
            }catch(IOException ioex){
                //error server lost connection with client
            }catch(ClassNotFoundException cnfex){
                //error invalid message sent by client
            }
        }
    }

    private void playerRegister(String username, String password,String fname,String lname,String picpath){
        Message result = new Message(MsgType.REGISTER);
        boolean playerexists = model.Players.playerExisted(username);
        if(!playerexists)
        {
            
        if(  model.Players.signUp(fname,lname ,username,password, picpath)) 
            
        {
            result.setData("signal", MsgSignal.SUCCESS);
          
            }
        }
        else{
           result.setData("signal", MsgSignal.FAILURE);}
        
        SendMessage(result);
   
    }
    
    public void requestGame(Message incoming){
        //handle request from client 1 and forward it to client2
        Message outgoing=new Message(MsgType.GAME_REQ,"source",player.getUsername());
        if(connectedPlayers.containsKey(incoming.getData("destination"))){
            connectedPlayers.get(incoming.getData("destination")).SendMessage(outgoing);        
        }
    }
    public void respondGame(Message incoming){
        //handle response from client 2 and forward it to client1
        if(incoming.getData("response").equals("accept")){
                game=new Game();
                connectedPlayers.get(incoming.getData("destination")).game=game;
        }
        Message outgoing=new Message(MsgType.GAME_RES,"source",player.getUsername());
        outgoing.setData("response", incoming.getData("response"));
        if(connectedPlayers.containsKey(incoming.getData("destination"))){
            connectedPlayers.get(incoming.getData("destination")).SendMessage(outgoing);        
        }
    }
    
    private void pushNotification(){
        for(Map.Entry<String, Session> session : connectedPlayers.entrySet()){
            session.getValue().SendMessage(new Message(MsgType.NOTIFY, player.getUsername(), player.getStatus()));
        }
    }
    private void initConnection(){
        for(Map.Entry<String, Player> player : allPlayers.entrySet()){
            Message message = new Message(MsgType.INIT);
            message.setData("username", player.getValue().getUsername());
            message.setData("score", String.valueOf(player.getValue().getScore()));
            message.setData("picpath", player.getValue().getPicPath());
            message.setData("status", player.getValue().getStatus());
            this.SendMessage(message);
        }
    }
}
