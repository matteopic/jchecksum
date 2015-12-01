package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ServerSerializer extends Thread {

	private Server server;
	private File target;

	public ServerSerializer(Server server, File target){
		this.server = server;
		this.target = target;
	}
	
	public void run() {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(target);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(server);
			oos.flush();
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static Server load(File file){
		try{
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
			Server server = (Server)ois.readObject();
			ois.close();
			return server;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
