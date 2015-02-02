package networking;

import java.net.*;
import java.util.Scanner;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

public class ChatClient
{
	static Scanner clientReader;
    String messageToServer;
    static DataOutputStream outToServer;
    static ChatBox chat;

    	   	    
    public static void main(String[] args)
	{
    	System.setProperty("javax.net.ssl.trustStore", "truststoreCA.jks");
    	
    	chat = new ChatBox("");
    	System.out.println("Chat Client started");
    	
 		clientReader = new Scanner(System.in);
		DataInputStream in;
		Thread inputThread, outputThread;
	    boolean connected = false;
	    int port = 56777;

	    while (connected == false)
	    {
	    	try
	    	{
	    		try
	    		{
	    			System.out.println("Enter an IP to connect to with YakChat!");
	    		    String serverName = clientReader.next();
	    		    
	    		    System.out.println("Enter your first username!");
	    		    String username = clientReader.next();
	    		    
	    		    System.out.println("Connecting to " + serverName + " on port " + port);
	    		    
	    			Socket client = new Socket(serverName, port);

	    			System.out.println("Just connected to "
	    					+ client.getRemoteSocketAddress());
  			
	    			connected = true;  			
	    			
	    			final InputStream inFromServer = client.getInputStream();
	    			final OutputStream out = client.getOutputStream();

	    			outToServer = new DataOutputStream(out);
	    				    			
	    			outToServer.writeUTF("!@connect " + username);
	    			
	    			inputThread = new Thread(
	    				new Runnable()
	    				{
	    					String inFeed = "";	    					
	    					DataInputStream in = new DataInputStream(inFromServer);
							public void run() 
							{
								while (true)
								{
									try
									{
										inFeed = in.readUTF();
										//System.out.println("Server says " + inFeed);
										((ChatInputPanel)chat.CP).ChatRecord.add(inFeed);
										((ChatInputPanel)chat.CP).repaint();
									} 
									catch (IOException e) 
									{
										e.printStackTrace();
									}
									if (inFeed != "")
										System.out.println(inFeed);
								}
							}
	    				}
	    			);
	    			inputThread.start();
	    			
	    		}
	    		
	    		catch (ConnectException E)
	    		{
	    			System.out.println("Nope");
	    		}	    		
	    	}
	    	catch(IOException e)
	    	{
	    		e.printStackTrace();
	    	}
	    }
	}
	}