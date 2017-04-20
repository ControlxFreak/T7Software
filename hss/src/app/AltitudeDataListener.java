/*
 * ---------------------------------------------------------------------------------
 * Title: AltitudeDataListener.java
 * Description:
 * This class continually listens for altitude updates from the server.
 * ---------------------------------------------------------------------------------
 * Lockheed Martin
 * Engineering Leadership Development Program
 * Team 7
 * 19 April 2017
 * Jarrett Mead
 * ---------------------------------------------------------------------------------
 * Change Log
 * 	19 April 2017 - Jarrett Mead - Class Birthday
 * ---------------------------------------------------------------------------------
 */
package app;

import java.io.IOException;
import java.net.Socket;

import app.view.TelemetryDataOverviewController;
import javafx.application.Platform;

public class AltitudeDataListener extends DataListener {

	public AltitudeDataListener(Socket socket, MainApp mainApp) throws IOException {
		super(socket, mainApp);
		System.out.println("Constructed AltitudeDataListener");
	}

	@Override
	void listen() {
		System.out.println("altitude listen()");
		double altitude;
		try {
			altitude = in.readDouble();
			System.out.println("Tried to read altitude.");
			Platform.runLater(() -> mainApp.updateDisplay(altitude, TelemetryDataOverviewController.dataType.ALTITUDE));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			shutDown();
		}
	}

}
