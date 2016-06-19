package gerenciadores;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.Timer;

import canvas.CPanel;
import make.Bounts;
import make.Crop;
import structs.Click;

public class ProgramManager implements ActionListener {

	private ImgsManager imgsManager = new ImgsManager();
	private CPanel animate = null;
	private Timer timer = null;
	private int fps = 30;
	private ArrayList<Bounts> bordas = new ArrayList<>();
	private FileManager fileManager = new FileManager();

	private float zoom = 1f;
	private int g_x = 0;
	private int g_y = 0;
	private Click mouse = new Click();
	private int mdx;
	private int mdy;
	private int mdxa;
	private int mdya;

	public ProgramManager() {

	}

	public void setAnimate(CPanel animate) {
		this.animate = animate;
		animate.setProgram(this);
	}

	public void startAnimation() {
		if (timer == null) {
			timer = new Timer(1000 / fps, this);
		}
		if (!timer.isRunning()) {
			timer.start();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		animate.repaint();
	}

	public void draw(Graphics g) {
		float zom = zoom;
		if (zom < 1) {

			zom = 1 / (-zom);
		}
		imgsManager.paint(g, zom, g_x, g_y);

		Color selec = new Color(0, 255, 0, 255);
		Color nselec = new Color(255, 0, 0, 255);
		for (Bounts b : bordas) {
			if (mouse.btn) {
				if (b.hitTest(mouse.x, mouse.y, g_x, g_y, zom)) {
					b.setSelected(true);
				} else {
					b.setSelected(false);
				}
			}
			if (b.isSelected()) {
				g.setColor(selec);
			} else {
				g.setColor(nselec);
			}
			g.drawRect((int) (zom * (b.x + g_x)), (int) (zom * (b.y + g_y)), (int) (b.w * zom), (int) (b.h * zom));
		}
		mouse.btn = false;
	}

	/* Completamente acao */
	public void acao_open() throws IOException {
		String temp = fileManager.open_image();
		if (temp != null) {
			BufferedImage image = ImageIO.read(new File(temp));
			imgsManager.add(image);
		}
	}

	public void acao_auto_cut_bordas() {
		Crop crop = new Crop();
		BufferedImage tocrop;
		imgsManager.GetMetrics();
		tocrop = new BufferedImage(imgsManager.getW(), imgsManager.getH(), BufferedImage.TYPE_INT_ARGB);
		imgsManager.paint_img_origin(tocrop.getGraphics());
		crop.setImageToCrop(tocrop);
		crop.setProspectNum(1);
		crop.gemBounts();
		bordas = crop.getBounts();
	}

	public void wminus() {
		zoom -= .2;
		if (zoom < 1) {
			if (zoom >= -1) {
				zoom = -1;
			}
		}
	}

	public void wplus() {
		zoom += .2;

		if (zoom < 1) {
			if (zoom >= -1) {
				zoom = 1;
			}
		}
	}

	public void click(int x, int y) {
		mouse.x = x;
		mouse.y = y;
		mouse.btn = true;
	}

	public void drag(int x, int y) {
		g_x = (x - mdx);
		g_y = (y - mdy);
	}

	public void press(int x, int y) {
		mdx = x - g_x;
		mdy = y - g_y;
	}

	public void releasse(int x, int y) {
		mdxa = x;
		mdya = y;
	}
}
