/*
 * ---------------------------------------------------------------------------------
 * Title: GyroscopeDataListener.java
 * Description:
 * This class continually listens for gyroscope updates from the server.
 * ---------------------------------------------------------------------------------
 * Lockheed Martin
 * Engineering Leadership Development Program
 * Team 7
 * 21 April 2017
 * Jarrett Mead
 * ---------------------------------------------------------------------------------
 * Change Log
 * 	21 April 2017 - Jarrett Mead - Class Birthday
 * ---------------------------------------------------------------------------------
 */
package app;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import app.view.TelemetryDataOverviewController;
import javafx.application.Platform;

public class GyroscopeDataListener extends DataListener {

	public GyroscopeDataListener(Socket socket, MainApp mainApp) throws IOException {
		super(socket, mainApp);
		in = new ObjectInputStream(this.socket.getInputStream());
		System.out.println("Constructed GyroscopeDataListener");
	}

	@Override
	void listen() {
		System.out.println("gyroscope listen()");
		byte[] buf = new byte[3];
		try {
			((ObjectInputStream)in).read(buf, 0, 3);
			System.out.println("Tried to read gyroscope data.");
			if(buf[0] == 0 && buf[1] == 0 && buf[3] == 0) {
				throw new EOFException();
			}
			Platform.runLater(() -> mainApp.updateDisplay(buf, TelemetryDataOverviewController.dataType.GYROSCOPE));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			shutDown();
		}
	}

}
