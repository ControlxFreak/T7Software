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
package networking.server.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Logger;

import app.view.TelemetryDataOverviewController;
import networking.MessageUtil;
import networking.server.UAVServer;

public class TemperatureDataConnectionHandler
	extends DataConnectionHandler {

	private static final Logger logger = Logger.getLogger(TemperatureDataConnectionHandler.class.getName());

	public TemperatureDataConnectionHandler(BufferedReader br) {
		super(br);
	}

	@Override
	void handleMessage() {
		System.out.println("cbuf = " + new String(cbuf));
		int len = MessageUtil.messageLen(cbuf);
		try {
		double data = Double.parseDouble((new String(cbuf)).substring(5, len+5));
		UAVServer.setAirTemp(data);
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
		logger.info("Running TemperatureDataConnectionHandler!");
		super.run();
		try {
			logger.info("Temperature client disconnected.");
			br.close();
			UAVServer.setAirTemp(Double.MIN_VALUE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.finer(e.toString());
		}
	}

}
