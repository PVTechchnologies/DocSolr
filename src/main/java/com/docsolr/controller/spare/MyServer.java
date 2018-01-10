package com.docsolr.controller.spare;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import java.io.*;  
import java.net.*; 

public class MyServer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	
			try{  
			ServerSocket ss=new ServerSocket(6666);  
			Socket s=ss.accept();//establishes connection   
			DataInputStream dis=new DataInputStream(s.getInputStream());  
			String  str=(String)dis.readUTF();  
			System.out.println("message= "+str);  
			ss.close();  
			}catch(Exception e){System.out.println(e);}  
			}  
			
	

}
