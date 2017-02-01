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
import static server.network.Server.allPlayers;

/**
 *
 * @author Ehab
 */
public class Session extends Thread{
    public static HashMap<Integer,Session> connectedPlayers = new HashMap<Integer,Session>();
    private Player player;
    private boolean connected = false;
    private boolean loggedIn = false;
    private Socket socket;
    private ObjectInputStream downLink;
    private ObjectOutputStream upLink;
    
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
            socket.close();
            upLink.close();
            downLink.close();
        }catch(IOException ioex){
            //error connection already closed
        }
    }
    private void playerLogin(String username, String password){
        Message loginResult = new Message(MsgType.LOGIN);
        boolean playerAuth = model.Players.playerAuth(username, password);
        if(playerAuth){
            loggedIn = true;
            player = model.Players.getPlayerInfo(username);
            loginResult.setData("signal", MsgSignal.SUCCESS);
            loginResult.setData("id", String.valueOf(player.getID()));
            loginResult.setData("username", player.getUsername());
            loginResult.setData("fullname", player.getFullName());
            Server.allPlayers.get(player.getUsername()).setStatus(Status.ONLINE);
            for(Map.Entry<String, Player> entry : allPlayers.entrySet()){
                System.out.println(entry.getValue().getUsername()+" - "+entry.getValue().getStatus());
            }
        }else{
            loginResult.setData("signal", MsgSignal.FAILURE);
        }
        SendMessage(loginResult);
    }
    private void playerLogout(){
        loggedIn = false;
        connected = false;
        connectedPlayers.remove(this);
        Server.allPlayers.get(player.getUsername()).setStatus(Status.OFFLINE);
        closeConnection();
    }
    private void MessageHandler(Message message){
        switch(message.getType()){
            case LOGIN:
                playerLogin(message.getData("username"), message.getData("password"));
                break;
            case LOGOUT:
                if(loggedIn)
                    playerLogout();
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
}
