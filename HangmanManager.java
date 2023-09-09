/*  Student information for assignment:
 *
 *  On my honor, <Garv Kinariwala>, this programming assignment is my own work
 *  and I have not provided this code to any other student.
 *
 *  Name: Garv Kinariwala
 *  email address: garvkinariwala@gmail.com
 *  UTEID: gnk289
 *  Section 5 digit ID: 52050
 *  Grader name: Justin
 *  Number of slip days used on this assignment: 2
 */
// add imports as necessary
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Manages the details of EvilHangman. This class keeps
 * tracks of the possible words from a dictionary during
 * rounds of hangman, based on guesses so far.
 *
 */
public class HangmanManager {
    // instance variables / fields
		private int guessCounter;
		private final Set<String> words;
		private Set<String> tempWords;
		private boolean debuOn;
		private int totalGuesses;
		private HangmanDifficulty difficulty;
		String hiddenPattern;
		String charGuessed;
		
    /**
     * Create a new HangmanManager from the provided set of words and phrases.
     * pre: words != null, words.size() > 0
     * @param words A set with the words for this instance of Hangman.
     * @param debugOn true if we should print out debugging to System.out.
     */
    public HangmanManager(Set<String> words, boolean debugOn) {
    	if (words == null || words.size() <= 0) {
    		throw new IllegalArgumentException("Violation of precondition: "
					+ "words cannot be null" + " words length must be greater than 0");
    	}
    	this.words = words;
    	this.debuOn = debugOn;
    	hiddenPattern = "";
    }

    /**
     * Create a new HangmanManager from the provided set of words and phrases.
     * Debugging is off.
     * pre: words != null, words.size() > 0
     * @param words A set with the words for this instance of Hangman.
     */
    public HangmanManager(Set<String> words) {
    	if (words == null || words.size() <= 0) {
    		throw new IllegalArgumentException("Violation of precondition: "
					+ "words cannot be null" + " words length must be greater than 0");
    	}
    	this.words = words;
    	this.debuOn = false;
    	hiddenPattern = "";
    }

    /**
     * Get the number of words in this HangmanManager of the given length.
     * pre: none
     * @param length The given length to check.
     * @return the number of words in the original Dictionary
     * with the given length
     */
    public int numWords(int length) {
    	int talleyCounter = 0;
    	for (String word : words) {
    		if (word.length() == length) {
    			talleyCounter++;
    		}
    	}
        return talleyCounter;
       }

    /**
     * Get for a new round of Hangman. Think of a round as a
     * complete game of Hangman.
     * @param wordLen the length of the word to pick this time.
     * numWords(wordLen) > 0
     * @param numGuesses the number of wrong guesses before the
     * player loses the round. numGuesses >= 1
     * @param diff The difficulty for this round.
     */
    public void prepForRound(int wordLen, int numGuesses, HangmanDifficulty diff) {
    	this.totalGuesses = numGuesses;
    	this.difficulty = diff;
    	this.guessCounter = 0;
    	tempWords = new HashSet<String>();
    	hiddenPattern = "";
    	for (int i = 0; i < wordLen; i++) {
    		hiddenPattern += "-";
    	}
    	charGuessed = "";
    	for (String word : words) {
    		if(word.length() == wordLen) {
    			tempWords.add(word);
    		}
    	}
    }

    /**
     * The number of words still possible (live) based on the guesses so far.
     *  Guesses will eliminate possible words.
     * @return the number of words that are still possibilities based on the
     * original dictionary and the guesses so far.
     */
    public int numWordsCurrent() {
    	return tempWords.size();
    }

    /**
     * Get the number of wrong guesses the user has left in
     * this round (game) of Hangman.
     * @return the number of wrong guesses the user has left
     * in this round (game) of Hangman.
     */
    public int getGuessesLeft() {
    	 return totalGuesses;
    }

    /**
     * Return a String that contains the letters the user has guessed
     * so far during this round.
     * The characters in the String are in alphabetical order.
     * The String is in the form [let1, let2, let3, ... letN].
     * For example [a, c, e, s, t, z]
     * @return a String that contains the letters the user
     * has guessed so far during this round.
     */
    public String getGuessesMade() {
    	return charGuessed;
    }

    /**
     * Check the status of a character.
     * @param guess The characater to check.
     * @return true if guess has been used or guessed this round of Hangman,
     * false otherwise.
     */
    public boolean alreadyGuessed(char guess) {
    	System.out.println(charGuessed + " " + guess);
    	return charGuessed.contains("" + guess);
    }

