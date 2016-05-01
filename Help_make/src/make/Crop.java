package make;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import structs.Pro_sprite;

public class Crop {
	private BufferedImage imageToCrop;
	private ArrayList<Bounts> bounts = new ArrayList<Bounts>();
	private String relatorio = "";
	private int prospectNum = 1;
	private int xmax, ymax;
	private BufferedImage imageViewToCrop;
	private byte[][] matriz;
	private byte[][] marca;
	private CropColorComparator comp;
	private byte option = 0;
	public static final byte opOrdena = 0x02;
	public static final byte opOrdProspect = 0x04;

	public Crop() {
	}

	public String getRelatorio() {
		return relatorio;
	}

	private void toRelatorio(String info) {
		relatorio += info.toUpperCase() + "\n";
	}

	public ArrayList<Bounts> getBounts() {
		return bounts;
	}

	public void setOption(byte option) {
		this.option = option;
	}

	public ColorRGBA load(String imagempath) throws Exception {
		relatorio = "";
		toRelatorio("Abrindo imagem:" + imagempath);
		File fp = new File(imagempath);
		if (fp.isDirectory()) {
			throw new Exception("nao é arquivo");
		}
		setImageToCrop(ImageIO.read(new FileInputStream(imagempath)));
		toRelatorio("imagem de largura/altura:" + imageToCrop.getWidth() + "/"
				+ imageToCrop.getHeight());

		return new ColorRGBA(imageToCrop.getRGB(0, 0));
	}

	public void setProspectNum(int prospectNum) {

		if (prospectNum < 1) {
			prospectNum = 1;
		}
		this.prospectNum = prospectNum;
	}

	public void setImageToCrop(BufferedImage imageToCrop) {
		this.imageToCrop = imageToCrop;
	}

	public BufferedImage getImageToCrop() {
		return imageToCrop;
	}

	public BufferedImage getImageviewToCrop() {
		return imageViewToCrop;
	}

	public void gemBounts() {

		comp = new CropColorComparator() {

			@Override
			public byte color201(int r, int g, int b, int a) {
				if (a != 0) {
					return 1;
				}
				return 0;
			}
		};
		
		toRelatorio("Procurando bordas...");
		if (imageToCrop == null) {
			return;
		}
		matriz = getMatriz(imageToCrop);
		marca = new byte[matriz.length][matriz[0].length];

		xmax = matriz.length;
		ymax = matriz[0].length;
		toRelatorio("bordas:");
		bounts = new ArrayList<Bounts>();
		if(0==(option & opOrdProspect)){
			for (int i = 0; i < marca[0].length; i++) {
				for (int j = 0; j < marca.length; j++) {
					marca(j, i, matriz, marca);
				}
			}
		}else{
			for (int i = 0; i < marca.length; i++) {
				for (int j = 0; j < marca[0].length; j++) {
					marca(i, j, matriz, marca);
				}
			}
		}
		if (0 != (option & opOrdena)) {
			Collections.sort(bounts, new Comparator<Bounts>() {
				@Override
				public int compare(Bounts o1, Bounts o2) {
					if (o1.y + o1.h == o2.y + o2.h || o1.y + o1.h/2 == o2.y + o2.h/2 || o1.y == o2.y) {
						if (o1.x < o2.x) {
							return -1;
						} else {
							return 1;
						}
					} else {
						if (o1.y < o2.y) {
							return -1;
						} else {
							return 1;
						}
					}
				}
			});
		}
		toRelatorio("Total de imagems a serem cortadas:" + bounts.size());
		// imageViewToCrop = getImageView(xmax, ymax, marca);
	}

	private void marca(int i, int j, byte[][] matriz, byte[][] marca) {
		// marca
		if (isNotOut(i, j)) {
			if (marca[i][j] == 0) {
				// se for 0 ou 1 1= imagem 0 = afa
				if (matriz[i][j] != 0) {
					Bounts tmp = new Bounts(i, j);
					prospectar2(i, j, tmp);
					tmp.math();// pois eta trocado x y e eu estou com
					// preguica de mudar na funcao...
					bounts.add(tmp);
					toRelatorio(tmp.toString());
				} else {
					marca[i][j] = 1;
				}
			}
		}
	}

	public BufferedImage getImageView(int w, int h, byte[][] matriz) {
		BufferedImage bufimg = new BufferedImage(w, h,
				BufferedImage.TYPE_BYTE_GRAY);
		Graphics g = bufimg.getGraphics();

		g.setColor(new Color(255, 255, 255));
		g.fillRect(0, 0, w, h);

		g.setColor(new Color(0, 0, 0));

		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz[0].length; j++) {

				if (matriz[i][j] != 1) {
					g.drawRect(i, j, 1, 1);
					System.out.print('#');
				} else {
					System.out.print(0);
				}
			}
			System.out.println();
		}

		return bufimg;
	}

	@SuppressWarnings("unused")
	private void prospectar(int x, int y, Bounts bounts2) {
		if (!isNotOut(x, y)) {
			return;
		}
		if (marca[x][y] != 0) {
			return;
		}
		if (matriz[x][y] == 0) {
			marca[x][y] = 1;
		} else {
			if (x < bounts2.x) {
				bounts2.x = x;
			}
			if (x > bounts2.w) {
				bounts2.w = x;
			}

			if (y < bounts2.y) {
				bounts2.y = y;
			}
			if (y > bounts2.h) {
				bounts2.h = y;
			}
			marca[x][y] = 2;

			int mx = -prospectNum, my = -prospectNum;
			while (mx < prospectNum + 1) {
				prospectar(x + mx, y + my, bounts2);
				my++;
				if (my > prospectNum) {
					my = -1;
					mx++;
				}
			}
		}
	}

	private void prospectar2(int x, int y, Bounts bounts2) {
		ArrayList<Vector2I> pixelToLook = new ArrayList<Vector2I>();
		pixelToLook.add(new Vector2I(x, y));
		int icont = 0, xn, yn;
		Vector2I loc;

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

					if (xn < bounts2.x) {
						bounts2.x = xn;
					}
					if (xn > bounts2.w) {
						bounts2.w = xn;
					}

					if (yn < bounts2.y) {
						bounts2.y = yn;
					}
					if (yn > bounts2.h) {
						bounts2.h = yn;
					}
					marca[xn][yn] = 2;
				}
			}
		}
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

	public void saveGemBounts(String file, String nome) throws IOException {
		File fp = new File(file);
		if (!fp.isDirectory()) {
			JOptionPane.showMessageDialog(null, "nao é diretorio");
			return;
		}
		int cont = 0;

		for (Bounts b : bounts) {
			ImageIO.write(imageToCrop.getSubimage(b.x, b.y, b.w, b.h), "PNG",
					new File(file + "/" + nome + cont + ".png"));
			cont++;
		}

	}

	public ArrayList<Pro_sprite> cut(ArrayList<Bounts> bounds) {
		ArrayList<Pro_sprite> temp = new ArrayList<>();
		
		for (Bounts b : bounds) {
			Pro_sprite t = new Pro_sprite();
			t.image = imageToCrop.getSubimage(b.x, b.y, b.w, b.h);
			t.h = b.h;
			t.w = b.w;
			t.x = b.x;
			t.y = b.y;
			temp.add(t);
		}
		return temp;
	}
	
}


class Vector2I {
	public int x, y;

	public Vector2I(int xx, int yy) {
		x = xx;
		y = yy;
	}
}
