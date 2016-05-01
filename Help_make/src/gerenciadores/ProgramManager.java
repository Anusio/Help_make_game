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

public class ProgramManager implements ActionListener{
	
	private ImgsManager imgsManager = new ImgsManager();
	private CPanel animate = null;
	private Timer timer = null;
	private int fps = 30;
	private ArrayList<Bounts> bordas = new ArrayList<>();
	
	private float zoom = 1f;
	private int g_x = 0;
	private int g_y = 0;
	
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
//		zoom -= 0.1;
//		System.out.println(zoom);
		imgsManager.paint(g, zoom, g_x, g_y);

		float zom = zoom;
		if (zom < 1) {
			if (zom >= -0.1) {
				zom = -1f;
			}
			zom = 1 / (-zom);
		}
		g.setColor(new Color(255, 0, 0, 255));
		for (Bounts b : bordas) {
			g.drawRect((int) (zom * (b.x + g_x)), (int) (zom * (b.y + g_y)), (int) (b.w * zom), (int) (b.h * zom));
		}
		
	}
	
	/*Completamente acao*/
	public void acao_open(String path) throws IOException {
		BufferedImage image = ImageIO.read(new File(path));
		imgsManager.add(image);
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
		bordas  = crop.getBounts();
	}
}


















