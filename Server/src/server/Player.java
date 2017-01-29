/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

/**
 *
 * @author Ehab
 */
public class Player {
    private int id;
    private int score;
    private String status;
    private String username;
    private String fname;
    private String lname;
    private String password;
    private String picPath;
    
    public Player(){
        
    }
    public void setID(int id){
        this.id = id;
    }
    public int getID(){
        return id;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public String getUsername(){
        return username;
    }
    public void setFname(String fname){
        this.fname = fname;
    }
    public String getFname(){
        return fname;
    }
    public void setLname(String lname){
        this.lname = lname;
    }
    public String getLname(){
        return lname;
    }
    public String getFullName(){
        return fname+" "+lname;
    }
    public void setPicPath(String picPath){
        this.picPath = picPath;
    }
    public String getPicPath(){
        return picPath;
    }
    public void setScore(int score){
        this.score = score;
    }
    public int getScore(){
        return score;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public String getStatus(){
        return status;
    }
}
