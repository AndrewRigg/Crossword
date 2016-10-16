package GUIs;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import Crossword.CrosswordGenerator;
import Sudoku.SudokuGenerator;
import WordSearch.WordSearchGenerator;

public class SetSize extends JComponent implements ActionListener{
	private static final long serialVersionUID = 1L;
	public CrosswordGenerator crossword;
	public WordSearchGenerator wordsearch;
	public SetDifficulty puz2;
	public SudokuGenerator sudoku;
	public PuzzleLoader puzzleLoader;
	FontPanel fontPanel;
	JFrame frame;
	JPanel panel, panel1;
	JButton generate;
	JButton back;
	ButtonModel blah;
	JLayeredPane layer;
	JLabel intro, pic;
	SpinnerNumberModel model;
	JSpinner spinner;
	Font font, font2;
	public String puzzle;
	ImageIcon image, image2, pic1, pic2;
	Image newimg;
	Image img;
	int difficulty = 4;
	ComponentListener comp;
	
	public SetSize(String puzzle, int difficulty) throws IOException {
		this.puzzle = puzzle;
		this.difficulty = difficulty;
		fontPanel = new FontPanel();
		font = new Font("Century Gothic", Font.BOLD, 40);
		font2 = new Font("Century Gothic", Font.PLAIN, 24);
		panel = new JPanel(new GridBagLayout());
		panel1 = new JPanel(new GridBagLayout());
		panel1.setOpaque(false);
		frame = new JFrame("Set Puzzle Size");
		frame.setSize(550, 400);
		panel1.setBounds(0, 0, frame.getWidth(), frame.getHeight()-50);
		//panel1.setAlignmentX(SwingConstants.CENTER);
		generate = new JButton("Generate");
		generate.setFont(font2);
		generate.setHorizontalAlignment(SwingConstants.CENTER);
		generate.addActionListener(this);
		// Set image path depending on OS
		String path1 = "";
		String path2 = "";
		String path3 = "";
		String path4 = "";
		//System.out.println(System.getProperty("os.name").toLowerCase());
		if( System.getProperty("os.name").toLowerCase().equals("linux")   ){
			
			path1 = "src/resources/back.png";
			path2 = "src/resources/back1.png";
			path3 = "src/resources/crossword.png";
			path4 = "src/resources/wordsearch.png";
		}
		else if(  System.getProperty("os.name").toLowerCase().contains("windows") ){
			path1 = "src\\resources\\back.png";
			path2 = "src\\resources\\back1.png";
			path3 = "src\\resources\\crossword.png";
			path4 = "src\\resources\\wordsearch.png";
		}
	
//		   AbstractAction buttonPressed = new AbstractAction() {
//	            @Override
//	            public void actionPerformed(ActionEvent e) {
//	            	//   frame.dispose();
//	            }
//	        };
//	        
//	    generate.addActionListener(buttonPressed);
//	    
//	    
//	    generate.getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_BACK_SPACE,0), "A_pressed");
//	    generate.getActionMap().put("A_pressed", buttonPressed);
//	    
//	    
//	    generate.getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_0,0), "b_pressed");
//	    generate.getActionMap().put("b_pressed", buttonPressed);
	    
		pic = new JLabel("");
		pic.setOpaque(false);
		pic.setBounds(0, 0, frame.getWidth(), frame.getHeight());
		pic.setBackground(new Color(255,255,255,255));
		pic.setBorder(null);
		
		image = new ImageIcon( path1 );  
		img = image.getImage();
		newimg = img.getScaledInstance(50, 30, java.awt.Image.SCALE_SMOOTH ) ; 
		image = new ImageIcon(newimg);
		image2 = new ImageIcon(path2);

		img = image2.getImage();
		newimg = img.getScaledInstance(50, 30, java.awt.Image.SCALE_SMOOTH ) ; 
		image2 = new ImageIcon(newimg);
		
		pic1 = new ImageIcon(path3);
		img = pic1.getImage();
		newimg = img.getScaledInstance(frame.getWidth(), frame.getHeight(), java.awt.Image.SCALE_SMOOTH ) ; 
		pic1 = new ImageIcon(newimg);
		
		pic2 = new ImageIcon(path4);
		img = pic2.getImage();
		newimg = img.getScaledInstance(frame.getWidth(), frame.getHeight(), java.awt.Image.SCALE_SMOOTH ) ; 
		pic2 = new ImageIcon(newimg);
		
		if(puzzle.equals("Crossword")){
			pic.setIcon(pic1);
		}else if(puzzle.equals("Word Search")){
			pic.setIcon(pic2);
		}
		
		back = new JButton("");
		back.setFont(font2);
		back.setHorizontalAlignment(SwingConstants.CENTER);
		mouseActionlabel(back);
		back.addActionListener(this);
		back.setIcon(image);
		back.setOpaque(false);
		
		back.setBounds(0, 0, 100, 100);
		back.setBackground(new Color(255,255,255,255));
		back.setBorder(null);
		
		frame.setPreferredSize(new Dimension(550,400));
		model = new SpinnerNumberModel(10, 3, 40, 1);
		spinner = new JSpinner(model);
		spinner.setForeground(Color.WHITE);
		spinner.setEditor(new JSpinner.DefaultEditor(spinner));
		spinner.setFont(font2);
		
		intro = new JLabel(puzzle + " Size");
		intro.setFont(font);
		intro.setHorizontalAlignment(SwingConstants.CENTER);
		intro.setForeground(Color.BLACK);
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 4;
		panel1.add(intro, c);
		
//		c.weightx = 1.0;
//		c.weighty = 0.0;
//		c.gridx = 1;
//		c.gridy = 0;
//		c.gridwidth = 1;
//		panel1.add(field, c);
		
		c.fill = GridBagConstraints.NONE;
		c.ipadx = 20;
		c.gridx = 3;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = new Insets(10,10,10,10);
		panel1.add(spinner, c);
	
		layer = new JLayeredPane();
		layer.setAlignmentX(frame.getWidth());
		layer.add(panel1, new Integer(0));
		layer.add(back, new Integer(0));
		layer.add(pic, new Integer(0));
		layer.setVisible(true);
		layer.setOpaque(true);
		
		//frame.getRootPane().add(layer, BorderLayout.CENTER);
		
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 0;
//		c.anchor = GridBagConstraints.WEST;
//	    c.fill = GridBagConstraints.BOTH;
		c.ipady = (int)(frame.getHeight()*0.8);
		c.insets = new Insets(0,0,0,0);
		panel.add(layer, c);
		
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 1;
		c.ipady = 10;
		//c.insets = new Insets(0,0,0,0);
		panel.add(generate, c);
		
		frame.setContentPane(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setMinimumSize(new Dimension(550,400));
		frame.setVisible(true);		
		frame.getRootPane().setDefaultButton(generate);
		frame.setResizable(false);
	}	
	
	void keyActionTextField(JTextField l) {

		l.addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
					back.doClick();
				}
				
				
//				if(e.getSource().equals(KeyEvent.VK_BACK_SPACE)){
//					if(puzzle.equals("Crossword")){
//						try {
//							puzzleLoader = new PuzzleLoader("");
//						} catch (IOException e1) {
//							e1.printStackTrace();
//						}
//						frame.dispose();	
//					}
//					if(puzzle.equals("Word Search")){
//						try {
//							puz2 = new SetDifficulty("Word Search");
//						} catch (IOException e1) {
//							// TODO Auto-generated catch block
//							e1.printStackTrace();
//						}
//						frame.dispose();
//					}
//				}
			}

			public void keyReleased(KeyEvent e) {
			}

			public void keyTyped(KeyEvent e) {

			}
		});
	}

	void mouseActionlabel(JButton b) {
		b.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent e) {
				
			}

			public void mouseEntered(MouseEvent e) {
				back.setIcon(image2);
			}

			public void mouseExited(MouseEvent e) {
				back.setIcon(image);
			}

			public void mousePressed(MouseEvent e) {

			}

			public void mouseReleased(MouseEvent e) {

			}
		});
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == generate){
			//System.out.println(puzzle);
			if(puzzle.equals("Crossword")){
				try {
					crossword = new CrosswordGenerator((Integer)spinner.getValue());
				}catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if(puzzle.equals("Word Search")){
				try {
					wordsearch = new WordSearchGenerator((Integer)spinner.getValue(), difficulty);
				}catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		if(e.getSource() == back){
			if(puzzle.equals("Crossword")){
				try {
					puzzleLoader = new PuzzleLoader("");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				frame.dispose();	
			}
			if(puzzle.equals("Word Search")){
				try {
					puz2 = new SetDifficulty("Word Search");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				frame.dispose();
			}
		}
	}
}