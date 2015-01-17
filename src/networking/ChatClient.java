package networking;

import java.net.*;
import java.util.Scanner;
import java.io.*;

public class ChatClient
{
	static Scanner systemReader;
	public static void main(String[] args)
	{
		systemReader = new Scanner(System.in);
		System.out.println("Enter an IP to connect to with YakChat!");
	    String serverName = (new Scanner(System.in)).next();
	    int port = 3389;
	    try
	      {
	         System.out.println("Connecting to " + serverName
	                             + " on port " + port);
	         Socket client = new Socket(serverName, port);
	         System.out.println("Just connected to "
	                      + client.getRemoteSocketAddress());
	         OutputStream outToServer = client.getOutputStream();
	         DataOutputStream out =
	                       new DataOutputStream(outToServer);

	         out.writeUTF("Hello from "
	                      + client.getLocalSocketAddress());
	         InputStream inFromServer = client.getInputStream();
	         DataInputStream in =
	                        new DataInputStream(inFromServer);
	         System.out.println("Server says " + in.readUTF());
	         client.close();
	      }catch(IOException e)
	      {
	         e.printStackTrace();
	      }
	}
}
