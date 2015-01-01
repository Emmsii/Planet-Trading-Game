package com.gpg.planettrade.server;

import java.util.Scanner;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.gpg.planettrade.core.Globals;
import com.gpg.planettrade.server.GameServer.PlayerConnection;

public class CommandListener implements Runnable{

	private Thread thread;
	private boolean running;
	
	private Server server;
	private Scanner scanner;
	
	public CommandListener(Server server){
		this.server = server;
		thread = new Thread(this, "command_listener");
		scanner = new Scanner(System.in);
	}
	
	public void start(){
		Log.info("Command listener started.");
		running = true;
		thread.start();
	}
	
	@Override
	public void run() {
		String command = "";
		while(running){
			command = scanner.nextLine();
			doCommand(command);
		}
	}
	
	private void doCommand(String command){
		if(command == null || command.trim() == "") return;
		
		if(command.equalsIgnoreCase("stop")){
			server.stop();
			System.exit(0);
			return;
		}
		
		if(command.startsWith("config")){
			String[] split = command.split("\\s+");
			if(split.length == 1){
				Log.info("Usage: config reload | config delete | config start");
				return;
			}
			if(split[1].equalsIgnoreCase("reload")){
				Log.info("Reloading config file. Note, values that affect galaxy generation wont make any changes.");
				Globals.init(FileHandler.loadPropertiesFile());
				return;
			}
			Log.info("Usage: config reload | config delete | config start");
			return;
		}
		
		if(command.startsWith("kick")){
			String[] split = command.split("\\s+");
			if(split.length == 1){
				Log.info("Usage: kick all | kick [playername]");
				return;
			}
			
			if(split[1].equalsIgnoreCase("all")){
				for(Connection c : server.getConnections()) c.close();
				Log.info("All players have been kicked.");
				return;
			}else{
				boolean kicked = false;
				for(Connection c : server.getConnections()){
					PlayerConnection pc = (PlayerConnection) c;
					if(pc.player.name.equalsIgnoreCase(split[1])){
						Log.info(pc.player.name + " has been kicked.");
						pc.close();
						kicked = true;
						break;
					}
				}
				if(!kicked){
					Log.info("Could not find player " + split[1]);
					return;
				}
				return;
			}
		}
		
		if(command.startsWith("ban")){
			String split[] = command.split("\\s+");
			if(split.length == 1){
				Log.info("Usage: ban [playername] [time]");
				Log.info("Time value is in seconds (optional).");
				return;
			}
			if(split.length == 2){
				//kick player
				//add player to bans list
			}
			if(split.length == 3){
				//kick player
				//add player to bans list
				//add time of ban to ban list.
			}
			return;
		}
		
		if(command.equalsIgnoreCase("list")){
			String result = "";
			for(Connection c : server.getConnections()){
				PlayerConnection pc = (PlayerConnection) c;
				result = result + pc.player.name + ", ";
			}
			Log.info("Connected Players [" + server.getConnections().length + "] " + result);
			return;
		}
		
		if(command.equalsIgnoreCase("?") || command.equalsIgnoreCase("help")){
			Log.info("Server Commands\n" +
					"stop - Stops the server safely.\n" +
					"kick all, [playername] - Kicks all players or certain players.\n" +
					"ban [playername] [time] - Bans a player, time value is optional (in seconds)." +
					"config reload - Reloads the config file.\n" +
					"list - Shows list of connected player.");
			return;
		}
		
		Log.info("That command doesn't exist. Try using 'help' or '?' for a list of commands.");
	}

}
