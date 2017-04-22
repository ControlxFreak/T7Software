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

import app.view.TelemetryDataOverviewController.dataType;
import networking.server.UAVServer;

public class ServerConnectionManager implements Runnable {

	private volatile boolean timeToExit = false;
	private MainApp mainApp;
	private ServerSocket server_socket;
	private DoubleDataListener temperature_listener;
	private DoubleDataListener altitude_listener;
	private VectorDataListener accel_listener;
	private VectorDataListener gyro_listener;

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
				temperature_listener = new DoubleDataListener(temperature_sock, mainApp, dataType.AIR_TEMP);
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
				altitude_listener = new DoubleDataListener(altitude_sock, mainApp, dataType.ALTITUDE);
				new Thread(altitude_listener).start();
				break;
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
		}

		/* Establish accelerometer data listener */
		while(!timeToExit) {
			try {
				Socket accelerometer_sock = server_socket.accept();
				accel_listener = new VectorDataListener(accelerometer_sock, mainApp, dataType.ACCELEROMETER);
				new Thread(accel_listener).start();
				break;
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
		}

		/* Establish gyroscope data listener */
		while(!timeToExit) {
			try {
				Socket gyroscope_sock = server_socket.accept();
				gyro_listener = new VectorDataListener(gyroscope_sock, mainApp, dataType.GYROSCOPE);
				new Thread(gyro_listener).start();
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
		accel_listener.shutDown();
		gyro_listener.shutDown();
	}

	protected void shutDown() {
		timeToExit = true;
	}

}
