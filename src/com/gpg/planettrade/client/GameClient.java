package com.gpg.planettrade.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.minlog.Log;
import com.gpg.planettrade.core.Globals;
import com.gpg.planettrade.core.Network;
import com.gpg.planettrade.core.Network.Login;

public class GameClient {

	public Client client;
		
	public List<String> players = new ArrayList<String>();
	
	public GameClient(MainComponent main){
		client = new Client(10250, 10250);
		client.start();
		
		Network.register(client);
		
		client.addListener(new ClientListener(this, main));
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
		
		String host = (String) JOptionPane.showInputDialog(null, "Host", "Connect to server", JOptionPane.QUESTION_MESSAGE, null, null, "localhost");
		
		while(true){
			Globals.username = (String) JOptionPane.showInputDialog(null, "Name", "Connect to server", JOptionPane.QUESTION_MESSAGE, null, null, "Username");
			if(Globals.username != null && Globals.username.length() < 12) break;
			else JOptionPane.showMessageDialog(null, "Your username must be shorter that 12 characters.");
		}
		try {
			client.connect(5000, host, Network.PORT);
		} catch (IOException e) {
			Log.warn("Cannot connect to server.");
//			e.printStackTrace();
//			System.exit(0);
		}
		
		Login login = new Login();
		login.name = Globals.username;
		client.sendTCP(login);
	}
	
	public void removePlayer(String name){
		for(int i = players.size() - 1; i >= 0; i--){
			if(players.get(i).equalsIgnoreCase(name)){
				Log.info(players.get(i) + " has disconnected.");
				players.remove(i);
				return;
			}
		}
	}
}
