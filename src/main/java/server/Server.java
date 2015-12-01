/**
 * Server.java
 *
 * Creato il 27/feb/07 14:46:01
 */
package server;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author Matteo Piccinini
 */
public class Server implements Serializable, Runnable{

    private static final long serialVersionUID = -7037043933651559747L;

    private transient ServerSocket ss;
    private List<ServerListener> listeners;
    
    private byte[]toCrack;
    private char[]alphabet;
    private int max;
    
    private String algorithm;
    private int index;
    private static int DEFAULT_WORK_SIZE = 5000;//1000000;
    private int wordLength;
    private boolean incrementWordLength = false;
    private int allJobsLength;
    private int workProgressIndex;
    private boolean found = false;
    private List<Job> sentJobs;

    public Server(byte[] toCrack, String alphabet, int min, int max, String algorithm){
    	listeners = new ArrayList<ServerListener>();
    	sentJobs = new ArrayList<Job>();
        this.alphabet = alphabet.toCharArray();
        index = 0;
        this.max = max;
        wordLength = min;
        this.algorithm = algorithm;
        this.toCrack = toCrack;

        allJobsLength = 0;
        for(int i = min; i <= max; i++){
            allJobsLength += Math.pow(alphabet.length(), i);
        }
    }
    
    public int getSentJobs(){
    	return sentJobs.size();
    }
    
    public void setWorkSize(int worksize){
    	DEFAULT_WORK_SIZE = worksize;
    }

    public void run(){
        try{
            if(ss == null){
            	ss = new ServerSocket();
                ss.setReuseAddress(true);
                ss.bind(new InetSocketAddress(2222));
            }

            while(index < allJobsLength && found == false){
                Socket socket = ss.accept();
                SocketProcessor sp = new SocketProcessor(this, socket);
                new Thread(sp).start();                
            }
            System.out.println("Stop serving");
        }catch(IOException e){
            e.printStackTrace();
        }finally{
        	try{
        		ss.close(); 
//        		System.out.println("Server socket closed");
        	}catch(Exception e){
        		System.out.println("Error closing server socket: " + e );
        		e.printStackTrace();
        	}
        }
    }

    public void stop(){
        try{
        	if(ss != null && ss.isBound())ss.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public int getAllWorkSize(){
        return allJobsLength;
    }

    public int workDone(){
        return workProgressIndex;
    }

    public synchronized Job nextJob(){
//    	if (!sentJobs.isEmpty())
    	
    	
        if(incrementWordLength){
            wordLength++;
            index = 0;
            incrementWordLength = false;
        }
        if (wordLength > max)return null;

        int combinazioni = (int)Math.pow(alphabet.length, wordLength);
        int endIndex = index + DEFAULT_WORK_SIZE;
        int workSize = DEFAULT_WORK_SIZE;
        if (endIndex > combinazioni){
            workSize = combinazioni - index;
            incrementWordLength = true;
        }
        Job job = new Job(toCrack, wordLength, alphabet, algorithm, index, workSize);
        index += workSize;
        workProgressIndex += workSize;
        sentJobs.add(job);
        return job;
    }

    public void notify(Job j, String password){
    	found = true;
        for (ServerListener listener : listeners) {
			listener.serverNotified(password);
		}
    }

    public void addServerListener(ServerListener l){
    	listeners.add(l);
    }
    
    public void removeServerListener(ServerListener l){
    	listeners.remove(l);
    }
    
    public void abort(Job j, int numCicli){
    	int index = sentJobs.indexOf(j);
    	if (index < 0)return;

    	int startIndex = j.getStartIndex() + numCicli;
    	int jobLength = j.getJobLength() - numCicli;
    	Job newJ = new Job(j.getToCrack(), j.getWordLength(), j.getAlphabet(), j.getAlgorithm(), startIndex, jobLength);
    	sentJobs.set(index, newJ);
    }

}
