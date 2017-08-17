package sudoku;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
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
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.text.Caret;
import javax.swing.text.DefaultCaret;

import crossword.JTextFieldLimit;
import crossword.SetUpImages;

import java.awt.event.AWTEventListener; //??




/**
 * Class to draw a word search
 */
public class DrawSudoku extends JComponent implements ActionListener, MouseListener {
	private static final long serialVersionUID = 1L;
	private static int squareSize = 50;	
	int x, y;
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	// Define colors
	Color HIGHLIGHT_COLOUR = new Color( 230 , 230,  230);
//	Color HINT_COLOR = new Color(150,150,220);
	Color HINT_COLOR = new Color(170,220,230);

	Color wrong = new Color(230,0,0);
	Color correct = new Color( 0 , 255,  0);
	//Color correct = new Color( 20 , 255,  20);
	//Color fixed = new Color(90,90,90);
	Color fixed = new Color(40,40,40);
	
	
	int[][] initial_config;
	boolean buttonPushed;
	SudokuGenerator sudo;
	SetUpImages imageSetUp; 
	JCheckBoxMenuItem clickSound;
	JMenuItem exit, thick, normal, smart, genius, save, newGame, chooseFont, 
	tips, colour, french, english, german, italian, spanish;
	JMenuBar menuBar;
	JMenu file, diff, options, languages;
	JFrame frame;
	JPanel panel, transparentLayer, largeGrid;
	JLayeredPane layer;
	JLabel [][] numbers, threeByThreeGrid;
	JTextField [][] nums;
	public int[][] grid, grid2;
	//String[][] grid2;
	GridBagConstraints c;
	JButton solution, hint, clue;
	DrawSudokuSolution sol;
	ArrayList<String> fullGrid;
	ArrayList<Integer> row, square, tempColumn, checks;
	ArrayList<ArrayList<Integer>> cols;
	ArrayList<ArrayList<Integer>> boxes;
	Font font, font2, font3, font4;
	Random rand;
	boolean solutionPushed, hintPushed, cluePushed;
	int difficulty;
	
	ArrayList<KeyEvent> keys;
	Action action;
	
	Icon [] flags;
	static int gridsize=9;
	Border border2;
	boolean gridEmpty, complete;
	
	
	
	