    /**
     * Get the current pattern. The pattern contains '-''s for
     * unrevealed (or guessed) characters and the actual character 
     * for "correctly guessed" characters.
     * @return the current pattern.
     */
    public String getPattern() {
    	return hiddenPattern;
    }

    /**
     * Update the game status (pattern, wrong guesses, word list),
     * based on the give guess.
     * @param guess pre: !alreadyGuessed(ch), the current guessed character
     * @return return a tree map with the resulting patterns and the number of
     * words in each of the new patterns.
     * The return value is for testing and debugging purposes.
     */
    public TreeMap<String, Integer> makeGuess(char guess) {
    	final int EASY_MOD = 2;
    	final int MEDIUM_MOD = 4;
    	if (alreadyGuessed(guess)) {
    		throw new IllegalArgumentException("Violation of precondition: "
					+ "That letter has already been guessed");
    	}
    	guessCounter++;
    	charGuessed += guess;
    	totalGuesses--;
    	TreeMap<String, Integer> pattern = new TreeMap<String, Integer>();
    	for (String words: tempWords) {
    		String key = stringToPattern(words);
    		pattern.put(key, pattern.getOrDefault(key, 0) + 1);
    	}
    	String maxPattern = "";
    	maxPattern = secondLargestPattern(pattern, "");
    	String secondLargest = secondLargestPattern(pattern, maxPattern);
    	if (((guessCounter % EASY_MOD == 0 && difficulty == HangmanDifficulty.EASY ) || 
    			(guessCounter % MEDIUM_MOD == 0 && difficulty == HangmanDifficulty.MEDIUM ))
    			 && secondLargest.compareTo("") != 0) {
    		maxPattern = secondLargest;
    	}
    	Set<String> possibleWords = new HashSet<String>();
    	for (String words: tempWords) {
    		if (stringToPattern(words).equals(maxPattern)){
    			possibleWords.add(words);
    		}
    	}
    	tempWords = possibleWords;
    	hiddenPattern = maxPattern;
        return pattern;
    }
    
    //returns the second largest pattern which is used based on the difficulty
    private String secondLargestPattern(TreeMap<String, Integer> pattern, String maxPattern) {
    	int max = 0;
    	String secondMaxPattern = "";
    	for (Map.Entry<String, Integer> entry: pattern.entrySet()) {
    		if (entry.getValue() > max && entry.getKey().compareTo(maxPattern) != 0) {
    			max = entry.getValue();
    			secondMaxPattern = entry.getKey();
    		}
    		if (entry.getValue() == max && entry.getKey().compareTo(maxPattern) != 0) {
    			int countDashes = 0;
        		for (int i = 0; i < secondMaxPattern.length(); i++) {
        			if(secondMaxPattern.charAt(i) == '-') {
        				countDashes++;
        			}
        		}
        		int dashCounter = 0;
        		for (int i = 0; i < entry.getKey().length(); i++) {
        			if(entry.getKey().charAt(i) == '-') {
        				dashCounter++;
        			}
        		}
        		if (dashCounter > countDashes) {
        			secondMaxPattern = entry.getKey();	
        		}
        		else if (countDashes == dashCounter) {
        			if (secondMaxPattern.compareTo(entry.getKey()) > 0) {
        				secondMaxPattern = entry.getKey();
        			}
        		}
    		}
    	}
    	return secondMaxPattern;
    }
    
    //helps update the pattern based on the letter guessed by the user
    private String stringToPattern(String word) {
    	String tempWord = "";
    	for (int i = 0; i < word.length(); i++) {
    		if (charGuessed.contains("" + word.charAt(i))) {
    			tempWord += word.charAt(i);
    		}
    		else {
    			tempWord += "-";
    		}
    	}
    	return tempWord;
    }
    
    /**
     * Return the secret word this HangmanManager finally ended up
     * picking for this round.
     * If there are multiple possible words left one is selected at random.
     * <br> pre: numWordsCurrent() > 0
     * @return return the secret word the manager picked.
     */
    public String getSecretWord() {
    	if (numWordsCurrent() > 0) {
    		throw new IllegalArgumentException("Violation of precondition: "
					+ "Total number of words must be greater than 0");
    	}
    	int index = (int)(Math.random()*tempWords.size() + 1);
        return (String) tempWords.toArray()[index] ;
    }
}