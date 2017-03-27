/*
 * ---------------------------------------------------------------------------------
 * Title: AltitudeDataConnectionHandler.java
 * Description:
 * A class that handles the main computer client connection for altitude data.
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
package networking.server.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Logger;

public class AltitudeDataConnectionHandler
	extends DataConnectionHandler {

	private static final Logger logger = Logger.getLogger(AltitudeDataConnectionHandler.class.getName());
	
	public AltitudeDataConnectionHandler(BufferedReader br) {
		super(br);
	}

	/* (non-Javadoc)
	 * @see networking.server.connection.DataConnectionHandler#run()
	 */
	@Override
	public void run() {
		logger.info("Running AltitudeDataConnectionHandler!");
		super.run();
		try {
			logger.info("Altitude client disconnected.");
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.finer(e.toString());
		}
	}

}
