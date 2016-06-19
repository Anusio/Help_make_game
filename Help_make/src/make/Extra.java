package make;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import terceiros.blur.GaussianFilter;

public class Extra {
	private BufferedImage imageToExtra;
	private String relatorio = "";
	private int prospectNum = 1;
	private int xmax, ymax;
	private byte[][] matriz;
	private byte[][] marca;
	private Color color = new Color(0, 0, 0, 255);
	private CropColorComparator comp;
	private byte option = 0;
	public static final byte opHilight = 0x02;
	public static final byte opHilightRandom = 0x04;

	public static final byte opAlphaCrop = 0x08;
	public static final byte opAlphaCrop1pixel = 0x10;

	public static final byte opBlurShadow = 0x20;
	public static final byte opBlurUseColor = 0x40;

	public String getRelatorio() {
		return relatorio;
	}

	private void toRelatorio(String info) {
		relatorio += info.toUpperCase() + "\n";
	}

	public BufferedImage getImageToExtra() {
		return imageToExtra;
	}

	public void setOption(byte option) {
		this.option = option;
	}

	public void addOption(byte option) {
		this.option = (byte) (this.option | option);
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public ColorRGBA load(String imagempath) throws Exception {
		relatorio = "";
		toRelatorio("Abrindo imagem:" + imagempath);
		File fp = new File(imagempath);
		if (fp.isDirectory()) {
			throw new Exception("nao é arquivo");
		}
		imageToExtra = (ImageIO.read(new FileInputStream(imagempath)));
		toRelatorio("imagem de largura/altura:" + imageToExtra.getWidth() + "/"
				+ imageToExtra.getHeight());

		return new ColorRGBA(imageToExtra.getRGB(0, 0));
	}

	public void setProspectNum(int prospectNum) {

		if (prospectNum < 1) {
			prospectNum = 1;
		}
		this.prospectNum = prospectNum;
	}

	public void doExtra(CropColorComparator comparador) {

		comp = comparador;

		toRelatorio("Procurando bordas...");
		if (imageToExtra == null) {
			return;
		}
		matriz = getMatriz(imageToExtra);
		marca = new byte[matriz.length][matriz[0].length];

		xmax = matriz.length;
		ymax = matriz[0].length;
		toRelatorio("bordas:");

		for (int i = 0; i < marca.length; i++) {
			for (int j = 0; j < marca[0].length; j++) {
				marca(i, j, matriz, marca);
			}
		}

		// toRelatorio("Total de imagems a serem cortadas:" + bounts.size());
		// imageViewToCrop = getImageView(xmax, ymax, marca);
	}

	private byte[][] getMatriz(BufferedImage imageToCrop2) {
		int wid = imageToCrop2.getWidth();
		int hei = imageToCrop2.getHeight();
		byte[][] matriz = new byte[wid][hei];
		for (int i = 0; i < wid; i++) {
			for (int j = 0; j < hei; j++) {
				int colour = imageToCrop2.getRGB(i, j);
				byte red = (byte) ((colour & 0x00ff0000) >> 16);
				byte green = (byte) ((colour & 0x0000ff00) >> 8);
				byte blue = (byte) (colour & 0x000000ff);
				byte alpha = (byte) ((colour >> 24) & 0xff);
				matriz[i][j] = comp.color201(red, green, blue, alpha);
			}
		}
		return matriz;
	}

	private void marca(int i, int j, byte[][] matriz, byte[][] marca) {
		// marca
		if (isNotOut(i, j)) {
			if (marca[i][j] == 0) {
				// se for 0 ou 1 1= imagem 0 = afa
				if (matriz[i][j] != 0) {
					prospectar2(i, j);
				} else {
					marca[i][j] = 1;
				}
			}
		}
	}

	private void prospectar2(int x, int y) {
		ArrayList<Vector2I> pixelToLook = new ArrayList<Vector2I>();
		pixelToLook.add(new Vector2I(x, y));
		int icont = 0, xn, yn;
		Vector2I loc;

		hiligtColorRandomIfNeed();
		while (icont < pixelToLook.size()) {
			loc = pixelToLook.get(icont);
			icont++;

			int mx = -prospectNum;
			int my = -prospectNum;
			xn = loc.x;
			yn = loc.y;
			//
			while (mx < prospectNum + 1) {
				// System.out.println(pixelToLook.si/ze());
				xn = loc.x + mx;
				yn = loc.y + my;
				my++;
				if (my > prospectNum) {
					my = -1;
					mx++;
				}
				// se nao tiver fora
				if (!isNotOut(xn, yn)) {
					continue;
				}
				if (0 != (option & opHilight)) {
					if (matriz[xn][yn] == 0) {
						imageToExtra.setRGB(xn, yn, getHiligtColor());
					}
				}
				// e nao foi marcado
				if (marca[xn][yn] != 0) {
					continue;
				}
				// se for 0 marque 1
				if (matriz[xn][yn] == 0) {
					marca[xn][yn] = 1;
				} else {
					// senao marque 2, veja a borda e adiciona os circumvisinhos
					// para serem olhados tbm
					if (!(xn == loc.x && yn == loc.y)) {
						pixelToLook.add(new Vector2I(xn, yn));
					}
					marca[xn][yn] = 2;
				}
			}
		}
	}

	private void hiligtColorRandomIfNeed() {
		if (0 != (option & opHilightRandom)) {
			Random r = new Random();
			color = new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255),
					255);
		}
	}

	private int getHiligtColor() {
		return color.getRGB();
	}

	public void doAlpha() {
		if (0 != (option & opAlphaCrop1pixel)) {
			color = new Color(imageToExtra.getRGB(0, 0));
		}
		for (int i = 0; i < imageToExtra.getWidth(); i++) {
			for (int j = 0; j < imageToExtra.getHeight(); j++) {
				if (color.equals(new Color(imageToExtra.getRGB(i, j)))) {
					imageToExtra.setRGB(i, j,
							new Color(255, 255, 255, 0).getRGB());
				}
			}
		}
	}

	public void doBlur() {
		BufferedImage toblur = imageToExtra;
		if ((option & opBlurUseColor) != 0) {
			toblur = null;
			toblur = new BufferedImage(imageToExtra.getWidth(),
					imageToExtra.getHeight(), BufferedImage.TYPE_INT_ARGB);
			for (int i = 0; i < imageToExtra.getWidth(); i++) {
				for (int j = 0; j < imageToExtra.getHeight(); j++) {
					if (!isAlpha0(imageToExtra.getRGB(i, j))) {
						toblur.setRGB(i, j, color.getRGB());
					} else {
						toblur.setRGB(i, j, new Color(0, 0, 0, 0).getRGB());
					}
				}
			}
		}

		BufferedImage image = new BufferedImage(imageToExtra.getWidth(),
				imageToExtra.getHeight(), BufferedImage.TYPE_INT_ARGB);

		if (prospectNum <= 0) {
			prospectNum = 0;
		}

		GaussianFilter b = new GaussianFilter(prospectNum);
		b.filter(toblur, image);
		if ((option & opBlurShadow) != 0) {
			for (int i = 0; i < image.getWidth(); i++) {
				for (int j = 0; j < image.getHeight(); j++) {
					if (!isAlpha0(imageToExtra.getRGB(i, j))) {
						image.setRGB(i, j, imageToExtra.getRGB(i, j));
					}
				}
			}
		}
		imageToExtra = image;
	}

	private boolean isAlpha0(int color) {
		byte alpha = (byte) ((color >> 24) & 0xff);
		if (alpha == 0) {
			return true;
		}
		return false;
	}

	private boolean isNotOut(int i, int j) {
		if (i < 0) {
			return false;
		}
		if (j < 0) {
			return false;
		}
		if (i >= xmax) {
			return false;
		}
		if (j >= ymax) {
			return false;
		}
		return true;
	}
}
