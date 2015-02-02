package networking;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ChatBox extends JFrame implements KeyListener
{
	static ChatPanel CP;
	//static ChatInputPanel CIP;
	String nextMessage = "";
	public ChatBox()
	{
		CP = new ChatPanel();
		add(CP);
		setSize(400, 800);
		setResizable(false);
		//setFocusable(true);
		setVisible(true);		
	}
	public ChatBox(String firstMessage)
	{
		System.out.println("Chatbox initialized");
		CP = new ChatInputPanel();
		add(CP);
		setSize(400, 800);
		setVisible(true);
		setFocusable(true);
		setResizable(false);
		addKeyListener(this);
	}
	
	public void keyPressed(KeyEvent E)
	{
		//System.out.println("key pressed - recorded in ChatBox");
		int keycode = E.getKeyCode();
		if (keycode == KeyEvent.VK_ENTER)
		{
			try 
			{
				ChatClient.outToServer.writeUTF(nextMessage);
				nextMessage = "";
				((ChatInputPanel)CP).message = nextMessage;								
				CP.repaint();
				System.out.println("Message sent");
			}
			catch (IOException e) 
			{
				
			}
		}
		else if (keycode == KeyEvent.VK_SPACE)
		{
			nextMessage += " ";
		}
		else if (keycode == KeyEvent.VK_SHIFT)
		{
			
		}
		else if (keycode == KeyEvent.VK_BACK_SPACE)
		{
			if (nextMessage.length() > 0)
			{
				nextMessage = nextMessage.substring(0, nextMessage.length()-1);
				((ChatInputPanel)CP).message = nextMessage;
				CP.repaint();
			}
		}
		else
		{
			nextMessage += KeyEvent.getKeyText(keycode);
			((ChatInputPanel)CP).message = nextMessage;
			System.out.println(nextMessage);
			CP.repaint();
		}
	}

	public void keyReleased(KeyEvent arg0) {}
	public void keyTyped(KeyEvent arg0) {}
}

class ChatPanel extends JPanel
{
	String chatTitle = "Chat Server";
	ArrayList<String> ChatRecord;
	public ChatPanel()
	{
		ChatRecord = new ArrayList<String>(1);
		setBackground(Color.BLACK);
	}
	public void paintComponent(Graphics G)
	{
		super.paintComponent(G);
		G.setColor(Color.GREEN);
		G.drawString(chatTitle, 5, 15);
		if (ChatServer.clients != null)
		{
			for (int q = 0; q < 4; q++)
			{
				if (ChatServer.clients[q] != null)
					G.drawString(ChatServer.clients[q].user.getRemoteSocketAddress().toString(), 5, (q * 15) + 30);
			}
		}
		G.drawLine(0, 90, 400, 90);
		for (int q = 0; q < ChatRecord.size(); q++)
		{
			G.drawString(ChatRecord.get(q), 5, (q * 15) + 105);
		}	
	}
}

class ChatInputPanel extends ChatPanel
{
	Scanner messageScanner;
	String chatTitle = "Chat Client Program - ";
	ArrayList<String> ChatRecord;
	String message = "";
	public ChatInputPanel()
	{
		ChatRecord = new ArrayList<String>(1);
		setBackground(Color.BLACK);
	}
	public void paintComponent(Graphics G)
	{
		super.paintComponent(G);	
		G.setColor(Color.BLACK);
		G.fillRect(0, 0, 400, 800);G.setColor(Color.GREEN);
		G.setColor(Color.GREEN);
		G.drawString(chatTitle, 5, 15);
		if (ChatServer.clients != null)
		{
			for (int q = 0; q < 4; q++)
			{
				if (ChatServer.clients[q] != null)
					G.drawString(ChatServer.clients[q].user.getRemoteSocketAddress().toString(), 5, (q * 15) + 30);
			}
		}
		G.drawLine(0, 90, 750, 90);
		for (int q = 0; q < ChatRecord.size(); q++)
		{
			messageScanner = new Scanner(ChatRecord.get(q));
			messageScanner.useDelimiter(": ");
			G.setColor(Color.GREEN);
			String clientname = messageScanner.next();
			G.drawString(clientname, 5, (q * 15) + 105);
			if (messageScanner.hasNext())
			{
				G.drawString(": ", (G.getFontMetrics().stringWidth(clientname)) + 5, (q * 15) + 105);
				G.setColor(Color.WHITE); 
				G.drawString(messageScanner.next(), (G.getFontMetrics().stringWidth(clientname + ": ")) + 10, (q * 15) + 105);
				//G.drawString(ChatRecord.get(q), 5, (q*15) + 105);
			}
		}
		G.setColor(Color.GREEN);
		G.drawLine(0, 735, 400, 735);
		G.drawString(message, 5, 750);
	}
}