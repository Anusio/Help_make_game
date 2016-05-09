package canvas;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class CPanelImage extends JPanel {
	private ArrayList<imgFile> files = new ArrayList<>();
	private String lastinpath = "";
	private String thepath;

	public CPanelImage(String path) {
		thepath = path;
		lastinpath = path + "a";
	}

	@Override
	public void paint(Graphics g) {
		newFolder(thepath);
		g.setColor(new Color(255, 255, 255));
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		int x, y, h, w;
		int box = 128;
		int dist = 20;
		int a = 0;
		int prow = 5;
		for (imgFile i : files) {
			x = ((a % prow) * (box + dist));
			y = ((a / prow) * (box + dist));
			w = box;
			h = box;
			if (i.img != null) {
				g.drawImage(i.img, x, y, w, h, null);
			}
			g.setColor(new Color(0, 0, 0));
			g.drawRect(x, y, w, h);
			g.drawString(i.name, x, h + y + 10);
			// g.fillRect(x, y, w, h);
			// hittest and thepth is this
			a++;
		}
	}

	public void setThepath(String thepath) {
		this.thepath = thepath;
	}

	private void newFolder(String path) {
		if (lastinpath.equals(path)) {
			return;
		}
		lastinpath = path;
		File file = new File(path);
		File fp;
		imgFile im;
		files.clear();
		String[] all = file.list();
		if (all != null) {
			for (String s : all) {
				im = new imgFile();
				if (s.length() < 16) {
					im.name = s;
				} else {
					im.name = s.substring(0, 16);
				}
				s = path + "/" + s;

				fp = new File(s);
				im.path = s;
				if (fp.isHidden()) {
					continue;
				}
				if (fp.isDirectory()) {
					im.type = imgFile.DIR;
					try {
						if (cotain_img(s)) {
							im.img = ImageIO.read(CPanelImage.class.getResource("/icons/filesystems128/folder_images.png"));
						} else {
							im.img = ImageIO.read(CPanelImage.class.getResource("/icons/filesystems128/folder.png"));
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					try {
						BufferedImage img = ImageIO.read(fp);
						im.type = imgFile.IMG;
						im.img = img;
					} catch (Exception e) {
						im.type = imgFile.OTH;
						try {
							im.img = ImageIO.read(CPanelImage.class.getResource("/icons/filesystems128/file.png"));
						} catch (IOException e2) {
							e2.printStackTrace();
						}
					}
				}
				files.add(im);
			}
		}
	}

	private boolean cotain_img(String s) {
		File fp = new File(s);
		String[] all = fp.list();
		for (int i = 0; i < all.length; i++) {
			File f = new File(s + "/" + all[i]);
			String mimetype = new MimetypesFileTypeMap().getContentType(f);
			String type = mimetype.split("/")[0];
			if (type.equals("image")) {
				return true;
			}
		}
		return false;
	}
}

class imgFile {
	public static final int DIR = 1;
	public static final int IMG = 2;
	public static final int OTH = 3;
	// public static final int DIRIMG = 4;

	public int type = 0;
	public String path;
	public String name;
	public BufferedImage img = null;
}