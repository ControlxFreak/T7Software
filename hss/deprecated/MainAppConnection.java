/*
 * ---------------------------------------------------------------------------------
 * Title: MainAppConnection.java
 * Description:
 * A class that periodically sends the main application updated telemetry data.
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
package networking.server.connection;

import java.io.IOException;
import java.io.ObjectOutputStream;

import app.model.TelemetryData;
import networking.server.UAVServer;

public class MainAppConnection implements Runnable {

	private final ObjectOutputStream out;
	volatile boolean timeToExit = false;

	public MainAppConnection(ObjectOutputStream out) {
		this.out = out;
	}

	public void run() {
		while(!timeToExit) {
			try {
				TelemetryData td = UAVServer.getTelData();
				System.out.println("UAVServer.getTelData() = " + td);
				System.out.println("telData.airTemp = " + td.getAirTemp());
				out.writeDouble(td.getAirTemp());
				out.flush();
				Thread.sleep(1000);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void shutDown() {
		timeToExit = true;
	}

}