/*
 * ---------------------------------------------------------------------------------
 * Title: UAVClientManager.java
 * Description:
 * This class manages connections from the HSS to the UAV.
 * ---------------------------------------------------------------------------------
 * Lockheed Martin
 * Engineering Leadership Development Program
 * Team 7
 * 30 April 2017
 * Jarrett Mead
 * ---------------------------------------------------------------------------------
 * Change Log
 * 	30 April 2017 - Jarrett Mead - Class Birthday
 * ---------------------------------------------------------------------------------
 */
package networking.client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.logging.Logger;

import T7.T7Messages.GenericMessage.MsgType;
import networking.server.UAVServer;

public class UAVClientManager implements Runnable {
	private static Logger logger = Logger.getLogger(UAVClientManager.class.getName());
	private volatile boolean timeToExit = false;

	@Override
	public void run() {
		try {

			initClients();

			//repairListeners();

			//shutDownListeners();

			try {
				server_socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch(Exception e) {
			logger.warning("Exception in UAVClientManager: " + e.toString());
		}
	}

	private void repairListeners() {
		System.out.println("repairListeners()");
		while(!timeToExit) {

		}
	}

	private void initClients() {

		/* Establish temperature data listener */
		while(!timeToExit) {
			try {
				Socket temperature_sock = server_socket.accept();
				temperature_listener = new DoubleDataListener(temperature_sock, mainApp, MsgType.TEMP);
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
				altitude_listener = new DoubleDataListener(altitude_sock, mainApp, MsgType.ALTITUDE);
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
				accel_listener = new VectorDataListener(accelerometer_sock, mainApp, MsgType.ACCEL);
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
				gyro_listener = new VectorDataListener(gyroscope_sock, mainApp, MsgType.GYRO);
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
