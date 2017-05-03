/*
 * ---------------------------------------------------------------------------------
 * Title: UAVServer.java
 * Description:
 * This class implements the TCP Server that handles data sent by the main computer.
 * ---------------------------------------------------------------------------------
 * Lockheed Martin
 * Engineering Leadership Development Program
 * Team 7
 * 12 March 2017
 * Jarrett Mead
 * ---------------------------------------------------------------------------------
 * Change Log
 * 	12 March 2017 - Jarrett Mead - Server Birthday
 * ---------------------------------------------------------------------------------
 */
package networking.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import T7.T7Messages.GenericMessage.MsgType;
import app.MainApp;
import javafx.application.Platform;

public class UAVServer implements Runnable {

	private static final int MC_PORT_NUM							= 9002;
	@SuppressWarnings("unused")
	private static Logger logger									= Logger.getLogger(UAVServer.class.getName());
	private volatile boolean timeToExit								= false;	// Main app uses this to alert server that it's time to shut down.
	private List<DataConnectionHandler> handlers					= new ArrayList<DataConnectionHandler>();

	@Override
	public void run()
	{

		/* Listens for incoming client connections and spawns appropriate threads. */
		ServerSocket cli_listener = null;
		try {
			cli_listener = new ServerSocket(MC_PORT_NUM);
			cli_listener.setSoTimeout(500);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		while(!timeToExit)
		{
			/*
			 * For each incoming connection, spawn a thread to create a handler thread of the
			 * appropriate type, then continue accepting connections. This is necessary because
			 * the handler factory waits for a connection request message before starting a
			 * handler thread. We don't want the listener thread to get stuck waiting on one
			 * connection.
			 */
			try
			{
				Socket cli_sock = cli_listener.accept();
				DataConnectionHandler handler = new DataConnectionHandler(this, cli_sock.getInputStream());
				handlers.add(handler);
				Thread handlerThread = new Thread(handler);
				handlerThread.start();
			}
			catch(SocketTimeoutException ste) {

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			cli_listener.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		shutDownHandlers();

	}

	public void shutDown() {
		timeToExit = true;
	}

	private void shutDownHandlers() {
		synchronized(handlers) {
			for(int i = 0; i < handlers.size(); i++) {
				handlers.get(i).shutDown();
			}
		}
	}

	public static int getPortNum() {
		return MC_PORT_NUM;
	}

	public void updateTelemetryData(double datum, MsgType type) {
		System.out.println("Setting " + type + " to " + datum);

		Platform.runLater(() -> MainApp.updateTelemetryDisplay(datum, type));
	}

}
