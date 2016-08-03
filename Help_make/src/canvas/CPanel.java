package canvas;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import gerenciadores.ProgramManager;

@SuppressWarnings("serial")
public class CPanel extends JPanel {

	private int _h = 0;
	private int _w = 0;

	private BufferedImage background;
	private ProgramManager programManager;

	@Override
	public void paint(Graphics g) {
		setBackGroundImage();
		_h = getHeight();
		_w = getWidth();
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.black);
		g.drawImage(background, 0, 0, null);

		if (programManager != null) {
			programManager.draw(g);
		}
	}

	private void setBackGroundImage() {
		if (getWidth() == _w && getHeight() == _h) {
			return;
		}
		// delete background vai para gc
		background = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) background.getGraphics();
		int tam = 25;
		Color branco = new Color(245, 245, 245);
		Color preto = new Color(60, 60, 60);
		for (int i = 0; i * tam < getWidth(); i++) {
			for (int j = 0; j * tam < getHeight(); j++) {
				if ((i % 2 == 0 && j % 2 == 0) || (i % 2 == 1 && j % 2 == 1)) {
					g.setColor(preto);
				} else {
					g.setColor(branco);
				}
				g.fillRect(i * tam, j * tam, tam, tam);
			}
		}
	}

	public void setProgram(ProgramManager programManager) {
		this.programManager = programManager;
	}

}