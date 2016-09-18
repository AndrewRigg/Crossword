import java.io.*;
import java.util.*;

public class WordSearchGenerator{

	int wordsearchSize;
	int gridSize;
	int x;     
	int y;
	String[][] grid;
	ArrayList<String> acrossClues;
	ArrayList<String> downClues;
	ArrayList<Word> words;	
	
	public WordSearchGenerator(int wordsearchSize) throws IOException{
		this.wordsearchSize = wordsearchSize;
		gridSize = wordsearchSize + 2;
		x = gridSize;     
		y = gridSize;
		grid = new String[x][y];
		acrossClues = new ArrayList<String>();
		downClues = new ArrayList<String>();
		ArrayList<Word> words = new ArrayList<Word>();
		words = ReadWords.getWordsandDefs("words_cambridge.txt");		
			grid = new String[x][y];		
			for(int i = 0; i < y; i++){
				for( int j = 0; j < x; j++){
					grid[i][j] = "_";
				}
			}	
			ArrayList<Entry> entries = new ArrayList<Entry>();
			for( int trys=0; trys < 10000; trys++){
				Random r = new Random();
				boolean direction = r.nextBoolean();
				if(!direction){
					new FitWords(grid, x, y, words, entries, "wordsearch", true);
				}
				else{
					new FitWords(grid, x, y, words, entries, "wordsearch", false);
				}
			}
			acrossClues.clear();
			downClues.clear();
			int problemNumber = 0;
			for(int i = 1; i < y-1; i++){
				for(int j = 1; j < x-1; j++){
					boolean twoOnSameSite = false;
					for(int current=0; current <entries.size(); current++){
						if(entries.get(current).getX() == j && entries.get(current).getY() == i){
							if(!twoOnSameSite){problemNumber++;}
							twoOnSameSite=true;
							if(entries.get(current).isAcross()){
								acrossClues.add(Integer.toString(problemNumber) + ". " + entries.get(current).getDefinition());
							}
							else{
								downClues.add(Integer.toString(problemNumber) + ". " + entries.get(current).getDefinition());
							}						
						}
					}				
				}
			}	
		new DrawWordSearch(grid, x, y, acrossClues, downClues, entries);
	}
}
