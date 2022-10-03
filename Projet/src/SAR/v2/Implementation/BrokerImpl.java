package SAR.v2.Implementation;

import java.util.HashMap;

public class BrokerImpl extends Broker {
	
	private String name;
	private HashMap<Integer, ChannelImpl> waiting; // Liste permettant de stocker les ports en attente d'unne connexion acceptée
	private HashMap<Integer, ChannelImpl> open; // Liste stockant les ports ayant une connexion ouverte, et la Channel liée

	BrokerImpl(String name) {
		super(name);
		this.name = name;
		this.waiting = new HashMap<Integer, ChannelImpl>();
		this.open = new HashMap<Integer, ChannelImpl>();
		Manager.BrokerManager.put(name, this); // On ajoute ce nouveau Broker à la liste des Broker de l'application
	}
	
	/**
	 * Méthode permettant d'initialiser la connexion, à partir d'un port fourni. Cela permet de créer un nouveau Channel pour une connexion entre deux Tasks.
	 * Le Channel correspondant au port est retourné lorsque la connexion est acceptée.
	 * @param port
	 * @return
	 * @throws InterruptedException 
	 */
	ChannelImpl accept(int port) throws InterruptedException {
		if (this.open.containsKey(port)) { // On vérifie que le port ne soit pas déjà utilisé pour une autre connexion
			throw new InterruptedException("Port " + port + " is already in use (either open or waiting for a connection");
		}
				
		synchronized(this) {
			while(!waiting.containsKey(port)) { // Tant que personne n'a rejoint la connexion, la Task est en attente.
				wait();
			}
		}
		
		ChannelImpl local = this.waiting.get(port); // La connexion a été ajoutée dans la liste de celles ouvertes, on récup le channel correspondant.
		this.waiting.remove(port);
		synchronized (local) { // On notifie la Channel pour qu'elle puisse read/write
			local.notifyAll();
		}
		
		this.open.put(port, local);
		
		System.out.println("Broker " + this.name + " successfully accepted connection on port " + port + ".");
		
		return local;
	}
	
	/**
	 * Méthode permettant d'établir une connexion à un Channel déjà initialisée par le Broker courant. Il faut que le nom soit le même que celui du Broker, le port également.
	 * Le booléen disconnected est initialisé à false (pour Channel) car la connexion est initialisée.
	 * @param name
	 * @param port
	 * @return
	 * @throws InterruptedException 
	 */
	ChannelImpl connect(String name, int port) throws InterruptedException {
		while(!Manager.BrokerManager.containsKey(name)) { // Si le Broker demandé n'existe pas dans la liste des Broker, on retourne une erreur
			Thread.sleep(200);
		}
		
		BrokerImpl BrokerToConnect = (BrokerImpl) Manager.BrokerManager.get(name); // On récupère le Broker correspondant au nom 
		
		ChannelImpl local = new ChannelImpl(); // On crée deux channels, une pour la Task actuelle, et une pour la Task en ouverture de connexion
		ChannelImpl external = new ChannelImpl();
		local.connectTo(external); // On interconnecte les deux channels entre elles pour leur permettre de communiquer
		external.connectTo(local);
		
		BrokerToConnect.addConnexionRequest(port, external);
		
		synchronized (BrokerToConnect) { // On notifie le broker à distance que la connexion est ouverte
			BrokerToConnect.notifyAll();
		}
		
		while (BrokerToConnect.waitingStatus(port)) {
			synchronized(external) {
				external.wait();
			}
		}
		
		this.open.put(port, local); // On ajoute la connexion à ce broker.
		
		System.out.println("Broker " + this.name + " successfully connected on port " + port + ".");
		
		return local;
	}
	
	/**
	 * Méthode permettant de vérifier l'état courant de la connexion. 
	 * Si le port demandé est en attente de connexion, la fonction retourne true.
	 * @param port
	 * @return
	 */
	public boolean waitingStatus(int port) {
		return this.waiting.containsKey(port);
	}
	
	/**
	 * Permet d'ouvrir la connexion.
	 * Supprime le port de la liste des connexions en attente, et l'ajoute dans celle des connexions ouvertes, avec la channel correspondant au Broker courant.
	 * @param port
	 * @param external
	 */
	public void addConnexionRequest(int port, ChannelImpl external) {
		this.waiting.put(port, external);
	}

}
