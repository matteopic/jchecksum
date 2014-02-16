/**
 * ClientGUI.java
 *
 * Creato il 30/mar/07 11:41:34
 */
package client;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

import server.Job;

/**
 *
 * @author Matteo Piccinini
 */
public class ClientGUI extends JPanel implements ClientListener {
	
	private static final long serialVersionUID = 6331082038627053955L;

	public ClientGUI(){
		SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				initGUI();
			}
		});
	}
	
	private void initGUI(){
		setLayout(new GridBagLayout());

		JLabel hostLabel = new JLabel("Host");
		host = new JTextField("localhost");

		JLabel portLabel = new JLabel("Port");
		Document integerTextDocument = new PlainDocument(){
			private static final long serialVersionUID = -7942661770060414932L;

			@Override
			public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
				try{
					char[]chars = str.toCharArray();
					for (int i = 0; i < chars.length; i++) {
						if(!Character.isDigit(chars[i]))return;	
					}
					super.insertString(offs, str, a);
				}catch(Exception e){}
			}
		};
		
		GridBagConstraints gbc;
		
		gbc = new GridBagConstraints();
		gbc.weightx = 1;
		gbc.gridx = 0;
		add(hostLabel, gbc);
		
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 3;
		gbc.gridx = 1;
		gbc.insets = new Insets(5, 5, 5, 5);
		add(host, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		add(portLabel, gbc);
		
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.insets = new Insets(5, 5, 5, 5);
		port = new JTextField(integerTextDocument, "2222", 0);
		add(port, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(5, 5, 5, 5);
		JButton update = new JButton("Update");
		add(update, gbc);
		
		update.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				update();
			}
		});

		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 3;
		add(new JLabel("Thread Priority"), gbc);

		final JComboBox priority = new JComboBox(priorities);
		priority.setSelectedIndex(2);

		priority.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				int index = priority.getSelectedIndex();
				for(Thread thread : threads){
					thread.setPriority(prVal[index]);	
				}
			}
		});

		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 3;
		add(priority, gbc);


		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 4;
		add(new JLabel("Thread Count"), gbc);
		
        SpinnerModel smTCount = new SpinnerNumberModel(1, 1, 20, 1);
        final JSpinner spinnerTCount = new JSpinner(smTCount);
        spinnerTCount.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				adjustThreadsCount( (Integer)spinnerTCount.getValue() );
			}
		});
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 4;
		add(spinnerTCount, gbc);
		
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 5;
		add(new JLabel("Cicli Completati"), gbc);
		
		jobCompleted = new JLabel("0");
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 5;
		add(jobCompleted, gbc);
	}

	private void adjustThreadsCount(int useThreads){
		if(threads == null)threads = new ArrayList<Thread>();
		int actualThreadsCount = threads.size(); 

		if(useThreads < actualThreadsCount){
			int toClose = actualThreadsCount - useThreads;

			for(int i = 0; i < toClose; i++){
				int index = actualThreadsCount - 1 - i;
				Thread t = threads.get(index);
				rc.terminateThread(t);
				threads.remove(index);
			}
		}else{
			int toStart = useThreads - actualThreadsCount;
			for(int i = 0; i < toStart; i++){
				Thread t = new Thread( rc, "Client " + (actualThreadsCount + i + 1) );
				threads.add(t);
				t.start();
			}

		}
	}
	
	private void update(){
		boolean start = false;
		if(rc == null){
			//rc = new RemoteClient();
			rc = new MultithredRemoteClient();
			rc.setClientListener(this);

			start = true;
		}

		int portInt = Integer.parseInt(port.getText());
		rc.setHost(host.getText(), portInt);

		if(start){
			adjustThreadsCount(1);
		}
	}

	public void jobAcquired(Job j){
		j.getAlgorithm();
	}
	
	public void jobEnded(Job j) {
		count += j.getJobLength();
		jobCompleted.setText(Integer.toString(count));
	}

	private transient MultithredRemoteClient rc;
	private JTextField host, port;
	private JLabel jobCompleted;
	private ArrayList<Thread> threads;
	private static String[]priorities = new String[]{
			"Molto Alta", "Alta", "Normale" ,"Bassa", "Molto Bassa"
	};
	private int count = 0;

	int[] prVal = new int[]{ Thread.MAX_PRIORITY, 7, 5, 3, Thread.MIN_PRIORITY };
	
	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setContentPane(new ClientGUI());
		f.pack();
		f.setVisible(true);
	}

}
