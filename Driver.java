import java.util.Scanner;
/**
Driver class runs the main program, here we get the user inputs to begin the program
*/
public class Driver {
	public static void main(String args[]) {
		//creating required objs
		CellularAutomaton obj;
		Scanner sc = new Scanner(System.in);
		int gSize, min, max, maxG, mod;
		double prob;
		String name;
 
		
		try {
			//getting required user input
			System.out.println("Give file name: ");
			name = sc.nextLine();
			System.out.println("Give grid size: ");
			gSize = sc.nextInt();
			System.out.println("Give initial probability: ");
			prob = sc.nextDouble();
			System.out.println("Give min Neighbours: ");
			min = sc.nextInt();
			System.out.println("Give max Neighbours: ");
			max = sc.nextInt();
			System.out.println("Give max Generations: ");
			maxG = sc.nextInt();
			System.out.println("Give modulus: ");
			mod = sc.nextInt();
			
			
			//setting obj
			obj = new CellularAutomaton(gSize, prob, min, max, maxG, mod, name);
			//obj = new CellularAutomaton(5, .5, 2, 3, 3, 1, name);
			
			
			//creating new gen until false
			while(obj.createNextGeneration()) {
			}
			System.out.println("Done"); //lets you know it executed properly
			
		//catches exception and ends program
		}catch(IllegalArgumentException e) {
			System.out.println("Input does not meet the specifications listed, program ending...");
		}
	}
}
