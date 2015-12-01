package client;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashSet;

import server.Job;
import server.PhraseGenerator;

public class MultithredRemoteClient extends RemoteClient {

	private Job job;
	private PhraseGenerator pg;
	private static ThreadLocal<Boolean> keepThreadRunning = new ThreadLocal<Boolean>();
	private HashSet<Thread> toTerminate;

	public MultithredRemoteClient(){
		toTerminate = new HashSet<Thread>();
	}

	public void terminateThread(Thread t){
		System.out.println(Thread.currentThread().getName() + " Richiesta di chiusura");
		toTerminate.add(t);
	}

    public boolean check(MessageDigest md, String password, byte[] toCrack){
        md.reset();
        byte[] check;
        try {
            check = md.digest(password.getBytes("UTF-8"));
            return Arrays.equals(check, toCrack);
        } catch (UnsupportedEncodingException e) { return false; }
    }
    

    public void run() {
        try{
        	keepThreadRunning.set(true);

        	MessageDigest md = null;
        	boolean newJobAquired;
        	Job processingJob = null;
        	while(keepThreadRunning.get()){
        		newJobAquired = false;

        		synchronized (this) {
	        		if(job == null){
	        			job = getJob();
	        			pg = new PhraseGenerator(job.getAlphabet(), job.getWordLength(), job.getStartIndex());
	        			newJobAquired = job != null;
	        			System.out.println(Thread.currentThread().getName() + " Ottenuto il job");
	        		}

	    			processingJob = job;
        		}

	        	if(processingJob == null){
	        		keepThreadRunning.set(false);
	        		System.out.println(Thread.currentThread().getName() + " STOP, nessun'altro lavoro dal server");
	        		break;
	        	}

	        	if(newJobAquired && listener != null)listener.jobAcquired(job);

        		String algorithm = processingJob.getAlgorithm();
        		if(md == null || (newJobAquired && !md.getAlgorithm().equals(algorithm))){
        			md = MessageDigest.getInstance(algorithm);
        		}

        		String password = null;
        		synchronized (pg) {
        			if(pg.hasMoreElements()){
            			password = pg.nextWord();
            			//System.out.println(Thread.currentThread().getName() + " testing " + password);
            		}
				}

        		//Se il PhraseGenerator non ha più parole da generare, il job corrente è terminato
        		//Il prossimo ciclo chiederà un nuovo job al server
        		if(password == null){
        			synchronized (job) {
        				job = null;
        				continue;
					}
        		}

        		boolean found = check(md, password, processingJob.getToCrack());
        		if(found){
        			//Se è stata trovata la password
        			synchronized (job) {
        				System.out.println("Devo notificare il server " + password);
        				notify(processingJob, password);
        				job = null;
        			}
	                break;
        		}

        		if(keepThreadRunning.get()){
        			Thread t = Thread.currentThread();
        			synchronized (toTerminate) {
        				if(toTerminate.contains( t )){
            				toTerminate.remove(t);
            				keepThreadRunning.set( false );
            			}	
					}
        		}
        	}
        	
        	
        	
//        	
//            while((j = getJob()) != null){
//            	
//
//    //          System.out.println(Thread.currentThread().getName() + " " + j);
//                String result = start(j);
//                if (result != null){
//                    notify(j, result);
//                    System.out.println("Devo notificare il server " + result);
//                    break;
//                }
//                if(listener != null)listener.jobEnded(j);
//            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
}
