package sudoku;
import java.io.*;
import java.util.*;

import javax.swing.JLabel;

public class SudokuGenerator{
	ArrayList<Integer> row, square, tempColumn, checks;
	ArrayList<ArrayList<Integer>> boxes, cols, fullGrid, puzzleGrid;
	int size;
	int gridSize;
	int x;     
	int y;
	int difficulty;
	int[][] grid;
	Random rand;
	JLabel [][] numbers;
	
	

	public SudokuGenerator(int difficulty)throws IOException{}
		


	public SudokuGenerator(int size, int difficulty)throws IOException{

		row = new ArrayList<Integer>();
		cols = new ArrayList<ArrayList<Integer>>();
		boxes = new ArrayList<ArrayList<Integer>>();
		square = new ArrayList<Integer>();
		tempColumn = new ArrayList<Integer>();
		checks = new ArrayList<Integer>();
		fullGrid = new ArrayList<ArrayList<Integer>>();
		puzzleGrid = new ArrayList<ArrayList<Integer>>();
		this.difficulty = difficulty;
		

		gridSize=9+2;
		
		x = gridSize;     
		y = gridSize;
		
		
		grid = new int[x][y];		
		for(int i = 0; i < y; i++){
			for( int j = 0; j < x; j++){
				grid[i][j] = 0;
			}
		}

		//generateSudoku();
		//removeNumbers(fullGrid);
		//System.out.println(fullGrid.toString());
		new DrawSudoku(grid, x, y, difficulty);

		
		
		// Solution and start grid made in here.
		//new DrawSudoku(difficulty);
		
		
	}

	
	
	
	
	
	
	
//	public void removeNumbers(ArrayList<ArrayList<Integer>> fullGrid) {
//		for(ArrayList<Integer> arrayList: fullGrid){
//			for(Integer a: arrayList){
//				//this will contain algorithms to systematically remove numbers from grid
//				//will temporary remove even numbers
//				if(a%2 == 0){
//					numbers[fullGrid.indexOf(arrayList)][arrayList.indexOf(a)].setText("");
//				}
//			}
//		}
//		
//	}


	
	
}
