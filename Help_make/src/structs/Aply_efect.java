package structs;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class Aply_efect {
	
	public void do_efect(ArrayList<Pro_sprite> array) {
		for (Pro_sprite p: array) {
			do_efect(p);
		}
	}
	
	public void do_efect(Pro_sprite p) {
		p.image = do_efect(p.image);
	}
	
	public abstract BufferedImage do_efect(BufferedImage img);
}
