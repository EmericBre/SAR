package ASR;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Broker {
	
	private String name;
	private List<Integer> waiting; // Liste permettant de stocker les ports en attente d'unne connexion acceptée
	private HashMap<Integer, Channel> open; // Liste stockant les ports ayant une connexion ouverte, et la Channel liée
	
	/**
	 * Constructeur pour créer un objet "conteneur", qui gèrera les différents threads et fera la liaison entre eux.
	 * @param name
	 */
	Broker(String name) {
		this.name = name;
		this.waiting = new ArrayList<Integer>();
		this.open = new HashMap<Integer, Channel>();
		Main.BrokerManager.put(name, this); // On ajoute ce nouveau Broker à la liste des Broker de l'application
	}
	
	/**
	 * Méthode permettant d'initialiser la connexion, à partir d'un port fourni. Cela permet de créer un nouveau Channel pour une connexion entre deux Tasks.
	 * Le Channel correspondant au port est retourné lorsque la connexion est acceptée.
	 * @param port
	 * @return
	 * @throws InterruptedException 
	 */
	synchronized Channel accept(int port) throws InterruptedException {
		if (this.open.containsKey(port) || this.waiting.contains(port)) { // On vérifie que le port ne soit pas déjà utilisé pour une autre connexion
			throw new InterruptedException("Port " + port + " is already in use (either open or waiting for a connection");
		}
		
		this.waiting.add(port); // On ajoute à la liste des connexions en attente le port.
		
		while(waiting.contains(port)) { // Tant que personne n'a rejoint la connexion, la Task est en attente.
			wait();
		}
		
		Channel local = this.open.get(port); // La connexion a été ajoutée dans la liste de celles ouvertes, on récup le channel correspondant.
		synchronized (local) { // On notifie la Channel pour qu'elle puisse read/write
			local.notifyAll();
		}
		
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
	synchronized Channel connect(String name, int port) throws InterruptedException {
		while(!Main.BrokerManager.containsKey(name)) { // Si le Broker demandé n'existe pas dans la liste des Broker, on retourne une erreur
			Thread.sleep(200);
		}
		
		Broker BrokerToConnect = Main.BrokerManager.get(name); // On récupère le Broker correspondant au nom 
		
		Channel local = new Channel(); // On crée deux channels, une pour la Task actuelle, et une pour la Task en ouverture de connexion
		Channel external = new Channel();
		local.connectTo(external); // On interconnecte les deux channels entre elles pour leur permettre de communiquer
		external.connectTo(local);
		
		if (BrokerToConnect.waitingStatus(port)) { // Si la connexion est toujours en attente, on la supprime de cette liste et on l'ajoute à la liste des connexions ouvertes
			BrokerToConnect.openConnect(port, external);
		}
		
		synchronized (BrokerToConnect) { // On notifie le broker à distance que la connexion est ouverte
			BrokerToConnect.notifyAll();
		}
		
		this.open.put(port, local); // On ajoute la connexion à ce broker.
		
		System.out.println("Broker " + this.name + " successfully connected on port " + port + ".");
		
		return local;
	}
	
	public boolean waitingStatus(int port) {
		return this.waiting.contains(port);
	}
	
	public void openConnect(int port, Channel external) {
		this.open.put(port, external);
		this.waiting.remove(this.waiting.indexOf(port));
	}
}
