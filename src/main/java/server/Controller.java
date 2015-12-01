/**
 * Controller.java
 *
 * Creato il 07/mar/07 18:11:13
 */
package server;

import java.security.MessageDigest;

import client.RemoteClient;

/**
 *
 * @author Matteo Piccinini
 */
public class Controller {

    public static void main(String[] args) {
        try{
//            final long start = System.currentTimeMillis();
            byte[]tocrack = MessageDigest.getInstance("MD5").digest("abcdefgh".getBytes("UTF-8"));
            final Server s = new Server(tocrack, "abcdefgh", 1,9, "MD5");
/*
            final JProgressBar progress = new JProgressBar();
            progress.setStringPainted(true);
            Timer t = new Timer(1000, new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    long now = System.currentTimeMillis();
                    long end = ((now - start) * s.getAllWorkSize()) / s.workDone();
                  progress.setValue(s.workDone());
                  progress.setString(s.workDone() + " / " + s.getAllWorkSize() + " " + new Date(end + now));
                }
            });

            progress.setMaximum(s.getAllWorkSize());
            JFrame f = new JFrame("Combinazioni possibili: " + s.getAllWorkSize());
            f.getContentPane().add(progress);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.pack();
            f.setVisible(true);
*/

            new Thread(s).start();

//            Client c1 = new Client(s);
            RemoteClient c1 = new RemoteClient();
            Thread t1 = new Thread(c1, "Client1");
            t1.start();
//            Client c2 = new Client(s);
//            Thread t2 = new Thread(c2, "Client2");
//            t2.start();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
