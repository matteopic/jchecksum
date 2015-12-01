/**
 * Client.java
 *
 * Creato il 02/mar/07 18:59:48
 */
package client;

import server.Job;
import server.Server;

/**
 *
 * @author Matteo Piccinini
 */
public class Client extends AbstractClient {


    private Server server;
    
    public Client(Server s){
        this.server = s;
    }


    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run() {
        Job j;
        while((j = server.nextJob()) != null){
//        	System.out.println(Thread.currentThread().getName() + " " + j);
            String result = start(j);
            if (result != null){
                server.notify(j, result);
                break;
            }
        }
        System.out.println(Thread.currentThread().getName() + " STOP, nessun'altro lavoro dal server");
    }    

    
   
}
