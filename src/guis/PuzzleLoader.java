package guis;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import crossword.CrosswordGenerator;
import crossword.SetUpImages;
import sudoku.SudokuGenerator;
import wordsearch.WordSearchGenerator;

public class PuzzleLoader extends JComponent implements ActionListener{
	private static final long serialVersionUID = 1L;
	public crossword.CrosswordGenerator crossword;
	public wordsearch.WordSearchGenerator wordsearch;
	public sudoku.SudokuGenerator sudoku;
	public sudoku.SudokuGenerator sudo;
	public SetUpImages imageSetUp, imageSetUp2, imageSetUp3, imageSetUp4;
	public SignUp signUp;
	public LogIn logIn;
	JLayeredPane layer2;
	@SuppressWarnings("rawtypes")
	JComboBox cwdDifficulty, wdsDifficulty, sudDifficulty, language, language2;
	String [] difficulty = {"EASY", "NORMAL", "HARD", "EXPERT"};
	JSpinner spinner1, spinner2, spinner3;
	SpinnerNumberModel model1, model2, model3;
	JFrame frame;
	JLayeredPane layer;
	JPanel panel, grid, country, country2;
	JButton cwd, wds, sud, signup, login;
	JLabel intro, pic, flag, flag2, arrow, clueLanguage, solutionLanguage;
	Font font, font2, font3, font4, font5;
	/// STEVE: these weren't initialised when automatic scroller difficulty was used
	///     2 implies defualt is "EASY" -- need to change this if we change defualt scroller value
	int cwdDiff=2; 
	int wdsDiff=2; 
	int sudDiff=2;
	String user;
	Icon [] icons;
	String [] images = {"crossword", "wordsearch", "sudoku"};
	String [] countries = {"english",  "french",  "german", "italian","spanish"};
	String [] arrows = {"arrow"};
	String [] countries2, countries3;
	Icon [] flags, flags2, arr1;
	Icon arr;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PuzzleLoader(String user) throws IOException {
		this.user = user;
		if(user == ""){
			frame = new JFrame("Auto Puzzle Generator");
		}else{
			frame = new JFrame("Welcome to PuzzleLoader "+ user);
		}

		frame.setMinimumSize(new Dimension(400,100));
		
		font = new Font("Century Gothic", Font.BOLD, 40);
		font2 = new Font("Century Gothic", Font.PLAIN, 24);
		font3 = new Font("Century Gothic", Font.PLAIN, 20);
		font3 = new Font("Century Gothic", Font.PLAIN, 16);
		font5 = new Font("Century Gothic", Font.PLAIN, 45);
		
		clueLanguage = new JLabel("CLUES");
		clueLanguage.setFont(font3);
		solutionLanguage = new JLabel("SOLUTIONS");
		solutionLanguage.setFont(font3);
		
		flag = new JLabel();
		flag.setBorder(BorderFactory.createEmptyBorder());
		//flag.setHorizontalAlignment(SwingConstants.CENTER);
		flag.setEnabled(true);
		flag.setVisible(true);
		mouseActionlabel(flag);
		
		flag2 = new JLabel();
		flag2.setBorder(BorderFactory.createEmptyBorder());
		//flag2.setHorizontalAlignment(SwingConstants.CENTER);
		flag2.setEnabled(true);
		flag2.setVisible(true);
		mouseActionlabel(flag2);
		
		arrow = new JLabel();
	//	arrow.setBorder(BorderFactory.createEmptyBorder());
		//flag2.setHorizontalAlignment(SwingConstants.CENTER);
		arrow.setEnabled(true);
		arrow.setVisible(true);
		
		countries2 = new String [5];
		countries3 = new String [5];
		for (int i = 0; i < countries.length; i++){
			countries2[i] = countries[i].toUpperCase();
			countries3[i] = countries[i].toUpperCase();
		}
		
		intro = new JLabel("Puzzle Generator");
		intro.setFont(font5);
		intro.setHorizontalAlignment(SwingConstants.CENTER);
		intro.setOpaque(true);
		//intro.setBounds(0, 0,frame.getWidth(),300);
		intro.setBackground(new Color(240,240,240,255));
		//intro.setBorder(null);
		
		country = new JPanel(new GridBagLayout());
		country.setBounds(266,10,155, 40);
		country.setVisible(true);
		country.setOpaque(false);
		
		country2 = new JPanel(new GridBagLayout());
		country2.setBounds(145,10,120, 40);
		country2.setVisible(true);
		country2.setOpaque(false);
		
		layer = new JLayeredPane();
		
		cwdDifficulty = new JComboBox(difficulty);
		cwdDifficulty.addActionListener(this);
		cwdDifficulty.setFont(font3);
		
		wdsDifficulty = new JComboBox(difficulty);
		wdsDifficulty.addActionListener(this);
		wdsDifficulty.setFont(font3);
		
		sudDifficulty = new JComboBox(difficulty);
		sudDifficulty.addActionListener(this);
		sudDifficulty.setFont(font3);
		
		language = new JComboBox(countries2);
		language.addActionListener(this);
		language.setFont(font4);
	//	language.setVisible(false);
			
		language2 = new JComboBox(countries3);
		language2.addActionListener(this);
		language2.setFont(font4);
	//	language2.setVisible(false);
		
		model1 = new SpinnerNumberModel(15, 4, 30, 1);
		spinner1 = new JSpinner(model1);
		spinner1.setForeground(Color.WHITE);
		spinner1.setEditor(new JSpinner.DefaultEditor(spinner1));
		spinner1.setFont(font2);
		
		model2 = new SpinnerNumberModel(15, 4, 50, 1);
		spinner2 = new JSpinner(model2);
		spinner2.setForeground(Color.WHITE);
		spinner2.setEditor(new JSpinner.DefaultEditor(spinner2));
		spinner2.setFont(font2);
		
		model3 = new SpinnerNumberModel(3, 1, 5, 1);
		spinner3 = new JSpinner(model3);
		spinner3.setForeground(Color.WHITE);
		spinner3.setEditor(new JSpinner.DefaultEditor(spinner3));
		spinner3.setFont(font2);
		spinner3.setEnabled(false);
		
		panel = new JPanel(new GridBagLayout());
		panel.setBackground(new Color(220,220,250,255));
		panel.setBounds(0,0,440,200);
		panel.setOpaque(false); 
		
		
		grid = new JPanel(new GridLayout(1, 3));
		cwd = new JButton();
		cwd.setFont(font2);
		cwd.setHorizontalAlignment(SwingConstants.CENTER);
		cwd.addActionListener(this);
		wds = new JButton();
		wds.setFont(font2);
		wds.setHorizontalAlignment(SwingConstants.CENTER);
		wds.addActionListener(this);
		sud = new JButton();
		sud.setFont(font2);
		sud.setHorizontalAlignment(SwingConstants.CENTER);
		sud.addActionListener(this);
		
		pic = new JLabel("");
		pic.setOpaque(false);
		pic.setBounds(0, 0, frame.getWidth(), 300);
		pic.setBackground(new Color(255,255,255,255));
		pic.setBorder(null);
		
		icons = new Icon[3];
		imageSetUp = new SetUpImages(images, 100, 100, icons, 0);
		
		flags = new Icon [5];
		flags2 = new Icon [5];
		arr1 = new Icon[1];
		imageSetUp2 = new SetUpImages(countries, 20, 30, flags, 0);
		imageSetUp3 = new SetUpImages(countries, 20, 30, flags2, 0);
		imageSetUp4 = new SetUpImages(arrows, 20, 30, arr1, 0);
		
		cwd.setIcon(icons[0]);
		wds.setIcon(icons[1]);
		sud.setIcon(icons[2]);
	
		flag.setIcon(flags[0]);
		flag2.setIcon(flags2[0]);
		arrow.setIcon(arr1[0]);
		
		frame.setPreferredSize(new Dimension(440,228));
		frame.setSize(440, 200);
				
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 0;	
		c.gridwidth = 1;
		c.insets = new Insets(0,0,0,0);
		//country.add(arrow, c);
		
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.insets = new Insets(0,0,0,0);
		//country.add(flag2, c);
		//panel.add(flag2, c);
		
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 1;
		//country.add(language2, c);
		//panel.add(language2, c);
		
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 2;
		c.gridheight = 1;		
		//country2.add(flag, c);
		//panel.add(flag, c);
		
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridx = 1;
		c.gridy = 2;
		c.gridheight = 1;		
		//country2.add(language, c);
		//panel.add(language, c);
		
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.gridx = 1;
		c.gridy = 1;	
		c.gridwidth = 1;
		c.insets = new Insets(0,10,0,0);
		//country2.add(clueLanguage, c);
		//panel.add(clueLanguage, c);
		
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridx = 1;
		c.gridy = 3;	
		c.gridwidth = 2;
		c.insets = new Insets(0,0,0,0);
		//country.add(solutionLanguage, c);
		//panel.add(solutionLanguage, c);
		
		grid.add(cwd);
		grid.add(wds);
		grid.add(sud);
		
		c.weightx = 0.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 3;
		c.insets = new Insets(0,0,0,0);
		panel.add(intro, c);
		
		
		
		
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 2;
		panel.add(grid, c);
		
		c.fill = GridBagConstraints.BOTH;	
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.gridx = 2;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.ipady = 5;
		c.insets = new Insets(60,10,50,20);
		//panel.add(spinner1, c);
		
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.gridx = 2;
		c.gridy = 1;
		c.gridheight = 1;
		c.insets = new Insets(50,10,50,20);
		//panel.add(spinner2, c);
		
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.gridx = 2;
		c.gridy = 2;
		c.gridheight = 1;
		c.insets = new Insets(70,70,75,10);
		//panel.add(spinner3, c);
		
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 1;
		c.insets = new Insets(65,10,50,100);
		//panel.add(cwdDifficulty, c);
		
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = new Insets(55,10,50,100);
		//panel.add(wdsDifficulty, c);
		
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 1;
		c.insets = new Insets(38,10,10,100);
		//panel.add(sudDifficulty, c);
		
		
		layer = new JLayeredPane();
		layer.setAlignmentX(frame.getWidth());
		layer.add(panel, new Integer(0));
//		layer.add(country, new Integer(0));
//		layer.add(country2, new Integer(0));
		layer.setVisible(true);
		layer.setOpaque(true);
		
		
		
		frame.setContentPane(layer);
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);		
		frame.setResizable(false);
	}

	
	
	
	private int getDifficulty(int index) {   // WHAT IS THIS!?
		return (int) Math.pow(index, 2);
	}

	
	
	
	void mouseActionlabel(JLabel flagX) {
		flagX.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent e) {
				
			}

			public void mouseEntered(MouseEvent e) {
				if (e.getSource() == flag){
					//language.setVisible(true);
				}
				if (e.getSource() == flag2){
					//language2.setVisible(true);
				}
				
			}

			public void mouseExited(MouseEvent e) {
				
			}

			public void mousePressed(MouseEvent e) {

			}

			public void mouseReleased(MouseEvent e) {

			}
		});
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == cwdDifficulty){
			@SuppressWarnings("rawtypes")
			JComboBox cwdDifficulty = (JComboBox)e.getSource();
			String msg = (String)cwdDifficulty.getSelectedItem();
			if(msg.equals("EASY")){
				cwdDiff = 2;
			}
			if(msg.equals("NORMAL")){
				cwdDiff = 4;
			}
			if(msg.equals("HARD")){
				cwdDiff = 8;
			}
			if(msg.equals("EXPERT")){
				cwdDiff = 16;
			}
		}
		if(e.getSource() == wdsDifficulty){
			@SuppressWarnings("rawtypes")
			JComboBox wdsDifficulty = (JComboBox)e.getSource();
			String msg = (String)wdsDifficulty.getSelectedItem();
			if(msg.equals("EASY")){
				wdsDiff = 2;
			}
			if(msg.equals("NORMAL")){
				wdsDiff = 4;
			}
			if(msg.equals("HARD")){
				wdsDiff = 8;
			}
			if(msg.equals("EXPERT")){
				wdsDiff = 16;
			}
		}
		if(e.getSource() == sudDifficulty){
			@SuppressWarnings("rawtypes")
			JComboBox sudDifficulty = (JComboBox)e.getSource();
			String msg = (String)sudDifficulty.getSelectedItem();
			if(msg.equals("EASY")){
				sudDiff = 2;
			}
			if(msg.equals("NORMAL")){
				sudDiff = 4;
			}
			if(msg.equals("HARD")){
				sudDiff = 8;
			}
			if(msg.equals("EXPERT")){
				sudDiff = 16;
			}
		}
		
		if(e.getSource() == language){
			@SuppressWarnings("rawtypes")
			JComboBox language = (JComboBox)e.getSource();
			String msg = (String)language.getSelectedItem();
			if(msg.equals("ENGLISH")){
				flag.setIcon(flags[0]);
				//language.setVisible(false);
			}else if(msg.equals("SPANISH")){
				flag.setIcon(flags[4]);	
				//language.setVisible(false);
			}else if(msg.equals("ITALIAN")){
				flag.setIcon(flags[3]);
				//language.setVisible(false);
			}else if(msg.equals("FRENCH")){
				flag.setIcon(flags[1]);
				//language.setVisible(false);
			}else if(msg.equals("GERMAN")){
				flag.setIcon(flags[2]);
				//language.setVisible(false);
			}
		}
		
		if(e.getSource() == language2){
			@SuppressWarnings("rawtypes")
			JComboBox language2 = (JComboBox)e.getSource();
			String msg = (String)language2.getSelectedItem();
			if(msg.equals("ENGLISH")){
				flag2.setIcon(flags2[0]);
				//language2.setVisible(false);
			}else if(msg.equals("SPANISH")){
				flag2.setIcon(flags2[4]);	
				//language2.setVisible(false);
			}else if(msg.equals("ITALIAN")){
				flag2.setIcon(flags2[3]);
				//language2.setVisible(false);
			}else if(msg.equals("FRENCH")){
				flag2.setIcon(flags2[1]);
				//language2.setVisible(false);
			}else if(msg.equals("GERMAN")){
				flag2.setIcon(flags2[2]);
				//language2.setVisible(false);
			}
		}
		
		if(e.getSource() == cwd){
			try {
				cwdDiff = getDifficulty(cwdDifficulty.getSelectedIndex());
				crossword = new CrosswordGenerator((Integer)spinner1.getValue());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			//frame.dispose();
		}
		if(e.getSource() == wds){
			try {
				//System.out.println("wdsDiff: " + wdsDiff);
				wordsearch = new WordSearchGenerator((Integer)spinner2.getValue(), wdsDiff);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			//frame.dispose();
		}
		if(e.getSource() == sud){
			try {
				sudDiff = sudDifficulty.getSelectedIndex();
				System.out.println("diff: " +sudDiff);
				sudo = new SudokuGenerator(9, sudDiff);

			} catch (IOException e1) {
				e1.printStackTrace();
			}
			//frame.dispose();
		}
		if (e.getSource() == signup){
			try {
				signUp = new SignUp();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			frame.dispose();
		}
		if (e.getSource() == login){
			try {
				logIn = new LogIn();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			frame.dispose();
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

