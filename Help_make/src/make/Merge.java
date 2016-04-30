package make;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class Merge {
	private BufferedImage[] imgs;
	private BufferedImage tosave;
	private String relatorio = "";
	private byte option = 0;
	public final static byte WhidtMeio = 0x02;
	public final static byte HeightBaixo = 0x04;
	public static final byte Borda = 0x08;

	public Merge() {

	}

	public void setOption(byte option) {
		this.option = option;
	}

	private void toRelatorio(String info) {
		relatorio += info.toUpperCase() + "\n";
	}

	public void load(String pasta) throws Exception {
		relatorio = "";
		toRelatorio("Abrindo pasta:" + pasta);
		File fp = new File(pasta);
		if (!fp.isDirectory()) {
			throw new Exception("Arquivo nao encontrado");
		}
		String[] imagedir = getListImage(fp);

		toRelatorio("imagens encontradas:" + imagedir.length);
		toRelatorio("imagens:");

		imgs = new BufferedImage[imagedir.length];
		for (int i = 0; i < imagedir.length; i++) {
			toRelatorio(imagedir[i]);
			imgs[i] = ImageIO.read(new FileInputStream(pasta + "/"
					+ imagedir[i]));
		}
	}

	@SuppressWarnings("unchecked")
	private String[] getListImage(File fp) {
		String[] files = fp.list();
		String[] images;
		int cont = 0;
		for (int i = 0; i < files.length; i++) {
			if (isImg(files[i])) {
				cont++;
			}
		}
		images = new String[cont];
		cont = 0;
		for (int i = 0; i < images.length; i++) {
			if (isImg(files[i])) {
				images[cont] = files[i];
				cont++;
			}
		}
		//Arrays.sort(images,);
		return images;
	}

	private boolean isImg(String string) {
		if (string.endsWith(".png")) {
			return true;
		}
		if (string.endsWith(".jpg")) {
			return true;
		}
		if (string.endsWith(".bmp")) {
			return true;
		}
		return false;
	}

	public void makeSheet(int col, int dist) {
		// col < 1
		if (dist % 2 == 1) {
			dist++;
		}
		int width = 0, heigth = 0;
		for (int i = 0; i < imgs.length; i++) {
			if (imgs[i].getWidth() > width) {
				width = imgs[i].getWidth();
			}
			if (imgs[i].getHeight() > heigth) {
				heigth = imgs[i].getHeight();
			}
		}

		int wmul = imgs.length;
		int hmul = 1;

		if (col < imgs.length) {
			wmul = col;
			hmul = imgs.length / col;
		}

		if (imgs.length % 10 != 0) {
			hmul++;
		}

		toRelatorio("Quantidade de linhas/colunas:" + wmul + "/" + (hmul));

		toRelatorio("Largura e altura por imagem (largura/altura):" + width
				+ "/" + heigth);

		tosave = new BufferedImage((width + dist) * (wmul), (heigth + dist)
				* (hmul), BufferedImage.TYPE_INT_ARGB);

		Graphics2D g = tosave.createGraphics();
		int x, y;
		for (int i = 0; i < imgs.length; i++) {
			x = i % col;
			y = i / col;

			if (0 != (option & WhidtMeio)) {
				x = x * width + (width - imgs[i].getWidth()) / 2;
			} else {
				x = x * width;
			}

			if (0 != (option & HeightBaixo)) {
				y = y * heigth + (heigth - imgs[i].getHeight());
			} else {
				y = y * heigth;
			}
			x += dist * (1 + i % col) - dist / 2;
			y += dist * (1 + i / col) - dist / 2;
			if (0 != (option & Borda)) {
				g.draw(new Rectangle(x, y, imgs[i].getWidth(), imgs[i]
						.getHeight()));
			}
			g.drawImage(imgs[i], x, y, imgs[i].getWidth(), imgs[i].getHeight(),
					null);
		}
	}

	public void saveImg(String filepath) {
		if (tosave == null) {
			return;
		}

		FileManager.save(tosave, filepath);
	}

	public BufferedImage getTosave() {
		return tosave;
	}

	public String getRelatorio() {
		return relatorio;
	}
}
