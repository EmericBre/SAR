package SAR.v2.Implementation;

import java.util.HashMap;

public class QueueBrokerImpl extends QueueBroker {
	
	private HashMap<Integer, MessageQueueImpl> mqs; // Liste des MessageQueues du Broker actuel avec leur port.
	private BrokerImpl broker;
	private Manager manager;

	public QueueBrokerImpl(String name, Manager manager) {
		 super(name);
		 this.broker = new BrokerImpl(name, manager);
		 this.mqs = new HashMap<Integer, MessageQueueImpl>();
		 this.manager = manager;
		 this.manager.addBroker(name, this); // On ajoute ce nouveau Broker à la liste des Broker de l'application
	}
	 
	/**
	 * Méthode permettant d'accepter une connexion.
	 * Lorsque la channel est récupérée, on crée une MessageQueue à partir de celle-ci et on l'ajoute dans la liste (avec son port).
	 * On retourne la MessageQueue obtenue.
	 */
	public MessageQueueImpl accept(int port) {
		try {
			ChannelImpl channel = this.broker.accept(this, port);
			MessageQueueImpl mq = new MessageQueueImpl(channel);
			mqs.put(port, mq);
			return mq;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.out.println("Impossible to connect.");
			e.printStackTrace();
			return null;
		}
	}
	 
	/**
	 * Méthode permettant de se connecter à un autre Broker.
	 * Lorsque la channel est récupérée, on crée une MessageQueue à partir de celle-ci et on l'ajoute dans la liste (avec son port).
	 * On retourne la MessageQueue obtenue.
	 * Si une MessageQueue existe déjà pour le Broker et le port souhaité, on la retourne directement.
	 */
	public MessageQueueImpl connect(String name, int port) {
		try {
			if (mqs.containsKey(port)) {
				return mqs.get(port);
			}
			ChannelImpl channel = this.broker.connect(this, name, port);
			MessageQueueImpl mq = new MessageQueueImpl(channel);
			mqs.put(port, mq);
			return mq;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.out.println("Impossible to connect.");
			e.printStackTrace();
			return null;
		}
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
