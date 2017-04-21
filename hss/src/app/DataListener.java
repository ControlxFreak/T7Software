/*
 * ---------------------------------------------------------------------------------
 * Title: DataListener.java
 * Description:
 * The superclass for all data listener classes.
 * ---------------------------------------------------------------------------------
 * Lockheed Martin
 * Engineering Leadership Development Program
 * Team 7
 * 19 April 2017
 * Jarrett Mead
 * ---------------------------------------------------------------------------------
 * Change Log
 * 	19 April 2017 - Jarrett Mead - Class Birthday
 * ---------------------------------------------------------------------------------
 */
package app;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public abstract class DataListener implements Runnable {

	volatile boolean timeToExit = false;
	Socket socket;
	InputStream in = null;
	MainApp mainApp;

	public DataListener(Socket socket, MainApp mainApp) throws IOException {
		System.out.println("Constructing DataListener");
		this.mainApp = mainApp;
		this.socket = socket;
	}

	@Override
	public void run() {
		System.out.println("Running DataListener");
		try {
			while(!timeToExit) {
				listen();
			}
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	abstract void listen();

	public void shutDown() {
		timeToExit = true;
	}

}
