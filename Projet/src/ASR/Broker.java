package ASR;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Broker {
	
	private String name;
	private List<Integer> waiting;
	private HashMap<Integer, Channel> open;
	
	/**
	 * Constructeur pour créer un objet "conteneur", qui gèrera les différents threads et fera la liaison entre eux.
	 * @param name
	 */
	Broker(String name) {
		this.name = name;
		this.waiting = new ArrayList<Integer>();
		this.open = new HashMap<Integer, Channel>();
		Main.BrokerManager.put(name, this);
	}
	
	/**
	 * Méthode permettant d'initialiser la connexion, à partir d'un port fourni. Cela permet de créer un nouveau Channel pour une connexion entre deux Tasks.
	 * Le Channel correspondant au port est retourné lorsque la connexion est acceptée.
	 * @param port
	 * @return
	 * @throws InterruptedException 
	 */
	synchronized Channel accept(int port) throws InterruptedException {
		if (this.open.containsKey(port) || this.waiting.contains(port)) {
			throw new InterruptedException("Port " + port + " is already in use (either open or waiting for a connection");
		}
		
		this.waiting.add(port);
		
		while(waiting.contains(port)) {
			wait();
		}
		
		Channel local = this.open.get(port);
		synchronized (local) {
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
		if(!Main.BrokerManager.containsKey(name)) {
			throw new InterruptedException("Impossible to initialize the connection : Broker " + name + " doesn't exist.");
		}
		
		Broker BrokerToConnect = Main.BrokerManager.get(name);
		
		Channel local = new Channel();
		Channel external = new Channel();
		local.connectTo(external);
		external.connectTo(local);
		
		if (BrokerToConnect.waitingStatus(port)) {
			BrokerToConnect.openConnect(port, external);
		}
		
		synchronized (BrokerToConnect) {
			BrokerToConnect.notifyAll();
		}
		
		this.open.put(port, local);
		
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
