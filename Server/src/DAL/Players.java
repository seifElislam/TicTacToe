/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.Player;
import server.TicTacToeDataBase;
import java.sql.Driver;

import java.sql.*;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ehab
 */
public class Players {

    public static DBConnection Obj = new DBConnection();
    static Connection conn = Obj.Connection();
Player player=new Player();
    public static Player getPlayerInfo(String username) {
        Player player = new Player();
        player.setID(1);
        player.setUsername(username);
        player.setFname("Ehab");
        player.setLname("Gamal");
        return player;
    }

    public static HashMap<Integer, Player> getAllPlayers() {
        //int id and player object
        return new HashMap<Integer, Player>();
    }

    public static boolean PlayerExisted(String username) {
        return false;
    }

    public static boolean PlayerAuth(String username, String password) {
        try {
           
            Statement stmt = conn.createStatement();

            String queryString = "select * from players where username ='" + username + "' and password='" + password + "'";
            ResultSet rs = stmt.executeQuery(queryString);
            if (rs.next()) {
                return true;
            }

            stmt.close();
            Obj.CloseConnection(conn);

        } catch (SQLException ex) {
            Logger.getLogger(Players.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;

    }

    public static boolean UpdatePlayer(Player player) {
        if (PlayerExisted(player.getUsername())) {
            return true; //update only user fname, lname, password, picpath
        }
        return false;
    }

    public static synchronized boolean signUp(Player player) {
        
        try {
            
            Statement stmt = conn.createStatement();

            

            
            
            
            String queryString = "INSERT INTO `players` ( `fname`, `lname`, `username`, `score`, `password`, `picpath`) VALUES ('"+player.getFname()+"', '"+player.getLname()+"', '"+player.getUsername()+"', '"+player.getScore()+"', '"+player.getPassword()+"', '"+player.getPicPath()+"')";
     stmt.executeUpdate(queryString);
        
         stmt.close();
     
           Obj.CloseConnection(conn);
           return true;
        
        } catch (SQLException ex) {
            Logger.getLogger(TicTacToeDataBase.class.getName()).log(Level.SEVERE, null, ex);
     return false;
        }
        
    }
    
     public static void main(String[] args)  {
//       if(PlayerAuth("sara","12345")){
//           System.out.println("truue");
//     }else{
//           System.out.println("alse");
//       }
//    }
Player p=new Player( "rahama", "mohamed","reham", 111, "12345", "img/icon.png");
    signUp(p); 
    
     }
     
     
    
}


