package comprehensive;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.FileReader;

/**
 * A class which defines a ProbabilityMap that holds all the next possible words for
 * a specified String and their probability of following this word.
 * 
 * @author Harrison LeTourneau and Natalie DeSimone
 * @version 4/21/2024
 */
public class ProbabilityMap {
	
	// Create a PriorityQueue and HashMap to handle parsed Strings
	private PriorityQueue<Map.Entry<String, Integer>> probQueue;
	private HashMap<String, Integer> hashProb;
	
	// Create ArrayList to assist with selecting random probability
	private ArrayList<String> randomList;
	
	// Create a Pattern to check against each String in the input file
	private static final Pattern formatPattern = Pattern.compile("[a-zA-Z_0-9]+");

	// Public booleans to manage ProbabilityMap status
	public boolean listMade;
	public boolean queueMade;
	public boolean isEmpty;

	/**
	 * Creates an empty ProbabilityMap.
	 */
	public ProbabilityMap(){	
		probQueue = new PriorityQueue<Map.Entry<String, Integer>>(new probComparator());
		hashProb = new HashMap<String, Integer>();
		
		queueMade = false;
		listMade = false;
		isEmpty = true;
	}
	
	/**
	 * Creates a ProbabilityMap for this seed contained in an input file.
	 * 
	 * @param filename - the input file to create this ProbabilityMap
	 * @param seed - the seed String of this ProbabilityMap; this map contains
	 * 				 this ProbabilityMap contains the possible words to follow
	 * 				 the seed and their probabilities
	 */
	public ProbabilityMap(String filename, String seed) {
		probQueue = new PriorityQueue<Map.Entry<String, Integer>>(new probComparator());
		
		// Attempt to parse through input file
		try {
			parseFile(filename, seed);

		}
		catch(IOException e) {
			System.out.println("Invalid file path. Please try again.");
		}
		queueMade = false;
		listMade = false;
		isEmpty = true;

	}
		
	/**
	 * Chooses the String from all possible next words based on their probabilities.
	 * 
	 * @return String next selected word
	 */
	public String chooseRandomProb() {
		if(!listMade)
			createRandomList();
		Random rng = new Random();
		return randomList.get(rng.nextInt(randomList.size()));
	}
	
	/**
	 * Chooses the next most probable String to follow the seed.
	 * 
	 * @return String next selected word
	 */
	public String chooseMostProb() {
		if(!queueMade)
			createQueue();
		return probQueue.peek().getKey();
	}
		
	/**
	 * Prints the K most probable items, in order most probable to least.
	 * 
	 * @param k - k next words to return
	 * @return - String representing the k most probable words
	 */
	public String printK(int k) {
		
		// Create the PriorityQueue for this ProbabilityMap
		createQueue();
		
		// Build return value
		StringBuilder printK = new StringBuilder();
		for(int i = 0; i < k; i++) {
			if(probQueue.isEmpty())
				return printK.toString();
			// Add each next most likely element and remove from PriorityQueue
			printK.append(probQueue.remove().getKey());
			printK.append(" ");
		}
		
		return printK.toString();
	}
	
	/**
	 * Adds another occurance of this word to follow the seed word. 
	 * 
	 * @param word - the String to add
	 * @return boolean false if new next possible word, true otherwise
	 */
	public Boolean addMapping(String word) {		
		isEmpty = false;
		
		// Check if mapping does not yet exist
		if(!hashProb.containsKey(word)) {
			hashProb.put(word, 1);
		return false;
		}
		
		else {
			// If mapping exists, increase occurance count by 1
			hashProb.put(word, hashProb.get(word) + 1);
			return true;
		}
	}
	
	// Private helper methods for this class
	
	
	/**
	 * Creates a list of all next possible words, duplicated the number of times
	 * they appear following the seed word in the input file.
	 */
	private void createRandomList() {
		randomList = new ArrayList<String>();
		for(Map.Entry<String, Integer> mapping : hashProb.entrySet()) {
			String key = mapping.getKey();
			for(int i = mapping.getValue(); i > 0; i--) {
				randomList.add(key);
			}
		}
		listMade = true;
	}
	
	/**
	 * Adds all the words from the HashMap created in parseFile to a PriorityQueue 
	 * which prioritizes word probability. 
	 */
	private void createQueue() {	
		queueMade = true;

		// Add each mapping to a PriorityQueue
		for(Map.Entry<String, Integer> mapping : hashProb.entrySet()) {
			probQueue.add(mapping);
		}
		
	}
	
	/**
	 * Parses through the input file.
	 * 
	 * @param filename - the input file to parse
	 * @param seed - the seed String of this ProbabilityMap
	 * @throws IOException if file name or path is invalid
	 */
	private void parseFile(String filename, String seed) throws IOException {	

		hashProb = new HashMap<String, Integer>();
		
		String thisWord = null;
		String nextWord;
		String line = null;
		String[] lineReader = null;
		boolean cont = false;
		int i = 0;
		
		// Wrap FileReader with BufferedReader to parse file
		BufferedReader fileReader = new BufferedReader(new FileReader(filename));
		
		// Check file is not empty, begin first line
		if((line = fileReader.readLine().trim()) != null) {
			// Words split by whitespace
			lineReader = line.split("\\s+");
			thisWord = format(lineReader[0]);
			i = 1;
			cont = true;
		}
		
		while(true) {
			if(cont) {
				// Parse through each line object
				while(i < lineReader.length) {
					nextWord = format(lineReader[i]);
					if(thisWord.equals(seed)) {
						addMapping(nextWord);
					}
					thisWord = nextWord;
					i++;
				}
			}
			i = 0;
			
			// Check for next line
			if((line = fileReader.readLine()) == null) {
				break;
			}
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
	
	
	/**
	 * Defines a comparator which compares values of each Mapping, resorts to Keys if tied.
	 */
	private class probComparator implements Comparator<Map.Entry<String, Integer>> {

		@Override
		public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
			if(o2.getValue().compareTo(o1.getValue()) == 0)
				return o1.getKey().compareTo(o2.getKey());
			
			else
				return o2.getValue().compareTo(o1.getValue());
		}
		
	}
	

}
