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

import java.util.logging.Logger;

import javax.swing.SwingUtilities;
import T7.T7Messages.GenericMessage.MsgType;
import app.model.Snapshot;
import app.org.multiwii.msp.MSP;
import app.org.multiwii.swingui.gui.MwConfiguration;
import app.org.multiwii.swingui.gui.chart.MwChartFactory;
import app.org.multiwii.swingui.gui.chart.MwChartPanel;
import app.org.multiwii.swingui.gui.instrument.MwCompasPanel;
import app.org.multiwii.swingui.gui.instrument.MwHudPanel;
import app.org.multiwii.swingui.gui.instrument.MwRCDataPanel;
import app.org.multiwii.swingui.gui.instrument.MwUAVPanel;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import jfxtras.scene.control.gauge.linear.SimpleMetroArcGauge;
import jfxtras.scene.control.gauge.linear.elements.PercentSegment;
import jfxtras.scene.control.gauge.linear.elements.Segment;

public class MainDisplayController {

	private static Logger logger			= Logger.getLogger(MainDisplayController.class.getName());

	@FXML
	private WebView video;
	@FXML
	private ImageView snapshot_display;
	@FXML
	private ImageView pin_display;
	@FXML
	private Label priLabel;
	@FXML
	private SwingNode chartNode;
	@FXML
	private SwingNode rcDataNode;
	@FXML
	private SwingNode uavNode;
	@FXML
	private SwingNode horizonNode;
	@FXML
	private SwingNode compassNode;
	@FXML
	private VBox tempBox;
	@FXML
	private Label acc_x_label;
	@FXML
	private Label acc_y_label;
	@FXML
	private Label acc_z_label;
	@FXML
	private Label gyro_roll_label;
	@FXML
	private Label gyro_pitch_label;
	@FXML
	private Label gyro_yaw_label;
	@FXML
	private Label att_x_label;
	@FXML
	private Label att_y_label;
	@FXML
	private Label att_z_label;
	@FXML
	private Label alt_label;
	@FXML
	private Label head_label;
	@FXML
	private Label batt_label;
	@FXML
	private Label temp_label;
	
	private MwRCDataPanel rcDataPanel;
	private MwHudPanel hudPanel;
	private MwCompasPanel compasPanel;
	private SimpleMetroArcGauge tempGauge;
	private Snapshot embedded_snap;

	public void setup() {
		logger.fine("Initializing MainDisplayController.");
		
		//SwingNode tempGaugeNode = null;

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

				rcDataPanel = new MwRCDataPanel(conf);
				MSP.getRealTimeData().addListener(rcDataPanel);
				//pane.setMinimumSize(new Dimension(770, 200));
				//pane.setMaximumSize(new Dimension(770, 200));
				rcDataNode.setContent(rcDataPanel);
				//centerRcBoxNodes();

				MwUAVPanel uavPanel = new MwUAVPanel(conf);
				MSP.getRealTimeData().addListener(uavPanel);
				uavNode.setContent(uavPanel);

				hudPanel = new MwHudPanel(conf);
				MSP.getRealTimeData().addListener(hudPanel);
				horizonNode.setContent(hudPanel);
				
				compasPanel = new MwCompasPanel(conf);
				MSP.getRealTimeData().addListener(compasPanel);
				compassNode.setContent(compasPanel);
				
				//frame.setVisible(true);
				//frame.repaint();
				realTimeChart.repaint();
				rcDataPanel.repaint();
				uavPanel.repaint();
				hudPanel.repaint();
				compasPanel.repaint();
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
		/*
		lowerHalf.getChildren().add(0, chartNode);
		receiversBox.getChildren().add(rcDataNode);
		receiversBox.getChildren().add(uavNode);
		horizonTempBox.getChildren().add(horizonNode);
		*/
		WebEngine engine = video.getEngine();
		engine.load("http://t7rpi.asuscomm.com/html/");
		video.setZoom(1.5);
		//snapshot_display.setImage(new Image(new File("/home/jarrett/T7Software/hss/src/main/resources/images/fire3.jpg").toURI().toString()));
		centerImage();
		//horizonTempBox.getChildren().add(tempGaugeNode);
		tempGauge = new SimpleMetroArcGauge();
		tempGauge.setPrefSize(175.0, 175.0);
		tempGauge.setMinSize(175.0, 175.0);
		/*
		tempGauge.getStyleClass().add("colorscheme-green-to-red-6");
		for(int i = 0; i < 10; i++) {
			Segment lSegment = new PercentSegment(tempGauge, i*(100.0/6), (i+1)*100.0/6);
			tempGauge.segments().add(lSegment);
		}
		*/
		System.out.println("StyleClass: " + tempGauge.getStyleClass());
		/*
		tempGauge.setStyle("-fxx-segment0-color: #11632f");
		tempGauge.setStyle("-fxx-segment1-color: #ffdb28");
		tempGauge.setStyle("-fxx-segment2-color: #ff312a");
		 */
		tempGauge.getStylesheets().add("file:src/app/view/temperature_segment.css");
		tempGauge.getStyleClass().add("colorscheme-green-to-red-3");
		System.out.println("tempGauge StyleClasses: " + tempGauge.getStyleClass());
		Segment gSeg = new PercentSegment(tempGauge, 0.0, 70.0);
		Segment ySeg = new PercentSegment(tempGauge, 70.0, 90.0);
		Segment rSeg = new PercentSegment(tempGauge, 90.0, 100.0);
		tempGauge.segments().add(gSeg);
		tempGauge.segments().add(ySeg);
		tempGauge.segments().add(rSeg);
		tempBox.getChildren().add(tempGauge);
	}

