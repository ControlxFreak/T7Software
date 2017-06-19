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
				System.out.println("2) Make a new Gyroscope connection");
				System.out.println("3) Make a new Altitude connection");
				System.out.println("4) Make a new Attitude connection");
				System.out.println("5) Make a new Temperature connection");
				System.out.println("6) Make a new Battery connection");
				System.out.println("7) Make a new Heading connection");
				System.out.println("8) Make a new Thermal Response connection");
				System.out.println("9) Make a new Heartbeat connection");
				System.out.println("10) Make a new WiFi connection");

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
					System.out.println("Sending Gyroscope request.");
					connType = MsgType.GYRO;
					break;
				case 3:
					System.out.println("Sending Altitude request.");
					connType = MsgType.ALTITUDE;
					break;
				case 4:
					System.out.println("Sending Attitude request.");
					connType = MsgType.ATTITUDE;
					break;
				case 5:
					System.out.println("Sending Temperature request.");
					connType = MsgType.TEMP;
					break;
				case 6:
					System.out.println("Sending Battery request.");
					connType = MsgType.BAT;
					break;
				case 7:
					System.out.println("Sending Heading request.");
					connType = MsgType.HEAD;
					break;
				case 8:
					System.out.println("Sending Thermal Response request.");
					connType = MsgType.THERMAL_RESPONSE;
					break;
				case 9:
					System.out.println("Sending Heartbeat request.");
					connType = MsgType.HEARTBEAT;
					break;
				case 10:
					System.out.println("Sending WiFi request.");
					connType = MsgType.WIFI;
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
					String input2;
					String input3;

					GenericMessage.Builder gm = GenericMessage.newBuilder();
					gm.setTime(System.currentTimeMillis());
					switch(connType) {
					case ACCEL:
						input2 = in.readLine();
						input3 = in.readLine();
						gm.setMsgtype(200)
							.setAccel(Accel.newBuilder().setX(Double.parseDouble(input))
									.setY(Double.parseDouble(input2)).setZ(Double.parseDouble(input3)));
						break;
					case GYRO:
						input2 = in.readLine();
						input3 = in.readLine();
						gm.setMsgtype(201)
							.setGyro(Gyro.newBuilder().setX(Double.parseDouble(input))
									.setY(Double.parseDouble(input2)).setZ(Double.parseDouble(input3)));
						break;
					case ALTITUDE:
						gm.setMsgtype(202)
							.setAltitude(Altitude.newBuilder().setAlt(Double.parseDouble(input)));
						break;
					case ATTITUDE:
						input2 = in.readLine();
						input3 = in.readLine();
						gm.setMsgtype(203)
							.setAttitude(Attitude.newBuilder().setRoll(Double.parseDouble(input))
									.setPitch(Double.parseDouble(input2)).setYaw(Double.parseDouble(input3)));
						break;
					case TEMP:
						gm.setMsgtype(204)
							.setTemp(Temp.newBuilder().setTemp(Double.parseDouble(input)));
						break;
					case BAT:
						gm.setMsgtype(205)
							.setBat(Battery.newBuilder().setPercent(Double.parseDouble(input)));
						break;
					case HEAD:
						gm.setMsgtype(206)
							.setHead(Heading.newBuilder().setHeading(Double.parseDouble(input)));
						break;
					case THERMAL_RESPONSE:
						gm.setMsgtype(207)
							.setThermalresponse(ThermalResponse.newBuilder().setResponse(Double.parseDouble(input)));
						break;
					case HEARTBEAT:
						boolean alive;
						alive = Double.parseDouble(input) == 1.0; 
						gm.setMsgtype(1)
							.setHeartbeat(HeartBeat.newBuilder().setAlive(alive));
						break;
					case WIFI:
						gm.setMsgtype(208)
							.setWifi(WiFi.newBuilder().setStrength(Double.parseDouble(input)));
						break;
					default:
						break;
					}
					System.out.println("Sending data.");
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
