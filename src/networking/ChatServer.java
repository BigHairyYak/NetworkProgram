package networking;

import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class ChatServer
{
	static ServerSocket serverSocket;
	static int port = 56777;
	static ClientHandler[] clients;
	//static ArrayList<Client> clients;
	static int numberOfUsers = 0;

	final static String CONNECTED = "!@connect"; //sends "connected" message and establishes starting nickname
	final static String CHAT = "!@chat"; //sends "says" message
	final static String DISCONNECTED = "!@dc"; //sends "disconnected" message
	final static String USER_SET = "!@nick"; //sends "changed name to" message and establishes new nickname

	static ChatBox chat;

	public static void main(String [] args)
	{
		 System.setProperty("javax.net.ssl.trustStore", "truststoreCA.jks");
		 System.setProperty("javax.net.ssl.trustStorePassword","unhackable");
		
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
		clients = new ClientHandler[4];

		ClientWaitingThread t = new ClientWaitingThread(port);
		t.start();
	}

	static void shareMessage(String sharedMessage)
	{
		//System.out.println("Sharing message: " + sharedMessage);
		try 
		{
			//System.out.println("Number of clients: " + clients.length);
			for (int q = 0; q < numberOfUsers; q++)
			{
				//ChatBox.CP.ChatRecord.add(sharedMessage);
				//System.out.println(q);
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

					//ChatServer.clients.add(new Client(user));
					ChatServer.clients[ChatServer.numberOfUsers] = new ClientHandler(user);
					ChatServer.clients[ChatServer.numberOfUsers].start();
					ChatServer.numberOfUsers++;
					//ChatServer.clients.get(ChatServer.numberOfUsers-1);
					//ChatServer.shareMessage("New client connected - " + user.getRemoteSocketAddress());
					ChatServer.chat.repaint();
					if (ChatServer.numberOfUsers >= 4)
					{
						System.out.println("Server full - standing by");
					}
				}
				catch (IOException e){System.out.println("THINGS ARE HAPPENING");}
			}
		}
	}
}
class ClientHandler extends Thread
{
	Scanner messageReader;

	Socket user;
	DataInputStream in;
	DataOutputStream out;

	String inFeed = "";
	String clientName;

	public ClientHandler(Socket User)
	{
		messageReader = new Scanner(inFeed);
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

				messageReader = new Scanner(inFeed);
				messageReader.useDelimiter(" ");

				//System.out.println("qqqqq: " + messageReader.next());
				if (messageReader.next().equals(ChatServer.CONNECTED))
				{
					clientName = messageReader.next();
					inFeed = "User " + clientName + " connected!";
				}
				else
				{
					inFeed = clientName + ": " + inFeed;
				}

				//ChatBox.CP.ChatRecord.add(user.getRemoteSocketAddress() + " says " + inFeed);
				//ChatServer.shareMessage(user.getRemoteSocketAddress() + " says " + inFeed);
				ChatBox.CP.ChatRecord.add(inFeed);
				ChatServer.shareMessage(inFeed);

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
