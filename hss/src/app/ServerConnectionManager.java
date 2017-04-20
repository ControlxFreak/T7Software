/*
 * ---------------------------------------------------------------------------------
 * Title: ServerConnectionManager.java
 * Description:
 * This class continually listens for incoming connections from the server.
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
import java.net.ServerSocket;
import java.net.Socket;

import networking.server.UAVServer;

public class ServerConnectionManager implements Runnable {

	private volatile boolean timeToExit = false;
	private MainApp mainApp;
	private ServerSocket server_socket;
	private TemperatureDataListener temperature_listener;
	private AltitudeDataListener altitude_listener;

	public ServerConnectionManager(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	@Override
	public void run() {

		try {
			server_socket = new ServerSocket(UAVServer.APP_PORT_NUM);
			server_socket.setSoTimeout(500);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		initListeners();

		repairListeners();

		shutDownListeners();

		try {
			server_socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void repairListeners() {
		System.out.println("repairListeners()");
		while(!timeToExit) {

		}
	}

	private void initListeners() {

		/* Establish temperature data listener */
		while(!timeToExit) {
			try {
				Socket temperature_sock = server_socket.accept();
				temperature_listener = new TemperatureDataListener(temperature_sock, mainApp);
				new Thread(temperature_listener).start();
				break;
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
		}

		/* Establish altitude data listener */
		while(!timeToExit) {
			try {
				Socket altitude_sock = server_socket.accept();
				altitude_listener = new AltitudeDataListener(altitude_sock, mainApp);
				new Thread(altitude_listener).start();
				break;
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
		}
	}

	private void shutDownListeners() {
		System.out.println("ShutDownListeners()");
		temperature_listener.shutDown();
		altitude_listener.shutDown();
	}

	protected void shutDown() {
		timeToExit = true;
	}

}
