package sudoku;
import java.util.ArrayList;


public class SudokuMethods {
	/**
	 * Class to generate sudoku starting configurations from a complete grid
	 * Different difficulty levels 
	 */
	
	//TODO: - tidy code
	//      - general style: rounded corners, different colours, ...
	//
	// - plot # starting entries for different methods. Any stat phys in there?
	// - (stat phys for crosswords - i.e. english language percolation! / word occurrences on bbc website / ... MAKE WEBSITE)
	
	
	public static int gridSize = 9;
	
	static int last_rand_pos=0;
	
	
	
	public static int[][] makeMedium(int[][] fullGrid){   // As hard as possible without guessing
		/**
		 *  Method just solves the full grid every time a number is removed.
		 *  To do this, it lists all possible numbers that can go at each site. If there is only 1, we add
		 *  it to grid (for all if multiple single choices), update the lists then repeat until no more can be added.
		 */
		
		int[][] startGrid = new int[9][9];
		for( int i=0; i<9; i++ ){
			for( int j=0; j<9; j++){
				startGrid[i][j] = fullGrid[i][j];
			}
		}	
	
		
		// Remove numbers. Do this a set number of times / until no more can be removed 
		for( int tries=0; tries<200; tries++ ){ // REDUCED TO 200 SINCE THIS METHOD IS NOW VERY SLOW
			
			// pick random space and remove      
			int xp=(int)(Math.random()*gridSize);  
			int yp=(int)(Math.random()*gridSize); 
			
			int value_removed = startGrid[xp][yp];
			startGrid[xp][yp]=0;                    /// TODO: CAREFUL!!!

			// Try to solve
			int[][] solved_config = new int[9][9];
			solved_config = SudokuMethods.solver_singles_hiddenSingles(  startGrid  );
			
			// Check if solved
			boolean is_solved = isSolved( solved_config );
			
			if( is_solved ){
				// remove number and re-enter loop to pick another
			}
			else{
				// put number back and try again
				startGrid[xp][yp] = value_removed;
			}
		
		}

		return startGrid;
	}
	
	
	
	
	
	
	
	
	public static int[][] makeEasy(int[][] fullGrid){   //  MAYBE "VERRRRY" EASY
		/**
		 * Easy sudoku makes sure that at every step (removal) the grid can still be solved
		 * by just looking at the row, column and square and checking if there is exactly
		 * one possible entry 
		 */
		
		int[][] startGrid = new int[9][9];
		
		for( int i=0; i<9; i++ ){
			for( int j=0; j<9; j++){
				startGrid[i][j] = fullGrid[i][j];
			}
		}	
	
		
		// Do this a set number of times / until no more can be removed / specific number of entries are left??
		
		for( int tries=0; tries<100000; tries++ ){
		
			// pick random space and remove            //TODO: do all similar numbers instead of random??
			int xp=(int)(Math.random()*gridSize);  
			int yp=(int)(Math.random()*gridSize); 
			
			int value_removed = startGrid[xp][yp];
			startGrid[xp][yp]=0;                    /// TODO: CAREFUL, this is not printed since it is not in set [1,9] 

			// Get lists of possibilities according to row, column and 3x3 square		
			ArrayList<Integer> poss_row = possibilities_from_row( startGrid, xp, yp );
			ArrayList<Integer> poss_col = possibilities_from_col( startGrid, xp, yp );
			ArrayList<Integer> poss_sqr = possibilities_from_sqr( startGrid, xp, yp );			
			
			// Take the 3 lists above and check for common numbers.
			//ArrayList<Integer> common_nums = new ArrayList<Integer>();
			int count_cns=0;
			for( Integer i: poss_row ){
				if( poss_col.contains(i) && poss_sqr.contains(i) ){
					//common_nums.add(i);
					count_cns++;
				}
			}
			
			if( count_cns != 1 ){
				// cannot solve sudoku with "easy" method so put number back
				startGrid[xp][yp] = value_removed;
			}
			else if( count_cns ==0 ){
				System.out.println("SOMETHING ODD - NO SHARED NUMBERS AFTER REMOVAL. SudokuMethods.java ");
			}
			else{
				// We have exactly 1 possibility for this square. Leave it out and pick another!
			}
		
		}

		return startGrid;
	}
	
	
	
	
//	public static int[][] makeEasy(int[][] fullGrid){   //  MAYBE "VERRRRY" EASY
//		/**
//		 * Easy sudoku makes sure that at every step (removal) the grid can still be solved
//		 * by just looking at the row, column and square and checking if there is exactly
//		 * one possible entry 
//		 */
//		
//		int[][] startGrid = new int[9][9];
//		
//		for( int i=0; i<9; i++ ){
//			for( int j=0; j<9; j++){
//				startGrid[i][j] = fullGrid[i][j];
//			}
//		}	
//	
//		
//		// Do this a set number of times / until no more can be removed / specific number of entries are left??
//		
//		for( int tries=0; tries<100000; tries++ ){
//		
//			// pick random space and remove            //TODO: do all similar numbers instead of random??
//			int xp=(int)(Math.random()*gridSize);  
//			int yp=(int)(Math.random()*gridSize); 
//			
//			int value_removed = startGrid[xp][yp];
//			startGrid[xp][yp]=0;                    /// TODO: CAREFUL, this is not printed since it is not in set [1,9] 
//
//			// Get lists of possibilities according to row, column and 3x3 square		
//			ArrayList<Integer> poss_row = possibilities_from_row( startGrid, xp, yp );
//			ArrayList<Integer> poss_col = possibilities_from_col( startGrid, xp, yp );
//			ArrayList<Integer> poss_sqr = possibilities_from_sqr( startGrid, xp, yp );			
//			
//			// Take the 3 lists above and check for common numbers.
//			//ArrayList<Integer> common_nums = new ArrayList<Integer>();
//			int count_cns=0;
//			for( Integer i: poss_row ){
//				if( poss_col.contains(i) && poss_sqr.contains(i) ){
//					//common_nums.add(i);
//					count_cns++;
//				}
//			}
//			
//			if( count_cns != 1 ){
//				// cannot solve sudoku with "easy" method so put number back
//				startGrid[xp][yp] = value_removed;
//			}
//			else if( count_cns ==0 ){
//				System.out.println("SOMETHING ODD - NO SHARED NUMBERS AFTER REMOVAL. SudokuMethods.java ");
//			}
//			else{
//				// We have exactly 1 possibility for this square. Leave it out and pick another!
//			}
//		
//		}
//
//		return startGrid;
//	}
	
	
	
	
	
	


	
	
	
	
	

	
	
