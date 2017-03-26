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
package networking;

import java.util.logging.Logger;

public class UAVClient {
	
	private static final int PORT_NUM						= 4444;
	@SuppressWarnings("unused")
	private static Logger logger							= Logger.getLogger(UAVClient.class.getName());
	private static volatile boolean timeToExit				= false;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
