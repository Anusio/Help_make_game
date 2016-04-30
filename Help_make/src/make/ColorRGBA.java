package make;

public class ColorRGBA {
	public byte r, g, b, a;

	public ColorRGBA(int colour) {
		r = (byte) ((colour & 0x00ff0000) >> 16);
		g = (byte) ((colour & 0x0000ff00) >> 8);
		b = (byte) (colour & 0x000000ff);
		a = (byte) ((colour >> 24) & 0xff);
	}
}
