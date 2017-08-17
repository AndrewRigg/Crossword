package guis;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import crossword.CrosswordGenerator;
import crossword.SetUpImages;
import sudoku.SudokuGenerator;
import wordsearch.WordSearchGenerator;

public class PuzzleLoader implements ActionListener{
	public CrosswordGenerator crossword;
	public WordSearchGenerator wordsearch;
	public SudokuGenerator sudoku;
	public SetUpImages imageSetUp, imageSetUp2;
	JFrame frame;
	JPanel panel, panel2, grid, grid2;
	JButton cwd, wds, sud;
	JLabel intro, intro2, icon;
	Font font;
	String [] images = {"crossword", "wordsearch", "sudoku"}, image = {"Logo5"};
	Icon [] icons, logo;
	
	public PuzzleLoader(String user) throws IOException {
		
		frame = new JFrame("Auto Puzzle Generator");
		frame.setMinimumSize(new Dimension(500,300));
		font = new Font("Agency FB", Font.BOLD, 90);
		
		intro = new JLabel("SUD");
		intro.setFont(font);
		intro.setForeground(Color.WHITE);
		
		intro2 = new JLabel(" WORDS");
		intro2.setFont(font);
		intro2.setForeground(Color.WHITE);
		
		icons = new Icon[4];
		imageSetUp = new SetUpImages(images, 100, 100, icons, 0);
		
		logo = new Icon[1];
		imageSetUp2 = new SetUpImages(image, 75, 50, logo, 0);
		
		cwd = new JButton();
		cwd.addActionListener(this);
		cwd.setIcon(icons[0]);	
		
		wds = new JButton();
		wds.addActionListener(this);
		wds.setIcon(icons[1]);
		
		sud = new JButton();
		sud.addActionListener(this);
		sud.setIcon(icons[2]);
		
		icon = new JLabel();
		icon.setBackground(Color.BLACK);
		icon.setIcon(logo[0]);
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;

		grid = new JPanel(new GridLayout(1, 3));
		
		grid.add(cwd);
		grid.add(wds);
		grid.add(sud);
		
		grid2 = new JPanel(new GridBagLayout());
		grid2.setBackground(Color.BLACK);
		
		c.weightx = 0.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		grid2.add(intro, c);
		
		c.gridx = 1;
		c.gridy = 0;
		grid2.add(icon, c);
		
		c.gridx = 2;
		c.gridy = 0;
		grid2.add(intro2, c);		
		
		panel = new JPanel(new GridBagLayout());
		
		c.weightx = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		panel.add(grid2, c);	
		
		c.gridx = 0;
		c.gridy = 1;
		panel.add(grid, c);
		
		frame.setContentPane(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);		
		//frame.setResizable(false);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == cwd){
			try {
				crossword = new CrosswordGenerator(15, 2);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		if(e.getSource() == wds){
			try {
				wordsearch = new WordSearchGenerator(15, 2, "words_cambridge.txt", "English", "English" , "None");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		if(e.getSource() == sud){
			try {
				sudoku = new SudokuGenerator(9, 2);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public static void main (String [] args){
		try {
			new PuzzleLoader("");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

