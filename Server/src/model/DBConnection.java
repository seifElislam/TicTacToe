/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Ehab
 */
public class DBConnection {
    private Connection connection;
    //ResultSet rs;
    public Connection Connection(){
          try {
                //rs = null;
                DriverManager.registerDriver(new com.mysql.jdbc.Driver());
                connection = DriverManager.getConnection("jdbc:mysql://localhost/TicTacToeDB?autoReconnect=true&useSSL=false", "root", "root");
          } catch (SQLException ex) {
                //throw sql exception
          }
    return connection;
    }
    
    /**
     *
     * @param connection
     */
    public void CloseConnection(Connection connection){
          try {
              connection.close();
          } catch (SQLException ex) {
              //throw sql exception
          }
    
    }
    
    
}
