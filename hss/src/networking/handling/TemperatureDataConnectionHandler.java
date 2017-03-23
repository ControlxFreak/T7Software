/*
 * ---------------------------------------------------------------------------------
 * Title: TemperatureDataConnectionHandler.java
 * Description:
 * A class that handles the main computer client connection for temperature data.
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
package networking.handling;

import java.io.BufferedReader;
import java.util.logging.Logger;

public class TemperatureDataConnectionHandler
	extends DataConnectionHandler
	implements Runnable {

	private static final Logger logger = Logger.getLogger(TemperatureDataConnectionHandler.class.getName());
	
	public TemperatureDataConnectionHandler(BufferedReader br) {
		super(br);
	}

	public void run() {
		// TODO Auto-generated method stub
		logger.info("Running TemperatureDataConnectionHandler!");
	}

}
