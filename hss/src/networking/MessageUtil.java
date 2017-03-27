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
	
	public static final int HEADER_LEN = 5;
	public static final int MAX_MC_MESSAGE_LEN = 512;

	/* TCP HEADER DATA ID NUMBERS */
	public static final int ACCELEROMETER_DATA	= 200;
	public static final int GYROSCOPE_DATA		= 201;
	public static final int ALTITUDE_DATA		= 202;
	public static final int TEMPERATURE_DATA	= 203;
	public static final int CAMERA_DATA			= 204;
	public static final int CONNECTION_REQUEST	= 205;

	public MessageUtil() {
		
	}
	
	/**
	 * This method dynamically reads a message from the Main
	 * Computer and stores it in a buffer. It returns the
	 * number of bytes of data read. If the message was empty
	 * (the client disconnected), the method returns 0.
	 * @param br
	 * @param cbuf
	 * @return size of the data section of the message
	 */
	public static int readMcMessage(BufferedReader br, char[] cbuf) {
		if(br == null) {
			return -1;
		}
		
		int numRead = -1;
		try {
			numRead = br.read(cbuf, 0, HEADER_LEN);
		} catch (IOException e) {
			// TODO Error logging
		}
		
		/* Finish reading header if less than 5 bytes were read. */
		while(numRead < HEADER_LEN && numRead != -1) {
			try {
				int tempNum = br.read(cbuf, numRead, HEADER_LEN-numRead);
				if(tempNum > -1) {
					numRead += tempNum;
				}
			} catch (IOException e) {
				// TODO Error logging
			}
		}
		
		int dataSize = mcMessageDataSize(new String(cbuf));
		try {
			numRead = br.read(cbuf, HEADER_LEN, dataSize);
		} catch (IOException e) {
			// TODO Error logging
		}
		return dataSize;
	}
	
	public static int mcMessageDataSize(String message) {
		if(message.isEmpty() || message == null || message.charAt(0) == 0) {
			return 0;
		}
		return Integer.parseInt(message.substring(3, 5));
	}
	
	public static char[] concat(char[] a, char[] b) {
		int aLen = a.length;
		int bLen = b.length;
		char[] c = new char[aLen+bLen];
		System.arraycopy(a, 0, c, 0, aLen);
		System.arraycopy(b, 0, c, aLen, bLen);
		return c;
	}
	
}