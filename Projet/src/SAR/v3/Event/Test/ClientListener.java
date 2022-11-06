package SAR.v3.Event.Test;

import SAR.v3.Implementation.MessageQueue;
import SAR.v3.Implementation.MessageQueue.Listener;

public class ClientListener implements Listener{
	
	MessageQueue mq;
	Client client;
	int nbReceive;
	
	public ClientListener(MessageQueue mq, Client client) {
		this.mq = mq;
		this.client = client;
		this.nbReceive = 0;
	}

	/**
	 * Recoit et traite les données.
	 */
	@Override
	public void received(byte[] msg) {
		// TODO Auto-generated method stub
		if (msg!=null) {
			synchronized(System.out) { // On synchronise pour éviter que plusieurs threads puissent écrire en même temps.
				System.out.print("Client "+client.name+" read : ");
				for (int j = 0; j < msg.length; j++) { // On lit le message octet par octet.
					System.out.print((char)msg[j]);
				}
				System.out.println("");
			}
		}
		
	}

	@Override
	public void sent(byte[] bytes, int offset, int length) {
		// TODO Auto-generated method stub
	}

	/**
	 * Permet de fermer une connexion existante.
	 * Relance une nouvelle connexion.
	 */
	@Override
	public void closed() {
		// TODO Auto-generated method stub
		System.out.println("Client "+client.name+" : Connection closed");
		synchronized(client) {
			client.nbOpen--;
			client.notify();
		}
	}

}
