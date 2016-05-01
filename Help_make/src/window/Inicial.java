package window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.LineBorder;

import canvas.CPanel;
import gerenciadores.ProgramManager;

import javax.swing.JTabbedPane;
import javax.swing.border.BevelBorder;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class Inicial {

	private JFrame frmHelpsprite;
	private ProgramManager programManager = new ProgramManager();
	private CPanel canvas;
	/**
	 * Create the application.
	 */
	public Inicial() {
		initialize();
		frmHelpsprite.setVisible(true);
		programManager.setAnimate(canvas);
		programManager.startAnimation();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmHelpsprite = new JFrame();
		frmHelpsprite.setTitle("Help_sprite");
		frmHelpsprite.setBounds(100, 100, 800, 600);
		frmHelpsprite.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frmHelpsprite.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("Arquivo");
		menuBar.add(mnFile);
		
		JMenuItem mntmOpen = new JMenuItem("Abrir");
		mnFile.add(mntmOpen);
		
		JMenuItem mntmSave = new JMenuItem("Salvar");
		mnFile.add(mntmSave);
		
		JMenuItem mntmSaveAs = new JMenuItem("Salvar como");
		mnFile.add(mntmSaveAs);
		
		JSplitPane splitPane = new JSplitPane();
		frmHelpsprite.getContentPane().add(splitPane, BorderLayout.CENTER);
		
		JPanel ferramentas = new JPanel();
		ferramentas.setPreferredSize(new Dimension(200, 200));
		splitPane.setLeftComponent(ferramentas);
		ferramentas.setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		ferramentas.add(tabbedPane);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Ação", null, panel, null);
		
		JButton btnAbrir = new JButton("Abrir");
		btnAbrir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					programManager.acao_open("teste.png");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		panel.add(btnAbrir);
		
		JButton btnBordas = new JButton("bordas");
		btnBordas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				programManager.acao_auto_cut_bordas();
			}
		});
		panel.add(btnBordas);
		
		JPanel pnItem = new JPanel();
		pnItem.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		pnItem.setPreferredSize(new Dimension(100, 100));
		ferramentas.add(pnItem, BorderLayout.NORTH);
		
		JPanel view = new JPanel();
		splitPane.setRightComponent(view);
		view.setLayout(new BorderLayout(0, 0));
		
		JPanel info = new JPanel();
		info.setBorder(new LineBorder(new Color(0, 0, 0)));
		info.setBackground(Color.WHITE);
		view.add(info, BorderLayout.NORTH);
		info.setLayout(new BorderLayout(0, 0));
		
		JLabel lblLblinfo = new JLabel("lblinfo");
		info.add(lblLblinfo, BorderLayout.CENTER);
		
		JButton btnOpes = new JButton("Op\u00E7\u00F5es");
		info.add(btnOpes, BorderLayout.EAST);
		
		canvas = new CPanel();
		canvas.setBorder(new LineBorder(new Color(0, 0, 0)));
		view.add(canvas, BorderLayout.CENTER);
	}

}
