package wordsearch;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import crossword.Entry;
import crossword.SetUpImages;
import resources.ResourceLoader;
import wordsearch.Coord;

/**
 * Class to draw a word search
 */
public class DrawWordSearch extends JComponent implements ActionListener, MouseWheelListener, MouseListener {
	private static final long serialVersionUID = 1L;
	int x, y, x_pos, y_pos, counter, wordLength, dir, startx, starty, squareSize, tempLayerX, tempLayerY, layerX, layerY, difficulty, startWordX, startWordY;
	final static int INITIAL_SQUARE_SIZE = 80, NUMBER_OF_LAYERS = 8;
	double width, height, mouseX, mouseY, scale, normalisedScale, tempLayerWidth, tempLayerHeight, tempScale;
	final double MAX_SCALE, MIN_SCALE;
	boolean buttonPushed, clicked, start, congratulations, reset, diagonal, notIn, mouseHeld;
	public String currentFont, dictionary, currentTopic, languageFrom, languageTo, tempWord, sortMethod, randomFill, thisWord;
	String[] fontNames, countries, topicWords, dictionaries,
			 ordering = { "RANDOM", "ALPHABETICAL", "BIGGEST", "SMALLEST" },
			 loopDirections = { "TopLeft", "Left", "BottomLeft", "Top", "Bottom", "TopRight", "Right", "BottomRight" };
	String[][] grid;
	ArrayList<String> fullGrid, tempStrikethrough, struckThrough, solutions, clueText, sorted, cluesAcross, cluesDown,randomLetters;

	JFrame frame;
	JLayeredPane layer, layer2, extra;
	JPanel panel, main, clues, mouseTrail;
	@SuppressWarnings({ "rawtypes" })
	JComboBox orderClues;
	JButton solution, hint, clue;
	JScrollPane area;
	JMenuItem exit, thick, normal, smart, genius, save, newGame, tips, french, english, german, italian, spanish, french2, english2, german2, italian2, spanish2;
	JMenuBar menuBar;
	JMenu file, diff, options, languages, languages2, size, topic;
	JMenuItem[] country1, country2, topics;
	JSpinner spinner;
	SpinnerNumberModel model;
	JCheckBoxMenuItem clickSound;

	Icon[] flags;
	ArrayList<Icon[][]> temporaryIcons, temporaryIcons2;
	SetUpImages imageSetUp, setImages, tempImage, tempHead;
	ArrayList<Character> trail;
	ArrayList<Coord> letters;
	Point pointStart, pointEnd;
	GridBagConstraints c, d;

	ArrayList<Entry> entries;
	ArrayList<JLabel> allClues, completed;
	ArrayList<JLabel[][]> allLetters;
	WordSearchGenerator wordsearch;
	Font font, font2, font3, font4, font5, font6, font7;
	Random rand;
	Color grey, clear;
	Dimension screenSize;
	Border border, border2;
	ArrayList<Font> fonts;
	Color[] colours;
	Color currentColour;
	Entry dynamicEntry;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	
	public DrawWordSearch(String[][] grid, int x, int y, ArrayList<String> cluesAcross, ArrayList<String> cluesDown,
			ArrayList<Entry> entries, int difficulty, String languageFrom, String languageTo, String currentTopic)
			throws IOException {

		String[] fontNames = { "Agency FB", "Arial", "Broadway", "Calibri", "Castellar", "Century Gothic", "Consolas",
				"Courier New", "Copperplate Gothic Bold", "French Script MT", "Segoe Script", "Times New Roman" };
		String[] dictionaries = { "English_", "en_fr_", "en_de_", "en_it_", "en_pt_", "en_es_" };	
		currentFont = "Century Gothic";
		dictionary = "words_cambridge.txt";
		currentTopic = "None";
		languageFrom = "English";
		languageTo = "English";
		this.fontNames = fontNames;
		this.dictionaries = dictionaries;
		thisWord = "";
		mouseHeld = false;
		startWordX = 0;
		startWordY = 0;
		this.currentTopic = currentTopic;
		this.languageFrom = languageFrom;
		this.languageTo = languageTo;
	
		currentColour = Color.BLACK;
		trail = new ArrayList<>();
		this.difficulty = difficulty;
		String[] countries = { "English", "French", "German", "Italian", "Portuguese", "Spanish" };
		String[] topicWords = { "None", "Animals", "House", "Nature", "Places", "Time", "Verbs" };
		this.countries = countries;
		this.topicWords = topicWords;
		font3 = new Font(currentFont, Font.PLAIN, 14);
		country1 = new JMenuItem[countries.length];
		country2 = new JMenuItem[countries.length];
		topics = new JMenuItem[topicWords.length];

		flags = new Icon[countries.length];
		imageSetUp = new SetUpImages(countries, 20, 30, flags, 0);
		UIManager.put("Menu.font", font3);
		UIManager.put("MenuItem.font", font3);
		UIManager.put("CheckBoxMenuItem.font", font3);
		UIManager.put("RadioButtonMenuItem.font", font3);

		model = new SpinnerNumberModel(x - 2, 6, 30, 1);
		spinner = new JSpinner(model);
		spinner.setEditor(new JSpinner.DefaultEditor(spinner));
		spinner.setFont(font3);

		menuBar = new JMenuBar();
		file = new JMenu("File");
		options = new JMenu("Options");
		diff = new JMenu("Difficulty");
		size = new JMenu("Size");

		file.setMnemonic(KeyEvent.VK_F);
		options.setMnemonic(KeyEvent.VK_O);
		diff.setMnemonic(KeyEvent.VK_D);
		size.setMnemonic(KeyEvent.VK_S);

		save = setUpMenus("Save", null, null, null);
		newGame = setUpMenus("New", null, null, file);
		exit = setUpMenus("Exit", null, null, file);
		

		languages = new JMenu("Clue Language");
		languages2 = new JMenu("Grid Language");
		topic = new JMenu("Topics");
		border2 = BorderFactory.createBevelBorder(BevelBorder.LOWERED);

		
		
		
		for (int i = 0; i < countries.length; i++) {
			country1[i] = setUpMenus(countries[i], flags[i], null, languages);
			country2[i] = setUpMenus(countries[i], flags[i], null, languages2);
			if (countries[i].equals(languageFrom)) {
				country1[i].setBorder(border2);
			}
			if (countries[i].equals(languageTo)) {
				country2[i].setBorder(border2);
			}
		}
		
		for (int i = 0; i < topicWords.length; i++) {
			topics[i] = new JMenuItem(topicWords[i]);
			topics[i].addActionListener(this);
			if (topicWords[i].equals(currentTopic)) {
				topics[i].setBorder(border2);
			}
			topic.add(topics[i]);
		}

		// file.add(save);
		file.add(newGame);
		file.add(exit);

		options.add(languages);
		options.add(languages2);
		options.add(topic);

		ButtonGroup group = new ButtonGroup();
		thick = new JRadioButtonMenuItem("Easy");
		if (difficulty == 2) {
			thick.setSelected(true);
		}
		thick.addActionListener(this);
		thick.setMnemonic(KeyEvent.VK_1);
		group.add(thick);
		normal = new JRadioButtonMenuItem("Normal");
		normal.setMnemonic(KeyEvent.VK_2);
		if (difficulty == 4) {
			normal.setSelected(true);
		}
		normal.addActionListener(this);
		group.add(normal);
		smart = new JRadioButtonMenuItem("Hard");
		smart.setMnemonic(KeyEvent.VK_3);
		if (difficulty == 8) {
			smart.setSelected(true);
		}
		smart.addActionListener(this);
		group.add(smart);
		genius = new JRadioButtonMenuItem("Expert");
		genius.setMnemonic(KeyEvent.VK_4);
		if (difficulty == 16) {
			genius.setSelected(true);
		}
		genius.addActionListener(this);
		group.add(genius);

		diff.add(thick);
		diff.add(normal);
		diff.add(smart);
		diff.add(genius);

		size.add(spinner);

		menuBar.add(file);
		menuBar.add(diff);
		menuBar.add(size);
		menuBar.add(options);

		frame = new JFrame("Auto Word Search");
		frame.setBackground(new Color(255, 255, 255, 255));
		frame.setMinimumSize(new Dimension(550, 400));
		this.x = x;
		this.y = y;
		this.grid = grid;
		this.entries = entries;
		this.cluesAcross = cluesAcross;
		this.cluesDown = cluesDown;
		squareSize = 40;
		scale = 10;
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		width = screenSize.getWidth();
		height = screenSize.getHeight();

		if (squareSize * (x + 6) > width && squareSize * (y + 4) > height - 30) {
			frame.setPreferredSize(new Dimension((int) width, (int) height - 30));
			squareSize = (int) (height / (x * 1.2));
			scale = squareSize / 4;
		} else if (squareSize * (x + 6) > width) {
			frame.setPreferredSize(new Dimension((int) width, squareSize * (y + 2)));
			squareSize = (int) (height / (x * 1.2));
			scale = squareSize / 4;
		} else if (squareSize * (y + 4) > height - 30) {
			frame.setPreferredSize(new Dimension(squareSize * (x + 6), (int) height - 30));
			squareSize = (int) (height / (x * 1.2));
			scale = squareSize / 4;
		} else {
			frame.setPreferredSize(new Dimension(squareSize * (x + 6), squareSize * (y + 4)));
			squareSize = 43;
		}

		this.normalisedScale = scale / 20;
		mouseTrail = new JPanel();
		mouseTrail.setBackground(clear);
		mouseTrail.setVisible(true);
		mouseTrail.setOpaque(true);
		mouseTrail.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent e) {
				pointEnd = e.getPoint();
			}

