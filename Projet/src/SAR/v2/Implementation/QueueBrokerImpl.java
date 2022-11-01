package SAR.v2.Implementation;

import java.util.HashMap;

public class QueueBrokerImpl extends QueueBroker {
	
	private HashMap<Integer, MessageQueueImpl> mqs;
	private BrokerImpl broker;

	public QueueBrokerImpl(String name, Manager manager) {
		 super(name);
		 this.broker = new BrokerImpl(name, manager);
		 this.mqs = new HashMap<Integer, MessageQueueImpl>();
	}
	 
	public MessageQueueImpl accept(int port) {
		try {
			ChannelImpl channel = this.broker.accept(port);
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
			ChannelImpl channel = this.broker.connect(name, port);
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
	
	public void freeUnusedPorts(int port) {
		mqs.remove(port);
		this.broker.open.remove(port);
	}
	 
}
