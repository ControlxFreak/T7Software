/*
 * ---------------------------------------------------------------------------------
 * Title: UAVClient.java
 * Description:
 * This class implements the TCP Client that sends data to the main computer.
 * ---------------------------------------------------------------------------------
 * Lockheed Martin
 * Engineering Leadership Development Program
 * Team 7
 * 26 March 2017
 * Jarrett Mead
 * ---------------------------------------------------------------------------------
 * Change Log
 * 	26 March 2017 - Jarrett Mead - Client Birthday
 * ---------------------------------------------------------------------------------
 */
package networking.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Logger;

public class UAVClient {

	private static final int PORT_NUM		= 9001;
	@SuppressWarnings("unused")
	private static Logger logger			= Logger.getLogger(UAVClient.class.getName());
	Socket mc_sock;
	BufferedWriter out;

	public UAVClient() {
		
		do {
			try {
				Thread.sleep(1000);
				System.out.println("Establishing connection.");
				mc_sock = new Socket(InetAddress.getLocalHost(), PORT_NUM);
			} catch(Exception e) {
				e.printStackTrace();
				continue;
			}
			break;
		} while(true);
		
		try {
			out = new BufferedWriter(new OutputStreamWriter(mc_sock.getOutputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void sendMessage(char[] cbuf) {
		try {
			out.write(cbuf);
		} catch (IOException e) {
			// TODO Error logging
		}
	}
	
	protected void finalize() throws Throwable {
		try {
			out.close();
		} finally {
			super.finalize();
		}
	}

}
