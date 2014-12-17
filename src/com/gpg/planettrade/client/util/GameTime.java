package com.gpg.planettrade.client.util;


public class GameTime {

	public static long serverTimeSeconds;
	public static long secondsPassed;
	public static long currentTimeSeconds;
	
	public static int time;
	
	public static void update(){
		time++;
		if(time % 60 == 0){
			secondsPassed++;
			currentTimeSeconds = serverTimeSeconds + secondsPassed;
		}
	}
	
	public static String getTimeString(long value){
		String result = "";
		if(value < 60) result = value + "s ";
		else{
			value /= 60;
			long minutes = value % 60;
			long hours = value / 60;
			if(hours < 1) result = String.format("%2dm", minutes);
			else result = String.format("%dh %2dm", hours, minutes);
		}
		return result;
	}
}
