package networking.client.testing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import networking.MessageUtil;

public class TestConnectionHandler implements Runnable {
	
	BufferedReader br;
	private boolean timeToExit = false;
	private char[] cbuf = new char[MessageUtil.MAX_MC_MESSAGE_LEN];

	public TestConnectionHandler(Socket cli_sock) {
		try {
			br = new BufferedReader(new InputStreamReader(cli_sock.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while(!timeToExit && MessageUtil.readMcMessage(br, cbuf) != 0) {
			System.out.println("Thanks for the message! :-)");
			cbuf = new char[MessageUtil.MAX_MC_MESSAGE_LEN];
		}
		System.out.println("Shutting down.");
	}

	public void shutDown() {
		timeToExit = true;
	}

}
