/**
 * ServerMonitor.java
 *
 * Creato il 14/mar/07 15:38:31
 */
package server;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 *
 * @author Matteo Piccinini
 */
public class ServerMonitor extends JPanel {

    private static final long serialVersionUID = 8970820070225785998L;
    public ServerMonitor(){
        super(new GridBagLayout());

        SwingUtilities.invokeLater(new Runnable(){
            public void run() {
                initGUI();
            }
        });
    }

    private void initGUI(){
        JLabel label = new JLabel("Brute Force Progress");
        progress = new JProgressBar();
        progress.setStringPainted(true);
        elapsed = new JLabel("Elapsed Time");
        remaining = new JLabel("Remaining Time");
        sentJobs = new JLabel("Sent Jobs");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        add(label, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(progress, gbc);
        
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(elapsed, gbc);
        
        gbc = new GridBagConstraints();
        gbc.gridy = 2;
        gbc.gridx = 1;
        add(remaining, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(sentJobs, gbc);
    
    }

    public void startMonitoring(Server server){
        this.server = server;
        start = System.currentTimeMillis();
        progress.setMaximum(server.getAllWorkSize());
        progress.setMinimum(0);
        progress.setValue(0);
        Timer t = getTimer();
        if (!t.isRunning())t.start();
    }
    
    public void stop(){
        Timer t = getTimer();
        if (t.isRunning())t.stop();
    }

    private Timer getTimer(){
        if (timer == null)timer = new Timer(1000, new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                checkServer();
            }
        });
        return timer;
    }

    private void checkServer(){
        int workDone = server.workDone();
        int allWorks = server.getAllWorkSize(); 
        
        long now = System.currentTimeMillis();
        progress.setValue(workDone);

        long elapsedTime = now - start;
        elapsed.setText("Elapsed Time " + formatTime(elapsedTime));

        if(workDone <= 0)return;
        long estimatedTime = (elapsedTime * allWorks) / workDone;
        long remainingTime = (start + estimatedTime) - now;
        remaining.setText("Remaining Time " + formatTime(remainingTime));
        
        
        sentJobs.setText("Sent Jobs: " + server.getSentJobs());
    }
    
    private String formatTime(long time){
    	StringBuffer sb = new StringBuffer(8);
    	int ore = (int)time / 3600000;
    	int resto = (int)time % 3600000;
    	int minuti = resto / 60000;
    	resto = resto % 60000;
    	int secondi = resto / 1000;
    	
    	if (ore < 10)
    		sb.append("0");
    	sb.append(ore);
    	
    	sb.append(":");
    	
    	if (minuti < 10)
    		sb.append("0");
    	sb.append(minuti);
    	
    	sb.append(":");
    	if (secondi < 10)
    		sb.append("0");

    	sb.append(secondi);
    	return sb.toString();
    }
    
    public static void main(String[] args) {
		System.out.println(new ServerMonitor().formatTime(1000*60*60*24));
	}

    private Server server;
    private JLabel elapsed, remaining,sentJobs;
    private JProgressBar progress;
    private long start;
    private Timer timer;
}
