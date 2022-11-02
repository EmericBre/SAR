package SAR.v2.Test.SansArret;

import SAR.v2.Implementation.Manager;
import SAR.v2.Implementation.MessageQueue;
import SAR.v2.Implementation.QueueBroker;
import SAR.v2.Implementation.QueueBrokerImpl;
import SAR.v2.Implementation.Task;

public class Server extends Task {
	
	QueueBroker broker;
	int port;
	Manager manager;
	String name;
	
	public Server(String name, int port, Manager manager) {
		this.broker = new QueueBrokerImpl(name, manager);
		this.port = port;
		this.manager = manager;
		this.name = name;
	}
	
	public void run() {
		while(true) {
			synchronized(this.manager) {
				this.manager.freeUnusedPorts(); // On supprime les connexions inactives.
			}
			MessageQueue mq;
			try {
				mq = broker.accept(port); // On accepte la connexion
				
				byte[] message = mq.receive();
				
				if (message != null) { // Si le message n'a pas été lu, on sort de la boucle.
					synchronized(System.out) { // On synchronise pour éviter que plusieurs threads puissent écrire en même temps.
						System.out.print("Serveur " + this.name + " lit : ");
						for (int j = 0; j < message.length; j++) { // On lit le message octet par octet.
							System.out.print((char)message[j]);
						}
						System.out.println("");
					}
				}
				
				mq.close(); // On ferme la connexion
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return;
			}
		}
	}

}
