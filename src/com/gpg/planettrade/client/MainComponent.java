package com.gpg.planettrade.client;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import org.lwjgl.Sys;

import com.esotericsoftware.minlog.Log;
import com.gpg.planettrade.client.menu.Marketplace;
import com.gpg.planettrade.client.menu.PlanetMenu;
import com.gpg.planettrade.client.menu.PlanetSelectMenu;
import com.gpg.planettrade.client.util.GameState;
import com.gpg.planettrade.client.util.GameTime;
import com.gpg.planettrade.client.util.Keyboard;
import com.gpg.planettrade.client.util.Mouse;
import com.gpg.planettrade.client.util.Text;
import com.gpg.planettrade.core.Globals;

public class MainComponent extends Canvas implements Runnable{

	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	public static final String NAME = "Name";
		
	private boolean aa = true;
	private boolean running = false;
	private int fps = 0;
	private int ups = 0;
	private double msp = 0;
	public int ping;
	private int renderPing;
	
	public GameClient gameClient;
	private GameState state;
	private Mouse mouse;
	private Keyboard key;
	
	private PlanetSelectMenu planetSelectMenu;
	private PlanetMenu planetMenu;
	private Marketplace marketplace;
	
	public MainComponent(){
		Log.info("LWJGL Version" + Sys.getVersion());
		
		gameClient = new GameClient(this);
		mouse = new Mouse();
		key = new Keyboard();
		state = new GameState();
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
		addKeyListener(key);
		
		switchState(3);
	}
	
	public static void main(String[] args){
		MainComponent main = new MainComponent();
		Dimension size = new Dimension(WIDTH, HEIGHT);
		main.setPreferredSize(size);
		main.setMinimumSize(size);
		main.setMaximumSize(size);
		
		JFrame frame = new JFrame("Client");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(main);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		main.start();
	}
	
	public void start(){
		running = true;
		new Thread(this, "main").start();
	}
	
	public void stop(){
		running = false;
	}
	
	public void run() {
		double nsPerFrame = 1000000000.0 / 60.0;
		double unprocessedTime = 0;
		double maxSkipFrames = 10;
		
		long lastTime = System.nanoTime();
		long lastFrameTime = System.currentTimeMillis();
		int frames = 0;
		int updates = 0;
		while(running){
			long now = System.nanoTime();
			double passedTime = (now - lastTime) / nsPerFrame;
			lastTime = now;
			
			if(passedTime < -maxSkipFrames) passedTime = -maxSkipFrames;
			if(passedTime > maxSkipFrames) passedTime  = maxSkipFrames;
			
			unprocessedTime += passedTime;
			
			boolean render = false;
			while(unprocessedTime > 1){
				unprocessedTime -= 1;
				update();
				updates++;
				render = true;
				
				if(render){
					render();
					frames++;
				}
				
				if(System.currentTimeMillis() > lastFrameTime + 1000){
					fps = frames;
					ups = updates;
					msp = 1000.0 / frames;
					renderPing = ping;
//					System.out.println(fps + "fps, " + ups + "ups | " + msp + "ms per frame.");
					lastFrameTime += 1000;
					frames = 0;
					updates = 0;
					
				}
			}
		}
	}

	private void update(){
		key.update();
		GameTime.update();		
		switch(state.getState()){
			case 1:
				planetSelectMenu.update();
				break;
			case 2:
				planetMenu.update();
				break;
			case 3:
				marketplace.update();
				break;
			default:
				break;
		}
		key.release();
	}
		
	private void render(){
		BufferStrategy bs = getBufferStrategy();
		if(bs == null){
			createBufferStrategy(3);
			requestFocus();
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		if(aa) this.antiAliasing(g);
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH + 20, HEIGHT + 20);
		g.setFont(new Font("Arial", Font.BOLD, 12));
		g.setColor(Color.WHITE);
					
		//Render here
		
		//Temp, render list of usernames connected.
//		g.drawRect(3, 47, 105, 17 * gameClient.players.size());
//		for(int i = 0; i < gameClient.players.size(); i++){
//			Text.render("[" + i + "] " + gameClient.players.get(i), 7, 60 + (i * 15), 12, Font.BOLD, g);
//		}
		
		Text.render(fps + "fps | " + ups + "ups | " + String.format("%.2f", msp) + "ms", 3, 709, 10, Font.BOLD, new Color(150, 150, 150), g);
		Text.render("[" + Globals.username + "] " + " Players: " + gameClient.players.size() + " | Ping " + renderPing + "ms", 3, 718, 10, Font.BOLD, new Color(150, 150, 150), g);
		Text.render("Current time: " + GameTime.currentTimeSeconds + " Server time: " + GameTime.serverTimeSeconds + " Seconds Passed: " + GameTime.secondsPassed, 300, 718, 10, Font.BOLD, new Color(150, 150, 150), g);
		
		Text.render(Globals.username, 20, 33, 25, Font.BOLD, g);
		Text.render("Stored Credits", 335, 17, 12, Font.BOLD, g);
		Text.render(Globals.toCredits(Globals.storedCredits), 335, 35, 19, Font.BOLD, new Color(81, 151, 201), g);
		Text.render("Planets Owned", 510, 17, 12, Font.BOLD, g);
		if(Globals.ownedPlanets != null) Text.render(Globals.ownedPlanets.size() + "", 509, 35, 19, Font.BOLD, new Color(150, 150, 150), g);
		else Text.render("0", 509, 35, 19, Font.BOLD, new Color(150, 150, 150), g);
		Text.render("Total Worth", 670, 17, 12, Font.BOLD, g);
		if(Globals.ownedPlanets != null) Text.render(Globals.toCredits(Globals.getWorth()), 670, 35, 19, Font.BOLD, new Color(81, 151, 201), g);

		g.drawRect(-1, -1, 325, 45);
		g.drawRect(-1, -1, 1285, 45);
			
		switch(state.getState()){
			case 1:
				planetSelectMenu.render(g);
				break;
			case 2:
				planetMenu.render(g);
				break;
			case 3:
				marketplace.render(g);
				break;
			default:
				break;
		}
				
		g.dispose();
		bs.show();
	}
	
	public void antiAliasing(Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		rh.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHints(rh);
	}
	
	public void switchState(int state){
		/* 
		 * States:
		 */
		switch(state){
			case 1:
				planetSelectMenu = new PlanetSelectMenu(mouse, key, this);
				if(Globals.ownedPlanets != null) planetSelectMenu.updateOwnedPlanets(Globals.ownedPlanets);
				this.state.setState(state);
				break;
			case 2:
				planetMenu = new PlanetMenu(mouse, key, this);
				this.state.setState(state);
				break;
			case 3:
				marketplace = new Marketplace(mouse, key, this);
				this.state.setState(state);
				break;
		}
	}
}
