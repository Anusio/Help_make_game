package window;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.JButton;
import java.awt.Color;
import javax.swing.ImageIcon;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.border.LineBorder;

import canvas.CPanelImage;

public class FileChooserImg implements ActionListener{

	private JFrame frame;
	private JTextField textField;
	private CPanelImage panel;
	private String inicial = "C:/Users/Anusio/Pictures/";
	private JComboBox comboBox;
	private Timer timer;
	
	/**
	 * Create the application.
	 */
	public FileChooserImg() {
		initialize();
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 800, 600);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		panel = new CPanelImage(inicial);
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		
		JPanel panel_1 = new JPanel();
		frame.getContentPane().add(panel_1, BorderLayout.NORTH);
		panel_1.setLayout(new GridLayout(0, 2, 0, 0));
		
		JPanel panel_4 = new JPanel();
		panel_1.add(panel_4);
		panel_4.setLayout(new BorderLayout(0, 0));
		
		comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"c:/descktop"}));
		panel_4.add(comboBox);
		
		JPanel panel_5 = new JPanel();
		panel_1.add(panel_5);
		panel_5.setLayout(new GridLayout(0, 6, 0, 0));
		
		JButton btnNewButton_1 = new JButton("");
		btnNewButton_1.setIcon(new ImageIcon(FileChooserImg.class.getResource("/icons/filesystems24/folder_green.png")));
		panel_5.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("");
		btnNewButton_2.setIcon(new ImageIcon(FileChooserImg.class.getResource("/icons/filesystems24/image.png")));
		panel_5.add(btnNewButton_2);
		
		JButton button_3 = new JButton("");
		panel_5.add(button_3);
		
		JButton button_4 = new JButton("");
		panel_5.add(button_4);
		
		JButton button_5 = new JButton("");
		panel_5.add(button_5);
		
		JButton button_6 = new JButton("");
		panel_5.add(button_6);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(new Color(0, 0, 0)));
//		panel_2.setPreferredSize(new Dimension(100, 100));
		frame.getContentPane().add(panel_2, BorderLayout.WEST);
		panel_2.setLayout(new GridLayout(0, 1, 0, 0));
		
		JButton btnNewButton = new JButton("");
		btnNewButton.setIcon(new ImageIcon(FileChooserImg.class.getResource("/icons/filesystems128/desktop.png")));
		panel_2.add(btnNewButton);
		
		JButton button = new JButton("");
		button.setIcon(new ImageIcon(FileChooserImg.class.getResource("/icons/filesystems128/folder_favorites.png")));
		panel_2.add(button);
		
		JButton button_1 = new JButton("");
		button_1.setIcon(new ImageIcon(FileChooserImg.class.getResource("/icons/filesystems128/folder_images.png")));
		panel_2.add(button_1);
		
		JButton button_2 = new JButton("");
		button_2.setIcon(new ImageIcon(FileChooserImg.class.getResource("/icons/filesystems128/folder_grey.png")));
		panel_2.add(button_2);
		
		JPanel panel_3 = new JPanel();
		frame.getContentPane().add(panel_3, BorderLayout.SOUTH);
		panel_3.setLayout(new BorderLayout(0, 0));
		
		textField = new JTextField();
		panel_3.add(textField);
		textField.setColumns(10);
		
		JButton btnEscolher = new JButton("Escolher");
		panel_3.add(btnEscolher, BorderLayout.EAST);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		panel.repaint();
	}
	
	public void startAnimation() {
		if (timer == null) {
			timer = new Timer(1000 / 30, this);
		}
		if (!timer.isRunning()) {
			timer.start();
		}
	}
}
