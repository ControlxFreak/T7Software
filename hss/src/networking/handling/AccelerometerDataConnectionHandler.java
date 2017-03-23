/*
 * ---------------------------------------------------------------------------------
 * Title: AccelerometerDataConnectionHandler.java
 * Description:
 * A class that handles the main computer client connection for accelerometer data.
 * ---------------------------------------------------------------------------------
 * Lockheed Martin
 * Engineering Leadership Development Program
 * Team 7
 * 12 March 2017
 * Jarrett Mead
 * ---------------------------------------------------------------------------------
 * Change Log
 * 	12 March 2017 - Jarrett Mead - Class Birthday
 * ---------------------------------------------------------------------------------
 */
package java.networking.handling;

import java.io.BufferedReader;
import java.util.logging.Logger;

import networking.UAVServer;

public class AccelerometerDataConnectionHandler
	extends DataConnectionHandler
	implements Runnable {

	private static final Logger logger = Logger.getLogger(AccelerometerDataConnectionHandler.class.getName());
	
	public AccelerometerDataConnectionHandler(BufferedReader br) {
		super(br);
	}

	public void run() {
		// TODO Auto-generated method stub
		logger.info("Running AccelerometerDataConnectionHandler!");
	}

}
