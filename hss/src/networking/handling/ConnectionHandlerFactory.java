/*
 * ---------------------------------------------------------------------------------
 * Title: ConnectionHandlerFactory.java
 * Description:
 * A factory that instantiates handler classes for different types of connections.
 * ---------------------------------------------------------------------------------
 * Lockheed Martin
 * Engineering Leadership Development Program
 * Team 7
 * 12 March 2017
 * Jarrett Mead
 * ---------------------------------------------------------------------------------
 * Change Log
 * 	12 March 2017 - Jarrett Mead - Factory Birthday
 * ---------------------------------------------------------------------------------
 */
package networking.handling;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;

import networking.MessageUtil;
import networking.UAVServer;

public class ConnectionHandlerFactory implements Runnable {
	
	private Socket socket;

	public ConnectionHandlerFactory(Socket socket) {
		this.socket = socket;
	}
	
	private DataConnectionHandler getConnectionHandler() {
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			// TODO Error logging
			return null;
		}
		char[] message = new char[512];
		
		do {
			MessageUtil.readMcMessage(br, message);
		} while(Integer.parseInt(new String(Arrays.copyOfRange(message, 0, 3))) != MessageUtil.CONNECTION_REQUEST);
		
		switch(Integer.parseInt(new String(Arrays.copyOfRange(message, 5, 8)))) {
		case MessageUtil.ACCELEROMETER_DATA:
			return new AccelerometerDataConnectionHandler(br);
		case MessageUtil.GYROSCOPE_DATA:
			return new GyroscopeDataConnectionHandler(br);
		case MessageUtil.ALTITUDE_DATA:
			return new AltitudeDataConnectionHandler(br);
		case MessageUtil.TEMPERATURE_DATA:
			return new TemperatureDataConnectionHandler(br);
		case MessageUtil.CAMERA_DATA:
			return new CameraDataConnectionHandler(br);
		default:
			// TODO Error logging.
			return null;
		}
	}

	public void run() {
		DataConnectionHandler handler = getConnectionHandler();
		UAVServer.addHandler(handler);
		new Thread((Runnable)handler).start();
	}

}
