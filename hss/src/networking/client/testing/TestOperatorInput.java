package networking.client.testing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TestOperatorInput implements Runnable {

	private static final int EXIT = 0;

	private boolean shut_down = false;

	private final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

	public void run() {
			String input = null;
			do {
				try {
					System.out.println();
					System.out.println("What would you like to do?");
					System.out.println("0) Exit");

					while(!in.ready()) {
						Thread.sleep(500);
					}
					input = in.readLine();

					if(!isValidInput(input)) {
						System.out.println();
						System.out.println("Invalid input. Please retry.");
						continue;
					} else {
						if(Integer.parseInt(input) == EXIT) {
							shut_down = true;
						}
					}
				} catch(IOException ioe) {
					// TODO Error Logging
				} catch(InterruptedException ie) {
					// TODO Error Logging
				} catch(NumberFormatException nfe) {
					// TODO Error Logging
				}
			} while(!shut_down);
			TestHSSServer.shutDown();
	}

	private static boolean isValidInput(String input) {
		try {
			int command = Integer.parseInt(input);
			if(command == EXIT) {
				return true;
			}
		} catch(NumberFormatException nfe) {
			// TODO Error Logging
		}
		return false;
	}
}