	/*
	public void printHorizonWidths() {
		System.out.println("Horizon VBox width: " + horizonTempBox.getWidth());
		System.out.println("Horizon JPanel width: " + ((SwingNode)horizonTempBox.getChildren().get(1)).getContent().getWidth());
		System.out.println("Horizon JPanel Dimension width: " + ((SwingNode)horizonTempBox.getChildren().get(1)).getContent().getSize().getWidth());
	}
	*/
	
	public void updateDatum(double d, MsgType type) {
		//String newVal = doubleDatumToLabelString(d);

		switch(type) {
		case TEMP:
			tempGauge.setValue(d);
			temp_label.setText(doubleDatumToLabelString(d));
			break;
		case ALTITUDE:
			compasPanel.readNewValue("alt", d);
			alt_label.setText(doubleDatumToLabelString(d));
			break;
		default:
			break;
		}
	}

	public void updateVectorData(double datumX, double datumY, double datumZ, MsgType type) {

		switch(type) {
		case ACCEL:
			acc_x_label.setText(doubleDatumToLabelString(datumX));
			acc_y_label.setText(doubleDatumToLabelString(datumY));
			acc_z_label.setText(doubleDatumToLabelString(datumZ));
			break;
		case GYRO:
			gyro_roll_label.setText(doubleDatumToLabelString(datumX));
			gyro_pitch_label.setText(doubleDatumToLabelString(datumY));
			gyro_yaw_label.setText(doubleDatumToLabelString(datumZ));
			break;
		case ATTITUDE:
			hudPanel.readNewValue("angx", datumX);
			hudPanel.readNewValue("angy", datumY);
			rcDataPanel.readNewValue("roll", datumX);
			rcDataPanel.readNewValue("pitch", datumY);
			rcDataPanel.readNewValue("yaw", datumZ);
			att_x_label.setText(doubleDatumToLabelString(datumX));
			att_y_label.setText(doubleDatumToLabelString(datumY));
			att_z_label.setText(doubleDatumToLabelString(datumZ));
			break;
		default:
			break;
		}
	}

	private String doubleDatumToLabelString(double d) {
		String s = "";
		if(d != Double.MIN_VALUE) {
			s = Double.toString(d);
		}
		return s;
	}
	/*
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
	*/

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

			snapshot_display.setX((snapshot_display.getFitWidth() - w) / 2 + 4);
			snapshot_display.setY((snapshot_display.getFitHeight() - h) / 2 + 4);
		}
	}

	public Image takeSnapshot() {
		return video.snapshot(null, null);
	}
	
	public void displaySnapshot(Snapshot snap) {
		setEmbeddedSnap(snap);
		snapshot_display.setImage(snap.getImage());
		pin_display.setImage(Snapshot.getPin());
		//priLabel.setText(Integer.toString(snap.getPriority()));
		priLabel.setText(Integer.toString(snap.getRelativePriority()));
		centerImage();
		/*
		snapshot_anchor.setPrefWidth(snapshot_display.getFitWidth());
		snapshot_anchor.setPrefHeight(snapshot_display.getFitHeight());
		*/
	}

	public void checkDeleteDisplayedSnapshot(Snapshot snapshot) {
		if(snapshot_display.getImage() != null) {
			if(snapshot_display.getImage().equals(snapshot.getImage())) {
				snapshot_display.setImage(null);
				pin_display.setImage(null);
				priLabel.setText("");
				embedded_snap = null;

				System.out.println("w: " + snapshot_display.getFitWidth() + ", h: " + snapshot_display.getFitHeight());
			}
		}
	}

	public Snapshot getEmbeddedSnap() {
		return embedded_snap;
	}

	public void setEmbeddedSnap(Snapshot embedded_snap) {
		this.embedded_snap = embedded_snap;
	}

	public void updateEmbeddedSnap() {
		if(embedded_snap != null) {
			if(embedded_snap.isTarget()) {
				priLabel.setText(Integer.toString(embedded_snap.getRelativePriority()));
			} else {
				priLabel.setText("");
			}
		}
	}

	/*
	public void streamVideo() {
		MediaPlayer player = new MediaPlayer(new Media("http://t7rpi.asuscomm.com/html"));
		video.setMediaPlayer(player);
		player.play();
	}
	*/
}

