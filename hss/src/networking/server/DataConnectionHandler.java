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
		System.out.println("connType = " + connType);
		System.out.println("Paused? = " + !MainApp.isDataTypeUnpaused(connType));
		logger.finest("Setting handler.");
		if(MainApp.isDataTypeUnpaused(connType)) {
			switch(connType) {
			case ACCEL:
				handlerMethod = this::handleAccelerationMessage;
				break;
			case GYRO:
				handlerMethod = this::handleGyroscopeMessage;
				break;
			case ALTITUDE:
				handlerMethod = this::handleAltitudeMessage;
				break;
			case ATTITUDE:
				handlerMethod = this::handleAttitudeMessage;
				break;
			case TEMP:
				handlerMethod = this::handleAirTempMessage;
				break;
			case BAT:
				handlerMethod = this::handleBatteryMessage;
				break;
			case HEAD:
				handlerMethod = this::handleHeadingMessage;
				break;
			case THERMAL_RESPONSE:
				handlerMethod = this::handleThermalResponseMessage;
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
		double x = gm.getAccel().getX();
		double y = gm.getAccel().getY();
		double z = gm.getAccel().getZ();
		server.updateTelemetryData(x, y, z, connType);
	}

	private void handleGyroscopeMessage(GenericMessage gm) {
		double x = gm.getGyro().getX();
		double y = gm.getGyro().getY();
		double z = gm.getGyro().getZ();
		server.updateTelemetryData(x, y, z, connType);
	}

	private void handleAttitudeMessage(GenericMessage gm) {
		double x = gm.getAttitude().getRoll();
		double y = gm.getAttitude().getPitch();
		double z = gm.getAttitude().getYaw();
		server.updateTelemetryData(x, y, z, connType);
	}

	private void handleBatteryMessage(GenericMessage gm) {
		double percent = gm.getBat().getPercent();
		server.updateTelemetryData(percent, connType);
	}
	
	private void handleHeadingMessage(GenericMessage gm) {
		double heading = gm.getHead().getHeading();
		server.updateTelemetryData(heading, connType);
	}
	
	private void handleThermalResponseMessage(GenericMessage gm) {
		double response = gm.getThermalresponse().getResponse();
		server.updateSnapshotThermalReading(response);
	}

	private void handlePaused(GenericMessage gm) {
		/*
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
		*/
		
		server.clearTelemetryData(connType);
	}

}
