package networking.client.testing;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.function.Consumer;

import T7.T7Messages.GenericMessage;
import T7.T7Messages.GenericMessage.MsgType;
import T7.T7Messages.MoveCamera.ArrowKeys;

public class TestConnectionHandler implements Runnable {

	private boolean timeToExit = false;
	private InputStream in = null;
	private MsgType connType;
	private Consumer<GenericMessage> handlerMethod;

	public TestConnectionHandler(Socket cli_sock) {
		while(!timeToExit && in == null) {
			System.out.println("Establishing connection.");
			try {
				Thread.sleep(1000);
				in = cli_sock.getInputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		System.out.println("Running handler!");
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
			switch(connType) {
			//case UPDATE_PARAM:
			//	break;
			case CONFIG_DATA:
				break;
			case MOVE_CAMERA:
				handlerMethod = this::handleCameraMessage;
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
					System.out.println(connType + " client disconnected.");
					in.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				shutDown();
			}
		}
	}

	public void shutDown() {
		timeToExit = true;
	}

	private void handleCameraMessage(GenericMessage gm) {
		int arrowKey = gm.getMovecamera().getArrowKey();
		System.out.println("arrowKey = " + ArrowKeys.forNumber(arrowKey));
	}

}
