/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

/**
 *
 * @author Ehab
 */
public class Server extends Application {
    public static HashMap<Integer,Player> allPlayers = DAL.Players.getAllPlayers();
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
            }catch(IOException ioex){
                //error cannot accept connections anymore - limit exceeded
            }
    	}
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //launch(args);
        Scanner scanner = new Scanner(System.in);
        System.out.print("Server port: ");
        int portNumber = scanner.nextInt();
        new Server(portNumber,999);
    }
    
}
