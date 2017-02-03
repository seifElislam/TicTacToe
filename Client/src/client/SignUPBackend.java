/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import client.network.Session;
import java.util.Scanner;



/**
 *
 * @author amira
 */
public class SignUPBackend {
        public static void main(String[] args) {
        System.out.println("client console started");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Port: ");
        String portNumber = scanner.nextLine();
        Session session = new Session("127.0.0.1", Integer.parseInt(portNumber));
        if(session.connected){
            System.out.print("Fname: ");
            String fname = scanner.nextLine();
            System.out.print("Lname: ");
            String lname= scanner.nextLine();
             System.out.print("Username: ");
            String username = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();
             System.out.print("picpath: ");
            String picpath = scanner.nextLine();
            
            session.signUpToServer(fname,lname,username,password,picpath);
       
       
        }
    }
}
