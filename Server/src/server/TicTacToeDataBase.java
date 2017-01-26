/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author amira
 */
package server;
import java.sql.Driver;

import java.sql.*;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TicTacToeDataBase {

    Connection con;
    Vector<Players> playersvector = new Vector<>();
    ResultSet rs;
   Players player =new Players();

    public static void main(String[] args) throws SQLException {
        new TicTacToeDataBase();
    }

    public TicTacToeDataBase() {


    }
    
    
public boolean signin(String username,String password){
  rs=null;
    try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/TicTacToeDB", "amira", "Amera_123");

            Statement stmt = con.createStatement();

            

            
            
            
            String queryString = "select * from players where username ='"+username+"' and password='"+password+"'";
        rs = stmt.executeQuery(queryString);
        if (rs.next()){return true;}
        
         stmt.close();
           con.close();
        
        } catch (SQLException ex) {
            Logger.getLogger(TicTacToeDataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    return false;
    
    
}
 public void  signup(String fname,String lname,String username,int score,String password,String picpath)
 {
        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/TicTacToeDB", "amira", "Amera_123");

            Statement stmt = con.createStatement();

            

            
            
            
            String queryString = "INSERT INTO `players` ( `fname`, `lname`, `username`, `score`, `password`, `picpath`) VALUES ('"+fname+"', '"+lname+"', '"+username+"', '"+score+"', '"+password+"', '"+picpath+"')";
     stmt.executeUpdate(queryString);
        
         stmt.close();
           con.close();
        
        } catch (SQLException ex) {
            Logger.getLogger(TicTacToeDataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
         
 }
  public void  modifyscore(String username,int score)
 {
 
     
     
       rs = null;
     try {
         DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/TicTacToeDB", "amira", "Amera_123");

            Statement stmt = con.createStatement();
            String queryString = new String("UPDATE `players` SET `score`='"+score+"' WHERE username = '"+username+"' ");
            rs = stmt.executeQuery(queryString);
            
          
          
                 stmt.close();
           con.close(); 
            
        } catch (SQLException ex) {
            Logger.getLogger(TicTacToeDataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
     
     
     
 }
  public Vector<Players> getallplayers()
  {        rs = null;
     try {DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/TicTacToeDB", "amira", "Amera_123");

            Statement stmt = con.createStatement();
            String queryString = new String("select * from players");
            rs = stmt.executeQuery(queryString);
            
            while (rs.next()) {
                Players p;
                p = new Players(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5), rs.getString(6), rs.getString(7));
               playersvector.add(p);
                
            }
          
                 stmt.close();
           con.close(); 
            
        } catch (SQLException ex) {
           
        }
  return playersvector;
}

    
    public String  getpicpath(String username)
 {
      rs = null;
      String picpath = null;
     try {   DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/TicTacToeDB", "amira", "Amera_123");

            Statement stmt = con.createStatement();
            String queryString = new String("SELECT picpath FROM players WHERE username = '"+username+"'");
            rs = stmt.executeQuery(queryString);
    
          
while(rs.next())

{
picpath=rs.getString(1);

}

                 stmt.close();
           con.close(); 
            
        } catch (SQLException ex) {
            Logger.getLogger(TicTacToeDataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
//     System.out.println(picpath);
 return picpath;
 }
 
    
  
    
    
    
        
public boolean checkIfUsernameExists(String username){
  rs=null;
    try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/TicTacToeDB", "amira", "Amera_123");

            Statement stmt = con.createStatement();

            

            
            
            
            String queryString = "select * from players where username ='"+username+"'";
        rs = stmt.executeQuery(queryString);
        if (rs.next()){return true;}
        
         stmt.close();
           con.close();
        
        } catch (SQLException ex) {
            Logger.getLogger(TicTacToeDataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    return false;
    
    
}
    
    
    
    
    
    
}
