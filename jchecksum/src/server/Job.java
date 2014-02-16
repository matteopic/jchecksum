/**
 * Job.java
 *
 * Creato il 07/mar/07 13:28:19
 */
package server;

import java.io.Serializable;

/**
 *
 * @author Matteo Piccinini
 */
public class Job implements Serializable{

	private static final long serialVersionUID = 7297624557922124374L;
	private byte[] toCrack;
    private int startIndex;
    private int jobLength;
    private int wordLength;
    private String alg;
    private char[] alphabet;
    
    public Job(byte[] toCrack, int wordLength, char[] alphabet, String alg, int startIndex, int jobLength){
        this.toCrack = toCrack;
        this.wordLength = wordLength;
        this.startIndex = startIndex;
        this.jobLength = jobLength;
        this.alg = alg;
        this.alphabet = alphabet;
    }
    
    /**
     * @return the tocrack
     */
    public byte[] getToCrack() {
        return toCrack;
    }
    
    /**
     * @return the wordLength
     */
    public int getWordLength() {
        return wordLength;
    }
    
    /**
     * @return the startIndex
     */
    public int getStartIndex() {
        return startIndex;
    }
    
    /**
     * @return the jobLength
     */
    public int getJobLength() {
        return jobLength;
    }
    
    /**
     * @return the alg
     */
    public String getAlgorithm() {
        return alg;
    }
    
    /**
     * @return the alphabet
     */
    public char[] getAlphabet() {
        return alphabet;
    }
    
    public String toString() {
    	return "Index: " + startIndex + ", Job Length: " + jobLength + ", Word Length: " + wordLength + ", Algorithm: " + alg + " Maxcomb:" + Math.pow(alphabet.length, wordLength) ; 
    }
}
