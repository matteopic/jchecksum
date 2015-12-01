/*
 * PhraseGenerator.java
 *
 * Creato il 28/feb/07 09:02:02
 */
package server;

import java.util.Arrays;
import java.util.Enumeration;

public class PhraseGenerator implements Enumeration{

    private char[]chars;
    private char[]buf;
    int index;
    long combinazioni;
//    int wordLength;

    public PhraseGenerator(char[]chars, int wordLength){
        this(chars, wordLength, 0);
    }

    public PhraseGenerator(char[]chars, int wordLength, int startFrom){
        this.chars = chars;
        index = startFrom;
//        this.wordLength = wordLength;
        buf = new char[wordLength];
        Arrays.fill(buf, chars[0]);
        combinazioni = (long)Math.pow(chars.length, wordLength);
    }

    public long getCombinazioni(){
        return combinazioni;
    }

    public String nextWord(){
        String gen = generateString();
//        System.out.println(gen);
        index++;
        return gen;
    }

    private String generateString(){
        initMask(index, chars.length, buf);
/*
        char[]ret = new char[indexes.length];
        for(int i = 0; i < ret.length; i++){
            ret[i] = chars[indexes[i]];
        }*/
        return new String(buf);
    }
//    private String generateString(){
//        int radix = indexes.length;
//        char buf[] = new char[indexes.length];
//        int charPos = indexes.length - 1;
//        while (index <= -radix) {
//            buf[charPos--] = chars[-(index % radix)];
//            index = index / radix;
//        }
//        buf[charPos] = chars[-index];
//
//    return new String(buf, charPos, (indexes.length - charPos));
//    }


    /* (non-Javadoc)
     * @see java.util.Enumeration#hasMoreElements()
     */
    public boolean hasMoreElements() {
        return index < combinazioni;
    }

    /* (non-Javadoc)
     * @see java.util.Enumeration#nextElement()
     */
    public Object nextElement() {
        return nextWord();
    }

//    private  void initMask(int decimalIndex, int base, int[]mask){
//       // System.out.print(decimalIndex + " "+ base + " "+mask.length+" ("+Integer.toString(decimalIndex, base)+") " );
//        double value = decimalIndex;
//        int resto;
//        double newVal;
//        for (int i=mask.length - 1; i >= 0; i--){
//            newVal = value / base;
//            //if (newVal == 0)return;
//            resto = (int)newVal % base;
//            mask[i] = resto;
//            resto = 0;
//            value = newVal;
//        }
//    }

    private void initMask(int i, int radix, char[]buf){
//        int xx = i - 1;
        int charPos = buf.length - 1;
        i = -i;
//System.out.println(i + " " + -radix);
        while (i <= -radix) {
            buf[charPos--] = chars[-(i % radix)];
            i = i / radix;
        }
        buf[charPos] = chars[-i];
//        System.out.print(new String(buf, charPos, (chars.length - charPos)));
        //System.out.println(" " + Integer.toString(xx, radix));
    }
    
    public static void main(String[] args) {
        PhraseGenerator pg = new PhraseGenerator("abcdefghijklmnopqrstuvwxyz".toCharArray(), 6);
        System.out.println(pg.getCombinazioni());
        
        int i = 0;
        while (pg.hasMoreElements()) {
        	i++;
            String element = (String) pg.nextElement();
            System.out.println(i + "/" + pg.getCombinazioni()+ " "+ element);
        }
    }
}
