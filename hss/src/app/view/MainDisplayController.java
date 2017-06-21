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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TimeZone;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickMarkPosition;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.Range;
import org.jfree.data.time.DynamicTimeSeriesCollection;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.Second;
import org.jfree.data.xy.XYDataset;
import T7.T7Messages.GenericMessage.MsgType;
import app.KeySpinner;
import app.model.Snapshot;
import app.org.multiwii.msp.MSP;
import app.org.multiwii.swingui.gui.MwConfiguration;
import app.org.multiwii.swingui.gui.instrument.MwCompasPanel;
import app.org.multiwii.swingui.gui.instrument.MwHudPanel;
import app.org.multiwii.swingui.gui.instrument.MwRCDataPanel;
import app.org.multiwii.swingui.gui.instrument.MwUAVPanel;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import jfxtras.scene.control.gauge.linear.SimpleMetroArcGauge;
import jfxtras.scene.control.gauge.linear.elements.PercentSegment;
import jfxtras.scene.control.gauge.linear.elements.Segment;

public class MainDisplayController {

	private static Logger logger			= Logger.getLogger(MainDisplayController.class.getName());
	private static final int COUNT = 1000; //2 * 60;
	private static final int SERIESNUM = 7;
	
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
	private Label range_label;
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
	@FXML
	private ImageView batteryImage;
	@FXML
	private Label batteryLabel;
	
	private MwRCDataPanel rcDataPanel;
	private MwUAVPanel uavPanel;
	private MwHudPanel hudPanel;
	private MwCompasPanel compasPanel;
	private SimpleMetroArcGauge tempGauge;
	private Snapshot embedded_snap;
	private static KeySpinner keySpinner = null;
	
	private DynamicTimeSeriesCollection dataset = new DynamicTimeSeriesCollection(SERIESNUM, COUNT, new Millisecond(), TimeZone.getDefault());

	private ArrayList<Double> attXList = new ArrayList<Double>();
	private ArrayList<Double> attYList = new ArrayList<Double>();
	private ArrayList<Double> attZList = new ArrayList<Double>();
	private ArrayList<Double> altList = new ArrayList<Double>();
	private ArrayList<Double> rangeList = new ArrayList<Double>();
	private ArrayList<Double> batList = new ArrayList<Double>();
	private ArrayList<Double> tempList = new ArrayList<Double>();
	
	private Timer timer;
	
