package ie.gmit.sw;

import java.util.Scanner;

/**
 * Generates a User interface where the user can enter the file names, shingle size, k and thread pool size.
 * 
 * @author Donal Burke
 * @version 1.0 
 *
 */

public class Menu {
	
	private Scanner console = new Scanner(System.in);
	
	public Menu() throws InterruptedException {
		
		System.out.println("Enter in:\n\t1) to enter document names:\n\t2) to exit program\n");
		int input = console.nextInt();
		
		if(input == 1) {
			System.out.println("Enter file name 1: ");
			String f1 = console.next();
			
			System.out.println("Enter file name 2: ");
			String f2 = console.next();
			
			System.out.println("Enter Shingle size: ");
			int shingleSize = console.nextInt();
			
			System.out.println("Enter k size: ");
			int k = console.nextInt();
			
			System.out.println("Enter pool size: ");
			int poolSize = console.nextInt();
			
			new Launcher().Launch(f1, f2, shingleSize, k, poolSize);
		} else {
			System.exit(0);
		}
		
	}
	
}
