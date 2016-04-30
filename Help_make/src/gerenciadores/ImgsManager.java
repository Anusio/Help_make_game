package gerenciadores;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class ImgsManager {
	private ArrayList<Pro_sprite> images;
	private int minx;
	private int miny;
	private int maxx;
	private int maxy;
	private Semaphore thearray = new Semaphore(1);

	public ImgsManager() {
		reset();
	}

	public void reset() {
		images = new ArrayList<>();
	}

	public void add(BufferedImage i) {
		try {
			thearray.acquire();
			images.add(new Pro_sprite(i));
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
			images.add(i);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			thearray.release();
		}
	}

	public void paint(Graphics g, float zoom, int x, int y) {
		if (zoom < 1) {
			if (zoom == 0) {
				zoom = -1;
			}
			zoom = 1 / (-zoom);
		}
		try {
			thearray.acquire();
			for (Pro_sprite i : images) {
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
		for (Pro_sprite i : images) {
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
		System.out.println(minx + " " + maxx + " " + miny + " " + maxy);
	}

	public int getW() {
		return maxx - minx;
	}

	public int getH() {
		return maxy - miny;
	}

	public void paint2(Graphics g) {
		try {
			thearray.acquire();

			for (Pro_sprite i : images) {
				if (i.draw) {
					g.drawImage(i.image, i.x - minx, i.y - miny, i.w, i.h, null);
				}
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			thearray.release();
		}
	}

	public boolean hasImgs() {
		if (images.size() == 0) {
			return false;
		}
		return true;
	}

	public void clear() {
		try {
			thearray.acquire();
			images.clear();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			thearray.release();
		}
	}

	public String size() {
		return images.size() + "";
	}
}
