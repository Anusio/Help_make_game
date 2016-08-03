package gerenciadores;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import make.Bounts;
import structs.Click;
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

	private Pro_sprite selescted = null;
	private int sely, selx;
	private boolean selectable = true;
	private Click mouse;
	public void paint(Graphics g) {
		mouse = ProgramManager.mouse;
		try {
			thearray.acquire();
			if (!mouse.btnl) {
				selescted = null;
			}
			Pro_sprite lastsel = null;
			for (Pro_sprite i : allimgs) {
				if (i.draw) {
					int _x = ProgramManager.getRealX(i.x);
					int _y = ProgramManager.getRealY(i.y);
					int _w = ProgramManager.getReal_Z(i.w);
					int _h = ProgramManager.getReal_Z(i.h);

					g.drawImage(i.image, _x, _y, _w, _h, null);

					if (selectable && selescted == null && mouse.btnl && hitTest(mouse.x, mouse.y, _x, _y, _w, _h, 1)) {
						lastsel = i;
					}
					
					if (hitTest(mouse.x, mouse.y, _x, _y, _w, _h, 1))
						g.drawRect(_x, _y, _w, _h);
				}
			}
			
			if(selescted != null){
				selescted.x = (int) (selx + mouse.x);
				selescted.y = (int) (sely + mouse.y);
			}else{
				if(lastsel != null){
					selescted = lastsel;
					sely = (selescted.y - mouse.y);
					selx = (selescted.x - mouse.x);
				}
			}
			
//			g.setColor(Color.black);
//			g.setFont(new Font("TimesRoman", Font.PLAIN, 18)); 
//			g.drawString(_x+" "+_y, 10, 60);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			thearray.release();
		}

	}

	public boolean hitTest(int x2, int y2, int x, int y, int w, int h, float zoom) {
		return (x2 > x * zoom && x2 < (x + w) * zoom) && (y2 > y * zoom && y2 < (y + h) * zoom);
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

	public int getMinx() {
		return minx;
	}
	
	public int getMiny() {
		return miny;
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

	public void cortar(ArrayList<Bounts> bordas) {
		try {
			BufferedImage tocrop;
			GetMetrics();
			tocrop = new BufferedImage(getW(), getH(), BufferedImage.TYPE_INT_ARGB);
			paint_img_origin(tocrop.getGraphics());
			thearray.acquire();
			allimgs.clear();
			for (Bounts b : bordas) {
				Pro_sprite e = new Pro_sprite(tocrop.getSubimage(b.x, b.y, b.w, b.h));
				e.x = b.x;
				e.y = b.y;
				allimgs.add(e);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			thearray.release();
		}
	}
}
