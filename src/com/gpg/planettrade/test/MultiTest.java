package com.gpg.planettrade.test;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.esotericsoftware.minlog.Log;

public class MultiTest {

	/*
	 * 00:10  INFO: Finished all with 1 threads in 10.715 seconds
	 * 00:05  INFO: Finished all with 2 threads in 5.532 seconds
	 * 00:03  INFO: Finished all with 3 threads in 3.796 seconds
	 * 00:03  INFO: Finished all with 4 threads in 3.498 seconds
	 * 00:02  INFO: Finished all with 5 threads in 2.921 seconds
	 * 00:02  INFO: Finished all with 6 threads in 2.891 seconds
	 * 00:03  INFO: Finished all with 7 threads in 3.568 seconds
	 * 00:02  INFO: Finished all with 8 threads in 2.928 seconds
	 * 00:02  INFO: Finished all with 8 threads in 2.92 seconds
	 */
	
	private static final int THREADS = Runtime.getRuntime().availableProcessors();
	
	public static void main(String[] args) throws Exception{
		ExecutorService executor = Executors.newFixedThreadPool(THREADS);
		String[] hostList = { "http://crunchify.com", "http://yahoo.com",
				"http://www.ebay.com", "http://google.com",
				"http://www.example.co", "https://paypal.com",
				"http://bing.com/", "http://techcrunch.com/",
				"http://mashable.com/", "http://thenextweb.com/",
				"http://wordpress.com/", "http://wordpress.org/",
				"http://example.com/", "http://sjsu.edu/",
				"http://ebay.co.uk/", "http://google.co.uk/",
				"http://www.wikipedia.org/",
				"http://en.wikipedia.org/wiki/Main_Page", "http://www.giftedpineapples.com/",
				"localhost:25565"};
		
		double start = System.currentTimeMillis();
		for(int i = 0; i < hostList.length; i++){
			String url = hostList[i];
			Runnable worker = new MyRunnable(url);
			executor.execute(worker);
		}
		executor.shutdown();
		
		while(!executor.isTerminated()){
			
		}
		Log.info("Finished all with " + THREADS + " threads in " + ((System.currentTimeMillis() - start) / 1000) + " seconds");
	}
	
	public static class MyRunnable implements Runnable{
		
		private final String url;
		
		public MyRunnable(String url){
			this.url = url;
		}
		
		public void run(){
			String result = "";
			int code = 200;
			try{
				URL siteURL = new URL(url);
				HttpURLConnection connection = (HttpURLConnection) siteURL.openConnection();
				connection.setRequestMethod("GET");
				connection.connect();
				code = connection.getResponseCode();
				if(code == 200) result = "Green\t";
			}catch(Exception e){
				result = "->Red<-\t";
			}
			Log.info(url + " \t\tStatus: " + result);
		}
	}
}
