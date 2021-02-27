import java.io.*;
/**
CellularAutomaton class is the main program and where everything happens. 
*/
public class CellularAutomaton {
	/**
	 * grid for program
	 */
	private boolean[][] grid;
	
	/**
	 * number of minNeighbours required to live
	 */
	private int minNeighbours;
	
	/**
	 * number of maxNeighbours required to live
	 */
	private int maxNeighbours;
	
	/**
	 * current gen count
	 */
	private int currentGeneration;
	
	/**
	 * max gen, so program knows when to end
	 */
	private int maxGenerations;
	
	/**
	 * Must be greater than 0 and less than maxGenerations
	 */
	private int modulus;
	
	/**
	 * holds file path from user
	 */
	private File saveFile;
	
	/**
	 * Alternate Constructor
	 * 
	 * If specifications met, initiates the program
	 * If specification not met, throws IllegalArgumentException
	 * 
	 @param gridSize --> size of grid
	 @param initialProbability --> prob req to create a random grid
	 @param minNeighbours --> minNeighbours required to live 
	 @param maxNeighbours --> maxNeighbours required to live
	 @param maxGenerations --> maximum amount of gens
	 @param modulus --> Must be greater than 0 and less than max gen
	 @param filename --> file name given from user, which is then saved in current directory
	 */
	public CellularAutomaton(int gridSize, double initialProbability, int minNeighbours, int maxNeighbours, int maxGenerations, int modulus, String fileName)throws IllegalArgumentException{
		this.grid = new boolean[gridSize][gridSize];
		this.minNeighbours = minNeighbours;
		this.maxNeighbours = maxNeighbours;
		this.maxGenerations = maxGenerations;
		this.modulus = modulus;
		
		//checks if specifications have been met
		if(this.specifications(initialProbability)) {
			this.saveFile = new File(System.getProperty("user.dir") + fileName); //save the file in the current directory
			this.setupInitialState(initialProbability); //if true, initiate program
		}else {
			throw new IllegalArgumentException(); //if false, throws exception
		}
		
	}
	
	/**
	 * specifications private method
	 *
	 * Specifications that must be met prior to program starting
	 * 
	 @return true or false depending on if specifications met  
	 */
	private boolean specifications(double prob) {
		if((this.grid.length >= 5) && (this.grid.length <= 50)) {
			if(prob >= 0 && prob < 1) {
				if(this.minNeighbours >= 1 && this.minNeighbours <= 2) {
					if(this.maxNeighbours >= 3 && this.maxNeighbours <= 4) {
						if(this.modulus > 0 && this.modulus < this.maxGenerations) {
							return true; //if all specifications met
						}
					}
				}
			}
		}
	    return false; //if even 1 specification not met
	}
	
	/**
	 * setupInitialState private method
	 *
	 * Sets up the initial grid with randomized true and false depending on if random val >= initial prob
	 * Calls save() once initial set it completed
	 */
	private void setupInitialState(double initialProbability) {
		
		for(int i=0; i<this.grid.length; i++) {
			for(int j=0; j<this.grid.length-1; j++) {
				double rand = Math.random(); //rand num between 0 and 1
				
				if(rand >= initialProbability) {
					grid[i][j] = true;
				}else {
					grid[i][j] = false;
				}
			}
		}
		
		//saves initial grid to file
		this.save();
	}
	
