package gerenciadores;

import java.awt.FileDialog;

import javax.swing.JFrame;

import terceiros.OpenFileFilter;
import window.Inicial;

public class FileManager {
	JFrame frame = null;

	public FileManager(JFrame f) {
		frame = f;
	}

	public FileManager() {
		// TODO Auto-generated constructor stub
	}

	public String open_image() {
		FileDialog fd = new FileDialog(frame, "Pegar Imagem", FileDialog.LOAD);
		fd.setDirectory(Inicial.conf.getLoadPath());
		fd.setFile("*.jpg;*.png;*.bmp");
//		fd.setFilenameFilter(new OpenFileFilter(extension));
		fd.setVisible(true);

		String name = null;
		if (name != fd.getDirectory()) {
			name = fd.getDirectory() + fd.getFile();
			Inicial.conf.setLoadPath(name);
		}
		return name;
	}

	public void save_image() {

	}
}
