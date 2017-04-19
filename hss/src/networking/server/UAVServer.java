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
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import networking.server.connection.ConnectionHandlerFactory;
import networking.server.connection.DataConnectionHandler;

public class UAVServer{

	private static final int MC_PORT_NUM							= 9002;
	public static final int APP_PORT_NUM							= 9003;
	@SuppressWarnings("unused")
	private static Logger logger									= Logger.getLogger(UAVServer.class.getName());
	private static volatile boolean timeToExit						= false;	// Operator input thread uses this to alert server that it's time to shut down.
	private static List<DataConnectionHandler> handlers				= Collections.synchronizedList(new ArrayList<DataConnectionHandler>());
	private static ObjectOutputStream telemetryStream;

	/**
	 * @param args
	 */
	public static void main(String[] args)
			throws IOException
	{
		/* Thread that takes in operator input. Used to shut down the server, among other things (possibly). */
		Thread operator_input = new Thread(new UAVServerOperatorInput());
		operator_input.start();

		/* Listens for incoming application connection. */
		ServerSocket app_listener = new ServerSocket(APP_PORT_NUM);
		app_listener.setSoTimeout(500);
		//MainAppConnection mac = null;
		while(!timeToExit)
		{
			/*
			 * Grab the needed view controllers from the main application.
			 */
			try
			{
				Socket app_sock = app_listener.accept();
				telemetryStream = new ObjectOutputStream(app_sock.getOutputStream());
				/*
				mac = new MainAppConnection(telemetryStream);
				Thread telDataUpdater = new Thread(mac);
				telDataUpdater.start();
				*/
				break;
			}
			catch(SocketTimeoutException ste) {
				//ste.printStackTrace();
			}
		}
		app_listener.close();

		/* Listens for incoming client connections and spawns appropriate threads. */
		ServerSocket cli_listener = new ServerSocket(MC_PORT_NUM);
		cli_listener.setSoTimeout(500);
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
				Thread handlerFactory = new Thread(new ConnectionHandlerFactory(cli_sock));
				handlerFactory.start();
			}
			catch(SocketTimeoutException ste) {

			}
		}
		cli_listener.close();
		//mac.shutDown();
		shutDownHandlers();

	}

	public static void shutDown() {
		timeToExit = true;
	}

	public static void addHandler(DataConnectionHandler handler){
		synchronized(handlers) {
			handlers.add(handler);
		}

		/*
		 * In case factory is trying to add a new thread while Server is in the process
		 * of shutting down, shut new thread down.
		 */
		if(timeToExit) {
			handler.shutDown();
		}
	}

	private static void shutDownHandlers() {
		synchronized(handlers) {
			for(int i = 0; i < handlers.size(); i++) {
				handlers.get(i).shutDown();
			}
		}
	}

	public static int getPortNum() {
		return MC_PORT_NUM;
	}

	public static void setAirTemp(double temp) {
		System.out.println("Setting telData.airTemp to " + temp);
		try {
			telemetryStream.writeDouble(temp);
			telemetryStream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
