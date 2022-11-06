package SAR.v3.Event.Test;

import java.util.HashMap;

import SAR.v3.Implementation.MessageQueue;
import SAR.v3.Implementation.MessageQueue.Listener;

public class ServerListener implements Listener{
	
	MessageQueue mq;
	Server server;
	
	public ServerListener(MessageQueue mq, Server server) {
		this.mq = mq;
		this.server = server;
	}

	/**
	 * Reçoit et traite les données
	 */
	@Override
	public void received(byte[] msg) {
		// TODO Auto-generated method stub
		if (msg!=null) {
			synchronized(System.out) { // On synchronise pour éviter que plusieurs threads puissent écrire en même temps.
				System.out.print("Server "+server.name+" read : ");
				for (int j = 0; j < msg.length; j++) { // On lit le message octet par octet.
					System.out.print((char)msg[j]);
				}
				System.out.println("");
			}
		}
		
		mq.close();
	}

	@Override
	public void sent(byte[] bytes, int offset, int length) {
		// TODO Auto-generated method stub
	}

	/**
	 * Permet de fermer une connexion existante.
	 * Relance l'acceptation d'une nouvelle connexion.
	 */
	@Override
	public void closed() {
		// TODO Auto-generated method stub
		server.mqs.remove(mq);
		synchronized(server) {
			server.nbOpen--;
			server.notify();
		}
	}

}
