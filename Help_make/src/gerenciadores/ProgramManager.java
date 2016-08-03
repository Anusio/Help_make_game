/**
 * cortar >> corta automatico ou selecionado por mao
 * gerar spritesheet >> efeitos in como blur ou color >> ordenar em quadrados igualmente espacados
 * gerar animacoes >> selecionar e gerar nomes e sequencias em txt
 * gerar bones >> animacoes boneadas e bones from spriteshet, cortes em txt
 * 
 */
package gerenciadores;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import canvas.CPanel;
import javafx.scene.input.ZoomEvent;
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

	private static float zoom = 1f;
	private static float zoom2 = 1f;
	private static int g_x = 0;
	private static int g_y = 0;
	private int mdx;
	private int mdy;

	public static Click mouse = new Click();

	public ProgramManager() {

	}

	public static int getRealX(double x) {
		return (int) (x * zoom);
	}

	public static int getRealY(double y) {
		return (int) (y * zoom);
	}

	public static int getReal_X(double x) {
		return (int) ((x - g_x));
	}

	public static int getReal_Y(double y) {
		return (int) ((y - g_y));
	}

	public static int getReal_Z(double x) {
		return (int) (x * zoom);
	}

	/* ACTIONS */
	/* End */
	public void wminus() {
		zoom2 -= .2;
		if (zoom2 < 1 && zoom2 > -1) {
			zoom2 = -1;
		}
		if (zoom2 < 1) {
			zoom = 1 / (-zoom2);
		} else {
			zoom = zoom2;
		}
	}

	public void wplus() {
		zoom2 += .2;
		if (zoom2 < 1) {
			zoom = 1 / (-zoom2);
		} else {
			zoom = zoom2;
		}
	}

	public void clickl(int x, int y) {
		mouse.x = getReal_X(x);
		mouse.y = getReal_Y(y);
		mouse.btnsingle = true;
	}

	public void move(int x, int y) {
		mouse.x = getReal_X(x);
		mouse.y = getReal_Y(y);
	}

	public void drag(int x, int y) {
		if (mouse.btnr) {
			g_x = (x - mdx);
			g_y = (y - mdy);
		}
		mouse.x = getReal_X(x);
		mouse.y = getReal_Y(y);
	}

	public void press(int x, int y, int btn) {
		if (btn == MouseEvent.BUTTON3) {
			mouse.btnr = true;
		}
		if (btn == MouseEvent.BUTTON1) {
			mouse.btnl = true;
		}
		mdx = x - g_x;
		mdy = y - g_y;
	}

	public void releasse(int x, int y, int btn) {
		if (btn == MouseEvent.BUTTON3) {
			mouse.btnr = false;
		}
		if (btn == MouseEvent.BUTTON1) {
			mouse.btnl = false;
		}
	}

	/**/
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

		g.translate(g_x, g_y);
		g.drawLine(-300, 0, 300, 0);
		g.drawLine(0, -300, 0, 300);
		// g.drawString("x " + g_x + " y " + g_y + " z " + zoom, 0, 10);
		// g.drawString("x " + mouse.x + " y " + mouse.y, 0, 30);

		imgsManager.paint(g);

		Color selec = new Color(0, 255, 0, 255);
		Color nselec = new Color(255, 0, 0, 255);
		for (Bounts b : bordas) {
			if (mouse.btnsingle) {
				if (b.hitTest(mouse.x, mouse.y, g_x, g_y, zoom)) {
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
			g.drawRect(getRealX(b.x+imgsManager.getMinx()), getRealY(b.y+imgsManager.getMiny()), getReal_Z(b.w),getReal_Z(b.h));
		}
		mouse.btnsingle = false;
	}

	/* Completamente acao */
	public void acao_open() throws IOException {
		String temp = fileManager.open_image();
		if (temp != null) {
			BufferedImage image = ImageIO.read(new File(temp));
			imgsManager.add(image);
		}
	}

	public void acao_auto_cut_bordas(int n) {
		Crop crop = new Crop();
		BufferedImage tocrop;
		imgsManager.GetMetrics();
		tocrop = new BufferedImage(imgsManager.getW(), imgsManager.getH(), BufferedImage.TYPE_INT_ARGB);
		imgsManager.paint_img_origin(tocrop.getGraphics());
		crop.setImageToCrop(tocrop);
		crop.setProspectNum(n);
		crop.gemBounts();
		bordas = crop.getBounts();
	}

	public void acao_cortar_bordas() {

		imgsManager.cortar(bordas);
		bordas.clear();
	}

	public void limparsel() {
		Bounts remove = null;
		for (Bounts b : bordas) {
			if (b.isSelected()) {
				remove = b;
			}
		}
		if (remove != null) {
			bordas.remove(remove);
		}
	}

	public void limparAll() {
		if (ask("Deletar seleções de corte?")) {
			bordas.clear();
		}
	}

	private boolean ask(String string) {
		int q = JOptionPane.showConfirmDialog(null, string, "", JOptionPane.YES_NO_OPTION);
		if (q == JOptionPane.YES_OPTION) {
			return true;
		}
		return false;
	}

	public void resetZoom() {
		zoom = 1;
	}

	public void resetOrigem() {
		g_x = g_y = 0;
	}
}
