/**
 * RemoteClient.java
 *
 * Creato il 28/mar/07 17:55:23
 */
package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import server.Job;

/**
 *
 * @author Matteo Piccinini
 */
public class RemoteClient extends AbstractClient{

    public RemoteClient(){}
    
    private String host;
    private int port;
    private transient Job j;
    protected ClientListener listener;
    
    public void setClientListener(ClientListener listener){
    	this.listener = listener;
    }
    
    public synchronized void setHost(String host, int port) {
		this.host = host;
		this.port = port;
	}
    
    public Job getCurrentJob(){
    	return j;
    }

    public void run() {
        try{
            while((j = getJob()) != null){
            	if(listener != null)listener.jobAcquired(j);

    //          System.out.println(Thread.currentThread().getName() + " " + j);
                String result = start(j);
                if (result != null){
                    notify(j, result);
                    System.out.println("Devo notificare il server " + result);
                    break;
                }
                if(listener != null)listener.jobEnded(j);
            }
            System.out.println(Thread.currentThread().getName() + " STOP, nessun'altro lavoro dal server");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    protected void notify(Job j, String password){
        try{
            Socket s = openServerSocket();
            OutputStream os = s.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeInt(1);//1 = codice di invio risultato
            oos.writeObject(j);
            oos.writeObject(password);
            oos.flush();
            oos.close();
//            System.out.println("Close server socket");
            s.close();
        }catch(Exception e){
            e.printStackTrace();

        }
    }

    protected Job getJob()throws Exception {
//      InetAddress addr = InetAddress.getByName(host);
      Socket s = openServerSocket();
      OutputStream os = s.getOutputStream();
      InputStream is = s.getInputStream();

      ObjectOutputStream oos = new ObjectOutputStream(os);
      oos.writeInt(0);//1 = codice di richiesta job
      oos.flush();

      ObjectInputStream ois = new ObjectInputStream(is);
      Job j = (Job)ois.readObject();

      ois.close();
      oos.close();
//      System.out.println("Close client side socket");
      s.close();
      return j;
    }

    private Socket openServerSocket()throws IOException {
    	//Once a socket has been closed, it is not available for further networking use 
    	//(i.e. can't be reconnected or rebound). A new socket needs to be created. 
    	Socket socket = new Socket();
		socket.setReuseAddress(true);
		socket.setTcpNoDelay(true);
    	socket.connect(new InetSocketAddress(host, port));
        return socket;
    }

    public static void main(String[] args) {
        RemoteClient rc = new RemoteClient();
        new Thread(rc).start();
    }
}
