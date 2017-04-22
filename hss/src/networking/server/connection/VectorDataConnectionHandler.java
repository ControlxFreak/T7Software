/*
 * ---------------------------------------------------------------------------------
 * Title: VectorDataConnectionHandler.java
 * Description:
 * A class that handles the main computer client connection for data that comes in
 * the form of a 3x1 vector.
 * ---------------------------------------------------------------------------------
 * Lockheed Martin
 * Engineering Leadership Development Program
 * Team 7
 * 22 March 2017
 * Jarrett Mead
 * ---------------------------------------------------------------------------------
 * Change Log
 * 	22 March 2017 - Jarrett Mead - Class Birthday
 * ---------------------------------------------------------------------------------
 */
package networking.server.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Logger;

import app.view.TelemetryDataOverviewController.dataType;
import networking.MessageUtil;
import networking.server.UAVServer;

public class VectorDataConnectionHandler
	extends DataConnectionHandler {

	public VectorDataConnectionHandler(BufferedReader br, dataType connType) {
		super(br);
		logger = Logger.getLogger(VectorDataConnectionHandler.class.getName());
		this.connType = connType;
	}

	@Override
	void handleMessage() {
		//TODO implement once protobuffer format is known
		String message = new String(cbuf);
		System.out.println("cbuf = " + message);
		int len = message.length();

		try {
		double data = Double.parseDouble(message.substring(5, len));
		UAVServer.updateTelemetryData(data, connType);
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
		logger.info("Running " + connType + " handler!");
		super.run();
		try {
			logger.info(connType + " client disconnected.");
			br.close();
			UAVServer.updateTelemetryData(Double.MIN_VALUE, connType);	//TODO
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.finer(e.toString());
		}
	}

}
