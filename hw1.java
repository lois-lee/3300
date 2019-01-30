package hw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.*;

public class Framework {
	int n; // number of applicants (employers)

	int APrefs[][]; // preference list of applicants (n*n)
	int EPrefs[][]; // preference list of employers (n*n)

	ArrayList<MatchedPair> MatchedPairsList; // your output should fill this arraylist which is empty at start

	public class MatchedPair {
		int appl; // applicant's number
		int empl; // employer's number

		public MatchedPair(int Appl, int Empl) {
			appl = Appl;
			empl = Empl;
		}

		public MatchedPair() {
		}
	}

	// reading the input
	void input(String input_name) {
		File file = new File(input_name);
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(file));

			String text = reader.readLine();

			String[] parts = text.split(" ");
			n = Integer.parseInt(parts[0]);

			APrefs = new int[n][n];
			EPrefs = new int[n][n];

			for (int i = 0; i < n; i++) {
				text = reader.readLine();
				String[] aList = text.split(" ");
				for (int j = 0; j < n; j++) {
					APrefs[i][j] = Integer.parseInt(aList[j]);
				}
			}

			for (int i = 0; i < n; i++) {
				text = reader.readLine();
				String[] eList = text.split(" ");
				for (int j = 0; j < n; j++) {
					EPrefs[i][j] = Integer.parseInt(eList[j]);
				}
			}

			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// writing the output
	void output(String output_name) {
		try {
			PrintWriter writer = new PrintWriter(output_name, "UTF-8");

			for (int i = 0; i < MatchedPairsList.size(); i++) {
				writer.println(MatchedPairsList.get(i).empl + " " + MatchedPairsList.get(i).appl);
			}

			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Framework(String[] Args) {
		input(Args[0]);

		MatchedPairsList = new ArrayList<MatchedPair>(); // you should put the final stable matching in this array list

		/*
		 * NOTE if you want to declare that man x and woman y will get matched in the
		 * matching, you can write a code similar to what follows: MatchedPair pair=new
		 * MatchedPair(x,y); MatchedPairsList.add(pair);
		 */

		// YOUR CODE STARTS HERE
		
		
		// Data structures that we need:
		// linked list of freeEmployers
		int n = APrefs.length;
		LinkedList<Integer> freeEmployers = new LinkedList<Integer>();
		// array Next for each employer e where Next[e] gives us the index of the next applicant to propose to
		int[] Next = new int[n];
		// an array Current to check which employer each applicant is engaged to
		int[] Current = new int[n];
		// an nxn array Ranking which has the rankings of the employers for each applicant
		int[][] Ranking = new int[n][n];

		// initialize each data structure:
		for (int i = 0; i < n; i = i+1) {
			freeEmployers.add(i);
			Next[i] = 0;
			Current[i] = -1;
		}
		// preprocessing step
		int temp;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				temp = APrefs[i][j];
				Ranking[i][temp] = j;
			}
		}
		
		// Actual algorithm
		// Initially all e∈E and a∈A are free
		int e;
		int a;
		int e_prime;
		// While there is an employer E who is free and hasn’t proposed to every
		// applicant
		int co = 0;
		while (freeEmployers.isEmpty()== false) {
			while ( Next[freeEmployers.peekFirst()] < n) {
			e = freeEmployers.getFirst(); // Choose such an employer E
			a = EPrefs[e][Next[e]];       // Let a be the highest-ranked woman in e’s preference list to whom e has not
									      // yet proposed
			if (co <=2) {
				System.out.print("a:");
				System.out.println(a);
				System.out.print("e:");
				System.out.println(e);
				co++;
			}
			
			if (Current[a] == -1) {       // If a is free then
				Current[a] = e;           // (e, a) become engaged
				freeEmployers.removeFirstOccurrence(e);  // remove e from list of free freeEmployers
			} 

			else if (Current[a] > -1) {  // Else a is currently engaged to e′
				
				e_prime = Current[a];     // If a prefers e′ to e then e remains free
				
				if (Ranking[a][e_prime] > Ranking[a][e]) { // Else a prefers e to e′ (a, e) become engaged e′
					int size = freeEmployers.size();
					freeEmployers.add(size, e_prime); // free e' and add to list of free employers
					Current[a] = e;                // (a,e) pair
					freeEmployers.removeFirstOccurrence(e);    // remove e
				}
				
				
			}
			Next[e] = Next[e] + 1; 
			if(co<5) {
				System.out.print("next:[");
				for (int i = 0 ; i<n; i++) {
					System.out.print(Next[i]);	
					System.out.print(",");
				}
				System.out.println("]");
				System.out.print("freeEmployers2:[");
				for (int i = 0 ; i<freeEmployers.size(); i++) {
					System.out.print(freeEmployers.get(i));	
					System.out.print(",");
				}
				
				System.out.println("]");
				System.out.print("current:[");
				for (int i = 0 ; i<n; i++) {
					System.out.print(Current[i]);	
					System.out.print(",");
				}
				System.out.println("]");
			}
			

		}
		}
		// Return the set S of engaged pairs
		for (int i = 0; i < Current.length; i++) {
			MatchedPair pair = new MatchedPair(Current[i], i);
			MatchedPairsList.add(pair);
			
		}
		System.out.println (MatchedPairsList);

		// YOUR CODE ENDS HERE

		output(Args[1]);
	}

	public static void main(String[] Args) // Strings in Args are the name of the input file followed by the name of the
											// output file
	{
		new Framework(Args);
	}
}
