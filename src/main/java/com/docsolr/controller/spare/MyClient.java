package com.docsolr.controller.spare;

import java.io.*;  
import java.net.*;

public class MyClient {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try{      
			Socket s=new Socket("122.177.7.43",50126);  
			DataOutputStream dout=new DataOutputStream(s.getOutputStream());  
			dout.writeUTF("Hello Server");  
			System.out.println("I have written something down");
			dout.flush();  
			dout.close();  
			s.close();  
			}catch(Exception e){System.out.println(e);}  
			} 
	}