	private DateAxis dateAxis;
	private ValueAxis valueAxis;
	
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	public void setup() {
		logger.fine("Initializing MainDisplayController.");

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				logger.finest("Invoking swing thread.");

				MwConfiguration.setLookAndFeel();
				MwConfiguration conf = new MwConfiguration();

				rcDataPanel = new MwRCDataPanel(conf);
				MSP.getRealTimeData().addListener(rcDataPanel);
				rcDataNode.setContent(rcDataPanel);

				uavPanel = new MwUAVPanel(conf);
				MSP.getRealTimeData().addListener(uavPanel);
				uavNode.setContent(uavPanel);

				hudPanel = new MwHudPanel(conf);
				MSP.getRealTimeData().addListener(hudPanel);
				horizonNode.setContent(hudPanel);
				
				compasPanel = new MwCompasPanel(conf);
				MSP.getRealTimeData().addListener(compasPanel);
				compassNode.setContent(compasPanel);
				
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
		keyImage.setImage(keySpinner.getIcon());
		
		WebEngine engine = video.getEngine();
		engine.load("http://t7rpi.asuscomm.com/html/");
		video.setZoom(1.5);
		centerImage();
		
		logo.setImage(new Image((new File("src/main/resources/images/default/svn_logo_medium.png")).toURI().toString()));
		logo.setFitWidth(logo.getFitWidth()*2);
		logo.setFitHeight(logo.getFitHeight()*2);
		tempGauge = new SimpleMetroArcGauge();
		tempGauge.setPrefSize(175.0, 175.0);
		tempGauge.setMinSize(175.0, 175.0);
		tempGauge.setMaxValue(350.0);
		System.out.println("StyleClass: " + tempGauge.getStyleClass());
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
		
		dataset.setTimeBase(new Millisecond());
		dataset.addSeries(new float[COUNT], 0, "Roll");
		dataset.addSeries(new float[COUNT], 1, "Pitch");
		dataset.addSeries(new float[COUNT], 2, "Yaw");
		dataset.addSeries(new float[COUNT], 3, "Altitude");
		dataset.addSeries(new float[COUNT], 4, "Range");
		dataset.addSeries(new float[COUNT], 5, "Battery");
		dataset.addSeries(new float[COUNT], 6, "Air Temperature");
		
		JFreeChart chart = createChart(dataset);
		dateAxis = (DateAxis) chart.getXYPlot().getDomainAxis();
		dateAxis.setTickUnit(new DateTickUnit(DateTickUnitType.MINUTE, 1));
		dateAxis.setTickMarkPosition(DateTickMarkPosition.START);
		dateAxis.setDateFormatOverride(new SimpleDateFormat("HH:mm"));
		valueAxis = chart.getXYPlot().getRangeAxis();
		chart.setAntiAlias(true);
		chart.setTextAntiAlias(true);
		ChartPanel chartPanel = new ChartPanel(chart);
		
		chartNode.setContent(chartPanel);
		
		timer = new Timer(1, new ActionListener() {
			
			
			
			@Override
			public void actionPerformed(ActionEvent e) {
				refreshChart();
			}
		});
		
		timer.start();
		
		batteryImage.setImage(new Image((new File("src/main/resources/images/default/battery-00-gray.png")).toURI().toString()));
	}
	
	private JFreeChart createChart(final XYDataset dataset) {
		final JFreeChart result = ChartFactory.createTimeSeriesChart(
				"", "", "", dataset, true, true, false);
		final XYPlot plot = result.getXYPlot();
		ValueAxis domain = plot.getDomainAxis();
		domain.setAutoRange(true);
		ValueAxis range = plot.getRangeAxis();
		range.setAutoRange(true);
		return result;
	}

	public void updateDatum(double d, MsgType type) {
		ArrayList<Double> datumList = null;

		switch(type) {
		case TEMP:
			tempGauge.setValue(d);
			temp_label.setText(doubleDatumToLabelString(d));
			datumList = tempList;
			break;
		case ALTITUDE:
			compasPanel.readNewValue(MSP.IDALT, d);
			alt_label.setText(doubleDatumToLabelString(d));
			rcDataPanel.readNewValue(MSP.IDRCALTITUDE, d);
			datumList = altList;
			break;
		case HEAD:
			compasPanel.readNewValue(MSP.IDHEAD, d);
			head_label.setText(doubleDatumToLabelString(d));
			return;
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
			return;
		case BAT:
			if(d > 80.0) {
				batteryImage.setImage(new Image((new File("src/main/resources/images/default/battery-100-gray.png")).toURI().toString()));
			} else if (d > 60.0) {
				batteryImage.setImage(new Image((new File("src/main/resources/images/default/battery-80-gray.png")).toURI().toString()));
			} else if (d > 40.0) {
				batteryImage.setImage(new Image((new File("src/main/resources/images/default/battery-60-gray.png")).toURI().toString()));
			} else if (d > 20.0) {
				batteryImage.setImage(new Image((new File("src/main/resources/images/default/battery-40-gray.png")).toURI().toString()));
			} else if (d > 5.0) {
				batteryImage.setImage(new Image((new File("src/main/resources/images/default/battery-20-gray.png")).toURI().toString()));
			} else {
				batteryImage.setImage(new Image((new File("src/main/resources/images/default/battery-00-gray.png")).toURI().toString()));
			}
			
			if(d == Double.MIN_VALUE) {
				batteryLabel.setText(null);
				d = 0;
			} else {
				batteryLabel.setText(doubleDatumToLabelString(d) + "%");
			}
			datumList = batList;
			break;
		case WIFI:
			rcDataPanel.readNewValue(MSP.IDRCRANGE, d);
			range_label.setText(doubleDatumToLabelString(d));
			datumList = rangeList;
			break;
		default:
			return;
		}
		
		datumList.add(d);
	}

