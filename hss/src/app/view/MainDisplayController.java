/*
 * ---------------------------------------------------------------------------------
 * Title: MainDisplayController.java
 * Description:
 * The controller class for the telemetry data overview GUI.
 * ---------------------------------------------------------------------------------
 * Lockheed Martin
 * Engineering Leadership Development Program
 * Team 7
 * 15 April 2017
 * Jarrett Mead
 * ---------------------------------------------------------------------------------
 * Change Log
 * 	15 April 2017 - Jarrett Mead - Class Birthday
 * ---------------------------------------------------------------------------------
 */
package app.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import app.org.multiwii.msp.MSP;
import app.org.multiwii.swingui.gui.MwConfiguration;
import app.org.multiwii.swingui.gui.MwGuiFrame;
import app.org.multiwii.swingui.gui.chart.MwChartFactory;
import app.org.multiwii.swingui.gui.chart.MwChartPanel;
import app.org.multiwii.swingui.gui.comp.MwJPanel;
import app.org.multiwii.swingui.gui.instrument.MwHudPanel;
import app.org.multiwii.swingui.gui.instrument.MwInstrumentJPanel;
import app.org.multiwii.swingui.gui.instrument.MwRCDataPanel;
import app.org.multiwii.swingui.gui.instrument.MwUAVPanel;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MainDisplayController {

	private static Logger logger			= Logger.getLogger(MainDisplayController.class.getName());

	@FXML
	private HBox lowerHalf;
	@FXML
	private ImageView video;
	@FXML
	private ImageView snapshot_display;
	@FXML
	private VBox receiversBox;
	@FXML
	private VBox horizonTempBox;

	public void setup() {
		logger.fine("Initializing MainDisplayController.");
		
		SwingNode chartNode = new SwingNode();
		SwingNode rcDataNode = new SwingNode();
		SwingNode uavNode = new SwingNode();
		SwingNode horizonNode = new SwingNode();
		SwingNode tempGaugeNode = null;

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				logger.finest("Invoking swing thread.");
				final int sizeX = 700;
				final int sizeY = 400;

				MwConfiguration.setLookAndFeel();
				MwConfiguration conf = new MwConfiguration();
				
				//MwGuiFrame frame = new MwGuiFrame(conf);
				
				MwChartPanel realTimeChart = MwChartFactory.createChart(conf, MSP.getRealTimeData().getDataSet());
				MSP.getRealTimeData().addListener(realTimeChart);
				realTimeChart.setPreferredSize(
						new java.awt.Dimension(sizeX, sizeY));

				chartNode.setContent(realTimeChart);

				MwInstrumentJPanel rcDataPanel = new MwRCDataPanel(conf);
				MSP.getRealTimeData().addListener(rcDataPanel);
				//pane.setMinimumSize(new Dimension(770, 200));
				//pane.setMaximumSize(new Dimension(770, 200));
				rcDataNode.setContent(rcDataPanel);
				//centerRcBoxNodes();

				MwUAVPanel uavPanel = new MwUAVPanel(conf);
				MSP.getRealTimeData().addListener(uavPanel);
				uavNode.setContent(uavPanel);

				MwInstrumentJPanel hudPanel = new MwHudPanel(conf);
				MSP.getRealTimeData().addListener(hudPanel);
				horizonNode.setContent(hudPanel);
				
				//frame.setVisible(true);
				//frame.repaint();
			}

		});
		logger.finer("Invoked swing thread later.");

		/*
		AnchorPane.setTopAnchor(chartPane, 100.0);
		AnchorPane.setLeftAnchor(chartPane, 0.0);
		AnchorPane.setRightAnchor(chartPane, 0.0);
		AnchorPane.setBottomAnchor(chartPane, 0.0);
		rootLayout.getChildren().add(chartPane);
		 */
		lowerHalf.getChildren().add(0, chartNode);
		receiversBox.getChildren().add(rcDataNode);
		receiversBox.getChildren().add(uavNode);
		horizonTempBox.getChildren().add(horizonNode);
		video.setImage(new Image(new File("/home/jarrett/Downloads/pics/fire800_400.jpg").toURI().toString()));
		snapshot_display.setImage(new Image(new File("/home/jarrett/Downloads/pics/fire3.jpg").toURI().toString()));
		centerImage();
		//horizonTempBox.getChildren().add(tempGaugeNode);
	}

	/*
	public void printHorizonWidths() {
		System.out.println("Horizon VBox width: " + horizonTempBox.getWidth());
		System.out.println("Horizon JPanel width: " + ((SwingNode)horizonTempBox.getChildren().get(1)).getContent().getWidth());
		System.out.println("Horizon JPanel Dimension width: " + ((SwingNode)horizonTempBox.getChildren().get(1)).getContent().getSize().getWidth());
	}
	public void updateDatum(double d, MsgType type) {
		String newVal = doubleDatumToLabelString(d);

		switch(type) {
		case TEMP:
			airTempLabel.setText(newVal);
			break;
		case ALTITUDE:
			altitudeLabel.setText(newVal);
			break;
		default:
			break;
		}
	}

	public void updateVectorDatum(double[] datum, MsgType type) {
		String x = "";
		String y = "";
		String z = "";

		switch(type) {
		case ACCEL:
			xAccelLabel.setText(x);
			yAccelLabel.setText(y);
			zAccelLabel.setText(z);
			break;
		case GYRO:
			rollLabel.setText(x);
			pitchLabel.setText(y);
			yawLabel.setText(z);
			break;
		default:
			break;
		}
	}
	*/

	private String doubleDatumToLabelString(double d) {
		String s = "";
		if(d != Double.MIN_VALUE) {
			s = Double.toString(d);
		}
		return s;
	}

	private void centerRcBoxNodes() {
		SwingNode node1 = (SwingNode)receiversBox.getChildren().get(1);
		SwingNode node2 = (SwingNode)receiversBox.getChildren().get(2);
		JComponent comp1 = node1.getContent();
		JComponent comp2 = node2.getContent();
		if(comp1 != null && comp2 != null) {
			double w = receiversBox.getWidth();
			double xpos = receiversBox.getLayoutX();
			double center = w/2 + xpos;

			node1.setLayoutX(center);
			node2.setLayoutX(center);
		}
	}

	private void centerImage() {
		Image img = snapshot_display.getImage();
		if(img != null) {
			double w = 0;
			double h = 0;

			double ratioX = snapshot_display.getFitWidth() / img.getWidth();
			double ratioY = snapshot_display.getFitHeight() / img.getHeight();

			double reducCoeff = 0;
			if(ratioX >= ratioY) {
				reducCoeff = ratioY;
			} else {
				reducCoeff = ratioX;
			}

			w = img.getWidth() * reducCoeff;
			h = img.getHeight() * reducCoeff;

			snapshot_display.setX((snapshot_display.getFitWidth() - w) / 2);
			//imageDisplay.setY((imageDisplay.getFitHeight() - h) / 2 + 1);
		}
	}
}

