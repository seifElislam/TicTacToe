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
    Player player = new Player();

    public static HashMap<String, Player> getAllPlayers() {
   

        HashMap<String, Player> hashmap = new HashMap<String, Player>();

        try {
            Statement stmt = conn.createStatement();
            String queryString = new String("select * from players");
            ResultSet rs = stmt.executeQuery(queryString);

            while (rs.next()) {

                Player p = new Player(rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5), rs.getString(6), rs.getString(7));
                hashmap.put(rs.getString(2), p);

            }

            stmt.close();
            Obj.CloseConnection(conn);

        } catch (SQLException ex) {

        }

        return hashmap;
    }

    public static boolean PlayerExisted(String username) {
        try {

            Statement stmt = conn.createStatement();

            String queryString = "select * from players where username ='" + username + "'";
            ResultSet rs = stmt.executeQuery(queryString);
            if (rs.next()) {
                return true;
            }

            stmt.close();
            Obj.CloseConnection(conn);

        } catch (SQLException ex) {
            Logger.getLogger(TicTacToeDataBase.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;

    }

    public static boolean PlayerAuth(String username, String password) {
       
        if(PlayerExisted(username)){
        System.out.println("username already exists ");
        return false ;
        
        }
        else{        try {

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
    }
    public static boolean UpdateScore(String username, int score) {
        try {

            Statement stmt = conn.createStatement();
            String queryString = new String("UPDATE `players` SET `score`='" + score + "' WHERE username = '" + username + "' ");
            stmt.executeQuery(queryString);

            stmt.close();
            Obj.CloseConnection(conn);
            return true;

        } catch (SQLException ex) {
            Logger.getLogger(TicTacToeDataBase.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    public static synchronized boolean signUp(Player player) {

        try {

            Statement stmt = conn.createStatement();

            String queryString = "INSERT INTO `players` ( `fname`, `lname`, `username`, `score`, `password`, `picpath`) VALUES ('" + player.getFname() + "', '" + player.getLname() + "', '" + player.getUsername() + "', '" + player.getScore() + "', '" + player.getPassword() + "', '" + player.getPicPath() + "')";
            stmt.executeUpdate(queryString);

            stmt.close();

            Obj.CloseConnection(conn);
            return true;

        } catch (SQLException ex) {
            Logger.getLogger(TicTacToeDataBase.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    public String getpicpath(String username) {

        String picpath = null;
        try {
            Statement stmt = conn.createStatement();
            String queryString = new String("SELECT picpath FROM players WHERE username = '" + username + "'");
            ResultSet rs = stmt.executeQuery(queryString);

            while (rs.next()) {
                picpath = rs.getString(1);

            }

            stmt.close();
            conn.close();

        } catch (SQLException ex) {
            Logger.getLogger(TicTacToeDataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
//     System.out.println(picpath);
        return picpath;
    }

//     public static void main(String[] args)  {
////       if(PlayerAuth("sara","12345")){
////           System.out.println("truue");
////     }else{
////           System.out.println("alse");
////       }
//////    }
////Player p=new Player( "rahama", "mohamed","reham", 111, "12345", "img/icon.png");
////    signUp(p); 
//    
//HashMap<String, Player> hashmap=getAllPlayers();
//
//        
//  for (String name: hashmap.keySet()){
//
//            String key =name.toString();
//            String  value = hashmap.get(name).getLname();  
//            System.out.println(key + " " + value);  
//
//
//}
//     }
}
