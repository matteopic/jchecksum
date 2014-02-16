import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.security.MessageDigest;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import client.ClientGUI;

import server.ServerGUI;

public class JChecksum extends JFrame{

    private static final long serialVersionUID = -6346418130659468017L;
    public JChecksum(){
		super("JChecksum by Matteo Piccinini");
		initGUI();
	}

	public void initGUI(){
        JPanel container = new JPanel();
		container.setLayout( new GridBagLayout());

		nomefile = new JTextField(25);
		nomefile.setTransferHandler(new FileTransferHandler(){
            private static final long serialVersionUID = -2771109750065886062L;

            public void fileDropped(File f){
				if (!f.isFile())return;
				try{
					nomefile.setText(f.getCanonicalPath());
				}catch(Exception e){}
			}
		});

		hash = new JTextField();
		testo = new JTextField();
		algoritmo = new JComboBox();
		sfoglia = new JButton("Sfoglia...");
        Dimension d = sfoglia.getPreferredSize();
        d.setSize(d.getWidth(), d.getHeight() - 5);
        sfoglia.setPreferredSize(d);

		buttonText = new JRadioButton("Testo: ");
		buttonFile = new JRadioButton("File: ", true);

		nomefile.setEnabled(true);
		sfoglia.setEnabled(true);
		testo.setEnabled(false);


		ButtonGroup bg = new ButtonGroup();
		bg.add(buttonText);
		bg.add(buttonFile);
		//bg.setSelected(buttonFile.getModel(), true);


		buttonText.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
					nomefile.setEnabled(false);
					sfoglia.setEnabled(false);
					testo.setEnabled(true);
					testo.requestFocus();
				}
			}
		);

		buttonFile.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
					nomefile.setEnabled(true);
					nomefile.requestFocus();
					sfoglia.setEnabled(true);
					testo.setEnabled(false);
				}
			}
		);




		ButtonGroup bg2 = new ButtonGroup();
		hex = new JRadioButton("Hex");
		b64 = new JRadioButton("Base64");
		bg2.add(hex);
		bg2.add(b64);
		bg2.setSelected(hex.getModel(), true);
		JPanel viste = new JPanel();
		//viste.setLayout(new GridLayout(3,1));
		viste.add(hex);
		viste.add(b64);

		hex.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
					cambiaVista();
				}
			}
		);

		b64.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
					cambiaVista();
				}
			}
		);

		messageDigest = new MessageDigest[]{
			new MD2(), new MD4(), new MD5(), new Tiger(), new SHA1(), new CRC()
		};

		//Arrays.sort(messageDigest);

		for(int i = 0; i < messageDigest.length; i++){
			algoritmo.addItem(messageDigest[i].getAlgorithm());
		}

		crea = new JButton("Calcola");
		suFile = new JCheckBox("Scrivi su file");

		JPanel pulsanti = new JPanel();
        pulsanti.add(algoritmo);
		pulsanti.add(crea);
		pulsanti.add(suFile);


		GridBagConstraints layout = new GridBagConstraints();
		layout.gridx = 0;
		layout.gridy = 0;
		layout.anchor = GridBagConstraints.WEST;
		container.add(buttonText, layout);

		/*layout = new GridBagConstraints();
		layout.gridx = 1;
		layout.gridy = 0;
		layout.anchor = GridBagConstraints.EAST;
		layout.insets = new Insets(5,5,5,5);
		container.add(new JLabel("Testo:"), layout);*/


		layout = new GridBagConstraints();
		layout.gridx = 0;
		layout.gridy = 1;
		layout.gridwidth = 4;
		layout.fill = GridBagConstraints.HORIZONTAL;
		layout.insets = new Insets(5,15,5,5);
		layout.weightx = 1;
		container.add(testo, layout);


		layout = new GridBagConstraints();
		layout.gridx = 0;
		layout.gridy = 2;
		layout.anchor = GridBagConstraints.WEST;
		container.add(buttonFile, layout);

		/*layout = new GridBagConstraints();
		layout.gridx = 1;
		layout.gridy = 1;
		layout.anchor = GridBagConstraints.EAST;
		layout.insets = new Insets(5,5,5,5);
		container.add(new JLabel("File:"), layout);*/


		layout = new GridBagConstraints();
		layout.gridx = 0;
		layout.gridy = 3;
        layout.gridwidth = 3;
		layout.fill = GridBagConstraints.HORIZONTAL;
		layout.insets = new Insets(5,15,5,5);
		layout.weightx = 1;
		container.add(nomefile, layout);

		layout = new GridBagConstraints();
		layout.gridx = 3;
		layout.gridy = 3;
		layout.insets = new Insets(5,5,5,5);
		container.add(sfoglia, layout);

		layout = new GridBagConstraints();
		layout.gridx = 0;
		layout.gridy = 5;
		layout.anchor = GridBagConstraints.WEST;
        layout.insets = new Insets(0,20,0,0);
		container.add(new JLabel("Hash:"), layout);

        layout = new GridBagConstraints();
        layout.gridx = 1;
        layout.gridy = 5;
        layout.gridwidth = 4;
        layout.anchor = GridBagConstraints.WEST;
        //layout.insets = new Insets(5,5,5,5);
        container.add(viste, layout);
        
		layout = new GridBagConstraints();
		layout.gridx = 0;
		layout.gridy = 6;
		layout.gridwidth = 4;
		layout.fill = GridBagConstraints.HORIZONTAL;
		layout.insets = new Insets(0,15,5,5);
		layout.weightx = 1;
		container.add(hash, layout);


