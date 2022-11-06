package SAR.v3.Event.Test;

import SAR.v3.Implementation.QueueBrokerImpl;
import SAR.v3.Event.Executor;
import SAR.v3.Implementation.Manager;
import SAR.v3.Implementation.QueueBroker;

public class Client extends Thread{

	String name;
	String serverName;
	int serverPort;
	QueueBroker broker;
	int nbClient;
	Manager manager;
	int nbOpen = 0;
	
	public Client(String name, String serverName, int serverPort, int bufferCapacity, Executor executor, int nbClient, Manager manager) {
		this.name = name;
		this.serverName = serverName;
		this.serverPort = serverPort;
		this.broker = new QueueBrokerImpl(name, manager, executor);
		this.nbClient = nbClient;
		this.manager = manager;
	}
	
	public void run() {
		while (true) {
			broker.connect(serverName, serverPort,  new Connecter(this)); // Demande une connexion et crée un "connecter" (listener)
			nbOpen++;
			synchronized(this) {
				while (nbOpen != 0) { // Attend que toutes les connexions soient fermées pour passer au tour de boucle suivant.
					try {
						wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
}
