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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickMarkPosition;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.data.Range;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYSeries;

import T7.T7Messages.GenericMessage.MsgType;
import app.KeySpinner;
import app.model.Snapshot;
import app.org.multiwii.msp.MSP;
import app.org.multiwii.swingui.gui.MwConfiguration;
import app.org.multiwii.swingui.gui.chart.MwChartFactory;
import app.org.multiwii.swingui.gui.chart.MwChartPanel;
import app.org.multiwii.swingui.gui.instrument.MwCompasPanel;
import app.org.multiwii.swingui.gui.instrument.MwHudPanel;
import app.org.multiwii.swingui.gui.instrument.MwRCDataPanel;
import app.org.multiwii.swingui.gui.instrument.MwUAVPanel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
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
	/*
	@FXML
	private SwingNode chartNode;
	*/
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
	@FXML
	private ImageView logo;
	@FXML
	private ImageView keyImage;
	
	private MwRCDataPanel rcDataPanel;
	private MwUAVPanel uavPanel;
	private MwHudPanel hudPanel;
	private MwCompasPanel compasPanel;
	private SimpleMetroArcGauge tempGauge;
	private Snapshot embedded_snap;
	private static KeySpinner keySpinner = null;
	
	private TimeSeriesCollection dataset = new TimeSeriesCollection();
	
	private TimeSeries accXSeries = new TimeSeries("");
	private TimeSeries accYSeries = new TimeSeries("");
	private TimeSeries accZSeries = new TimeSeries("");
	private TimeSeries gyroXSeries = new TimeSeries("");
	private TimeSeries gyroYSeries = new TimeSeries("");
	private TimeSeries gyroZSeries = new TimeSeries("");
	private TimeSeries attXSeries = new TimeSeries("");
	private TimeSeries attYSeries = new TimeSeries("");
	private TimeSeries attZSeries = new TimeSeries("");
	private TimeSeries altSeries = new TimeSeries("");
	private TimeSeries rangeSeries = new TimeSeries("");
	private TimeSeries headSeries = new TimeSeries("");
	private TimeSeries batSeries = new TimeSeries("");
	private TimeSeries tempSeries = new TimeSeries("");
	
	private DateAxis dateAxis;
	private ValueAxis valueAxis;
	
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	public void setup() {
		logger.fine("Initializing MainDisplayController.");
		
		//SwingNode tempGaugeNode = null;

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				logger.finest("Invoking swing thread.");
				/*
				final int sizeX = 700;
				final int sizeY = 400;
				*/

				MwConfiguration.setLookAndFeel();
				MwConfiguration conf = new MwConfiguration();
				
				//MwGuiFrame frame = new MwGuiFrame(conf);
				/*
				MwChartPanel realTimeChart = MwChartFactory.createChart(conf, MSP.getRealTimeData().getDataSet());
				MSP.getRealTimeData().addListener(realTimeChart);
				realTimeChart.setPreferredSize(
						new java.awt.Dimension(sizeX, sizeY));

				chartNode.setContent(realTimeChart);
				 */

				rcDataPanel = new MwRCDataPanel(conf);
				MSP.getRealTimeData().addListener(rcDataPanel);
				//pane.setMinimumSize(new Dimension(770, 200));
				//pane.setMaximumSize(new Dimension(770, 200));
				rcDataNode.setContent(rcDataPanel);
				//centerRcBoxNodes();

				uavPanel = new MwUAVPanel(conf);
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
				//realTimeChart.repaint();
				rcDataPanel.repaint();
				uavPanel.repaint();
				hudPanel.repaint();
				compasPanel.repaint();
			}

		});
		logger.finer("Invoked swing thread later.");

		KeyCode[] key_arr = {KeyCode.S, KeyCode.E, KeyCode.C, KeyCode.T};
		Image[] image_arr = {new Image((new File("src/main/resources/images/default/camera.png")).toURI().toString()),
				new Image((new File("src/main/resources/images/default/polaroid.jpg")).toURI().toString()),
				new Image((new File("src/main/resources/images/default/configuration.png")).toURI().toString()),
				new Image((new File("src/main/resources/images/default/power.png")).toURI().toString())};
		keySpinner = new KeySpinner(new ArrayList<KeyCode>(Arrays.asList(key_arr)),
				new ArrayList<Image>(Arrays.asList(image_arr)));
		//refreshKeyLabel();
		keyImage.setImage(keySpinner.getIcon());

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
		
		logo.setImage(new Image((new File("src/main/resources/images/default/svn_logo_medium.png")).toURI().toString()));
		logo.setFitWidth(logo.getFitWidth()*2);
		logo.setFitHeight(logo.getFitHeight()*2);
		//horizonTempBox.getChildren().add(tempGaugeNode);
		tempGauge = new SimpleMetroArcGauge();
		tempGauge.setPrefSize(175.0, 175.0);
		tempGauge.setMinSize(175.0, 175.0);
		tempGauge.setMaxValue(350.0);
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
		Segment gSeg = new PercentSegment(tempGauge, 0.0, 68.0);
		Segment ySeg = new PercentSegment(tempGauge, 68.0, 88.0);
		Segment rSeg = new PercentSegment(tempGauge, 88.0, 100.0);
		tempGauge.segments().add(gSeg);
		tempGauge.segments().add(ySeg);
		tempGauge.segments().add(rSeg);
		tempBox.getChildren().add(tempGauge);
		
		dataset.addSeries(accXSeries);
		dataset.addSeries(accYSeries);
		dataset.addSeries(accZSeries);
		dataset.addSeries(gyroXSeries);
		dataset.addSeries(gyroYSeries);
		dataset.addSeries(gyroZSeries);
		dataset.addSeries(attXSeries);
		dataset.addSeries(attYSeries);
		dataset.addSeries(attZSeries);
		dataset.addSeries(altSeries);
		dataset.addSeries(rangeSeries);
		dataset.addSeries(headSeries);
		dataset.addSeries(batSeries);
		dataset.addSeries(tempSeries);
		
		JFreeChart chart = ChartFactory.createTimeSeriesChart("", "", "", dataset, false, true, true);
		dateAxis = (DateAxis) chart.getXYPlot().getDomainAxis();
		dateAxis.setTickUnit(new DateTickUnit(DateTickUnitType.MINUTE, 1));
		dateAxis.setTickMarkPosition(DateTickMarkPosition.START);
		dateAxis.setDateFormatOverride(new SimpleDateFormat("HH:mm"));
		valueAxis = (ValueAxis) chart.getXYPlot().getRangeAxis();
		chart.setAntiAlias(true);
		chart.setTextAntiAlias(true);
		ChartPanel chartPanel = new ChartPanel(chart);
		
		chartNode.setContent(chartPanel);
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
		Calendar cal = Calendar.getInstance();
		Millisecond milli = new Millisecond(cal.getTime());
		TimeSeries datumSeries = null;

		switch(type) {
		case TEMP:
			tempGauge.setValue(d);
			temp_label.setText(doubleDatumToLabelString(d));
			datumSeries = tempSeries;
			break;
		case ALTITUDE:
			compasPanel.readNewValue(MSP.IDALT, d);
			alt_label.setText(doubleDatumToLabelString(d));
			rcDataPanel.readNewValue(MSP.IDRCALTITUDE, d);
			datumSeries = altSeries;
			break;
		case HEAD:
			compasPanel.readNewValue(MSP.IDHEAD, d);
			head_label.setText(doubleDatumToLabelString(d));
			datumSeries = headSeries;
			break;
		case HEARTBEAT:
			if(d == 1.0) {
				uavPanel.readNewValue("0", 2000.0);
				uavPanel.readNewValue("1", 2000.0);
				uavPanel.readNewValue("2", 2000.0);
				uavPanel.readNewValue("3", 2000.0);
			} else {
				uavPanel.readNewValue("0", 0.0);
				uavPanel.readNewValue("1", 0.0);
				uavPanel.readNewValue("2", 0.0);
				uavPanel.readNewValue("3", 0.0);
			}
		default:
			return;
		}
		
		datumSeries.add(milli, d);
		resizeChart();
	}

	public void updateVectorData(double datumX, double datumY, double datumZ, MsgType type) {
		Calendar cal = Calendar.getInstance();
		Millisecond milli = new Millisecond(cal.getTime());
		TimeSeries xSeries = null;
		TimeSeries ySeries = null;
		TimeSeries zSeries = null;

		switch(type) {
		case ACCEL:
			acc_x_label.setText(doubleDatumToLabelString(datumX));
			acc_y_label.setText(doubleDatumToLabelString(datumY));
			acc_z_label.setText(doubleDatumToLabelString(datumZ));
			rcDataPanel.readNewValue(MSP.IDRCACCX, datumX);
			rcDataPanel.readNewValue(MSP.IDRCACCY, datumY);
			rcDataPanel.readNewValue(MSP.IDRCACCZ, datumZ);
			/*
			uavPanel.readNewValue("0", 2000.0);
			uavPanel.readNewValue("1", 2000.0);
			uavPanel.readNewValue("2", 2000.0);
			uavPanel.readNewValue("3", 2000.0);
			*/
			xSeries = accXSeries;
			ySeries = accYSeries;
			zSeries = accZSeries;
			break;
		case GYRO:
			gyro_roll_label.setText(doubleDatumToLabelString(datumX));
			gyro_pitch_label.setText(doubleDatumToLabelString(datumY));
			gyro_yaw_label.setText(doubleDatumToLabelString(datumZ));
			xSeries = gyroXSeries;
			ySeries = gyroYSeries;
			zSeries = gyroZSeries;
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
			xSeries = attXSeries;
			ySeries = attYSeries;
			zSeries = attZSeries;
			break;
		default:
			return;
		}
		
		xSeries.add(milli, datumX);
		ySeries.add(milli, datumY);
		zSeries.add(milli, datumZ);
		resizeChart();
	}
	
	/*
	private void resizeChart() {
		Range xRange = dataset.getDomainBounds(false);
		Range yRange = dataset.getRangeBounds(false);
		
		double xUp = xRange.getUpperBound();
		double xLow = xRange.getLowerBound();
		double yUp = yRange.getUpperBound();
		double yLow = yRange.getLowerBound();
		
		dateAxis.setUpperBound(xUp);
		if(xUp - xLow > 10) {
			dateAxis.setLowerBound(xUp - 10);
		}
		
		valueAxis.setUpperBound(yUp);
		if(yUp - yLow > 50) {
			valueAxis.setLowerBound(yUp - 50);
		}
	}
	*/
	
	private void resizeChart() {
		Range xRange = dataset.getDomainBounds(false);
		System.out.println("xRange = " + xRange.toString());
		System.out.println("X upper bound = " + xRange.getUpperBound());
		dateAxis.setRangeAboutValue(xRange.getUpperBound(), 10);
		
		Range yRange = dataset.getRangeBounds(false);
		System.out.println("yRange = " + yRange.toString());
		System.out.println("Y upper bound = " + yRange.getUpperBound());
		System.out.println("Y lower bound = " + yRange.getLowerBound());
		valueAxis.setRange(yRange.getLowerBound(), yRange.getUpperBound());
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
	
	public void spinKey() {
		keySpinner.spin();
		//refreshKeyLabel();
		keyImage.setImage(keySpinner.getIcon());
	}
	
	/*
	public void refreshKeyLabel() {
		String actionStr = "";
		switch(keySpinner.getKey()) {
		case S:
			actionStr = "Take Snapshot";
			break;
		case E:
			actionStr = "Snapshot Explorer";
			break;
		case C:
			actionStr = "Data Configuration Manager";
			break;
		case T:
			actionStr = "UAV Termination Menu";
			break;
		default:
			break;
		}
		keyLabel.setText(actionStr);
	}
	*/

	public void updateEmbeddedSnap() {
		if(embedded_snap != null) {
			if(embedded_snap.isTarget()) {
				priLabel.setText(Integer.toString(embedded_snap.getRelativePriority()));
			} else {
				priLabel.setText("");
			}
		}
	}
	
	public KeySpinner getKeySpinner() {
		return keySpinner;
	}

	/*
	public void streamVideo() {
		MediaPlayer player = new MediaPlayer(new Media("http://t7rpi.asuscomm.com/html"));
		video.setMediaPlayer(player);
		player.play();
	}
	*/
}

