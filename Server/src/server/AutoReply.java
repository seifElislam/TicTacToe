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
    private static final String[] REPLIES = {
        "Hello",
        "Nice to meet u",
        "Yes!",
        "How about u?",
        "Speak for your self",
        "I enjoy playing with you ",
        "you are a nice human",
        "I am listnening to music now",
        "What is your favourite food?",
        "Nice try",
        "No I don't",
        "I am sad",
        "I love ♥ java ☻"
    };
    private static Random index = new Random();
    public static String getReply(){
        return REPLIES[index.nextInt(REPLIES.length)];
    }
}
