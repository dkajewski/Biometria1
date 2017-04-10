package biometria;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class HistogramPanel extends JPanel {

	int[] r;
	int[] g;
	int[] b;
	int max = 0;
	int height = 140;

	public HistogramPanel(int[] r, int[] g, int[] b) {
		this.r = r;
		this.g = g;
		this.b = b;

		findMax(r, g, b);
	}

	private void findMax(int[] r, int[] g, int[] b) {

		for (int i = 0; i < r.length; i++) {
			if (max < r[i]) {
				max = r[i];
			}
		}

		for (int i = 0; i < g.length; i++) {
			if (max < g[i]) {
				max = g[i];
			}
		}

		for (int i = 0; i < b.length; i++) {
			if (max < b[i]) {
				max = b[i];
			}
		}

		// System.out.println(max);
	}

	private void Draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setColor(Color.WHITE);
		g2d.fillRect(10, 10, 513, 150);

		g2d.setColor(Color.BLACK);
		g2d.drawRect(10, 10, 513, 150);

		// g2d.setColor(Color.BLACK);
		// g2d.drawLine(20, 40, 250, 40);

		g2d.setColor(Color.WHITE);
		g2d.fillRect(10, 180, 513, 150);

		g2d.setColor(Color.BLACK);
		g2d.drawRect(10, 180, 513, 150);

		g2d.setColor(Color.WHITE);
		g2d.fillRect(530, 10, 513, 150);

		g2d.setColor(Color.BLACK);
		g2d.drawRect(530, 10, 513, 150);
		
		g2d.setColor(Color.WHITE);
		g2d.fillRect(530, 180, 513, 150);

		g2d.setColor(Color.BLACK);
		g2d.drawRect(530, 180, 513, 150);

		// czerwony
		int x = 11;
		g2d.setColor(Color.RED);
		for (int i = 0; i < r.length; i++) {
			double percent = 1.0000 * r[i] / max;
			double l = 1.0000 * height * percent;
			// System.out.println(r[i]);
			int length = (int) l;
			if (length > 0) {
				g2d.drawLine(x, 159, x, 159 - (length));
				g2d.drawLine(++x, 159, x, 159 - (length));
				x++;
			} else {
				x += 2;
			}
		}

		// zielony
		x = 11;
		g2d.setColor(Color.GREEN);
		for (int i = 0; i < this.g.length; i++) {
			double percent = 1.0000 * this.g[i] / max;
			double l = 1.0000 * height * percent;
			// System.out.println(this.g[i]);
			int length = (int) l;
			if (length > 0) {
				g2d.drawLine(x, 329, x, 329 - (length));
				g2d.drawLine(++x, 329, x, 329 - (length));
				x++;
			} else {
				x += 2;
			}
		}

		// niebieski
		x = 531;
		g2d.setColor(Color.BLUE);
		for (int i = 0; i < b.length; i++) {
			double percent = 1.0000 * b[i] / max;
			double l = 1.0000 * height * percent;
			// System.out.println(b[i]);
			int length = (int) l;
			if (length > 0) {
				g2d.drawLine(x, 159, x, 159 - (length));
				g2d.drawLine(++x, 159, x, 159 - (length));
				x++;
			} else {
				x += 2;
			}
		}

		// szary
		x = 531;
		g2d.setColor(Color.BLACK);
		for (int i = 0; i < b.length; i++) {
			double percent = ((1.0000 * b[i] + 1.0*r[i] + 1.0*this.g[i])/3) / max;
			double l = 1.0000 * height * percent;
			// System.out.println(b[i]);
			int length = (int) l;
			if (length > 0) {
				g2d.drawLine(x, 329, x, 329 - (length));
				g2d.drawLine(++x, 329, x, 329 - (length));
				x++;
			} else {
				x += 2;
			}
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Draw(g);
	}
}
