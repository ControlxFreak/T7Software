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

public abstract class DataConnectionHandler {
	
	BufferedReader br;
	private static volatile boolean timeToExit = false;

	public DataConnectionHandler(BufferedReader br) {
		this.br = br;
	}
	
	public static void shutDown() {
		timeToExit = true;
	}

}
