import java.security.*;
import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.event.*;

public class JChecksum extends JFrame{

	public JChecksum(){
		super("JChecksum by Matteo Piccinini");
		initGUI();
	}

	public void initGUI(){
		Container container = getContentPane();
		container.setLayout( new GridBagLayout());

		nomefile = new JTextField(25);
		nomefile.setTransferHandler(new FileTransferHandler(){
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
		sfoglia = new JButton("Sfoglia");

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
		bytes = new JRadioButton("Byte");
		b64 = new JRadioButton("Base64");
		bg2.add(hex);
		bg2.add(bytes);
		bg2.add(b64);
		bg2.setSelected(hex.getModel(), true);
		JPanel viste = new JPanel();
		viste.setLayout(new GridLayout(3,1));
		viste.add(hex);
		viste.add(bytes);
		viste.add(b64);

		hex.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
					cambiaVista();
				}
			}
		);

		bytes.addActionListener(new ActionListener(){
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
		pulsanti.add(crea);
		pulsanti.add(suFile);


		GridBagConstraints layout = new GridBagConstraints();
		layout.gridx = 0;
		layout.gridy = 0;
		layout.anchor = GridBagConstraints.EAST;
		container.add(buttonText, layout);

		/*layout = new GridBagConstraints();
		layout.gridx = 1;
		layout.gridy = 0;
		layout.anchor = GridBagConstraints.EAST;
		layout.insets = new Insets(5,5,5,5);
		container.add(new JLabel("Testo:"), layout);*/


		layout = new GridBagConstraints();
		layout.gridx = 2;
		layout.gridy = 0;
		layout.gridwidth = 2;
		layout.fill = GridBagConstraints.HORIZONTAL;
		layout.insets = new Insets(5,5,5,5);
		layout.weightx = 1;
		container.add(testo, layout);


		layout = new GridBagConstraints();
		layout.gridx = 0;
		layout.gridy = 1;
		layout.anchor = GridBagConstraints.EAST;
		container.add(buttonFile, layout);

		/*layout = new GridBagConstraints();
		layout.gridx = 1;
		layout.gridy = 1;
		layout.anchor = GridBagConstraints.EAST;
		layout.insets = new Insets(5,5,5,5);
		container.add(new JLabel("File:"), layout);*/


		layout = new GridBagConstraints();
		layout.gridx = 2;
		layout.gridy = 1;
		layout.fill = GridBagConstraints.HORIZONTAL;
		layout.insets = new Insets(5,5,5,5);
		layout.weightx = 1;
		container.add(nomefile, layout);

		layout = new GridBagConstraints();
		layout.gridx = 3;
		layout.gridy = 1;
		layout.insets = new Insets(5,5,5,5);
		container.add(sfoglia, layout);

		layout = new GridBagConstraints();
		layout.gridx = 4;
		layout.gridy = 1;
		layout.insets = new Insets(5,5,5,5);
		container.add(algoritmo, layout);

		layout = new GridBagConstraints();
		layout.gridx = 0;
		layout.gridy = 2;
		layout.anchor = GridBagConstraints.EAST;
		container.add(new JLabel("Hash:"), layout);


		layout = new GridBagConstraints();
		layout.gridx = 2;
		layout.gridy = 2;
		layout.gridwidth = 3;
		layout.fill = GridBagConstraints.HORIZONTAL;
		layout.insets = new Insets(5,5,5,5);
		layout.weightx = 1;
		container.add(hash, layout);


		layout = new GridBagConstraints();
		layout.gridx = 5;
		layout.gridy = 2;
		layout.insets = new Insets(5,5,5,5);
		container.add(viste, layout);


		layout = new GridBagConstraints();
		layout.gridx = 1;
		layout.gridy = 3;
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


		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		show();
	}

	public void create()throws Exception{
		if (buttonFile.isSelected()){
			chk = createChecksum(nomefile.getText());
		}else{
			chk = createStringChecksum(testo.getText());
		}

		cambiaVista();

		if (suFile.isSelected() && buttonFile.isSelected() && !bytes.isSelected()){
			File f = new File(nomefile.getText() + "." +algoritmo.getSelectedItem());
			FileWriter fw = new FileWriter(f);
			fw.write( hash.getText() );
			fw.flush();
			fw.close();
			JOptionPane.showMessageDialog(this, f+ " creato", "CheckSum Creato", JOptionPane.INFORMATION_MESSAGE);
		}
		else if (suFile.isSelected() && buttonFile.isSelected() && bytes.isSelected()){
			File f = new File(nomefile.getText() + "." +algoritmo.getSelectedItem());
			FileOutputStream fw = new FileOutputStream(f);
			fw.write( chk );
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

		if (bytes.isSelected()){
			hash.setText(new String(chk));
		}else if(b64.isSelected()){
			sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
			hash.setText( encoder.encodeBuffer(chk) );
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
private JRadioButton buttonText, buttonFile, bytes, b64, hex;
private byte[]chk;
private MessageDigest[] messageDigest;
}