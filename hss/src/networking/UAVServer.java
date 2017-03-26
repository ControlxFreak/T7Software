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
package networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import networking.handling.ConnectionHandlerFactory;
import networking.handling.DataConnectionHandler;

public class UAVServer{
	
	private static final int PORT_NUM						= 4444;
	@SuppressWarnings("unused")
	private static Logger logger							= Logger.getLogger(UAVServer.class.getName());
	private static volatile boolean timeToExit				= false;	// Operator input thread uses this to alert server that it's time to shut down.
	private static List<DataConnectionHandler> handlers		= Collections.synchronizedList(new ArrayList<DataConnectionHandler>());

	/**
	 * @param args
	 */
	public static void main(String[] args)
			throws IOException
	{
		/* Thread that takes in operator input. Used to shut down the server, among other things (possibly). */
		Thread operator_input = new Thread(new UAVServerOperatorInput());
		operator_input.start();
		
		/* Listens for incoming client connections and spawns appropriate threads. */
		ServerSocket listener = new ServerSocket(PORT_NUM);
		listener.setSoTimeout(500);
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
				Socket cli_sock = listener.accept();
				Thread handlerFactory = new Thread(new ConnectionHandlerFactory(cli_sock));
				handlerFactory.start();
			}
			catch(SocketTimeoutException ste) {
				
			}
		}
		listener.close();
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

}
