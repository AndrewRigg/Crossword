package WordSearch;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import Crossword.DrawSolution;
import Crossword.Entry;

/**
 * Class to draw a word search
 */
public class DrawWordSearch extends JComponent implements ActionListener {
	private static final long serialVersionUID = 1L;
	private static int squareSize = 40;	
	int x, y;
	JFrame frame;
	JPanel panel, transparentLayer, transparentLayer2, transparentLayer3, transparentLayer4, main, clues;
	JLayeredPane layer;
	JLabel [][] letters, letters2, letters3, letters4;
	ArrayList<JLabel[][]> allLayers;
	String[][] grid;
	GridBagConstraints c;
	String [] loopDirections = {"top", "topRight", "right", "bottomRight", "bottom", "bottomLeft", "left", "topLeft"};
	String operatingSystem;
	String imagePath = "";
	JButton reveal;
	boolean diagonal;
	JScrollPane area;
	DrawSolution sol;
	ArrayList<String> fullGrid, tempStrikethrough;
	ArrayList<JLabel> completed;
	ArrayList<Entry> entries;
	ArrayList<JLabel> allClues;
	String randomFill = "AAAAAAAAABBCCDDDDEEEEEEEEEEEFFGGGHHIIIIIIIIIJKLLLLMMNNNNNNOOOOOOOOPPQRRRRRRSSSSTTTTTTUUUUVVWWXYYZ";
	Font font, font2, font3, font4;
	Random rand;
	boolean buttonPushed, clicked;
	Color grey;
	int wordLength, dir, startx, starty;
	Color clear;
	Dimension screenSize; 
	double width;
	double height;
	Border border;
	String tempWord = "";
	
