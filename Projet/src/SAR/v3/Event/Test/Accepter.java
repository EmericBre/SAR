package SAR.v3.Event.Test;

import SAR.v3.Implementation.MessageQueue;
import SAR.v3.Implementation.QueueBroker.AcceptListener;

public class Accepter implements AcceptListener{
	
	Server server;
	
	public Accepter(Server server) {
		this.server = server;
	}

	/**
	 * Permet de confirmer la connexion avec la MessageQueue
	 * Permet d'ajouter un Listener à la MessageQueue nouvellement créée.
	 * Le serveur envoie un message aux clients;
	 */
	@Override
	public void accepted(int port, MessageQueue mq) {
		// TODO Auto-generated method stub
		server.mqs.put(mq, port);
		mq.setListener(new ServerListener(mq, server));
		mq.send("serveur".getBytes());
	}

}
