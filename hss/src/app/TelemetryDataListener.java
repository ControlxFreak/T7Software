/*
 * ---------------------------------------------------------------------------------
 * Title: TelemetryDataListener.java
 * Description:
 * This class continually listens for updates to the telemetry data.
 * ---------------------------------------------------------------------------------
 * Lockheed Martin
 * Engineering Leadership Development Program
 * Team 7
 * 16 April 2017
 * Jarrett Mead
 * ---------------------------------------------------------------------------------
 * Change Log
 * 	16 April 2017 - Jarrett Mead - Class Birthday
 * ---------------------------------------------------------------------------------
 */
package app;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;

import app.view.TelemetryDataOverviewController;
import javafx.application.Platform;
import networking.server.UAVServer;

public class TelemetryDataListener implements Runnable {

	private volatile boolean timeToExit = false;
	private ObjectInputStream in;
	private MainApp mainApp;
	private Socket sock = null;

	public TelemetryDataListener(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	public void run() {

		while(sock == null) {
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			establishConnection();
		}

		while(in == null) {
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//Platform.runLater(() -> establishStream());
			establishStream();
		}

		while(!timeToExit) {
			double airTemp;
			try {
				airTemp = in.readDouble();
				System.out.println("Tried to read.");
				//mainApp.updateAirTemp(airTemp);
				Platform.runLater(() -> mainApp.updateAirTemp(airTemp));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
	}

	private void establishConnection() {
		try {
			System.out.println("Establishing connection with server.");
			sock = new Socket(InetAddress.getLocalHost(), UAVServer.APP_PORT_NUM);
		} catch(Exception e) {
			System.out.println("Unable to establish connection.");
		}
	}

	private void establishStream() {
		try {
			System.out.println("Establishing input stream with server.");
			in = new ObjectInputStream(sock.getInputStream());
		} catch (IOException e) {

		}

	}

	protected void shutDown() {
		timeToExit = true;
	}

}
