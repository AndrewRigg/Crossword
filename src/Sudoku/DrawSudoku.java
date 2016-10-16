package Sudoku;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

/**
 * Class to draw a word search
 */
public class DrawSudoku extends JComponent implements ActionListener {
	private static final long serialVersionUID = 1L;
	private static int squareSize = 30;	
	int x, y;
	JFrame frame;
	JPanel panel, transparentLayer, largeGrid;
	JLayeredPane layer;
	JLabel [][] numbers, threeByThreeGrid;
	int[][] grid;
	String[][] grid2;
	GridBagConstraints c;
	JButton reveal;
	DrawSudokuSolution sol;
	ArrayList<String> fullGrid;
	ArrayList<Integer> row, square, tempColumn, checks;
	ArrayList<ArrayList<Integer>> cols;
	ArrayList<ArrayList<Integer>> boxes;
	Font font, font2;
	Random rand;
	boolean buttonPushed;
	
	//@SuppressWarnings("unchecked")
	public DrawSudoku() throws IOException{
		x = 11;
		y = 11;
		font = new Font("Century Gothic", Font.PLAIN, 30);
		font2 = new Font("Century Gothic", Font.PLAIN, 24);
		//sol = new DrawSudokuSolution(grid, x, y);
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
		threeByThreeGrid = new JLabel [3][3];
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		rand = new Random();
		Border border = BorderFactory.createLineBorder(Color.BLACK);	
		Border border2 = BorderFactory.createLineBorder(Color.BLACK);
		buttonPushed = false;
		
		reveal = new JButton("Show Solution");
		reveal.setFont(font2);
		reveal.setEnabled(true);
		reveal.addActionListener(this);

		transparentLayer = new JPanel(new GridLayout(x-2, y-2));
		transparentLayer.setBounds(squareSize-1,squareSize-1,squareSize*(x-2),squareSize*(y-2));
		transparentLayer.setOpaque(false);
		transparentLayer.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		
		largeGrid = new JPanel(new GridLayout(3, 3));
		largeGrid.setBounds(squareSize-1,squareSize-1,squareSize*(x-2),squareSize*(y-2));
		largeGrid.setOpaque(false);
		largeGrid.setAlignmentX(JComponent.CENTER_ALIGNMENT);

		for (int i = 0; i < x-2; i++){
			for (int j = 0; j < y-2; j++){
				numbers[i][j] = new JLabel();
				numbers[i][j].setFont(font);
				numbers[i][j].setForeground(Color.BLACK);
				numbers[i][j].setBorder(border);
				numbers[i][j].setHorizontalAlignment(JTextField.CENTER);
				numbers[i][j].setVerticalAlignment(JTextField.CENTER);
				transparentLayer.add(numbers[i][j]);
			}
		}
		
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				threeByThreeGrid[i][j] = new JLabel();
				threeByThreeGrid[i][j].setBorder(border2);
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
		panel.add(layer, c);

		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 1;
		c.ipady = 10;
		panel.add(reveal, c);
		
		frame = new JFrame("Auto Sudoku");
		frame.setPreferredSize(new Dimension(squareSize*(x)+squareSize/2,squareSize*(y+2)+10));
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBackground(new Color(255,255,255,255));
		//frame.setMinimumSize(new Dimension(550,400));
		frame.setContentPane(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.getRootPane().setDefaultButton(reveal);
		//generateSudoku();
	}

	//Generates random grid of numbers to build a sudoku puzzle#
		public void generateSudoku(){
			boxes.clear();
			cols.clear();
			row.clear();
			checks.clear();
			for (int i = 0; i < x-2; i++){
				int bound = 9;
				
				ArrayList<Integer> tempRow = new ArrayList<Integer>();
				ArrayList<Integer> tempBox = new ArrayList<Integer>();
				for (int j = 0; j < y-2; j++){
					if(j == 0){
						for(int k = 1; k < 10; k++){
							row.add(k);
							if(i == 0){
								ArrayList<Integer> box = new ArrayList<Integer>();
								boxes.add(box);
								ArrayList<Integer> temps = new ArrayList<Integer>();
								cols.add(temps);
							}
						}
					}
					tempRow.clear();
					tempBox.clear();
					for(Integer a : cols.get(j)){
						if(row.contains(a) && bound > 0){
							tempRow.add(a);
							row.remove(a);
							bound--;
						}
					}
					for(Integer b : boxes.get((i/3)*3+(j/3))){
						if(row.contains(b) && bound > 0){
							tempBox.add(b);
							row.remove(b);
							bound--;
						}
					}
					if(row.isEmpty()){
						if(i != 8 || j != 8){
							System.out.println("got here first");
							//generateSudoku();
						}else{
							System.out.println("got here");
							bound = 100;
							//break;
						}
						System.out.println("Got here");
						//break;
					}
					int temp = (rand.nextInt(bound));
					int insertion = row.get(temp);
					numbers[i][j].setText(""+insertion);
					checks.add(row.get(temp));
					cols.get(j).add(row.get(temp));
					boxes.get(((i/3)*3)+(j/3)).add(row.get(temp));
					row.remove(row.get(temp));
					bound--;
					for(Integer a: tempRow){
						if(!row.contains(a)){
							row.add(a);
						bound++;
						}
					}
					for(Integer b: tempBox){
						if(!row.contains(b)){
							row.add(b);
							bound++;
						}
					}
				}
			}		
		}
	
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==reveal){
			sol.frame.setVisible(!sol.frame.isVisible());
			if(sol.frame.isVisible()){
				reveal.setText("Hide Solution");
				try {
					sol = new DrawSudokuSolution(grid, 11, 11);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
//				for (int i = 0; i < x-2; i++){
//					for (int j = 0; j < y-2; j++){
//						if(numbers[i][j].getText().equals("")){
//							numbers[i][j].setForeground(new Color(0,0,0,255));
//						}
//						else if(numbers[i][j].getText().toLowerCase().equals(grid[j+1][i+1])){
//							numbers[i][j].setForeground(new Color(0,255,0,255));
//						}else{
//							numbers[i][j].setForeground(new Color(255,0,0,255));
//						}
					//}
			//	}
			}
			else{
				reveal.setText("Show Solution");
				for (int i = 0; i < x-2; i++){
					for (int j = 0; j < y-2; j++){
						numbers[i][j].setForeground(new Color(0,0,0,255));
					}
				}
			}
		}
	}
}
