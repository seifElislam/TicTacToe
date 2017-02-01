/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.network;

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

/**
 *
 * @author Ehab
 */
public class Server {
    public static HashMap<String,Player> allPlayers = model.Players.getAllPlayers();
    private final int portNumber;
    private final int maxConnections;
    private ServerSocket serverSocket;
    private Socket socket;
    private boolean running = false;

    public Server(int portNumber, int maxConnections){
        this.portNumber = portNumber;
        this.maxConnections = maxConnections;
        running = runServer();
        if(running){
            System.out.println("Server started successfully!");
            for(Map.Entry<String, Player> entry : allPlayers.entrySet()){
                System.out.println(entry.getValue().getUsername()+" - "+entry.getValue().getStatus());
            }
            startCommunication();
        }else
            System.out.println("Server failed to start");
    }
    public boolean runServer(){
        try{
            serverSocket = new ServerSocket(portNumber, maxConnections);
            return true;
        }catch(IOException ex){
            return false;
        }
    }
    //TODO move this function to new thread
    public void startCommunication(){
    	while(running && Session.connectedPlayers.size()<maxConnections){
            try{
                socket = serverSocket.accept();
                new Session(socket);
                System.out.print("New client connected @ ");
                System.out.println(new SimpleDateFormat("yyy/MM/dd HH:mm:ss").format(new Date()));
                for(Map.Entry<String, Player> entry : allPlayers.entrySet()){
                    System.out.println(entry.getValue().getUsername()+" - "+entry.getValue().getStatus());
                }
            }catch(IOException ioex){
                //error cannot accept connections anymore - limit exceeded
            }
    	}
    }
    
}
