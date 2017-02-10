/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.network;

import assets.Message;
import assets.MsgType;
import model.Player;
import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import static server.network.Session.connectedPlayers;

/**
 *
 * @author Ehab
 */
public class Server {
    public static HashMap<String,Player> allPlayers = model.Players.getAllPlayers();
    private int portNumber;
    private ServerSocket serverSocket;
    private Socket socket;
    public boolean running = false;

    public Server(){}
    private boolean runServer(){
        try{
            serverSocket = new ServerSocket(portNumber);
            return true;
        }catch(IOException ex){
            return false;
        }
    }
    public boolean startServer(int portNumber){
        this.portNumber = portNumber;
        running = runServer();
        if(running)
            startCommunication();
        return running;
    }
    public void stopServer(){
        running = false;
        Message notification = new Message(MsgType.TERM);
        for(Map.Entry<String, Session> session : connectedPlayers.entrySet()){
            session.getValue().sendMessage(notification);
        }
        try{
            serverSocket.close();
        }catch(IOException ioex){
        }
    }
    private void startCommunication(){
        new Thread(()->{
            while(running){
                try{
                    socket = serverSocket.accept();
                    new Session(socket);
                }catch(IOException ioex){
                    //error cannot accept connections anymore - limit exceeded
                }
            }
        }).start();
    }
}
