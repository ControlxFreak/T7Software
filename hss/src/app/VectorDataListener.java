/*
 * ---------------------------------------------------------------------------------
 * Title: VectorDataListener.java
 * Description:
 * This class continually listens for data updates from the server of the type
 * vector.
 * ---------------------------------------------------------------------------------
 * Lockheed Martin
 * Engineering Leadership Development Program
 * Team 7
 * 22 April 2017
 * Jarrett Mead
 * ---------------------------------------------------------------------------------
 * Change Log
 * 	22 April 2017 - Jarrett Mead - Class Birthday
 * ---------------------------------------------------------------------------------
 */
package app;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

import app.view.TelemetryDataOverviewController.dataType;
import javafx.application.Platform;

public class VectorDataListener extends DataListener {

	public VectorDataListener(Socket socket, MainApp mainApp, dataType connType) throws IOException {
		super(socket, mainApp);
		this.connType = connType;
		in = new DataInputStream(this.socket.getInputStream());
		logger = Logger.getLogger(VectorDataListener.class.getName());
		logger.fine("Constructed " + connType + " listener");
	}

	@Override
	void listen() {
		System.out.println("listen()");
		//TODO implement
		double datum;
		try {
			datum = ((DataInputStream)in).readDouble();
			System.out.println("Tried to read.");
			Platform.runLater(() -> mainApp.updateDisplay(datum, connType));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			shutDown();
		}
	}

}
