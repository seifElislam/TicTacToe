/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assets;

import java.io.Serializable;
import java.util.HashMap;

/**
 *
 * @author Ehab
 */
public class Message implements Serializable{
    private MsgType type;
    private HashMap<String,String> data;
    
    public Message(MsgType type){
        this.type = type;
        data = new HashMap<String,String>();
    }
    public Message(MsgType type, String key, String value){
        this.type = type;
        data = new HashMap<String,String>();
        data.put(key, value);
    }
    public void setType(MsgType type){
        this.type = type;
    }
    public MsgType getType(){ 
        return type; 
    }
    public void setData(String key, String value){
        data.put(key,value);
    }
    public String getData(String key){
        if(data.containsKey(key))
            return data.get(key);
        else
            return null;
    }
}
