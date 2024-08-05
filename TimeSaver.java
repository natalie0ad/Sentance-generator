package comprehensive;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class to manage many ProbabilityMap objects created from an input file.
 * 
 * @author Harrison LeTourneau and Natalie DeSimone
 * @version 4/21/24
 */
public class TimeSaver {
	
	// Create a Pattern to check against each String in the input file
	private static final Pattern formatPattern = Pattern.compile("[a-zA-Z_0-9]+");
	
	// Create a HashMap to hold each String and it's ProbabilityMap
	private HashMap<String, ProbabilityMap> timeSaver;
	
	/**
	 * Constructs a new TimeSaver, which holds a new PriorityQueue for each word
	 * in the input file.
	 * 
	 * @param filename - the input file to parse
	 */
	public TimeSaver(String filename) {
		
		// Attempt to parse through input file
		try {
			parseFile(filename);
		}
		catch(IOException e) {
			System.out.println("File path invalid, please try again.");
		}
				
	}
	
	/**
	 * Prints the K next most probable elements following the seed word, either
	 * selecting the 'one' most probable word to follow the word or selecting 
	 * the next word, random by probability, from 'all' next possible words.
	 * Either input, 'one' or 'all', represents a Markov Chain.
	 * 
	 * @param filename - the input file to parse
	 * @param seed - the first word of the resulting Markov Chain
	 * @param k - the k next words to return
	 * @param oneOrAll - String 'one' or 'all', determining the behaviour of the
	 * 					 Markov Chain 
	 * @return a String representing the Markov Chain
	 */
	public String printK(String seed, int k, String oneOrAll) {
		if(oneOrAll.equals("one")) 
			return one(seed, k);
		else
			return all(seed, k);
	}
	
	// Private helper methods for this class
	
	/**
	 * Parses through the input file. Creates a new ProbabilityMap for each word.
	 * 
	 * @param filename - the input file to parse
	 * @throws IOException if file name or path is invalid
	 */
	private void parseFile(String filename) throws IOException{
		
		timeSaver = new HashMap<String, ProbabilityMap>();
				
		String thisWord = null;
		String nextWord;
		String line;
		String[] lineReader = null;
		boolean cont = false;
		int i = 0;
		
		// Wrap FileReader with BufferedReader to parse file
		BufferedReader fileReader = new BufferedReader(new FileReader(filename));

		// Check file is not empty, begin first line
		if((line = fileReader.readLine().trim()) != null) {
			// Words split by whitespace
			lineReader= line.split("\\s+");
			thisWord = format(lineReader[0]);
			i = 1;
			cont = true;
		}
		
		while(true) {
			if(cont){
				// Parse through each line object
				while(i < lineReader.length) {
					nextWord = format(lineReader[i]);
					timeSaver.putIfAbsent(thisWord, new ProbabilityMap());
					timeSaver.get(thisWord).addMapping(nextWord);
					thisWord = nextWord;
					i++;
				}
			}
			i = 0;
			
			// Check for next line
			if((line = fileReader.readLine()) == null)
				break;
			line = line.trim();
			
			// Handle blank lines (whitespace lines)
			if(line.length() > 0) {
				lineReader = line.split("\\s+");
				cont = true;
			}
			else
				cont = false;

		}
		
		fileReader.close();
	}
	
	/**
	 * Private helper method for 'one' version of printK's Markov Chain.
	 * 
	 * @param seed - the first word of the resulting Markov Chain
	 * @param k - the k next words to return
	 * @return a String representing the Markov Chain
	 */
	private String one(String seed, int k) {
		
		// Build return value and add seed
		StringBuilder returnVal = new StringBuilder(seed);
		returnVal.append(" ");
		
		// Keep track of this and next words
		String currWord = seed;
		String next;
		
		int i = 1;
		
		while(i < k) {
			ProbabilityMap probMap = timeSaver.get(currWord);
			
			// Check for next possible words
			if(probMap == null || probMap.isEmpty) {
				next = seed;
			}
			else {
				next = probMap.chooseMostProb();
			}
			
			// Append word to return value
			returnVal.append(next);
			returnVal.append(" ");
			currWord = next;
			i++;
		}
		
		return returnVal.toString();	
	}
	
	
	/**
	 * Private helper method for 'all' version of printK's Markov Chain.
	 * 
	 * @param seed - the first word of the resulting Markov Chain
	 * @param k - the k next words to return
	 * @return a String representing the Markov Chain
	 */
	private String all(String seed, int k) {
		
		// Build return value and add seed
		StringBuilder returnVal = new StringBuilder(seed);
		returnVal.append(" ");
		
		//Keep track of this and next words
		String currWord = seed;
		String next;
				
		
		for(int i = 1; i < k; i++) {
			ProbabilityMap probMap = timeSaver.get(currWord);
			
			// Check for next possible words
			if(probMap == null || probMap.isEmpty)
				next = seed;
			else {
				next = probMap.chooseRandomProb();
			}
			
			// Append word to return value
			returnVal.append(next);
			returnVal.append(" ");
			currWord = next;
		}
		
		return returnVal.toString();
	}	
	
	/**
	 * Returns a 'formatted' version of the input String. Removes all characters 
	 * besides letters, numbers, and underscore and returns all lowercase String.
	 * 
	 * @param word - String to format
	 * @return formatted String
	 */
	private String format(String word) {
		
		// Check if word is without non-char
		Matcher matcher = formatPattern.matcher(word);
		if(matcher.matches())
			return word.toLowerCase();
		
		// Remove non-char values
		else {	
			word.replace("[^a-zA-Z_0-9]", "");
			return word.toLowerCase();
		}
	}
}
