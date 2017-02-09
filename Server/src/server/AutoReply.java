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
        "Hello",
        "Nice to meet u",
        "Yes!",
        "How about u?",
        "speak for your self",
        "I enjoy playing with u ",
        "you are a nice human",
        "i am listnening to music now",
        "what is your favourite food?",
        "nice try",
        "no i don't",
        "i am sad",
        "i love java"
    };
    private static Random index = new Random();
    public static String getReply(){
        return replies[index.nextInt(replies.length)];
    }
}
