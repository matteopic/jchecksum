/**
 * AbstractClient.java
 *
 * Creato il 28/mar/07 18:00:34
 */
package client;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import server.Job;
import server.PhraseGenerator;

/**
 *
 * @author Matteo Piccinini
 */
public abstract class AbstractClient implements Runnable {

    public String start(Job j){
//        System.out.println("Startd job " + j);
        initDigester(j.getAlgorithm());

        PhraseGenerator pg = new PhraseGenerator(j.getAlphabet(), j.getWordLength(), j.getStartIndex());
        for(int i = 0; i < j.getJobLength(); i++){
            String password = pg.nextWord();
            //if(password.startsWith("aedc"))
//              System.out.println((j.getStartIndex()+i) + "/" + pg.getCombinazioni()  + ")" + password);
            boolean found = check(password, j.getToCrack());
            if(found){
//                System.out.println("Trovata: " + password + " " + i);
                return password;
            }
        }

        return null;
    }

    public boolean check(String password, byte[] toCrack){
        md.reset();
        byte[] check;
        try {
            check = md.digest(password.getBytes("UTF-8"));
            return Arrays.equals(check, toCrack);
        } catch (UnsupportedEncodingException e) { return false; }
    }

    protected void initDigester(String alg){
        if(md == null || !md.getAlgorithm().equals(alg)){
            try {
                md = MessageDigest.getInstance(alg);
            } catch (NoSuchAlgorithmException e) {
                md = null;
                e.printStackTrace();
            }        
        }
    }
    private MessageDigest md;
}