			public void mouseDragged(MouseEvent e) {
				pointEnd = e.getPoint();
				repaint();
			}
		});
		layer = new JLayeredPane();
		tempLayerX = layer.getWidth();
		tempLayerY = layer.getHeight();
		fullGrid = new ArrayList<String>();
		allClues = new ArrayList<JLabel>();
		completed = new ArrayList<JLabel>();
		allLetters = new ArrayList<JLabel[][]>();
		randomLetters = new ArrayList<String>();
		tempStrikethrough = new ArrayList<String>();
		solutions = new ArrayList<String>();
		struckThrough = new ArrayList<String>();
		clueText = new ArrayList<String>();
		sorted = new ArrayList<String>();
		orderClues = new JComboBox(ordering);
		orderClues.addActionListener(this);
		orderClues.setFont(font5);
		extra = new JLayeredPane();
		notIn = true;
		counter = 0;
		MAX_SCALE = 17.0;
		MIN_SCALE = 3.5;
		tempLayerX = 40;
		tempLayerY = 40;
		layerX = 40;
		layerY = 40;
		tempWord = "";
		sortMethod = "smallest";
		randomFill = "AAAAAAAAABBCCDDDDEEEEEEEEEEEFFGGGHHIIIIIIIIIJKLLLLMMNNNNNNOOOOOOOOPPQRRRRRRSSSSTTTTTTUUUUVVWWXYYZ";
		temporaryIcons = new ArrayList<Icon[][]>();
		temporaryIcons2 = new ArrayList<Icon[][]>();
		font3 = new Font(currentFont, Font.PLAIN, 18);
		font6 = new Font(currentFont, Font.BOLD, 18);
		font7 = new Font(currentFont, Font.PLAIN, 16);
		font2 = new Font(currentFont, Font.PLAIN, 24);
		font = new Font(currentFont, Font.PLAIN, (int) (normalisedScale * INITIAL_SQUARE_SIZE / 5 * 3));
		Map fontAttr = font3.getAttributes();
		fontAttr.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
		font4 = new Font(fontAttr);
		start = true;
		congratulations = false;
		reset = true;
		rand = new Random();
		for (int i = 0; i < x * y; i++) {
			randomLetters.add(Character.toString(randomFill.charAt(rand.nextInt(randomFill.length()))));
		}
		System.out.print(MouseInfo.getPointerInfo().getLocation() + " | ");
		grey = new Color(200, 200, 200, 255);
		wordLength = 0;
		dir = 0;
		startx = 0;
		starty = 0;
		area = new JScrollPane();
		clear = new Color(255, 255, 255, 255);

		border = BorderFactory.createLineBorder(Color.GREEN);
		layer2 = new JLayeredPane();
		c = new GridBagConstraints();
		d = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		buttonPushed = false;

		solution = new JButton("Solution");
		solution.setFont(font2);
		solution.setEnabled(true);
		solution.addActionListener(this);
		solution.addMouseWheelListener(this);

		hint = new JButton("Hint");
		hint.setFont(font2);
		hint.setEnabled(true);
		hint.addActionListener(this);
		hint.addMouseWheelListener(this);

		clue = new JButton("Reveal");
		clue.setFont(font2);
		clue.setEnabled(true);
		clue.addActionListener(this);
		clue.addMouseWheelListener(this);

		for (int i = 0; i < NUMBER_OF_LAYERS; i++) {
			setUpIcons(temporaryIcons);
			setUpIcons(temporaryIcons2);
			setUpLetters(allLetters);
		}

		layer.setVisible(true);
		
		drawGrid();

		clues = new JPanel(new GridBagLayout());
		int roughClues = (int) (Math.pow(x, 2) / 10) + 8;
		clues.setBounds(0, 40, 18 * x, (roughClues + 1) * 16);
		clues.setBackground(clear);
		clues.setVisible(true);
		clues.setOpaque(false);

		setUpClues();

		main = new JPanel(new GridBagLayout());
		main.setBackground(clear);
		main.setVisible(true);
		main.setOpaque(true);

		c.weightx = 0.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(0, 0, 0, 0);
		main.add(layer, c);

		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 1;
		c.gridy = 0;
		c.ipady = 10;
		main.add(clues, c);

		area = new JScrollPane(main, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		area.getVerticalScrollBar().setUnitIncrement(10);
		area.getHorizontalScrollBar().setUnitIncrement(10);
		area.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		area.setBackground(clear);
		area.setVisible(true);
		area.setOpaque(true);
		area.addMouseWheelListener(this);
		area.setWheelScrollingEnabled(true);

		panel = new JPanel(new GridBagLayout());
		panel.setOpaque(false);
		panel.setBackground(clear);

		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 3;
		panel.add(area, c);

		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 1;
		c.ipady = 10;
		c.gridwidth = 1;
		panel.add(hint, c);

		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridx = 1;
		c.gridy = 1;
		c.ipady = 10;
		panel.add(clue, c);

		frame.setContentPane(panel);
		frame.pack();
		frame.setBackground(clear);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.getRootPane().setDefaultButton(solution);
		frame.setJMenuBar(menuBar);
	}

	public JMenuItem setUpMenus(String itemName, Icon itemIcon, Border itemBorder, JMenu menu){
		JMenuItem  menuItem = new JMenuItem(itemName, itemIcon);
		menuItem.addActionListener(this);
		menuItem.setBorder(itemBorder);
		if(menu != null){
			menu.add(menuItem);
		}
		return menuItem;
	}
	
	private void setUpLetters(ArrayList<JLabel[][]> allLetters) {
		JLabel[][] letters = new JLabel[x - 2][y - 2];
		allLetters.add(letters);
	}

	private void setUpIcons(ArrayList<Icon[][]> allLetters) {
		Icon[][] icon = new Icon[x - 2][y - 2];
		Icon[][] icon2 = new Icon[x - 2][y - 2];
		temporaryIcons.add(icon);
		temporaryIcons2.add(icon2);
	}

	@SuppressWarnings("unchecked")
	private void setUpClues() {
		clues.removeAll();
		clueText.clear();
		sorted.clear();
		allClues.clear();
		for (Entry entry : entries) {
			clueText.add(entry.getWord().toUpperCase());
		}
		clueText = sortedStrings(clueText, sortMethod);
		for (String a : clueText) {
			if (!struckThrough.contains(a)) {
				sorted.add(a);
			}
		}
		struckThrough = sortedStrings(struckThrough, sortMethod);
		for (String a : sorted) {
			JLabel temp = new JLabel(a);
			temp.setForeground(currentColour);
			mouseActionlabel(temp);
			temp.setFont(new Font(currentFont, Font.PLAIN, 18));
			temp.setFont(font3);
			temp.setHorizontalAlignment(SwingConstants.LEFT);
			temp.setVerticalAlignment(SwingConstants.TOP);
			temp.setCursor(new Cursor(Cursor.HAND_CURSOR));
			allClues.add(temp);
		}
		for (String b : struckThrough) {
			JLabel temp2 = new JLabel(b);
			temp2.setForeground(currentColour);
			mouseActionlabel(temp2);
			Font tempFnt = new Font(currentFont, Font.PLAIN, 18);
			@SuppressWarnings("rawtypes")
			Map fontAttr = tempFnt.getAttributes();
			fontAttr.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
			font4 = new Font(fontAttr);
			temp2.setFont(font4);
			temp2.setHorizontalAlignment(SwingConstants.LEFT);
			temp2.setVerticalAlignment(SwingConstants.TOP);
			temp2.setCursor(new Cursor(Cursor.HAND_CURSOR));
			allClues.add(temp2);
		}

		// clues.add(temp4);
		clues.repaint();
		int columnSize = 0;
		if (x * INITIAL_SQUARE_SIZE / 2 > frame.getHeight()) {
			// 26 for my laptop need variable here (tried frame.getHeight()/
			columnSize = 26;
			// columnSize = (int)(frame.getHeight()/16);
		} else {
			columnSize = (int) (x * 1.55);
		}
		// System.out.println("allClues: "+allClues.size());
		// System.out.println("Column size: "+ columnSize);
		int cols = allClues.size() / columnSize + 1;
		for (int k = 0; k < cols; k++) {

			d.gridx = k;
			for (int j = 0; j < columnSize; j++) {
				JLabel temp4 = new JLabel(" ");
				d.weightx = 0.0;
				d.weighty = 0.0;
				d.insets = new Insets(0, 0, 0, 0);
				d.ipady = 15;
				d.gridx = k;
				d.gridy = 0;
				clues.add(temp4, d);

				JLabel temp7 = new JLabel(" ");

				// d.weightx = 0.0;
				d.weighty = 1.0;
				d.ipady = 0;
				d.anchor = GridBagConstraints.ABOVE_BASELINE_LEADING;

				d.gridy = j + 1;
				d.insets = new Insets(0, 10, 0, 10);
				if (allClues.size() > columnSize * k + j) {
					clues.add(allClues.get(columnSize * k + j), d);
				} else {
					clues.add(temp7, d);
				}

			}
			JLabel temp5 = new JLabel(" ");
			d.weightx = 0.0;
			d.weighty = 1000;
			d.gridx = k;
			d.gridy = columnSize + 1;
			// d.insets = new Insets(0,10,0,10);
			clues.add(temp5, d);
		}
		JLabel temp6 = new JLabel(" ");

		d.weightx = 1.0;
		d.weighty = 1.0;
		d.gridx = cols + 1;
		d.gridy = 0;
		clues.add(temp6, d);

		clues.setAlignmentX(SwingConstants.LEFT);
	}

	/**
	 * Class to set grids of labels for each potential layer
	 * 
	 * @param labels
	 * @param layer
	 */
	public JPanel setUpLayers(JLabel[][] labels, JPanel layer, int level) {
		layer = new JPanel(new GridLayout(x - 2, y - 2));
		layer.setBounds(layerX, layerY, squareSize * (x - 2), squareSize * (y - 2));
		border = BorderFactory.createLineBorder(currentColour);
		layer.setBorder(border);
		layer.setBackground(clear);
		layer.setOpaque(false);
		for (int i = 0; i < x - 2; i++) {
			for (int j = 0; j < y - 2; j++) {
				ImageIcon temp, temp2;
				labels[i][j] = new JLabel();
				labels[i][j].setHorizontalTextPosition(SwingConstants.CENTER);
				labels[i][j].setOpaque(false);
				labels[i][j].setFont(font);
				labels[i][j].setForeground(currentColour);
				if (temporaryIcons.get(level)[i][j] != null) {
					temp = (ImageIcon) temporaryIcons.get(level)[i][j];
					Image img = temp.getImage();
					Image newimg = img.getScaledInstance(squareSize, squareSize, java.awt.Image.SCALE_SMOOTH);
					temp = new ImageIcon(newimg);
					labels[i][j].setIcon(temp);
				} else if (temporaryIcons2.get(level)[i][j] != null) {
					temp2 = (ImageIcon) temporaryIcons2.get(level)[i][j];
					Image img = temp2.getImage();
					Image newimg = img.getScaledInstance(squareSize, squareSize, java.awt.Image.SCALE_SMOOTH);
					temp2 = new ImageIcon(newimg);
					labels[i][j].setIcon(temp2);
				}
				if (level == 0) {
					if (grid[j + 1][i + 1] != "_") {
						if (buttonPushed) {
							// labels[i][j].setOpaque(false);
							labels[i][j].setBackground(Color.GREEN);
						}
						labels[i][j].setText(grid[j + 1][i + 1].toUpperCase());
					} else {
						labels[i][j].setText(randomLetters.get(i * x + j));
					}
				}
				labels[i][j].setBounds(squareSize, squareSize, squareSize * (x - 2), squareSize * (y - 2));
				labels[i][j].setHorizontalAlignment(JTextField.CENTER);
				labels[i][j].setVerticalAlignment(JTextField.CENTER);
				// mouseActionlabel(labels[i][j]);
				labels[i][j].addMouseListener(this);
				layer.add(labels[i][j]);
				layer.add(mouseTrail);
			}
		}
		return layer;
	}

	public void struckOffList(int i, int j) {
		tempStrikethrough.clear();
		for (Entry b : entries) {
			if (b.palindromic) {
				if (b.start_x == j + 1 && b.start_y == i + 1) {
					tempWord = b.getWord();
					tempStrikethrough.add(tempWord);
					start = true;
				} else if (b.end_x == j + 1 && b.end_y == i + 1) {
					tempWord = b.getWord();
					tempStrikethrough.add(tempWord);
					start = false;
				}
			}
			if (b.start_x == j + 1 && b.start_y == i + 1) {
				tempWord = b.getWord();
				tempStrikethrough.add(tempWord);
			}
		}
	}

	void mouseActionlabel(JLabel l) {
		l.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				for (int i = 0; i < x - 2; i++) {
					for (int j = 0; j < y - 2; j++) {
						if (e.getSource().equals(allLetters.get(0)[i][j])) {
							for (Entry a : entries) {
								loopAroundWord(i, j, a);
							}
							struckOffList(i, j);
							// tempStrikethrough.clear();
							// for (Entry b : entries) {
							// if (b.palindromic) {
							// if (b.start_x == j + 1 && b.start_y == i + 1) {
							// tempWord = b.getWord();
							// tempStrikethrough.add(tempWord);
							// start = true;
							// } else if (b.end_x == j + 1 && b.end_y == i + 1)
							// {
							// tempWord = b.getWord();
							// tempStrikethrough.add(tempWord);
							// start = false;
							// }
							// }
							// if (b.start_x == j + 1 && b.start_y == i + 1) {
							// tempWord = b.getWord();
							// tempStrikethrough.add(tempWord);
							// }
							// }
						}
					}
				}
				for (int i = 0; i < allClues.size(); i++) {
					allClues.get(i).setOpaque(false);
					allClues.get(i).setForeground(currentColour);
					if (e.getSource().equals(allClues.get(i))) {
						allClues.get(i).setOpaque(true);
						allClues.get(i).setForeground(Color.BLUE);
						// set this word to this for showing reveal
						thisWord = allClues.get(i).getText();
					}
				}

			}

			public void mouseEntered(MouseEvent e) {
				for (JLabel lab : allClues) {
					if (e.getSource() == lab && notIn && reset) {
						// clues.setBackground(new Color(250, 250, 250, 255));
						orderClues.setVisible(true);
						notIn = false;
					}
				}
				for (int i = 0; i < x - 2; i++) {
					for (int j = 0; j < y - 2; j++) {
						if (e.getSource() == allLetters.get(0)[i][j]) {
							orderClues.setVisible(false);
							reset = true;
						}
					}
				}
			}

			public void mouseExited(MouseEvent e) {
				for (JLabel lab : allClues) {
					if (e.getSource() == lab) {
						clues.setBackground(clear);
						notIn = true;
					}
				}
			}

			public void mousePressed(MouseEvent e) {

			}

			public void mouseReleased(MouseEvent e) {

			}
		});
	}

	/**
	 * Method for looping around clues when you click on them
	 * 
	 * @param i
	 * @param j
	 * @param a
	 */
	public void loopAroundWord(int i, int j, Entry a) {
		x_pos = a.end_x;
		y_pos = a.end_y;
		if (a.palindromic && !start) {
			x_pos = a.start_x;
			y_pos = a.start_y;
		}
		if (x_pos == j + 1 && y_pos == i + 1) {
			if (tempStrikethrough.contains(a.getWord())) {
				for (JLabel temp : allClues) {
					if (temp.getText().equals(a.getWord().toUpperCase()) && !(struckThrough.contains(temp.getText()))) {
						if (!solutions.contains(temp.getText())) {
							solutions.add(temp.getText());
							counter++;
							temp.setFont(font4);
							sorted.remove(temp.getText());
							struckThrough.add(temp.getText());
						}
						String[] images = setImageDirections(a.direction);
						Icon[] icons = new Icon[9];
						Icon[] icons2 = new Icon[8];
						setImages = new SetUpImages(images, squareSize, squareSize, icons, 0);
						tempImage = new SetUpImages(loopDirections, squareSize, squareSize, icons2, 0);
						if (a.direction.equals("snaking")) {
							// System.out.println("Looping snaking style!");
							ArrayList<Coord> letterCoords = a.getLetterCoords();
							// set up snake head image
							int x = letterCoords.get(0).xcoord;
							// System.out.println("xcoord in snake: "+ x);
							int y = letterCoords.get(0).ycoord;
							int nextX = letterCoords.get(1).xcoord;
							int nextY = letterCoords.get(1).ycoord;
							int penX = letterCoords.get(a.getWordLength() - 2).xcoord;
							int penY = letterCoords.get(a.getWordLength() - 2).ycoord;
							int lastX = letterCoords.get(a.getWordLength() - 1).xcoord;
							int lastY = letterCoords.get(a.getWordLength() - 1).ycoord;
							setSnakeHead(x, y, nextX, nextY, icons2, temporaryIcons);
							// set middle images

							for (int c = 1; c < a.getWordLength() - 1; c++) {
								setSnakingImages(letterCoords.get(c - 1).xcoord, letterCoords.get(c - 1).ycoord,
										letterCoords.get(c).xcoord, letterCoords.get(c).ycoord,
										letterCoords.get(c + 1).xcoord, letterCoords.get(c + 1).ycoord, icons,
										temporaryIcons);
							}
							// set up snake tail image
							setSnakeHead(lastX, lastY, penX, penY, icons2, temporaryIcons);

						} else {
							setDiagonalImages(a.end_y - 1, a.end_x - 1, 2, icons, temporaryIcons);
							setDiagonalImages(a.start_y - 1, a.start_x - 1, 0, icons, temporaryIcons);
							int[] t = setIncrements(a.direction);
							for (int c = 0; c < a.getWordLength() - 1; c++) {
								if (!(c == 0)) {
									setDiagonalImages(a.start_y - 1 + c * t[1], a.start_x - 1 + c * t[0], 1, icons,
											temporaryIcons);
								}
								if (a.isDiagonal) {
									if (a.direction.equals("BLTRdiagonal")) {
										setDiagonalImages(a.start_y - 2 + c * t[1], a.start_x - 1 + c * t[0], 3, icons,
												temporaryIcons);
										setDiagonalImages(a.start_y - 1 + c * t[1], a.start_x + c * t[0], 4, icons,
												temporaryIcons);
									} else if (a.direction.equals("backwardsBLTRdiagonal")) {
										setDiagonalImages(a.start_y + c * t[1], a.start_x - 2 + (c - 1) * t[0], 3,
												icons, temporaryIcons);
										setDiagonalImages(a.start_y - 1 + c * t[1], a.start_x - 2 + c * t[0], 4, icons,
												temporaryIcons);
									} else if (a.direction.equals("diagonal")) {
										setDiagonalImages(a.start_y - 1 + c * t[1], a.start_x - 1 + (c + 1) * t[0], 3,
												icons, temporaryIcons);
										setDiagonalImages(a.start_y + c * t[1], a.start_x - 1 + c * t[0], 4, icons,
												temporaryIcons);
									} else if (a.direction.equals("backwardsdiagonal")) {
										setDiagonalImages(a.start_y - 1 + c * t[1], a.start_x - 2 + c * t[0], 3, icons,
												temporaryIcons);
										setDiagonalImages(a.start_y - 2 + c * t[1], a.start_x - 1 + c * t[0], 4, icons,
												temporaryIcons);
									}
								}
							}
						}
						tempStrikethrough.clear();
					}
				}
				setUpClues();
			}
		}
		if (counter == entries.size() && !congratulations) {
			JOptionPane.showMessageDialog(frame, "Congratulations!");
			SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {

				/**
				 * To implement threads Allow GUI to run without pausing while
				 * congratulations comes up later
				 * 
				 * @Override(non-Javadoc) @see javax.swing. SwingWorker#
				 *                        doInBackground ()
				 */
				protected Void doInBackground() throws Exception {
					this.publish("Everything");
					Thread.sleep(3000);
					return null;
				}

				@SuppressWarnings("unused")
				protected void process(ArrayList<String> res) {
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					JOptionPane.showMessageDialog(frame, "Congratulations!");
				}
			};
			worker.execute();
			congratulations = true;
		}
	}

	/**
	 * Method for looping around individual words
	 * 
	 * @param a
	 */
	public void loopAroundWord2(Entry a) {
		x_pos = a.end_x;
		y_pos = a.end_y;
		String[] images = setImageDirections(a.direction);
		Icon[] icons = new Icon[9];
		Icon[] icons2 = new Icon[8];
		setImages = new SetUpImages(images, squareSize, squareSize, icons, 0);
		tempImage = new SetUpImages(loopDirections, squareSize, squareSize, icons2, 0);
		if (a.direction.equals("snaking")) {
			ArrayList<Coord> letterCoords = a.getLetterCoords();
			int x = letterCoords.get(0).xcoord;
			int y = letterCoords.get(0).ycoord;
			int nextX = letterCoords.get(1).xcoord;
			int nextY = letterCoords.get(1).ycoord;
			int penX = letterCoords.get(a.getWordLength() - 2).xcoord;
			int penY = letterCoords.get(a.getWordLength() - 2).ycoord;
			int lastX = letterCoords.get(a.getWordLength() - 1).xcoord;
			int lastY = letterCoords.get(a.getWordLength() - 1).ycoord;
			setSnakeHead(x, y, nextX, nextY, icons2, temporaryIcons2);
			for (int c = 1; c < a.getWordLength() - 1; c++) {
				setSnakingImages(letterCoords.get(c - 1).xcoord, letterCoords.get(c - 1).ycoord,
						letterCoords.get(c).xcoord, letterCoords.get(c).ycoord, letterCoords.get(c + 1).xcoord,
						letterCoords.get(c + 1).ycoord, icons, temporaryIcons2);
			}
			setSnakeHead(lastX, lastY, penX, penY, icons2, temporaryIcons2);
		} else {
			setDiagonalImages(a.end_y - 1, a.end_x - 1, 2, icons, temporaryIcons2);
			setDiagonalImages(a.start_y - 1, a.start_x - 1, 0, icons, temporaryIcons2);
			int[] t = setIncrements(a.direction);
			for (int c = 0; c < a.getWordLength() - 1; c++) {
				if (!(c == 0)) {
					setDiagonalImages(a.start_y - 1 + c * t[1], a.start_x - 1 + c * t[0], 1, icons, temporaryIcons2);
				}
				if (a.isDiagonal) {
					if (a.direction.equals("BLTRdiagonal")) {
						setDiagonalImages(a.start_y - 2 + c * t[1], a.start_x - 1 + c * t[0], 3, icons,
								temporaryIcons2);
						setDiagonalImages(a.start_y - 1 + c * t[1], a.start_x + c * t[0], 4, icons, temporaryIcons2);
					} else if (a.direction.equals("backwardsBLTRdiagonal")) {
						setDiagonalImages(a.start_y + c * t[1], a.start_x - 2 + (c - 1) * t[0], 3, icons,
								temporaryIcons2);
						setDiagonalImages(a.start_y - 1 + c * t[1], a.start_x - 2 + c * t[0], 4, icons,
								temporaryIcons2);
					} else if (a.direction.equals("diagonal")) {
						setDiagonalImages(a.start_y - 1 + c * t[1], a.start_x - 1 + (c + 1) * t[0], 3, icons,
								temporaryIcons2);
						setDiagonalImages(a.start_y + c * t[1], a.start_x - 1 + c * t[0], 4, icons, temporaryIcons2);
					} else if (a.direction.equals("backwardsdiagonal")) {
						setDiagonalImages(a.start_y - 1 + c * t[1], a.start_x - 2 + c * t[0], 3, icons,
								temporaryIcons2);
						setDiagonalImages(a.start_y - 2 + c * t[1], a.start_x - 1 + c * t[0], 4, icons,
								temporaryIcons2);
					}
				}
			}
		}
	}

	private int[] setIncrements(String direction) {
		int[] inc = new int[2];
		if (direction.equals("across")) {
			inc[0] = 1;
			inc[1] = 0;
		} else if (direction.equals("backwards")) {
			inc[0] = -1;
			inc[1] = 0;
		} else if (direction.equals("down")) {
			inc[0] = 0;
			inc[1] = 1;
		} else if (direction.equals("up")) {
			inc[0] = 0;
			inc[1] = -1;
		} else if (direction.equals("diagonal")) {
			inc[0] = 1;
			inc[1] = 1;
		} else if (direction.equals("backwardsdiagonal")) {
			inc[0] = -1;
			inc[1] = -1;
		} else if (direction.equals("BLTRdiagonal")) {
			inc[0] = 1;
			inc[1] = -1;
		} else if (direction.equals("backwardsBLTRdiagonal")) {
			inc[0] = -1;
			inc[1] = 1;
		}
		return inc;
	}

	public void setSnakingImages(int prevY, int prevX, int y, int x, int nextY, int nextX, Icon[] icons,
			ArrayList<Icon[][]> iconLayers) {
		System.out.println("Starting snake body");
		System.out.println("icons: " + icons.toString() + " length: " + icons.length);
		int image = 0;
		for (JLabel[][] lab : allLetters) {
			if (!buttonPushed) {
				lab[x - 1][y - 1].setOpaque(false);
			}
			if (iconLayers.get(allLetters.indexOf(lab))[x - 1][y - 1] == null
					&& lab[x - 1][y - 1].getText().equals("")) {
				if (prevY == y && nextY == y) {
					image = 4;
				} else if (prevX == x && nextX == x) {
					image = 3;
				} else if ((prevX == x + 1 && nextX == x && prevY == y && nextY == y - 1)
						|| (prevX == x && nextX == x + 1 && prevY == y - 1 && nextY == y)) {
					// "TopRightJoin"; //done
					System.out.println("Got toprightjoin");
					image = 8;
				} else if ((prevX == x + 1 && nextX == x && prevY == y && nextY == y + 1)
						|| (prevX == x && nextX == x + 1 && prevY == y + 1 && nextY == y)) {
					// "BottomRightJoin"; //done
					image = 6;
				} else if ((prevX == x - 1 && nextX == x && prevY == y && nextY == y - 1)
						|| (prevX == x && nextX == x - 1 && prevY == y - 1 && nextY == y)) {
					// "TopLeftJoin"; //done
					image = 7;
				} else if ((prevX == x - 1 && nextX == x && prevY == y && nextY == y + 1)
						|| (prevX == x && nextX == x - 1 && prevY == y + 1 && nextY == y)) {
					// "BottomLeftJoin";
					image = 5;
				}
				System.out.println("image:" + image);
				lab[x - 1][y - 1].setIcon(icons[image]);
				iconLayers.get(allLetters.indexOf(lab))[x - 1][y - 1] = icons[image];
				lab[x - 1][y - 1].setText(" ");
				break;
			}
		}
	}

	public void setSnakeHead(int y, int x, int nextY, int nextX, Icon[] icons2, ArrayList<Icon[][]> iconLayers) {
		int image = 0;
		System.out.println("Starting snake head");
		// String image = "";
		for (JLabel[][] lab : allLetters) {
			if (!buttonPushed) {
				lab[x - 1][y - 1].setOpaque(false);
			}
			if (iconLayers.get(allLetters.indexOf(lab))[x - 1][y - 1] == null
					&& lab[x - 1][y - 1].getText().equals("")) {
				if (x < nextX && y < nextY) {
					image = 0;
				} else if (x < nextX && y == nextY) {
					image = 3;
				} else if (x < nextX && y > nextY) {
					image = 5;
				} else if (x == nextX && y < nextY) {
					image = 1;
				} else if (x == nextX && y > nextY) {
					image = 6;
				} else if (x > nextX && y < nextY) {
					image = 2;
				} else if (x > nextX && y == nextY) {
					image = 4;
				} else if (x > nextX && y > nextY) {
					image = 7;
				}
				System.out.println("Image: " + image);
				// Icon icon = null;
				// setImages = new SetUpImages(image, squareSize, squareSize,
				// icon);
				lab[x - 1][y - 1].setIcon(icons2[image]);
				iconLayers.get(allLetters.indexOf(lab))[x - 1][y - 1] = icons2[image];
				lab[x - 1][y - 1].setText(" ");
				break;
			}
		}
	}

	public void setDiagonalImages(int x, int y, int image, Icon[] icons, ArrayList<Icon[][]> iconLayers) {
		for (JLabel[][] lab : allLetters) {
			if (!buttonPushed) {
				// lab[x][y].setOpaque(false);
			}
			if (iconLayers.get(allLetters.indexOf(lab))[x][y] == null && lab[x][y].getText().equals("")) {
				lab[x][y].setIcon(icons[image]);
				iconLayers.get(allLetters.indexOf(lab))[x][y] = icons[image];
				lab[x][y].setText(" ");
				break;
			}
		}
	}

	/**
	 * 
	 */
	public void drawGrid() {
		layer.removeAll();
		layer.setOpaque(false);
		layer.setBackground(Color.RED);
		layer.setBounds(0, 0, squareSize * (x), squareSize * (y));
		tempLayerX = layerX;
		tempLayerY = layerY;
		layer.setPreferredSize(new Dimension(squareSize * (x - 1) + 40, squareSize * (y)));
		for (int i = 0; i < NUMBER_OF_LAYERS; i++) {
			JPanel transparentLayer = new JPanel(new GridLayout(x - 2, y - 2));
			transparentLayer = setUpLayers(allLetters.get(i), transparentLayer, i);
			layer.add(transparentLayer, new Integer(0));
		}
	}

	/**
	 * Method to Alphabetise arraylist of strings
	 * 
	 * @param strings
	 * @return
	 */
	public ArrayList<String> sortedStrings(ArrayList<String> strings, String method) {
		if (method.equals("alphabetical")) {
			ArrayList<String> list = strings;
			Collections.sort(list);
			strings = (ArrayList<String>) list;
		} else if (method.equals("smallest")) {
			strings = sortBySize(strings, true);
		} else if (method.equals("random")) {
			ArrayList<String> list = strings;
			Collections.shuffle(list);
			strings = (ArrayList<String>) list;
		} else if (method.equals("biggest")) {
			strings = sortBySize(strings, false);
			ArrayList<String> list = strings;
			Collections.reverse(list);
			strings = (ArrayList<String>) list;
		}
		return strings;
	}

	/**
	 * Sort arraylist of strings by size and alphabetically or reverse of both.
	 * Involves sorting list, then running through and putting any smaller words
	 * in front of larger ones. Knowing they are ordered means only need to
	 * check one is smaller or equal than the other. The boolean is to determine
	 * if it's ordering each size group in alphabetical or reverse alphabetical
	 * order. (The latter list can then be reverse sorted to give the
	 * alphabetical by size largest first ordering).
	 * 
	 * @param strings
	 * @param filter
	 * @return
	 */
	private ArrayList<String> sortBySize(ArrayList<String> strings, boolean filter) {
		ArrayList<String> list = strings;
		Collections.sort(list);
		strings = (ArrayList<String>) list;
		ArrayList<String> temp = new ArrayList<String>();
		for (String a : strings) {
			if (temp.isEmpty()) {
				temp.add(a);
			} else if (temp.get(temp.size() - 1).length() <= a.length() && filter) {
				temp.add(temp.size(), a);
			} else if (temp.get(temp.size() - 1).length() < a.length()) {
				temp.add(temp.size(), a);
			} else {
				for (int b = temp.size() - 1; b >= 0; b--) {
					if (b == 0) {
						if (temp.get(b).length() <= a.length() && filter) {
							temp.add(1, a);
							break;
						} else if (temp.get(b).length() < a.length()) {
							temp.add(1, a);
							break;
						} else {
							temp.add(0, a);
							break;
						}
					} else if (temp.get(b).length() <= a.length() && filter) {
						temp.add(b + 1, a);
						break;
					} else if (temp.get(b).length() < a.length()) {
						temp.add(b + 1, a);
						break;
					}
				}
			}
		}
		return temp;
	}

	/**
	 * Get names of images for relevant direction of word
	 * 
	 * @param direction
	 * @param diagonal
	 * @return
	 */
	public String[] setImageDirections(String direction) {
		String middle = "Horizontal";
		String start = "Horizontal";
		String end = "Horizontal";
		String corner1 = "Horizontal";
		String corner2 = "Vertical";
		String snakeTR = "TopRightJoin";
		String snakeBR = "BottomRightJoin";
		String snakeTL = "TopLeftJoin";
		String snakeBL = "BottomLeftJoin";
		if (direction.equals("across")) {
			start = "Left";
			middle = "Horizontal";
			end = "Right";
		} else if (direction.equals("down")) {
			start = "Top";
			middle = "Vertical";
			end = "Bottom";
		} else if (direction.equals("diagonal")) {
			start = "TopLeft";
			middle = "DiagonalDownRight";
			end = "BottomRight";
			corner1 = "BottomLeftCorner";
			corner2 = "TopRightCorner";
		} else if (direction.equals("backwards")) {
			start = "Right";
			middle = "Horizontal";
			end = "Left";
		} else if (direction.equals("up")) {
			start = "Bottom";
			middle = "Vertical";
			end = "Top";
		} else if (direction.equals("backwardsdiagonal")) {
			start = "BottomRight";
			middle = "DiagonalDownRight";
			end = "TopLeft";
			corner1 = "TopRightCorner";
			corner2 = "BottomLeftCorner";
		} else if (direction.equals("BLTRdiagonal")) {
			start = "BottomLeft";
			middle = "DiagonalUpRight";
			end = "TopRight";
			corner1 = "BottomRightCorner";
			corner2 = "TopLeftCorner";
		} else if (direction.equals("backwardsBLTRdiagonal")) {
			start = "TopRight";
			middle = "DiagonalUpRight";
			end = "BottomLeft";
			corner1 = "TopLeftCorner";
			corner2 = "BottomRightCorner";
		}

		String[] images = { start, middle, end, corner1, corner2, snakeTR, snakeBR, snakeTL, snakeBL };
		for (int i = 0; i < images.length; i++) {
			System.out.println("images array: " + images[i].toString());
		}
		return images;
	}

	public void actionPerformed(ActionEvent e) {

		// ***************TECHNICAL NOTE******************************
		// change the next line below to "== hint" to add in solutions
		// ***********************************************************
		if (e.getSource() == solution) {
			diagonal = false;
			buttonPushed = !buttonPushed;
			if (buttonPushed) {
				solution.setText("Hide Solution");
				System.out.print("\nEntries: ");
				for (Entry a : entries) {
					loopAroundWord2(a);
				}
				for (Entry a : entries) {
					System.out.print(a.getWord().toString() + ", ");
				}
				System.out.println("Entries: " + entries.size());
			} else {
				solution.setText("Show Solution");
				for (int i = 0; i < x - 2; i++) {
					for (int j = 0; j < y - 2; j++) {
						for (JLabel[][] lab : allLetters) {
							temporaryIcons2.get(allLetters.indexOf(lab))[i][j] = null;
							lab[i][j].setIcon(null);
						}
						allLetters.get(0)[j][i].setBackground(clear);
					}
				}
				resetSizes();
				for (int i = 0; i < x - 2; i++) {
					for (int j = 0; j < y - 2; j++) {
						for (JLabel[][] labs : allLetters) {
							if (temporaryIcons.get(allLetters.indexOf(labs))[i][j] != null) {
								for (JLabel[][] labs2 : allLetters) {
									labs2[i][j].setOpaque(false);
								}
								ImageIcon temp = (ImageIcon) temporaryIcons.get(allLetters.indexOf(labs))[i][j];
								Image img = temp.getImage();
								Image newimg = img.getScaledInstance(squareSize, squareSize,
										java.awt.Image.SCALE_SMOOTH);
								temp = new ImageIcon(newimg);
								labs[i][j].setIcon(temp);
							}
						}
					}
				}
			}
		}
		if (e.getSource() == orderClues) {
			@SuppressWarnings("rawtypes")
			JComboBox cb = (JComboBox) e.getSource();
			String msg = (String) cb.getSelectedItem();
			notIn = false;
			reset = false;
			if (msg.equals("ALPHABETICAL")) {
				orderClues.setVisible(false);
				sortMethod = "alphabetical";
				setUpClues();
			} else if (msg.equals("BIGGEST")) {
				orderClues.setVisible(false);
				sortMethod = "biggest";
				setUpClues();
			} else if (msg.equals("SMALLEST")) {
				orderClues.setVisible(false);
				sortMethod = "smallest";
				setUpClues();
			} else if (msg.equals("RANDOM")) {
				orderClues.setVisible(false);
				sortMethod = "random";
				setUpClues();
			}
		}

		if (e.getSource() == hint) {
			int hintClue = rand.nextInt(sorted.size());
			for (final Entry ent : entries) {
				for (int i = 0; i < allClues.size(); i++) {
					if (ent.getWord().toUpperCase().equals(thisWord) && !struckThrough.contains(ent.getWord().toUpperCase()) && allClues.get(i).getText().equals(thisWord)) {
						hintClue = i;
						System.out.println("Entered here"); 
						//To stop hinting the same word all the time
						//thisWord = "";
					}
				}
			}
			System.out.println("this word: " + thisWord);
			showHint(hintClue);
		}
		
		if (e.getSource() == clue) {
			showClue();
		}

		if (e.getSource() == exit) {
			frame.dispose();
		}

		if (e.getSource() == newGame) {
			System.out.println("New Game");
			try {
				frame.dispose();
				wordsearch = new WordSearchGenerator((Integer) spinner.getValue(), difficulty, dictionary, languageFrom, languageTo, currentTopic);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		if (e.getSource() == thick) {
			System.out.println("Thick mode enabled");
			difficulty = 2;
		}

		if (e.getSource() == normal) {
			System.out.println("Normal mode enabled");
			difficulty = 4;
		}

		if (e.getSource() == smart) {
			System.out.println("Smart mode enabled");
			difficulty = 8;
		}

		if (e.getSource() == genius) {
			System.out.println("Genius mode enabled");
			difficulty = 16;
		}

		for (int i = 0; i < countries.length; i++) {
			if (e.getSource() == country1[i]) {
				for (int j = 0; j < country1.length; j++) {
					country1[j].setBorder(null);
					country1[j].setOpaque(false);
				}
				languageFrom = countries[i];
				System.out.println("from: " + languageFrom);
				country1[i].setOpaque(true);
				country1[i].setBorder(border2);
			}
		}

		for (int i = 0; i < topicWords.length; i++) {
			if (e.getSource() == topics[i]) {
				for (int j = 0; j < topicWords.length; j++) {
					topics[j].setBorder(null);
					topics[j].setOpaque(false);
				}
				currentTopic = topicWords[i];
				for (int j = 0; j < countries.length; j++) {
					if (countries[j].equals(languageTo)) {
						dictionary = dictionaries[j] + topicWords[i].toLowerCase() + ".txt";
						System.out.println("Dict: " + dictionary);
					}
				}
				topics[i].setOpaque(true);
				topics[i].setBorder(border2);
			}
		}

		for (int i = 0; i < countries.length; i++) {
			if (e.getSource() == country2[i]) {
				for (int j = 0; j < country2.length; j++) {
					country2[j].setBorder(null);
					country2[j].setOpaque(false);
				}
				languageTo = countries[i];
				System.out.println("to: " + languageTo);
				country2[i].setOpaque(true);
				country2[i].setBorder(border2);
			}
		}
	}

	private void showClue() {
		if (!sorted.isEmpty()) {
			for (final Entry ent : entries) {
				if (ent.getWord().toUpperCase().equals(thisWord)) {
					if (!struckThrough.contains(ent.getWord().toUpperCase())) {
						struckThrough.add(ent.getWord().toUpperCase());
						loopAroundWord2(ent);
					}
					resetSizes();
					setUpClues();
				}
			}
		}
	}

	private void showHint(int clue) {
		final int hintClue = clue;
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			protected Void doInBackground() throws Exception {
				allClues.get(hintClue).setOpaque(true);
				allClues.get(hintClue).setBackground(Color.GREEN);
				long current = System.currentTimeMillis();
				long counter = 0;
				hint.setEnabled(false);
				boolean started = false;
				for (Entry ent : entries) {
					ArrayList<Coord> letterCoords = ent.getLetterCoords();
					if (ent.getWord().toUpperCase().equals(allClues.get(hintClue).getText()) && !started) {
						int letterInWord = rand.nextInt(ent.getWordLength());
						int currentLetterX = letterCoords.get(letterInWord).getX();
						int currentLetterY = letterCoords.get(letterInWord).getY();
						started = true;
						while (counter < 800) {
							ImageIcon icon = new ImageIcon("CentreGreen.png");
							Image img = ResourceLoader.getImage("CentreGreen.png");
							Image newimg = img.getScaledInstance((int)(74*normalisedScale), (int)(74*normalisedScale), java.awt.Image.SCALE_SMOOTH ) ; 
							icon = new ImageIcon(newimg);
							allLetters.get(0)[currentLetterY - 1][currentLetterX - 1].setIcon(icon);
							Thread.sleep(200);
							allLetters.get(0)[currentLetterY - 1][currentLetterX - 1].setIcon(null);
							Thread.sleep(200);
							counter = System.currentTimeMillis() - current;
						}
						allLetters.get(0)[currentLetterY - 1][currentLetterX - 1].setBackground(clear);
						allLetters.get(0)[currentLetterY - 1][currentLetterX - 1].setOpaque(false);
					}
				}
				allClues.get(hintClue).setOpaque(false);
				allClues.get(hintClue).setBackground(currentColour);
				hint.setEnabled(true);
				return null;
			}
		};
		worker.execute();
		allClues.get(hintClue).setForeground(currentColour);
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		if (e.isControlDown()) {
			area.setWheelScrollingEnabled(false);
			if (e.getWheelRotation() < 0) {
				if (scale < MAX_SCALE) {
					scale++;
				}
			} else {
				System.out.println("scrolled down");
				if (scale > MIN_SCALE) {
					scale--;
				}
			}
			resetSizes();
		} else {
			area.setWheelScrollingEnabled(true);
		}
	}

	public void resetSizes() {
		normalisedScale = scale / 20;
		squareSize = (int) (normalisedScale * INITIAL_SQUARE_SIZE + 3);
		font = new Font(currentFont, Font.PLAIN, (int) (normalisedScale * INITIAL_SQUARE_SIZE / 5 * 3));
		main.revalidate();
		drawGrid();
	}
	
	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
		if (mouseHeld) {
			for (int i = 0; i < x - 2; i++) {
				for (int j = 0; j < y - 2; j++) {
					if (e.getSource() == allLetters.get(0)[i][j]) {
						trail.add(allLetters.get(0)[i][j].getText().charAt(0));
					}
				}
			}
		} else {
			trail.clear();
		}
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {

		pointStart = e.getPoint();
		if (!mouseHeld) {
			for (int i = 0; i < x - 2; i++) {
				for (int j = 0; j < y - 2; j++) {
					if (e.getSource() == allLetters.get(0)[i][j]) {
						startWordX = i;
						startWordY = j;
						trail.clear();
						mouseHeld = true;
						trail.add(allLetters.get(0)[i][j].getText().charAt(0));
						System.out.println("trail: " + trail);
					}
				}
			}
		}
	}

	public void mouseReleased(MouseEvent e) {
		if (mouseHeld) {
			for (int i = 0; i < x - 2; i++) {
				for (int j = 0; j < y - 2; j++) {
					if (e.getSource() == allLetters.get(0)[i][j]) {
						mouseHeld = false;
						StringBuilder str = new StringBuilder();
						for (Character a : trail) {
							str.append(a);
						}
						String temp = str.toString(); // SC: this is what we
														// have clicked and
														// dragged
						System.out.println("temp: " + temp.toLowerCase());
						// dynamicEntry.setWord(dynamicEntry.getWord()+allLetters.get(0)[i][j].getText());
						// int start_x, int start_y, boolean across, String
						// word, String definition)
						for (Entry ent : entries) {
							// System.out.println("ent: "+ent.getWord());
							if (ent.getWord().equals(temp.toLowerCase())) {
								if (!struckThrough.contains(temp)) {
									struckThrough.add(temp);
									resetSizes();
									setUpClues();
									loopAroundWord2(ent);
									// System.out.println("EQUAL");
								}
							} else if (ent.isDiagonal) {

								// STEVE: Diagonal words will not likely contain
								// correct letters since it's impossible to
								// exactly move diagonally. Here, we need a
								// second method to catch these potential
								// diagonals
								// This should be enough:
								// 1. Look at first and last letters
								// 2. Make sure the "temp" string at least
								// contains all the correct letters
								// 3. Look at x,y coords of these 

								// STEVE: JUST MAKE EVERYTHING LOWER CASE TO
								// BEGIN WITH
								char first_lett = temp.toLowerCase().charAt(0);
								char last_lett = temp.toLowerCase().charAt(temp.length() - 1);
								String entry2 = ent.getWord().toLowerCase();

								if (entry2.charAt(0) == first_lett && entry2.charAt(entry2.length() - 1) == last_lett) {
									boolean containsAllLetters = true;
									// check temp contains all letters in word
									for (int c = 0; c < entry2.length(); c++) {
										if (!temp.toLowerCase().contains("" + entry2.charAt(c))) {
											containsAllLetters = false;
										}
									}
									if (containsAllLetters && !struckThrough.contains(entry2.toUpperCase())) {
										struckThrough.add(entry2.toUpperCase());
										resetSizes();
										setUpClues();
										loopAroundWord2(ent);
									}
								}
							}
						}
						trail.clear();
					}
				}
			}
			trail.clear();
		} else {
			trail.clear();
		}
	}
}