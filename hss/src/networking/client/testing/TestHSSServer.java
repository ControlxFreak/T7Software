package networking.client.testing;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import networking.client.UAVClient;

public class TestHSSServer{

	private static volatile boolean timeToExit				= false;
	private static List<TestConnectionHandler> handlers	= Collections.synchronizedList(new ArrayList<TestConnectionHandler>());
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
			throws IOException
	{
		/* Thread that takes in operator input. Used to shut down the server, among other things (possibly). */
		Thread operator_input = new Thread(new TestOperatorInput());
		operator_input.start();
		
		/* Listens for incoming client connections and spawns appropriate threads. */
		ServerSocket listener = new ServerSocket(UAVClient.getPortNum());
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
				TestConnectionHandler handler = new TestConnectionHandler(cli_sock);
				handlers.add(handler);
				new Thread(handler).start();
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
	
	private static void shutDownHandlers() {
		synchronized(handlers) {
			for(int i = 0; i < handlers.size(); i++) {
				handlers.get(i).shutDown();
			}
		}
	}

}
