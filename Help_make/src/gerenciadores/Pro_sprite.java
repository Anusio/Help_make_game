package gerenciadores;

import java.awt.image.BufferedImage;

public class Pro_sprite {
	public BufferedImage image = null;
	public int x, y, w, h;
	public boolean draw = true;

	public Pro_sprite() {
		x = y = w = h = 0;
	}

	public Pro_sprite(BufferedImage i) {
		image = i;
		x = y = 0;
		w = image.getWidth(null);
		h = image.getHeight(null);
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

}
