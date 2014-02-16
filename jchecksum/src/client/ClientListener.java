package client;

import server.Job;

public interface ClientListener {

	public void jobAcquired(Job j);
	
	public void jobEnded(Job j);
}
