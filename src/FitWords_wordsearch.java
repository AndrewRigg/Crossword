import java.util.*;
import java.lang.Math;

/**
 * Fitting diagonal words in wordsearch
 * 
 */
public class FitWords_wordsearch {

	//'Random fit approach' to try and fit words ACROSS into grid. Updates ArrayList<Entry> and grid all at once
	public FitWords_wordsearch(String[][] grid, int xLength, int yLength, ArrayList<Word> wordlist, ArrayList<Entry> entries, String dir) {
		
		boolean biggestFirst = false;
		int init_x = (int) (Math.random() * (xLength - 2) + 1);
	    int init_y = (int) (Math.random() * (yLength - 2) + 1);
		
		
			


		//Make sure space on LHS is empty, otherwise do nothing
		String toWorkWith = "";
		if ( dir.equals("across") ) {
			// Max length of word due to grid size 
			int maxPossLength = (xLength - 1) - init_x;
			// Get the configuration of grid sites 'across' from the random initial site
			// Will end looking like:  "_____" or "_B__A___" etc...
			for (int i = 0; i < maxPossLength; i++) {
				toWorkWith = toWorkWith + grid[init_x + i][init_y];			
			}
		} else if( dir.equals("down") ) { 
			int maxPossLength = (yLength - 1) - init_y;
			for (int i = 0; i < maxPossLength; i++) {
				toWorkWith = toWorkWith + grid[init_x][init_y + i];					
			}
		} else if( dir.equals("diagonal") && true ){
			int maxX = (xLength - 1) - init_x;
			int maxY = (yLength - 1) - init_y;
			int maxPossLength = 0;
			if( maxX < maxY ){ maxPossLength = maxX; }
			else{ maxPossLength = maxY; }
			for (int i = 0; i < maxPossLength; i++) {
				toWorkWith = toWorkWith + grid[init_x + i][init_y + i];					
			}
		}

		
		
		
		
		
		// Now need to fit something to the "toWorkWith" String.
		// Uses **SearchWords.java**
		String[] word_and_def = new String[2];
		String word = "";
		String definition = "";

		// Get max possible word length
		int maxWordLength = toWorkWith.length();
		if (maxWordLength > 2 && toWorkWith.contains("_")) {
			
			word_and_def = Search.findWord(wordlist, toWorkWith, biggestFirst);   //new parameter here
			
			word = word_and_def[0];
			definition = word_and_def[1];
		}
		if (!word.equals("")) {

			// Only add entry if it's not been seen before.
			boolean seenBefore = false;
			for (int j = 0; j < entries.size(); j++) {
				if(word.toLowerCase().contains(entries.get(j).getWord().toLowerCase())){
					seenBefore = true;
				}
			}
			if (!seenBefore) {

				// Then fit has been found
				// Need to modify grid
				for (int g = 0; g < word.length(); g++) {
					if ( dir.equals("across") ) {
						grid[init_x + g][init_y] = "" + word.charAt(g);
					} else if( dir.equals("down") ) {
						grid[init_x][init_y + g] = "" + word.charAt(g);
					} else if( dir.equals( "diagonal" ) ){
						grid[init_x + g][init_y + g] = "" + word.charAt(g);						
					}
					
				}

				// Update Entries list
				Entry entry = new Entry(init_x, init_y, true, word, definition); //across boolean not sensible for wordsearches
				entries.add(entry);
			}
		}
		///}Andy bug
	}
}
