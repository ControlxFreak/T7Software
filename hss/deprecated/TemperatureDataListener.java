/*
 * ---------------------------------------------------------------------------------
 * Title: TemperatureDataListener.java
 * Description:
 * This class continually listens for temperature updates from the server.
 * ---------------------------------------------------------------------------------
 * Lockheed Martin
 * Engineering Leadership Development Program
 * Team 7
 * 18 April 2017
 * Jarrett Mead
 * ---------------------------------------------------------------------------------
 * Change Log
 * 	18 April 2017 - Jarrett Mead - Class Birthday
 * ---------------------------------------------------------------------------------
 */
package app;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import app.view.TelemetryDataOverviewController;
import javafx.application.Platform;

public class TemperatureDataListener extends DataListener {

	public TemperatureDataListener(Socket socket, MainApp mainApp) throws IOException {
		super(socket, mainApp);
		in = new DataInputStream(this.socket.getInputStream());
		System.out.println("Constructed TemperatureDataListener");
	}

	@Override
	void listen() {
		System.out.println("listen()");
		double airTemp;
		try {
			airTemp = ((DataInputStream)in).readDouble();
			System.out.println("Tried to read.");
			Platform.runLater(() -> mainApp.updateDisplay(airTemp, TelemetryDataOverviewController.dataType.AIR_TEMP));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			shutDown();
		}
	}

}
