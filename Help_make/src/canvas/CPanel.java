package canvas;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class CPanel extends JPanel {

	private int _h = 0;
	private int _w = 0;

	private BufferedImage background;

	@Override
	public void paint(Graphics g) {
		setBackGroundImage();
		_h = getHeight();
		_w = getWidth();
//		g.drawRect(0, 0, _w, _h);
//		g.setColor(new Color(255, 255, 255, 255));
//		g.fillRect(0, 0, _w, _h);
		g.drawImage(background, 0, 0,null);
		//info(g);
	}

	private void setBackGroundImage() {
		if (getWidth() == _w && getHeight() == _h) {
			return;
		}
		// delete background vai para gc
		background = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) background.getGraphics();
		int tam = 25;
		boolean flag = true;
		Color branco = new Color(245, 245, 245);
		Color preto = new Color(60, 60, 60);
		for (int i = 0; i*tam < getWidth(); i++) {
			for (int j = 0; j*tam < getHeight(); j++) {
				if ((i%2 == 0 && j%2 == 0)||(i%2 == 1 && j%2 == 1)) {
					flag = false;
					g.setColor(preto);
				} else {
					g.setColor(branco);
					flag = true;
				}
				g.fillRect(i * tam, j * tam, tam, tam);
			}
		}
	}

	private void info(Graphics g) {
		g.setColor(new Color(0, 0, 0));
		g.drawString("Color", 100, 100);
	}

}