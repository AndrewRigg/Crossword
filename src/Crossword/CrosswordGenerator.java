package Crossword;
import java.io.*;
import java.util.*;

public class CrosswordGenerator{

	int crosswordSize;
	int gridSize;
	int x;     
	int y;
	Random r;
	String[][] grid;
	String[][] gridInit;
	ArrayList<String> acrossClues; 
	ArrayList<String> downClues;
	ArrayList<Word> words;
	ArrayList<Entry> entries;
	boolean connected, direction;	
	DrawCrossword crossword;
	
    int count_fails=0;
	
	public CrosswordGenerator(int crosswordSize) throws IOException{
		this.crosswordSize = crosswordSize;
		gridSize = crosswordSize + 2;
		x = gridSize;     
		y = gridSize;
		grid = new String[x][y];
		gridInit = new String[x][y];
		acrossClues = new ArrayList<String>();
		downClues = new ArrayList<String>();
		words = new ArrayList<Word>();
		words = ReadWords.getWordsandDefs("words_cambridge.txt");
		connected = false;			 
		while(!connected){					
			grid = new String[x][y];
			for(int i = 0; i < y; i++){
				for( int j = 0; j < x; j++){
					grid[i][j] = "_";
				}
			}	
			entries = new ArrayList<Entry>();
			for(int trys=0; trys < 10000; trys++){
				r = new Random();
				direction = r.nextBoolean();
				if(!direction){
					new FitWords_crossword(grid, x, y, words, entries, true);  
					//new FitWords_forceConnection(grid, x, y, words, entries, true);
				}
				else{
					new FitWords_crossword(grid, x, y, words, entries, false);
					//new FitWords_forceConnection(grid, x, y, words, entries, false);
				}
			}
			gridInit = new String[x][y];			
			for(int i=0; i<y; i++){
				for(int j=0; j<x; j++){
					gridInit[i][j] = "";
				}
			}
			acrossClues.clear();
			downClues.clear();
			int problemNumber = 0;
			int entryAcross = 0;
			int entryDown = 0;
			for(int i = 1; i < y-1; i++){
				for(int j = 1; j < x-1; j++){
					boolean twoOnSameSite = false;
					for(int current = 0; current < entries.size(); current++){
						if(entries.get(current).getX() == j && entries.get(current).getY() == i){
							if(!twoOnSameSite){
								problemNumber++;
							}
							twoOnSameSite = true;
							gridInit[j][i] = Integer.toString(problemNumber);
							entries.get(current).setClueNumber(problemNumber);	//add clue number to entry
							if(entries.get(current).isAcross()){
								entries.get(current).definition = Integer.toString(problemNumber) + ".  " + entries.get(current).getDefinition();
								acrossClues.add(entries.get(current).definition);
								entries.get(current).setEntryAcross(entryAcross);
								entryAcross++;
							}
							else{
								entries.get(current).definition = Integer.toString(problemNumber) + ".  " + entries.get(current).getDefinition();
								downClues.add(entries.get(current).definition);
								entries.get(current).setEntryDown(entryDown);
								entryDown++;
							}						
						}
					}				
				}
			}	
			connected = HoshenKopelman.isConnected(grid, x, y);
			//if(!connected){ count_fails++; }
			
		}
		
		//System.out.println( count_fails );
		crossword = new DrawCrossword(gridInit, grid, x, y, acrossClues, downClues, entries);
		
	}
}
