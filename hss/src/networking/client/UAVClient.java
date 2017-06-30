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

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Logger;

import T7.T7Messages.GenericMessage;
import T7.T7Messages.GenericMessage.MsgType;

public class UAVClient implements Runnable{

	private static final int PORT_NUM		= 9001;
	private static Logger logger			= Logger.getLogger(UAVClient.class.getName());
	private Socket mc_sock;
	private volatile boolean timeToExit = false;

	public void sendMessage(GenericMessage gm) throws SocketException {
		try {
			logger.info("Sending " + MsgType.forNumber(gm.getMsgtype()) + " message: " + gm.toString());
			gm.writeDelimitedTo(mc_sock.getOutputStream());
		} catch (SocketException e) {
			throw e;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void finalize() throws Throwable {
		try {
			mc_sock.close();
		} finally {
			super.finalize();
		}
	}

	public static int getPortNum() {
		return PORT_NUM;
	}

	@Override
	public void run() {

		while(!timeToExit) {
			try {
				Thread.sleep(1000);
				System.out.println("Establishing connection.");
				//mc_sock = new Socket(InetAddress.getLocalHost(), PORT_NUM);
				mc_sock = new Socket("127.0.0.1",9001);

			} catch(Exception e) {
				logger.finer(e.getMessage());
				continue;
			}
			break;
		}

		while(!timeToExit) {

		}

		try {
			if(mc_sock != null) {
				mc_sock.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void shutDown() {
		timeToExit = true;
	}

}
