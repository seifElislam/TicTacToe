/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import assets.Status;
import java.sql.*;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Amira
 */
public class Players {

    public static DBConnection Obj = new DBConnection();
    public static HashMap<String, Player> getAllPlayers() {

        HashMap<String, Player> hashmap = new HashMap<>();
        try {
            Connection conn = Obj.Connection();
            Statement stmt = conn.createStatement();
            String queryString = "select * from players";
            ResultSet rs = stmt.executeQuery(queryString);
            while (rs.next()) {
                Player p = new Player(rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5), rs.getString(6), rs.getString(7));
                p.setStatus(Status.OFFLINE);
                hashmap.put(rs.getString("username"), p);
            }
            stmt.close();
            Obj.CloseConnection(conn);
        } catch (SQLException ex) {
        }
        return hashmap;
    }
    public static Player getPlayerInfo(String username) {
        Player player = new Player();
        try {
            Connection conn = Obj.Connection();
            Statement stmt = conn.createStatement();
            String queryString = "SELECT * FROM players WHERE username = '" + username + "'";
            ResultSet rs = stmt.executeQuery(queryString);
            while (rs.next()) {
                player.setID(rs.getInt("id"));
                player.setFname(rs.getString("fname"));
                player.setLname(rs.getString("lname"));
                player.setUsername(rs.getString("username"));
                player.setScore(rs.getInt("score"));
                player.setPicPath(rs.getString("picpath"));
            }
            stmt.close();
            Obj.CloseConnection(conn);
        } catch (SQLException ex) {
        }
        return player;
    }
    public static boolean playerExisted(String username) {
        try {
            Connection conn = Obj.Connection();
            Statement stmt = conn.createStatement();
            String queryString = "select * from players where username ='" + username + "'";
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
    public static boolean playerAuth(String username, String password) {
        boolean validAuth = false;
        if (playerExisted(username)) {
            try {
                Connection conn = Obj.Connection();
                Statement stmt = conn.createStatement();
                String queryString = "select * from players where username ='" + username + "' and password='" + password + "'";
                ResultSet rs = stmt.executeQuery(queryString);
                if (rs.next()) {
                    validAuth = true;
                }
                stmt.close();
                Obj.CloseConnection(conn);
            } catch (SQLException ex) {
                Logger.getLogger(Players.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return validAuth;
    }
    public static boolean updateScore(String username, int score) {
        try {
            Connection conn = Obj.Connection();
            Statement stmt = conn.createStatement();
            String queryString = "UPDATE `players` SET `score`='" + score + "' WHERE username = '" + username + "' ";
            stmt.executeQuery(queryString);
            stmt.close();
            Obj.CloseConnection(conn);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(Players.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    public static synchronized boolean signUp(String fname,String lname ,String username,String password,String picpath) {
        try {
            Connection conn = Obj.Connection();
            Statement stmt = conn.createStatement();
            String queryString = "INSERT INTO `players` ( `fname`, `lname`, `username`, `score`, `password`, `picpath`) VALUES ('" +fname + "', '" + lname + "', '" + username + "', '" + 0 + "', '" + password + "', '" + picpath + "')";
            stmt.executeUpdate(queryString);
            stmt.close();
            Obj.CloseConnection(conn);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(Players.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    public String getpicpath(String username) {

        String picpath = null;
        try {
            Connection conn = Obj.Connection();
            Statement stmt = conn.createStatement();
            String queryString = "SELECT picpath FROM players WHERE username = '" + username + "'";
            ResultSet rs = stmt.executeQuery(queryString);
            while (rs.next()) {
                picpath = rs.getString(1);
            }
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(Players.class.getName()).log(Level.SEVERE, null, ex);
        }
        return picpath;
    }
}
