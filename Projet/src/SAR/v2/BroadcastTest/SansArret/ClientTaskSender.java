package SAR.v2.BroadcastTest.SansArret;

import java.util.HashMap;

import SAR.v2.Implementation.*;

public class ClientTaskSender extends Task {
	
	public static HashMap<String, MessageQueue> queues = new HashMap<>();

	String name;
	QueueBroker broker;
	String toConnect;
	int port;
	
	public ClientTaskSender(String name, String toConnect, int port, Manager manager) {
		this.name = name;
		this.toConnect = toConnect;
		this.broker = new QueueBrokerImpl(name, manager);
		this.port = port;
	}
	
	public void run() {
		MessageQueue mq = null;
		try {
			while (true) {
				mq = broker.connect("serveur", port); // On crée une nouvelle connexion
				
				synchronized(queues) { // On synchronise la queue pour qu'il n'y ai pas plusieurs ajouts en même temps.
					if (!queues.containsKey(toConnect)) { // Si la connexion n'est pas déjà existante, on l'ajoute dans la liste.
						queues.put(toConnect, mq);
						queues.notifyAll();
					}
				}
				
				byte[] b = name.getBytes();
				mq.send(b, 0, b.length);
				Thread.sleep(1);
				synchronized(queues) { // On supprime la connexion de la queue.
					queues.remove(toConnect);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return;
		}
	}
}
