package SAR.v3.Event.Test;

import SAR.v3.Implementation.MessageQueue;
import SAR.v3.Implementation.QueueBroker.ConnectListener;

public class Connecter implements ConnectListener {
	
	Client client;
	
	public Connecter(Client client) {
		this.client = client;
	}

	
	/**
	 * Permet d'ajouter un Listener à la Queue nouvellement créée et d'envoyer un message.
	 */
	@Override
	public void connected(String name, int port, MessageQueue mq) {
		// TODO Auto-generated method stub
		
		mq.setListener(new ClientListener(mq, client));
		mq.send(name.getBytes());
	}

	@Override
	public void refused(String name, int port) {
		// TODO Auto-generated method stub
	}

}