	public static int[][] solver_singles_hiddenSingles( int[][] strtGrid ){
		/**
		 * Solves sudoku using only "singles" and "hidden singles".  
		 * (hidden: there may be 2 or more options in every grid in a given row, but 
		 * if only one of them contains a "4" option, this must be the 4)
		 */
		int[][] solvegrid = new int[9][9];
		for( int i=0; i<9; i++ ){
			for( int j=0; j<9; j++){
				solvegrid[i][j] = strtGrid[i][j];
			}
		}		
		
		boolean solving = true;
				
		while(solving){
			
			
			// HOLD CURRENT GRID TO CHECK FOR DIFFERENCES
			int[][] prev_grid = new int[9][9];
			for( int i=0; i<9; i++ ){
				for( int j=0; j<9; j++){
					prev_grid[i][j] = solvegrid[i][j];
				}
			}	
			
			
			// Add singles
			add_singles( solvegrid );
			
			// Add hidden singles 
			add_hiddenSingles_ROWS( solvegrid );
		    add_hiddenSingles_COLS( solvegrid );
			add_hiddenSingles_3x3s( solvegrid );
			
						
			// check we made at least 1 change
			boolean anything_added = false;
			for( int iii=0; iii<9; iii++){
				for( int jjj=0; jjj<9; jjj++ ){
					if ( solvegrid[iii][jjj] != 0  &&  prev_grid[iii][jjj] == 0 ){
						anything_added = true;
					}
				}
			}
			if( !anything_added){
				solving=false;
			}
			
			
		}//end while
		

		return solvegrid;
	}
	

	
	
	
	
	
	public static int[] getHint_singles_hiddenSingles( int[][] strtGrid ){
		/**
		 * Uses solver_noGuessing to get a number that is definitely fillable as a hint
		 * EITHER as a single, or as a hidden single
		 */
		
		
		int[] hint_xy = new int[2];
		ArrayList<Integer> x_coords = new ArrayList<Integer>();
		ArrayList<Integer> y_coords = new ArrayList<Integer>();

		int[][] solvegrid = new int[9][9];
		for( int i=0; i<9; i++ ){
			for( int j=0; j<9; j++){
				solvegrid[i][j] = strtGrid[i][j];
			}
		}	
		
		// First, look for SINGLES
		// add from checking rows
		add_singles( solvegrid );
		//check for any changes			
		for( int iii=0; iii<9; iii++){
			for( int jjj=0; jjj<9; jjj++ ){
				if ( solvegrid[iii][jjj] != 0  &&  strtGrid[iii][jjj] == 0 ){
					x_coords.add(iii);   y_coords.add(jjj);
				}
			}
		}
		
		
			
		// HIDDEN SINGLES
		// Reset solvegrid[][] since we only want 1 "move" in the solver
		for( int i=0; i<9; i++ ){
			for( int j=0; j<9; j++){
				solvegrid[i][j] = strtGrid[i][j];
			}
		}	
		// add from checking rows
		add_hiddenSingles_ROWS( solvegrid );
		//check for any changes			
		for( int iii=0; iii<9; iii++){
			for( int jjj=0; jjj<9; jjj++ ){
				if ( solvegrid[iii][jjj] != 0  &&  strtGrid[iii][jjj] == 0 ){
					x_coords.add(iii);   y_coords.add(jjj);
				}
			}
		}
			
		
		
		// SAME AGAIN but for COLS
		for( int i=0; i<9; i++ ){
			for( int j=0; j<9; j++){
				solvegrid[i][j] = strtGrid[i][j];
			}
		}		
		// add from checking rows
		add_hiddenSingles_COLS( solvegrid );
		//check for any changes			
		for( int iii=0; iii<9; iii++){
			for( int jjj=0; jjj<9; jjj++ ){
				if ( solvegrid[iii][jjj] != 0  &&  strtGrid[iii][jjj] == 0 ){
					x_coords.add(iii);   y_coords.add(jjj);
				}
			}
		}
			
	
		
		// SAME AGAIN but for 3x3 squares
		for( int i=0; i<9; i++ ){
			for( int j=0; j<9; j++){
				solvegrid[i][j] = strtGrid[i][j];
			}
		}				
		// add from checking rows
		add_hiddenSingles_3x3s( solvegrid );
		//check for any changes			
		for( int iii=0; iii<9; iii++){
			for( int jjj=0; jjj<9; jjj++ ){
				if ( solvegrid[iii][jjj] != 0  &&  strtGrid[iii][jjj] == 0 ){
					x_coords.add(iii);   y_coords.add(jjj);
				}
			}
		}
		
		
		// List will contain multiple entries if row/col/3x3 have produced same possible entry
		// Remove these
		if(  x_coords.size()==y_coords.size()    ){
			for( int g=x_coords.size()-1; g>=0; g-- ){
				int xx=x_coords.get(g);  int yy=y_coords.get(g);
				for( int h=0; h<g; h++ ){
					if( x_coords.get(h)==xx  &&  y_coords.get(h)==yy  ){
						//remove them
						x_coords.remove(h);   y_coords.remove(h); 
						break; // since we need to update array sizes before continuing loop
					}
				}
			}
		}else{
			System.out.println("zz x_xcoords != y cords");
		}

		
		// Choose one of the possible hints randomly but avoid the last one chosen
		if( x_coords.size() == y_coords.size()  ){
			boolean new_pos=false;
			while(!new_pos){
				int pos = (int)( Math.random()*x_coords.size()  );
				if( x_coords.size()>1  && pos!=last_rand_pos){
					hint_xy[0]=x_coords.get(pos);   hint_xy[1]=y_coords.get(pos);
					last_rand_pos = pos;
					new_pos=true;
				}
				else if( x_coords.size()==1  ){
					hint_xy[0]=x_coords.get(0);   hint_xy[1]=y_coords.get(0);
					new_pos=true;
				}
				else if( x_coords.size()==0  ){
					//no hint available from this method - could be the case for harder sudoku puzzles
					
					System.out.println("No hints available for this method. DEAL WITH THIS");  //TODO
					return null;
				}
			}		
		}
		else{
			System.out.println("x_coords != y_coords length");
		}
		
		
		return hint_xy;			
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void add_singles(  int[][] grid ){
		
		// Lists of all possible numbers for each grid point
		@SuppressWarnings("unchecked")
		ArrayList<Integer>[][] all_lists =  (ArrayList<Integer>[][])new ArrayList[9][9];
		for( int iii=0; iii<9; iii++){
			for( int jjj=0; jjj<9; jjj++ ){
				all_lists[iii][jjj] = new ArrayList<Integer>();
			}
		}	
		
		// Update all grid possibilities
		for( int iii=0; iii<9; iii++){
			for( int jjj=0; jjj<9; jjj++ ){
				if( grid[iii][jjj] == 0 ){   // Do not get possibilities from occupied sites
					all_lists[iii][jjj] = getGridPossibilities( grid, iii, jjj );
				}
			}
		}	
		
		// Add any singles
		for( int iii=0; iii<9; iii++){
			for( int jjj=0; jjj<9; jjj++ ){
				if( grid[iii][jjj]==0  &&  all_lists[iii][jjj].size() == 1 ){   // Do not get possibilities from occupied sites					
					grid[iii][jjj] = all_lists[iii][jjj].get(0);				
				}
			}
		}	

	}
	
	
	

	
	
	
	
	
	
	public static void add_hiddenSingles_ROWS( int[][] grid ){
		/**
		 * Use all grid possibilities for all grids for each ROW
		 * Make a "row_required" list; check if any grids only have 1 of these
		 */
		// Lists of all possible numbers for each grid point
		@SuppressWarnings("unchecked")
		ArrayList<Integer>[][] all_lists =  (ArrayList<Integer>[][])new ArrayList[9][9];
		for( int iii=0; iii<9; iii++){
			for( int jjj=0; jjj<9; jjj++ ){
				all_lists[iii][jjj] = new ArrayList<Integer>();
			}
		}	

		
		
		//for each row
		for( int i=0; i<9; i++){
			
			

			// Update all grid possibilities
			for( int iii=0; iii<9; iii++){
				for( int jjj=0; jjj<9; jjj++ ){
					if( grid[iii][jjj] == 0 ){   // Do not get possibilities from occupied sites
						all_lists[iii][jjj] = getGridPossibilities( grid, iii, jjj );
					}
				}
			}		
			
			 
			// make list of numbers this row needs
			ArrayList<Integer> required = new ArrayList<Integer>(); /// TODO: WE CNA MAKE THIS REQUIRED LIST MUCH EASIER!
			for( int j=0; j<9; j++ ){
	
				for( Integer poss: all_lists[i][j] ){
					if( !required.contains(poss) ){
						required.add( poss );
					}
				}
			}
			
			
			// now check how many squares each possibility appears in
			for( Integer r: required){
				
				int count_r=0;
				for( int j=0; j<9; j++ ){
					if( all_lists[i][j].contains( r ) ){  count_r++; }
				}
				if( count_r==1 ){
					// then we know we can add this number. find out where:
					for( int j=0; j<9; j++ ){
						if( all_lists[i][j].contains( r ) ){  
							// add in the definite entry!
							
							grid[i][j] = r; 

						}
					}
					
				}
				
			}
			
					
		}//end row
		

			
	}
	
	
	
	
	
	public static void add_hiddenSingles_COLS( int[][] grid ){
		/**
		 * Use all grid possibilities for all grids for each COLUMN
		 * Make a "col_required" list; check if any grids only have 1 of these
		 */
		// Lists of all possible numbers for each grid point
		ArrayList<Integer>[][] all_lists =  (ArrayList<Integer>[][])new ArrayList[9][9];
		for( int i=0; i<9; i++){
			for( int j=0; j<9; j++ ){
				all_lists[i][j] = new ArrayList<Integer>(); // just initialize empty arraylist
			}
		}		
		
		
		//for each COLUMN
		for( int j=0; j<9; j++){
			
			// Update all grid possibilities
			for( int iii=0; iii<9; iii++){
				for( int jjj=0; jjj<9; jjj++ ){
					if( grid[iii][jjj] == 0 ){   // Do not get possibilities from occupied sites
						all_lists[iii][jjj] = getGridPossibilities( grid, iii, jjj );
					}
				}
			}			
			
			
			// make list of number this row needs
			ArrayList<Integer> required = new ArrayList<Integer>();
			for( int i=0; i<9; i++ ){
				for( Integer poss: all_lists[i][j] ){
					if( !required.contains(poss) ){
						required.add( poss );
					}
				}
			}
			
			
			// now check how many squares each possibility appears in
			for( Integer r: required){
				
				int count_r=0;
				for( int i=0; i<9; i++ ){
					if( all_lists[i][j].contains( r ) ){  count_r++; }
				}
				if( count_r==1 ){
					// then we know we can add this number. find out where:
					for( int i=0; i<9; i++ ){
						if( all_lists[i][j].contains( r ) ){  
							// add in the definite entry!
							grid[i][j] = r;
						}
					}
					
				}
				
			}
		

			
		}
		
	}
	
	
	

	
	

	
	
	
	
	
	
	public static void add_hiddenSingles_3x3s( int[][] grid ){
		/**
		 * Use all grid possibilities for all grids for each 3X3 GRID
		 * Make a "3x3_required" list; check if any grids only have 1 of these
		 */
		// Lists of all possible numbers for each grid point
		ArrayList<Integer>[][] all_lists =  (ArrayList<Integer>[][])new ArrayList[9][9];
		for( int i=0; i<9; i++){
			for( int j=0; j<9; j++ ){
				// initialize empty
				all_lists[i][j] = new ArrayList<Integer>();
			}
		}		

		
		
		//for each 3x3 grid!  Layout is: top left 3x3 is called 0, top middle, 1 and so on.
		for( int sq=0; sq<9; sq++){
			
			// Update all grid possibilities
			for( int iii=0; iii<9; iii++){
				for( int jjj=0; jjj<9; jjj++ ){
					if( grid[iii][jjj] == 0 ){   // Do not get possibilities from occupied sites
						all_lists[iii][jjj] = getGridPossibilities( grid, iii, jjj );
					}
				}
			}			
			
			
			int x_strt = (sq%3)*3;
			int y_strt = (sq/3)*3;
			
			
						
			// make list of number this 3x3 grid needs
			ArrayList<Integer> required = new ArrayList<Integer>();
			for( int i=x_strt; i<3; i++ ){
				for( int j=y_strt; j<3; j++){
				
					for( Integer poss : all_lists[i][j] ){
						if( !required.contains(poss) ){
							required.add( poss );
						}
					}
				
				}
			}
			
			
			
			// now check how many squares each possibility appears in
			for( Integer r: required){
				
				int count_r=0;
				for( int i=x_strt; i<3; i++ ){
					for( int j=y_strt; j<3; j++ ){
						if( all_lists[i][j].contains( r ) ){  count_r++; }
					}
				}
				if( count_r==1 ){
					// then we know we can add this number. find out where:
					for( int i=x_strt; i<3; i++ ){
						for( int j=y_strt; j<3; j++){
							if( all_lists[i][j].contains( r ) ){  
								// add in the definite entry!
								grid[i][j] = r;
							}
						}
					}
					
				}
				
			}

			
		}
			
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	@SuppressWarnings("unchecked") // WTF IS THIS!? Need it for our array[][] of ArrayLists... possible bugs!? //TODO
//	public static int[][] solver_basic( int[][] strtGrid ){
//		/**
//		 * Method to solve (??) sudokus by writing out every possible number for each
//		 * grid space and checking if there is exactly 1 possibility for any of them.
//		 * If so, add them, update grid lists, look again. 
//		 */
//		int[][] solvegrid = new int[9][9];
//		for( int i=0; i<9; i++ ){
//			for( int j=0; j<9; j++){
//				solvegrid[i][j] = strtGrid[i][j];
//			}
//		}	
//		
//		
//
//		// Lists of all possible numbers for each grid point
//		ArrayList<Integer>[][] all_lists =  (ArrayList<Integer>[][])new ArrayList[9][9];
//		for( int i=0; i<9; i++){
//			for( int j=0; j<9; j++ ){
//				all_lists[i][j] = new ArrayList<Integer>();
//			}
//		}
//		
//		
//		
//		
//		boolean solving = true;
//		while( solving ){
//		
//			// Update all_lists
//			for( int i=0; i<9; i++){
//				for( int j=0; j<9; j++ ){
//					if(solvegrid[i][j] == 0 ){   // Do not get possibilities from occupied sites
//						all_lists[i][j] = getGridPossibilities( solvegrid, i, j );
//					}
//				}
//			}
//			
//			// Check we can actually add something
//			boolean anything_to_add = false;
//			for( int i=0; i<9; i++){
//				for( int j=0; j<9; j++ ){
//					if(  all_lists[i][j].size() == 1  ){
//						anything_to_add = true;
//					}					
//				}
//			}
//			// Stop if we can't add any more
//			if( !anything_to_add ){ solving = false; }   
//			
//			
//			// Add any definite numbers
//			for( int i=0; i<9; i++){
//				for( int j=0; j<9; j++ ){
//					if(  all_lists[i][j].size() == 1  ){
//						int n = all_lists[i][j].get(0);
//						solvegrid[i][j] = n;
//						// clear the corresponding list
//						all_lists[i][j].clear();
//					}
//				}
//			}
//
//		}
//        
//		
//		return solvegrid;
//	}
//	
	
	
	
	
	
	
	
	
	
	public static ArrayList<Integer> getGridPossibilities( int[][] strt_grid, int xxx, int yyy ){
		/**
		 * Method to produce list of possible numbers for single grid space
		 */
		ArrayList<Integer> ents = new ArrayList<Integer>();
		
		// Get lists of possibilities according to row, column and 3x3 square		
		ArrayList<Integer> poss_row = possibilities_from_row( strt_grid, xxx, yyy );
		ArrayList<Integer> poss_col = possibilities_from_col( strt_grid, xxx, yyy);
		ArrayList<Integer> poss_sqr = possibilities_from_sqr( strt_grid, xxx, yyy );			
		
		// Take the 3 lists above and check for common numbers.
		for( Integer i: poss_row ){
			if( poss_col.contains(i) && poss_sqr.contains(i) ){     // STEVE TRYING TO DEBUG
			//if( poss_col.contains(i) && poss_sqr.contains(i) ){

				ents.add(i);
			}
		}				
		
		
		return ents;
	}
	
	
	
	
	
	
	
	
	
	public static ArrayList<Integer> possibilities_from_sqr( int[][] strt_grid, int xx, int yy ){
		ArrayList<Integer> poss = new ArrayList<Integer>();		
		for(int i=1; i<gridSize+1; i++){
			poss.add( i );
		}
		
		//Get top left of current 3x3 square   // TODO: GENERALISE FOR NON-3X3 GRIDS
		int tl_x = (xx/3)*3;
		int tl_y = (yy/3)*3;		
		
		for( int i=0; i< 3 ; i++ ){    // TODO: GENERALISE FOR NON-3X3 GRIDS
			for( int j=0; j<3; j++ ){
				
				if( poss.contains( strt_grid[tl_x+i][tl_y+j] ) ){
					// then find it in poss and remove it
					int position = poss.indexOf( strt_grid[tl_x+i][tl_y+j]  );
					poss.remove( position );
				}
				
			}
		}
		
		return poss;
	}
	
	
	
	
	
	
	public static ArrayList<Integer> possibilities_from_row( int[][] strt_grid, int xx, int yy ){
		
		ArrayList<Integer> poss = new ArrayList<Integer>();		
		for(int i=1; i<gridSize+1; i++){
			poss.add( i );
		}
		
		for(int i=0; i<gridSize; i++){   //
			if( poss.contains( strt_grid[i][yy] ) ){
				// then find it in poss and remove it
				int position = poss.indexOf( strt_grid[i][yy]  );
				poss.remove( position );
			}
		}
		
		return poss;
	}
	
	
	
	
	
	public static ArrayList<Integer> possibilities_from_col( int[][] strt_grid, int xx, int yy ){
		
		ArrayList<Integer> poss = new ArrayList<Integer>();		
		for(int i=1; i<gridSize+1; i++){
			poss.add( i );
		}
		
		for(int i=0; i<gridSize; i++){
			if( poss.contains( strt_grid[xx][i] ) ){
				// then find it in poss and remove it
				int position = poss.indexOf( strt_grid[xx][i]  );
				poss.remove( position );
			}
		}
		
		return poss;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static boolean isSolved( int[][] grid ){  
		/**
		 * Check if grid is solved
		 */
		boolean solved = true;
		
		int desired_tot = 1+2+3+4+5+6+7+8+9;
		
		for( int i=0; i<9; i++ ){
			
			int tot=0;
			// check each row
			for( int j=0; j<9; j++){
				tot = tot + grid[i][j];
			}
			if( tot != desired_tot ){
				solved = false;
			}
			
			tot=0;
			// check each col
			for( int j=0; j<9; j++){
				tot = tot + grid[j][i];
			}
			if( tot != desired_tot ){
				solved = false;
			}
			
			// no need to check 3x3 grids...
		}

		
		return solved;
	}
	
	
	
	
	

	
	public static boolean isFull( int[][] grid ){  
		/**
		 * Check if grid is full
		 */
		boolean solved = true;
		
		for( int i=0; i<9; i++ ){
			for( int j=0; j<9; j++){
				if( grid[i][j] == 0 ){  // 0 entries signify no number //TODO
					solved = false;
				}
			}
		}
		
		return solved;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
