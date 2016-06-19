package gerenciadores;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import structs.Pro_sprite;

public class ImgsManager {
	private ArrayList<Pro_sprite> allimgs = new ArrayList<>();
	private Semaphore thearray = new Semaphore(1);
	private int minx;
	private int miny;
	private int maxx;
	private int maxy;

	public ImgsManager() {
		reset();
	}

	public void reset() {
		try {
			thearray.acquire();
			allimgs = new ArrayList<>();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			thearray.release();
		}
		
		minx = maxx = miny = maxy = 0;
	}
	
	public void add(BufferedImage i) {
		try {
			thearray.acquire();
			allimgs.add(new Pro_sprite(i));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			thearray.release();
		}
	}

	public void add(Pro_sprite i) {
		try {
			thearray.acquire();
			allimgs.add(i);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			thearray.release();
		}
	}

	public void paint(Graphics g, float zoom, int x, int y) {

		try {
			thearray.acquire();
			for (Pro_sprite i : allimgs) {
				if (i.draw) {
					g.drawImage(i.image, (int) (zoom * (i.x + x)), (int) (zoom * (i.y + y)), (int) (i.w * zoom),
							(int) (i.h * zoom), null);
				}
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			thearray.release();
		}
		
	}

	public void GetMetrics() {
		minx = Integer.MAX_VALUE;
		miny = Integer.MAX_VALUE;
		maxx = Integer.MIN_VALUE;
		maxy = Integer.MIN_VALUE;
		for (Pro_sprite i : allimgs) {
			if (i.x < minx) {
				minx = i.x;
			}
			if (i.y < miny) {
				miny = i.y;
			}
			if (i.x + i.w > maxx) {
				maxx = i.x + i.w;
			}
			if (i.y + i.h > maxy) {
				maxy = i.y + i.h;
			}
		}
	}

	public int getW() {
		return maxx - minx;
	}

	public int getH() {
		return maxy - miny;
	}

	public void paint_img_origin(Graphics g) {
		try {
			thearray.acquire();

			for (Pro_sprite i : allimgs) {
				if (i.draw) {
					g.drawImage(i.image, i.x - minx, i.y - miny, i.w, i.h, null);
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			thearray.release();
		}
	}
}
