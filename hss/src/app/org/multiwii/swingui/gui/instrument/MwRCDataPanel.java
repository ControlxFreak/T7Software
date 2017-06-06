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

package app.org.multiwii.swingui.gui.instrument;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import app.org.multiwii.swingui.gui.MwConfiguration;
import app.org.multiwii.msp.MSP;

public class MwRCDataPanel extends MwInstrumentJPanel {

	private static final long serialVersionUID = 1L;

	private final Image background = super.getImage("rcdata.png");
	private final int[] startx = initializePositionX();
	private final int[] starty = initializePositionY();
	private final int[] gMax = initializeGreenMax();
	private final int[] yMax = initializeYellowMax();
	private final int[] valMax = initializeValMax();
	private final int rcDatabarWidth = 7;

	private int[] initializePositionY() {
		int[] m = new int[8];
		int starty = 16;
		for (int i = 0; i < m.length; i++) {
			m[i] = starty;
			starty += rcDatabarWidth + 8;
		}
		return m;
	}

	private int[] initializeGreenMax() {
		int[] m = new int[8];
		
		m[0] = 15;
		m[1] = 40;
		m[2] = 40;
		m[3] = 40;
		m[4] = 150;
		m[5] = 25;
		m[6] = 25;
		m[7] = 25;
		
		return m;
	}

	private int[] initializeYellowMax() {
		int[] m = new int[8];
		
		m[0] = 20;
		m[1] = 55;
		m[2] = 55;
		m[3] = 55;
		m[4] = 185;
		m[5] = 32;
		m[6] = 32;
		m[7] = 32;
		
		return m;
	}

	private int[] initializeValMax() {
		int[] m = new int[8];
		
		m[0] = 30;
		m[1] = 80;
		m[2] = 80;
		m[3] = 80;
		m[4] = 200;
		m[5] = 35;
		m[6] = 35;
		m[7] = 35;
		
		return m;
	}

	private int[] initializePositionX() {
		int[] m = new int[8];
		for (int i = 0; i < m.length; i++) {
			m[i] = 41;
		}
		return m;
	}

	private final double[] dataRC = new double[8];

	public MwRCDataPanel(MwConfiguration conf) {
		super(new Dimension(200, 150), conf);
		super.setBarMax(118);
		super.setBarWidth(rcDatabarWidth);

		for (int i = 0; i < dataRC.length; i++) {
			dataRC[i] = 0;
		}
	}

	private void drawBackground(Graphics2D g2d) {

		// int w = 200;

		BufferedImage bi = new BufferedImage(getMaxRadiusX(), getMaxRadiusY(),
				BufferedImage.TYPE_INT_ARGB);

		Graphics g = bi.getGraphics();
		g.drawImage(background, 0, 0, null);

		// float[] scales = { 1.0f ,1.0f,1.0f,0.8f};
		// float[] offsets = new float[4];
		// RescaleOp rop = new RescaleOp(scales, offsets, null);

		g2d.drawImage(bi, null, 0, 0);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		drawBackground(g2d);

		//drawBar(g2d, 2, dataRC, null, startx, starty, XAXIS);
		
		for(int i = 0; i < dataRC.length; i++) {
			drawRcBar(g2d, 2, dataRC[i], startx[i], starty[i], gMax[i], yMax[i], valMax[i]);
		}
	}

	@Override
	public void readNewValue(Integer string, int i) {
		// TODO Auto-generated method stub

	}

	@Override
	public void readNewValue(String name, Double value) {

		if (MSP.IDRCALTITUDE.equals(name)) {
			dataRC[0] = value;
		} else if (MSP.IDRCROLL.equals(name)) {
			dataRC[1] = value;
		} else if (MSP.IDRCPITCH.equals(name)) {
			dataRC[2] = value;
		} else if (MSP.IDRCYAW.equals(name)) {
			dataRC[3] = value;
		} else if (MSP.IDRCRANGE.equals(name)) {
			dataRC[4] = value;
		} else if (MSP.IDRCACCX.equals(name)) {
			dataRC[5] = value;
		} else if (MSP.IDRCACCY.equals(name)) {
			dataRC[6] = value;
		} else if (MSP.IDRCACCZ.equals(name)) {
			dataRC[7] = value;
		}
		repaint();
	}

	@Override
	void resetAllValuesImpl() {
	}
}
