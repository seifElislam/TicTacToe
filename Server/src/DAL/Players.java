/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAL;

import java.util.HashMap;
import server.Player;

/**
 *
 * @author Ehab
 */
public class Players {
    public static DBConnection conn = new DBConnection();
    public static Player getPlayerInfo(String username){
        Player player = new Player();
        player.setID(1);
        player.setUsername(username);
        player.setFname("Ehab");
        player.setLname("Gamal");
        return player;
    }
    public static HashMap<Integer,Player> getAllPlayers(){
        //int id and player object
        return new HashMap<Integer,Player>();
    }
    public static boolean PlayerExisted(String username){
        return false;
    }
    public static boolean PlayerAuth(String username, String password){
        //if(PlayerExisted(username))
        //    return true; //if existed check for username and pass validation
        //return false;
        if(username.equals("ehab") && password.equals("password"))
            return true;
        return false;
    }
    public static boolean UpdatePlayer(Player player){
        if(PlayerExisted(player.getUsername()))
            return true; //update only user fname, lname, password, picpath
        return false;
    }
    public static synchronized boolean InsertPlayer(Player player){
        if(!PlayerExisted(player.getUsername()))
            return true; //insert new player if not existed already
        return false;
    }
}