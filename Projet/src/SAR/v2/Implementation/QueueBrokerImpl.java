package SAR.v2.Implementation;

import java.util.HashMap;

public class QueueBrokerImpl extends QueueBroker {
	
	private HashMap<Integer, MessageQueueImpl> mqs;
	private BrokerImpl broker;
	private Manager manager;

	public QueueBrokerImpl(String name, Manager manager) {
		 super(name);
		 this.broker = new BrokerImpl(name, manager);
		 this.mqs = new HashMap<Integer, MessageQueueImpl>();
		 this.manager = manager;
		 this.manager.addBroker(name, this); // On ajoute ce nouveau Broker à la liste des Broker de l'application
	}
	 
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
	
	public BrokerImpl getBroker() {
		return broker;
	}
	
	public HashMap<Integer, MessageQueueImpl> getMqs() {
		return mqs;
	}
	 
}