	@SuppressWarnings("unchecked")
	public DrawWordSearch(String[][] grid, int x, int y, ArrayList<String> cluesAcross, ArrayList<String> cluesDown,  ArrayList<Entry> entries) throws IOException{
		this.x = x;
		this.y = y;
		this.grid = grid;
		this.entries = entries;
		fullGrid = new ArrayList<String>();
		allClues = new ArrayList<JLabel>();
		completed = new ArrayList<JLabel>();
		allLayers = new ArrayList<JLabel[][]>();
		tempStrikethrough = new ArrayList<String>();
		
		font3 = new Font("Century Gothic", Font.PLAIN, 18);
		font2 = new Font("Century Gothic", Font.PLAIN, 24);
		font = new Font("Century Gothic", Font.PLAIN, squareSize / 5 * 3);
		Map fontAttr = font3.getAttributes();
		fontAttr.put (TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
		font4 = new Font(fontAttr);
		
		frame = new JFrame("Auto Word Search");
		frame.setBackground(new Color(255,255,255,255));
		frame.setMinimumSize(new Dimension(550,400));
		
		sol = new DrawSolution(grid, x, y, squareSize, "Word Search");
		grey = new Color(200,200,200,255);
		wordLength = 0;
		dir = 0;
		startx = 0;
		starty = 0;
		area = new JScrollPane();
		clear = new Color(255, 255, 255, 255);
		
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		width = screenSize.getWidth();
		height = screenSize.getHeight();
		
		/**
		 *  Set image path depending on OS
		 */
		operatingSystem = System.getProperty("os.name").toLowerCase();
		if(operatingSystem.equals("linux")){
			imagePath = "src/resources/";
		}
		else if(operatingSystem.contains("windows")){
			imagePath = "src\\resources\\";
		}
	
		border = BorderFactory.createLineBorder(Color.BLACK);
		layer = new JLayeredPane();
		letters = new JLabel [x-2][y-2];
		letters2 = new JLabel [x-2][y-2];
		letters3 = new JLabel [x-2][y-2];
		letters4 = new JLabel [x-2][y-2];
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		rand = new Random();
		buttonPushed = false;
		
		reveal = new JButton("Show Solution");
		reveal.setFont(font2);
		reveal.setEnabled(true);
		reveal.addActionListener(this);

		transparentLayer = new JPanel(new GridLayout(x-2, y-2));
		transparentLayer.setBounds(squareSize,squareSize,squareSize*(x-2),squareSize*(y-2));
		transparentLayer.setBorder(border);
		transparentLayer.setBackground(clear);
		transparentLayer.setOpaque(false);

		for (int i = 0; i < x-2; i++){
			for (int j = 0; j < y-2; j++){
				letters[i][j] = new JLabel();
				letters[i][j].setHorizontalTextPosition(SwingConstants.CENTER);
				letters[i][j].setOpaque(false);
				letters[i][j].setFont(font);
				letters[i][j].setForeground(Color.BLACK);
				letters[i][j].setBackground(clear);
				letters[i][j].setBorder(null);
				letters[i][j].setBounds(squareSize, squareSize, squareSize * (x - 2), squareSize * (y - 2));
				if(grid[j+1][i+1] != "_"){
					letters[i][j].setText(grid[j+1][i+1].toUpperCase());
				}else{
					letters[i][j].setText(Character.toString(randomFill.charAt(rand.nextInt(randomFill.length()))));
				}
				letters[i][j].setHorizontalAlignment(JTextField.CENTER);
				
				letters[i][j].setVerticalAlignment(JTextField.CENTER);
				mouseActionlabel(letters[i][j]);
				transparentLayer.add(letters[i][j]);
			}
		}		
		

		transparentLayer2 = new JPanel(new GridLayout(x-2, y-2));
		transparentLayer2.setBounds(squareSize,squareSize,squareSize*(x-2),squareSize*(y-2));
		transparentLayer2.setBorder(border);
		transparentLayer2.setBackground(clear);
		transparentLayer2.setOpaque(false);

		for (int i = 0; i < x-2; i++){
			for (int j = 0; j < y-2; j++){
				letters2[i][j] = new JLabel();
				letters2[i][j].setHorizontalTextPosition(SwingConstants.CENTER);
				letters2[i][j].setOpaque(false);
				letters2[i][j].setFont(font);
				letters2[i][j].setForeground(Color.BLACK);
				letters2[i][j].setBackground(clear);
				letters2[i][j].setBorder(null);
				letters2[i][j].setBounds(squareSize, squareSize, squareSize * (x - 2), squareSize * (y - 2));
//				if(grid[j+1][i+1] != "_"){
//					//letters2[i][j].setText(grid[j+1][i+1].toUpperCase());
//				}else{
//					letters2[i][j].setText("X");
//					//letters2[i][j].setText(Character.toString(randomFill.charAt(rand.nextInt(randomFill.length()))));
//				}
				letters2[i][j].setHorizontalAlignment(JTextField.CENTER);
				letters2[i][j].setVerticalAlignment(JTextField.CENTER);
				mouseActionlabel(letters2[i][j]);
				transparentLayer2.add(letters2[i][j]);
			}
		}		
		
		transparentLayer3 = new JPanel(new GridLayout(x-2, y-2));
		transparentLayer3.setBounds(squareSize,squareSize,squareSize*(x-2),squareSize*(y-2));
		transparentLayer3.setBorder(border);
		transparentLayer3.setBackground(clear);
		transparentLayer3.setOpaque(false);

		for (int i = 0; i < x-2; i++){
			for (int j = 0; j < y-2; j++){
				letters3[i][j] = new JLabel();
				letters3[i][j].setHorizontalTextPosition(SwingConstants.CENTER);
				letters3[i][j].setOpaque(false);
				letters3[i][j].setFont(font);
				letters3[i][j].setForeground(Color.BLACK);
				letters3[i][j].setBackground(clear);
				letters3[i][j].setBorder(null);
				letters3[i][j].setBounds(squareSize, squareSize, squareSize * (x - 2), squareSize * (y - 2));
//				if(grid[j+1][i+1] != "_"){
//					//letters2[i][j].setText(grid[j+1][i+1].toUpperCase());
//				}else{
//					letters3[i][j].setText("Y");
//					//letters2[i][j].setText(Character.toString(randomFill.charAt(rand.nextInt(randomFill.length()))));
//				}
				letters3[i][j].setHorizontalAlignment(JTextField.CENTER);
				letters3[i][j].setVerticalAlignment(JTextField.CENTER);
				mouseActionlabel(letters3[i][j]);
				transparentLayer3.add(letters3[i][j]);
			}
		}		
		
		transparentLayer4 = new JPanel(new GridLayout(x-2, y-2));
		transparentLayer4.setBounds(squareSize,squareSize,squareSize*(x-2),squareSize*(y-2));
		transparentLayer4.setBorder(border);
		transparentLayer4.setBackground(clear);
		transparentLayer4.setOpaque(false);

		for (int i = 0; i < x-2; i++){
			for (int j = 0; j < y-2; j++){
				letters4[i][j] = new JLabel();
				letters4[i][j].setHorizontalTextPosition(SwingConstants.CENTER);
				letters4[i][j].setOpaque(false);
				letters4[i][j].setFont(font);
				letters4[i][j].setForeground(Color.BLACK);
				letters4[i][j].setBackground(clear);
				letters4[i][j].setBorder(null);
				letters4[i][j].setBounds(squareSize, squareSize, squareSize * (x - 2), squareSize * (y - 2));
//				if(grid[j+1][i+1] != "_"){
//					//letters2[i][j].setText(grid[j+1][i+1].toUpperCase());
//				}else{
//					letters4[i][j].setText("Z");
//					//letters2[i][j].setText(Character.toString(randomFill.charAt(rand.nextInt(randomFill.length()))));
//				}
				letters4[i][j].setHorizontalAlignment(JTextField.CENTER);
				letters4[i][j].setVerticalAlignment(JTextField.CENTER);
				mouseActionlabel(letters4[i][j]);
				transparentLayer4.add(letters4[i][j]);
			}
		}		
		
		allLayers.add(letters);
		allLayers.add(letters2);
		allLayers.add(letters3);
		allLayers.add(letters4);
		
		
	//	transparentLayer2 = new JPanel(new GridLayout(x-2, y-2));
//		transparentLayer3 = new JPanel(new GridLayout(x-2, y-2));
//		transparentLayer4 = new JPanel(new GridLayout(x-2, y-2));
		//setUpLayers(letters2, transparentLayer2);
//		setUpLayers(letters3, transparentLayer3);
//		setUpLayers(letters4, transparentLayer4);
	
		layer.setBackground(clear);
		layer.setVisible(true);
		layer.setOpaque(true);
		layer.setBounds(squareSize,squareSize,squareSize*(x-2),squareSize*(y-2));
		layer.setPreferredSize(new Dimension(squareSize*(x),squareSize*(y)));
		layer.setMinimumSize(new Dimension(squareSize*(x),squareSize*(y+2)));
		layer.add(transparentLayer, new Integer(0));
		layer.add(transparentLayer4, new Integer(0));
		layer.add(transparentLayer3, new Integer(0));
		layer.add(transparentLayer2, new Integer(0));
		clues = new JPanel(new GridLayout(cluesAcross.size()+cluesDown.size(), 1));
		clues.setBackground(clear);
		
		clues.setVisible(true);
		clues.setOpaque(true);
		
		for(Entry entry: entries){
			JLabel temp = new JLabel(entry.getWord().toUpperCase());
			temp.setFont(new Font("Century Gothic", Font.PLAIN, 18));
			allClues.add(temp);
		}
		
		for(JLabel temp: allClues){
			clues.add(temp);
		}
		
		main = new JPanel(new GridBagLayout());
		main.setBackground(clear);
		main.setVisible(true);
		main.setOpaque(false);
		
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 0;
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
		
		panel = new JPanel(new GridBagLayout());
		panel.setOpaque(false);
		panel.setBackground(clear);
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
		
		if(squareSize*(x+6) > width && squareSize*(y+2) > height-30){
			frame.setPreferredSize(new Dimension((int)width,(int)height-30));
		}
		else if(squareSize*(x+6) > width){
			frame.setPreferredSize(new Dimension((int)width,squareSize*(y+2)));
		}else if(squareSize*(y+2) > height-30){
			frame.setPreferredSize(new Dimension(squareSize*(x+6), (int)height-30));
		}else{
			frame.setPreferredSize(new Dimension(squareSize*(x+6), squareSize*(y+2)));
		}
		frame.setContentPane(panel);
		frame.pack();
		frame.setBackground(clear);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.getRootPane().setDefaultButton(reveal);
	}
	
	/**
	 * This sets the imageIcon from a given name of image and creates the corresponding image, 
	 * from it.  The image is then scaled and placed into a temporary image
	 * and finally this is set back to the imageIcon itself.  
	 */
	public Icon setImage(String path, int scaleX, int scaleY){
		ImageIcon icon = new ImageIcon(path);
		Image img = icon.getImage();
		Image newimg = img.getScaledInstance(scaleX, scaleY, java.awt.Image.SCALE_SMOOTH ) ; 
		icon = new ImageIcon(newimg);
		return icon;	
	}
	
	/**
	 * Class to set grids of labels for each potential layer
	 * @param labels
	 * @param layer
	 */
	public void setUpLayers(JLabel[][] labels, JPanel layer){
		
		layer = new JPanel(new GridLayout(x-2, y-2));
		layer.setBounds(squareSize,squareSize,squareSize*(x-2),squareSize*(y-2));
		layer.setBorder(border);
		layer.setBackground(clear);
		layer.setOpaque(true);

		for (int i = 0; i < x-2; i++){
			for (int j = 0; j < y-2; j++){
				labels[i][j] = new JLabel();
				labels[i][j].setHorizontalTextPosition(SwingConstants.CENTER);
				labels[i][j].setOpaque(true);
				labels[i][j].setFont(font);
				labels[i][j].setForeground(Color.BLACK);
				labels[i][j].setBackground(Color.GREEN);
				labels[i][j].setText("X");
				labels[i][j].setBorder(null);
				labels[i][j].setBounds(squareSize, squareSize, squareSize * (x - 2), squareSize * (y - 2));
				labels[i][j].setHorizontalAlignment(JTextField.CENTER);
				labels[i][j].setVerticalAlignment(JTextField.CENTER);
				mouseActionlabel(labels[i][j]);
				layer.add(labels[i][j]);				
			}
		}
	}
	
	
	/**
	 * Class which takes String name of image (eg top for the top facing loop image) and appends the 
	 * appropriate path according to operating system to the beginning and then appends ".png" to the end.
	 */
	public String setPath(String imageName){
		return (imagePath + imageName + "2.png");	
	}
	
	void mouseActionlabel(JLabel l) {
		l.addMouseListener(new MouseListener() {
		
			
			public void mouseClicked(MouseEvent e) {
				for (int i = 0; i < x-2; i++){
					for (int j = 0; j < y-2; j++){
						if (e.getSource().equals(letters[i][j])){
							for(Entry a : entries){
								if(a.end_x == j+1 && a.end_y == i+1){
									System.out.println("a.endx: " + a.end_x + " a.endy: " + a.end_y);
									System.out.println("now: " + tempWord);
									if(tempStrikethrough.contains(a.getWord())){
										for (JLabel temp: allClues){
											if(temp.getText().equals(a.getWord().toUpperCase())){
												temp.setFont(font4);
												String[] images = setImageDirections(a.direction);
												Icon ic0 = setImage(setPath(images[0]), squareSize, squareSize);
												Icon ic1 = setImage(setPath(images[1]), squareSize, squareSize);
												Icon ic2 = setImage(setPath(images[2]), squareSize, squareSize);
												Icon ic3 = setImage(setPath(images[3]), squareSize, squareSize);
												Icon ic4 = setImage(setPath(images[4]), squareSize, squareSize);
												for(JLabel[][] lab: allLayers){
													if(lab[i][j].getText().equals("")){
														lab[i][j].setIcon(ic2);
														lab[i][j].setText(" ");
														System.out.println("Direction: "+ a.direction+ " layer: " + allLayers.indexOf(lab));
														break;
													}
												}
												for(JLabel[][] lab: allLayers){
													if(lab[a.start_y-1][a.start_x-1].getText().equals("")){
														lab[a.start_y-1][a.start_x-1].setIcon(ic0);
														lab[a.start_y-1][a.start_x-1].setText(" ");
														System.out.println("layer: " + allLayers.indexOf(lab));
														break;
													}
												}
											
												int [] t = setIncrements(a.direction);
												for(int c = 0; c < a.getWordLength()-1; c++){
													//Try to dynamically set images on different layers
													if(!(c == 0)){
														for(JLabel[][] lab: allLayers){
															if(lab[a.start_y-1+c*t[1]][a.start_x-1+c*t[0]].getText().equals("")){
																lab[a.start_y-1+c*t[1]][a.start_x-1+c*t[0]].setIcon(ic1);
																lab[a.start_y-1+c*t[1]][a.start_x-1+c*t[0]].setText(" ");
																System.out.println("layer: " + allLayers.indexOf(lab));
																break;
															}
														}
													}											
													if(a.isDiagonal){
														if(a.direction.equals("BLTRdiagonal")){
															for(JLabel[][] lab: allLayers){
																if(lab[a.start_y-2+c*t[1]][a.start_x-1+c*t[0]].getText().equals("")){
																	lab[a.start_y-2+c*t[1]][a.start_x-1+c*t[0]].setIcon(ic3);
																	lab[a.start_y-2+c*t[1]][a.start_x-1+c*t[0]].setText(" ");
																	System.out.println("layer: " + allLayers.indexOf(lab));
																	break;
																}
															}
															for(JLabel[][] lab: allLayers){
																if(lab[a.start_y-1+c*t[1]][a.start_x+c*t[0]].getText().equals("")){
																	lab[a.start_y-1+c*t[1]][a.start_x+c*t[0]].setIcon(ic4);
																	lab[a.start_y-1+c*t[1]][a.start_x+c*t[0]].setText(" ");
																	System.out.println("layer: " + allLayers.indexOf(lab));
																	break;
																}
															}
														}else if(a.direction.equals("backwardsBLTRdiagonal")){
															for(JLabel[][] lab: allLayers){
																if(lab[a.start_y-1+c*t[1]][a.start_x-2+c*t[0]].getText().equals("")){
																	lab[a.start_y-1+c*t[1]][a.start_x-2+c*t[0]].setIcon(ic4);
																	lab[a.start_y-1+c*t[1]][a.start_x-2+c*t[0]].setText(" ");
																	System.out.println("layer: " + allLayers.indexOf(lab));
																	break;
																}
															}
															for(JLabel[][] lab: allLayers){
																if(lab[a.start_y+c*t[1]][a.start_x-2+(c-1)*t[0]].getText().equals("")){
																	lab[a.start_y+c*t[1]][a.start_x-2+(c-1)*t[0]].setIcon(ic3);
																	lab[a.start_y+c*t[1]][a.start_x-2+(c-1)*t[0]].setText(" ");
																	System.out.println("layer: " + allLayers.indexOf(lab));
																	break;
																}
															}
														}else if(a.direction.equals("diagonal")){
															for(JLabel[][] lab: allLayers){
																if(lab[a.start_y-1+c*t[1]][a.start_x-1+(c+1)*t[0]].getText().equals("")){
																	lab[a.start_y-1+c*t[1]][a.start_x-1+(c+1)*t[0]].setIcon(ic3);
																	lab[a.start_y-1+c*t[1]][a.start_x-1+(c+1)*t[0]].setText(" ");
																	System.out.println("layer: " + allLayers.indexOf(lab));
																	break;
																}
															}
															for(JLabel[][] lab: allLayers){
																if(lab[a.start_y+c*t[1]][a.start_x-1+c*t[0]].getText().equals("")){
																	lab[a.start_y+c*t[1]][a.start_x-1+c*t[0]].setIcon(ic4);
																	lab[a.start_y+c*t[1]][a.start_x-1+c*t[0]].setText(" ");
																	System.out.println("layer: " + allLayers.indexOf(lab));
																	break;
																}
															}
														}else{
															for(JLabel[][] lab: allLayers){
																if(lab[a.start_y-1+c*t[1]][a.start_x-2+c*t[0]].getText().equals("")){
																	lab[a.start_y-1+c*t[1]][a.start_x-2+c*t[0]].setIcon(ic3);
																	lab[a.start_y-1+c*t[1]][a.start_x-2+c*t[0]].setText(" ");
																	System.out.println("layer: " + allLayers.indexOf(lab));
																	break;
																}
															}
															for(JLabel[][] lab: allLayers){
																if(lab[a.start_y-2 + c*t[1]][a.start_x-1+c*t[0]].getText().equals("")){
																	lab[a.start_y-2 + c*t[1]][a.start_x-1+c*t[0]].setIcon(ic4);
																	lab[a.start_y-2 + c*t[1]][a.start_x-1+c*t[0]].setText(" ");
																	System.out.println("layer: " + allLayers.indexOf(lab));
																	break;
																}
															}
//															System.out.println("a.start_x: " + a.start_x);
//															System.out.println("a.start_y: " + a.start_y);
//															System.out.println("t[0,1] = {" + t[0]+","+t[1]+"}");
														}
													}
												}
												
												
												
//												
//												
//												int [] t = setIncrements(a.direction);
//												for(int c = 0; c < a.getWordLength()-1; c++){
//													//Try to dynamically set images on different layers
//													if(!(c == 0)){
//														//if(letters[a.start_y-1+c*t[1]][a.start_x-1+c*t[0]].getText().equals(""))
//															letters[a.start_y-1+c*t[1]][a.start_x-1+c*t[0]].setIcon(ic1);
//													}											
//													if(a.isDiagonal){
//														if(a.direction.equals("BLTRdiagonal")){
//															letters2[a.start_y-2+c*t[1]][a.start_x-1+c*t[0]].setIcon(ic3);
//															letters2[a.start_y-1+c*t[1]][a.start_x+c*t[0]].setIcon(ic4);
//														}else if(a.direction.equals("backwardsBLTRdiagonal")){
//															letters4[a.start_y-1+c*t[1]][a.start_x-2+c*t[0]].setIcon(ic4);
//															letters4[a.start_y+c*t[1]][a.start_x-2+(c-1)*t[0]].setIcon(ic3);
//														}else if(a.direction.equals("diagonal")){
//															letters3[a.start_y-1+c*t[1]][a.start_x-1+(c+1)*t[0]].setIcon(ic3);
//															letters3[a.start_y+c*t[1]][a.start_x-1+c*t[0]].setIcon(ic4);
//														}else{
//															System.out.println("a.start_x: " + a.start_x);
//															System.out.println("a.start_y: " + a.start_y);
//															System.out.println("t[0,1] = {" + t[0]+","+t[1]+"}");
//															letters4[a.start_y-1+c*t[1]][a.start_x-2+c*t[0]].setIcon(ic3);
//															letters4[a.start_y-2 + c*t[1]][a.start_x-1+c*t[0]].setIcon(ic4);
//														}
//													}
//												}
												tempStrikethrough.clear();
											}									
										}	
									}
								}
							}
							tempStrikethrough.clear();
							for(Entry b : entries){
								if(b.start_x == j+1 && b.start_y == i+1){
									tempWord = b.getWord();
									//tempStrikethrough.clear();
									tempStrikethrough.add(tempWord);
									System.out.println("Start clicked: "+ tempWord);
								}
							}
						}
					}
				}
			}

			private int[] setIncrements(String direction) {
				int [] inc = new int[2];
				if(direction.equals("across")){
					inc[0] = 1;
					inc[1] = 0;
				}
				else if(direction.equals("backwards")){
					inc[0] = -1;
					inc[1] = 0;				
				}
				else if(direction.equals("down")){
					inc[0] = 0;
					inc[1] = 1;
				}
				else if(direction.equals("up")){
					inc[0] = 0;
					inc[1] = -1;
				}
				else if(direction.equals("diagonal")){
					inc[0] = 1;
					inc[1] = 1;
				}
				else if(direction.equals("backwardsdiagonal")){
					inc[0] = -1;
					inc[1] = -1;
				}
				else if(direction.equals("BLTRdiagonal")){
					inc[0] = 1;
					inc[1] = -1;
				}
				else if(direction.equals("backwardsBLTRdiagonal")){
					inc[0] = -1;
					inc[1] = 1;
				}
				return inc;
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mousePressed(MouseEvent e) {

			}

			public void mouseReleased(MouseEvent e) {

			}
		});
	}
	
	/**
	 * Get names of images for relevant direction of word
	 * @param direction
	 * @param diagonal
	 * @return
	 */
	public String [] setImageDirections(String direction){
		String middle = "";
		String start = "";
		String end = "";
		String corner1 = "";
		String corner2 = "";
		if(direction.equals("across")){
			start = "Left";
			middle = "Horizontal";
			end = "Right";
		}else if(direction.equals("down")){
			start = "Top";
			middle = "Vertical";
			end = "Bottom";
		}else if(direction.equals("diagonal")){
			start = "TopLeft";
			middle = "DiagonalDownRight";
			end = "BottomRight";
			corner1 = "BottomLeftCorner";
			corner2 = "TopRightCorner";
			System.out.println("Changed Diagonal");
		}else if(direction.equals("backwards")){
			start = "Right";
			middle = "Horizontal";
			end = "Left";
		}else if(direction.equals("up")){
			start = "Bottom";
			middle = "Vertical";
			end = "Top";
		}else if(direction.equals("backwardsdiagonal")){
			start = "BottomRight";
			middle = "DiagonalDownRight";
			end = "TopLeft";
			corner1 = "TopRightCorner";
			corner2 = "BottomLeftCorner";
			System.out.println("Changed Diagonal");
		}else if(direction.equals("BLTRdiagonal")){
			start = "BottomLeft";
			middle = "DiagonalUpRight";
			end = "TopRight";
			corner1 = "BottomRightCorner";
			corner2 = "TopLeftCorner";
			System.out.println("Changed Diagonal");
		}else if(direction.equals("backwardsBLTRdiagonal")){
			start = "TopRight";
			middle = "DiagonalUpRight";
			end = "BottomLeft";
			corner1 = "TopLeftCorner";
			corner2 = "BottomRightCorner";
			System.out.println("Changed Diagonal");
		}else{
			start = "Left";
			middle = "Horizontal";
			end = "Right";
			corner1 = "";
			corner2 = "";
		}
		String [] images = {start, middle, end, corner1, corner2};
		return images; 
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==reveal){
			diagonal = false;
			buttonPushed = !buttonPushed;
				if(buttonPushed){
					for (int i = 0; i < x-1; i++){
						for (int j = 0; j < y-1; j++){
							if(!grid[i][j].equals("_")){
								reveal.setText("Hide Solution");
								letters[j-1][i-1].setBackground(Color.GREEN);
							}
					}
				}
			}else{
				for (int i = 0; i < x-1; i++){
					for (int j = 0; j < y-1; j++){
						if(!grid[i][j].equals("_")){
							reveal.setText("Show Solution");
							letters[j-1][i-1].setBackground(new Color(255,255,255,255));
						}
					}
				}
			}
		}
	}
}
