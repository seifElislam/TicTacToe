/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import assets.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

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
        Message loginResult = new Message(MsgType.AUTH_RES);
        boolean playerAuth = DAL.Players.PlayerAuth(username, password);
        if(playerAuth){
            loggedIn = true;
            //player = DAL.Players.getPlayerInfo(username);
            loginResult.setData("signal", MsgSignal.SUCCESS);
            loginResult.setData("id", String.valueOf(player.getID()));
            loginResult.setData("username", player.getUsername());
            loginResult.setData("fullname", player.getFullName());
        }else{
            loginResult.setData("signal", MsgSignal.FAILURE);
        }
        SendMessage(loginResult);
    }
    private void playerLogout(){
        loggedIn = false;
        connected = false;
        connectedPlayers.remove(this);
        //allPlayers.get(player.getID()).setStatus("offline");
        closeConnection();
    }
    private void MessageHandler(Message message){
        switch(message.getType()){
            case MsgType.AUTH_REQ:
                playerLogin(message.getData("username"), message.getData("password"));
                break;
            case MsgType.LOGOUT:
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
