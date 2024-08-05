package comprehensive;

/**
 * A class to utilize ProbabilityMap and TimeSaver through the terminal.
 * 
 * @author Harrison LeTourneau and Natalie DeSimone
 * @version 4/21/24
 */
public class TextGenerator {

	/**
	 * Generates the next k most probable words to follow the seed word in the input file
	 * in order from most probable to least
	 * 
	 * @param filename - the input file
	 * @param seed - seed word
	 * @param k - k number of words to return
	 */
	public static void Generate(String filename, String seed, int k) {
		
		ProbabilityMap probMap = new ProbabilityMap(filename, seed);
		System.out.println(probMap.printK(k));
	}
	
	/**
	 * Prints the K next most probable elements following the seed word, either
	 * selecting the 'one' most probable word to follow the word or selecting 
	 * the next word, random by probability, from 'all' next possible words.
	 * Either input, 'one' or 'all', represents a Markov Chain.
	 * 
	 * @param filename - the input file
	 * @param seed - the first word of the resulting Markov Chain
	 * @param k - the k next words to return
	 * @param oneOrAll - String 'one' or 'all', determining the behaviour of the
	 * 					 Markov Chain 
	 * @return a String representing the Markov Chain
	 */
	public static void Generate(String filename, String seed, int k, String oneOrAll) {
		TimeSaver timeSaver = new TimeSaver(filename);
		System.out.println(timeSaver.printK(seed, k, oneOrAll));
	}
	
	public static void main(String[] args) {
						
		if(args.length == 3) {
			Generate(args[0], args[1], Integer.parseInt(args[2]));
		}
		
		if(args.length == 4) {
			Generate(args[0], args[1], Integer.parseInt(args[2]), args[3]);

		}
	}

}
