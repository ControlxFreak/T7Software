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
package networking.handling;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Logger;

public class AccelerometerDataConnectionHandler
	extends DataConnectionHandler {

	private static final Logger logger = Logger.getLogger(AccelerometerDataConnectionHandler.class.getName());
	
	public AccelerometerDataConnectionHandler(BufferedReader br) {
		super(br);
	}

	/* (non-Javadoc)
	 * @see networking.handling.DataConnectionHandler#run()
	 */
	@Override
	public void run() {
		logger.info("Running AccelerometerDataConnectionHandler!");
		super.run();
		try {
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.finer(e.toString());
		}
	}

}