	/**
	 * save public method
	 *
	 * Adds the following grid into file 
	 * "*" Represent alive
	 */ 
	public void save() {
		this.currentGeneration++;
		BufferedWriter bw;
		int numOfAlive = 0;
		try {
			//if new file open in overwrite mode
			if (saveFile.createNewFile()) {
				bw = new BufferedWriter(new FileWriter(saveFile.getPath(), false));
			}else {//else opens in append mode
				bw = new BufferedWriter(new FileWriter(saveFile.getPath(), true));
				if(this.currentGeneration == 1) {
					bw.write("\n\n-----------NEW-----------\n\n");   //new run through program
				}													//helps differentiate previous runs
			}
			
			//current gen
			bw.write("Current generation count: " + this.currentGeneration);
			bw.newLine();
			
			//The below is creating the visual on file
			for(int i=0; i<this.grid.length; i++) {
				bw.write("|");
				for(int j=0; j<this.grid[0].length; j++) {
						if(this.grid[i][j]) {
							numOfAlive++;
							bw.write("  *  |");
						}else {
							bw.write("     |");
						}
					}
					bw.newLine();
				}
			bw.newLine(); 
			//num of cells alive
			bw.write("Number of living cells in the current generation: " + numOfAlive);
			bw.newLine();
			bw.newLine();
			bw.close();
			
		}catch(IOException e) {//catch exception
			System.out.println("An error occurred.");
		}
	}
	
	/**
	 * createNextGeneration public method
	 * 
	 * Creates a new gen and calculates which location on grid are alive using shouldBeAlive()
	 @return true or false depending on if current has reached max gens
	 */
	public boolean createNextGeneration() {
		//place holder grid
		boolean[][] newGrid = new boolean[grid.length][grid.length];
		
		for(int i=0; i<newGrid.length; i++) {
			for(int j=0; j<newGrid.length; j++) {
				newGrid[i][j] = this.shouldBeAlive(i,j); //calculates which location on grid are alive
			}
		}
		
		//makes copy of newGrid onto original grid
		for(int i=0; i<newGrid.length; i++) {
			for(int j=0; j<newGrid.length; j++) {
				this.grid[i][j] = newGrid[i][j];
			}
		}
		
		//adds to save file
		if((this.currentGeneration == this.maxGenerations) || ((this.currentGeneration % this.modulus) == 0)) {
			this.save();
		}
		//checks if current gen is still smaller than max gen
		if(this.currentGeneration < this.maxGenerations) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * shouldBeAlive private method
	 *
	 * calculates if gird at [i][j] should be alive or dead
	 *@return true of false depending if location should be alive or dead based on specifications
	 */
	private boolean shouldBeAlive(int i, int j){
		int count = 0; //keeps count of neighbors
		int x1, y1;// placeholder so orig vals do not change
		
		//use 2 for loops to completely wrap around pos [i][j]
		for (int x = i-1; x < i + 2; x++) {
            for (int y = j-1; y < j + 2; y++) {
            	 if ( !(x == i && y == j)) {//position is not itself
            		 if((i+j)%2 == 0 ) {//this determines which of the below alg to use
            			 if((x+y) % 2 != 0) {
            				x1 = x;
 	            			y1 = y;
 	            			
 	            			//checks if out of grid and fixes accordingly
 	            			if(x1 < 0) {
 	            				x1 = grid.length-1;
 	            			}
 	            			else if(x1 >= grid.length) {
 	            				x1 = 0;
 	            			}else if(y1 < 0) {
 	            				y1 = grid.length-1;
 	            			}else if(y1 >= grid.length) {
 	            				y1 = 0;
 	            			}
 	            			//checks if neighbor is alive
 	            			if(this.grid[x1][y1] == true) {  
 		            			count++;//adds neighbor
 	            			}		
            			 }
            		 }else {
            			 if((x+y) % 2 == 0) {
            				x1 = x;
  	            			y1 = y;
  	            			
  	            			//checks if out of grid and fixes accordingly
  	            			if(x1 < 0) {
  	            				x1 = grid.length-1;
  	            			}
  	            			else if(x1 >= grid.length) {
  	            				x1 = 0;
  	            			}else if(y1 < 0) {
  	            				y1 = grid.length-1;
  	            			}else if(y1 >= grid.length) {
  	            				y1 = 0;
  	            			}
  	            			//checks if neighbor is alive
  	            			if(this.grid[x1][y1] == true) {  
  	            				count++; //adds neighbor
  	            			}		
            			 }
            		 }
            	}
            }
		}
		
		
		//if specifications met, returns true
		if((count >= this.minNeighbours) && (count <= this.maxNeighbours)) {
			return true;
		}
		return false;
	}
	
}
