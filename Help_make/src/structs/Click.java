package structs;

public class Click {
	public int x, y;
	public boolean btnl = false;
	public boolean btnr = false;
	public boolean btnsingle = false;
	
	@Override
	public String toString() {
		return x+" "+y;
	}
	
}