//        layout = new GridBagConstraints();
//        layout.gridx = 4;
//        layout.gridy = 4;
//        layout.insets = new Insets(5,5,5,5);
//        container.add(algoritmo, layout);

        
        layout = new GridBagConstraints();
		layout.gridx = 1;
		layout.gridy = 4;
		layout.gridwidth = 4;
		container.add(pulsanti, layout);

		crea.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
					try{
						create();
					}catch(Exception e){
						JOptionPane.showMessageDialog(null, e.getMessage(), "Error!!!", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		);


		sfoglia.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				try{
					JFileChooser chooser = new JFileChooser();
					 int returnVal = chooser.showOpenDialog(null);
					  if(returnVal == JFileChooser.APPROVE_OPTION){
						  File f = chooser.getSelectedFile();
						  if (f.isFile() && f.exists()){
					  		nomefile.setText( f.getCanonicalPath());
						}
					}
				}catch(Exception e){}
			}
		}
		);

        ServerGUI sg = new ServerGUI();
        JTabbedPane tabbed = new JTabbedPane();
        setContentPane(tabbed);
        tabbed.add("Generator", container);
        tabbed.add("Server", sg);
        tabbed.add("Client", new ClientGUI());
        sg.setVisible(false);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void create()throws Exception{
		if (buttonFile.isSelected()){
			chk = createChecksum(nomefile.getText());
		}else{
			chk = createStringChecksum(testo.getText());
		}

		cambiaVista();

		if (suFile.isSelected() && buttonFile.isSelected()){
			File f = new File(nomefile.getText() + "." +algoritmo.getSelectedItem());
			FileWriter fw = new FileWriter(f);
			fw.write( hash.getText() );
			fw.flush();
			fw.close();
			JOptionPane.showMessageDialog(this, f+ " creato", "CheckSum Creato", JOptionPane.INFORMATION_MESSAGE);
		}
    }

	public boolean check(String filename)throws Exception{
		byte[] chk1 = createChecksum(filename);
		byte[] chk2 = new byte[chk1.length];
		File f = new File(filename + "." + algoritmo.getSelectedItem());
		InputStream is = new FileInputStream(f);
		is.read(chk2);
		is.close();

		for (int i = 0; i < chk1.length; i++){
			if (chk1[i] != chk2[i])return false;
		}

		return true;
    }

	public byte[] createChecksum(String filename) throws Exception{
		int index = algoritmo.getSelectedIndex();
		if (index == -1)return null;;
		MessageDigest digest = messageDigest[index];
		digest.reset();

		InputStream fis =  new FileInputStream(filename);

		//String alg = (String)algoritmo.getSelectedItem();
		//MessageDigest digest = MessageDigest.getInstance(alg);

		byte[] buffer = new byte[1024];
		int byteLetti;
		while((byteLetti = fis.read(buffer, 0, 1024)) > 0){
			digest.update(buffer, 0, byteLetti);
		}

		fis.close();
		return digest.digest();
	}

	public byte[] createStringChecksum(String theString)throws Exception{
		int index = algoritmo.getSelectedIndex();
		if (index == -1)return null;
		//String alg = (String)algoritmo.getSelectedItem();
		//MessageDigest digest = MessageDigest.getInstance(alg);
		MessageDigest digest = messageDigest[index];
		digest.reset();
		digest.update(theString.getBytes("UTF-8"));
		return digest.digest();

	}

	public void cambiaVista(){
		if (chk == null)return;

		if(b64.isSelected()){
			sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
            String b64 = encoder.encodeBuffer(chk);
            b64 = b64.replaceAll("\\r\\n", "");
            b64 = b64.replaceAll("\\r","");
            b64 = b64.replaceAll("\\n","");
			hash.setText( b64 );
		}else{
			StringBuffer sb = new StringBuffer();
			Byte byteObj;
			int byteToInt;
			String hexString;

			for(int i = 0; i < chk.length; i++){
				byteObj = new Byte(chk[i]);
				byteToInt = byteObj.intValue();
				hexString = Integer.toHexString( byteToInt );

				if (byteToInt < 0)sb.append(  hexString.substring(6,8)  );//tolgo gli FFFFFF iniziali

				else if (byteToInt < 16){
					sb.append("0");			//Aggiungo uno zero in avanti in modo da creare una coppia
					sb.append(hexString);
				}
				else sb.append(  hexString  );

			}
			hash.setText( sb.toString().toUpperCase() );
		}

	}

	public static void main (String[]args){
		new JChecksum();
	}

private JButton crea, sfoglia;
private JCheckBox suFile;
private JComboBox algoritmo;
private JTextField nomefile, hash, testo;
private JRadioButton buttonText, buttonFile, b64, hex;
private byte[]chk;
private MessageDigest[] messageDigest;
}