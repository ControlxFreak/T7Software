/*
 * ---------------------------------------------------------------------------------
 * Title: DataConnectionHandler.java
 * Description:
 * An abstract class for handling main computer client connections.
 * ---------------------------------------------------------------------------------
 * Lockheed Martin
 * Engineering Leadership Development Program
 * Team 7
 * 14 March 2017
 * Jarrett Mead
 * ---------------------------------------------------------------------------------
 * Change Log
 * 	14 March 2017 - Jarrett Mead - Class Birthday
 * ---------------------------------------------------------------------------------
 */
package networking.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;
import java.util.logging.Logger;

import T7.T7Messages.GenericMessage;
import T7.T7Messages.GenericMessage.MsgType;

public class DataConnectionHandler
	implements Runnable{

	private InputStream in;
	private UAVServer server;
	volatile boolean timeToExit = false;
	static Logger logger = Logger.getLogger(DataConnectionHandler.class.getName());;
	//DataType connType;
	private MsgType connType;
	private Consumer<GenericMessage> handlerMethod;

	public DataConnectionHandler(UAVServer server, InputStream in) {
		this.server = server;
		this.in = in;
	}

	public void shutDown() {
		timeToExit = true;
	}

	@Override
	public void run() {
		logger.info("Running handler!");
		try {
			while(in.available()<1) {

			}
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			shutDown();
		}
		try {
			System.out.println("Parsing message.");
			GenericMessage gm = GenericMessage.parseDelimitedFrom(in);
			connType = gm.getMsgtype();
			switch(connType) {
			case ACCEL:
				break;
			case GYRO:
				break;
			case ALTITUDE:
				handlerMethod = this::handleAltitudeMessage;
				break;
			case ATTITUDE:
				break;
			case TEMP:
				handlerMethod = this::handleAirTempMessage;
				break;
			case BAT:
				break;
			default:
				break;
			}
			handlerMethod.accept(gm);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		while(!timeToExit) {
			GenericMessage gm;
			try {
				gm = GenericMessage.parseDelimitedFrom(in);
				handlerMethod.accept(gm);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					logger.info(connType + " client disconnected.");
					in.close();
					server.updateTelemetryData(Double.MIN_VALUE, connType);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				shutDown();
			}
		}
	}

	private void handleAirTempMessage(GenericMessage gm) {
		double temp = gm.getTemp().getTemp();
		server.updateTelemetryData(temp, connType);
	}

	private void handleAltitudeMessage(GenericMessage gm) {
		double alt = gm.getAltitude().getAlt();
		server.updateTelemetryData(alt, connType);
	}

}
