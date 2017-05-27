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

import app.view.TelemetryDataOverviewController;
import networking.MessageUtil;
import networking.server.UAVServer;

public class AltitudeDataConnectionHandler
	extends DataConnectionHandler {

	private static final Logger logger = Logger.getLogger(AltitudeDataConnectionHandler.class.getName());

	public AltitudeDataConnectionHandler(BufferedReader br) {
		super(br);
	}

	@Override
	void handleMessage() {
		String message = new String(cbuf);
		System.out.println("cbuf = " + message);
		int len = message.length();
		try {
		double data = Double.parseDouble(message.substring(5, len));
		UAVServer.updateTelemetryData(data, TelemetryDataOverviewController.dataType.ALTITUDE);
		} catch(NumberFormatException nfe) {
			nfe.printStackTrace();
		}
		cbuf = new char[MessageUtil.MAX_MC_MESSAGE_LEN];
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
			UAVServer.updateTelemetryData(Double.MIN_VALUE, TelemetryDataOverviewController.dataType.ALTITUDE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.finer(e.toString());
		}
	}

}
