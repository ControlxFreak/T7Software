/*
 * ---------------------------------------------------------------------------------
 * Title: UAVClientService.java
 * Description:
 * This class spawns messengers for the different data types.
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

import networking.client.messaging.*;

public class UAVClientService {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CameraMovementMessenger cmm				= new CameraMovementMessenger();
		MissionParametersMessenger mpm			= new MissionParametersMessenger();
		SensorDataConfigurationMessenger sdcm	= new SensorDataConfigurationMessenger();
		
		cmm.sendRequest();
		mpm.sendRequest();
		sdcm.sendRequest();
		
		cmm.updateCameraOrientation("406".toCharArray());
		mpm.updateMissionParameter("300".toCharArray(), "blah,blah,blah".toCharArray());
		sdcm.updateSensorConfiguration("4278".toCharArray(), false);
	}

}
