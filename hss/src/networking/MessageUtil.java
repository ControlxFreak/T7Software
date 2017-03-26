/*
 * ---------------------------------------------------------------------------------
 * Title: MessageUtil.java
 * Description:
 * A utility class for messaging.
 * ---------------------------------------------------------------------------------
 * Lockheed Martin
 * Engineering Leadership Development Program
 * Team 7
 * 13 March 2017
 * Jarrett Mead
 * ---------------------------------------------------------------------------------
 * Change Log
 * 	13 March 2017 - Jarrett Mead - Class Birthday
 * ---------------------------------------------------------------------------------
 */
package networking;

import java.io.BufferedReader;
import java.io.IOException;

public class MessageUtil {
	
	public static final int MC_HEADER_LEN = 5;

	/* TCP HEADER DATA ID NUMBERS */
	public static final int ACCELEROMETER_DATA	= 200;
	public static final int GYROSCOPE_DATA		= 201;
	public static final int ALTITUDE_DATA		= 202;
	public static final int TEMPERATURE_DATA	= 203;
	public static final int CAMERA_DATA			= 204;
	public static final int CONNECTION_REQUEST	= 205;

	public MessageUtil() {
		
	}
	
	public static int readMcMessage(BufferedReader br, char[] cbuf) {
		if(br == null) {
			return -1;
		}
		
		int numRead = -1;
		try {
			numRead = br.read(cbuf, 0, MC_HEADER_LEN);
		} catch (IOException e) {
			// TODO Error logging
		}
		
		/* Finish reading header if less than 5 bytes were read. */
		while(numRead < MC_HEADER_LEN) {
			try {
				int tempNum = br.read(cbuf, numRead, MC_HEADER_LEN-numRead);
				if(tempNum > -1) {
					numRead += tempNum;
				}
			} catch (IOException e) {
				// TODO Error logging
			}
		}
		
		int messageSize = mcMessageDataSize(new String(cbuf)) + 5;
		try {
			numRead = br.read(cbuf, MC_HEADER_LEN, messageSize-MC_HEADER_LEN);
		} catch (IOException e) {
			// TODO Error logging
		}
		
		return messageSize;
	}
	
	public static int mcMessageDataSize(String message) {
		return Integer.parseInt(message.substring(3, 5));
	}
	
}