	public DrawSudoku(int[][] grid, int x, int y, int difficulty) throws IOException{
		String [] countries = {"English",  "French",  "German", "Italian","Spanish"};
		this.grid = grid;
		this.difficulty = difficulty;
		complete = false;
		
		border2 = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
		
		font = new Font("Century Gothic", Font.PLAIN, 30);
		font2 = new Font("Century Gothic", Font.PLAIN, 24);
		font4 = new Font("Century Gothic", Font.BOLD, 32);
		font3 = new Font("Century Gothic", Font.PLAIN, 14);
		
		flags = new Icon [5];
		imageSetUp = new SetUpImages(countries, 20, 30, flags, 0);
		UIManager.put("Menu.font", font3);
		UIManager.put("MenuItem.font", font3);
		UIManager.put("CheckBoxMenuItem.font", font3);
		UIManager.put("RadioButtonMenuItem.font", font3);
		
		menuBar = new JMenuBar();
		file = new JMenu("File");
		options = new JMenu("Options");
		diff = new JMenu("Difficulty");
		
		file.setMnemonic(KeyEvent.VK_F);
		options.setMnemonic(KeyEvent.VK_O);
		diff.setMnemonic(KeyEvent.VK_D);
		
		save = new JMenuItem("Save");
		save.addActionListener(this);
		newGame = new JMenuItem("New");
		newGame.addActionListener(this);
		exit = new JMenuItem("Exit");
		exit.addActionListener(this);
		
		chooseFont = new JMenuItem("Font");
		chooseFont.addActionListener(this);
		colour = new JMenuItem("Colour");
		colour.addActionListener(this);
		languages = new JMenu("Language");
		
		english = new JMenuItem("English",  flags[0]);
		french = new JMenuItem("French",  flags[1]);
		german = new JMenuItem("German", flags[2]);
		italian = new JMenuItem("Italian", flags[3]);
		spanish = new JMenuItem("Spanish", flags[4]);
		
		languages.add(english);
		languages.add(french);
		languages.add(german);
		languages.add(italian);
		languages.add(spanish);
		
		
		//file.add(save);
		file.add(newGame);
		file.add(exit);
		
//		options.add(chooseFont);
//		options.add(colour);
		//options.add(languages);
		//options.addSeparator();
		clickSound = new JCheckBoxMenuItem("Click Sound");
		clickSound.setMnemonic(KeyEvent.VK_C);
		options.add(clickSound);
		
		ButtonGroup group = new ButtonGroup();
		thick = new JRadioButtonMenuItem("Easy");
		if(difficulty == 1){
			thick.setSelected(true);
		}
		thick.addActionListener(this);
		thick.setMnemonic(KeyEvent.VK_1);
		group.add(thick);
		normal = new JRadioButtonMenuItem("Normal");
		normal.setMnemonic(KeyEvent.VK_2);
		if(difficulty == 2){
			normal.setSelected(true);
		}
		normal.addActionListener(this);
		group.add(normal);
		smart = new JRadioButtonMenuItem("Hard");
		smart.setMnemonic(KeyEvent.VK_3);
		if(difficulty == 3){
			smart.setSelected(true);
		}
		smart.addActionListener(this);
		group.add(smart);
		genius = new JRadioButtonMenuItem("Expert");
		genius.setMnemonic(KeyEvent.VK_4);
		if(difficulty == 4){
			genius.setSelected(true);
		}
		genius.addActionListener(this);
		group.add(genius);
		
		diff.add(thick);
		diff.add(normal);
		//diff.add(smart);
		//diff.add(genius);
		
		menuBar.add(file);
		menuBar.add(diff);
		//menuBar.add(options);
		
		
	
		
		
		int gridSize=9+2;
		this.x = gridSize;     
		this.y = gridSize;
		
		
		grid = new int[9][9];		
		for(int i = 0; i < 9; i++){
			for( int j = 0; j < 9; j++){
				grid[i][j] = 0;
			}
		}
		
		
		sol = new DrawSudokuSolution( x, y);  // Create the whole object that will be revealed on "show solution"
		sol.generateSudoku(grid);             // Andy's algorithm to generate sudoku solution.
		sol.setVisible(false);
		
		grid2 = grid.clone();
		// We now have a full grid[][] for sudoku base. Modify grid[][] to starting configuration
		initial_config = new int[9][9];
		
		
		
		
		
		
		
		// TEST SUDOKU READER METHOD.
		//initial_config = SudokuReader.readSudoku("sudoku_finland.dat");
		//initial_config = SudokuReader.readSudoku("sudoku_medium2.dat");
		// Attempt to solve
		//int[][] solved_grid = new int[9][9];
		//solved_grid = SudokuMethods.solver_noGuessing( initial_config );
        //
		//System.out.println("Is solved? -- "+ SudokuMethods.isSolved(solved_grid)  );
		

		
		
		
		
			
		if( difficulty == 1){
			initial_config = SudokuMethods.makeEasy( grid );
		}
		else if( difficulty == 2 ){
			initial_config = SudokuMethods.makeMedium( grid );  // Might be ridiculously hard...
		}
		else{
			System.out.println("ONLY EASY AND MEDIUM HAVE BEEN IMPLEMENTED");
			initial_config = SudokuMethods.makeMedium( grid );
		}		
		//System.out.println( grid[0][0] + " " + grid[1][1] + " "+ grid[0][1]);   // CAREFUL: ANDY HAS FLIPPED [X][Y] TO GRID[Y][X]
		
		
	
		
		
		
		
		row = new ArrayList<Integer>();
		cols = new ArrayList<ArrayList<Integer>>();
		boxes = new ArrayList<ArrayList<Integer>>();
		square = new ArrayList<Integer>();
		tempColumn = new ArrayList<Integer>();
		checks = new ArrayList<Integer>();
		fullGrid = new ArrayList<String>();
		panel = new JPanel(new GridBagLayout());
		panel.setOpaque(false);
		layer = new JLayeredPane();
		numbers = new JLabel [x-2][y-2];
		nums = new JTextField [x-2][y-2];
		threeByThreeGrid = new JLabel [3][3];
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		rand = new Random();
		Border border = BorderFactory.createLineBorder(Color.GRAY);	  // this is border for single grids
		Border border2 = BorderFactory.createLineBorder(Color.BLACK);     //  border for 3x3 squares
		//Border border3 = BorderFactory.createLineBorder(Color.BLACK, 4, true);
		solutionPushed = false;
		
		solution = new JButton("Solution");
		solution.setFont(font2);
		//solution.setEnabled(true);
		solution.addActionListener(this);
		
		hint = new JButton("Hint");
		hint.setFont(font2);
		hint.addActionListener(this);
		
		
		clue = new JButton("Check");
		clue.setFont(font2);
		clue.addActionListener(this);
		
		transparentLayer = new JPanel(new GridLayout(x-2, y-2));
		transparentLayer.setBounds(squareSize-1,squareSize-1,squareSize*(x-2),squareSize*(y-2));
		//transparentLayer.setOpaque(true);
		transparentLayer.setOpaque(false); // STEVE
		transparentLayer.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		
		largeGrid = new JPanel(new GridLayout(3, 3));
		largeGrid.setBounds(squareSize-1,squareSize-1,squareSize*(x-2),squareSize*(y-2));
		largeGrid.setOpaque(false);
		largeGrid.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		
		Color white = new Color(255,255,255, 255);
		for (int i = 0; i < x-2; i++){
			for (int j = 0; j < y-2; j++){
				nums[i][j] = new JTextField();
				nums[i][j].setFont(font);
				nums[i][j].setForeground(Color.BLACK);
				nums[i][j].setBorder(border);
				nums[i][j].setHorizontalAlignment(JTextField.CENTER);
				nums[i][j].setDocument(new JTextFieldLimit(1, false));				
				keyActionTextField(nums[i][j]);
				nums[i][j].addMouseListener(this);
				nums[i][j].setCaretColor(white);
				
				transparentLayer.add(nums[i][j]);
			}
		}
		
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				threeByThreeGrid[i][j] = new JLabel();
				threeByThreeGrid[i][j].setBorder(BorderFactory.createStrokeBorder(new BasicStroke(2.0f)));
				largeGrid.add(threeByThreeGrid[i][j]);
			}
		}
		
		transparentLayer.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		
		layer.add(transparentLayer, new Integer(0));
		layer.add(largeGrid, new Integer(1));
		
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 3;
		panel.add(layer, c);

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
		
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridx = 2;
		c.gridy = 1;
		c.ipady = 10;
		//panel.add(solution, c);
		
		frame = new JFrame("Auto Sudoku");
		frame.setPreferredSize(new Dimension(squareSize*(x)+squareSize/2,squareSize*(y+2)+10));
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBackground(new Color(255,255,255,255));
		//frame.setMinimumSize(new Dimension(550,400));
		frame.setContentPane(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.getRootPane().setDefaultButton(solution);
		//generateSudoku();
		
		frame.setJMenuBar(menuBar);
		
	
		
		
		
		// Print solution (or as far as solver got)
		
//		// Put starting config into nums[][] JTextFields
//		for (int i = 0; i < x-2; i++){
//			for (int j = 0; j < y-2; j++){
//				if( solved_grid[i][j] != 0 ){                      // CHANGE X AND Y COORDS FFS
//					nums[i][j].setText( Integer.toString( solved_grid[i][j])  );
//					nums[i][j].setEditable(false);
//					//nums[i][j].setForeground(new Color( 163 , 194,  163));
//					nums[i][j].setForeground(Color.BLACK);
//					nums[i][j].setHighlighter(null);
//					nums[i][j].setBackground(Color.WHITE);;
//				}
//			}
//		}	
		

//		if(difficulty == 1){
//			initial_config = SudokuMethods.makeEasy( grid );	
//		}
//		else if(difficulty == 2){	
//			initial_config = SudokuMethods.makeMedium( grid );  // Might be ridiculously hard...
//		}	

		
		
		// Put starting config into nums[][] JTextFields
		for (int i = 0; i < x-2; i++){
			for (int j = 0; j < y-2; j++){
				if( initial_config[i][j] != 0 ){                      // TODO: CHANGE THIS 0 THING

					nums[i][j].setEnabled(true);
					nums[i][j].setEditable(false);
					nums[i][j].setBackground(Color.WHITE);
					nums[i][j].setForeground( fixed );
					nums[i][j].setFont(font4);
					nums[i][j].setText( Integer.toString( initial_config[i][j])  );		
					//nums[i][j].setEnabled(false);
				}
				else{
					nums[i][j].setText("");
				}
			}
		}
		

		
		// Count number of starting entries (just to gauge difference between methods)
		int cnt_nums=0;
		for (int i = 0; i < x-2; i++){
			for (int j = 0; j < y-2; j++){
				if( initial_config[i][j] != 0 ){                      // TODO: CHANGE THIS 0 THING
					cnt_nums++;
				}
			}
		}
		
		System.out.println("Number of initial entries = " + cnt_nums);		
		
		
		
	}

	
	
	
	
	
	
	public void actionPerformed(ActionEvent e) {
		//change line below to " ==hint" to implement full answers in sudoku
		
		hintPushed=false;
		
		
		if(e.getSource()==clue){		
			buttonPushed = !buttonPushed;
			
			int count = 0;
			for( int i=0; i<9; i++){
				for( int j=0; j<9; j++ ){
					if (!nums[i][j].getText().equals("") && !nums[i][j].getFont().equals(font4)){
						count++;
					}
				}
			}
			if(count == 0){
				JOptionPane.showMessageDialog(frame, "No Squares Filled!");
				return;
			}
			//sol.frame.setVisible(!sol.frame.isVisible());
			if(buttonPushed){
				clue.setText("Hide");
				//solution.setText("Hide Solution");
				// Highlight incorrect numbers
				for( int i=0; i<9; i++){
					for( int j=0; j<9; j++ ){
						 nums[i][j].setBackground(Color.WHITE);
							if( nums[i][j].isEnabled() && !nums[i][j].getText().equals("")  ){		
								//System.out.println(grid[i][j]);
								System.out.println("nums: " + Integer.parseInt(nums[i][j].getText()));
								System.out.println("init: "+grid2[i][j]);
								if( Integer.parseInt(nums[i][j].getText())== grid2[i][j] && nums[i][j].isEditable()){
									nums[i][j].setBackground( correct );
								}
								else if( Integer.parseInt(nums[i][j].getText())!= grid2[i][j] && nums[i][j].isEditable()){
//									System.out.println("nums: " + Integer.parseInt(nums[i][j].getText()));
//									System.out.println("init: "+initial_config[i][j]);
									nums[i][j].setBackground( wrong );
									nums[i][j].setForeground( new Color(255,255,255) );
								}
							}
					}					
				}		
			}
				
			else{
				//solution.setText("Show Solution");
				clue.setText("Check");
				for (int i = 0; i < x-2; i++){
					for (int j = 0; j < y-2; j++){
						if( nums[i][j].isEditable() ){
							nums[i][j].setBackground(new Color(255,255,255));
							nums[i][j].setForeground(new Color(0,0,0));
						}
						else{
							nums[i][j].setForeground( fixed );
						}
					}
				}
			}	
			
		}
		if(e.getSource()==clickSound){
			System.out.println("ClickSound on");
			//choose clicksound on/ off
		}
		if(e.getSource()==chooseFont){
			System.out.println("Choose Font");
			//choose font submenu
		}
		if(e.getSource()==hint){
			boolean hintAvailable = false;
			if(buttonPushed){
				buttonPushed = !buttonPushed;
				clue.setText("Check");
				for (int row = 0; row < gridsize ; row++) {
					for (int col = 0; col < gridsize ; col++) {	
						 nums[row][col].setForeground(Color.BLACK);
					}
				}
			}
			//we need an array of ints for the hint finding method
			int[][] nummys = new int[9][9];
			for (int plm=0; plm<9; plm++){
				for(int ijn=0; ijn<9; ijn++){
					if( nums[plm][ijn].getText().equals("") ){
						nummys[plm][ijn] = 0;
						hintAvailable = true;
					}
					else{
						nummys[plm][ijn]= Integer.parseInt(   nums[plm][ijn].getText()  );
					}
				}
			}
			//make all white first
			for (int plm=0; plm<9; plm++){
				for(int ijn=0; ijn<9; ijn++){
					nums[ plm ][ ijn ].setBackground(Color.WHITE);				
				}
			}	
			
			if(hintAvailable){
			// Do one step in solving sudoku and pick one that would be entered
			int[] hint_xy= SudokuMethods.getHint_singles_hiddenSingles( nummys  );
			nums[ hint_xy[0] ][ hint_xy[1] ].setBackground( HINT_COLOR );		
			nums[ hint_xy[0] ][ hint_xy[1] ].setCaretColor(HINT_COLOR);
			nums[ hint_xy[0] ][ hint_xy[1] ].requestFocus();
			}
		}
		
		if(e.getSource()==clue){
			System.out.println("clue");
			cluePushed = !cluePushed;
			//show green for correct numbers, red for incorrect
			
		}
		
		if(e.getSource()==save){
			System.out.println("Puzzle Saved.");
			//Save puzzle state
		}
		
		if(e.getSource()==newGame){
			System.out.println("New Game.");
			//load new game
			try {
				frame.dispose();
				sudo = new SudokuGenerator(9, difficulty);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		if(e.getSource()==thick){
			System.out.println("Thick mode enabled");
			difficulty = 1;
//			try {
//				frame.dispose();
//				sudo = new SudokuGenerator(9, sudDiff);
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
		}
		
		if(e.getSource()==normal){
			System.out.println("Normal mode enabled");
			difficulty = 2;
//			try {
//				frame.dispose();
//				sudo = new SudokuGenerator(9, sudDiff);
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
		}
		
		if(e.getSource()==smart){
			System.out.println("Smart mode enabled");
			difficulty = 3;
//			try {
//				frame.dispose();
//				sudo = new SudokuGenerator(9, sudDiff);
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
		}
		
		if(e.getSource()==genius){
			System.out.println("Genius mode enabled");
			difficulty = 4;
//			try {
//				frame.dispose();
//				sudo = new SudokuGenerator(9, sudDiff);
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
		}
		
		if(e.getSource()==exit){
			//System.exit(0);
			frame.dispose();
		}
		
	}
	
	
	
	
	
	
	

	
	


	// KEY ACTIONS FOR MOVING WITH ARROW KEYS -----------------------	

	Action someAction = new AbstractAction() {
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			System.out.println("do some action");
		}
	};


	void keyActionTextField(JTextField l) {

		l.addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent e) {


				for (int row = 0; row < gridsize ; row++) {
					for (int col = 0; col < gridsize ; col++) {	
						if (e.getSource() == nums[row][col]) {
							 nums[row][col].setBackground(Color.WHITE);
							 nums[row][col].setCaretColor(Color.WHITE);
							if (e.getKeyCode() == KeyEvent.VK_UP) {
								int newstart=row;
								if( newstart-1 < 0 ){
									newstart=gridsize;
								}
								nums[ (newstart-1) ][col].requestFocus();
								nums[ newstart-1 ][col].getCaret().setVisible(true);
								nums[newstart-1][col].setBackground(HIGHLIGHT_COLOUR);
								nums[newstart-1][col].setCaretColor(HIGHLIGHT_COLOUR);
							}
							else if (e.getKeyCode() == KeyEvent.VK_DOWN) {		
								
								int newstart=row;
								if( newstart+1 > gridsize-1 ){
									newstart=-1;
								}
								
								nums[ (newstart+1) ][col].requestFocus();
								nums[ newstart+1 ][col].getCaret().setVisible(true);
								nums[ newstart+1][col].setBackground(HIGHLIGHT_COLOUR);
								nums[newstart+1][col].setCaretColor(HIGHLIGHT_COLOUR);
							}
							else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
								
								int newstart=col;
								if( newstart+1 > gridsize-1 ){
									newstart=-1;
								}
								nums[ row ][ newstart+1 ].requestFocus();
								nums[ row ][ newstart+1 ].getCaret().setVisible(true);
								nums[row][ newstart+1].setBackground(HIGHLIGHT_COLOUR);
								nums[row][newstart+1].setCaretColor(HIGHLIGHT_COLOUR);
							}
							else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
								
								 
								
								int newstart=col;
								if( newstart-1 < 0 ){
									newstart=gridsize;
								}
								nums[ row ][ newstart-1 ].requestFocus();
								nums[ row ][ newstart-1 ].getCaret().setVisible(true);
								nums[row][ newstart-1 ].setBackground(HIGHLIGHT_COLOUR);
								nums[row][newstart-1].setCaretColor(HIGHLIGHT_COLOUR);
							}

							else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
								nums[row][col].setText("");	
								nums[row][col].setBackground(HIGHLIGHT_COLOUR);
								nums[row][col].setCaretColor(HIGHLIGHT_COLOUR);
							}
						
							else{
								nums[row][col].setBackground(HIGHLIGHT_COLOUR);
								nums[row][col].setCaretColor(HIGHLIGHT_COLOUR);
							}
						}
					}
				}
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				
				//STEVE: for some reason we can delete non-editable TextFields but can
				//  never add anything back. Easy fix: just redraw the intitial config every time
				//  a key has been pushed
				for (int i = 0; i < x-2; i++){
					for (int j = 0; j < y-2; j++){
						if( initial_config[i][j] != 0 ){
							nums[i][j].setText( Integer.toString(initial_config[i][j]) );
						}
					}
				}
				
				// Check if puzzle is successfully complete
				complete = true;
				for (int row = 0; row < gridsize ; row++) {
					for (int col = 0; col < gridsize ; col++) {	
						if( nums[row][col].getText().isEmpty() ||  (Integer.parseInt(nums[row][col].getText())!=grid2[row][col])     ){
							complete = false;
						}
					}
				}
				if(complete){
					JOptionPane.showMessageDialog(frame, "Congratulations!");
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}


		});
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
		if(buttonPushed){
			buttonPushed = !buttonPushed;
			clue.setText("Check");
			for (int row = 0; row < gridsize ; row++) {
				for (int col = 0; col < gridsize ; col++) {	
					 nums[row][col].setForeground(Color.BLACK);
				}
			}
		}
		for (int row = 0; row < gridsize ; row++) {
			for (int col = 0; col < gridsize ; col++) {	
				 nums[row][col].setBackground(Color.WHITE);

				if (e.getSource() == nums[row][col]) {

					 nums[row][col].setBackground(HIGHLIGHT_COLOUR);
					 nums[row][col].setCaretColor(HIGHLIGHT_COLOUR);
				}
			}
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	
	
	
	
	
	
}
