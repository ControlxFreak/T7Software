/*
 * ---------------------------------------------------------------------------------
 * Title: DataConnectionHandler.java
 * Description:
 * An abstract class for handling main computer client connections.
 * ---------------------------------------------------------------------------------
 * Lockheed Martin
 * Engineering Leadership Development Program
 * Team 7
 * 14 March 2017
 * Jarrett Mead
 * ---------------------------------------------------------------------------------
 * Change Log
 * 	14 March 2017 - Jarrett Mead - Class Birthday
 * ---------------------------------------------------------------------------------
 */
package networking.handling;

import java.io.BufferedReader;
import java.io.IOException;

import networking.MessageUtil;

public abstract class DataConnectionHandler
	implements Runnable{
	
	BufferedReader br;
	private char[] cbuf = new char[MessageUtil.MAX_MC_MESSAGE_LEN];
	private volatile boolean timeToExit = false;

	public DataConnectionHandler(BufferedReader br) {
		this.br = br;
	}
	
	public void shutDown() {
		timeToExit = true;
	}

	private void handleMessage() {
		System.out.println("Thanks for the message! :-)");
		cbuf = new char[MessageUtil.MAX_MC_MESSAGE_LEN];
	}
	
	public void run() {
		while(!timeToExit && MessageUtil.readMcMessage(br, cbuf) != 0) {
			handleMessage();
		}
	}

}
