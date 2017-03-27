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
package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Logger;

public class UAVClient {

	private static final int PORT_NUM						= 9001;
	@SuppressWarnings("unused")
	private static Logger logger							= Logger.getLogger(UAVClient.class.getName());
	private volatile boolean timeToExit						= false;

	public UAVClient() {
		String input = null;
		boolean time_to_exit = false;
		boolean valid_input_received = false;
		final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		Socket mc_sock;
		BufferedWriter out;
		
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
		
		do {
			try {
				valid_input_received = true;
				
				System.out.println();
				System.out.println("Welcome to the test UAV client.");
				System.out.println("What would you like to do?");
				System.out.println("0) Exit");
				System.out.println("1) Make a new Accelerometer connection");
				System.out.println("2) Make a new Altitude connection");
				System.out.println("3) Make a new Camera connection");
				System.out.println("4) Make a new Gyroscope connection");
				System.out.println("5) Make a new Temperature connection");
				
				while(!in.ready()) {
					Thread.sleep(500);
				}
				input = in.readLine();
				
				switch(Integer.parseInt(input)) {
				case 0:
					System.out.println("Exiting.");
					time_to_exit = true;
					break;
				case 1:
					System.out.println("Sending Accelerometer request.");
					sendRequest(200);
					break;
				case 2:
					System.out.println("Sending Altitude request.");
					sendRequest(202);
					break;
				case 3:
					System.out.println("Sending Camera request.");
					sendRequest(204);
					break;
				case 4:
					System.out.println("Sending Gyroscope request.");
					sendRequest(201);
					break;
				case 5:
					System.out.println("Sending Temperature request.");
					sendRequest(203);
					break;
				default:
					valid_input_received = false;
					System.out.println("Invalid input.");
					break;
				}
			} catch(IOException ioe) {
				// TODO Error Logging
			} catch(InterruptedException ie) {
				// TODO Error Logging
			} catch(NumberFormatException nfe) {
				valid_input_received = false;
				System.out.println();
				System.out.println("Wrong input format.");
				System.out.println();
			} finally {
				try {
					out.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} while(!time_to_exit && !valid_input_received);
		
		if(time_to_exit) {
			try {
				mc_sock.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		do {
			try {
				valid_input_received = true;
				
				System.out.println();
				System.out.println("What would you like to do?");
				System.out.println("0) Exit");
				System.out.println("1) Send data");
				
				while(!in.ready()) {
					Thread.sleep(500);
				}
				input = in.readLine();
				
				switch(Integer.parseInt(input)) {
				case 0:
					System.out.println("Exiting.");
					time_to_exit = true;
					break;
				case 1:
					System.out.println("Sending data.");
					sendData();
					break;
				default:
					System.out.println("Invalid input.");
					break;
				}
			} catch(IOException ioe) {
				// TODO Error Logging
			} catch(InterruptedException ie) {
				// TODO Error Logging
			} catch(NumberFormatException nfe) {
				System.out.println();
				System.out.println("Wrong input format.");
				System.out.println();
			} finally {
				try {
					out.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} while(!time_to_exit);

	}

}
