package wordsearch;

import java.awt.Color;
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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.Border;

import crossword.Entry;
import resources.SetUpImages;

/**
 * Class to draw a word search
 */
public class DrawWordSearch extends JComponent implements ActionListener, MouseWheelListener {
	SetUpImages setImages;
	JFrame frame;
	JLayeredPane layer, layer2, extra;
	JPanel panel, main, clues;
	@SuppressWarnings({ "rawtypes" })
	JComboBox orderClues;
	JButton reveal;
	JScrollPane area;
	GridBagConstraints c;
	ArrayList<String> fullGrid, tempStrikethrough, struckThrough, solutions, clueText, sorted, cluesAcross, cluesDown,
			randomLetters;
	ArrayList<Entry> entries;
	ArrayList<JLabel> allClues, completed;
	ArrayList<JLabel[][]> allLetters;
	ArrayList<Icon[][]> temporaryIcons;
	Font font, font2, font3, font4, font5;
	Random rand;
	Color grey, clear;
	Dimension screenSize;
	Border border;
	String[][] grid;
	String[] ordering = { "RANDOM", "ALPHABETICAL", "BIGGEST", "SMALLEST" },
			loopDirections = { "top", "topRight", "right", "bottomRight", "bottom", "bottomLeft", "left", "topLeft" };
	String tempWord, sortMethod, randomFill;
	private static final long serialVersionUID = 1L;
	double width, height, mouseX, mouseY, scale, normalisedScale, tempLayerWidth, tempLayerHeight, tempScale;
	final double MAX_SCALE, MIN_SCALE;
	int x, y, x_pos, y_pos, counter, wordLength, dir, startx, starty, squareSize, tempLayerX, tempLayerY, layerX, layerY;
	final static int INITIAL_SQUARE_SIZE = 80, NUMBER_OF_LAYERS = 8;
	boolean buttonPushed, clicked, start, congratulations, reset, diagonal, notIn;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public DrawWordSearch(String[][] grid, int x, int y, ArrayList<String> cluesAcross, ArrayList<String> cluesDown,
			ArrayList<Entry> entries) throws IOException {
		System.out.println("Started again");
		this.x = x;
		this.y = y;
		this.grid = grid;
		this.entries = entries;
		this.cluesAcross = cluesAcross;
		this.cluesDown = cluesDown;
		scale = 10.0;
		this.normalisedScale = scale / 20;
		squareSize = 40;
		layer = new JLayeredPane();
		layer.setSize((x-2)*squareSize, (x-2)*squareSize);
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
		sortMethod = "random";
		randomFill = "AAAAAAAAABBCCDDDDEEEEEEEEEEEFFGGGHHIIIIIIIIIJKLLLLMMNNNNNNOOOOOOOOPPQRRRRRRSSSSTTTTTTUUUUVVWWXYYZ";
		temporaryIcons = new ArrayList<Icon[][]>();
		int test = (int) (3 * INITIAL_SQUARE_SIZE * normalisedScale / 5);
		System.out.println("Test: " + test);
		font3 = new Font("Century Gothic", Font.PLAIN, 18);
		font2 = new Font("Century Gothic", Font.PLAIN, 24);
		font = new Font("Century Gothic", Font.PLAIN, (int) (normalisedScale * INITIAL_SQUARE_SIZE / 5 * 3));
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
		Point mouseCoord = MouseInfo.getPointerInfo().getLocation();
		double mouseX = mouseCoord.getX();
		double mouseY = mouseCoord.getY();
		System.out.println("mouseX: " + mouseX + " mouseY: " + mouseY);

		frame = new JFrame("Auto Word Search");
		frame.setBackground(new Color(255, 255, 255, 255));
		frame.setMinimumSize(new Dimension(550, 400));

		grey = new Color(200, 200, 200, 255);
		wordLength = 0;
		dir = 0;
		startx = 0;
		starty = 0;
		area = new JScrollPane();
		clear = new Color(255, 255, 255, 255);

		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		width = screenSize.getWidth();
		height = screenSize.getHeight();

		border = BorderFactory.createLineBorder(Color.BLACK);
		//layer = new JLayeredPane();
		layer2 = new JLayeredPane();
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		buttonPushed = false;

		reveal = new JButton("Show Solution");
		reveal.setFont(font2);
		reveal.setEnabled(true);
		reveal.addActionListener(this);
		reveal.addMouseWheelListener(this);

		for (int i = 0; i < NUMBER_OF_LAYERS; i++) {
			setUpIcons(temporaryIcons);
			setUpLetters(allLetters);
		}
		
		layer.setVisible(true);
		layer.setOpaque(true);
		layer.addMouseWheelListener(this);

		drawGrid(normalisedScale);
		
		clues = new JPanel(new GridLayout(cluesAcross.size() + cluesDown.size(), 1));
		clues.setBounds(0, 40, 18 * x, font3.getSize()*(cluesAcross.size()+cluesDown.size()));
		clues.setBackground(clear);
		clues.setVisible(true);
		clues.setOpaque(true);

		setUpClues(normalisedScale);

		extra.setBackground(clear);
		extra.setVisible(true);
		extra.setOpaque(true);
		extra.setBounds(squareSize * (x-1)+40, 0, squareSize * (x - 2), font3.getSize()*allClues.size());
		extra.addMouseWheelListener(this);

		orderClues.setBounds(0, 0, 18 * x, 30);
		orderClues.setBorder(border);
		orderClues.setBackground(clear);
		orderClues.setOpaque(false);
		orderClues.setVisible(false);

		c.weightx = 0.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 0;
		extra.add(orderClues, c);

		c.weightx = 0.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(0, 0, 0, 0);
		extra.add(clues, c);

		main = new JPanel(new GridBagLayout());
		main.setBackground(clear);
		main.setVisible(true);
		main.setOpaque(false);

		c.weightx = 0.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		//c.insets = new Insets(0, 0, 0, 0);
		main.add(layer, c);

		c.weightx = 0.1;
		c.weighty = 0.0;
		c.gridx = 1;
		c.gridy = 0;
		c.ipady = 10;
		//c.anchor = GridBagConstraints.NORTHWEST;
		main.add(extra, c);

		area = new JScrollPane(main, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		area.getVerticalScrollBar().setUnitIncrement(10);
		area.getHorizontalScrollBar().setUnitIncrement(10);
		area.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		area.setBackground(clear);
		area.setVisible(true);
		area.setOpaque(true);

		panel = new JPanel(new GridBagLayout());
		panel.setOpaque(false);
		panel.setBackground(clear);

		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		panel.add(area, c);

		c.weightx = 0.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 1;
		c.ipady = 10;
		panel.add(reveal, c);

		if (squareSize * (x + 6) > width && squareSize * (y + 2) > height - 30) {
			frame.setPreferredSize(new Dimension((int) width, (int) height - 30));
		} else if (squareSize * (x + 6) > width) {
			frame.setPreferredSize(new Dimension((int) width, squareSize * (y + 2)));
		} else if (squareSize * (y + 2) > height - 30) {
			frame.setPreferredSize(new Dimension(squareSize * (x + 6), (int) height - 30));
		} else {
			frame.setPreferredSize(new Dimension(squareSize * (x + 6), squareSize * (y + 2)));
		}
		frame.setContentPane(panel);
		frame.pack();
		frame.setBackground(clear);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.getRootPane().setDefaultButton(reveal);
	}

	private void setUpLetters(ArrayList<JLabel[][]> allLetters) {
		JLabel[][] letters = new JLabel[x - 2][y - 2];
		allLetters.add(letters);
	}

	private void setUpIcons(ArrayList<Icon[][]> allLetters) {
		Icon[][] icon = new Icon[x - 2][y - 2];
		temporaryIcons.add(icon);
	}

	private void setUpClues(double noralisedScale) {
		clues.removeAll();
		clueText.clear();
		sorted.clear();
		allClues.clear();
		for (Entry entry : entries) {
			clueText.add(entry.getWord().toUpperCase());
		}
		sorted = sortedStrings(clueText, sortMethod);
		for (String a : sorted) {
			JLabel temp = new JLabel(a);
			mouseActionlabel(temp);
			if (struckThrough.contains(a)) {
				temp.setFont(font4);
			} else {
				temp.setFont(font3);
			}

			allClues.add(temp);
		}
		for (JLabel temp : allClues) {
			clues.add(temp);
		}
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
		layer.setBorder(border);
		layer.setBackground(clear);
		layer.setOpaque(false);
		for (int i = 0; i < x - 2; i++) {
			for (int j = 0; j < y - 2; j++) {
				ImageIcon temp;
				labels[i][j] = new JLabel();
				labels[i][j].setHorizontalTextPosition(SwingConstants.CENTER);
				labels[i][j].setOpaque(false);
				labels[i][j].setFont(font);
				if (temporaryIcons.get(level)[i][j] != null) {
					temp = (ImageIcon) temporaryIcons.get(level)[i][j];
					Image img = temp.getImage();
					Image newimg = img.getScaledInstance(squareSize, squareSize, java.awt.Image.SCALE_SMOOTH);
					temp = new ImageIcon(newimg);
					labels[i][j].setIcon(temp);
				}
				if (level == 0) {
					if (grid[j + 1][i + 1] != "_") {
						if (buttonPushed) {
							labels[i][j].setOpaque(true);
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
				mouseActionlabel(labels[i][j]);
				layer.add(labels[i][j]);
			}
		}
		return layer;
	}

	void mouseActionlabel(JLabel l) {
		l.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				for (int i = 0; i < x - 2; i++) {
					for (int j = 0; j < y - 2; j++) {
						if (e.getSource().equals(allLetters.get(0)[i][j])) {
							for (Entry a : entries) {
								x_pos = a.end_x;
								y_pos = a.end_y;
								if (a.palindromic && !start) {
									x_pos = a.start_x;
									y_pos = a.start_y;
								}
								if (x_pos == j + 1 && y_pos == i + 1) {
									if (tempStrikethrough.contains(a.getWord())) {
										for (JLabel temp : allClues) {
											if (temp.getText().equals(a.getWord().toUpperCase())
													&& !(struckThrough.contains(temp.getText()))) {
												if (!solutions.contains(temp.getText())) {
													solutions.add(temp.getText());
													counter++;
													temp.setFont(font4);
													struckThrough.add(temp.getText());
												}
												String[] images = setImageDirections(a.direction);
												Icon[] icons = new Icon[5];
												setImages = new SetUpImages(images, squareSize, squareSize, icons);
												setDiagonalImages(a.end_y - 1, a.end_x - 1, 2, icons);
												setDiagonalImages(a.start_y - 1, a.start_x - 1, 0, icons);
												int[] t = setIncrements(a.direction);
												for (int c = 0; c < a.getWordLength() - 1; c++) {
													if (!(c == 0)) {
														setDiagonalImages(a.start_y - 1 + c * t[1],
																a.start_x - 1 + c * t[0], 1, icons);
													}
													if (a.isDiagonal) {
														if (a.direction.equals("BLTRdiagonal")) {
															setDiagonalImages(a.start_y - 2 + c * t[1],
																	a.start_x - 1 + c * t[0], 3, icons);
															setDiagonalImages(a.start_y - 1 + c * t[1],
																	a.start_x + c * t[0], 4, icons);
														} else if (a.direction.equals("backwardsBLTRdiagonal")) {
															setDiagonalImages(a.start_y + c * t[1],
																	a.start_x - 2 + (c - 1) * t[0], 3, icons);
															setDiagonalImages(a.start_y - 1 + c * t[1],
																	a.start_x - 2 + c * t[0], 4, icons);
														} else if (a.direction.equals("diagonal")) {
															setDiagonalImages(a.start_y - 1 + c * t[1],
																	a.start_x - 1 + (c + 1) * t[0], 3, icons);
															setDiagonalImages(a.start_y + c * t[1],
																	a.start_x - 1 + c * t[0], 4, icons);
														} else {
															setDiagonalImages(a.start_y - 1 + c * t[1],
																	a.start_x - 2 + c * t[0], 3, icons);
															setDiagonalImages(a.start_y - 2 + c * t[1],
																	a.start_x - 1 + c * t[0], 4, icons);
														}
													}
												}
												tempStrikethrough.clear();
											}
										}
									}
								}
								if (counter == entries.size() && !congratulations) {
									JOptionPane.showMessageDialog(frame, "Congratulations!");
									SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {

										/**
										 * To implement threads Allow GUI to run
										 * without pausing while congratulations
										 * comes up later
										 * 
										 * @Override(non-Javadoc) @see
										 *                        javax.swing.
										 *                        SwingWorker#
										 *                        doInBackground
										 *                        ()
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

			public void mouseEntered(MouseEvent e) {
				for (JLabel lab : allClues) {
					if (e.getSource() == lab && notIn && reset) {
						clues.setBackground(new Color(250, 250, 250, 255));
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

	public void setDiagonalImages(int x, int y, int image, Icon[] icons) {
		for (JLabel[][] lab : allLetters) {
			if (!buttonPushed) {
				lab[x][y].setOpaque(false);
			}
			if (temporaryIcons.get(allLetters.indexOf(lab))[x][y] == null && lab[x][y].getText().equals("")) {
				lab[x][y].setIcon(icons[image]);
				temporaryIcons.get(allLetters.indexOf(lab))[x][y] = icons[image];
				lab[x][y].setText(" ");
				break;
			}
		}
	}

	public void drawGrid(double normalised) {
		layer.removeAll();
//		if(tempScale != normalisedScale){
//			layerX = (int)(tempLayerX + ((mouseX - frame.getX())/((x-2)*INITIAL_SQUARE_SIZE*(tempScale - normalisedScale))));
//			layerY = (int)(tempLayerY + ((mouseY - frame.getY())/((x-2)*INITIAL_SQUARE_SIZE*(tempScale - normalisedScale))));
//		}
//		if(layer.getWidth()<(x-2)*INITIAL_SQUARE_SIZE){
//			layer.setBounds(40,40, squareSize * (x - 2), squareSize * (y - 2));
//			System.out.println("Setting to edge");
//		}else{
			layer.setBounds(layerX, layerY, squareSize * (x - 2), squareSize * (y - 2));
		//}
		tempLayerX = layerX;
		tempLayerY = layerY;
		System.out.println("layerX = "+layerX+ " layerY = "+layerY+" frameX: "+frame.getX()+" frameY: "+ frame.getY());
		//layer.setBounds((int)((mouseX-frame.getX())/), (int)((mouseY-frame.getY())- squareSize*normalisedScale), squareSize * (x - 2), squareSize * (y - 2));
		//layer.setBounds(squareSize, squareSize, squareSize * (x - 2), squareSize * (y - 2));
		layer.setPreferredSize(new Dimension(squareSize * (x-1)+40, squareSize * (y)));
		//layer.setMinimumSize(new Dimension(squareSize * (x), squareSize * (y + 2)));
		for (int i = 0; i < NUMBER_OF_LAYERS; i++) {
			JPanel transparentLayer = new JPanel(new GridLayout(x - 2, y - 2));
			transparentLayer = setUpLayers(allLetters.get(i), transparentLayer, i);
			layer.add(transparentLayer, new Integer(0));
		}

		/*
		 * //Logger logger = null; //logger.log(Level.FINER,
		 * DiagnosisMessages.systemHealthStatus()); //Trying logrecord stuff
		 * LogRecord record = null; String str = "squareSize: "+ squareSize;
		 * logger.log(record); System.out.println("squareSize: "+ squareSize);
		 */
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
		String middle = "";
		String start = "";
		String end = "";
		String corner1 = "";
		String corner2 = "";
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
		} else {
			start = "Left";
			middle = "Horizontal";
			end = "Right";
			corner1 = "";
			corner2 = "";
		}
		String[] images = { start, middle, end, corner1, corner2 };
		return images;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == reveal) {
			diagonal = false;
			buttonPushed = !buttonPushed;
			if (buttonPushed) {
				reveal.setText("Hide Solution");
				for (int i = 0; i < x - 1; i++) {
					for (int j = 0; j < y - 1; j++) {
						if (!grid[i][j].equals("_")) {
							allLetters.get(0)[j - 1][i - 1].setIcon(null);
							allLetters.get(0)[j - 1][i - 1].setOpaque(true);
							allLetters.get(0)[j - 1][i - 1].setBackground(Color.GREEN);
						}
					}
				}
			} else {
				reveal.setText("Show Solution");
				for (int i = 0; i < x - 2; i++) {
					for (int j = 0; j < y - 2; j++) {
						allLetters.get(0)[j][i].setOpaque(true);
						allLetters.get(0)[j][i].setBackground(clear);
					}
				}
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
				setUpClues(normalisedScale);
			} else if (msg.equals("BIGGEST")) {
				orderClues.setVisible(false);
				sortMethod = "biggest";
				setUpClues(normalisedScale);
			} else if (msg.equals("SMALLEST")) {
				orderClues.setVisible(false);
				sortMethod = "smallest";
				setUpClues(normalisedScale);
			} else if (msg.equals("RANDOM")) {
				orderClues.setVisible(false);
				sortMethod = "random";
				setUpClues(normalisedScale);
			}
		}
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		Point mouseCoord = MouseInfo.getPointerInfo().getLocation();
		mouseX = mouseCoord.getX();
		mouseY = mouseCoord.getY();
		tempScale = normalisedScale;
		if (e.isControlDown()) {
			if (e.getWheelRotation() < 0) {
				if (scale < MAX_SCALE) {
					scale++;
				}
				resetSizes();
			} else {
				System.out.println("scrolled down");
				if (scale > MIN_SCALE) {
					scale--;
				}
				resetSizes();
			}
		}
		if (e.getWheelRotation() < 0) {
			//System.out.println("Scrolling Up...");
		} else {
			//System.out.println("Scrolling Down...");
		}
		// else scroll like normal
	}

	public void resetSizes() {
//		tempLayerX = layer.getX();
//		tempLayerY = layer.getY();
		tempLayerWidth = layer.getWidth();
		tempLayerHeight = layer.getHeight();
		font = new Font("Century Gothic", Font.PLAIN, squareSize / 5 * 3);
		normalisedScale = scale / 20;
		squareSize = (int) (normalisedScale * INITIAL_SQUARE_SIZE);
		main.revalidate();
		//font3 = new Font("Century Gothic", Font.PLAIN, (int)(18*normalisedScale));
		font2 = new Font("Century Gothic", Font.PLAIN, (int) (normalisedScale * INITIAL_SQUARE_SIZE / 5 * 3));
		font = new Font("Century Gothic", Font.PLAIN, (int) (normalisedScale * INITIAL_SQUARE_SIZE / 5 * 3));
		drawGrid(normalisedScale);
		System.out.println("Scale: " + scale + " Normalised: " + normalisedScale + " squareSize: " + squareSize);
		System.out.println("mouseX: " + mouseX + " mouseY: " + mouseY);
		System.out.println("tempLayerX: "+tempLayerX + " tempLayerY: " +tempLayerY +" tempLayerWidth: "+tempLayerWidth
				+" tempLayerHeight: " +tempLayerHeight+"\n\n");
	}
}
