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
    private String type;
    private HashMap<String,String> data;
    
    public Message(String type){
        this.type = type;
        data = new HashMap<String,String>();
    }
    public Message(String type, int DestID){
        this.type = type;
        data.put("destid", String.valueOf(DestID));
    }
    public void setType(String type){
        this.type = type;
    }
    public String getType(){ 
        return type; 
    }
    public void setData(String key, String value){
        //if(!data.containsKey(key))
        data.put(key,value);
    }
    public String getData(String key){
        if(data.containsKey(key))
            return data.get(key);
        else
            return null;
    }
    public int getDestID(){
        if(data.containsKey("destid"))
            return Integer.parseInt(data.get("destid"));
        else
            return 0;
    }
    public void setDestID(int DestID){
        if(!data.containsKey("destid"))
            data.put("destid", String.valueOf(DestID));
        else
            data.replace("destid", String.valueOf(DestID));
    }
}
