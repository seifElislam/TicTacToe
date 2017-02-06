/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.network;

import assets.Message;
import assets.MsgType;
import server.*;
import server.network.Session;
import model.Player;
import java.io.IOException;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import server.network.Session;
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

    public Server(){
//        //this.portNumber = portNumber;
//        running = runServer();
//        if(running){
//            System.out.println("Server started successfully!");
//            for(Map.Entry<String, Player> entry : allPlayers.entrySet()){
//                System.out.println(entry.getValue().getUsername()+" - "+entry.getValue().getStatus());
//            }
//            startCommunication();
//        }else
//            System.out.println("Server failed to start");
    }
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
        for(Map.Entry<String, Session> session : connectedPlayers.entrySet()){
            Message notification = new Message(MsgType.TERM);
            session.getValue().SendMessage(notification);
        }
        try{
            serverSocket.close();
        }catch(IOException ioex){
            
        }
    }
    //TODO move this function to new thread
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
