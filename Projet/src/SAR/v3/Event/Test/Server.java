package SAR.v3.Event.Test;

import java.util.HashMap;

import SAR.v3.Event.Executor;
import SAR.v3.Implementation.Manager;
import SAR.v3.Implementation.MessageQueue;
import SAR.v3.Implementation.QueueBroker;
import SAR.v3.Implementation.QueueBrokerImpl;

public class Server extends Thread{

	String name;
	int serverPort;
	int nbClient;
	QueueBroker broker;
	HashMap<MessageQueue, Integer> mqs;
	Manager manager;
	public int nbOpen = 0;
	
	public Server(String name, int serverPort, int bufferCapacity, int nbClient, Executor executor, Manager manager) {
		this.name = name;
		this.serverPort = serverPort;
		this.nbClient = nbClient;
		this.broker = new QueueBrokerImpl(name, manager, executor);
		mqs = new HashMap<>();
		this.manager = manager;
	}
	
	public void run() {
		while(true) {
			for (int i = 0; i < nbClient; i++) {
				nbOpen++;
				broker.bind(i, new Accepter(this)); // Accepte une connexion et crée un "accepter" (listener)
			}
			synchronized(this) {
				while(nbOpen!=0) { // Vérifie que toutes les connexions soient fermées. Si elles ne le sont pas, on attend.
					try {
						wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			manager.freeUnusedPorts(); // Libère les connexions inutilisées.
		}
	}
}
