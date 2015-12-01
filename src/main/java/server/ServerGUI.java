/**
 * ServerGUI.java
 *
 * Creato il 14/mar/07 13:04:56
 */
package server;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

/**
 *
 * @author Matteo Piccinini
 */
public class ServerGUI extends JPanel implements ServerListener {
    
    private static final long serialVersionUID = 3347947167771761988L;
    public ServerGUI(){
        SwingUtilities.invokeLater(new Runnable(){
            public void run() {
                initGUI();                
            }
        });
    }
    
    private void initGUI(){
        toCrack = new JTextField();
        
        //Search Simbols
        numeri = new JCheckBox("0...9");
        lettereMin = new JCheckBox("a...z");
        lettereMax = new JCheckBox("A...Z");
        simboli = new JCheckBox("!@#...");
        spazi = new JCheckBox("Space");
        custom = new JTextField();
        custom.addCaretListener(new CaretListener(){
        	public void caretUpdate(CaretEvent e) {
        		String txt = custom.getText();
        		boolean useCustom = txt != null && txt.trim().length() > 0;
        		all.getModel().setEnabled(!useCustom);
        		numeri.getModel().setEnabled(!useCustom);
                lettereMin.getModel().setEnabled(!useCustom);
                lettereMax.getModel().setEnabled(!useCustom);
                simboli.getModel().setEnabled(!useCustom);
                spazi.getModel().setEnabled(!useCustom);
        	}
        });
        all = new JCheckBox("All");

        all.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
            	boolean selected = all.isSelected();
                numeri.getModel().setSelected(selected);
                lettereMin.getModel().setSelected(selected);
                lettereMax.getModel().setSelected(selected);
                simboli.getModel().setSelected(selected);
                spazi.getModel().setSelected(selected);             
            }
        });

        JPanel searchSymbols = new JPanel();
        searchSymbols.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        Insets indent = new Insets(0, 10, 0, 0); 
        
        searchSymbols.add(all, gbc);
        gbc.insets = new Insets(0,10,0,0);
        searchSymbols.add(numeri, gbc);
        searchSymbols.add(lettereMin, gbc);
        searchSymbols.add(lettereMax, gbc);
        searchSymbols.add(simboli, gbc);
        searchSymbols.add(spazi, gbc);
        
        gbc.insets = new Insets(10, 0, 0, 0);
        searchSymbols.add(new JLabel("Custom"), gbc);
        gbc.insets = indent;
        searchSymbols.add(custom, gbc);

        Border bSS = BorderFactory.createTitledBorder("Search Symbols");
        searchSymbols.setBorder(bSS);
        

        JPanel passwordLength = new JPanel(new GridBagLayout());
        Border bPL = BorderFactory.createTitledBorder("Password Length");
        passwordLength.setBorder(bPL);


        SpinnerModel smMin = new SpinnerNumberModel(4, 0, Integer.MAX_VALUE, 1);
        SpinnerModel smMax = new SpinnerNumberModel(8, 0, Integer.MAX_VALUE, 1);
        spinnerMin = new JSpinner(smMin);
        spinnerMax = new JSpinner(smMax);

        GridBagConstraints gc;
        
        //Min Char
        gc = new GridBagConstraints();
        gc.gridx = 1;
        gc.gridy = 0;
        gc.insets = new Insets(0,0,0,4);
        gc.anchor = GridBagConstraints.EAST;
        passwordLength.add(new JLabel("Min"), gc);
        
        gc = new GridBagConstraints();
        gc.gridx = 2;
        gc.gridy = 0;
        spinnerMin.setPreferredSize(new Dimension(40, (int)spinnerMin.getPreferredSize().getHeight()));
        passwordLength.add(spinnerMin, gc);
        
        gc = new GridBagConstraints();
        gc.gridx = 3;
        gc.gridy = 0;
        gc.insets = new Insets(0,4,0,0);
        gc.anchor = GridBagConstraints.EAST;
        passwordLength.add(new JLabel("Char."), gc);
        //
        
        //Max Char
        gc = new GridBagConstraints();
        gc.gridx = 1;
        gc.gridy = 1;
        gc.insets = new Insets(0,0,0,4);
        gc.anchor = GridBagConstraints.WEST;
        passwordLength.add(new JLabel("Max"), gc);
        
        
        gc = new GridBagConstraints();
        gc.gridx = 2;
        gc.gridy = 1;
        spinnerMax.setPreferredSize(new Dimension(40,(int)spinnerMax.getPreferredSize().getHeight()));
        passwordLength.add(spinnerMax, gc);

        gc = new GridBagConstraints();
        gc.gridx = 3;
        gc.gridy = 1;
        gc.insets = new Insets(0,4,0,0);
        gc.anchor = GridBagConstraints.EAST;
        passwordLength.add(new JLabel("Char."), gc);
        //
        
