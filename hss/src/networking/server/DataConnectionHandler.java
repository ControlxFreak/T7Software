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
import app.MainApp;

public class DataConnectionHandler
	implements Runnable{

	private InputStream in;
	private UAVServer server;
	volatile boolean timeToExit = false;
	static Logger logger = Logger.getLogger(DataConnectionHandler.class.getName());
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
			connType = MsgType.forNumber(gm.getMsgtype());
			setHandler();
			handlerMethod.accept(gm);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		while(!timeToExit) {
			GenericMessage gm;
			try {
				gm = GenericMessage.parseDelimitedFrom(in);
				setHandler();
				if(gm == null) {
					throw new Exception();
				}
				handlerMethod.accept(gm);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					logger.info(connType + " client disconnected.");
					in.close();
					server.clearTelemetryData(connType);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				shutDown();
			}
		}
	}

	private void setHandler() {
		System.out.println("SETTING HANDLER");
		System.out.println("Paused? = " + !MainApp.isDataTypeUnpaused(connType));
		logger.finest("Setting handler.");
		if(MainApp.isDataTypeUnpaused(connType)) {
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
				logger.warning("Unrecognized connection type.");
				break;
			}
		} else {
			handlerMethod = this::handlePaused;
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

	private void handleAccelerationMessage(GenericMessage gm) {
	}

	private void handleGyroscopeMessage(GenericMessage gm) {
	}

	private void handleAttitudeMessage(GenericMessage gm) {
	}

	private void handleBatteryMessage(GenericMessage gm) {
	}

	private void handlePaused(GenericMessage gm) {
		switch(connType) {
		case TEMP:
		case ALTITUDE:
		case ATTITUDE:
		case BAT:
			server.clearTelemetryData(connType);
			break;
		case ACCEL:
		case GYRO:
			break;
		default:
			break;
		}
	}

}
