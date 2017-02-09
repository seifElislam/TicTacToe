/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.util.Random;
/**
 *
 * @author Ehab
 */
public class AutoReply {
    private static String[] replies = {
        "auto reply 1",
        "auto reply 2",
        "auto reply 3",
        "auto reply 4",
        "auto reply 5",
        "auto reply 6",
        "auto reply 7",
        "auto reply 8",
        "auto reply 9",
        "auto reply 10",
    };
    private static Random index = new Random();
    public static String getReply(){
        return replies[index.nextInt(replies.length)];
    }
}
