/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.network;

import assets.*;
import client.controllers.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author Ehab
 */
public class Session {
    private Socket socket;
    private final int portNumber;
    private final String ipAddress;
    private ObjectInputStream downLink;
    private ObjectOutputStream upLink;
    public boolean connected = false;
    private boolean loggedin = false;
    private boolean playing = false;
    
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
            case LOGIN:
                if(message.getData("signal").equals(MsgSignal.SUCCESS)){
                    System.out.println("login to server succedded");
                    System.out.println("ID: "+message.getData("id"));
                    System.out.println("username: "+message.getData("username"));
                    System.out.println("Fullname: "+message.getData("fullname"));
                    loggedin = true;
                }
                else{
                    System.out.println("login to server failed");
                    connected = false;
                }
                break;    
            case REGISTER:
                if(message.getData("signal").equals(MsgSignal.SUCCESS)){
                    System.out.println("register  succedded");
         
                 
                  
                }
                else{
                    System.out.println("register failes to server failed");
                    connected = false;
                }
                break;
            case INIT:
                System.out.println(message.getData("username")+" "+message.getData("status"));
                break;
            case NOTIFY:
                System.out.println(message.getData("username")+" became "+message.getData("status"));
                break;
            case GAME_REQ:
                respondToRequest(message);
                break;
            case GAME_RES:
                handleResponse(message);
                break;
            default:
                System.out.println("server sent unhandled message");
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

    public void signUpToServer(String fname, String lname, String username, String password, String picpath) {
     Message message = new Message(MsgType.REGISTER);
        message.setData("username", username);
        message.setData("password", password);
        message.setData("fname",fname);
        message.setData("lname",lname);
        message.setData("picpath",picpath);
       
        sendMessage(message);
    }
    public void requestGame(String secondPlayerName){
        //**ALERT** waiting for other player response with cancel button
        Message message=new Message(MsgType.GAME_REQ,"destination",secondPlayerName);
        sendMessage(message);
    }
    public void respondToRequest(Message incoming){
        //**Alert** with the request from **playerRequestingGame** returns boolean **accept**
        String playerRequestingGame =incoming.getData("source");        
        boolean accept=true;       
        Message outgoing=new Message(MsgType.GAME_RES,"destination",playerRequestingGame);
        if(accept){
            outgoing.setData("response", "accept");
            //set scene to game scene
        }else{
            outgoing.setData("response", "deny");
        }
        sendMessage(outgoing);
    }
    public void handleResponse(Message incoming){
        if(incoming.getData("response").equals("accept")){
            //change scene to gameplay scene
        }else{
            System.out.println("player 2 denied game request");
        }
    }
}
