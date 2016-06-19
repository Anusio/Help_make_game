package window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;

import canvas.CPanel;
import gerenciadores.ConfigManager;
import gerenciadores.ProgramManager;
import javax.swing.JScrollPane;

public class Inicial {
	public static ConfigManager conf;
	private JFrame frmHelpsprite;
	private ProgramManager programManager = new ProgramManager();
	private CPanel canvas;
	/**
	 * Create the application.
	 */
	public Inicial() {
		conf = new ConfigManager();
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
		tabbedPane.addTab("Cortar", null, panel, null);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_3 = new JPanel();
		panel_3.setBackground(Color.WHITE);
		panel.add(panel_3, BorderLayout.NORTH);
		panel_3.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_1 = new JPanel();
		panel_3.add(panel_1);
		panel_1.setLayout(new GridLayout(0, 1, 0, 0));
		
		JButton btnAdicionarImagem = new JButton("Adicionar imagem");
		btnAdicionarImagem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					programManager.acao_open();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		panel_1.add(btnAdicionarImagem);
		
		JButton btnAutoCortar = new JButton("Auto cortar");
		btnAutoCortar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				programManager.acao_auto_cut_bordas();
			}
		});
		panel_1.add(btnAutoCortar);
		
		JButton btnAdicionarBorda = new JButton("Adicionar borda");
		panel_1.add(btnAdicionarBorda);
		
		JButton btnAjustar = new JButton("Ajustar");
		panel_1.add(btnAjustar);
		
		JButton btnLimpar = new JButton("Limpar 1");
		panel_1.add(btnLimpar);
		
		JButton btnLimparTodos = new JButton("Limpar todos");
		panel_1.add(btnLimparTodos);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel_2.setBackground(Color.WHITE);
		panel.add(panel_2);
		
		JPanel panel_4 = new JPanel();
		panel.add(panel_4, BorderLayout.SOUTH);
		panel_4.setLayout(new GridLayout(0, 1, 0, 0));
		
		JButton btnEfetuarCorte = new JButton("Efetuar corte");
		panel_4.add(btnEfetuarCorte);
		
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
		
		JScrollPane scrollPane = new JScrollPane();
		view.add(scrollPane, BorderLayout.CENTER);
		
		canvas = new CPanel();
		scrollPane.setViewportView(canvas);
		canvas.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		canvas.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e.getWheelRotation() > 0) {
					programManager.wminus();
				}
				if (e.getWheelRotation() < 0) {
					programManager.wplus();
				}
			}
		});
		canvas.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				//panel.move(e.getX(), e.getY());
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				//panel.move(e.getX(), e.getY());
				programManager.drag(e.getX(), e.getY());
			}
		});
		canvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				//panel.click(e.getX(), e.getY(), e.getButton());
				programManager.press(e.getX(), e.getY());
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				programManager.releasse(e.getX(),e.getY());
				
				if(e.getButton() == MouseEvent.BUTTON1){
					programManager.click(e.getX(),e.getY());
				}
			}
		});
	}

}
