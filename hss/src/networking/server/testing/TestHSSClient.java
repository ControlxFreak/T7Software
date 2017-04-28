package networking.server.testing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import T7.T7Messages.*;
import T7.T7Messages.GenericMessage.MsgType;
import networking.server.UAVServer;

public class TestHSSClient {

	private static OutputStream out;

	public TestHSSClient() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String input = null;
		boolean time_to_exit = false;
		boolean valid_input_received = false;
		final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		Socket hss_sock;
		MsgType connType = null;

		do {
			try {
				Thread.sleep(1000);
				System.out.println("Establishing connection.");
				hss_sock = new Socket(InetAddress.getLocalHost(), UAVServer.getPortNum());
			} catch(Exception e) {
				e.printStackTrace();
				continue;
			}
			break;
		} while(true);

		try {
			out = hss_sock.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		do {
			try {
				valid_input_received = true;

				System.out.println();
				System.out.println("Welcome to the test MC client.");
				System.out.println("What would you like to do?");
				System.out.println("0) Exit");
				System.out.println("1) Make a new Accelerometer connection");
				System.out.println("2) Make a new Altitude connection");
				System.out.println("3) Make a new Gyroscope connection");
				System.out.println("4) Make a new Temperature connection");

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
					connType = MsgType.ACCEL;
					break;
				case 2:
					System.out.println("Sending Altitude request.");
					connType = MsgType.ALTITUDE;
					break;
				case 3:
					System.out.println("Sending Gyroscope request.");
					connType = MsgType.GYRO;
					break;
				case 4:
					System.out.println("Sending Temperature request.");
					connType = MsgType.TEMP;
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
			}
			/*
			finally {
				try {
					out.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			*/
		} while(!time_to_exit && !valid_input_received);

		if(time_to_exit) {
			try {
				hss_sock.close();
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
					System.out.println("Enter data.");

					while(!in.ready()) {
						Thread.sleep(500);
					}
					input = in.readLine();

					System.out.println("Sending data.");
					GenericMessage.Builder gm = GenericMessage.newBuilder();
					gm.setTime(System.currentTimeMillis());
					switch(connType) {
					case ACCEL:
						gm.setMsgtype(MsgType.ACCEL)
							.setAccel(Accel.newBuilder().setX(1.0).setY(2.0).setZ(3.0));
						break;
					case ALTITUDE:
						gm.setMsgtype(MsgType.ALTITUDE)
							.setAltitude(Altitude.newBuilder().setAlt(Double.parseDouble(input)));
						break;
					case GYRO:
						gm.setMsgtype(MsgType.GYRO)
							.setGyro(Gyro.newBuilder().setX(3.0).setY(2.0).setZ(1.0));
						break;
					case TEMP:
						gm.setMsgtype(MsgType.TEMP)
							.setTemp(Temp.newBuilder().setTime(System.currentTimeMillis())
									.setTemp(Double.parseDouble(input)));
						break;
					default:
						break;
					}
					gm.build().writeDelimitedTo(out);
					out.flush();
					System.out.println("Sent message.");
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
