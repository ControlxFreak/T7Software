/*
 * ---------------------------------------------------------------------------------
 * Title: SensorDataConfigurationMessenger.java
 * Description:
 * A wrapper class for UAVClient that provides methods for sending sensor
 * data configuration updates to the main computer.
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

public class SensorDataConfigurationMessenger {
	
	private final UAVClient client = new UAVClient();
	
	public void sendRequest() {
		client.sendMessage("10303101".toCharArray());
	}
	
	public void updateSensorConfiguration(char[] sensor_id, boolean setting) {
		int binary_setting = setting ? 1 : 0;
		char[] data = MessageUtil.concat(sensor_id, Integer.toString(binary_setting).toCharArray());
		int data_size = data.length;
		char[] size_array;
		if(data_size < 10) {
			size_array = MessageUtil.concat("0".toCharArray(), Integer.toString(data_size).toCharArray());
		} else {
			size_array = Integer.toString(data_size).toCharArray();
		}
		char[] header = MessageUtil.concat("101".toCharArray(), size_array);
		client.sendMessage(MessageUtil.concat(header, data));
	}

}
