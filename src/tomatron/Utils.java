package tomatron;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.SystemTray;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Utils {
	/**
	 * Create a tray icon image.
	 * 
	 * @param textHeightScale
	 *            Let text occupy this percentage of the available height in the
	 *            tray icon. Range (0, 1]
	 * @param renderedString
	 *            The string to be rendered. Note that only the text height is
	 *            fixed, long strings will not be properly rendered.
	 * @param backgroundColor
	 *            The background color of the tray icon
	 */
	public static Image createTrayIconImage(String renderedString,
			double textHeightScale, Color backgroundColor) {
		if (textHeightScale <= 0 || textHeightScale > 1) {
			throw new IllegalArgumentException(
					"Text height percentage should be in (0, 1].");
		}

		Dimension trayIconSize = SystemTray.getSystemTray().getTrayIconSize();
		int w = trayIconSize.width;
		int h = trayIconSize.width;

		BufferedImage iconImage = new BufferedImage(w, h,
				BufferedImage.TYPE_INT_RGB);
		Graphics g = iconImage.getGraphics();
		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		int textHeight = (int) Math.floor((h - 2) * textHeightScale);
		Font font = new Font("Monospace", Font.PLAIN, textHeight);
		g.setFont(font);

		Rectangle2D textSize = font.getStringBounds(renderedString,
				g2.getFontRenderContext());
		int textX = (int) Math.floor(.5 * w - .5 * textSize.getWidth());
		int textY = (int) Math.floor(textSize.getHeight());

		g.setColor(backgroundColor);
		g.fillRect(0, 0, w, h);
		g.setColor(new Color(255, 255, 255));
		g.drawString(renderedString, textX, textY);
		g.setColor(new Color(0, 0, 0));
		g.drawRect(0, 0, w - 1, h - 1);

		return iconImage;
	}
}
