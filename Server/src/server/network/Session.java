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
import javafx.application.Platform;
import javax.management.Notification;
import server.AIGame;
import server.AutoReply;
import server.Game;
import server.ServerApp;
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
    private AIGame aiGame;
    private static int moveNum=0;
    
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
            connected = false;
            upLink.close();
            downLink.close();
            socket.close();
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
            loginResult.setData("fname", player.getFname());
            loginResult.setData("lname", player.getLname());
            loginResult.setData("picpath", player.getPicPath());
            loginResult.setData("score", String.valueOf(player.getScore()));
            Server.allPlayers.get(player.getUsername()).setStatus(Status.ONLINE);
            this.connectedPlayers.put(player.getUsername(), this);
            SendMessage(loginResult);
            initConnection();
            pushNotification("status", Server.allPlayers.get(player.getUsername()).getStatus());
        }else{
            loginResult.setData("signal", MsgSignal.FAILURE);
            SendMessage(loginResult);
            connected = false;
        }
    }
    private void playerLogout(){
        connectedPlayers.remove(this);
        Server.allPlayers.get(player.getUsername()).setStatus(Status.OFFLINE);
        ServerApp.serverController.bindPlayersTable();
        pushNotification("status", Server.allPlayers.get(player.getUsername()).getStatus());
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
            case AIGAME_REQ :
                AIrequestGame();
                break;
            case MOVE:
                handleMove(message);
                break;
            case CHAT:
                chatHandler(message);
                break;
            default:
                SendMessage(new Message(MsgType.UNKNOWN));
                break;
        }
    }
    public void SendMessage(Message message){
        try{
            this.upLink.writeObject(message);
        }catch(IOException ioex){
            //error cannot send message to client
        }
    }
    public void chatHandler(Message message){
        connectedPlayers.get(message.getData("sender")).SendMessage(message);
        if(!message.getData("sender").equals(message.getData("receiver"))){
            if(connectedPlayers.containsKey(message.getData("receiver")))
                connectedPlayers.get(message.getData("receiver")).SendMessage(message);    
        }else{
            //message.setData("sender", "compo");
            //message.setData("text", "this is autoreply from computer");
            //System.out.println("inside else sender "+message.getData("sender")+" : "+message.getData("text"));
            Message autoReply = new Message(MsgType.CHAT);
            autoReply.setData("sender", "compo");
            autoReply.setData("text", AutoReply.getReply());
            connectedPlayers.get(message.getData("receiver")).SendMessage(autoReply);
        }
            
    }
    public void run(){
        while(connected){
            try{
                Message message = (Message)downLink.readObject();
                MessageHandler(message);
            }catch(IOException ioex){
                closeConnection();
            }catch(ClassNotFoundException cnfex){
                //error invalid message sent by client
            }
        }
    }

    private void playerRegister(String username, String password,String fname,String lname,String picpath){
        Message result = new Message(MsgType.REGISTER);
        if(!model.Players.playerExisted(username)){
            if(model.Players.signUp(fname,lname ,username,password, picpath)){
                result.setData("signal", MsgSignal.SUCCESS);
                Player newPlayer = new Player(fname, lname, username, 0, password, picpath);
                newPlayer.setStatus(Status.OFFLINE);
                broadcastNewPlayer(newPlayer);
                Server.allPlayers.put(username, newPlayer);
                ServerApp.serverController.bindPlayersTable();
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
            
            ServerApp.server.allPlayers.get(player.getUsername()).setStatus(Status.PLAYING);
            Session.connectedPlayers.get(player.getUsername()).pushNotification("status", Status.PLAYING);
            ServerApp.server.allPlayers.get(incoming.getData("destination")).setStatus(Status.PLAYING);
            Session.connectedPlayers.get(incoming.getData("destination")).pushNotification("status", Status.PLAYING);
            ServerApp.serverController.bindPlayersTable();
        }
    }
    public void respondGame(Message incoming){
        //handle response from client 2 and forward it to client1
        if(incoming.getData("response").equals("accept")){
                game=new Game(incoming.getData("destination"),player.getUsername());
                connectedPlayers.get(incoming.getData("destination")).game=game;
        }else{
            ServerApp.server.allPlayers.get(player.getUsername()).setStatus(Status.ONLINE);
            Session.connectedPlayers.get("destination").pushNotification("status", Status.ONLINE);
            ServerApp.server.allPlayers.get(player.getUsername()).setStatus(Status.ONLINE);
            Session.connectedPlayers.get("destination").pushNotification("status", Status.ONLINE);
            ServerApp.serverController.bindPlayersTable();
        }
        Message outgoing=new Message(MsgType.GAME_RES,"source",player.getUsername());
        outgoing.setData("response", incoming.getData("response"));
        if(connectedPlayers.containsKey(incoming.getData("destination"))){
            connectedPlayers.get(incoming.getData("destination")).SendMessage(outgoing);        
        }
    }
    private void AIrequestGame(){
        aiGame = new AIGame(player.getUsername());
        ServerApp.server.allPlayers.get(player.getUsername()).setStatus(Status.PLAYING);
            Session.connectedPlayers.get(player.getUsername()).pushNotification("status", Status.PLAYING);
            ServerApp.serverController.bindPlayersTable();
    }
    private void handleMove(Message message) {
         if(message.getData("target")!=null&&message.getData("target").equals("computer")){
             aiGame.takeMove(Integer.parseInt(message.getData("x")), Integer.parseInt(message.getData("y")));
         }else{
            if(game.validateMove(player.getUsername(), Integer.parseInt(message.getData("x")), Integer.parseInt(message.getData("y")))){
            
                switch (game.checkForWin(player.getUsername(), Integer.parseInt(message.getData("x")), Integer.parseInt(message.getData("y")))){
                    case "gameOn":
                        if(game.incMove%2==0){
                            connectedPlayers.get(game.getPlayer1()).SendMessage(message);
                           
                           
                        }else{
                            connectedPlayers.get(game.getPlayer2()).SendMessage(message);
                            
                          
                        }
                    
                        break;
                    case "win" :
                        SendMessage(new Message(MsgType.GAME_OVER,"line","You win !"));
                        Message lose=new Message(MsgType.GAME_OVER,"line","You lose !");
                        String username=player.getUsername();
                        model.Players.updateScoreWin(username);
                        ServerApp.server.allPlayers.get(this.player.getUsername()).setScore(ServerApp.server.allPlayers.get(this.player.getUsername()).getScore()+10);
                        ServerApp.serverController.bindPlayersTable();
                        lose.setData("x", message.getData("x"));
                        lose.setData("y", message.getData("y"));
                        connectedPlayers.get(game.incMove%2==1?game.getPlayer1():game.getPlayer2()).SendMessage(lose);
                        
                        ServerApp.server.allPlayers.get(game.getPlayer1()).setStatus(Status.ONLINE);
                        Session.connectedPlayers.get(game.getPlayer1()).pushNotification("status", Status.ONLINE);
                        ServerApp.server.allPlayers.get(game.getPlayer2()).setStatus(Status.ONLINE);
                        Session.connectedPlayers.get(game.getPlayer2()).pushNotification("status", Status.ONLINE);
                        ServerApp.serverController.bindPlayersTable();
                        game=null;
                        break;
                        
                    case "draw":
                        
                        SendMessage(new Message(MsgType.GAME_OVER,"line","Draw !"));
                        Message draw=new Message(MsgType.GAME_OVER,"line","Draw !");
                         String username2=player.getUsername();
                        model.Players.updateScoreDraw(username2);
                        ServerApp.server.allPlayers.get(this.player.getUsername()).setScore(ServerApp.server.allPlayers.get(this.player.getUsername()).getScore()+5);
                        ServerApp.serverController.bindPlayersTable();
                        draw.setData("x", message.getData("x"));
                        draw.setData("y", message.getData("y"));
                        connectedPlayers.get(game.incMove%2==1?game.getPlayer1():game.getPlayer2()).SendMessage(draw);
                        ServerApp.server.allPlayers.get(game.getPlayer1()).setStatus(Status.ONLINE);
                        Session.connectedPlayers.get(game.getPlayer1()).pushNotification("status", Status.ONLINE);
                        ServerApp.server.allPlayers.get(game.getPlayer2()).setStatus(Status.ONLINE);
                        Session.connectedPlayers.get(game.getPlayer2()).pushNotification("status", Status.ONLINE);
                        ServerApp.serverController.bindPlayersTable();
                        game=null;
                        break;
                }
            }
        

        }
    }
    public void pushNotification(String key, String value){
        for(Map.Entry<String, Session> session : connectedPlayers.entrySet()){
            Message notification = new Message(MsgType.NOTIFY);
            notification.setData("username", player.getUsername());
            notification.setData("key", key);
            notification.setData("value", value);
            session.getValue().SendMessage(notification);
        }
        ServerApp.serverController.bindPlayersTable();
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
    private void broadcastNewPlayer(Player newPlayer){
        for(Map.Entry<String, Session> session : connectedPlayers.entrySet()){
            Message message = new Message(MsgType.INIT);
            message.setData("username", newPlayer.getUsername());
            message.setData("score", String.valueOf(newPlayer.getScore()));
            message.setData("picpath", newPlayer.getPicPath());
            message.setData("status", newPlayer.getStatus());
            session.getValue().SendMessage(message);
        }
    }
}
