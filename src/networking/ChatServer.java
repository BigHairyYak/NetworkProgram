package networking;

import java.net.*;
import java.util.ArrayList;
import java.io.*;

public class ChatServer extends Thread
{
	   static ServerSocket serverSocket;
	   static int port = 3389;
	   static Socket[] users;
	   static int numberOfUsers = 0;
	   
	   public ChatServer(int port) throws IOException
	   {
	      serverSocket = new ServerSocket(port);
	      serverSocket.setSoTimeout(10000);
	      users = new Socket[4];
	   }
	   
	   public static void main(String [] args)
	   {
	      //int port = 3389;
	      try
	      {
	         Thread t = new ChatServer(port);
	         t.start();
	      }catch(IOException e)
	      {
	         e.printStackTrace();
	      }
	   }
	   
}
class ClientWaitingThread extends Thread
{
	public void run()
	{
		System.out.println("Client waiting thread started - waiting for clients...");
		while(true)
		{
			try
			{
				Socket server = ChatServer.serverSocket.accept();
				ChatServer.users[ChatServer.numberOfUsers] = server;
				ChatServer.numberOfUsers++;
			}
			catch (IOException e)
			{
				
			}
		}
	}
}
class ServerThread extends Thread
{
	   public void run()
	   {
	      while(true)
	      {
	         try
	         {
	            System.out.println("Waiting for client on port " +
	            ChatServer.serverSocket.getLocalPort() + "...");
	            Socket server = ChatServer.serverSocket.accept();
	            System.out.println("Just connected to "
	                  + server.getRemoteSocketAddress());
	            DataInputStream in =
	                  new DataInputStream(server.getInputStream());
	            System.out.println(in.readUTF());
	            DataOutputStream out =
	                 new DataOutputStream(server.getOutputStream());
	            out.writeUTF("Thank you for connecting to "
	              + server.getLocalSocketAddress() + "\nGoodbye!");
	            //server.close();
	         }catch(SocketTimeoutException s)
	         {
	            System.out.println("Socket timed out!");
	            break;
	         }catch(IOException e)
	         {
	            e.printStackTrace();
	            break;
	         }
	      }
	   }
}
