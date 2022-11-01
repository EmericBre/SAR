package SAR.v2.Implementation;

import java.util.HashMap;

public class BrokerImpl extends Broker {
	
	private String name;
	public HashMap<Integer, ChannelImpl> open; // Liste stockant les ports ayant une connexion ouverte, et la Channel liée
	private Manager manager;
	
	BrokerImpl(String name, Manager manager) {
		super(name);
		this.name = name;
		this.open = new HashMap<Integer, ChannelImpl>();
		this.manager = manager;
	}
	
	/**
	 * Méthode permettant d'initialiser la connexion, à partir d'un port fourni. Cela permet de créer un nouveau Channel pour une connexion entre deux Tasks.
	 * Le Channel correspondant au port est retourné lorsque la connexion est acceptée.
	 * @param port
	 * @return
	 * @throws InterruptedException 
	 */
	public synchronized ChannelImpl accept(QueueBroker broker, int port) throws InterruptedException {
		if (this.open.containsKey(port)) { // On vérifie que le port ne soit pas déjà utilisé pour une autre connexion
			throw new InterruptedException("Port " + port + " is already in use (either open or waiting for a connection");
		}
		
		ChannelImpl channelaccept = manager.accept((QueueBrokerImpl)broker, port);

		this.open.put(port, channelaccept);

		System.out.println("Broker " + this.name + " successfully accepted connection on port " + port + ".");

		return channelaccept;
	}
	
	/**
	 * Méthode permettant d'établir une connexion à un Channel déjà initialisée par le Broker courant. Il faut que le nom soit le même que celui du Broker, le port également.
	 * Le booléen disconnected est initialisé à false (pour Channel) car la connexion est initialisée.
	 * @param name
	 * @param port
	 * @return
	 * @throws InterruptedException 
	 */
	public synchronized ChannelImpl connect(QueueBroker broker, String name, int port) throws InterruptedException {
		
		ChannelImpl channelconnect = manager.connect((QueueBrokerImpl)broker, name, port);

		this.open.put(port, channelconnect); // On ajoute la connexion à ce broker.
		
		System.out.println("Broker " + this.name + " successfully connected on port " + port + ".");
		
		return channelconnect;
	}

}
