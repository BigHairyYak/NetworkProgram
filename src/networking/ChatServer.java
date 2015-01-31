package networking;

import java.net.*;
import java.util.ArrayList;
import java.io.*;

public class ChatServer
{
	   static ServerSocket serverSocket;
	   static int port = 3389;
	   static Client[] clients;
	   //static ArrayList<Client> clients;
	   static int numberOfUsers = 0;
	   
	   static ChatBox chat;

	   public static void main(String [] args)
	   {
		   chat = new ChatBox();
		   chat.setVisible(true);
		   try 
		   {
			   serverSocket = new ServerSocket(port);
		   } catch (IOException e) 
		   {
			   e.printStackTrace();
		   }
		  //clients = new ArrayList<Client>(1);
		   clients = new Client[4];

	      ClientWaitingThread t = new ClientWaitingThread(port);
	      t.start();
	   }
	   
		static void shareMessage(String sharedMessage)
		{
			System.out.println("Sharing message: " + sharedMessage);
			try 
			{
				//System.out.println("Number of clients: " + clients.length);
				for (int q = 0; q < numberOfUsers; q++)
				{
					//ChatBox.CP.ChatRecord.add(sharedMessage);
					System.out.println(q);
					clients[q]
							.out
							.writeUTF(sharedMessage);
				}
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
}
class ClientWaitingThread extends Thread
{
	public ClientWaitingThread(int port) {}
	
	public void run()
	{
		System.out.println("Client waiting thread started - waiting for clients...");
		while(true)
		{
			if (ChatServer.numberOfUsers < 4)
			{
				try
				{
					Socket user = ChatServer.serverSocket.accept();
					System.out.println("New client connected - " + user.getRemoteSocketAddress());
					//ChatServer.clients.add(new Client(user));
					ChatServer.clients[ChatServer.numberOfUsers] = new Client(user);
					ChatServer.clients[ChatServer.numberOfUsers].start();
					ChatServer.numberOfUsers++;
					//ChatServer.clients.get(ChatServer.numberOfUsers-1);
					ChatServer.chat.repaint();
					if (ChatServer.numberOfUsers >= 4)
					{
						System.out.println("Server full - standing by");
					}
				}
				catch (IOException e){}
			}
		}
	}
}
class Client extends Thread
{
	Socket user;
	DataInputStream in;
	DataOutputStream out;
	String inFeed;
	static String outFeed;
	
	public Client(Socket User)
	{
		try 
		{
			user = User; 
			in = new DataInputStream(user.getInputStream());			   
			out = new DataOutputStream(user.getOutputStream());
		}

		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	public void run()
	{        
		while(true)
		{
			try
			{
				//System.out.println("Looking for messages");
				inFeed = in.readUTF();
				//ChatBox.CP.ChatRecord.add(user.getRemoteSocketAddress() + " says " + inFeed);
				ChatServer.shareMessage(user.getRemoteSocketAddress() + " says " + inFeed);
				ChatBox.CP.repaint();
			}
			catch(SocketTimeoutException s)
			{
				System.out.println("Socket timed out!");
				break;
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
}