	public void updateVectorData(double datumX, double datumY, double datumZ, MsgType type) {
		ArrayList<Double> xList = null;
		ArrayList<Double> yList = null;
		ArrayList<Double> zList = null;

		switch(type) {
		case ACCEL:
			acc_x_label.setText(doubleDatumToLabelString(datumX));
			acc_y_label.setText(doubleDatumToLabelString(datumY));
			acc_z_label.setText(doubleDatumToLabelString(datumZ));
			rcDataPanel.readNewValue(MSP.IDRCACCX, datumX);
			rcDataPanel.readNewValue(MSP.IDRCACCY, datumY);
			rcDataPanel.readNewValue(MSP.IDRCACCZ, datumZ);
			return;
		case GYRO:
			gyro_roll_label.setText(doubleDatumToLabelString(datumX));
			gyro_pitch_label.setText(doubleDatumToLabelString(datumY));
			gyro_yaw_label.setText(doubleDatumToLabelString(datumZ));
			return;
		case ATTITUDE:
			hudPanel.readNewValue("angx", datumX);
			hudPanel.readNewValue("angy", datumY);
			rcDataPanel.readNewValue("roll", datumX);
			rcDataPanel.readNewValue("pitch", datumY);
			rcDataPanel.readNewValue("yaw", datumZ);
			att_x_label.setText(doubleDatumToLabelString(datumX));
			att_y_label.setText(doubleDatumToLabelString(datumY));
			att_z_label.setText(doubleDatumToLabelString(datumZ));
			xList = attXList;
			yList = attYList;
			zList = attZList;
			break;
		default:
			return;
		}
		
		xList.add(datumX);
		yList.add(datumY);
		zList.add(datumZ);
	}
	
	private void resizeChart() {
		
		Range yRange = dataset.getRangeBounds(false);
		System.out.println("yRange = " + yRange.toString());
		System.out.println("Y upper bound = " + yRange.getUpperBound());
		System.out.println("Y lower bound = " + yRange.getLowerBound());
		if(yRange.getLength() > 0) {
			valueAxis.setRange(yRange.getLowerBound(), yRange.getUpperBound());
		}
	}

	private String doubleDatumToLabelString(double d) {
		String s = "";
		if(d != Double.MIN_VALUE) {
			s = Double.toString(d);
		}
		return s;
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
		if(snap.isTarget()) {
			priLabel.setText(Integer.toString(snap.getRelativePriority()));
		} else {
			priLabel.setText("");
		}
		centerImage();
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
	
	private void refreshChart() {
		float[] newData = new float[SERIESNUM];

		if(attXList.size() == 0) {
			newData[0] = 0;
		} else {
			newData[0] = attXList.get(attXList.size()-1).floatValue();
		}

		if(attYList.size() == 0) {
			newData[1] = 0;
		} else {
			newData[1] = attYList.get(attYList.size()-1).floatValue();
		}

		if(attZList.size() == 0) {
			newData[2] = 0;
		} else {
			newData[2] = attZList.get(attZList.size()-1).floatValue();
		}

		if(altList.size() == 0) {
			newData[3] = 0;
		} else {
			newData[3] = altList.get(altList.size()-1).floatValue();
		}

		if(rangeList.size() == 0) {
			newData[4] = 0;
		} else {
			newData[4] = rangeList.get(rangeList.size()-1).floatValue();
		}

		if(batList.size() == 0) {
			newData[5] = 0;
		} else {
			newData[5] = batList.get(batList.size()-1).floatValue();
		}

		if(tempList.size() == 0) {
			newData[6] = 0;
		} else {
			newData[6] = tempList.get(tempList.size()-1).floatValue();
		}
		
		dataset.advanceTime();
		dataset.appendData(newData);
	}

	public void stopTimer() {
		timer.stop();
	}
}

