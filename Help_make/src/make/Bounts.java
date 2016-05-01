package make;

public class Bounts {
	
	public static int nomes = 0;
	
	public Bounts(int xx, int yy) {
		x = w = xx;
		y = h = yy;
		nome = nomes+"";
		nomes++;
	}

	public int x, y, w, h;
	public String nome;
	
	@Override
	public String toString() {
		return "X:" + x + " Y:" + y + " LARGURA:" + w + " ALTURA:" + h;
	}

	public void math() {
		w = w-x;
		h = h-y;
		w++;
		h++;
	}

	public boolean hitTest(int x2, int y2, float zoom) {
		// TODO Auto-generated method stub
		return false;
	}

	public void selected(boolean b) {
		// TODO Auto-generated method stub
		
	}

}
