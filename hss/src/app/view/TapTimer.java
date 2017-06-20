package app.view;

import java.util.function.Consumer;

import app.MainApp;
import javafx.application.Platform;
import javafx.scene.Node;

class TapTimer implements Runnable {
	
	private Tapper tapper;
	private Node node;
	private Consumer<Node> singleTap;
	private Consumer<Node> doubleTap;
	
	public TapTimer(Tapper tapper, Node node) {
		this.tapper = tapper;
		this.node = node;
		
		if(tapper instanceof SnapshotExplorerController) {
			singleTap = MainApp.getSnapshotExplorerController()::singleTap;
			doubleTap = MainApp.getSnapshotExplorerController()::doubleTap;
		} else if(tapper instanceof DataConfigurationDialogController) {
			singleTap = MainApp.getConfigController()::singleTap;
			doubleTap = MainApp.getConfigController()::doubleTap;
		}
	}

	@Override
	public void run() {
		System.out.println("Kicking off timer.");
		try {
			Thread.sleep(400);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if(tapper.isSingleTap()) {
			Platform.runLater(() -> singleTap.accept(node));
		} else {
			Platform.runLater(() -> doubleTap.accept(node));
		}
	}

}
