package make;

public class Bounts {

	public static int nomes = 0;
	private boolean selected = false;

	public Bounts(int xx, int yy) {
		x = w = xx;
		y = h = yy;
		nome = nomes + "";
		nomes++;
	}

	public int x, y, w, h;
	public String nome;

	@Override
	public String toString() {
		return "X:" + x + " Y:" + y + " LARGURA:" + w + " ALTURA:" + h;
	}

	public void math() {
		w = w - x;
		h = h - y;
		w++;
		h++;
	}

	public boolean hitTest(int x2, int y2, float zoom) {
		return (x2 > x*zoom && x2 < (x + w)*zoom) && (y2 > y*zoom && y2 < (y + h)*zoom);
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isSelected() {
		return selected;
	}
}
