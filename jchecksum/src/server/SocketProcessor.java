/**
 * SocketProcessor.java
 *
 * Creato il 28/mar/07 17:44:32
 */
package server;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author Matteo Piccinini
 */
public class SocketProcessor implements Runnable {

    private Server server;
    private Socket socket;

    public SocketProcessor(Server server, Socket socket){
        this.server = server;
        this.socket = socket;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run() {
//        System.out.println("Processing server request");
        try{
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            int requestCode = ois.readInt();

            switch (requestCode) {
                case 0://Richiesta di un nuovo job
                    sendJob(os);
                    break;
                case 1://Invio della soluzione da parte del client
                    readSolution(ois);
                    break;
            }

            os.close();
            is.close();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
            	socket.close();
            	System.out.println("ServerSide socket closed");
            }catch(Exception e){
            	System.out.println("ServerSide socket closing error: " + e);
            	e.printStackTrace();
            }
        }
    }
    
    private void sendJob(OutputStream os) throws Exception {
        BufferedOutputStream bos = new BufferedOutputStream(os);
        ObjectOutputStream oos = new ObjectOutputStream(bos);

        Job j = server.nextJob();
        oos.writeObject(j);

        oos.flush();
    }

    private void readSolution(ObjectInputStream ois)throws Exception{
        Job found = (Job)ois.readObject();
        String password = (String)ois.readObject();
        ois.close();
        server.notify(found, password);        
    }

}
