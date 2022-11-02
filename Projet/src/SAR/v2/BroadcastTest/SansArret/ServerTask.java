package SAR.v2.BroadcastTest.SansArret;

import java.util.HashMap;

import SAR.v2.Implementation.*;

public class ServerTask extends Task{
	
	private HashMap<Integer, MessageQueue> serveurqueues = new HashMap<>();
	
	QueueBroker broker;
	int port;
	Manager manager;
	
	public ServerTask(String name, int port, Manager manager) {
		this.broker = new QueueBrokerImpl(name, manager);
		this.port = port;
		this.manager = manager;
	}
	
	public void run() {
		
		while(true) {
			this.manager.freeUnusedPorts(); // On supprime les connexions inactives.
			
			for (int i = 0; i< 10; i++) {
				if (i%2==0) { // Les Clients pairs sont ceux qui envoient des messages (les Senders). C'est également eux qui se connectent au Serveur.
					MessageQueue mq = null;
					try {
						mq = broker.accept(i); // On accepte la connexion à chacun des Senders.
					} catch(Exception e) {
					}
					serveurqueues.put(i, mq); // On ajoute la connexion à la liste des MessageQueues actives pour le serveur.
				}
			}
			for (int i = 0; i < 10; i++) {
				if (i%2==0) { // Les Clients pairs sont ceux qui envoient des messages (les Senders). C'est également eux qui se connectent au Serveur.
					byte[] message = null;
					try {
						message = serveurqueues.get(i).receive(); // On lit les messages envoyés.
						synchronized(System.out) { // On synchronise pour éviter que plusieurs threads puissent écrire en même temps.
							System.out.print("Serveur lit : ");
							for (int j = 0; j < message.length; j++) { // On lit le message octet par octet.
								System.out.print((char)message[j]);
							}
							System.out.println("");
						}
					} catch(Exception e) {
					}
					for (HashMap.Entry<Integer, MessageQueue> entry : serveurqueues.entrySet()) { // Pour chacun des autres MessageQueues, on envoie le message que l'on vient de recevoir.
						if (entry.getKey() != i) {
							try {
								entry.getValue().send(message, 0, message.length);
							} catch(Exception e) {
							}
						}
					}
				}
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (int i = 0; i < 10; i++) {
				if(i%2==0) { // Les Clients pairs sont ceux qui envoient des messages (les Senders). C'est également eux qui se connectent au Serveur.
					serveurqueues.get(i).close(); // On ferme la connexion.
					serveurqueues.remove(i); // On supprime la MessageQueue des connexions actives du serveur.
				}
			}
		}
	}

}
