/*
 * ---------------------------------------------------------------------------------
 * Title: CameraMovementMessenger.java
 * Description:
 * A wrapper class for UAVClient that provides methods for sending
 * camera pan/tilt commands to the main computer.
 * ---------------------------------------------------------------------------------
 * Lockheed Martin
 * Engineering Leadership Development Program
 * Team 7
 * 26 March 2017
 * Jarrett Mead
 * ---------------------------------------------------------------------------------
 * Change Log
 * 	26 March 2017 - Jarrett Mead - Class Birthday
 * ---------------------------------------------------------------------------------
 */
package networking.client.messaging;

import networking.MessageUtil;
import networking.client.UAVClient;

public class CameraMovementMessenger {
	
	private final UAVClient client = new UAVClient();
	
	public void sendRequest() {
		client.sendMessage("10303102".toCharArray());
	}
	
	public void updateCameraOrientation(char[] data) {
		int data_size = data.length;
		char[] size_array;
		if(data_size < 10) {
			size_array = MessageUtil.concat("0".toCharArray(), Integer.toString(data_size).toCharArray());
		} else {
			size_array = Integer.toString(data_size).toCharArray();
		}
		char[] header = MessageUtil.concat("102".toCharArray(), size_array);
		client.sendMessage(MessageUtil.concat(header, data));
	}

}
