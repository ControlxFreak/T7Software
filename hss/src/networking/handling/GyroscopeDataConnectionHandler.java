/*
 * ---------------------------------------------------------------------------------
 * Title: GyroscopeDataConnectionHandler.java
 * Description:
 * A class that handles the main computer client connection for gyroscope data.
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

public class GyroscopeDataConnectionHandler 
	extends DataConnectionHandler {

	private static final Logger logger = Logger.getLogger(GyroscopeDataConnectionHandler.class.getName());
	
	public GyroscopeDataConnectionHandler(BufferedReader br) {
		super(br);
	}

	/* (non-Javadoc)
	 * @see networking.handling.DataConnectionHandler#run()
	 */
	@Override
	public void run() {
		logger.info("Running GyroscopeDataConnectionHandler!");
	}

}
