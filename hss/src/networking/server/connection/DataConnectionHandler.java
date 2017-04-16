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
package networking.server.connection;

import java.io.BufferedReader;
import networking.MessageUtil;

public abstract class DataConnectionHandler
	implements Runnable{

	BufferedReader br;
	char[] cbuf = new char[MessageUtil.MAX_MC_MESSAGE_LEN];
	volatile boolean timeToExit = false;

	public DataConnectionHandler(BufferedReader br) {
		this.br = br;
	}

	public void shutDown() {
		timeToExit = true;
	}

	void handleMessage() {
		System.out.println("Thanks for the message! :-)");
		cbuf = new char[MessageUtil.MAX_MC_MESSAGE_LEN];
	}

	public void run() {
		while(!timeToExit && MessageUtil.readMcMessage(br, cbuf) != 0) {
			handleMessage();
		}
	}

}