//        Container c = this;
        setLayout(new GridBagLayout());

        gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = 2;
        gc.insets = new Insets(5, 5, 5, 5);
        gc.fill = GridBagConstraints.HORIZONTAL;
        add(new JLabel("Password Hash (Hex or B64)"), gc);

        gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = 1;
        gc.gridwidth = 2;
        gc.weightx = 2;
        gc.insets = new Insets(5, 5, 5, 5);
        gc.fill = GridBagConstraints.HORIZONTAL;
        add(toCrack, gc);
                
        
        gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = 2;
        gc.weightx = 2;
        gc.weighty = 1;
        gc.insets = new Insets(5, 5, 5, 5);
        gc.fill = GridBagConstraints.BOTH;
        add(searchSymbols, gc);
        
        gc = new GridBagConstraints();
        gc.gridx = 1;
        gc.gridy = 2;
        gc.weightx = 3;
        gc.weighty = 1;
        gc.insets = new Insets(5, 0, 5, 5);
        gc.fill = GridBagConstraints.BOTH;
        add(passwordLength, gc);
        
        serverMonitor = new ServerMonitor();
        gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = 3;
        gc.weighty = 1;
        gc.gridwidth = 2;
        gc.insets = new Insets(0, 5, 5, 5);
        gc.fill = GridBagConstraints.BOTH;
        add(serverMonitor, gc);
        
        start = new JButton("Start");
        start.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                start();
                start.setEnabled(false);
            }
        });

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(start);

        JButton stop = new JButton("Stop");
        buttons.add(stop);

        stop.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                stopServer();                
            }
        });

        gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = 4;
        gc.gridwidth = 2;
        gc.anchor = GridBagConstraints.EAST;
        gc.weightx = 3;
        //gc.weighty = 1;
        gc.insets = new Insets(0, 5, 5, 5);
        gc.fill = GridBagConstraints.HORIZONTAL;
        add(buttons, gc);
//        setVisible(true);
    }
    
    public void start(){
        StringBuffer alphabet = new StringBuffer();
        if(all.getModel().isEnabled()){
	        if(numeri.isSelected())
	            alphabet.append("0123456789");
	
	        if(lettereMax.isSelected())
	            alphabet.append("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
	
	        if (simboli.isSelected())
	            alphabet.append("|\\!\"£$%&/()=?^ì\'èé[]+*ù§à°#òç@-_.,;");
	
	        if(spazi.isSelected())
	            alphabet.append("\t ");
	
	        if(lettereMin.isSelected() || alphabet.length() == 0)
	            alphabet.append("abcdefghijklmnopqrstuvwxyz");
        }else{
        	alphabet.append(custom.getText());
        }
        byte[] bytes = decodeHash();
        if (bytes == null){
            JOptionPane.showMessageDialog(this, "Cannot decode password", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String chars = alphabet.toString();
        if(custom.getText().trim().length() > 0)
        	chars = custom.getText();

        int min = ((Number)spinnerMin.getValue()).intValue();
        int max = ((Number)spinnerMax.getValue()).intValue();
        server = new Server(bytes, chars, min, max, "MD5");
        server.addServerListener(this);
        serverMonitor.startMonitoring(server);
//        client = new Client(server);

        new Thread(server, "Server Thread").start();
    }

    private byte[] decodeHash(){
        String txt = toCrack.getText();
        if (txt == null || txt.trim().length() == 0) return null;

        try {
            byte[] ret = decodeHex(txt.toUpperCase().toCharArray());
            return ret;
        } catch (RuntimeException e) {}
        
        return null;
    }
    
    private byte[] decodeHex(char[]data){
            int len = data.length;

            byte[] out = new byte[len >> 1];

            // two characters form the hex value.
            for (int i = 0, j = 0; j < len; i++) {
                int f = toDigit(data[j], j) << 4;
                j++;
                f = f | toDigit(data[j], j);
                j++;
                out[i] = (byte) (f & 0xFF);
            }

            return out;
    }

    protected static int toDigit(char ch, int index){
        int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new RuntimeException("Illegal hexadecimal charcter " + ch + " at index " + index);
        }
        return digit;
    }

    private void stopServer(){
        if(server != null)server.stop();
        start.getModel().setEnabled(true);
    }
    
    
/*    private String getAlgorithm(byte[]toCrack){
        Iterator iter = Security.getAlgorithms("MessageDigest").iterator();
        while (iter.hasNext()){

        }
        
        
        switch (toCrack.length) {
        case 16:
            return "MD5";
        case 20:
            return "SHA1";
        case 32:
            return "SHA1";
        case 64:
            return "SHA512";            
        default:
            break;
        }
    }*/ 
    public void serverNotified(String password) {
    	JOptionPane.showMessageDialog(getTopLevelAncestor(), "Password Found: " + password, "Success", JOptionPane.INFORMATION_MESSAGE);
	}     


    public static void main(String[] args) {
    	JFrame f = new JFrame("Recover Lost Password");
        ServerGUI sg = new ServerGUI();
        f.setContentPane(sg);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    private JTextField toCrack, custom;
    private JCheckBox numeri, lettereMin, lettereMax, simboli, spazi, all;
    private JSpinner spinnerMin, spinnerMax;
//    private Client client;
    private ServerMonitor serverMonitor;
    private Server server;
    private JButton start;

}
