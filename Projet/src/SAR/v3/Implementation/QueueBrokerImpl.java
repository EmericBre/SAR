package SAR.v3.Implementation;

import java.util.HashMap;

import SAR.v3.Event.*;

public class QueueBrokerImpl extends QueueBroker {
	
	private HashMap<Integer, MessageQueueImpl> mqs;
	private BrokerImpl broker;
	private Executor executor;
	private Manager manager;

	public QueueBrokerImpl(String name, Manager manager, Executor executor) {
		super(name);
		this.broker = new BrokerImpl(name, manager);
		this.mqs = new HashMap<Integer, MessageQueueImpl>();
		this.executor = executor;
		this.manager = manager;
		this.manager.addBroker(name, this); // On ajoute ce nouveau Broker à la liste des Broker de l'application
	}

	/**
	 * Méthode remplaçant le accept de la v2.
	 * Permet de créer un event pour commencer l'acceptation d'une connexion.
	 */
	@Override
	public boolean bind(int port, AcceptListener listener) {
		// TODO Auto-generated method stub
		executor.push(new BindEvent(executor, port, listener, this, manager));
		return true;
	}

	/**
	 * Méthode pour refuser la connexion entre deux threads.
	 */
	@Override
	public boolean unbind(int port) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Méthode remplaçant le connect de la v2.
	 * Permet de créer un event pour initialiser la connexion entre deux threads.
	 */
	@Override
	public boolean connect(String name, int port, ConnectListener listener) {
		// TODO Auto-generated method stub
		executor.push(new ConnectEvent(executor, name, port, listener, this));
		return true;
	}
	
	/**
	 * On retourne le Broker correspondant.
	 * @return
	 */
	public BrokerImpl getBroker() {
		return broker;
	}
	
	/**
	 * On retourne la liste des MessageQueues actives.
	 * @return
	 */
	public HashMap<Integer, MessageQueueImpl> getMqs() {
		return mqs;
	}
	 
}
