/**
 * Copyright (C) 2012 @author treym (Trey Marc)
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package app.org.multiwii.swingui.ds;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

/**
 * A pojo implementation of a datasource for holding severals dataset of sensors
 *
 * @author treym
 */
public class MwDataSourceImpl implements MwDataSource {
	//private static final Logger LOGGER = Logger
			//.getLogger(MwDataSourceImpl.class);

	// TODO impl factory
	// private MwDataSourceImpl(){}

	//private Map<Class<? extends MwSensorClass>, TimeSeriesCollection> dataset = new Hashtable<Class<? extends MwSensorClass>, TimeSeriesCollection>();
	private TimeSeriesCollection tsc = new TimeSeriesCollection();

	//private final Map<Class<? extends MwSensorClass>, List<MwDataSourceListener>> listeners = new Hashtable<Class<? extends MwSensorClass>, List<MwDataSourceListener>>();
	private List<MwDataSourceListener> listeners = new ArrayList<MwDataSourceListener>();
	private long maxItemAge = 2000;

	//private final Map<Class<? extends MwSensorClass>, Map<String, TimeSeries>> sensors = new Hashtable<Class<? extends MwSensorClass>, Map<String, TimeSeries>>();
	//private final Map<String, TimeSeries> sensors = new HashMap<String, TimeSeries>();

	// public int getMaxItemCount() {
	// return maxItemCount;
	// }

	// TODO set max ages counts for each dataset not all

	// public void setMaxItemCount(final int maxItemCount1) {
	// if (maxItemCount1 > 0) {
	// // this.maxItemCount = maxItemCount1;
	// for (Class<? extends MwSensorClass> sclass : sensors.keySet()) {
	// Hashtable<String, TimeSeries> series = sensors.get(sclass);
	// for (String sensorName : series.keySet()) {
	// series.get(sensorName).setMaximumItemCount(maxItemCount);
	// }
	// }
	// }
	// }

	@Override
	public void addListener(final MwDataSourceListener newListener) {
		if (newListener != null) {
			listeners.add(newListener);
		}
	}

	/**
	 * Creates a dataset.
	 *
	 * @return the dataset.
	 */

	@Override
	public final XYDataset getDataSet() {

		return tsc;

	}

	public final long getMaxItemAge() {
		return maxItemAge;
	}

	// public final XYDataset getDataset() {
	// if (dataset == null) {
	// return getLatestDataset();
	// }
	// return dataset;
	// }

	@Override
	public final void notifyListener(final String name, final Double value) {
			for (final MwDataSourceListener mwDataSourceListener : listeners) {
				mwDataSourceListener.readNewValue(name, value);

			}
	}

	@Override
	public final boolean put(final Date date, final String sensorName,
			final Double value) {

		/*
		if (sensorName == null || sensorName.length() == 0) {
			return false;
		}

		notifyListener(sensorName, value);

		TimeSeriesCollection ts = dataset.get(sensorClass);
		if (ts == null) {
			ts = new TimeSeriesCollection();
			dataset.put(sensorClass, ts);

		}

		TimeSeries timeserie = s.get(sensorName);

		if (timeserie == null) {
			timeserie = new TimeSeries(sensorName);
			// timeserie.setMaximumItemCount(maxItemCount);
			timeserie.setMaximumItemAge(maxItemAge);

			s.put(sensorName, timeserie);
			ts.addSeries(s.get(sensorName));

		}

		try {
			// if the refresh rate is high , we may have multiple answer within
			// the same millis
			timeserie.addOrUpdate(new Millisecond(date), value);
		} catch (final Exception e) {
			LOGGER.error(e.getMessage());
		}
		*/
		return true;

	}

	@Override
	public final boolean removeListener(final MwDataSourceListener deadListener) {
		if (deadListener != null) {

			return listeners.remove(deadListener);

		}
		return false;
	}

	/*
	public final void setMaxItemAge(final int maxItemAge1) {
		if (maxItemAge1 > 0) {
			this.maxItemAge = maxItemAge1;
			for (final Class<? extends MwSensorClass> sclass : sensors.keySet()) {
				final Map<String, TimeSeries> series = sensors.get(sclass);
				for (final String sensorName : series.keySet()) {
					series.get(sensorName).setMaximumItemAge(maxItemAge);
				}
			}
		}

	}
	*/

}
